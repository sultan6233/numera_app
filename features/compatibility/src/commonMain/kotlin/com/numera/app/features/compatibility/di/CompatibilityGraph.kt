package com.numera.app.features.compatibility.di

import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.features.compatibility.data.CompanionApi
import com.numera.app.features.compatibility.data.CompanionRepositoryImpl
import com.numera.app.features.compatibility.domain.CompanionRepository
import com.numera.app.features.compatibility.presentation.CompatibilityDetailViewModel
import com.numera.app.features.compatibility.presentation.CompatibilityViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@GraphExtension(CompatibilityScope::class)
interface CompatibilityGraph {
    val viewModel: CompatibilityViewModel
    val detailViewModel: CompatibilityDetailViewModel

    @Provides
    @SingleIn(CompatibilityScope::class)
    fun provideCompanionRepository(companionApi: CompanionApi, sessionStorage: SessionStorage): CompanionRepository =
        CompanionRepositoryImpl(companionApi, sessionStorage)

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    interface Factory {
        fun createCompatibilityGraph(): CompatibilityGraph
    }
}
