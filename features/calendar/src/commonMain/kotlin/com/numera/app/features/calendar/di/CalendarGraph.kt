package com.numera.app.features.calendar.di

import com.numera.app.features.calendar.presentation.CalendarViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension

@GraphExtension(CalendarScope::class)
interface CalendarGraph {
    val viewModel: CalendarViewModel

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    interface Factory {
        fun createCalendarGraph(): CalendarGraph
    }
}
