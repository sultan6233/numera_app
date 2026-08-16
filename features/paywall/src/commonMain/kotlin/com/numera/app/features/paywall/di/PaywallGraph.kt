package com.numera.app.features.paywall.di

import com.numera.app.features.paywall.presentation.PaywallViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension

@GraphExtension(PaywallScope::class)
interface PaywallGraph {
    val viewModel: PaywallViewModel

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    interface Factory {
        fun createPaywallGraph(): PaywallGraph
    }
}
