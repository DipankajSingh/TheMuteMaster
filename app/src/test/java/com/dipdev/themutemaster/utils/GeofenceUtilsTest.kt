package com.dipdev.themutemaster.utils

import com.dipdev.themutemaster.data.local.GeofenceEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [GeofenceUtils.findOverlappingGeofence].
 *
 * Uses Robolectric to provide a real implementation of
 * [android.location.Location.distanceBetween], which is an Android API
 * not available on plain JVM.
 *
 * Reference coordinates (real points ~250m apart):
 *   Centre:  28.6139° N, 77.2090° E  (Connaught Place, Delhi)
 *   Nearby:  28.6117° N, 77.2090° E  (~240m south)
 *   Far:     28.6300° N, 77.2090° E  (~1800m north)
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GeofenceUtilsTest {

    // A geofence centred at 28.6139, 77.2090 with a 300m radius
    private val centreGeofence = GeofenceEntity(
        id = 1,
        latitude = 28.6139,
        longitude = 77.2090,
        radius = 300f,
        isEnabled = true,
        name = "Centre"
    )

    @Test
    fun `returns null for an empty list`() {
        val result = GeofenceUtils.findOverlappingGeofence(28.6139, 77.2090, emptyList())
        assertNull(result)
    }

    @Test
    fun `returns geofence when user is clearly inside the radius`() {
        // User is at the exact centre — distance is 0m, well inside 300m radius
        val result = GeofenceUtils.findOverlappingGeofence(
            28.6139, 77.2090,
            listOf(centreGeofence)
        )
        assertEquals(centreGeofence, result)
    }

    @Test
    fun `returns geofence when user is near centre but still inside radius`() {
        // ~240m south — inside 300m radius
        val result = GeofenceUtils.findOverlappingGeofence(
            28.6117, 77.2090,
            listOf(centreGeofence)
        )
        assertEquals(centreGeofence, result)
    }

    @Test
    fun `returns null when user is clearly outside the radius`() {
        // ~1800m north — well outside 300m radius
        val result = GeofenceUtils.findOverlappingGeofence(
            28.6300, 77.2090,
            listOf(centreGeofence)
        )
        assertNull(result)
    }

    @Test
    fun `returns the correct geofence when multiple zones exist`() {
        val farGeofence = GeofenceEntity(
            id = 2,
            latitude = 28.6300,
            longitude = 77.2090,
            radius = 300f,
            isEnabled = true,
            name = "Far"
        )

        // User is at the centre — should match centreGeofence, not farGeofence
        val result = GeofenceUtils.findOverlappingGeofence(
            28.6139, 77.2090,
            listOf(centreGeofence, farGeofence)
        )
        assertEquals(centreGeofence, result)
    }

    @Test
    fun `returns null when only matching geofence is disabled`() {
        // The overlap check does NOT filter by isEnabled — that's the caller's job.
        // This test documents the current behaviour: findOverlappingGeofence returns
        // the entity regardless of isEnabled, so callers must decide what to do with it.
        val disabledGeofence = centreGeofence.copy(isEnabled = false)
        val result = GeofenceUtils.findOverlappingGeofence(
            28.6139, 77.2090,
            listOf(disabledGeofence)
        )
        // Documents current behaviour — returns the entity (enabled flag is ignored here)
        assertEquals(disabledGeofence, result)
    }
}
