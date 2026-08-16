package com.numera.app.features.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.billing.BillingClient
import com.numera.app.core.billing.SubscriptionState
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.DateMath
import com.numera.app.core.numerology.PersonalDayCalculator
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.core.numerology.todaySimpleDate
import com.numera.app.features.calendar.presentation.model.CalendarDayUi
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private val MONTH_NAMES = listOf(
    "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
    "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
)

@Inject
class CalendarViewModel(
    private val sessionStorage: SessionStorage,
    private val billingClient: BillingClient
) : ViewModel() {

    private val today = todaySimpleDate()
    private val _state = MutableStateFlow(CalendarState.Content(year = today.year, month = today.month))
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<CalendarEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: CalendarAction) {
        val content = state.value as? CalendarState.Content ?: return
        when (action) {
            CalendarAction.OnPrevMonth -> shiftMonth(content, -1)
            CalendarAction.OnNextMonth -> shiftMonth(content, 1)
            is CalendarAction.OnDayClick -> {
                if (content.subscribed) _state.value = content.copy(selectedDay = action.day)
                else setEvent(CalendarEvent.NavigateToPaywall)
            }
            CalendarAction.OnDismissDayDetail -> _state.value = content.copy(selectedDay = null)
            CalendarAction.OnUnlockClick -> setEvent(CalendarEvent.NavigateToPaywall)
        }
    }

    private fun setEvent(event: CalendarEvent) = viewModelScope.launch { _event.emit(event) }

    private fun shiftMonth(content: CalendarState.Content, delta: Int) {
        var month = content.month + delta
        var year = content.year
        if (month < 1) { month = 12; year -= 1 }
        if (month > 12) { month = 1; year += 1 }
        _state.value = content.copy(year = year, month = month, selectedDay = null)
        buildDays()
    }

    /**
     * Same reactive-subscription pattern as NumbersViewModel: the calendar can be reached right
     * after a purchase completes on the paywall (same NavKey/ViewModelStore across that round
     * trip), so a one-shot subscription read here would leave it locked even post-purchase.
     */
    private var subscriptionJob: Job? = null

    private fun buildDays() {
        subscriptionJob?.cancel()
        subscriptionJob = viewModelScope.launch {
            billingClient.subscriptionState.collectLatest { subscriptionState ->
                val content = state.value as CalendarState.Content
                val birthDate = SimpleDate(sessionStorage.birthYear(), sessionStorage.birthMonth(), sessionStorage.birthDay())
                val leadingBlanks = DateMath.mondayFirstDayOfWeek(content.year, content.month, 1)
                val days = (1..DateMath.daysInMonth(content.year, content.month)).map { day ->
                    val date = SimpleDate(content.year, content.month, day)
                    CalendarDayUi(
                        day = day,
                        personalDay = PersonalDayCalculator.personalDay(birthDate, date),
                        isToday = date == today
                    )
                }
                _state.value = content.copy(
                    monthLabel = "${MONTH_NAMES[content.month - 1]} ${content.year}",
                    leadingBlanks = leadingBlanks,
                    days = days,
                    subscribed = subscriptionState is SubscriptionState.Subscribed
                )
            }
        }
    }

    init { buildDays() }
}
