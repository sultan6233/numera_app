package com.numera.app.core.billing

import kotlinx.coroutines.flow.StateFlow

sealed interface SubscriptionState {
    data object Unknown : SubscriptionState
    data object NotSubscribed : SubscriptionState
    data class Subscribed(val expiresAtEpochMillis: Long, val productId: String) : SubscriptionState
}

/** Product ids expected to be configured identically in both App Store Connect and Play Console. */
object SubscriptionProducts {
    const val WEEKLY = "numera_weekly"
    const val MONTHLY = "numera_monthly"
    const val YEARLY = "numera_yearly"
}

interface BillingClient {
    val subscriptionState: StateFlow<SubscriptionState>
    suspend fun purchase(productId: String): Result<Unit>
    suspend fun restorePurchases(): Result<Unit>
}
