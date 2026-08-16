package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.NumerologyProfile
import com.numera.app.core.numerology.model.SimpleDate

/**
 * Extended Pythagorean numbers beyond the core five (life path/expression/soul urge/
 * personality/birth day) - still pure arithmetic derived from name and birth date, no new
 * inputs required.
 */
object AdvancedNumerology {

    fun maturityNumber(profile: NumerologyProfile): Int =
        PythagoreanCalculator.reduceToSingleDigit(
            PythagoreanCalculator.lifePath(profile.birthDate) + PythagoreanCalculator.expression(profile.fullName)
        )

    /**
     * Personal Year/Month are conventionally reduced fully to 1-9 (no master-number exception) -
     * unlike the core identity numbers, these describe a recurring yearly cycle rather than a
     * fixed trait, and every numerology reference treats them as plain single digits.
     */
    fun personalYear(birthDate: SimpleDate, currentYear: Int): Int =
        PythagoreanCalculator.reduceFully(
            PythagoreanCalculator.digitSum(birthDate.day) +
                PythagoreanCalculator.digitSum(birthDate.month) +
                PythagoreanCalculator.digitSum(currentYear)
        )

    fun personalMonth(birthDate: SimpleDate, currentYear: Int, currentMonth: Int): Int =
        PythagoreanCalculator.reduceFully(personalYear(birthDate, currentYear) + currentMonth)

    /** Most frequent letter-value digit across the full name - the trait you lean on instinctively. */
    fun hiddenPassionNumber(fullName: String): Int {
        val values = PythagoreanCalculator.latinLetters(fullName)
            .map { PythagoreanCalculator.letterValue(it) }
            .filter { it > 0 }
        return values.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key ?: 0
    }

    /** Value of the very first letter of the name - the instinctive first move in a new situation. */
    fun cornerstoneNumber(fullName: String): Int {
        val first = PythagoreanCalculator.latinLetters(fullName).firstOrNull() ?: return 0
        return PythagoreanCalculator.letterValue(first)
    }

    /** Value of the very last letter of the name - how you tend to finish what you start. */
    fun capstoneNumber(fullName: String): Int {
        val last = PythagoreanCalculator.latinLetters(fullName).lastOrNull() ?: return 0
        return PythagoreanCalculator.letterValue(last)
    }

    /**
     * Karmic Debt numbers (13, 14, 16, 19) surface when a core number's *unreduced* running
     * total passes through one of these exact values on its way down to a single digit -
     * checked against birth date and each of the three name-derived totals (the standard set
     * of checkpoints), plus the standalone case of being born on the 13th/14th/16th/19th.
     */
    fun karmicDebtNumbers(profile: NumerologyProfile): List<Int> {
        val debtValues = setOf(13, 14, 16, 19)
        val found = mutableSetOf<Int>()

        if (profile.birthDate.day in debtValues) found += profile.birthDate.day

        val letters = PythagoreanCalculator.latinLetters(profile.fullName)
        val rawTotals = listOf(
            PythagoreanCalculator.digitSum(profile.birthDate.day) +
                PythagoreanCalculator.digitSum(profile.birthDate.month) +
                PythagoreanCalculator.digitSum(profile.birthDate.year),
            letters.sumOf { PythagoreanCalculator.letterValue(it) },
            letters.filter { PythagoreanCalculator.isVowel(it) }.sumOf { PythagoreanCalculator.letterValue(it) },
            letters.filter { !PythagoreanCalculator.isVowel(it) }.sumOf { PythagoreanCalculator.letterValue(it) }
        )

        for (total in rawTotals) {
            var current = total
            while (current > 9 && current != 11 && current != 22 && current != 33) {
                if (current in debtValues) found += current
                current = PythagoreanCalculator.digitSum(current)
            }
        }
        return found.sorted()
    }

    /** Digits 1-9 that never appear among the name's letter values - the traits you weren't
     *  handed by default and have to build deliberately. */
    fun karmicLessonNumbers(fullName: String): List<Int> {
        val present = PythagoreanCalculator.latinLetters(fullName)
            .map { PythagoreanCalculator.letterValue(it) }
            .toSet()
        return (1..9).filterNot { it in present }
    }
}
