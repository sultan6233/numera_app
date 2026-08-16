package com.numera.app.features.numbers.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numera.app.core.commonMain.Navigator

@Composable
fun KarmicAnalysisEntry(navigator: Navigator, viewModel: KarmicAnalysisViewModel) {
    KarmicAnalysisScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onBack = navigator::goBack
    )
}
