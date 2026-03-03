package com.dipdev.themutemaster.data.local

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioManager
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
 * Unit tests for [MuteStateManager].
 *
 * Uses Robolectric as the test runner so Android framework classes
 * (AudioManager, Log, SharedPreferences) work correctly on JVM without
 * needing a device or emulator.
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class MuteStateManagerTest {

    // --- Mocks ---
    private lateinit var mockContext: Context
    private lateinit var mockAudioManager: AudioManager
    private lateinit var mockPrefs: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor

    // The class under test
    private lateinit var muteStateManager: MuteStateManager

    @Before
    fun setUp() {
        mockAudioManager = mock()
        mockPrefs = mock()
        mockEditor = mock()
        mockContext = mock()

        // Wire up SharedPreferences editing chain
        whenever(mockContext.getSharedPreferences("mute_state", Context.MODE_PRIVATE))
            .thenReturn(mockPrefs)
        whenever(mockPrefs.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putBoolean(any(), any())).thenReturn(mockEditor)
        whenever(mockEditor.putInt(any(), any())).thenReturn(mockEditor)

        // Wire up AudioManager
        whenever(mockContext.getSystemService(Context.AUDIO_SERVICE))
            .thenReturn(mockAudioManager)

        muteStateManager = MuteStateManager(mockContext)
    }

    // -----------------------------------------------------------------------
    // attemptMute()
    // -----------------------------------------------------------------------

    @Test
    fun `attemptMute backs off when phone is already in VIBRATE mode`() {
        whenever(mockPrefs.getBoolean(eq("is_muted_by_app"), eq(false))).thenReturn(false)
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_VIBRATE)

        val result = muteStateManager.attemptMute()

        assertFalse("Should back off when already vibrating", result)
        // Must NOT write to prefs as muted
        verify(mockEditor, never()).putBoolean(eq("is_muted_by_app"), eq(true))
    }

    @Test
    fun `attemptMute backs off when phone is already in SILENT mode`() {
        whenever(mockPrefs.getBoolean(eq("is_muted_by_app"), eq(false))).thenReturn(false)
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_SILENT)

        val result = muteStateManager.attemptMute()

        assertFalse("Should back off when already silent", result)
        verify(mockEditor, never()).putBoolean(eq("is_muted_by_app"), eq(true))
    }

    @Test
    fun `attemptMute mutes and returns true when phone is NORMAL`() {
        whenever(mockPrefs.getBoolean(eq("is_muted_by_app"), eq(false))).thenReturn(false)
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_NORMAL)

        val result = muteStateManager.attemptMute()

        assertTrue("Should return true when successfully muted", result)
        // Ringer mode must be set to VIBRATE
        verify(mockAudioManager).ringerMode = AudioManager.RINGER_MODE_VIBRATE
        // Must persist the is-muted flag
        verify(mockEditor).putBoolean("is_muted_by_app", true)
        // apply() is called twice (save original ringer + save muted flag)
        verify(mockEditor, atLeast(1)).apply()
    }

    @Test
    fun `attemptMute returns true immediately when app already thinks it muted`() {
        // Simulates: we entered a zone, service restarted, receiver fires again
        whenever(mockPrefs.getBoolean(eq("is_muted_by_app"), eq(false))).thenReturn(true)

        val result = muteStateManager.attemptMute()

        assertTrue("Should return true — app already in control", result)
        // Must NOT touch the audio manager at all
        verify(mockAudioManager, never()).ringerMode
    }

    // -----------------------------------------------------------------------
    // attemptRestore()
    // -----------------------------------------------------------------------

    @Test
    fun `attemptRestore does nothing and returns false if app never muted`() {
        whenever(mockPrefs.getBoolean(eq("is_muted_by_app"), eq(false))).thenReturn(false)

        val result = muteStateManager.attemptRestore()

        assertFalse("Should return false — nothing to restore", result)
        verify(mockAudioManager, never()).ringerMode = any()
    }

    @Test
    fun `attemptRestore restores original ringer and returns true`() {
        whenever(mockPrefs.getBoolean(eq("is_muted_by_app"), eq(false))).thenReturn(true)
        // Phone is still in VIBRATE (we are in control)
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_VIBRATE)
        // The original mode we saved was NORMAL
        whenever(mockPrefs.getInt(eq("original_ringer_mode"), any()))
            .thenReturn(AudioManager.RINGER_MODE_NORMAL)

        val result = muteStateManager.attemptRestore()

        assertTrue("Should return true — successfully restored", result)
        verify(mockAudioManager).ringerMode = AudioManager.RINGER_MODE_NORMAL
    }

    @Test
    fun `attemptRestore returns false if user already manually unmuted`() {
        whenever(mockPrefs.getBoolean(eq("is_muted_by_app"), eq(false))).thenReturn(true)
        // User already switched back to NORMAL themselves
        whenever(mockAudioManager.ringerMode).thenReturn(AudioManager.RINGER_MODE_NORMAL)

        val result = muteStateManager.attemptRestore()

        // We clear our flag (user took control) but return false
        assertFalse("Should return false — user already unmuted manually", result)
        verify(mockEditor).putBoolean("is_muted_by_app", false)
        // Must NOT try to set the ringer again
        verify(mockAudioManager, never()).ringerMode = any()
    }
}
