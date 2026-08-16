package com.numera.app.core.billing

import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient as PlayBillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Wraps Google Play Billing (v8). Purchases are resolved through this class's own
 * PurchasesUpdatedListener callback, not the caller's coroutine continuation directly - Play
 * Billing can deliver an update at any time (e.g. a pending purchase completing later), so
 * [purchase] resumes as soon as the flow launches and state updates arrive separately via
 * [subscriptionState].
 */
class AndroidBillingClient(private val context: Context) : BillingClient, PurchasesUpdatedListener {

    private val _subscriptionState = MutableStateFlow<SubscriptionState>(SubscriptionState.Unknown)
    override val subscriptionState: StateFlow<SubscriptionState> = _subscriptionState

    private val client = PlayBillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            com.android.billingclient.api.PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    private var connected = false

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode != com.android.billingclient.api.BillingClient.BillingResponseCode.OK) return
        purchases?.forEach { handlePurchase(it) }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) return
        if (!purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            client.acknowledgePurchase(params) {}
        }
        val productId = purchase.products.firstOrNull() ?: return
        // Play doesn't hand back a real expiry timestamp client-side - this app relies on the
        // backend's App Store/Play server-notification webhooks (see backend spec) as the source
        // of truth for entitlement; this local flag only unblocks the UI immediately post-purchase.
        _subscriptionState.value = SubscriptionState.Subscribed(
            expiresAtEpochMillis = Long.MAX_VALUE,
            productId = productId
        )
    }

    private suspend fun ensureConnected() {
        if (connected) return
        suspendCancellableCoroutine<Unit> { continuation ->
            client.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(result: BillingResult) {
                    connected = result.responseCode == com.android.billingclient.api.BillingClient.BillingResponseCode.OK
                    if (continuation.isActive) continuation.resume(Unit)
                }

                override fun onBillingServiceDisconnected() {
                    connected = false
                }
            })
        }
    }

    /**
     * Real Play Billing needs the app published (at least to internal testing) with matching
     * products configured in Play Console - neither exists yet for this project. Rather than
     * block all paywall/entitlement UI work on that store setup, debug builds grant access
     * immediately. This never runs in a release build (FLAG_DEBUGGABLE is false there) - remove
     * once the app is in Play Console internal testing with real products and license testers.
     */
    private fun isDebugBuild(): Boolean =
        context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    override suspend fun purchase(productId: String): Result<Unit> = runCatching {
        if (isDebugBuild()) {
            _subscriptionState.value = SubscriptionState.Subscribed(Long.MAX_VALUE, productId)
            return@runCatching
        }
        ensureConnected()
        val activity = currentActivity ?: error("No foreground Activity to launch the billing flow from")
        val productDetails = queryProductDetails(productId) ?: error("Product $productId not found")
        val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken
            ?: error("No subscription offer for $productId")

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .setOfferToken(offerToken)
            .build()
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()
        client.launchBillingFlow(activity, flowParams)
        // Result arrives asynchronously via onPurchasesUpdated -> subscriptionState.
    }

    override suspend fun restorePurchases(): Result<Unit> = runCatching {
        ensureConnected()
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(com.android.billingclient.api.BillingClient.ProductType.SUBS)
            .build()
        suspendCancellableCoroutine<Unit> { continuation ->
            client.queryPurchasesAsync(params) { _, purchases ->
                purchases.forEach { handlePurchase(it) }
                if (purchases.isEmpty()) _subscriptionState.value = SubscriptionState.NotSubscribed
                if (continuation.isActive) continuation.resume(Unit)
            }
        }
    }

    private suspend fun queryProductDetails(productId: String): ProductDetails? {
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(com.android.billingclient.api.BillingClient.ProductType.SUBS)
            .build()
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()
        return suspendCancellableCoroutine { continuation ->
            client.queryProductDetailsAsync(params) { _, result ->
                if (continuation.isActive) continuation.resume(result.productDetailsList.firstOrNull())
            }
        }
    }

    companion object {
        /** Set by the hosting Activity - Play Billing requires a foreground Activity to launch from. */
        var currentActivity: Activity? = null
    }
}
