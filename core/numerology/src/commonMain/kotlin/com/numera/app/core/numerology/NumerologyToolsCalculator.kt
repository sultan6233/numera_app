package com.numera.app.core.numerology

/**
 * Generic Pythagorean reduction for arbitrary text/digits (business name, address, phone
 * number) - the same two tables the personal-name and birth-date calculators use, just applied
 * to whatever string the user types instead of their profile.
 */
object NumerologyToolsCalculator {
    /** For names/words: business name, street name, brand. */
    fun valueForText(text: String): Int =
        PythagoreanCalculator.reduceToSingleDigit(
            PythagoreanCalculator.latinLetters(text).sumOf { PythagoreanCalculator.letterValue(it) }
        )

    /** For numbers: phone number, house number, apartment number. */
    fun valueForDigits(text: String): Int =
        PythagoreanCalculator.reduceToSingleDigit(text.filter { it.isDigit() }.sumOf { it.digitToInt() })
}
