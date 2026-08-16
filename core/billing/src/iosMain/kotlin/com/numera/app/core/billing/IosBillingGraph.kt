package com.numera.app.core.billing

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface IosBillingGraph {
    @Provides
    @SingleIn(AppScope::class)
    fun provideBillingClient(): BillingClient = IosBillingClient()
}
