package com.numera.app.features.calendar.presentation

import androidx.compose.runtime.Stable
import com.numera.app.features.calendar.presentation.model.CalendarDayUi

sealed interface CalendarState {
    @Stable
    data class Content(
        val year: Int,
        val month: Int,
        val monthLabel: String = "",
        val leadingBlanks: Int = 0,
        val days: List<CalendarDayUi> = emptyList(),
        val subscribed: Boolean = false,
        val selectedDay: CalendarDayUi? = null
    ) : CalendarState
}
