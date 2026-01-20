package com.dipdev.themutemaster.data

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.media.AudioManager
import android.util.Log
import com.dipdev.themutemaster.data.local.GeofenceEntity
import com.dipdev.themutemaster.receiver.GeofenceBroadcastReceiver
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeofenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val client = LocationServices.getGeofencingClient(context)
    // 1. We need this to check where we are right now
    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    fun addGeofence(entity: GeofenceEntity) {
        if (!entity.isEnabled) return

        val geofence = Geofence.Builder()
            .setRequestId(entity.id.toString())
            .setCircularRegion(entity.latitude, entity.longitude, entity.radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()

        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        client.addGeofences(request, geofencePendingIntent).run {
            addOnSuccessListener { Log.d("GeofenceManager", "Added Geofence: ${entity.id}") }
            addOnFailureListener { Log.e("GeofenceManager", "Failed to add: ${it.message}") }
        }
    }

    /**
     * UPDATED: Accepts the full Entity so we know WHERE it was.
     */
    @SuppressLint("MissingPermission") // Assumes location perm is granted
    fun removeGeofence(entity: GeofenceEntity) {
        // 1. Remove from System (Stop tracking)
        client.removeGeofences(listOf(entity.id.toString())).addOnSuccessListener {
            Log.d("GeofenceManager", "Removed Geofence: ${entity.id}")

            // 2. CHECK: Are we currently standing inside this deleted zone?
            locationClient.lastLocation.addOnSuccessListener { currentLocation ->
                if (currentLocation != null) {
                    val distance = FloatArray(1)
                    Location.distanceBetween(
                        currentLocation.latitude,
                        currentLocation.longitude,
                        entity.latitude,
                        entity.longitude,
                        distance
                    )

                    // distance[0] is the distance in meters
                    // If distance < radius, we are physically inside the zone we just deleted.
                    if (distance[0] <= entity.radius) {
                        Log.d("GeofenceManager", "User was inside deleted zone. Unmuting.")
                        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                    }
                }
            }
        }.addOnFailureListener {
            Log.e("GeofenceManager", "Failed to remove: ${it.message}")
        }
    }
}