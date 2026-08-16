package com.numera.app.features.compatibility.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numera.app.core.commonMain.Navigator

@Composable
fun CompatibilityEntry(navigator: Navigator, viewModel: CompatibilityViewModel) {
    CompatibilityEventHandler(uiEvent = viewModel.event, navigator = navigator)
    CompatibilityScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction,
        onBack = navigator::goBack
    )
}
