package com.dipdev.themutemaster.ui.screens.manageLocation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipdev.themutemaster.data.GeofenceManager
import com.dipdev.themutemaster.data.local.GeofenceDao
import com.dipdev.themutemaster.data.local.GeofenceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageLocationViewModel @Inject constructor(
    private val dao: GeofenceDao,
    savedStateHandle: SavedStateHandle,
    private val geofenceManager: GeofenceManager,
) : ViewModel() {

    // ... (UI State Variables remain the same) ...
    var selectedProfile by  mutableStateOf("Vibrate")
    var locationName by mutableStateOf("")
    var radius by mutableFloatStateOf(100f)
    var isMutingEnabled by mutableStateOf(true)
    var locationAddress by mutableStateOf("Loading...")
        private set

    // Internal tracker for the object
    private var currentEntity: GeofenceEntity? = null

    init {
        try {
            val idString = savedStateHandle.get<String>("id")
            if (idString != null && idString != "null") {
                loadExistingLocation(idString.toInt())
            } else {
                locationAddress = "How did we get here??"
            }
        } catch (e: Exception) {
            println("Error parsing ID: ${e.message}")
        }
    }

    private fun loadExistingLocation(id: Int) {
        viewModelScope.launch {
            val found = dao.getGeofenceById(id)
            if (found != null) {
                currentEntity = found // Store the full object for later use

                locationAddress = found.fullAddress ?: "Unknown Address"
                radius = found.radius
                isMutingEnabled = found.isEnabled
                locationName = found.name
            }
        }
    }

    fun saveChanges() {
        val existing = currentEntity ?: return // Safety check

        viewModelScope.launch {
            // Create updated entity based on UI inputs
            val updatedEntity = existing.copy(
                name = locationName,
                radius = radius,
                isEnabled = isMutingEnabled
                // Address/Lat/Lng stay the same as original
            )

            // 1. Save to DB
            dao.insertGeofence(updatedEntity)

            // Update local tracker
            currentEntity = updatedEntity

            // 2. Update System Geofence
            if (isMutingEnabled) {
                geofenceManager.addGeofence(updatedEntity)
            } else {
                // If disabled, remove from system (Pass FULL entity for safety check)
                geofenceManager.removeGeofence(updatedEntity)
            }
        }
    }

    fun deleteLocation() {
        val entityToDelete = currentEntity ?: return

        viewModelScope.launch {
            // 1. Remove from System FIRST (Needs Lat/Lng to check if we are inside)
            geofenceManager.removeGeofence(entityToDelete)

            // 2. Remove from Database
            dao.deleteGeofenceById(entityToDelete.id)
        }
    }
}