package com.dipdev.themutemaster.data.local

import android.content.Context
import android.media.AudioManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton
import com.dipdev.themutemaster.data.local.PreferencesManager

@Singleton
class MuteStateManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) {
    private val prefs = context.getSharedPreferences("mute_state", Context.MODE_PRIVATE)
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    companion object {
        private const val KEY_IS_MUTED_BY_APP = "is_muted_by_app"
        private const val KEY_ORIGINAL_RINGER = "original_ringer_mode"
        private const val KEY_ORIGINAL_MEDIA_VOLUME = "original_media_volume"
    }

    /**
     * Called on Geofence ENTER.
     * Returns TRUE if we actually muted the phone.
     */
    fun attemptMute(): Boolean {
        // Optimization: If we already think we muted it, double check we are still in control
        if (isAppMuted()) return true

        val currentRinger = audioManager.ringerMode

        // 1. CHECK: Is the phone already Silent/Vibrate?
        if (currentRinger != AudioManager.RINGER_MODE_NORMAL) {
            Log.d("MuteMaster", "Phone already silent. Backing off.")
            setAppMuted(false)
            return false
        }

        // 2. ACTION: Phone is noisy. We take control.
        prefs.edit().putInt(KEY_ORIGINAL_RINGER, currentRinger).apply()

        // Use VIBRATE to avoid DND permission issues
        audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE

        // Mute Media Volume if preference is enabled
        val muteMedia = runBlocking { preferencesManager.muteMediaVolumeFlow.first() }
        Log.d("MuteMaster", "muteMediaVolume preference is: $muteMedia")
        if (muteMedia) {
            val currentMediaVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            Log.d("MuteMaster", "Current media volume: $currentMediaVol")
            prefs.edit().putInt(KEY_ORIGINAL_MEDIA_VOLUME, currentMediaVol).apply()
            
            // Try both adjustStreamVolume and setStreamVolume to ensure muting
            try {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
            } catch (e: Exception) {
                Log.e("MuteMaster", "Failed adjustStreamVolume: ${e.message}")
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
            Log.d("MuteMaster", "Media volume set to 0.")
        } else {
            prefs.edit().remove(KEY_ORIGINAL_MEDIA_VOLUME).apply()
        }

        setAppMuted(true)
        Log.d("MuteMaster", "MuteMaster silenced the phone.")
        return true
    }

    /**
     * Called on Geofence EXIT.
     */
    fun attemptRestore(): Boolean {
        if (!isAppMuted()) return false

        // Mark session as finished immediately
        setAppMuted(false)

        val currentRinger = audioManager.ringerMode
        if (currentRinger == AudioManager.RINGER_MODE_NORMAL) {
            return false // User manually unmuted, we did nothing
        }

        val original = prefs.getInt(KEY_ORIGINAL_RINGER, AudioManager.RINGER_MODE_NORMAL)
        audioManager.ringerMode = original

        // Restore Media Volume if it was saved
        if (prefs.contains(KEY_ORIGINAL_MEDIA_VOLUME)) {
            val originalMedia = prefs.getInt(KEY_ORIGINAL_MEDIA_VOLUME, 0)
            
            try {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0)
            } catch (e: Exception) {
                Log.e("MuteMaster", "Failed adjustStreamVolume unmute: ${e.message}")
            }
            
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalMedia, 0)
            prefs.edit().remove(KEY_ORIGINAL_MEDIA_VOLUME).apply()
        }

        return true // We restored it
    }

    // --- UPDATED: Now Public for Service Access ---
    fun isAppMuted(): Boolean = prefs.getBoolean(KEY_IS_MUTED_BY_APP, false)

    fun setAppMuted(isMuted: Boolean) {
        prefs.edit().putBoolean(KEY_IS_MUTED_BY_APP, isMuted).apply()
    }
}