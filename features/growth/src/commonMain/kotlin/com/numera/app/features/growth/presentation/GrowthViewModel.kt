package com.numera.app.features.growth.presentation

import androidx.lifecycle.ViewModel
import com.numera.app.core.commonMain.session.SessionStorage
import com.numera.app.core.numerology.DateMath
import com.numera.app.core.numerology.PersonalDayCalculator
import com.numera.app.core.numerology.model.SimpleDate
import com.numera.app.core.numerology.todaySimpleDate
import com.numera.app.features.growth.domain.HabitSuggestions
import com.numera.app.features.growth.presentation.model.HabitUi
import com.numera.app.features.growth.presentation.model.MoodEntryUi
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val FIELD_SEPARATOR = ";;"

/** Mood -> a 1-5 score, used only to find which personal-day numbers tend to correlate with a
 *  better mood - never shown to the user as a raw number. */
private val MOOD_SCORES = mapOf("😞" to 1, "😐" to 2, "🙂" to 3, "😄" to 4, "🤩" to 5)

@Inject
class GrowthViewModel(private val sessionStorage: SessionStorage) : ViewModel() {

    private val today = todaySimpleDate()
    private val birthDate by lazy {
        SimpleDate(sessionStorage.birthYear(), sessionStorage.birthMonth(), sessionStorage.birthDay())
    }

    private val _state = MutableStateFlow(GrowthState.Content())
    val state = _state.asStateFlow()

    fun onAction(action: GrowthAction) {
        val content = state.value as? GrowthState.Content ?: return
        when (action) {
            is GrowthAction.OnTabSelected -> _state.value = content.copy(tab = action.tab)
            is GrowthAction.OnNewHabitTitleChanged -> _state.value = content.copy(newHabitTitle = action.value)
            GrowthAction.OnAddHabit -> addHabit(content)
            is GrowthAction.OnToggleHabit -> toggleHabit(content, action.id)
            is GrowthAction.OnDeleteHabit -> deleteHabit(content, action.id)
            is GrowthAction.OnMoodSelected -> _state.value = content.copy(selectedMood = action.mood)
            is GrowthAction.OnMoodNoteChanged -> _state.value = content.copy(newMoodNote = action.value)
            GrowthAction.OnSaveMood -> saveMood(content)
            is GrowthAction.OnDeleteMood -> deleteMood(content, action.id)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun addHabit(content: GrowthState.Content) {
        if (content.newHabitTitle.isBlank()) return
        val id = Uuid.random().toString()
        val records = sessionStorage.habitRecords() + listOf(id, content.newHabitTitle).joinToString(FIELD_SEPARATOR)
        sessionStorage.saveHabitRecords(records)
        _state.value = content.copy(habits = buildHabits(), newHabitTitle = "")
    }

    private fun toggleHabit(content: GrowthState.Content, id: String) {
        val todayRecord = completionRecord(id, today)
        val completions = sessionStorage.habitCompletionRecords().toMutableList()
        if (!completions.remove(todayRecord)) completions.add(todayRecord)
        sessionStorage.saveHabitCompletionRecords(completions)
        _state.value = content.copy(habits = buildHabits())
    }

    private fun deleteHabit(content: GrowthState.Content, id: String) {
        sessionStorage.saveHabitRecords(
            sessionStorage.habitRecords().filterNot { it.substringBefore(FIELD_SEPARATOR) == id }
        )
        sessionStorage.saveHabitCompletionRecords(
            sessionStorage.habitCompletionRecords().filterNot { it.substringBefore(FIELD_SEPARATOR) == id }
        )
        _state.value = content.copy(habits = buildHabits())
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun saveMood(content: GrowthState.Content) {
        val entry = MoodEntryUi(Uuid.random().toString(), content.selectedMood, content.newMoodNote, today.year, today.month, today.day)
        val records = sessionStorage.moodRecords() + entry.toRecord()
        sessionStorage.saveMoodRecords(records)
        val moods = listOf(entry) + content.moods
        _state.value = content.copy(moods = moods, newMoodNote = "", moodInsight = computeMoodInsight(moods))
    }

    private fun deleteMood(content: GrowthState.Content, id: String) {
        sessionStorage.saveMoodRecords(
            sessionStorage.moodRecords().filterNot { it.substringBefore(FIELD_SEPARATOR) == id }
        )
        val moods = content.moods.filterNot { it.id == id }
        _state.value = content.copy(moods = moods, moodInsight = computeMoodInsight(moods))
    }

    private fun completionRecord(habitId: String, date: SimpleDate): String =
        listOf(habitId, date.year.toString(), date.month.toString(), date.day.toString()).joinToString(FIELD_SEPARATOR)

    private fun String.toHabitDefinitionOrNull(): Pair<String, String>? {
        val parts = split(FIELD_SEPARATOR)
        if (parts.size != 2) return null
        return parts[0] to parts[1]
    }

    private fun String.toCompletionOrNull(): Pair<String, SimpleDate>? {
        val parts = split(FIELD_SEPARATOR)
        if (parts.size != 4) return null
        val year = parts[1].toIntOrNull() ?: return null
        val month = parts[2].toIntOrNull() ?: return null
        val day = parts[3].toIntOrNull() ?: return null
        return parts[0] to SimpleDate(year, month, day)
    }

    private fun MoodEntryUi.toRecord(): String =
        listOf(id, mood, note, year.toString(), month.toString(), day.toString()).joinToString(FIELD_SEPARATOR)

    private fun String.toMoodOrNull(): MoodEntryUi? {
        val parts = split(FIELD_SEPARATOR)
        if (parts.size != 6) return null
        val year = parts[3].toIntOrNull() ?: return null
        val month = parts[4].toIntOrNull() ?: return null
        val day = parts[5].toIntOrNull() ?: return null
        return MoodEntryUi(parts[0], parts[1], parts[2], year, month, day)
    }

    private fun buildHabits(): List<HabitUi> {
        val definitions = sessionStorage.habitRecords().mapNotNull { it.toHabitDefinitionOrNull() }
        val completionsByHabit = sessionStorage.habitCompletionRecords()
            .mapNotNull { it.toCompletionOrNull() }
            .groupBy({ it.first }, { it.second })
        return definitions.map { (id, title) ->
            val dates = completionsByHabit[id]?.toSet() ?: emptySet()
            HabitUi(id = id, title = title, doneToday = today in dates, streak = streakFor(dates))
        }
    }

    /**
     * Counts consecutive completed days ending today - or ending yesterday if today isn't done
     * yet, so the streak doesn't visibly "break" the moment the clock rolls over past midnight
     * before the user has had a chance to check in.
     */
    private fun streakFor(dates: Set<SimpleDate>): Int {
        var streak = 0
        var cursor = if (today in dates) today else DateMath.previousDay(today)
        while (cursor in dates) {
            streak++
            cursor = DateMath.previousDay(cursor)
        }
        return streak
    }

    /**
     * Looks for a personal-day number where logged moods are, on average, meaningfully better
     * than a neutral score - purely a local pattern in the user's own data, no claim beyond that.
     */
    private fun computeMoodInsight(moods: List<MoodEntryUi>): String? {
        if (moods.size < 3) return null
        val byPersonalDay = moods
            .groupBy { PersonalDayCalculator.personalDay(birthDate, SimpleDate(it.year, it.month, it.day)) }
            .filterValues { it.size >= 2 }
        val best = byPersonalDay.maxByOrNull { (_, entries) -> entries.averageScore() } ?: return null
        if (best.value.averageScore() < 3.5) return null
        return "Похоже, дни с личным числом ${best.key} у вас чаще проходят лучше — вы отмечаете хорошее настроение заметно чаще обычного."
    }

    private fun List<MoodEntryUi>.averageScore(): Double =
        mapNotNull { MOOD_SCORES[it.mood] }.average()

    private fun load() {
        val moods = sessionStorage.moodRecords().mapNotNull { it.toMoodOrNull() }.reversed()
        val personalDay = PersonalDayCalculator.personalDay(birthDate, today)
        _state.value = (state.value as GrowthState.Content).copy(
            habits = buildHabits(),
            moods = moods,
            personalDayToday = personalDay,
            habitSuggestion = HabitSuggestions.forPersonalDay(personalDay),
            moodInsight = computeMoodInsight(moods)
        )
    }

    init { load() }
}
