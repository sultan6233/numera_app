package com.numera.app.features.numbers.domain

import org.jetbrains.compose.resources.StringResource
import numera.features.numbers.generated.resources.Res
import numera.features.numbers.generated.resources.number_1_title
import numera.features.numbers.generated.resources.number_1_essence
import numera.features.numbers.generated.resources.number_1_strength_1
import numera.features.numbers.generated.resources.number_1_strength_2
import numera.features.numbers.generated.resources.number_1_strength_3
import numera.features.numbers.generated.resources.number_1_challenge_1
import numera.features.numbers.generated.resources.number_1_challenge_2
import numera.features.numbers.generated.resources.number_1_advice
import numera.features.numbers.generated.resources.number_2_title
import numera.features.numbers.generated.resources.number_2_essence
import numera.features.numbers.generated.resources.number_2_strength_1
import numera.features.numbers.generated.resources.number_2_strength_2
import numera.features.numbers.generated.resources.number_2_strength_3
import numera.features.numbers.generated.resources.number_2_challenge_1
import numera.features.numbers.generated.resources.number_2_challenge_2
import numera.features.numbers.generated.resources.number_2_advice
import numera.features.numbers.generated.resources.number_3_title
import numera.features.numbers.generated.resources.number_3_essence
import numera.features.numbers.generated.resources.number_3_strength_1
import numera.features.numbers.generated.resources.number_3_strength_2
import numera.features.numbers.generated.resources.number_3_strength_3
import numera.features.numbers.generated.resources.number_3_challenge_1
import numera.features.numbers.generated.resources.number_3_challenge_2
import numera.features.numbers.generated.resources.number_3_advice
import numera.features.numbers.generated.resources.number_4_title
import numera.features.numbers.generated.resources.number_4_essence
import numera.features.numbers.generated.resources.number_4_strength_1
import numera.features.numbers.generated.resources.number_4_strength_2
import numera.features.numbers.generated.resources.number_4_strength_3
import numera.features.numbers.generated.resources.number_4_challenge_1
import numera.features.numbers.generated.resources.number_4_challenge_2
import numera.features.numbers.generated.resources.number_4_advice
import numera.features.numbers.generated.resources.number_5_title
import numera.features.numbers.generated.resources.number_5_essence
import numera.features.numbers.generated.resources.number_5_strength_1
import numera.features.numbers.generated.resources.number_5_strength_2
import numera.features.numbers.generated.resources.number_5_strength_3
import numera.features.numbers.generated.resources.number_5_challenge_1
import numera.features.numbers.generated.resources.number_5_challenge_2
import numera.features.numbers.generated.resources.number_5_advice
import numera.features.numbers.generated.resources.number_6_title
import numera.features.numbers.generated.resources.number_6_essence
import numera.features.numbers.generated.resources.number_6_strength_1
import numera.features.numbers.generated.resources.number_6_strength_2
import numera.features.numbers.generated.resources.number_6_strength_3
import numera.features.numbers.generated.resources.number_6_challenge_1
import numera.features.numbers.generated.resources.number_6_challenge_2
import numera.features.numbers.generated.resources.number_6_advice
import numera.features.numbers.generated.resources.number_7_title
import numera.features.numbers.generated.resources.number_7_essence
import numera.features.numbers.generated.resources.number_7_strength_1
import numera.features.numbers.generated.resources.number_7_strength_2
import numera.features.numbers.generated.resources.number_7_strength_3
import numera.features.numbers.generated.resources.number_7_challenge_1
import numera.features.numbers.generated.resources.number_7_challenge_2
import numera.features.numbers.generated.resources.number_7_advice
import numera.features.numbers.generated.resources.number_8_title
import numera.features.numbers.generated.resources.number_8_essence
import numera.features.numbers.generated.resources.number_8_strength_1
import numera.features.numbers.generated.resources.number_8_strength_2
import numera.features.numbers.generated.resources.number_8_strength_3
import numera.features.numbers.generated.resources.number_8_challenge_1
import numera.features.numbers.generated.resources.number_8_challenge_2
import numera.features.numbers.generated.resources.number_8_advice
import numera.features.numbers.generated.resources.number_9_title
import numera.features.numbers.generated.resources.number_9_essence
import numera.features.numbers.generated.resources.number_9_strength_1
import numera.features.numbers.generated.resources.number_9_strength_2
import numera.features.numbers.generated.resources.number_9_strength_3
import numera.features.numbers.generated.resources.number_9_challenge_1
import numera.features.numbers.generated.resources.number_9_challenge_2
import numera.features.numbers.generated.resources.number_9_advice
import numera.features.numbers.generated.resources.number_11_title
import numera.features.numbers.generated.resources.number_11_essence
import numera.features.numbers.generated.resources.number_11_strength_1
import numera.features.numbers.generated.resources.number_11_strength_2
import numera.features.numbers.generated.resources.number_11_strength_3
import numera.features.numbers.generated.resources.number_11_challenge_1
import numera.features.numbers.generated.resources.number_11_challenge_2
import numera.features.numbers.generated.resources.number_11_advice
import numera.features.numbers.generated.resources.number_22_title
import numera.features.numbers.generated.resources.number_22_essence
import numera.features.numbers.generated.resources.number_22_strength_1
import numera.features.numbers.generated.resources.number_22_strength_2
import numera.features.numbers.generated.resources.number_22_strength_3
import numera.features.numbers.generated.resources.number_22_challenge_1
import numera.features.numbers.generated.resources.number_22_challenge_2
import numera.features.numbers.generated.resources.number_22_advice
import numera.features.numbers.generated.resources.number_33_title
import numera.features.numbers.generated.resources.number_33_essence
import numera.features.numbers.generated.resources.number_33_strength_1
import numera.features.numbers.generated.resources.number_33_strength_2
import numera.features.numbers.generated.resources.number_33_strength_3
import numera.features.numbers.generated.resources.number_33_challenge_1
import numera.features.numbers.generated.resources.number_33_challenge_2
import numera.features.numbers.generated.resources.number_33_advice

/**
 * Static reference text - the standardized part of Pythagorean numerology (the archetypal
 * meaning of each digit is centuries-old public tradition), written originally for this app
 * rather than pulled from any one source. Each entry mirrors the depth of a real numerology
 * reference (essence + strengths + challenges + practical advice), not a one-line label.
 *
 * Fields hold StringResource ids rather than resolved strings - meaningFor() is called from
 * plain (non-@Composable) call sites, so resolution to localized text happens later, wherever
 * the value is actually rendered, via stringResource().
 */
data class NumberMeaning(
    val title: StringResource,
    val essence: StringResource,
    val strengths: List<StringResource>,
    val challenges: List<StringResource>,
    val advice: StringResource
)

object NumberMeanings {
    private val meanings = mapOf(
        1 to NumberMeaning(
            title = Res.string.number_1_title,
            essence = Res.string.number_1_essence,
            strengths = listOf(Res.string.number_1_strength_1, Res.string.number_1_strength_2, Res.string.number_1_strength_3),
            challenges = listOf(Res.string.number_1_challenge_1, Res.string.number_1_challenge_2),
            advice = Res.string.number_1_advice
        ),
        2 to NumberMeaning(
            title = Res.string.number_2_title,
            essence = Res.string.number_2_essence,
            strengths = listOf(Res.string.number_2_strength_1, Res.string.number_2_strength_2, Res.string.number_2_strength_3),
            challenges = listOf(Res.string.number_2_challenge_1, Res.string.number_2_challenge_2),
            advice = Res.string.number_2_advice
        ),
        3 to NumberMeaning(
            title = Res.string.number_3_title,
            essence = Res.string.number_3_essence,
            strengths = listOf(Res.string.number_3_strength_1, Res.string.number_3_strength_2, Res.string.number_3_strength_3),
            challenges = listOf(Res.string.number_3_challenge_1, Res.string.number_3_challenge_2),
            advice = Res.string.number_3_advice
        ),
        4 to NumberMeaning(
            title = Res.string.number_4_title,
            essence = Res.string.number_4_essence,
            strengths = listOf(Res.string.number_4_strength_1, Res.string.number_4_strength_2, Res.string.number_4_strength_3),
            challenges = listOf(Res.string.number_4_challenge_1, Res.string.number_4_challenge_2),
            advice = Res.string.number_4_advice
        ),
        5 to NumberMeaning(
            title = Res.string.number_5_title,
            essence = Res.string.number_5_essence,
            strengths = listOf(Res.string.number_5_strength_1, Res.string.number_5_strength_2, Res.string.number_5_strength_3),
            challenges = listOf(Res.string.number_5_challenge_1, Res.string.number_5_challenge_2),
            advice = Res.string.number_5_advice
        ),
        6 to NumberMeaning(
            title = Res.string.number_6_title,
            essence = Res.string.number_6_essence,
            strengths = listOf(Res.string.number_6_strength_1, Res.string.number_6_strength_2, Res.string.number_6_strength_3),
            challenges = listOf(Res.string.number_6_challenge_1, Res.string.number_6_challenge_2),
            advice = Res.string.number_6_advice
        ),
        7 to NumberMeaning(
            title = Res.string.number_7_title,
            essence = Res.string.number_7_essence,
            strengths = listOf(Res.string.number_7_strength_1, Res.string.number_7_strength_2, Res.string.number_7_strength_3),
            challenges = listOf(Res.string.number_7_challenge_1, Res.string.number_7_challenge_2),
            advice = Res.string.number_7_advice
        ),
        8 to NumberMeaning(
            title = Res.string.number_8_title,
            essence = Res.string.number_8_essence,
            strengths = listOf(Res.string.number_8_strength_1, Res.string.number_8_strength_2, Res.string.number_8_strength_3),
            challenges = listOf(Res.string.number_8_challenge_1, Res.string.number_8_challenge_2),
            advice = Res.string.number_8_advice
        ),
        9 to NumberMeaning(
            title = Res.string.number_9_title,
            essence = Res.string.number_9_essence,
            strengths = listOf(Res.string.number_9_strength_1, Res.string.number_9_strength_2, Res.string.number_9_strength_3),
            challenges = listOf(Res.string.number_9_challenge_1, Res.string.number_9_challenge_2),
            advice = Res.string.number_9_advice
        ),
        11 to NumberMeaning(
            title = Res.string.number_11_title,
            essence = Res.string.number_11_essence,
            strengths = listOf(Res.string.number_11_strength_1, Res.string.number_11_strength_2, Res.string.number_11_strength_3),
            challenges = listOf(Res.string.number_11_challenge_1, Res.string.number_11_challenge_2),
            advice = Res.string.number_11_advice
        ),
        22 to NumberMeaning(
            title = Res.string.number_22_title,
            essence = Res.string.number_22_essence,
            strengths = listOf(Res.string.number_22_strength_1, Res.string.number_22_strength_2, Res.string.number_22_strength_3),
            challenges = listOf(Res.string.number_22_challenge_1, Res.string.number_22_challenge_2),
            advice = Res.string.number_22_advice
        ),
        33 to NumberMeaning(
            title = Res.string.number_33_title,
            essence = Res.string.number_33_essence,
            strengths = listOf(Res.string.number_33_strength_1, Res.string.number_33_strength_2, Res.string.number_33_strength_3),
            challenges = listOf(Res.string.number_33_challenge_1, Res.string.number_33_challenge_2),
            advice = Res.string.number_33_advice
        )
    )

    fun meaningFor(value: Int): NumberMeaning = meanings[value] ?: meanings.getValue(9)
}
