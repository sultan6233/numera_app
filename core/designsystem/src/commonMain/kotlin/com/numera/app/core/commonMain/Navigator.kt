package com.numera.app.core.commonMain

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class Navigator(private val backStack: NavBackStack<NavKey>) {
    fun navigateTo(route: NavKey) {
        backStack.add(route)
    }

    fun goBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    fun popTo(route: NavKey) {
        val index = backStack.indexOf(route)
        if (index != -1) {
            while (backStack.size > index + 1) backStack.removeLastOrNull()
        }
    }

    fun replaceAll(route: NavKey) {
        backStack.clear()
        backStack.add(route)
    }

    fun navigateToTab(route: NavKey) {
        val index = backStack.indexOf(route)
        if (index != -1) {
            while (backStack.size > index + 1) backStack.removeLastOrNull()
        } else {
            backStack.add(route)
        }
    }
}
