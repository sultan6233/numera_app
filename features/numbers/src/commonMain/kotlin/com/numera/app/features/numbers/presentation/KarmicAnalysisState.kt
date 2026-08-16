package com.numera.app.features.numbers.presentation

sealed interface KarmicAnalysisState {
    data object Loading : KarmicAnalysisState
    data class Content(
        val debts: List<KarmicItemUi>,
        val lessons: List<KarmicItemUi>
    ) : KarmicAnalysisState
}

data class KarmicItemUi(val number: Int, val text: String)
