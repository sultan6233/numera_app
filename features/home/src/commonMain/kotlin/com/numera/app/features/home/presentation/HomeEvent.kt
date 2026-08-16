package com.numera.app.features.home.presentation

sealed interface HomeEvent {
    data class ShowError(val error: String) : HomeEvent
}
