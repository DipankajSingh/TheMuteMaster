package com.dipdev.themutemaster.utils

import android.location.Location
import com.dipdev.themutemaster.data.local.GeofenceEntity

object GeofenceUtils {

    /**
     * Checks if the user's current coordinates are inside (or very close to)
     * any existing saved geofence.
     *
     * @param currentLat User's current Latitude
     * @param currentLng User's current Longitude
     * @param savedList The list of all saved geofences from Room
     * @param checkRadius Usually the radius of the saved fence (e.g., 100m).
     * If distance < radius, they are "inside" it.
     * @return The GeofenceEntity that overlaps, or null if none found.
     */
    fun findOverlappingGeofence(
        currentLat: Double,
        currentLng: Double,
        savedList: List<GeofenceEntity>
    ): GeofenceEntity? {
        // Optimization: Create this array once to avoid garbage collection churn
        val results = FloatArray(1)

        for (geofence in savedList) {
            // NATIVE ANDROID MATH (Fastest/Most Accurate)
            // Calculates distance in METERS between two points
            Location.distanceBetween(
                currentLat,
                currentLng,
                geofence.latitude,
                geofence.longitude,
                results
            )

            val distanceInMeters = results[0]

            // LOGIC: Are we closer than the radius?
            // If the geofence is 100m wide, and we are 40m away, we are INSIDE it.
            if (distanceInMeters < geofence.radius) {
                return geofence
            }
        }
        return null
    }
}