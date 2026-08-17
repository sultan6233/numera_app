package com.numera.app.features.calendar.domain

import org.jetbrains.compose.resources.StringResource
import numera.features.calendar.generated.resources.Res
import numera.features.calendar.generated.resources.day_energy_1_title
import numera.features.calendar.generated.resources.day_energy_1_text
import numera.features.calendar.generated.resources.day_energy_2_title
import numera.features.calendar.generated.resources.day_energy_2_text
import numera.features.calendar.generated.resources.day_energy_3_title
import numera.features.calendar.generated.resources.day_energy_3_text
import numera.features.calendar.generated.resources.day_energy_4_title
import numera.features.calendar.generated.resources.day_energy_4_text
import numera.features.calendar.generated.resources.day_energy_5_title
import numera.features.calendar.generated.resources.day_energy_5_text
import numera.features.calendar.generated.resources.day_energy_6_title
import numera.features.calendar.generated.resources.day_energy_6_text
import numera.features.calendar.generated.resources.day_energy_7_title
import numera.features.calendar.generated.resources.day_energy_7_text
import numera.features.calendar.generated.resources.day_energy_8_title
import numera.features.calendar.generated.resources.day_energy_8_text
import numera.features.calendar.generated.resources.day_energy_9_title
import numera.features.calendar.generated.resources.day_energy_9_text
import numera.features.calendar.generated.resources.day_energy_11_title
import numera.features.calendar.generated.resources.day_energy_11_text
import numera.features.calendar.generated.resources.day_energy_22_title
import numera.features.calendar.generated.resources.day_energy_22_text
import numera.features.calendar.generated.resources.day_energy_33_title
import numera.features.calendar.generated.resources.day_energy_33_text

data class DayEnergy(val title: StringResource, val text: StringResource)

/**
 * Short, day-scoped flavor text for the personal day number - distinct from the identity-level
 * meanings on the Numbers tab (this describes what today asks of you, not who you are).
 */
object DayEnergyMeanings {
    private val meanings = mapOf(
        1 to DayEnergy(Res.string.day_energy_1_title, Res.string.day_energy_1_text),
        2 to DayEnergy(Res.string.day_energy_2_title, Res.string.day_energy_2_text),
        3 to DayEnergy(Res.string.day_energy_3_title, Res.string.day_energy_3_text),
        4 to DayEnergy(Res.string.day_energy_4_title, Res.string.day_energy_4_text),
        5 to DayEnergy(Res.string.day_energy_5_title, Res.string.day_energy_5_text),
        6 to DayEnergy(Res.string.day_energy_6_title, Res.string.day_energy_6_text),
        7 to DayEnergy(Res.string.day_energy_7_title, Res.string.day_energy_7_text),
        8 to DayEnergy(Res.string.day_energy_8_title, Res.string.day_energy_8_text),
        9 to DayEnergy(Res.string.day_energy_9_title, Res.string.day_energy_9_text),
        11 to DayEnergy(Res.string.day_energy_11_title, Res.string.day_energy_11_text),
        22 to DayEnergy(Res.string.day_energy_22_title, Res.string.day_energy_22_text),
        33 to DayEnergy(Res.string.day_energy_33_title, Res.string.day_energy_33_text)
    )

    fun meaningFor(personalDay: Int): DayEnergy = meanings[personalDay] ?: meanings.getValue(9)
}
