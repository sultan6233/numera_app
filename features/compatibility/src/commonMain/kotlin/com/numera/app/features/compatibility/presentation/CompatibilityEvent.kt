package com.numera.app.features.compatibility.presentation

sealed interface CompatibilityEvent {
    data class NavigateToDetail(val companionId: String) : CompatibilityEvent
}
