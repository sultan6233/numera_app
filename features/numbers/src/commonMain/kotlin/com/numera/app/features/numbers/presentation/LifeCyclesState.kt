package com.numera.app.features.numbers.presentation

sealed interface LifeCyclesState {
    data object Loading : LifeCyclesState
    data class Content(val stages: List<LifeStageUi>) : LifeCyclesState
}

data class LifeStageUi(
    val ageRangeLabel: String,
    val pinnacleNumber: Int,
    val pinnacleText: String,
    val challengeNumber: Int,
    val challengeText: String
)
