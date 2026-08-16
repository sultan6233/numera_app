package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.CompatibilityResult
import com.numera.app.core.numerology.model.NumerologyProfile

object CompatibilityCalculator {
    fun compatibility(a: NumerologyProfile, b: NumerologyProfile): CompatibilityResult {
        val lifePathA = PythagoreanCalculator.lifePath(a.birthDate)
        val lifePathB = PythagoreanCalculator.lifePath(b.birthDate)
        val expressionA = PythagoreanCalculator.expression(a.fullName)
        val expressionB = PythagoreanCalculator.expression(b.fullName)

        val lifePathDiff = circularDistance(lifePathA, lifePathB)
        val expressionDiff = circularDistance(expressionA, expressionB)

        // Closer numbers (on the 1-9 wheel) score higher; life path is weighted more than
        // expression since it's considered the more stable/core trait in this system.
        val score = (100 - lifePathDiff * 8 - expressionDiff * 4).coerceIn(10, 100)

        val label = when {
            score >= 80 -> "Сильная совместимость"
            score >= 55 -> "Хорошая совместимость"
            else -> "Требует работы над отношениями"
        }

        return CompatibilityResult(score = score, label = label, lifePathA = lifePathA, lifePathB = lifePathB)
    }

    private fun circularDistance(a: Int, b: Int): Int {
        val diff = kotlin.math.abs(a - b)
        return minOf(diff, 9 - diff)
    }
}
