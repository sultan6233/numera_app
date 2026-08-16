package com.numera.app.features.calendar.presentation

sealed interface CalendarEvent {
    data object NavigateToPaywall : CalendarEvent
}
