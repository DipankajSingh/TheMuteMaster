package com.dipdev.themutemaster.ui.screens.manageLocation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dipdev.themutemaster.data.local.GeofenceDao
import com.dipdev.themutemaster.data.local.GeofenceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageLocationViewModel @Inject constructor(
    private val dao: GeofenceDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var selectedProfile by  mutableStateOf("Vibrate")

    var locationName by mutableStateOf("")
    var radius by mutableFloatStateOf(100f)
    var isMutingEnabled by mutableStateOf(true)

    var locationAddress by mutableStateOf("Loading...")
        private set // Private setter so UI can't accidentally change it

    private var currentId: Int = 0
    private var currentLat: Double = 0.0
    private var currentLng: Double = 0.0

    init {
        val idString = savedStateHandle.get<String>("id")

        if (idString != null && idString != "null") {
            loadExistingLocation(idString.toInt())
        } else {
            locationAddress = "How did we get here??"
        }
    }

    private fun loadExistingLocation(id: Int) {
        viewModelScope.launch {
            val found = dao.getGeofenceById(id)

            if (found != null) {
                currentId = found.id ?: 0
                currentLat = found.latitude
                currentLng = found.longitude

                locationAddress = found.fullAddress ?: "Unknown Address"

                radius = found.radius
                isMutingEnabled = found.isEnabled
                locationName = found.name
            }
        }
    }

    fun saveChanges() {
        if (currentId == 0) return

        viewModelScope.launch {
            dao.insertGeofence(
                GeofenceEntity(
                    id = currentId,
                    latitude = currentLat,
                    longitude = currentLng,
                    fullAddress = locationAddress,
                    radius = radius,
                    isEnabled = isMutingEnabled,
                    name = locationName

                )
            )
        }
    }

    fun deleteLocation() {
        if (currentId == 0) return

        viewModelScope.launch {
            val toDelete = GeofenceEntity(
                id = currentId,
                latitude = 0.0,
                longitude = 0.0,
                fullAddress = ""
            )
            dao.deleteGeofence(toDelete)
        }
    }
}