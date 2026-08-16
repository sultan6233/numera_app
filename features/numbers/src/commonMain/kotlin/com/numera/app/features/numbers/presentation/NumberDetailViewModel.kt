package com.numera.app.features.numbers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.PythagoreanCalculator
import com.numera.app.core.numerology.model.NumerologyProfile
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.numbers.domain.NumberMeanings
import com.numera.app.features.numbers.presentation.model.NumberType
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Inject
class NumberDetailViewModel(private val sessionStorage: SessionStorage) : ViewModel() {

    private val _state = MutableStateFlow<NumberDetailState>(NumberDetailState.Loading)
    val state = _state.asStateFlow()

    fun load(numberType: String) = viewModelScope.launch {
        val profile = NumerologyProfile(
            fullName = sessionStorage.fullName(),
            birthDate = SimpleDate(sessionStorage.birthYear(), sessionStorage.birthMonth(), sessionStorage.birthDay())
        )
        val numbers = PythagoreanCalculator.allNumbers(profile)
        val (categoryLabel, value) = when (numberType) {
            NumberType.LIFE_PATH -> "Число жизненного пути" to numbers.lifePath
            NumberType.EXPRESSION -> "Число судьбы" to numbers.expression
            NumberType.SOUL_URGE -> "Число души" to numbers.soulUrge
            NumberType.PERSONALITY -> "Число личности" to numbers.personality
            NumberType.HEALTH -> "Код здоровья" to numbers.healthCode
            NumberType.BUSINESS -> "Код бизнеса" to numbers.businessCode
            else -> "Число" to numbers.lifePath
        }
        _state.value = NumberDetailState.Content(categoryLabel, value, NumberMeanings.meaningFor(value))
    }
}
