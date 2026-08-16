package com.numera.app.features.home.data

import com.numera.app.features.home.presentation.model.DailyInsightUi

fun DailyInsightResponse.toUi(): DailyInsightUi = DailyInsightUi(
    headline = headline,
    greeting = greeting,
    body = body,
    affirmation = affirmation,
    luckyNumber = luckyNumber
)
