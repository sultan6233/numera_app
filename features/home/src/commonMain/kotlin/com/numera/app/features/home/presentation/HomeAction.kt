package com.numera.app.features.home.presentation

sealed interface HomeAction {
    data object OnRefresh : HomeAction
}
