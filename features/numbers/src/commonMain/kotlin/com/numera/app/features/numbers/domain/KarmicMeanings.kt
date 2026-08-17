package com.numera.app.features.numbers.domain

import org.jetbrains.compose.resources.getString
import numera.features.numbers.generated.resources.Res
import numera.features.numbers.generated.resources.karmic_debt_13
import numera.features.numbers.generated.resources.karmic_debt_14
import numera.features.numbers.generated.resources.karmic_debt_16
import numera.features.numbers.generated.resources.karmic_debt_19
import numera.features.numbers.generated.resources.karmic_lesson_1
import numera.features.numbers.generated.resources.karmic_lesson_2
import numera.features.numbers.generated.resources.karmic_lesson_3
import numera.features.numbers.generated.resources.karmic_lesson_4
import numera.features.numbers.generated.resources.karmic_lesson_5
import numera.features.numbers.generated.resources.karmic_lesson_6
import numera.features.numbers.generated.resources.karmic_lesson_7
import numera.features.numbers.generated.resources.karmic_lesson_8
import numera.features.numbers.generated.resources.karmic_lesson_9

object KarmicMeanings {
    private val debtMeanings = mapOf(
        13 to Res.string.karmic_debt_13,
        14 to Res.string.karmic_debt_14,
        16 to Res.string.karmic_debt_16,
        19 to Res.string.karmic_debt_19
    )

    private val lessonMeanings = mapOf(
        1 to Res.string.karmic_lesson_1,
        2 to Res.string.karmic_lesson_2,
        3 to Res.string.karmic_lesson_3,
        4 to Res.string.karmic_lesson_4,
        5 to Res.string.karmic_lesson_5,
        6 to Res.string.karmic_lesson_6,
        7 to Res.string.karmic_lesson_7,
        8 to Res.string.karmic_lesson_8,
        9 to Res.string.karmic_lesson_9
    )

    suspend fun debtMeaning(debtNumber: Int): String = debtMeanings[debtNumber]?.let { getString(it) }.orEmpty()
    suspend fun lessonMeaning(number: Int): String = lessonMeanings[number]?.let { getString(it) }.orEmpty()
}
