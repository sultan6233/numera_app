package com.numera.app.features.calendar.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.numera.app.core.commonMain.Navigator
import com.numera.app.core.commonMain.Paywall
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CalendarEventHandler(uiEvent: SharedFlow<CalendarEvent>, navigator: Navigator) {
    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                CalendarEvent.NavigateToPaywall -> navigator.navigateTo(Paywall)
            }
        }
    }
}
