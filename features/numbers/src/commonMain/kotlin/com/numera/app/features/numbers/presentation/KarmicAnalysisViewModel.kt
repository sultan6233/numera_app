package com.numera.app.features.numbers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.AdvancedNumerology
import com.numera.app.core.numerology.model.NumerologyProfile
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.numbers.domain.KarmicMeanings
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Inject
class KarmicAnalysisViewModel(private val sessionStorage: SessionStorage) : ViewModel() {

    private val _state = MutableStateFlow<KarmicAnalysisState>(KarmicAnalysisState.Loading)
    val state = _state.asStateFlow()

    fun load() = viewModelScope.launch {
        val profile = NumerologyProfile(
            fullName = sessionStorage.fullName(),
            birthDate = SimpleDate(sessionStorage.birthYear(), sessionStorage.birthMonth(), sessionStorage.birthDay())
        )
        val debts = AdvancedNumerology.karmicDebtNumbers(profile)
            .map { KarmicItemUi(it, KarmicMeanings.debtMeaning(it)) }
        val lessons = AdvancedNumerology.karmicLessonNumbers(profile.fullName)
            .map { KarmicItemUi(it, KarmicMeanings.lessonMeaning(it)) }
        _state.value = KarmicAnalysisState.Content(debts, lessons)
    }

    init { load() }
}
