package com.numera.app.features.growth.domain

import org.jetbrains.compose.resources.getString
import numera.features.growth.generated.resources.Res
import numera.features.growth.generated.resources.habit_suggestion_1
import numera.features.growth.generated.resources.habit_suggestion_2
import numera.features.growth.generated.resources.habit_suggestion_3
import numera.features.growth.generated.resources.habit_suggestion_4
import numera.features.growth.generated.resources.habit_suggestion_5
import numera.features.growth.generated.resources.habit_suggestion_6
import numera.features.growth.generated.resources.habit_suggestion_7
import numera.features.growth.generated.resources.habit_suggestion_8
import numera.features.growth.generated.resources.habit_suggestion_9
import numera.features.growth.generated.resources.habit_suggestion_11
import numera.features.growth.generated.resources.habit_suggestion_22
import numera.features.growth.generated.resources.habit_suggestion_33

/** Short, habit-formation-framed suggestion keyed by today's personal day number - distinct
 *  from the calendar's day-energy text and the Numbers tab's identity meanings, since this one
 *  is written specifically around starting/keeping/skipping a habit today. */
object HabitSuggestions {
    private val suggestions = mapOf(
        1 to Res.string.habit_suggestion_1,
        2 to Res.string.habit_suggestion_2,
        3 to Res.string.habit_suggestion_3,
        4 to Res.string.habit_suggestion_4,
        5 to Res.string.habit_suggestion_5,
        6 to Res.string.habit_suggestion_6,
        7 to Res.string.habit_suggestion_7,
        8 to Res.string.habit_suggestion_8,
        9 to Res.string.habit_suggestion_9,
        11 to Res.string.habit_suggestion_11,
        22 to Res.string.habit_suggestion_22,
        33 to Res.string.habit_suggestion_33
    )

    suspend fun forPersonalDay(personalDay: Int): String =
        getString(suggestions[personalDay] ?: suggestions.getValue(9))
}
