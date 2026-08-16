package com.numera.app.features.calendar.presentation

import androidx.lifecycle.ViewModel
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.DateMath
import com.numera.app.core.numerology.PersonalDayCalculator
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.core.numerology.todaySimpleDate
import com.numera.app.features.calendar.presentation.model.CalendarDayUi
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Inject
class CalendarViewModel(private val sessionStorage: SessionStorage) : ViewModel() {

    private val today = todaySimpleDate()
    private val _state = MutableStateFlow(CalendarState.Content(year = today.year, month = today.month))
    val state = _state.asStateFlow()

    fun onAction(action: CalendarAction) {
        val content = state.value as? CalendarState.Content ?: return
        when (action) {
            CalendarAction.OnPrevMonth -> shiftMonth(content, -1)
            CalendarAction.OnNextMonth -> shiftMonth(content, 1)
        }
    }

    private fun shiftMonth(content: CalendarState.Content, delta: Int) {
        var month = content.month + delta
        var year = content.year
        if (month < 1) { month = 12; year -= 1 }
        if (month > 12) { month = 1; year += 1 }
        _state.value = CalendarState.Content(year = year, month = month)
        buildDays()
    }

    private fun buildDays() {
        val content = state.value as CalendarState.Content
        val birthDate = SimpleDate(sessionStorage.birthYear(), sessionStorage.birthMonth(), sessionStorage.birthDay())
        val days = (1..DateMath.daysInMonth(content.year, content.month)).map { day ->
            val date = SimpleDate(content.year, content.month, day)
            CalendarDayUi(
                day = day,
                personalDay = PersonalDayCalculator.personalDay(birthDate, date),
                isToday = date == today
            )
        }
        _state.value = content.copy(days = days)
    }

    init { buildDays() }
}
