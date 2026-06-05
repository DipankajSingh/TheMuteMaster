package com.dipdev.themutemaster.data

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.media.AudioManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
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
    @ApplicationContext private val context: Context,
    private val muteStateManager: com.dipdev.themutemaster.data.local.MuteStateManager
) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val client = LocationServices.getGeofencingClient(context)
    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        // FLAG_MUTABLE is required for Android 12+ (S) compatibility
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        PendingIntent.getBroadcast(context, 0, intent, flags)
    }

    /**
     * Safely adds a geofence. Returns true if request was sent successfully.
     */
    fun addGeofence(entity: GeofenceEntity): Boolean {
        if (!hasLocationPermission()) {
            Log.e("GeofenceManager", "Cannot add geofence: Location permission missing.")
            return false
        }

        if (!entity.isEnabled) return false

        try {
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

            // SECURITY: The try-catch handles the case where perm is revoked *during* execution
            client.addGeofences(request, geofencePendingIntent).run {
                addOnSuccessListener { Log.d("GeofenceManager", "Added Geofence: ${entity.id}") }
                addOnFailureListener { Log.e("GeofenceManager", "Failed to add: ${it.message}") }
            }
            return true
        } catch (e: SecurityException) {
            Log.e("GeofenceManager", "Security Exception adding geofence: ${e.message}")
            return false
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Unknown error adding geofence: ${e.message}")
            return false
        }
    }

    /**
     * Removes a geofence and checks if we need to unmute.
     */
    fun removeGeofence(entity: GeofenceEntity) {
        // Removing geofences technically doesn't require FINE_LOCATION in all API levels,
        // but checking "lastLocation" DOES. So we check safely.

        try {
            // 1. Remove from System (Stop tracking)
            client.removeGeofences(listOf(entity.id.toString())).addOnSuccessListener {
                Log.d("GeofenceManager", "Removed Geofence: ${entity.id}")

                // Only attempt to unmute if we have permission to check location
                if (hasLocationPermission()) {
                    checkIfUserIsInsideDeletedZone(entity)
                } else {
                    Log.w("GeofenceManager", "Geofence removed, but skipped location check (Permission missing).")
                }
            }.addOnFailureListener {
                Log.e("GeofenceManager", "Failed to remove: ${it.message}")
            }
        } catch (e: Exception) {
            Log.e("GeofenceManager", "Error removing geofence: ${e.message}")
        }
    }

    private fun checkIfUserIsInsideDeletedZone(entity: GeofenceEntity) {
        try {
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

                    // distance[0] is in meters. If < radius, we are still inside.
                    if (distance[0] <= entity.radius) {
                        Log.d("GeofenceManager", "User was inside deleted zone. Unmuting.")
                        safeUnmute(entity.id)
                    }
                } else {
                    Log.w("GeofenceManager", "Last location was null. Cannot verify if user is inside zone.")
                    // Optional Suggestion: You COULD force unmute here just to be safe.
                    // safeUnmute(entity.id)
                }
            }.addOnFailureListener {
                Log.e("GeofenceManager", "Failed to get last location: ${it.message}")
            }
        } catch (e: SecurityException) {
            Log.e("GeofenceManager", "Location permission revoked during check.")
        }
    }

    private fun safeUnmute(entityId: Int) {
        val wasRestored = muteStateManager.attemptRestore("GEOFENCE_$entityId")
        if (wasRestored) {
            val intent = Intent(context, com.dipdev.themutemaster.service.MuteService::class.java)
            context.stopService(intent)
        }
    }

    private fun hasLocationPermission(): Boolean {
        val hasFine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasBackground = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        return hasFine && hasBackground
    }
}