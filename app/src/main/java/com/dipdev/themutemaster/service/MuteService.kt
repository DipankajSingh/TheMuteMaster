package com.dipdev.themutemaster.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo // <--- Added this import
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dipdev.themutemaster.R
import com.dipdev.themutemaster.data.local.MuteStateManager
import com.dipdev.themutemaster.utils.NotificationConstants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MuteService : Service() {

    @Inject lateinit var muteStateManager: MuteStateManager

    private lateinit var audioManager: AudioManager
    private val channelId = NotificationConstants.CHANNEL_ID
    private val notificationId = NotificationConstants.NOTIFICATION_ID

    private val volumeChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == AudioManager.RINGER_MODE_CHANGED_ACTION) {
                val currentMode = audioManager.ringerMode

                if (currentMode == AudioManager.RINGER_MODE_NORMAL) {
                    Log.d("MuteService", "Manual Unmute Detected. Stopping service.")

                    muteStateManager.clearAllTriggers()
                    stopSelf()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(volumeChangeReceiver, IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION), Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(volumeChangeReceiver, IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION))
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!muteStateManager.isAppMuted()) {
            stopSelf()
            return START_NOT_STICKY
        }

        // --- UPDATED START FOREGROUND LOGIC ---
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ requires the type to match the Manifest
                androidx.core.app.ServiceCompat.startForeground(
                    this,
                    notificationId,
                    createNotification(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
                )
            } else {
                // Older versions just take the ID and Notification
                startForeground(notificationId, createNotification())
            }
        } catch (e: Exception) {
            Log.e("MuteService", "Failed to start foreground service: ${e.message}")
            // Even if foreground service fails, we are already muted, but we might get killed soon.
            // We can't do much here except avoid crashing.
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(volumeChangeReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, NotificationConstants.CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW
            ).apply { setShowBadge(false) }
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle("Auto-Muting Active")
            .setContentText("You are in a silent zone.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
}