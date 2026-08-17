package com.numera.app.features.numbers.domain

import org.jetbrains.compose.resources.getString
import numera.features.numbers.generated.resources.Res
import numera.features.numbers.generated.resources.personal_year_theme_1
import numera.features.numbers.generated.resources.personal_year_theme_2
import numera.features.numbers.generated.resources.personal_year_theme_3
import numera.features.numbers.generated.resources.personal_year_theme_4
import numera.features.numbers.generated.resources.personal_year_theme_5
import numera.features.numbers.generated.resources.personal_year_theme_6
import numera.features.numbers.generated.resources.personal_year_theme_7
import numera.features.numbers.generated.resources.personal_year_theme_8
import numera.features.numbers.generated.resources.personal_year_theme_9
import numera.features.numbers.generated.resources.personal_month_theme_1
import numera.features.numbers.generated.resources.personal_month_theme_2
import numera.features.numbers.generated.resources.personal_month_theme_3
import numera.features.numbers.generated.resources.personal_month_theme_4
import numera.features.numbers.generated.resources.personal_month_theme_5
import numera.features.numbers.generated.resources.personal_month_theme_6
import numera.features.numbers.generated.resources.personal_month_theme_7
import numera.features.numbers.generated.resources.personal_month_theme_8
import numera.features.numbers.generated.resources.personal_month_theme_9

/**
 * Personal Year/Month always reduce to a plain 1-9 digit (see AdvancedNumerology), so unlike
 * NumberMeanings there's no master-number case to cover here.
 */
object PersonalCycleMeanings {
    private val yearThemes = mapOf(
        1 to Res.string.personal_year_theme_1,
        2 to Res.string.personal_year_theme_2,
        3 to Res.string.personal_year_theme_3,
        4 to Res.string.personal_year_theme_4,
        5 to Res.string.personal_year_theme_5,
        6 to Res.string.personal_year_theme_6,
        7 to Res.string.personal_year_theme_7,
        8 to Res.string.personal_year_theme_8,
        9 to Res.string.personal_year_theme_9
    )

    private val monthThemes = mapOf(
        1 to Res.string.personal_month_theme_1,
        2 to Res.string.personal_month_theme_2,
        3 to Res.string.personal_month_theme_3,
        4 to Res.string.personal_month_theme_4,
        5 to Res.string.personal_month_theme_5,
        6 to Res.string.personal_month_theme_6,
        7 to Res.string.personal_month_theme_7,
        8 to Res.string.personal_month_theme_8,
        9 to Res.string.personal_month_theme_9
    )

    suspend fun yearTheme(personalYear: Int): String = getString(yearThemes[personalYear] ?: yearThemes.getValue(9))
    suspend fun monthTheme(personalMonth: Int): String = getString(monthThemes[personalMonth] ?: monthThemes.getValue(9))
}
