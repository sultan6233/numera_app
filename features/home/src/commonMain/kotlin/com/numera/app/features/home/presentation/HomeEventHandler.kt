package com.numera.app.features.home.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeEventHandler(uiEvent: SharedFlow<HomeEvent>, snackbarHostState: SnackbarHostState) {
    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                is HomeEvent.ShowError -> snackbarHostState.showSnackbar(event.error)
            }
        }
    }
}
