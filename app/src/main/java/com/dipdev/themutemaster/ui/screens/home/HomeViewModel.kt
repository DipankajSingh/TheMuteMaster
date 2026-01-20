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
import com.dipdev.themutemaster.utils.GeofenceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val locationClient: LocationClient,
    private val dao: GeofenceDao,
    private val geofenceManager: GeofenceManager

) : ViewModel() {

    // --- UI STATE ---
    var locationText by mutableStateOf<String?>("Locating...")
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
            // distinctUntilChanged ensures we don't re-run logic if the list looks identical
            dao.getAllGeofences().collect { updatedList ->

                val lat = currentLatitude
                val lng = currentLongitude

                // Only run logic if we actually know where we are
                if (lat != null && lng != null) {

                    // Fast Calculation (Memory only)
                    val duplicate = GeofenceUtils.findOverlappingGeofence(lat, lng, updatedList)

                    if (duplicate != null) {
                        // It is saved!
                        isLocationSaved = true
                        isLocationMuted = duplicate.isEnabled

                        // OPTIONAL: Only update text if it looks different to avoid flickering.
                        // If the user renamed it to "Home", we want to show "Home", not "123 St".
                        if (duplicate.fullAddress != null && duplicate.fullAddress != locationText) {
                            locationText = duplicate.fullAddress
                        }
                    } else {
                        // It is NOT saved (or was deleted)
                        // If it WAS saved before, update the UI state.
                        if (isLocationSaved) {
                            isLocationSaved = false
                            isLocationMuted = false
                            // We DO NOT touch locationText here.
                            // Keep the address visible so the user can click "Save" again immediately.
                        }
                    }
                }
            }
        }
    }

    fun fetchLocation(forceRefresh: Boolean=false) {
        val currentTime = System.currentTimeMillis()
        if (!forceRefresh && locationText != null && (currentTime - lastFetchTime < CACHE_TIMEOUT)) {
            return
        }
        viewModelScope.launch {
            try {
                lastFetchTime = System.currentTimeMillis()
                locationText = "Locating..."
                val location = locationClient.getCurrentLocation()

                if (location != null) {
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
                } else {
                    locationText = "Unable to get location. Is GPS on?"
                }
            } catch (e: SecurityException) {
                locationText = "Location permission missing."
            } catch (e: Exception) {
                locationText = "Error: ${e.localizedMessage ?: "Unknown error"}"
            }
        }
    }

    fun onPermissionDenied() {
        locationText = "Permission denied. Please allow access in settings."
    }

    fun saveLocation() {
        val lat = currentLatitude ?: return
        val lng = currentLongitude ?: return

        // 1. INPUT VALIDATION: Stop "Locating..." from being saved as an address
        val addressToSave = when {
            locationText.isNullOrBlank() -> "Unknown Address"
            locationText == "Locating..." -> "Unknown Address"
            locationText!!.startsWith("Error") -> "Unknown Address"
            else -> locationText!!
        }

        viewModelScope.launch {
            try {
                // 2. CREATE ENTITY ONCE
                val entity = GeofenceEntity(
                    latitude = lat,
                    longitude = lng,
                    fullAddress = addressToSave,
                    isEnabled = true
                )

                // 3. INSERT & GET ID (Long -> Int)
                val newId = dao.insertGeofence(entity).toInt()

                // 4. OPTIMIZATION: Copy the ID into the entity (No DB Read required)
                val finalEntity = entity.copy(id = newId)

                // 5. REGISTER GEOFENCE
                // Now we pass the complete object directly to the manager
                geofenceManager.addGeofence(finalEntity)

                // 6. UPDATE UI
                locationId = newId.toString()
                isLocationSaved = true
                isLocationMuted = true

                // Optional: Update text to the sanitized version immediately
                locationText = addressToSave

            } catch (e: Exception) {
                // CRITICAL: Catch DB or Geofence errors
                e.printStackTrace()
                // Ideally, emit a "Save Failed" event to the UI here
            }
        }
    }
}