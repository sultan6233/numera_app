package com.numera.app.features.growth.domain

import org.jetbrains.compose.resources.StringResource
import numera.features.growth.generated.resources.Res
import numera.features.growth.generated.resources.tag_work
import numera.features.growth.generated.resources.tag_relationships
import numera.features.growth.generated.resources.tag_health
import numera.features.growth.generated.resources.tag_sleep
import numera.features.growth.generated.resources.tag_money
import numera.features.growth.generated.resources.tag_weather
import numera.features.growth.generated.resources.tag_sport
import numera.features.growth.generated.resources.tag_rest

data class MoodTag(val id: String, val labelRes: StringResource)

/** A fixed set of common mood-influence categories - quicker to tap than typing a note every
 *  time, and gives the free-text note something to add detail to rather than carry everything.
 *
 *  [MoodTag.id] is a stable, language-independent key persisted in mood records - the display
 *  label is resolved from [MoodTag.labelRes] at render time, so a tag chosen under one locale
 *  still matches correctly (and displays translated) after the user switches app language. */
object MoodTags {
    val ALL = listOf(
        MoodTag("work", Res.string.tag_work),
        MoodTag("relationships", Res.string.tag_relationships),
        MoodTag("health", Res.string.tag_health),
        MoodTag("sleep", Res.string.tag_sleep),
        MoodTag("money", Res.string.tag_money),
        MoodTag("weather", Res.string.tag_weather),
        MoodTag("sport", Res.string.tag_sport),
        MoodTag("rest", Res.string.tag_rest)
    )

    fun labelFor(id: String): StringResource = ALL.find { it.id == id }?.labelRes ?: Res.string.tag_work
}
