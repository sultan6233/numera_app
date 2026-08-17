package com.numera.app.features.numbers.domain

import org.jetbrains.compose.resources.getString
import numera.features.numbers.generated.resources.Res
import numera.features.numbers.generated.resources.pinnacle_theme_0
import numera.features.numbers.generated.resources.pinnacle_theme_1
import numera.features.numbers.generated.resources.pinnacle_theme_2
import numera.features.numbers.generated.resources.pinnacle_theme_3
import numera.features.numbers.generated.resources.pinnacle_theme_4
import numera.features.numbers.generated.resources.pinnacle_theme_5
import numera.features.numbers.generated.resources.pinnacle_theme_6
import numera.features.numbers.generated.resources.pinnacle_theme_7
import numera.features.numbers.generated.resources.pinnacle_theme_8
import numera.features.numbers.generated.resources.pinnacle_theme_9
import numera.features.numbers.generated.resources.challenge_theme_0
import numera.features.numbers.generated.resources.challenge_theme_1
import numera.features.numbers.generated.resources.challenge_theme_2
import numera.features.numbers.generated.resources.challenge_theme_3
import numera.features.numbers.generated.resources.challenge_theme_4
import numera.features.numbers.generated.resources.challenge_theme_5
import numera.features.numbers.generated.resources.challenge_theme_6
import numera.features.numbers.generated.resources.challenge_theme_7
import numera.features.numbers.generated.resources.challenge_theme_8
import numera.features.numbers.generated.resources.challenge_theme_9

/** Short opportunity/obstacle framing for Pinnacle and Challenge numbers - distinct from
 *  NumberMeanings, which describes identity traits rather than a life stage's theme. */
object LifeCycleMeanings {
    private val pinnacleThemes = mapOf(
        0 to Res.string.pinnacle_theme_0,
        1 to Res.string.pinnacle_theme_1,
        2 to Res.string.pinnacle_theme_2,
        3 to Res.string.pinnacle_theme_3,
        4 to Res.string.pinnacle_theme_4,
        5 to Res.string.pinnacle_theme_5,
        6 to Res.string.pinnacle_theme_6,
        7 to Res.string.pinnacle_theme_7,
        8 to Res.string.pinnacle_theme_8,
        9 to Res.string.pinnacle_theme_9
    )

    private val challengeThemes = mapOf(
        0 to Res.string.challenge_theme_0,
        1 to Res.string.challenge_theme_1,
        2 to Res.string.challenge_theme_2,
        3 to Res.string.challenge_theme_3,
        4 to Res.string.challenge_theme_4,
        5 to Res.string.challenge_theme_5,
        6 to Res.string.challenge_theme_6,
        7 to Res.string.challenge_theme_7,
        8 to Res.string.challenge_theme_8,
        9 to Res.string.challenge_theme_9
    )

    suspend fun pinnacleTheme(number: Int): String = getString(pinnacleThemes[number] ?: pinnacleThemes.getValue(0))
    suspend fun challengeTheme(number: Int): String = getString(challengeThemes[number] ?: challengeThemes.getValue(0))
}
