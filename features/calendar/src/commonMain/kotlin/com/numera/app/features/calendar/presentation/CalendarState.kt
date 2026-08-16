package com.numera.app.features.calendar.presentation

import androidx.compose.runtime.Stable
import com.numera.app.features.calendar.presentation.model.CalendarDayUi

sealed interface CalendarState {
    @Stable
    data class Content(
        val year: Int,
        val month: Int,
        val days: List<CalendarDayUi> = emptyList()
    ) : CalendarState
}
