package com.numera.app.features.profile.presentation

import androidx.compose.runtime.Stable

sealed interface ProfileState {
    @Stable
    data class Content(
        val name: String = "",
        val birthDateText: String = "",
        val subscribed: Boolean = false,
        val notificationsEnabled: Boolean = true,
        val isRestoring: Boolean = false
    ) : ProfileState
}
