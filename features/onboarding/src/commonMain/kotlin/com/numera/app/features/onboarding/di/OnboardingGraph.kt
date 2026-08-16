package com.numera.app.features.onboarding.di

import com.numera.app.features.onboarding.presentation.OnboardingViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension

@GraphExtension(OnboardingScope::class)
interface OnboardingGraph {
    val viewModel: OnboardingViewModel

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    interface Factory {
        fun createOnboardingGraph(): OnboardingGraph
    }
}
