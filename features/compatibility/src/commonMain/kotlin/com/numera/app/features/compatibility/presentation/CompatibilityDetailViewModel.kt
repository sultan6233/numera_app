package com.numera.app.features.compatibility.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.CompatibilityCalculator
import com.numera.app.core.numerology.model.NumerologyProfile
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.compatibility.domain.CompanionRepository
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Inject
class CompatibilityDetailViewModel(
    private val repository: CompanionRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val _state = MutableStateFlow<CompatibilityDetailState>(CompatibilityDetailState.Loading)
    val state = _state.asStateFlow()

    fun load(companionId: String) = viewModelScope.launch {
        val companion = repository.findCompanion(companionId) ?: return@launch
        val me = NumerologyProfile(
            fullName = sessionStorage.fullName(),
            birthDate = SimpleDate(sessionStorage.birthYear(), sessionStorage.birthMonth(), sessionStorage.birthDay())
        )
        val other = NumerologyProfile(fullName = companion.name, birthDate = companion.birthDate)
        val result = CompatibilityCalculator.compatibility(me, other)
        _state.value = CompatibilityDetailState.Content(companion.name, result.score, result.label, result.breakdown)
    }
}
