package com.numera.app.features.home.data

import kotlinx.serialization.Serializable

@Serializable
data class DailyInsightResponse(
    val headline: String,
    val greeting: String,
    val body: List<String>,
    val affirmation: String,
    val luckyNumber: Int
)
