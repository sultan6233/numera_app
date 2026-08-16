package com.numera.app.features.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.commonMain.deviceTimezoneId
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.PythagoreanCalculator
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.features.onboarding.data.ProfileApi
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Inject
class OnboardingViewModel(
    private val sessionStorage: SessionStorage,
    private val profileApi: ProfileApi
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState.Content())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<OnboardingEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: OnboardingAction) {
        val content = state.value as? OnboardingState.Content ?: return
        when (action) {
            is OnboardingAction.OnNameChanged -> _state.value = content.copy(name = action.value)
            OnboardingAction.OnNameNext -> {
                if (content.name.isBlank()) return
                _state.value = content.copy(step = OnboardingStep.BIRTH_DATE)
            }
            is OnboardingAction.OnDayChanged -> _state.value = content.copy(day = action.value.filter { it.isDigit() }.take(2))
            is OnboardingAction.OnMonthChanged -> _state.value = content.copy(month = action.value.filter { it.isDigit() }.take(2))
            is OnboardingAction.OnYearChanged -> _state.value = content.copy(year = action.value.filter { it.isDigit() }.take(4))
            OnboardingAction.OnBirthDateNext -> startCalculating(content)
            OnboardingAction.OnContinueClick -> finishOnboarding(content)
        }
    }

    private fun startCalculating(content: OnboardingState.Content) {
        val day = content.day.toIntOrNull() ?: return
        val month = content.month.toIntOrNull() ?: return
        val year = content.year.toIntOrNull() ?: return
        if (day !in 1..31 || month !in 1..12 || year !in 1900..2100) return

        _state.value = content.copy(step = OnboardingStep.CALCULATING)
        viewModelScope.launch {
            delay(CALCULATING_DELAY_MS)
            val lifePath = PythagoreanCalculator.lifePath(SimpleDate(year, month, day))
            _state.value = (state.value as OnboardingState.Content).copy(
                step = OnboardingStep.TEASER,
                lifePathPreview = lifePath
            )
        }
    }

    private fun finishOnboarding(content: OnboardingState.Content) {
        val birthYear = content.year.toIntOrNull() ?: 2000
        val birthMonth = content.month.toIntOrNull() ?: 1
        val birthDay = content.day.toIntOrNull() ?: 1
        sessionStorage.saveProfile(content.name, birthYear, birthMonth, birthDay)

        viewModelScope.launch {
            // Local profile is already saved above - the app works fully offline either way, so
            // a failed sync here (no connectivity, backend hiccup) shouldn't block onboarding.
            try {
                val isoDate = "$birthYear-${birthMonth.pad2()}-${birthDay.pad2()}"
                profileApi.updateProfile(content.name, isoDate, deviceTimezoneId())
            } catch (e: Exception) {
                // Daily insight falls back to the local calculation (see HomeViewModel) if this
                // never made it to the backend.
            }
            _event.emit(OnboardingEvent.NavigateToPaywall)
        }
    }

    private fun Int.pad2(): String = toString().padStart(2, '0')

    private companion object {
        const val CALCULATING_DELAY_MS = 1600L
    }
}
