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
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MuteService : Service() {

    @Inject lateinit var muteStateManager: MuteStateManager

    private lateinit var audioManager: AudioManager
    private val channelId = "mute_master_status_channel"
    private val notificationId = 1234

    private val volumeChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == AudioManager.RINGER_MODE_CHANGED_ACTION) {
                val currentMode = audioManager.ringerMode

                if (currentMode == AudioManager.RINGER_MODE_NORMAL) {
                    Log.d("MuteService", "Manual Unmute Detected. Stopping service.")

                    muteStateManager.setAppMuted(false)
                    stopSelf()
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        registerReceiver(volumeChangeReceiver, IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null && !muteStateManager.isAppMuted()) {
            stopSelf()
            return START_NOT_STICKY
        }

        val muted = muteStateManager.attemptMute()

        // --- UPDATED START FOREGROUND LOGIC ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10+ requires the type to match the Manifest
            startForeground(
                notificationId,
                createNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            // Older versions just take the ID and Notification
            startForeground(notificationId, createNotification())
        }

        if (!muted) {
            stopSelf()
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
                channelId, "Active Status", NotificationManager.IMPORTANCE_LOW
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