package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.SimpleDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PythagoreanCalculatorTest {

    @Test
    fun lifePath_reducesToSingleDigit() {
        // 1990-07-16 -> 1+9+9+0+0+7+1+6 = 33 (kept as master number)
        val result = PythagoreanCalculator.lifePath(SimpleDate(1990, 7, 16))
        assertEquals(33, result)
    }

    @Test
    fun lifePath_reducesRegularSum() {
        // 1988-03-14 -> 1+9+8+8+0+3+1+4 = 34 -> 3+4 = 7
        val result = PythagoreanCalculator.lifePath(SimpleDate(1988, 3, 14))
        assertEquals(7, result)
    }

    @Test
    fun expression_isDeterministicForSameName() {
        val first = PythagoreanCalculator.expression("Anna Ivanova")
        val second = PythagoreanCalculator.expression("Anna Ivanova")
        assertEquals(first, second)
    }

    @Test
    fun soulUrge_onlyCountsVowels() {
        val soulUrge = PythagoreanCalculator.soulUrge("AAAA")
        // A=1 each, 4 vowels -> 4
        assertEquals(4, soulUrge)
    }

    @Test
    fun personality_onlyCountsConsonants() {
        val personality = PythagoreanCalculator.personality("AAAA")
        assertEquals(0, personality)
    }

    @Test
    fun birthDayNumber_keepsMasterNumber() {
        // 29 -> 2+9 = 11, kept as master number
        assertEquals(11, PythagoreanCalculator.birthDayNumber(SimpleDate(2000, 1, 29)))
    }

    @Test
    fun birthDayNumber_reducesRegularDay() {
        assertEquals(6, PythagoreanCalculator.birthDayNumber(SimpleDate(2000, 1, 15)))
    }

    @Test
    fun expression_transliteratesCyrillicNameToSameValueAsItsLatinSpelling() {
        // Regression test: letterValue only understands A-Z, so a Cyrillic name used to score 0
        // for expression/soulUrge/personality instead of being transliterated first.
        assertEquals(PythagoreanCalculator.expression("Anna"), PythagoreanCalculator.expression("Анна"))
    }

    @Test
    fun expression_isNonZeroForCyrillicOnlyName() {
        assertTrue(PythagoreanCalculator.expression("Максим Петров") > 0)
    }
}
