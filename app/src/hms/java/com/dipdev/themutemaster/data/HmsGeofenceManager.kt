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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.dipdev.themutemaster.data.local.GeofenceEntity
import com.dipdev.themutemaster.receiver.GeofenceBroadcastReceiver
import com.huawei.hms.location.Geofence
import com.huawei.hms.location.GeofenceRequest
import com.huawei.hms.location.LocationServices
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HmsGeofenceManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val muteStateManager: com.dipdev.themutemaster.data.local.MuteStateManager
) : GeofenceManager {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val client = LocationServices.getGeofenceService(context)
    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        PendingIntent.getBroadcast(context, 0, intent, flags)
    }

    override fun addGeofence(entity: GeofenceEntity): Boolean {
        if (!hasLocationPermission()) {
            Log.e("HmsGeofenceManager", "Cannot add geofence: Location permission missing.")
            return false
        }

        if (!entity.isEnabled) return false

        try {
            val geofence = Geofence.Builder()
                .setUniqueId(entity.id.toString())
                .setRoundArea(entity.latitude, entity.longitude, entity.radius)
                .setValidContinueTime(Geofence.GEOFENCE_NEVER_EXPIRE)
                .setConversions(Geofence.ENTER_GEOFENCE_CONVERSION or Geofence.EXIT_GEOFENCE_CONVERSION)
                .build()

            val request = GeofenceRequest.Builder()
                .setInitConversions(GeofenceRequest.ENTER_INIT_CONVERSION)
                .createGeofence(geofence)
                .build()

            client.createGeofenceList(request, geofencePendingIntent).run {
                addOnSuccessListener { Log.d("HmsGeofenceManager", "Added Geofence: ${entity.id}") }
                addOnFailureListener { Log.e("HmsGeofenceManager", "Failed to add: ${it.message}") }
            }
            return true
        } catch (e: SecurityException) {
            Log.e("HmsGeofenceManager", "Security Exception adding geofence: ${e.message}")
            return false
        } catch (e: Exception) {
            Log.e("HmsGeofenceManager", "Unknown error adding geofence: ${e.message}")
            return false
        }
    }

    override fun removeGeofence(entity: GeofenceEntity) {
        try {
            client.deleteGeofenceList(listOf(entity.id.toString())).addOnSuccessListener {
                Log.d("HmsGeofenceManager", "Removed Geofence: ${entity.id}")

                if (hasLocationPermission()) {
                    checkIfUserIsInsideDeletedZone(entity)
                } else {
                    Log.w("HmsGeofenceManager", "Geofence removed, but skipped location check (Permission missing).")
                }
            }.addOnFailureListener {
                Log.e("HmsGeofenceManager", "Failed to remove: ${it.message}")
            }
        } catch (e: Exception) {
            Log.e("HmsGeofenceManager", "Error removing geofence: ${e.message}")
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

                    if (distance[0] <= entity.radius) {
                        Log.d("HmsGeofenceManager", "User was inside deleted zone. Unmuting.")
                        safeUnmute(entity.id)
                    }
                }
            }.addOnFailureListener {
                Log.e("HmsGeofenceManager", "Failed to get last location: ${it.message}")
            }
        } catch (e: SecurityException) {
            Log.e("HmsGeofenceManager", "Location permission revoked during check.")
        }
    }

    private fun safeUnmute(entityId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val wasRestored = muteStateManager.attemptRestore("GEOFENCE_$entityId")
            if (wasRestored) {
                val intent = Intent(context, com.dipdev.themutemaster.service.MuteService::class.java)
                context.stopService(intent)
            }
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
