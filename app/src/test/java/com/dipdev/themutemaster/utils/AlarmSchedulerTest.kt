package com.dipdev.themutemaster.utils

import android.content.Context
import com.dipdev.themutemaster.utils.AlarmScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Calendar

/**
 * Unit tests for [AlarmScheduler.calculateNextOccurrence] via reflection.
 *
 * This tests the day-of-week mapping logic which converts between
 * Calendar's Sun=1..Sat=7 and our Mon=1..Sun=7 system.
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class AlarmSchedulerTest {

    private lateinit var mockContext: Context
    private lateinit var scheduler: AlarmScheduler

    @Before
    fun setUp() {
        mockContext = mock()
        // Mock the alarm service to avoid NPE during construction
        val mockAlarmManager = mock<android.app.AlarmManager>()
        whenever(mockContext.getSystemService(Context.ALARM_SERVICE)).thenReturn(mockAlarmManager)

        scheduler = AlarmScheduler(mockContext)
    }

    private fun invokeCalculateNextOccurrence(timeMins: Int, daysOfWeek: String): Calendar? {
        val method = AlarmScheduler::class.java.getDeclaredMethod(
            "calculateNextOccurrence",
            Int::class.java,
            String::class.java
        )
        method.isAccessible = true
        return method.invoke(scheduler, timeMins, daysOfWeek) as Calendar?
    }

    @Test
    fun `returns null for empty daysOfWeek`() {
        val result = invokeCalculateNextOccurrence(540, "")
        assertNull(result)
    }

    @Test
    fun `returns null for all-invalid day values`() {
        val result = invokeCalculateNextOccurrence(540, "0,8,9")
        assertNull(result)
    }

    @Test
    fun `returns a non-null Calendar for valid days`() {
        // Every day should always find a match
        val result = invokeCalculateNextOccurrence(540, "1,2,3,4,5,6,7")
        assertNotNull(result)
    }

    @Test
    fun `result has correct hour and minute`() {
        val result = invokeCalculateNextOccurrence(570, "1,2,3,4,5,6,7") // 9:30
        assertNotNull(result)
        assertEquals(9, result!!.get(Calendar.HOUR_OF_DAY))
        assertEquals(30, result.get(Calendar.MINUTE))
        assertEquals(0, result.get(Calendar.SECOND))
    }

    @Test
    fun `result is in the future`() {
        val result = invokeCalculateNextOccurrence(0, "1,2,3,4,5,6,7") // midnight
        assertNotNull(result)
        // Should be today's midnight (if not passed) or tomorrow's
        val now = Calendar.getInstance()
        assertTrue("Result should be in the future or now", result!!.timeInMillis >= now.timeInMillis - 1000)
    }

    @Test
    fun `single day schedule finds the correct day`() {
        // Only Monday (1)
        val result = invokeCalculateNextOccurrence(540, "1")
        assertNotNull(result)

        // The resulting calendar's day should be Monday
        val calDow = result!!.get(Calendar.DAY_OF_WEEK)
        val javaDow = if (calDow == Calendar.SUNDAY) 7 else calDow - 1
        assertEquals("Should schedule on Monday", 1, javaDow)
    }

    private fun assertTrue(message: String, condition: Boolean) {
        org.junit.Assert.assertTrue(message, condition)
    }
}
