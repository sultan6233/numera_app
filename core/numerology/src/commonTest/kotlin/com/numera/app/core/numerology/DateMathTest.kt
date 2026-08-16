package com.numera.app.core.numerology

import kotlin.test.Test
import kotlin.test.assertEquals

class DateMathTest {

    @Test
    fun mondayFirstDayOfWeek_knownMonday() {
        // 2024-01-01 was a Monday.
        assertEquals(0, DateMath.mondayFirstDayOfWeek(2024, 1, 1))
    }

    @Test
    fun mondayFirstDayOfWeek_knownSaturday() {
        // 2000-01-01 was a Saturday.
        assertEquals(5, DateMath.mondayFirstDayOfWeek(2000, 1, 1))
    }

    @Test
    fun mondayFirstDayOfWeek_knownSunday() {
        // 2023-01-01 was a Sunday.
        assertEquals(6, DateMath.mondayFirstDayOfWeek(2023, 1, 1))
    }

    @Test
    fun daysInMonth_handlesLeapYearFebruary() {
        assertEquals(29, DateMath.daysInMonth(2024, 2))
        assertEquals(28, DateMath.daysInMonth(2023, 2))
    }
}
