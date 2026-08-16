package com.numera.app.features.paywall.presentation

sealed interface PaywallAction {
    data class OnPlanSelected(val productId: String) : PaywallAction
    data object OnPurchaseClick : PaywallAction
    data object OnRestoreClick : PaywallAction
    data object OnSkipClick : PaywallAction
}
