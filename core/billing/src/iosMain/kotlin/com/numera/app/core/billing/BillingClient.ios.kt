package com.numera.app.core.billing

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * PLACEHOLDER pending real StoreKit 2 wiring. StoreKit 2's async/await Product/Transaction API
 * needs to be validated by compiling and running on an actual Mac/Xcode toolchain - this repo's
 * dev environment can't build or verify iOS Kotlin/Native targets, so rather than hand-write
 * StoreKit interop that can't be checked here, this keeps the UI/paywall flow buildable and
 * testable in the simulator via in-memory state, with real purchases wired in as a follow-up
 * once there's a macOS machine available.
 */
class IosBillingClient : BillingClient {
    private val _subscriptionState = MutableStateFlow<SubscriptionState>(SubscriptionState.NotSubscribed)
    override val subscriptionState: StateFlow<SubscriptionState> = _subscriptionState

    override suspend fun purchase(productId: String): Result<Unit> {
        // TODO: replace with StoreKit 2 Product.purchase() once this can be built on macOS.
        _subscriptionState.value = SubscriptionState.Subscribed(
            expiresAtEpochMillis = Long.MAX_VALUE,
            productId = productId
        )
        return Result.success(Unit)
    }

    override suspend fun restorePurchases(): Result<Unit> {
        // TODO: replace with AppStore.sync() + Transaction.currentEntitlements once buildable.
        return Result.success(Unit)
    }
}
