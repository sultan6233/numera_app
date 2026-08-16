package com.numera.app.features.numbers.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.numera.app.core.commonMain.Compatibility
import com.numera.app.core.commonMain.NumberDetail
import com.numera.app.core.commonMain.Navigator
import com.numera.app.core.commonMain.Paywall
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NumbersEventHandler(uiEvent: SharedFlow<NumbersEvent>, navigator: Navigator) {
    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                is NumbersEvent.NavigateToDetail -> navigator.navigateTo(NumberDetail(event.numberType))
                NumbersEvent.NavigateToPaywall -> navigator.navigateTo(Paywall)
                NumbersEvent.NavigateToCompatibility -> navigator.navigateTo(Compatibility)
            }
        }
    }
}
