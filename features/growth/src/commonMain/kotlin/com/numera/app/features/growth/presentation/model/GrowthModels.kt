package com.numera.app.features.growth.presentation.model

enum class GrowthTab { HABITS, MOOD }

data class HabitUi(val id: String, val title: String, val done: Boolean)

data class MoodEntryUi(val id: String, val mood: String, val note: String)
