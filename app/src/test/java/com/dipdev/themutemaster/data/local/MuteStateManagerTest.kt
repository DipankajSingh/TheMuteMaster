package com.dipdev.themutemaster.data.local

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeast
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [MuteStateManager] with the Mute Lock System.
 *
 * Tests the trigger-based muting/restoring logic:
 * - Multiple triggers can coexist
 * - Volume is only restored when ALL triggers are removed
 * - SoundProfile overrides are applied correctly
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class MuteStateManagerTest {

    private lateinit var mockContext: Context
    private lateinit var mockAudioManager: AudioManager
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor
    private lateinit var mockPreferencesManager: PreferencesManager

    private lateinit var muteStateManager: MuteStateManager

    @Before
    fun setUp() {
        mockAudioManager = mock()
        mockPrefs = mock()
        mockEditor = mock()
        mockContext = mock()
        mockPreferencesManager = mock()

        // Wire up SharedPreferences
        whenever(mockContext.getSharedPreferences("mute_state", Context.MODE_PRIVATE))
            .thenReturn(mockPrefs)
        whenever(mockPrefs.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putInt(any(), any())).thenReturn(mockEditor)
        whenever(mockEditor.putStringSet(any(), any())).thenReturn(mockEditor)
        whenever(mockEditor.remove(any())).thenReturn(mockEditor)

        // Wire up AudioManager
        whenever(mockContext.getSystemService(Context.AUDIO_SERVICE))
            .thenReturn(mockAudioManager)

        // Default: no active triggers, media preference disabled
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(emptySet())
        whenever(mockPreferencesManager.muteMediaVolumeFlow)
            .thenReturn(flowOf(false))

        muteStateManager = MuteStateManager(mockContext, mockPreferencesManager)
    }

    // -----------------------------------------------------------------------
    // attemptMute() — Basic Trigger Logic
    // -----------------------------------------------------------------------

    @Test
    fun `attemptMute returns true for the first trigger when phone is NORMAL`() {
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_NORMAL)

        val result = muteStateManager.attemptMute("GEOFENCE_1")

        assertTrue("First trigger should return true", result)
        verify(mockAudioManager).ringerMode = AudioManager.RINGER_MODE_VIBRATE
        verify(mockEditor).putStringSet(eq("active_triggers"), any())
        verify(mockEditor, atLeast(1)).apply()
    }

    @Test
    fun `attemptMute saves original ringer mode before changing it`() {
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_NORMAL)

        muteStateManager.attemptMute("GEOFENCE_1")

        verify(mockEditor).putInt("original_ringer_mode", AudioManager.RINGER_MODE_NORMAL)
    }

    @Test
    fun `attemptMute registers trigger even when phone is already VIBRATE`() {
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_VIBRATE)

        val result = muteStateManager.attemptMute("SCHEDULE_1")

        // Should still return true (first trigger) and register
        assertTrue("Must register the trigger even if phone is already silent", result)
        verify(mockEditor).putStringSet(eq("active_triggers"), any())
        // Should NOT change ringer (already vibrating)
        verify(mockAudioManager, never()).ringerMode = any()
    }

    @Test
    fun `attemptMute returns false for second trigger`() {
        // Simulate one existing trigger
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(setOf("GEOFENCE_1"))

        val result = muteStateManager.attemptMute("SCHEDULE_1")

        assertFalse("Second trigger should return false", result)
    }

    @Test
    fun `attemptMute does not overwrite original ringer for second trigger`() {
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(setOf("GEOFENCE_1"))

        muteStateManager.attemptMute("SCHEDULE_1")

        verify(mockEditor, never()).putInt(eq("original_ringer_mode"), any())
    }

    @Test
    fun `attemptMute with duplicate triggerId does not add it again`() {
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(setOf("GEOFENCE_1"))

        muteStateManager.attemptMute("GEOFENCE_1")

        // putStringSet should not be called since the trigger already exists
        verify(mockEditor, never()).putStringSet(any(), any())
    }

    // -----------------------------------------------------------------------
    // attemptMute() — SoundProfile
    // -----------------------------------------------------------------------

    @Test
    fun `attemptMute with SoundProfile mutes media`() {
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_NORMAL)

        val profile = SoundProfile(
            ringerMode = AudioManager.RINGER_MODE_VIBRATE,
            muteMedia = true,
            customMediaVolumePercent = null
        )

        muteStateManager.attemptMute("SCHEDULE_1", profile)

        verify(mockAudioManager).setStreamVolume(eq(AudioManager.STREAM_MUSIC), eq(0), eq(0))
    }

    @Test
    fun `attemptMute with custom media volume sets correct level`() {
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_NORMAL)
        whenever(mockAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).thenReturn(15)

        val profile = SoundProfile(
            ringerMode = AudioManager.RINGER_MODE_VIBRATE,
            muteMedia = false,
            customMediaVolumePercent = 50
        )

        muteStateManager.attemptMute("SCHEDULE_1", profile)

        // 50% of 15 = 7
        verify(mockAudioManager).setStreamVolume(eq(AudioManager.STREAM_MUSIC), eq(7), eq(0))
    }

    // -----------------------------------------------------------------------
    // attemptRestore()
    // -----------------------------------------------------------------------

    @Test
    fun `attemptRestore returns false for unknown trigger`() {
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(setOf("GEOFENCE_1"))

        val result = muteStateManager.attemptRestore("UNKNOWN_TRIGGER")

        assertFalse("Unknown trigger should return false", result)
    }

    @Test
    fun `attemptRestore does not restore when other triggers remain`() {
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(setOf("GEOFENCE_1", "SCHEDULE_1"))

        val result = muteStateManager.attemptRestore("GEOFENCE_1")

        assertFalse("Should not restore while other triggers are active", result)
        verify(mockAudioManager, never()).ringerMode = any()
    }

    @Test
    fun `attemptRestore restores ringer when last trigger is removed`() {
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(setOf("GEOFENCE_1"))
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_VIBRATE)
        whenever(mockPrefs.getInt(eq("original_ringer_mode"), any()))
            .thenReturn(AudioManager.RINGER_MODE_NORMAL)
        whenever(mockPrefs.contains("original_media_volume")).thenReturn(false)

        val result = muteStateManager.attemptRestore("GEOFENCE_1")

        assertTrue("Should restore when last trigger is removed", result)
        verify(mockAudioManager).ringerMode = AudioManager.RINGER_MODE_NORMAL
    }

    @Test
    fun `attemptRestore returns false if user manually unmuted`() {
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(setOf("GEOFENCE_1"))
        // User already set back to NORMAL
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_NORMAL)

        val result = muteStateManager.attemptRestore("GEOFENCE_1")

        assertFalse("Should return false — user already unmuted", result)
        verify(mockAudioManager, never()).ringerMode = any()
    }

    @Test
    fun `attemptRestore restores media volume when it was saved`() {
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(setOf("SCHEDULE_1"))
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_VIBRATE)
        whenever(mockPrefs.getInt(eq("original_ringer_mode"), any()))
            .thenReturn(AudioManager.RINGER_MODE_NORMAL)
        whenever(mockPrefs.contains("original_media_volume")).thenReturn(true)
        whenever(mockPrefs.getInt(eq("original_media_volume"), any())).thenReturn(10)

        val result = muteStateManager.attemptRestore("SCHEDULE_1")

        assertTrue(result)
        verify(mockAudioManager).setStreamVolume(eq(AudioManager.STREAM_MUSIC), eq(10), eq(0))
    }

    // -----------------------------------------------------------------------
    // isAppMuted() / clearAllTriggers()
    // -----------------------------------------------------------------------

    @Test
    fun `isAppMuted returns false when no triggers`() {
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(emptySet())

        assertFalse(muteStateManager.isAppMuted())
    }

    @Test
    fun `isAppMuted returns true when triggers exist`() {
        whenever(mockPrefs.getStringSet(eq("active_triggers"), any()))
            .thenReturn(setOf("GEOFENCE_1"))

        assertTrue(muteStateManager.isAppMuted())
    }

    @Test
    fun `clearAllTriggers writes an empty set`() {
        muteStateManager.clearAllTriggers()

        verify(mockEditor).putStringSet(eq("active_triggers"), eq(emptySet()))
    }
}
