package com.numera.app.features.numbers.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numera.app.core.commonMain.Navigator

@Composable
fun NumberDetailEntry(navigator: Navigator, numberType: String, viewModel: NumberDetailViewModel) {
    LaunchedEffect(numberType) { viewModel.load(numberType) }
    NumberDetailScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onBack = navigator::goBack
    )
}
