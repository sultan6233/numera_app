package com.numera.app.features.compatibility.presentation

import com.numera.app.core.numerology.model.CompatibilityBreakdownItem

sealed interface CompatibilityDetailState {
    data object Loading : CompatibilityDetailState
    data class Content(
        val companionName: String,
        val score: Int,
        val labelKey: String,
        val breakdown: List<CompatibilityBreakdownItem> = emptyList()
    ) : CompatibilityDetailState
}
