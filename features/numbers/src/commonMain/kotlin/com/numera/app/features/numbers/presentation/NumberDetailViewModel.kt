package com.numera.app.features.numbers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.AdvancedNumerology
import com.numera.app.core.numerology.PythagoreanCalculator
import com.numera.app.core.numerology.model.NumerologyProfile
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.numbers.domain.NumberMeanings
import com.numera.app.features.numbers.presentation.model.NumberType
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import numera.features.numbers.generated.resources.Res
import numera.features.numbers.generated.resources.number_life_path
import numera.features.numbers.generated.resources.number_expression
import numera.features.numbers.generated.resources.number_soul_urge
import numera.features.numbers.generated.resources.number_personality
import numera.features.numbers.generated.resources.number_health
import numera.features.numbers.generated.resources.number_business
import numera.features.numbers.generated.resources.number_maturity
import numera.features.numbers.generated.resources.number_hidden_passion
import numera.features.numbers.generated.resources.number_cornerstone
import numera.features.numbers.generated.resources.number_capstone
import numera.features.numbers.generated.resources.number_generic

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
            NumberType.LIFE_PATH -> getString(Res.string.number_life_path) to numbers.lifePath
            NumberType.EXPRESSION -> getString(Res.string.number_expression) to numbers.expression
            NumberType.SOUL_URGE -> getString(Res.string.number_soul_urge) to numbers.soulUrge
            NumberType.PERSONALITY -> getString(Res.string.number_personality) to numbers.personality
            NumberType.HEALTH -> getString(Res.string.number_health) to numbers.healthCode
            NumberType.BUSINESS -> getString(Res.string.number_business) to numbers.businessCode
            NumberType.MATURITY -> getString(Res.string.number_maturity) to AdvancedNumerology.maturityNumber(profile)
            NumberType.HIDDEN_PASSION -> getString(Res.string.number_hidden_passion) to AdvancedNumerology.hiddenPassionNumber(profile.fullName)
            NumberType.CORNERSTONE -> getString(Res.string.number_cornerstone) to AdvancedNumerology.cornerstoneNumber(profile.fullName)
            NumberType.CAPSTONE -> getString(Res.string.number_capstone) to AdvancedNumerology.capstoneNumber(profile.fullName)
            else -> getString(Res.string.number_generic) to numbers.lifePath
        }
        _state.value = NumberDetailState.Content(categoryLabel, value, NumberMeanings.meaningFor(value))
    }
}
