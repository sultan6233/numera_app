package com.numera.app.features.profile.di

import com.numera.app.features.profile.presentation.ProfileViewModel
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.GraphExtension

@GraphExtension(ProfileScope::class)
interface ProfileGraph {
    val viewModel: ProfileViewModel

    @ContributesTo(AppScope::class)
    @GraphExtension.Factory
    interface Factory {
        fun createProfileGraph(): ProfileGraph
    }
}
