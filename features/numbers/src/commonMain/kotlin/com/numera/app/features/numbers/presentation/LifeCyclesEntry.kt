package com.numera.app.features.numbers.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LifeCyclesEntry(viewModel: LifeCyclesViewModel) {
    LifeCyclesScreen(state = viewModel.state.collectAsStateWithLifecycle().value)
}
