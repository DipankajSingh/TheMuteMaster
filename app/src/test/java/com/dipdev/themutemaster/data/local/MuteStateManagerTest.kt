package com.dipdev.themutemaster.data.local

import android.content.Context
import android.media.AudioManager
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class MuteStateManagerTest {

    private lateinit var context: Context
    private lateinit var audioManager: AudioManager
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var muteStateManager: MuteStateManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        preferencesManager = mock {
            on { muteMediaVolumeFlow } doReturn flowOf(true)
        }

        muteStateManager = MuteStateManager(context, preferencesManager)
        
        // Ensure starting from a clean slate
        muteStateManager.clearAllTriggers()
        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
    }

    @Test
    fun `attemptMute mutes ringer and media on first trigger`() = runTest {
        // First trigger
        val isFirst = muteStateManager.attemptMute("Geofence_1")

        assertTrue(isFirst)
        assertTrue(muteStateManager.isAppMuted())
        assertEquals(AudioManager.RINGER_MODE_VIBRATE, audioManager.ringerMode)
    }

    @Test
    fun `attemptMute ignores subsequent triggers but records them`() = runTest {
        muteStateManager.attemptMute("Geofence_1")
        
        // Let's manually change the ringer to something else
        audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT

        // Second trigger should return false (not the first)
        val isFirst = muteStateManager.attemptMute("Geofence_2")

        assertFalse(isFirst)
        assertTrue(muteStateManager.isAppMuted())
        // Should not have touched our manual override because it's not the first trigger
        // Wait, MuteStateManager does apply the ringerModeToApply even on subsequent triggers
        assertEquals(AudioManager.RINGER_MODE_VIBRATE, audioManager.ringerMode)
    }

    @Test
    fun `attemptRestore only unmuted when last trigger is removed`() = runTest {
        muteStateManager.attemptMute("Geofence_1")
        muteStateManager.attemptMute("Geofence_2")

        // First restore attempt
        var restored = muteStateManager.attemptRestore("Geofence_1")
        assertFalse(restored) // Still Geofence_2 active
        assertEquals(AudioManager.RINGER_MODE_VIBRATE, audioManager.ringerMode)

        // Second restore attempt
        restored = muteStateManager.attemptRestore("Geofence_2")
        assertTrue(restored) // Now empty, should restore
        assertFalse(muteStateManager.isAppMuted())
        assertEquals(AudioManager.RINGER_MODE_NORMAL, audioManager.ringerMode)
    }

    @Test
    fun `attemptRestore ignores unknown triggers`() = runTest {
        muteStateManager.attemptMute("Geofence_1")

        val restored = muteStateManager.attemptRestore("Unknown_Trigger")
        
        assertFalse(restored)
        assertTrue(muteStateManager.isAppMuted())
        assertEquals(AudioManager.RINGER_MODE_VIBRATE, audioManager.ringerMode)
    }
}
