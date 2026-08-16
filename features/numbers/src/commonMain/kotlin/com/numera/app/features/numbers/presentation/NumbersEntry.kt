package com.numera.app.features.numbers.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numera.app.core.commonMain.Navigator

@Composable
fun NumbersEntry(navigator: Navigator, viewModel: NumbersViewModel) {
    NumbersEventHandler(uiEvent = viewModel.event, navigator = navigator)
    NumbersScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction
    )
}
