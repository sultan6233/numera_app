package com.numera.app.features.numbers.di

import com.numera.app.features.numbers.presentation.KarmicAnalysisViewModel
import com.numera.app.features.numbers.presentation.LifeCyclesViewModel
import com.numera.app.features.numbers.presentation.NumberDetailViewModel
import com.numera.app.features.numbers.presentation.NumbersViewModel
import com.numera.app.features.numbers.presentation.NumerologyToolsViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension

@GraphExtension(NumbersScope::class)
interface NumbersGraph {
    val viewModel: NumbersViewModel
    val numberDetailViewModel: NumberDetailViewModel
    val karmicAnalysisViewModel: KarmicAnalysisViewModel
    val lifeCyclesViewModel: LifeCyclesViewModel
    val numerologyToolsViewModel: NumerologyToolsViewModel

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    interface Factory {
        fun createNumbersGraph(): NumbersGraph
    }
}
