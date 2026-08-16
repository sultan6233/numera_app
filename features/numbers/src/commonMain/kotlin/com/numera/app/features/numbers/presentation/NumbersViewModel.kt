package com.numera.app.features.numbers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.billing.SubscriptionState
import com.numera.app.core.billing.BillingClient
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.AdvancedNumerology
import com.numera.app.core.numerology.LuckyNumbers
import com.numera.app.core.numerology.PythagoreanCalculator
import com.numera.app.core.numerology.model.NumerologyProfile
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.core.numerology.todaySimpleDate
import com.numera.app.features.numbers.domain.PersonalCycleMeanings
import com.numera.app.features.numbers.presentation.model.NumberCardUi
import com.numera.app.features.numbers.presentation.model.NumberType
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Inject
class NumbersViewModel(
    private val sessionStorage: SessionStorage,
    private val billingClient: BillingClient
) : ViewModel() {

    private val _state = MutableStateFlow(NumbersState.Content())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NumbersEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: NumbersAction) {
        when (action) {
            NumbersAction.OnRefresh -> load()
            is NumbersAction.OnCardClick -> handleCardClick(action.numberType)
            NumbersAction.OnCompatibilityClick -> setEvent(NumbersEvent.NavigateToCompatibility)
            NumbersAction.OnKarmicAnalysisClick -> setEvent(NumbersEvent.NavigateToKarmicAnalysis)
            NumbersAction.OnLifeCyclesClick -> setEvent(NumbersEvent.NavigateToLifeCycles)
            NumbersAction.OnToolsClick -> setEvent(NumbersEvent.NavigateToTools)
        }
    }

    private fun handleCardClick(numberType: String) {
        val card = state.value.cards.find { it.type == numberType } ?: return
        if (card.locked) {
            setEvent(NumbersEvent.NavigateToPaywall)
        } else {
            setEvent(NumbersEvent.NavigateToDetail(numberType))
        }
    }

    private fun setEvent(event: NumbersEvent) = viewModelScope.launch { _event.emit(event) }

    private val profile by lazy {
        NumerologyProfile(
            fullName = sessionStorage.fullName(),
            birthDate = SimpleDate(sessionStorage.birthYear(), sessionStorage.birthMonth(), sessionStorage.birthDay())
        )
    }

    /**
     * Rebuilds the card list on every subscriptionState emission, not just once at load - this
     * ViewModel survives a round trip through the paywall (same NavKey, same ViewModelStore), so
     * a one-shot read here would leave cards locked even right after a successful purchase.
     */
    private var subscriptionJob: Job? = null

    private fun load() {
        _state.value = state.value.copy(loading = true)
        subscriptionJob?.cancel()
        subscriptionJob = viewModelScope.launch {
            billingClient.subscriptionState.collectLatest { subscriptionState ->
                val subscribed = subscriptionState is SubscriptionState.Subscribed
                val numbers = PythagoreanCalculator.allNumbers(profile)
                val cards = listOf(
                    NumberCardUi(NumberType.LIFE_PATH, "Число жизненного пути", numbers.lifePath, locked = false),
                    NumberCardUi(NumberType.EXPRESSION, "Число судьбы", numbers.expression, locked = false),
                    NumberCardUi(NumberType.SOUL_URGE, "Число души", numbers.soulUrge, locked = !subscribed),
                    NumberCardUi(NumberType.PERSONALITY, "Число личности", numbers.personality, locked = !subscribed),
                    NumberCardUi(NumberType.HEALTH, "Код здоровья", numbers.healthCode, locked = !subscribed),
                    NumberCardUi(NumberType.BUSINESS, "Код бизнеса", numbers.businessCode, locked = !subscribed),
                    NumberCardUi(NumberType.MATURITY, "Число зрелости", AdvancedNumerology.maturityNumber(profile), locked = !subscribed),
                    NumberCardUi(
                        NumberType.HIDDEN_PASSION,
                        "Скрытая страсть",
                        AdvancedNumerology.hiddenPassionNumber(profile.fullName),
                        locked = !subscribed
                    ),
                    NumberCardUi(
                        NumberType.CORNERSTONE,
                        "Краеугольный камень",
                        AdvancedNumerology.cornerstoneNumber(profile.fullName),
                        locked = !subscribed
                    ),
                    NumberCardUi(
                        NumberType.CAPSTONE,
                        "Завершающий камень",
                        AdvancedNumerology.capstoneNumber(profile.fullName),
                        locked = !subscribed
                    )
                )

                val today = todaySimpleDate()
                val personalYear = AdvancedNumerology.personalYear(profile.birthDate, today.year)
                val personalMonth = AdvancedNumerology.personalMonth(profile.birthDate, today.year, today.month)
                val luckyNumbers = LuckyNumbers.luckyNumbers(numbers, personalYear)
                val luckyColor = LuckyNumbers.luckyColor(numbers.lifePath)

                _state.value = state.value.copy(
                    cards = cards,
                    loading = false,
                    personalYear = personalYear,
                    personalYearTheme = PersonalCycleMeanings.yearTheme(personalYear),
                    personalMonth = personalMonth,
                    personalMonthTheme = PersonalCycleMeanings.monthTheme(personalMonth),
                    luckyNumbers = luckyNumbers,
                    luckyColor = luckyColor
                )
            }
        }
    }

    init { load() }
}
