package com.dipdev.themutemaster.receiver

import kotlinx.coroutines.launch
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
import com.dipdev.themutemaster.utils.CrashReporter
import com.dipdev.themutemaster.utils.NotificationConstants
import com.dipdev.themutemaster.utils.hasNotificationPermission
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GeofenceBroadcastReceiver : BroadcastReceiver() {

    @Inject lateinit var muteStateManager: MuteStateManager
    @Inject lateinit var crashReporter: CrashReporter

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return
        if (geofencingEvent.hasError()) {
            val errorCode = geofencingEvent.errorCode
            Log.e("GeofenceReceiver", "Error Code: $errorCode")
            crashReporter.log("GeofenceEvent error received")
            crashReporter.setKey("geofence_error_code", errorCode)
            crashReporter.recordNonFatal(
                RuntimeException("GeofencingEvent error: code=$errorCode"),
                context = "GeofenceBroadcastReceiver.onReceive"
            )
            return
        }

        val pendingResult = goAsync()
        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            crashReporter.log("GeofenceBroadcastReceiver: transition=${geofencingEvent.geofenceTransition}, geofences=${geofencingEvent.triggeringGeofences?.map { it.requestId }}")        
            try {
                when (geofencingEvent.geofenceTransition) {
                    Geofence.GEOFENCE_TRANSITION_ENTER -> {
                        geofencingEvent.triggeringGeofences?.forEach { geofence ->
                            val wasMuted = muteStateManager.attemptMute("GEOFENCE_${geofence.requestId}")
                            if (wasMuted) {
                                startMuteService(context)
                            } else {
                                Log.d("GeofenceReceiver", "Entered zone ${geofence.requestId}, but phone was already silent or we are already muting it.")
                            }
                        }
                    }
                    Geofence.GEOFENCE_TRANSITION_EXIT -> {
                        geofencingEvent.triggeringGeofences?.forEach { geofence ->
                            val wasRestored = muteStateManager.attemptRestore("GEOFENCE_${geofence.requestId}")
                            if (wasRestored) {
                                stopMuteService(context)
                            }
                        }
                    }
                    else -> {
                        val transition = geofencingEvent.geofenceTransition
                        Log.e("GeofenceReceiver", "Unknown transition: $transition")
                        crashReporter.recordNonFatal(
                            RuntimeException("Unknown geofence transition type: $transition"),
                            context = "GeofenceBroadcastReceiver.onReceive"
                        )
                    }
                }
            } finally {
                pendingResult.finish()
            }
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
            crashReporter.recordNonFatal(e, context = "GeofenceBroadcastReceiver.startMuteService")
        }
    }

    private fun stopMuteService(context: Context) {
        val intent = Intent(context, com.dipdev.themutemaster.service.MuteService::class.java)
        context.stopService(intent)
    }
}