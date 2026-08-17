package com.numera.app.features.numbers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.PinnacleCalculator
import com.numera.app.core.numerology.PythagoreanCalculator
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.numbers.domain.LifeCycleMeanings
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import numera.features.numbers.generated.resources.Res
import numera.features.numbers.generated.resources.age_and_older
import numera.features.numbers.generated.resources.age_range

@Inject
class LifeCyclesViewModel(private val sessionStorage: SessionStorage) : ViewModel() {

    private val _state = MutableStateFlow<LifeCyclesState>(LifeCyclesState.Loading)
    val state = _state.asStateFlow()

    fun load() = viewModelScope.launch {
        val birthDate = SimpleDate(sessionStorage.birthYear(), sessionStorage.birthMonth(), sessionStorage.birthDay())
        val lifePath = PythagoreanCalculator.lifePath(birthDate)
        val pinnacles = PinnacleCalculator.pinnacles(birthDate, lifePath)
        val challenges = PinnacleCalculator.challenges(birthDate, lifePath)

        val stages = pinnacles.zip(challenges).map { (pinnacle, challenge) ->
            LifeStageUi(
                ageRangeLabel = ageRangeLabel(pinnacle.startAge, pinnacle.endAge),
                pinnacleNumber = pinnacle.number,
                pinnacleText = LifeCycleMeanings.pinnacleTheme(pinnacle.number),
                challengeNumber = challenge.number,
                challengeText = LifeCycleMeanings.challengeTheme(challenge.number)
            )
        }
        _state.value = LifeCyclesState.Content(stages)
    }

    private suspend fun ageRangeLabel(start: Int, end: Int?): String =
        if (end == null) getString(Res.string.age_and_older, start) else getString(Res.string.age_range, start, end)

    init { load() }
}
