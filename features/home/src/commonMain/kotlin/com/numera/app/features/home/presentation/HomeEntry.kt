package com.numera.app.features.home.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numera.app.core.commonMain.Navigator

@Composable
fun HomeEntry(navigator: Navigator, viewModel: HomeViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    HomeEventHandler(uiEvent = viewModel.event, snackbarHostState = snackbarHostState)
    HomeScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}
