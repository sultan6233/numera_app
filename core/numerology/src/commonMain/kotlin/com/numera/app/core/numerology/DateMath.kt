package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.SimpleDate

object DateMath {
    fun daysInMonth(year: Int, month: Int): Int = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> 30
    }

    /** The calendar day immediately before [date] - used for walking backward through a
     *  history of dates (e.g. counting a streak) without pulling in kotlinx-datetime. */
    fun previousDay(date: SimpleDate): SimpleDate {
        var day = date.day - 1
        var month = date.month
        var year = date.year
        if (day < 1) {
            month -= 1
            if (month < 1) { month = 12; year -= 1 }
            day = daysInMonth(year, month)
        }
        return SimpleDate(year, month, day)
    }

    /**
     * Sakamoto's algorithm (Gregorian calendar). Returns a Monday-first index (0=Monday..6=Sunday)
     * to match how weeks are conventionally laid out in Russian calendar UIs.
     */
    fun mondayFirstDayOfWeek(year: Int, month: Int, day: Int): Int {
        val t = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
        val y = if (month < 3) year - 1 else year
        val sundayFirst = (y + y / 4 - y / 100 + y / 400 + t[month - 1] + day) % 7
        return (sundayFirst + 6) % 7
    }

    private fun isLeapYear(year: Int): Boolean =
        (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
}
