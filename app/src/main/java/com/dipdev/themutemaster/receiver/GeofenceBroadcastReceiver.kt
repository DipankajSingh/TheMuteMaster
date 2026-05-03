package com.dipdev.themutemaster.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dipdev.themutemaster.R
import com.dipdev.themutemaster.data.local.MuteStateManager
import com.dipdev.themutemaster.utils.NotificationConstants
import com.dipdev.themutemaster.utils.hasNotificationPermission
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    @Inject lateinit var muteStateManager: MuteStateManager

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return
        if (geofencingEvent.hasError()) {
            Log.e("GeofenceReceiver", "Error Code: ${geofencingEvent.errorCode}")
            return
        }

        when (geofencingEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                val wasMuted = muteStateManager.attemptMute()
                if (wasMuted) {
                    startMuteService(context)
                } else {
                    Log.d("GeofenceReceiver", "Entered zone, but phone was already silent.")
                }
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                val wasRestored = muteStateManager.attemptRestore()
                if (wasRestored) {
                    stopMuteService(context)
                }
            }
            else -> Log.e("GeofenceReceiver", "Unknown transition: ${geofencingEvent.geofenceTransition}")
        }
    }

    private fun startMuteService(context: Context) {
        try {
            val intent = Intent(context, com.dipdev.themutemaster.service.MuteService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        } catch (e: Exception) {
            Log.e("GeofenceReceiver", "Failed to start MuteService: ${e.message}")
        }
    }

    private fun stopMuteService(context: Context) {
        val intent = Intent(context, com.dipdev.themutemaster.service.MuteService::class.java)
        context.stopService(intent)
    }
}