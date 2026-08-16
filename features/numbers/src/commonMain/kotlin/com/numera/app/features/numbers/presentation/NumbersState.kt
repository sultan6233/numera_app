package com.numera.app.features.numbers.presentation

import androidx.compose.runtime.Stable
import com.numera.app.features.numbers.presentation.model.NumberCardUi

sealed interface NumbersState {
    @Stable
    data class Content(
        val cards: List<NumberCardUi> = emptyList(),
        val loading: Boolean = true
    ) : NumbersState
}
