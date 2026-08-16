package com.numera.app.features.numbers.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun KarmicAnalysisEntry(viewModel: KarmicAnalysisViewModel) {
    KarmicAnalysisScreen(state = viewModel.state.collectAsStateWithLifecycle().value)
}
