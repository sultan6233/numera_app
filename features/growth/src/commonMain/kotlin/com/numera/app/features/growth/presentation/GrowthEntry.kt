package com.numera.app.features.growth.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numera.app.core.commonMain.Navigator

@Composable
fun GrowthEntry(navigator: Navigator, viewModel: GrowthViewModel) {
    GrowthScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction
    )
}
