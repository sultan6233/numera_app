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

data class CompatibilityResult(
    val score: Int,
    val label: String,
    val lifePathA: Int,
    val lifePathB: Int
)
