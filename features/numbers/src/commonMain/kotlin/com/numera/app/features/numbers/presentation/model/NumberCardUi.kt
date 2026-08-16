package com.numera.app.features.numbers.presentation.model

data class NumberCardUi(
    val type: String,
    val label: String,
    val value: Int,
    val locked: Boolean
)

object NumberType {
    const val LIFE_PATH = "life_path"
    const val EXPRESSION = "expression"
    const val SOUL_URGE = "soul_urge"
    const val PERSONALITY = "personality"
    const val HEALTH = "health"
    const val BUSINESS = "business"
    const val MATURITY = "maturity"
    const val HIDDEN_PASSION = "hidden_passion"
    const val CORNERSTONE = "cornerstone"
    const val CAPSTONE = "capstone"
}
