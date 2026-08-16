package com.numera.app.features.compatibility.presentation

sealed interface CompatibilityAction {
    data object OnAddClick : CompatibilityAction
    data object OnDismissDialog : CompatibilityAction
    data class OnNameChanged(val value: String) : CompatibilityAction
    data class OnDayChanged(val value: String) : CompatibilityAction
    data class OnMonthChanged(val value: String) : CompatibilityAction
    data class OnYearChanged(val value: String) : CompatibilityAction
    data object OnSaveClick : CompatibilityAction
    data class OnCompanionClick(val companionId: String) : CompatibilityAction
}
