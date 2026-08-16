package com.numera.app.features.home.di

import com.numera.app.features.home.data.HomeApi
import com.numera.app.features.home.data.HomeRepositoryImpl
import com.numera.app.features.home.domain.HomeRepository
import com.numera.app.features.home.presentation.HomeViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@GraphExtension(HomeScope::class)
interface HomeGraph {
    val viewModel: HomeViewModel

    @Provides
    @SingleIn(HomeScope::class)
    fun provideHomeRepository(homeApi: HomeApi): HomeRepository = HomeRepositoryImpl(homeApi)

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    interface Factory {
        fun createHomeGraph(): HomeGraph
    }
}
