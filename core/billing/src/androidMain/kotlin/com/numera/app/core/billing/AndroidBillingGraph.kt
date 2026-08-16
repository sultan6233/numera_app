package com.numera.app.core.billing

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

/**
 * Set once from the host Application/MainActivity before the Metro graph is created. Keeps
 * `Context` out of the (Kotlin-common) AppGraph's constructor entirely, since AppGraph is shared
 * with iOS where there's no such type - see [AndroidBillingClient.currentActivity] for the same
 * pattern applied to the foreground Activity.
 */
object ApplicationContextHolder {
    lateinit var context: Context
}

@ContributesTo(AppScope::class)
interface AndroidBillingGraph {
    @Provides
    @SingleIn(AppScope::class)
    fun provideBillingClient(): BillingClient = AndroidBillingClient(ApplicationContextHolder.context)
}
