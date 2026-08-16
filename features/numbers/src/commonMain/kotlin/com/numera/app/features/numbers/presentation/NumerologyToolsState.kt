package com.numera.app.features.numbers.presentation

enum class ToolMode { NAME, DIGITS }

data class NumerologyToolsState(
    val mode: ToolMode = ToolMode.NAME,
    val input: String = "",
    val result: Int? = null
)
