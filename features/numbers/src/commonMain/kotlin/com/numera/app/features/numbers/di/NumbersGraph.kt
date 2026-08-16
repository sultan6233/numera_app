package com.numera.app.features.numbers.di

import com.numera.app.features.numbers.presentation.NumberDetailViewModel
import com.numera.app.features.numbers.presentation.NumbersViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension

@GraphExtension(NumbersScope::class)
interface NumbersGraph {
    val viewModel: NumbersViewModel
    val numberDetailViewModel: NumberDetailViewModel

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    interface Factory {
        fun createNumbersGraph(): NumbersGraph
    }
}
