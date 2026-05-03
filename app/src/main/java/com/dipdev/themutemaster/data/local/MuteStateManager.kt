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
        private const val KEY_ACTIVE_TRIGGERS = "active_triggers"
        private const val KEY_ORIGINAL_RINGER = "original_ringer_mode"
        private const val KEY_ORIGINAL_MEDIA_VOLUME = "original_media_volume"
    }

    /**
     * Called on trigger ENTER.
     * Returns TRUE if this was the first trigger (we took control).
     * The trigger is ALWAYS registered so that attemptRestore works correctly.
     */
    fun attemptMute(triggerId: String, profile: SoundProfile? = null): Boolean {
        val triggers = getActiveTriggers().toMutableSet()
        val isFirstTrigger = triggers.isEmpty()

        // Determine the profile to apply
        val ringerModeToApply = profile?.ringerMode ?: AudioManager.RINGER_MODE_VIBRATE
        val muteMedia = profile?.muteMedia ?: runBlocking { preferencesManager.muteMediaVolumeFlow.first() }
        val customMediaVol = profile?.customMediaVolumePercent

        if (isFirstTrigger) {
            val currentRinger = audioManager.ringerMode

            // Save original states BEFORE we change anything
            prefs.edit().putInt(KEY_ORIGINAL_RINGER, currentRinger).apply()
            val currentMediaVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
            prefs.edit().putInt(KEY_ORIGINAL_MEDIA_VOLUME, currentMediaVol).apply()

            // Only change ringer if the phone isn't already silent/vibrate
            if (currentRinger == AudioManager.RINGER_MODE_NORMAL && ringerModeToApply != null) {
                audioManager.ringerMode = ringerModeToApply
            }
        } else if (ringerModeToApply != null) {
            // Additional trigger — still apply if a more restrictive mode is needed
            audioManager.ringerMode = ringerModeToApply
        }

        // Apply media settings
        if (muteMedia) {
            try {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0)
            } catch (e: Exception) {
                Log.e("MuteMaster", "Failed adjustStreamVolume: ${e.message}")
            }
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
            Log.d("MuteMaster", "Media volume muted.")
        } else if (customMediaVol != null) {
            val maxVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val targetVol = (maxVol * (customMediaVol / 100f)).toInt()
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, targetVol, 0)
            Log.d("MuteMaster", "Media volume set to custom level: $targetVol")
        }

        if (!triggers.contains(triggerId)) {
            triggers.add(triggerId)
            setActiveTriggers(triggers)
            Log.d("MuteMaster", "Added trigger $triggerId. Active triggers: $triggers")
        }

        return isFirstTrigger
    }

    /**
     * Called on trigger EXIT.
     * Returns TRUE if we restored the volume (meaning no triggers left).
     */
    fun attemptRestore(triggerId: String): Boolean {
        val triggers = getActiveTriggers().toMutableSet()
        if (!triggers.contains(triggerId)) return false
        
        triggers.remove(triggerId)
        setActiveTriggers(triggers)
        Log.d("MuteMaster", "Removed trigger $triggerId. Active triggers: $triggers")

        // If there are still active triggers, DO NOT restore volume
        if (triggers.isNotEmpty()) return false

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
    fun isAppMuted(): Boolean = getActiveTriggers().isNotEmpty()

    fun clearAllTriggers() {
        setActiveTriggers(emptySet())
    }

    private fun getActiveTriggers(): Set<String> {
        return prefs.getStringSet(KEY_ACTIVE_TRIGGERS, emptySet()) ?: emptySet()
    }

    private fun setActiveTriggers(triggers: Set<String>) {
        prefs.edit().putStringSet(KEY_ACTIVE_TRIGGERS, triggers).apply()
    }
}