package com.numera.app.features.growth.presentation

import androidx.lifecycle.ViewModel
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.features.growth.presentation.model.HabitUi
import com.numera.app.features.growth.presentation.model.MoodEntryUi
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val FIELD_SEPARATOR = ";;"

@Inject
class GrowthViewModel(private val sessionStorage: SessionStorage) : ViewModel() {

    private val _state = MutableStateFlow(GrowthState.Content())
    val state = _state.asStateFlow()

    fun onAction(action: GrowthAction) {
        val content = state.value as? GrowthState.Content ?: return
        when (action) {
            is GrowthAction.OnTabSelected -> _state.value = content.copy(tab = action.tab)
            is GrowthAction.OnNewHabitTitleChanged -> _state.value = content.copy(newHabitTitle = action.value)
            GrowthAction.OnAddHabit -> addHabit(content)
            is GrowthAction.OnToggleHabit -> toggleHabit(content, action.id)
            is GrowthAction.OnMoodSelected -> _state.value = content.copy(selectedMood = action.mood)
            is GrowthAction.OnMoodNoteChanged -> _state.value = content.copy(newMoodNote = action.value)
            GrowthAction.OnSaveMood -> saveMood(content)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun addHabit(content: GrowthState.Content) {
        if (content.newHabitTitle.isBlank()) return
        val habit = HabitUi(Uuid.random().toString(), content.newHabitTitle, done = false)
        val records = sessionStorage.habitRecords() + habit.toRecord()
        sessionStorage.saveHabitRecords(records)
        _state.value = content.copy(habits = content.habits + habit, newHabitTitle = "")
    }

    private fun toggleHabit(content: GrowthState.Content, id: String) {
        val updated = content.habits.map { if (it.id == id) it.copy(done = !it.done) else it }
        sessionStorage.saveHabitRecords(updated.map { it.toRecord() })
        _state.value = content.copy(habits = updated)
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun saveMood(content: GrowthState.Content) {
        val entry = MoodEntryUi(Uuid.random().toString(), content.selectedMood, content.newMoodNote)
        val records = sessionStorage.moodRecords() + entry.toRecord()
        sessionStorage.saveMoodRecords(records)
        _state.value = content.copy(moods = listOf(entry) + content.moods, newMoodNote = "")
    }

    private fun HabitUi.toRecord(): String = listOf(id, title, if (done) "1" else "0").joinToString(FIELD_SEPARATOR)

    private fun String.toHabitOrNull(): HabitUi? {
        val parts = split(FIELD_SEPARATOR)
        if (parts.size != 3) return null
        return HabitUi(parts[0], parts[1], parts[2] == "1")
    }

    private fun MoodEntryUi.toRecord(): String = listOf(id, mood, note).joinToString(FIELD_SEPARATOR)

    private fun String.toMoodOrNull(): MoodEntryUi? {
        val parts = split(FIELD_SEPARATOR)
        if (parts.size != 3) return null
        return MoodEntryUi(parts[0], parts[1], parts[2])
    }

    private fun load() {
        _state.value = state.value.let { it as GrowthState.Content }.copy(
            habits = sessionStorage.habitRecords().mapNotNull { it.toHabitOrNull() },
            moods = sessionStorage.moodRecords().mapNotNull { it.toMoodOrNull() }.reversed()
        )
    }

    init { load() }
}
