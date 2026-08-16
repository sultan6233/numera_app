package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.LifeCycle
import com.numera.app.core.numerology.model.SimpleDate

/**
 * The four Pinnacles (opportunities) and four Challenges (obstacles) that structure a life in
 * Pythagorean numerology. Each stage covers roughly a nine-year age band; the first band's
 * length is set by the Life Path number and the fourth band is open-ended (lasts the rest of
 * life).
 */
object PinnacleCalculator {

    fun pinnacles(birthDate: SimpleDate, lifePath: Int): List<LifeCycle> {
        val month = PythagoreanCalculator.reduceFully(birthDate.month)
        val day = PythagoreanCalculator.reduceFully(birthDate.day)
        val year = PythagoreanCalculator.reduceFully(PythagoreanCalculator.digitSum(birthDate.year))

        val first = PythagoreanCalculator.reduceFully(month + day)
        val second = PythagoreanCalculator.reduceFully(day + year)
        val third = PythagoreanCalculator.reduceFully(first + second)
        val fourth = PythagoreanCalculator.reduceFully(month + year)

        return stagesFor(lifePath, first, second, third, fourth)
    }

    fun challenges(birthDate: SimpleDate, lifePath: Int): List<LifeCycle> {
        val month = PythagoreanCalculator.reduceFully(birthDate.month)
        val day = PythagoreanCalculator.reduceFully(birthDate.day)
        val year = PythagoreanCalculator.reduceFully(PythagoreanCalculator.digitSum(birthDate.year))

        val first = kotlin.math.abs(month - day)
        val second = kotlin.math.abs(day - year)
        val third = kotlin.math.abs(first - second)
        val fourth = kotlin.math.abs(month - year)

        return stagesFor(lifePath, first, second, third, fourth)
    }

    private fun stagesFor(lifePath: Int, first: Int, second: Int, third: Int, fourth: Int): List<LifeCycle> {
        val firstEnd = 36 - lifePath
        return listOf(
            LifeCycle(first, 0, firstEnd),
            LifeCycle(second, firstEnd + 1, firstEnd + 9),
            LifeCycle(third, firstEnd + 10, firstEnd + 18),
            LifeCycle(fourth, firstEnd + 19, null)
        )
    }
}
