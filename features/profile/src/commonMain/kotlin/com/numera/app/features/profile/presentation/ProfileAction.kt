package com.numera.app.features.profile.presentation

sealed interface ProfileAction {
    data object OnRestoreClick : ProfileAction
    data object OnUpgradeClick : ProfileAction
    data class OnNotificationsToggled(val enabled: Boolean) : ProfileAction
}
