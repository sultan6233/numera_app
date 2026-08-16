package com.numera.app.core.commonMain

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey

@Serializable data object Onboarding : Route
@Serializable data object Paywall : Route
@Serializable data object Home : Route
@Serializable data object Numbers : Route
@Serializable data class NumberDetail(val numberType: String) : Route
@Serializable data object Compatibility : Route
@Serializable data class CompatibilityDetail(val companionId: String) : Route
@Serializable data object KarmicAnalysis : Route
@Serializable data object LifeCycles : Route
@Serializable data object NumerologyTools : Route
@Serializable data object CalendarEnergy : Route
@Serializable data object Growth : Route
@Serializable data object Profile : Route
