package com.numera.app.features.compatibility.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.numera.app.core.commonMain.CompatibilityDetail
import com.numera.app.core.commonMain.Navigator
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CompatibilityEventHandler(uiEvent: SharedFlow<CompatibilityEvent>, navigator: Navigator) {
    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                is CompatibilityEvent.NavigateToDetail -> navigator.navigateTo(CompatibilityDetail(event.companionId))
            }
        }
    }
}
