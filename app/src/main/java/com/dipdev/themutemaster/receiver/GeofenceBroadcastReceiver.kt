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
import com.dipdev.themutemaster.utils.hasNotificationPermission
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val notificationId = 1234
    private val channelId = "mute_master_debug_channel" // Renamed for clarity

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent) ?: return

        if (geofencingEvent.hasError()) {
            val errorMsg = "Error Code: ${geofencingEvent.errorCode}"
            Log.e("GeofenceReceiver", errorMsg)
            sendDebugNotification(context, "Geofence Error", errorMsg) // Notify on error too!
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition
        val muteStateManager = MuteStateManager(context)
        val timestamp = getTimestamp()

        when (geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Log.d("GeofenceReceiver", "ENTER Event at $timestamp")

                // Attempt Mute
                val wasMuted = muteStateManager.attemptMute()

                // Detailed Debug Message
                val status = if (wasMuted) "Success (Muted)" else "Skipped (Already Silent/Error)"
                sendDebugNotification(context, "Entered Zone", "Time: $timestamp\nResult: $status")
            }

            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.d("GeofenceReceiver", "EXIT Event at $timestamp")

                // Attempt Restore
                val wasRestored = muteStateManager.attemptRestore()

                // Detailed Debug Message
                // WE DO NOT CANCEL. We update it so you see the proof.
                val status = if (wasRestored) "Success (Restored)" else "Skipped (No Change)"
                sendDebugNotification(context, "Exited Zone", "Time: $timestamp\nResult: $status")
            }

            else -> {
                Log.e("GeofenceReceiver", "Unknown transition: $geofenceTransition")
            }
        }
    }

    private fun sendDebugNotification(context: Context, title: String, message: String) {
        // 1. Permission Check
        if (!context.hasNotificationPermission()) {
            Log.w("GeofenceReceiver", "Debug Notification skipped: No Permission")
            return
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 2. Create Channel (High Importance for Debugging so it pops up)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Debug Logs",
                NotificationManager.IMPORTANCE_HIGH // Make it noisy for testing
            ).apply {
                description = "Geofence Debug Messages"
                setShowBadge(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 3. Build Notification with BigTextStyle (for multi-line logs)
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ensure this icon exists
            .setContentTitle(title)
            .setContentText(message) // Short version
            .setStyle(NotificationCompat.BigTextStyle().bigText(message)) // Expandable long version
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL) // Vibrate/Sound to alert you
            .setAutoCancel(false) // Keep it there until you dismiss it
            .build()

        try {
            // Using same ID (1234) means it overwrites the previous one.
            // If you want a HISTORY log, use `System.currentTimeMillis().toInt()` instead of `notificationId`.
            notificationManager.notify(notificationId, notification)
        } catch (e: SecurityException) {
            Log.e("GeofenceReceiver", "Failed to notify: ${e.message}")
        }
    }

    private fun getTimestamp(): String {
        return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    }
}