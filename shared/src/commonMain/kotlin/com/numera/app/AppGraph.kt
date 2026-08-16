package com.numera.app

import com.numera.app.core.billing.BillingClient
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.features.calendar.di.CalendarGraph
import com.numera.app.features.compatibility.di.CompatibilityGraph
import com.numera.app.features.growth.di.GrowthGraph
import com.numera.app.features.home.di.HomeGraph
import com.numera.app.features.numbers.di.NumbersGraph
import com.numera.app.features.onboarding.di.OnboardingGraph
import com.numera.app.features.paywall.di.PaywallGraph
import com.numera.app.features.profile.di.ProfileGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
interface AppGraph {
    val onboardingGraphFactory: OnboardingGraph.Factory
    val paywallGraphFactory: PaywallGraph.Factory
    val homeGraphFactory: HomeGraph.Factory
    val numbersGraphFactory: NumbersGraph.Factory
    val compatibilityGraphFactory: CompatibilityGraph.Factory
    val calendarGraphFactory: CalendarGraph.Factory
    val growthGraphFactory: GrowthGraph.Factory
    val profileGraphFactory: ProfileGraph.Factory
    val sessionStorage: SessionStorage
    val billingClient: BillingClient
}
