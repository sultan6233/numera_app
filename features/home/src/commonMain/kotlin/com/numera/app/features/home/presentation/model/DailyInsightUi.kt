package com.numera.app.features.home.presentation.model

data class DailyInsightUi(
    val headline: String,
    val greeting: String,
    val body: List<String>,
    val affirmation: String,
    val luckyNumber: Int
)
