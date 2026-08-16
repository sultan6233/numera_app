package com.numera.app.features.paywall.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.billing.BillingClient
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.PythagoreanCalculator
import com.numera.app.core.numerology.model.SimpleDate
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Inject
class PaywallViewModel(
    private val billingClient: BillingClient,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val _state = MutableStateFlow(PaywallState.Content())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<PaywallEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: PaywallAction) {
        val content = state.value as? PaywallState.Content ?: return
        when (action) {
            is PaywallAction.OnPlanSelected -> _state.value = content.copy(selectedProductId = action.productId)
            PaywallAction.OnPurchaseClick -> purchase(content.selectedProductId)
            PaywallAction.OnRestoreClick -> restore()
            PaywallAction.OnSkipClick -> setEvent(PaywallEvent.NavigateToHome)
        }
    }

    private fun purchase(productId: String) = viewModelScope.launch {
        _state.value = (state.value as PaywallState.Content).copy(isPurchasing = true)
        val result = billingClient.purchase(productId)
        _state.value = (state.value as PaywallState.Content).copy(isPurchasing = false)
        result.onSuccess { setEvent(PaywallEvent.NavigateToHome) }
        result.onFailure { setEvent(PaywallEvent.ShowError(it.message.orEmpty())) }
    }

    private fun restore() = viewModelScope.launch {
        val result = billingClient.restorePurchases()
        result.onSuccess { setEvent(PaywallEvent.NavigateToHome) }
        result.onFailure { setEvent(PaywallEvent.ShowError(it.message.orEmpty())) }
    }

    private fun setEvent(event: PaywallEvent) = viewModelScope.launch { _event.emit(event) }

    /**
     * The paywall leads with the user's own life path number (already teased in onboarding),
     * not a generic icon - showing something real they've already seen is the paywall's whole
     * "value before ask" argument, not just its decoration.
     */
    private fun loadLifePath() {
        val birthDate = SimpleDate(sessionStorage.birthYear(), sessionStorage.birthMonth(), sessionStorage.birthDay())
        val lifePath = PythagoreanCalculator.lifePath(birthDate)
        val expression = PythagoreanCalculator.expression(sessionStorage.fullName())
        _state.value = (state.value as PaywallState.Content).copy(
            lifePathNumber = lifePath,
            expressionNumber = expression
        )
    }

    init { loadLifePath() }
}
