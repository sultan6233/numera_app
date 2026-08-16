package com.numera.app.features.calendar.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.numera.app.core.commonMain.Navigator

@Composable
fun CalendarEntry(navigator: Navigator, viewModel: CalendarViewModel) {
    CalendarScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = viewModel::onAction
    )
}
