package com.numera.app.features.numbers.presentation

import androidx.lifecycle.ViewModel
import com.numera.app.core.numerology.NumerologyToolsCalculator
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Inject
class NumerologyToolsViewModel : ViewModel() {

    private val _state = MutableStateFlow(NumerologyToolsState())
    val state = _state.asStateFlow()

    fun onAction(action: NumerologyToolsAction) {
        when (action) {
            is NumerologyToolsAction.OnInputChanged -> _state.value = _state.value.copy(input = action.value, result = null)
            is NumerologyToolsAction.OnModeChanged -> _state.value = _state.value.copy(mode = action.mode, result = null)
            NumerologyToolsAction.OnCalculateClick -> calculate()
        }
    }

    private fun calculate() {
        val current = _state.value
        if (current.input.isBlank()) return
        val result = when (current.mode) {
            ToolMode.NAME -> NumerologyToolsCalculator.valueForText(current.input)
            ToolMode.DIGITS -> NumerologyToolsCalculator.valueForDigits(current.input)
        }
        _state.value = current.copy(result = result)
    }
}
