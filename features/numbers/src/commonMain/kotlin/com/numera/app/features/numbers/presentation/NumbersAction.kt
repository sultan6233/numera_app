package com.numera.app.features.numbers.presentation

sealed interface NumbersAction {
    data object OnRefresh : NumbersAction
    data class OnCardClick(val numberType: String) : NumbersAction
    data object OnCompatibilityClick : NumbersAction
}
