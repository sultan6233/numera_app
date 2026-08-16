package com.numera.app

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.numera.app.core.commonMain.CalendarEnergy
import com.numera.app.core.commonMain.Compatibility
import com.numera.app.core.commonMain.CompatibilityDetail
import com.numera.app.core.commonMain.Growth
import com.numera.app.core.commonMain.Home
import com.numera.app.core.commonMain.Navigator
import com.numera.app.core.commonMain.NumberDetail
import com.numera.app.core.commonMain.Numbers
import com.numera.app.core.commonMain.Onboarding
import com.numera.app.core.commonMain.Paywall
import com.numera.app.core.commonMain.Profile
import com.numera.app.core.commonMain.NumeraTheme
import com.numera.app.core.commonMain.Route
import com.numera.app.core.commonMain.components.NumeraBottomBar
import com.numera.app.core.commonMain.rememberViewModelStoreNavEntryDecorator
import com.numera.app.core.commonMain.session.rememberPlatformSettings
import com.numera.app.features.calendar.presentation.CalendarEntry
import com.numera.app.features.compatibility.presentation.CompatibilityDetailEntry
import com.numera.app.features.compatibility.presentation.CompatibilityEntry
import com.numera.app.features.growth.presentation.GrowthEntry
import com.numera.app.features.home.presentation.HomeEntry
import com.numera.app.features.numbers.presentation.NumberDetailEntry
import com.numera.app.features.numbers.presentation.NumbersEntry
import com.numera.app.features.onboarding.presentation.OnboardingEntry
import com.numera.app.features.paywall.presentation.PaywallEntry
import com.numera.app.features.profile.presentation.ProfileEntry
import dev.zacsweers.metro.createGraph
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@OptIn(ExperimentalSerializationApi::class)
internal val config = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<Route>()
        }
    }
}
val LocalAppGraph = staticCompositionLocalOf<AppGraph> {
    error("AppGraph not provided")
}

private val TAB_ROUTES = setOf(Home, Numbers, CalendarEnergy, Growth, Profile)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val appGraph = remember {
        createGraph<AppGraph>()
    }
    val settings = rememberPlatformSettings()
    val startDestination = remember(settings) {
        appGraph.sessionStorage.attach(settings)
        if (appGraph.sessionStorage.hasCompletedOnboarding()) Home else Onboarding
    }
    NumeraTheme {
        val navController = rememberNavBackStack(config, startDestination)
        val navigator = remember { Navigator(navController) }
        val currentRoute = navController.lastOrNull() as? Route
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                bottomBar = {
                    if (currentRoute in TAB_ROUTES) {
                        NumeraBottomBar(
                            currentRoute = currentRoute,
                            onTabClick = { navigator.navigateToTab(it) },
                            modifier = Modifier.navigationBarsPadding()
                        )
                    }
                }
            ) { innerPadding ->
                NavDisplay(
                    modifier = Modifier.padding(innerPadding),
                    backStack = navController,
                    onBack = { navController.removeLastOrNull() },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    entryProvider = entryProvider {
                        entry<Onboarding> {
                            val viewModel = viewModel {
                                appGraph.onboardingGraphFactory.createOnboardingGraph().viewModel
                            }
                            OnboardingEntry(navigator, viewModel = viewModel)
                        }
                        entry<Paywall> {
                            val viewModel = viewModel {
                                appGraph.paywallGraphFactory.createPaywallGraph().viewModel
                            }
                            PaywallEntry(navigator, viewModel = viewModel)
                        }
                        entry<Home> {
                            val viewModel = viewModel {
                                appGraph.homeGraphFactory.createHomeGraph().viewModel
                            }
                            HomeEntry(navigator, viewModel = viewModel)
                        }
                        entry<Numbers> {
                            val viewModel = viewModel {
                                appGraph.numbersGraphFactory.createNumbersGraph().viewModel
                            }
                            NumbersEntry(navigator, viewModel = viewModel)
                        }
                        entry<NumberDetail> { key ->
                            val viewModel = viewModel {
                                appGraph.numbersGraphFactory.createNumbersGraph().numberDetailViewModel
                            }
                            NumberDetailEntry(navigator, numberType = key.numberType, viewModel = viewModel)
                        }
                        entry<Compatibility> {
                            val viewModel = viewModel {
                                appGraph.compatibilityGraphFactory.createCompatibilityGraph().viewModel
                            }
                            CompatibilityEntry(navigator, viewModel = viewModel)
                        }
                        entry<CompatibilityDetail> { key ->
                            val viewModel = viewModel {
                                appGraph.compatibilityGraphFactory.createCompatibilityGraph().detailViewModel
                            }
                            CompatibilityDetailEntry(
                                navigator,
                                companionId = key.companionId,
                                viewModel = viewModel
                            )
                        }
                        entry<CalendarEnergy> {
                            val viewModel = viewModel {
                                appGraph.calendarGraphFactory.createCalendarGraph().viewModel
                            }
                            CalendarEntry(navigator, viewModel = viewModel)
                        }
                        entry<Growth> {
                            val viewModel = viewModel {
                                appGraph.growthGraphFactory.createGrowthGraph().viewModel
                            }
                            GrowthEntry(navigator, viewModel = viewModel)
                        }
                        entry<Profile> {
                            val viewModel = viewModel {
                                appGraph.profileGraphFactory.createProfileGraph().viewModel
                            }
                            ProfileEntry(navigator, viewModel = viewModel)
                        }
                    }
                )
            }
        }
    }
}
