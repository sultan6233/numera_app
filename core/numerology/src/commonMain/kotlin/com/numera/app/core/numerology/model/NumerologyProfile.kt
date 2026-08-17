package com.numera.app.core.numerology.model

data class NumerologyProfile(val fullName: String, val birthDate: SimpleDate)

data class NumerologyNumbers(
    val lifePath: Int,
    val expression: Int,
    val soulUrge: Int,
    val personality: Int,
    val birthDay: Int,
    val healthCode: Int,
    val businessCode: Int
)

/** [categoryKey] and [CompatibilityResult.labelKey] are stable, language-independent identifiers
 *  (not display text) - this module has no UI/resource layer, so localizing them to a shown
 *  string happens in the presentation layer that renders the result. */
data class CompatibilityBreakdownItem(val categoryKey: String, val score: Int)

data class CompatibilityResult(
    val score: Int,
    val labelKey: String,
    val lifePathA: Int,
    val lifePathB: Int,
    val breakdown: List<CompatibilityBreakdownItem> = emptyList()
)

data class LifeCycle(val number: Int, val startAge: Int, val endAge: Int?)
