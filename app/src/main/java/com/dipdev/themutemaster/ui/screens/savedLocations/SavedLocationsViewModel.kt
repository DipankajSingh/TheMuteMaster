package com.dipdev.themutemaster.ui.screens.savedLocations


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipdev.themutemaster.data.local.GeofenceDao
import com.dipdev.themutemaster.data.local.GeofenceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedLocationsViewModel @Inject constructor(
    private val dao: GeofenceDao
) : ViewModel() {

    // Hot Flow: Automatically updates the UI whenever the DB changes
    val locations = dao.getAllGeofences()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteLocation(geofence: GeofenceEntity) {
        viewModelScope.launch {
            dao.deleteGeofence(geofence)
        }
    }

    fun toggleLocation(geofence: GeofenceEntity, isEnabled: Boolean) {
        viewModelScope.launch {
            // We copy the object with the new status and update it
            dao.insertGeofence(geofence.copy(isEnabled = isEnabled))
        }
    }
}