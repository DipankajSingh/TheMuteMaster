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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationClient: LocationClient,
    private val dao: GeofenceDao,
    private val geofenceManager: GeofenceManager,
    private val preferencesManager: PreferencesManager // 1. Inject PreferencesManager

) : ViewModel() {

    // --- UI STATE ---
    var locationText by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isError by mutableStateOf(false)
        private set

    var isLocationSaved by mutableStateOf(false)
        private set

    var isLocationMuted by mutableStateOf(false)
        private set

    var locationId by mutableStateOf("")
        private set

    var currentLatitude by mutableStateOf<Double?>(null)
        private set
    var currentLongitude by mutableStateOf<Double?>(null)
        private set

    private var lastFetchTime: Long = 0
    private val CACHE_TIMEOUT = 60 * 3000L

    init {
        viewModelScope.launch {
            dao.getAllGeofences().collect { updatedList ->
                val lat = currentLatitude
                val lng = currentLongitude

                if (lat != null && lng != null) {
                    val duplicate = GeofenceUtils.findOverlappingGeofence(lat, lng, updatedList)

                    if (duplicate != null) {
                        isLocationSaved = true
                        isLocationMuted = duplicate.isEnabled
                        if (duplicate.fullAddress != null && duplicate.fullAddress != locationText) {
                            locationText = duplicate.fullAddress
                        }
                    } else {
                        if (isLocationSaved) {
                            isLocationSaved = false
                            isLocationMuted = false
                        }
                    }
                }
            }
        }
    }

    fun fetchLocation(forceRefresh: Boolean = false) {
        val currentTime = System.currentTimeMillis()
        if (!forceRefresh && locationText != null && (currentTime - lastFetchTime < CACHE_TIMEOUT)) {
            return
        }

        viewModelScope.launch {
            isLoading = true
            isError = false

            try {
                delay(500) // Reduced to 500ms (5000ms is too long for users!)

                val location = locationClient.getCurrentLocation()

                if (location != null) {
                    lastFetchTime = System.currentTimeMillis()
                    currentLatitude = location.latitude
                    currentLongitude = location.longitude

                    val duplicate = GeofenceUtils.findOverlappingGeofence(
                        location.latitude,
                        location.longitude,
                        dao.getAllGeofencesOneShot()
                    )

                    if (duplicate != null) {
                        isLocationMuted = duplicate.isEnabled
                        isLocationSaved = true
                        locationText = duplicate.fullAddress ?: "Unknown Address"
                        locationId = duplicate.id.toString()
                    } else {
                        isLocationSaved = false
                        isLocationMuted = false
                        locationText = locationClient.getAddressFromCoordinates(
                            location.latitude,
                            location.longitude
                        )
                    }
                    isError = false
                } else {
                    isError = true
                    locationText = "Unable to get location. Is GPS on?"
                }
            } catch (e: SecurityException) {
                isError = true
                locationText = "Location permission missing."
            } catch (e: Exception) {
                isError = true
                locationText = "Error: ${e.localizedMessage ?: "Unknown error"}"
            } finally {
                isLoading = false
            }
        }
    }

    fun onPermissionDenied() {
        locationText = "Permission denied. Please allow access in settings."
    }

    fun saveLocation() {
        val lat = currentLatitude ?: return
        val lng = currentLongitude ?: return

        val addressToSave = when {
            locationText.isNullOrBlank() -> "Unknown Address"
            locationText == "Locating..." -> "Unknown Address"
            locationText!!.startsWith("Error") -> "Unknown Address"
            else -> locationText!!
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

                locationId = newId.toString()
                isLocationSaved = true
                isLocationMuted = true
                locationText = addressToSave

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}