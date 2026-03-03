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
                    showActiveNotification(context)
                } else {
                    Log.d("GeofenceReceiver", "Entered zone, but phone was already silent.")
                }
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                val wasRestored = muteStateManager.attemptRestore()
                if (wasRestored) {
                    cancelNotification(context)
                }
            }
            else -> Log.e("GeofenceReceiver", "Unknown transition: ${geofencingEvent.geofenceTransition}")
        }
    }

    private fun showActiveNotification(context: Context) {
        if (!context.hasNotificationPermission()) {
            Log.w("GeofenceReceiver", "Skipping notification: POST_NOTIFICATIONS permission missing")
            return
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationConstants.CHANNEL_ID,
                NotificationConstants.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows when auto-muting is active"
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Auto-Muting Active")
            .setContentText("You are in a silent zone.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        try {
            notificationManager.notify(NotificationConstants.NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            Log.e("GeofenceReceiver", "Failed to show notification: ${e.message}")
        }
    }

    private fun cancelNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NotificationConstants.NOTIFICATION_ID)
    }
}