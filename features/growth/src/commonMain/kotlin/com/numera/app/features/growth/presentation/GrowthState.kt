package com.numera.app.features.growth.presentation

import androidx.compose.runtime.Stable
import com.numera.app.features.growth.presentation.model.GrowthTab
import com.numera.app.features.growth.presentation.model.HabitUi
import com.numera.app.features.growth.presentation.model.MoodEntryUi

sealed interface GrowthState {
    @Stable
    data class Content(
        val tab: GrowthTab = GrowthTab.HABITS,
        val habits: List<HabitUi> = emptyList(),
        val newHabitTitle: String = "",
        val moods: List<MoodEntryUi> = emptyList(),
        val selectedMood: String = "🙂",
        val newMoodNote: String = "",
        val selectedTags: Set<String> = emptySet(),
        val editingMoodId: String? = null,
        val personalDayToday: Int = 0,
        val habitSuggestion: String = "",
        val moodInsight: String? = null,
        val weeklySummary: String? = null,
        /** Average mood score per day for the last 14 days, oldest first, null where there's no
         *  entry that day - drives the trend chart. */
        val moodTrend: List<Double?> = emptyList()
    ) : GrowthState
}
