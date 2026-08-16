package com.numera.app.core.numerology

import com.numera.app.core.numerology.model.SimpleDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PinnacleCalculatorTest {

    @Test
    fun pinnacles_returnsFourStagesWithAnOpenEndedLastOne() {
        val stages = PinnacleCalculator.pinnacles(SimpleDate(1988, 3, 14), lifePath = 7)
        assertEquals(4, stages.size)
        assertEquals(0, stages.first().startAge)
        assertNull(stages.last().endAge)
    }

    @Test
    fun pinnacles_stagesAreContiguous() {
        val stages = PinnacleCalculator.pinnacles(SimpleDate(1988, 3, 14), lifePath = 7)
        for (i in 0 until stages.size - 1) {
            assertEquals(stages[i].endAge!! + 1, stages[i + 1].startAge)
        }
    }

    @Test
    fun challenges_neverNegative() {
        val stages = PinnacleCalculator.challenges(SimpleDate(1988, 3, 14), lifePath = 7)
        assertTrue(stages.all { it.number >= 0 })
    }
}
