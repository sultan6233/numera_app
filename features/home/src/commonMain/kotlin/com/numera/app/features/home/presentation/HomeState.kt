package com.numera.app.features.home.presentation

import androidx.compose.runtime.Stable
import com.numera.app.features.home.presentation.model.DailyInsightUi

sealed interface HomeState {
    @Stable
    data class Content(
        val userName: String = "",
        val insight: DailyInsightUi? = null,
        val loading: Boolean = true,
        val streak: Int = 1
    ) : HomeState
}
