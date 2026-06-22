package com.dipdev.themutemaster.data

import com.dipdev.themutemaster.data.local.GeofenceEntity

interface GeofenceManager {
    fun addGeofence(entity: GeofenceEntity): Boolean
    fun removeGeofence(entity: GeofenceEntity)
}
