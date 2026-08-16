package com.numera.app.features.onboarding.presentation

sealed interface OnboardingAction {
    data class OnNameChanged(val value: String) : OnboardingAction
    data object OnNameNext : OnboardingAction
    data class OnDayChanged(val value: String) : OnboardingAction
    data class OnMonthChanged(val value: String) : OnboardingAction
    data class OnYearChanged(val value: String) : OnboardingAction
    data object OnBirthDateNext : OnboardingAction
    data object OnContinueClick : OnboardingAction
}
