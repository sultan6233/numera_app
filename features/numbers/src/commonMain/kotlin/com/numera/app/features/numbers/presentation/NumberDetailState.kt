package com.numera.app.features.numbers.presentation

import androidx.compose.runtime.Stable
import com.numera.app.features.numbers.domain.NumberMeaning

sealed interface NumberDetailState {
    data object Loading : NumberDetailState

    @Stable
    data class Content(
        val categoryLabel: String,
        val value: Int,
        val meaning: NumberMeaning
    ) : NumberDetailState
}
