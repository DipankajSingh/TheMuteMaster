package com.dipdev.themutemaster.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipdev.themutemaster.data.GeofenceManager
import com.dipdev.themutemaster.data.LocationClient
import com.dipdev.themutemaster.data.local.GeofenceDao
import com.dipdev.themutemaster.data.local.GeofenceEntity
import com.dipdev.themutemaster.data.local.PreferencesManager // Import this
import com.dipdev.themutemaster.utils.GeofenceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val locationText: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isLocationSaved: Boolean = false,
    val isLocationMuted: Boolean = false,
    val locationId: String = "",
    val currentLatitude: Double? = null,
    val currentLongitude: Double? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationClient: LocationClient,
    private val dao: GeofenceDao,
    private val geofenceManager: GeofenceManager,
    private val preferencesManager: PreferencesManager // 1. Inject PreferencesManager

) : ViewModel() {

    // --- UI STATE ---
    var uiState by mutableStateOf(HomeUiState())
        private set

    private var lastFetchTime: Long = 0
    private val CACHE_TIMEOUT = 30 * 1000L

    init {
        viewModelScope.launch {
            dao.getAllGeofences().collect { updatedList ->
                val lat = uiState.currentLatitude
                val lng = uiState.currentLongitude

                if (lat != null && lng != null) {
                    val duplicate = GeofenceUtils.findOverlappingGeofence(lat, lng, updatedList)

                    if (duplicate != null) {
                        val newLocationText = if (duplicate.fullAddress != null && duplicate.fullAddress != uiState.locationText) {
                            duplicate.fullAddress
                        } else {
                            uiState.locationText
                        }
                        uiState = uiState.copy(
                            isLocationSaved = true,
                            isLocationMuted = duplicate.isEnabled,
                            locationText = newLocationText
                        )
                    } else {
                        if (uiState.isLocationSaved) {
                            uiState = uiState.copy(
                                isLocationSaved = false,
                                isLocationMuted = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun fetchLocation(forceRefresh: Boolean = false) {
        val currentTime = System.currentTimeMillis()
        if (!forceRefresh && uiState.locationText != null && (currentTime - lastFetchTime < CACHE_TIMEOUT)) {
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, isError = false)

            try {
                delay(500) // Reduced to 500ms (5000ms is too long for users!)

                val location = locationClient.getCurrentLocation()

                if (location != null) {
                    lastFetchTime = System.currentTimeMillis()
                    uiState = uiState.copy(
                        currentLatitude = location.latitude,
                        currentLongitude = location.longitude
                    )

                    val duplicate = GeofenceUtils.findOverlappingGeofence(
                        location.latitude,
                        location.longitude,
                        dao.getAllGeofencesOneShot()
                    )

                    if (duplicate != null) {
                        uiState = uiState.copy(
                            isLocationMuted = duplicate.isEnabled,
                            isLocationSaved = true,
                            locationText = duplicate.fullAddress ?: "Unknown Address",
                            locationId = duplicate.id.toString(),
                            isError = false
                        )
                    } else {
                        uiState = uiState.copy(
                            isLocationSaved = false,
                            isLocationMuted = false,
                            locationText = locationClient.getAddressFromCoordinates(
                                location.latitude,
                                location.longitude
                            ),
                            isError = false
                        )
                    }
                } else {
                    uiState = uiState.copy(
                        isError = true,
                        locationText = "Unable to get location. Is GPS on?"
                    )
                }
            } catch (e: SecurityException) {
                uiState = uiState.copy(
                    isError = true,
                    locationText = "Location permission missing."
                )
            } catch (e: LocationClient.LocationException) {
                uiState = uiState.copy(
                    isError = true,
                    locationText = e.message ?: "Location error"
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isError = true,
                    locationText = "Error: ${e.localizedMessage ?: "Unknown error"}"
                )
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    fun onPermissionDenied() {
        uiState = uiState.copy(locationText = "Permission denied. Please allow access in settings.")
    }

    fun saveLocation() {
        val lat = uiState.currentLatitude ?: return
        val lng = uiState.currentLongitude ?: return
        val locText = uiState.locationText

        val addressToSave = when {
            locText.isNullOrBlank() -> "Unknown Address"
            locText == "Locating..." -> "Unknown Address"
            locText.startsWith("Error") -> "Unknown Address"
            else -> locText
        }

        viewModelScope.launch {
            try {
                // 2. Get Default Radius from DataStore (Async)
                // .first() grabs the current value and cancels the collection immediately
                val preferredRadius = preferencesManager.defaultRadiusFlow.first()

                val entity = GeofenceEntity(
                    latitude = lat,
                    longitude = lng,
                    fullAddress = addressToSave,
                    isEnabled = true,
                    radius = preferredRadius // 3. Use the value
                )

                val newId = dao.insertGeofence(entity).toInt()
                val finalEntity = entity.copy(id = newId)

                geofenceManager.addGeofence(finalEntity)

                uiState = uiState.copy(
                    locationId = newId.toString(),
                    isLocationSaved = true,
                    isLocationMuted = true,
                    locationText = addressToSave
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleMute() {
        val locationId = uiState.locationId
        if (locationId.isBlank()) return

        viewModelScope.launch {
            try {
                val id = locationId.toIntOrNull() ?: return@launch
                val geofence = dao.getGeofenceById(id) ?: return@launch
                val updated = geofence.copy(isEnabled = !geofence.isEnabled)
                dao.insertGeofence(updated) // insertGeofence with REPLACE handles updates

                if (updated.isEnabled) {
                    geofenceManager.addGeofence(updated)
                } else {
                    geofenceManager.removeGeofence(updated)
                }

                uiState = uiState.copy(isLocationMuted = updated.isEnabled)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}