package com.numera.app.features.profile.presentation

sealed interface ProfileEvent {
    data object NavigateToPaywall : ProfileEvent
    data class ShowMessage(val message: String) : ProfileEvent
}
