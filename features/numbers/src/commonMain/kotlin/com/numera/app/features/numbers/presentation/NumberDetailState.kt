package com.numera.app.features.numbers.presentation

import androidx.compose.runtime.Stable

sealed interface NumberDetailState {
    data object Loading : NumberDetailState

    @Stable
    data class Content(val title: String, val value: Int, val description: String) : NumberDetailState
}
