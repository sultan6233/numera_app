package com.numera.app.features.calendar.presentation

import com.numera.app.features.calendar.presentation.model.CalendarDayUi

sealed interface CalendarAction {
    data object OnPrevMonth : CalendarAction
    data object OnNextMonth : CalendarAction
    data class OnDayClick(val day: CalendarDayUi) : CalendarAction
    data object OnDismissDayDetail : CalendarAction
    data object OnUnlockClick : CalendarAction
}
