package com.dipdev.themutemaster.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "geofences")
data class GeofenceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val latitude: Double,
    val longitude: Double,
    val radius: Float = 300f,
    val isEnabled: Boolean = true,
    val fullAddress: String? = null,
    val name: String = "Saved Address"
)