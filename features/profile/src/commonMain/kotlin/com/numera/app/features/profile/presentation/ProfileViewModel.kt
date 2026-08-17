package com.numera.app.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numera.app.core.billing.BillingClient
import com.numera.app.core.billing.SubscriptionState
import com.numera.app.core.commonMain.session.SessionStorage
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import numera.features.profile.generated.resources.Res
import numera.features.profile.generated.resources.purchases_restored

@Inject
class ProfileViewModel(
    private val sessionStorage: SessionStorage,
    private val billingClient: BillingClient
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState.Content())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ProfileEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnRestoreClick -> restore()
            ProfileAction.OnUpgradeClick -> viewModelScope.launch { _event.emit(ProfileEvent.NavigateToPaywall) }
            is ProfileAction.OnNotificationsToggled -> toggleNotifications(action.enabled)
        }
    }

    private fun toggleNotifications(enabled: Boolean) {
        sessionStorage.setNotificationsEnabled(enabled)
        _state.value = (state.value as ProfileState.Content).copy(notificationsEnabled = enabled)
    }

    private fun restore() = viewModelScope.launch {
        _state.value = (state.value as ProfileState.Content).copy(isRestoring = true)
        val result = billingClient.restorePurchases()
        _state.value = (state.value as ProfileState.Content).copy(
            isRestoring = false,
            subscribed = billingClient.subscriptionState.value is SubscriptionState.Subscribed
        )
        result.onFailure { _event.emit(ProfileEvent.ShowMessage(it.message.orEmpty())) }
        result.onSuccess { _event.emit(ProfileEvent.ShowMessage(getString(Res.string.purchases_restored))) }
    }

    private fun load() {
        _state.value = ProfileState.Content(
            name = sessionStorage.fullName(),
            birthDateText = "${sessionStorage.birthDay()}.${sessionStorage.birthMonth()}.${sessionStorage.birthYear()}",
            subscribed = billingClient.subscriptionState.value is SubscriptionState.Subscribed,
            notificationsEnabled = sessionStorage.notificationsEnabled()
        )
    }

    init { load() }
}
