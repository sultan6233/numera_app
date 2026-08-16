package com.numera.app.features.home.domain

import com.numera.app.features.home.presentation.model.DailyInsightUi

interface HomeRepository {
    suspend fun getDailyInsight(date: String): Result<DailyInsightUi>
}
