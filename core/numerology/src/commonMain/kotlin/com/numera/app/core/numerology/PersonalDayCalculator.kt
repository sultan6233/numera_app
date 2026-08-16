package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.SimpleDate

/**
 * Personal Day Number - the standard numerology mechanic for a value that legitimately changes
 * every day for the same person, used as the seed for daily content (see backend spec).
 */
object PersonalDayCalculator {
    fun personalDay(birthDate: SimpleDate, targetDate: SimpleDate): Int =
        PythagoreanCalculator.reduceToSingleDigit(
            birthDate.day + birthDate.month + targetDate.day + targetDate.month + targetDate.year
        )
}
