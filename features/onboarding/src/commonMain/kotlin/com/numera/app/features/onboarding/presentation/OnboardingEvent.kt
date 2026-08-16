package com.numera.app.features.onboarding.presentation

sealed interface OnboardingEvent {
    data object NavigateToPaywall : OnboardingEvent
}
