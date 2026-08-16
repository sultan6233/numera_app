package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.CompatibilityBreakdownItem
import com.numera.app.core.numerology.model.CompatibilityResult
import com.numera.app.core.numerology.model.NumerologyProfile

object CompatibilityCalculator {
    fun compatibility(a: NumerologyProfile, b: NumerologyProfile): CompatibilityResult {
        val lifePathA = PythagoreanCalculator.lifePath(a.birthDate)
        val lifePathB = PythagoreanCalculator.lifePath(b.birthDate)
        val expressionA = PythagoreanCalculator.expression(a.fullName)
        val expressionB = PythagoreanCalculator.expression(b.fullName)
        val soulUrgeA = PythagoreanCalculator.soulUrge(a.fullName)
        val soulUrgeB = PythagoreanCalculator.soulUrge(b.fullName)
        val personalityA = PythagoreanCalculator.personality(a.fullName)
        val personalityB = PythagoreanCalculator.personality(b.fullName)

        // Life path is weighted more than the rest since it's considered the more stable/core
        // trait in this system; soul urge and personality get an equal, lighter voice each.
        val lifePathScore = pairScore(lifePathA, lifePathB)
        val expressionScore = pairScore(expressionA, expressionB)
        val soulUrgeScore = pairScore(soulUrgeA, soulUrgeB)
        val personalityScore = pairScore(personalityA, personalityB)
        val overall = (lifePathScore * 4 + expressionScore * 2 + soulUrgeScore * 2 + personalityScore) / 9

        return CompatibilityResult(
            score = overall,
            label = labelFor(overall),
            lifePathA = lifePathA,
            lifePathB = lifePathB,
            breakdown = listOf(
                CompatibilityBreakdownItem("Жизненный путь", lifePathScore),
                CompatibilityBreakdownItem("Судьба", expressionScore),
                CompatibilityBreakdownItem("Душа", soulUrgeScore),
                CompatibilityBreakdownItem("Личность", personalityScore)
            )
        )
    }

    private fun pairScore(x: Int, y: Int): Int =
        (100 - circularDistance(wheelDigit(x), wheelDigit(y)) * 8).coerceIn(10, 100)

    private fun labelFor(score: Int): String = when {
        score >= 80 -> "Сильная совместимость"
        score >= 55 -> "Хорошая совместимость"
        else -> "Требует работы над отношениями"
    }

    /** Master numbers don't sit on the 1-9 compatibility wheel - collapse them to their base
     *  digit (11→2, 22→4, 33→6) only for this distance comparison. */
    private fun wheelDigit(n: Int): Int = when (n) {
        11 -> 2
        22 -> 4
        33 -> 6
        else -> n
    }

    private fun circularDistance(a: Int, b: Int): Int {
        val diff = kotlin.math.abs(a - b)
        return minOf(diff, 9 - diff)
    }
}
