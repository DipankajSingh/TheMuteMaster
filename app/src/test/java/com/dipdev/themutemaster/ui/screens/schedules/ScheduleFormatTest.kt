package com.dipdev.themutemaster.ui.screens.schedules

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [formatTimeMins] and [formatDaysOfWeek].
 */
class ScheduleFormatTest {

    // -----------------------------------------------------------------------
    // formatTimeMins()
    // -----------------------------------------------------------------------

    @Test
    fun `formatTimeMins midnight shows 12-00 AM`() {
        assertEquals("12:00 AM", formatTimeMins(0))
    }

    @Test
    fun `formatTimeMins 1 AM`() {
        assertEquals("1:00 AM", formatTimeMins(60))
    }

    @Test
    fun `formatTimeMins 9-30 AM`() {
        assertEquals("9:30 AM", formatTimeMins(570))
    }

    @Test
    fun `formatTimeMins noon shows 12-00 PM`() {
        assertEquals("12:00 PM", formatTimeMins(720))
    }

    @Test
    fun `formatTimeMins 1-00 PM`() {
        assertEquals("1:00 PM", formatTimeMins(780))
    }

    @Test
    fun `formatTimeMins 11-59 PM`() {
        assertEquals("11:59 PM", formatTimeMins(1439))
    }

    @Test
    fun `formatTimeMins 5-00 PM`() {
        assertEquals("5:00 PM", formatTimeMins(1020))
    }

    // -----------------------------------------------------------------------
    // formatDaysOfWeek()
    // -----------------------------------------------------------------------

    @Test
    fun `formatDaysOfWeek empty string returns Once`() {
        assertEquals("Once", formatDaysOfWeek(""))
    }

    @Test
    fun `formatDaysOfWeek all 7 days returns Every day`() {
        assertEquals("Every day", formatDaysOfWeek("1,2,3,4,5,6,7"))
    }

    @Test
    fun `formatDaysOfWeek Mon-Fri`() {
        assertEquals("Mon, Tue, Wed, Thu, Fri", formatDaysOfWeek("1,2,3,4,5"))
    }

    @Test
    fun `formatDaysOfWeek single day`() {
        assertEquals("Wed", formatDaysOfWeek("3"))
    }

    @Test
    fun `formatDaysOfWeek weekend`() {
        assertEquals("Sat, Sun", formatDaysOfWeek("6,7"))
    }

    @Test
    fun `formatDaysOfWeek unsorted input is sorted in output`() {
        assertEquals("Mon, Wed, Fri", formatDaysOfWeek("5,1,3"))
    }

    @Test
    fun `formatDaysOfWeek invalid day values are ignored`() {
        assertEquals("Mon", formatDaysOfWeek("0,1,8"))
    }

    @Test
    fun `formatDaysOfWeek all invalid returns Once`() {
        assertEquals("Once", formatDaysOfWeek("0,8,9"))
    }

    @Test
    fun `formatDaysOfWeek non-numeric input is ignored`() {
        assertEquals("Tue", formatDaysOfWeek("abc,2,xyz"))
    }
}
