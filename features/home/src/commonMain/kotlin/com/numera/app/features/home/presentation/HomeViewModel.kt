package com.numera.app.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.PersonalDayCalculator
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.core.numerology.todaySimpleDate
import com.numera.app.features.home.domain.HomeRepository
import com.numera.app.features.home.presentation.model.DailyInsightUi
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Inject
class HomeViewModel(
    private val repository: HomeRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState.Content())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<HomeEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRefresh -> load()
        }
    }

    private fun load() = viewModelScope.launch {
        _state.value = state.value.copy(loading = true)
        val today = todaySimpleDate()
        val dateKey = "${today.year}-${today.month.toString().padStart(2, '0')}-${today.day.toString().padStart(2, '0')}"
        val result = repository.getDailyInsight(dateKey)
        result.onSuccess {
            _state.value = state.value.copy(insight = it, loading = false, userName = sessionStorage.fullName())
        }
        result.onFailure {
            // Backend unreachable (or not deployed yet) - fall back to a locally computed insight
            // so the screen is never empty; the Personal Day Number is real numerology math, not
            // a placeholder.
            _state.value = state.value.copy(
                insight = fallbackInsight(today),
                loading = false,
                userName = sessionStorage.fullName()
            )
        }
    }

    private fun fallbackInsight(today: SimpleDate): DailyInsightUi {
        val birthDate = SimpleDate(sessionStorage.birthYear(), sessionStorage.birthMonth(), sessionStorage.birthDay())
        val personalDay = PersonalDayCalculator.personalDay(birthDate, today)
        return DailyInsightUi(
            headline = "Личный день: $personalDay",
            greeting = "Доброе утро",
            body = listOf(
                "Сегодня ваш личный день — $personalDay. Прислушайтесь к своим ощущениям и не спешите с крупными решениями.",
                "Хороший день, чтобы навести порядок в делах и уделить немного времени себе."
            ),
            affirmation = "Я доверяю своему пути и двигаюсь в своём темпе.",
            luckyNumber = personalDay
        )
    }

    init { load() }
}
