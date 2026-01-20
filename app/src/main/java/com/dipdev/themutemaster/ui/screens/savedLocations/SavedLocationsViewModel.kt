package com.dipdev.themutemaster.ui.screens.savedLocations


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipdev.themutemaster.data.GeofenceManager
import com.dipdev.themutemaster.data.local.GeofenceDao
import com.dipdev.themutemaster.data.local.GeofenceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedLocationsViewModel @Inject constructor(
    private val dao: GeofenceDao,
    private val geofenceManager: GeofenceManager
) : ViewModel() {

    // Hot Flow: Automatically updates the UI whenever the DB changes
    val locations = dao.getAllGeofences()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun toggleLocation(geofence: GeofenceEntity, isEnabled: Boolean) {
        viewModelScope.launch {
            val updated = geofence.copy(isEnabled = isEnabled)
            dao.insertGeofence(updated)

            if (isEnabled) {
                geofenceManager.addGeofence(updated)
            } else {
                geofenceManager.removeGeofence(updated)
            }
        }
    }

    fun deleteLocation(geofence: GeofenceEntity) {
        viewModelScope.launch {
            dao.deleteGeofence(geofence)
            geofenceManager.removeGeofence(geofence)
        }
    }
}