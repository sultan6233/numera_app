package com.numera.app.features.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numera.app.core.commonMain.Navigator

@Composable
fun ProfileEntry(navigator: Navigator, viewModel: ProfileViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    ProfileEventHandler(uiEvent = viewModel.event, navigator = navigator, snackbarHostState = snackbarHostState)
    ProfileScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}
