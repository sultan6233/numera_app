package com.numera.app.features.calendar.presentation

sealed interface CalendarAction {
    data object OnPrevMonth : CalendarAction
    data object OnNextMonth : CalendarAction
}
