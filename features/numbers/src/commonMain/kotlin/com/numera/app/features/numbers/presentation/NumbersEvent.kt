package com.numera.app.features.numbers.presentation

sealed interface NumbersEvent {
    data class NavigateToDetail(val numberType: String) : NumbersEvent
    data object NavigateToPaywall : NumbersEvent
    data object NavigateToCompatibility : NumbersEvent
}
