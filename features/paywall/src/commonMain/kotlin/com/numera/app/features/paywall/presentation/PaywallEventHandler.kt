package com.numera.app.features.paywall.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.numera.app.core.commonMain.Home
import com.numera.app.core.commonMain.Navigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PaywallEventHandler(
    uiEvent: SharedFlow<PaywallEvent>,
    navigator: Navigator,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                PaywallEvent.NavigateToHome -> navigator.replaceAll(Home)
                is PaywallEvent.ShowError -> snackbarHostState.showSnackbar(event.error)
            }
        }
    }
}
