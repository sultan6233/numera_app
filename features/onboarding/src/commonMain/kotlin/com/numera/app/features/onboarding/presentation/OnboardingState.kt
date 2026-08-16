package com.numera.app.features.onboarding.presentation

import androidx.compose.runtime.Stable

enum class OnboardingStep { NAME, BIRTH_DATE, CALCULATING, TEASER }

sealed interface OnboardingState {
    @Stable
    data class Content(
        val step: OnboardingStep = OnboardingStep.NAME,
        val name: String = "",
        val day: String = "",
        val month: String = "",
        val year: String = "",
        val lifePathPreview: Int? = null
    ) : OnboardingState
}
