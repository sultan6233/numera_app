package com.numera.app.features.compatibility.presentation

import androidx.compose.runtime.Stable
import com.numera.app.features.compatibility.presentation.model.CompanionUi

sealed interface CompatibilityState {
    @Stable
    data class Content(
        val companions: List<CompanionUi> = emptyList(),
        val loading: Boolean = true,
        val showAddDialog: Boolean = false,
        val newName: String = "",
        val newDay: String = "",
        val newMonth: String = "",
        val newYear: String = ""
    ) : CompatibilityState
}
