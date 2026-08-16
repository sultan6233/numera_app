package com.numera.app.features.compatibility.presentation

sealed interface CompatibilityDetailState {
    data object Loading : CompatibilityDetailState
    data class Content(val companionName: String, val score: Int, val label: String) : CompatibilityDetailState
}
