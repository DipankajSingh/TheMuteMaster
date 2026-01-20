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
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    // A constant ID ensures we update/remove the SAME notification every time
    private val notificationId = 1234
    private val channelId = "mute_master_status_channel"

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return
        if (geofencingEvent.hasError()) {
            Log.e("GeofenceReceiver", "Error Code: ${geofencingEvent.errorCode}")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        val muteStateManager = MuteStateManager(context)

        when (geofenceTransition) {
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
            else -> Log.e("GeofenceReceiver", "Unknown transition: $geofenceTransition")
        }
    }

    private fun showActiveNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Active Status",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows when auto-muting is active"
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Auto-Muting Active")
            .setContentText("You are in a silent zone.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    private fun cancelNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}