package com.numera.app.features.growth.presentation

import com.numera.app.features.growth.presentation.model.GrowthTab

sealed interface GrowthAction {
    data class OnTabSelected(val tab: GrowthTab) : GrowthAction
    data class OnNewHabitTitleChanged(val value: String) : GrowthAction
    data object OnAddHabit : GrowthAction
    data class OnToggleHabit(val id: String) : GrowthAction
    data class OnDeleteHabit(val id: String) : GrowthAction
    data class OnMoodSelected(val mood: String) : GrowthAction
    data class OnMoodNoteChanged(val value: String) : GrowthAction
    data object OnSaveMood : GrowthAction
    data class OnDeleteMood(val id: String) : GrowthAction
    data class OnTagToggled(val tag: String) : GrowthAction
    data class OnMoodEntryClick(val id: String) : GrowthAction
    data object OnCancelMoodEdit : GrowthAction
}
