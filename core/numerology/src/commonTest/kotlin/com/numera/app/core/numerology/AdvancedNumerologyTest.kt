package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.NumerologyProfile
import com.numera.app.core.numerology.model.SimpleDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AdvancedNumerologyTest {

    @Test
    fun karmicDebtNumbers_flagsBirthOnADebtDay() {
        val profile = NumerologyProfile(fullName = "Test Name", birthDate = SimpleDate(2000, 5, 13))
        assertTrue(13 in AdvancedNumerology.karmicDebtNumbers(profile))
    }

    @Test
    fun karmicDebtNumbers_emptyWhenNoDebtPresent() {
        val profile = NumerologyProfile(fullName = "Anna", birthDate = SimpleDate(2000, 5, 10))
        assertTrue(AdvancedNumerology.karmicDebtNumbers(profile).none { it == profile.birthDate.day })
    }

    @Test
    fun personalYear_isAlwaysASingleDigit() {
        val year = AdvancedNumerology.personalYear(SimpleDate(1990, 7, 16), 2026)
        assertTrue(year in 1..9)
    }

    @Test
    fun personalMonth_isAlwaysASingleDigit() {
        val month = AdvancedNumerology.personalMonth(SimpleDate(1990, 7, 16), 2026, 11)
        assertTrue(month in 1..9)
    }

    @Test
    fun karmicLessonNumbers_excludesDigitsPresentInName() {
        // "A" -> letterValue 1, so 1 must not show up as a missing/lesson number.
        val lessons = AdvancedNumerology.karmicLessonNumbers("A")
        assertTrue(1 !in lessons)
        assertTrue(2 in lessons)
    }

    @Test
    fun hiddenPassionNumber_picksTheMostFrequentLetterValue() {
        // A=1 (x2), B=2 (x1) -> most frequent value is 1.
        assertEquals(1, AdvancedNumerology.hiddenPassionNumber("AAB"))
    }

    @Test
    fun cornerstoneAndCapstone_readFirstAndLastLetterOfTheName() {
        // A=1, B=2, C=3 -> cornerstone reads "A", capstone reads "C".
        assertEquals(1, AdvancedNumerology.cornerstoneNumber("ABC"))
        assertEquals(3, AdvancedNumerology.capstoneNumber("ABC"))
    }
}
