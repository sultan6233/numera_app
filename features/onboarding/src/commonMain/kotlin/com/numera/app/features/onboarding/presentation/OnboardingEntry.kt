package com.numera.app.features.onboarding.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numera.app.core.commonMain.Navigator

@Composable
fun OnboardingEntry(navigator: Navigator, viewModel: OnboardingViewModel) {
    OnboardingEventHandler(uiEvent = viewModel.event, navigator = navigator)
    OnboardingScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction
    )
}
