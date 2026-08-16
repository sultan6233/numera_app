package com.numera.app.features.growth.presentation.model

enum class GrowthTab { HABITS, MOOD }

data class HabitUi(val id: String, val title: String, val doneToday: Boolean, val streak: Int)

data class MoodEntryUi(
    val id: String,
    val mood: String,
    val note: String,
    val tags: List<String>,
    val year: Int,
    val month: Int,
    val day: Int
)
