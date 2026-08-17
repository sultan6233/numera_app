package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.NumerologyNumbers

object LuckyNumbers {
    fun luckyNumbers(numbers: NumerologyNumbers, personalYear: Int): List<Int> =
        listOf(numbers.lifePath, numbers.expression, personalYear).distinct()

    /** Stable, language-independent color keys - this module has no UI/resource layer, so
     *  resolving a key to a localized color name happens in the presentation layer. */
    private val colorKeys = mapOf(
        1 to "red", 2 to "orange", 3 to "yellow", 4 to "green", 5 to "blue",
        6 to "indigo", 7 to "violet", 8 to "brown", 9 to "gold",
        11 to "silver", 22 to "emerald", 33 to "white"
    )

    fun luckyColor(lifePath: Int): String = colorKeys[lifePath] ?: colorKeys.getValue(9)
}
