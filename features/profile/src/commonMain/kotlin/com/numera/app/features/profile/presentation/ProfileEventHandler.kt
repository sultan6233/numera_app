package com.numera.app.features.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.numera.app.core.commonMain.Navigator
import com.numera.app.core.commonMain.Paywall
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileEventHandler(
    uiEvent: SharedFlow<ProfileEvent>,
    navigator: Navigator,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                ProfileEvent.NavigateToPaywall -> navigator.navigateTo(Paywall)
                is ProfileEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }
}
