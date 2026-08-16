package com.numera.app.features.paywall.presentation

sealed interface PaywallEvent {
    data object NavigateToHome : PaywallEvent
    data class ShowError(val error: String) : PaywallEvent
}
