package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.NumerologyNumbers
import com.numera.app.core.numerology.model.NumerologyProfile
import com.numera.app.core.numerology.model.SimpleDate

/**
 * Standard Western (Pythagorean) numerology - the same convention used by the large majority
 * of numerology apps and reference books. Pure arithmetic, no I/O, no platform dependencies.
 */
object PythagoreanCalculator {

    fun lifePath(birthDate: SimpleDate): Int =
        reduceToSingleDigit(digitSum(birthDate.day) + digitSum(birthDate.month) + digitSum(birthDate.year))

    fun expression(fullName: String): Int =
        reduceToSingleDigit(fullName.filter { it.isLetter() }.sumOf { letterValue(it) })

    fun soulUrge(fullName: String): Int =
        reduceToSingleDigit(fullName.filter { it.isLetter() && isVowel(it) }.sumOf { letterValue(it) })

    fun personality(fullName: String): Int =
        reduceToSingleDigit(fullName.filter { it.isLetter() && !isVowel(it) }.sumOf { letterValue(it) })

    fun birthDayNumber(birthDate: SimpleDate): Int = reduceToSingleDigit(birthDate.day)

    /**
     * Health/business "codes" aren't a standardized branch of Pythagorean numerology the way
     * life path/expression are - this is a simplified derived formula (combining the numbers
     * that are standardized) rather than a claim of a separate canonical system.
     */
    fun healthCode(profile: NumerologyProfile): Int =
        reduceToSingleDigit(lifePath(profile.birthDate) + expression(profile.fullName))

    fun businessCode(profile: NumerologyProfile): Int =
        reduceToSingleDigit(expression(profile.fullName) + personality(profile.fullName))

    fun allNumbers(profile: NumerologyProfile): NumerologyNumbers = NumerologyNumbers(
        lifePath = lifePath(profile.birthDate),
        expression = expression(profile.fullName),
        soulUrge = soulUrge(profile.fullName),
        personality = personality(profile.fullName),
        birthDay = birthDayNumber(profile.birthDate),
        healthCode = healthCode(profile),
        businessCode = businessCode(profile)
    )

    private fun isVowel(c: Char): Boolean = c.uppercaseChar() in "AEIOU"

    /** A=1..I=9, J=1..R=9, S=1..Z=8 - the standard Pythagorean letter-to-number table. */
    private fun letterValue(c: Char): Int {
        val upper = c.uppercaseChar()
        if (upper !in 'A'..'Z') return 0
        return (upper - 'A') % 9 + 1
    }

    private fun digitSum(n: Int): Int = n.toString().filter { it.isDigit() }.sumOf { it.digitToInt() }

    /** Master numbers (11, 22, 33) are kept unreduced - standard Pythagorean convention. */
    internal fun reduceToSingleDigit(input: Int): Int {
        var n = input
        while (n > 9 && n != 11 && n != 22 && n != 33) {
            n = digitSum(n)
        }
        return n
    }
}
