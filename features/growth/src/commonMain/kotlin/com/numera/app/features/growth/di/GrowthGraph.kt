package com.numera.app.features.growth.di

import com.numera.app.features.growth.presentation.GrowthViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension

@GraphExtension(GrowthScope::class)
interface GrowthGraph {
    val viewModel: GrowthViewModel

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    interface Factory {
        fun createGrowthGraph(): GrowthGraph
    }
}
