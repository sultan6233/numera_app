package com.numera.app.features.compatibility.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numera.app.core.commonMain.Navigator

@Composable
fun CompatibilityDetailEntry(navigator: Navigator, companionId: String, viewModel: CompatibilityDetailViewModel) {
    LaunchedEffect(companionId) { viewModel.load(companionId) }
    CompatibilityDetailScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onBack = navigator::goBack
    )
}
