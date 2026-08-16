package com.numera.app.features.compatibility.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.compatibility.domain.CompanionRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Inject
class CompatibilityViewModel(private val repository: CompanionRepository) : ViewModel() {

    private val _state = MutableStateFlow(CompatibilityState.Content())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<CompatibilityEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: CompatibilityAction) {
        val content = state.value as? CompatibilityState.Content ?: return
        when (action) {
            CompatibilityAction.OnAddClick -> _state.value = content.copy(showAddDialog = true)
            CompatibilityAction.OnDismissDialog -> _state.value = content.copy(showAddDialog = false)
            is CompatibilityAction.OnNameChanged -> _state.value = content.copy(newName = action.value)
            is CompatibilityAction.OnDayChanged -> _state.value = content.copy(newDay = action.value.filter { it.isDigit() }.take(2))
            is CompatibilityAction.OnMonthChanged -> _state.value = content.copy(newMonth = action.value.filter { it.isDigit() }.take(2))
            is CompatibilityAction.OnYearChanged -> _state.value = content.copy(newYear = action.value.filter { it.isDigit() }.take(4))
            CompatibilityAction.OnSaveClick -> save(content)
            is CompatibilityAction.OnCompanionClick -> viewModelScope.launch {
                _event.emit(CompatibilityEvent.NavigateToDetail(action.companionId))
            }
            is CompatibilityAction.OnRemoveClick -> remove(action.companionId)
        }
    }

    private fun save(content: CompatibilityState.Content) = viewModelScope.launch {
        val day = content.newDay.toIntOrNull() ?: return@launch
        val month = content.newMonth.toIntOrNull() ?: return@launch
        val year = content.newYear.toIntOrNull() ?: return@launch
        if (content.newName.isBlank() || day !in 1..31 || month !in 1..12 || year !in 1900..2100) return@launch

        repository.addCompanion(content.newName, SimpleDate(year, month, day))
        _state.value = (state.value as CompatibilityState.Content).copy(
            companions = repository.getCompanions(),
            showAddDialog = false,
            newName = "",
            newDay = "",
            newMonth = "",
            newYear = ""
        )
    }

    private fun remove(id: String) = viewModelScope.launch {
        repository.removeCompanion(id)
        _state.value = (state.value as CompatibilityState.Content).copy(
            companions = (state.value as CompatibilityState.Content).companions.filter { it.id != id }
        )
    }

    private fun load() = viewModelScope.launch {
        _state.value = (state.value as CompatibilityState.Content).copy(
            companions = repository.getCompanions(),
            loading = false
        )
    }

    init { load() }
}
