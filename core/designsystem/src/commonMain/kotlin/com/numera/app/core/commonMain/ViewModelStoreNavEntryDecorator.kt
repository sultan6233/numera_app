package com.numera.app.core.commonMain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation3.runtime.NavEntryDecorator

/**
 * Gives every nav3 backstack entry its own [ViewModelStore], scoped to [androidx.navigation3.runtime.NavEntry.contentKey].
 *
 * Navigation3 does not ship this out of the box, so `viewModel { ... }` calls inside entry
 * content would otherwise all share a single app-wide [ViewModelStoreOwner], colliding by
 * ViewModel class across unrelated screens.
 */
class ViewModelStoreNavEntryDecorator<T : Any>(
    private val stores: MutableMap<Any, ViewModelStore> = mutableMapOf()
) : NavEntryDecorator<T>(
    onPop = { key -> stores.remove(key)?.clear() },
    decorate = { entry ->
        val store = remember(entry.contentKey) { stores.getOrPut(entry.contentKey) { ViewModelStore() } }
        val owner = remember(store) {
            object : ViewModelStoreOwner {
                override val viewModelStore = store
            }
        }
        CompositionLocalProvider(LocalViewModelStoreOwner provides owner) {
            entry.Content()
        }
    }
)

@Composable
fun <T : Any> rememberViewModelStoreNavEntryDecorator(): ViewModelStoreNavEntryDecorator<T> =
    remember { ViewModelStoreNavEntryDecorator() }
