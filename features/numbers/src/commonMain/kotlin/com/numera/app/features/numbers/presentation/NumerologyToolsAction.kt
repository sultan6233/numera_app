package com.numera.app.features.numbers.presentation

sealed interface NumerologyToolsAction {
    data class OnInputChanged(val value: String) : NumerologyToolsAction
    data class OnModeChanged(val mode: ToolMode) : NumerologyToolsAction
    data object OnCalculateClick : NumerologyToolsAction
}
