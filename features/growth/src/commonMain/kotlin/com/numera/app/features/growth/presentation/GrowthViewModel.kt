package com.numera.app.features.growth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.getPluralString
import numera.features.growth.generated.resources.Res
import numera.features.growth.generated.resources.mood_insight
import numera.features.growth.generated.resources.weekly_summary
import numera.features.growth.generated.resources.mood_entries
import numera.features.growth.generated.resources.score_label_1
import numera.features.growth.generated.resources.score_label_2
import numera.features.growth.generated.resources.score_label_3
import numera.features.growth.generated.resources.score_label_4
import numera.features.growth.generated.resources.score_label_5

private const val FIELD_SEPARATOR = ";;"
private const val TAG_SEPARATOR = ","
private const val TREND_DAYS = 14

/** Mood -> a 1-5 score, used only to find which personal-day numbers tend to correlate with a
 *  better mood - never shown to the user as a raw number. */
private val MOOD_SCORES = mapOf("😞" to 1, "😐" to 2, "🙂" to 3, "😄" to 4, "🤩" to 5)
private val SCORE_EMOJI = mapOf(1 to "😞", 2 to "😐", 3 to "🙂", 4 to "😄", 5 to "🤩")
private val SCORE_LABELS = mapOf(
    1 to Res.string.score_label_1,
    2 to Res.string.score_label_2,
    3 to Res.string.score_label_3,
    4 to Res.string.score_label_4,
    5 to Res.string.score_label_5
)

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
            GrowthAction.OnSaveMood -> viewModelScope.launch { saveMood(content) }
            is GrowthAction.OnDeleteMood -> viewModelScope.launch { deleteMood(content, action.id) }
            is GrowthAction.OnTagToggled -> toggleTag(content, action.tag)
            is GrowthAction.OnMoodEntryClick -> startEditingMood(content, action.id)
            GrowthAction.OnCancelMoodEdit -> cancelMoodEdit(content)
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

    private fun toggleTag(content: GrowthState.Content, tag: String) {
        val tags = content.selectedTags.toMutableSet()
        if (!tags.remove(tag)) tags.add(tag)
        _state.value = content.copy(selectedTags = tags)
    }

    private fun startEditingMood(content: GrowthState.Content, id: String) {
        val entry = content.moods.find { it.id == id } ?: return
        _state.value = content.copy(
            editingMoodId = id,
            selectedMood = entry.mood,
            newMoodNote = entry.note,
            selectedTags = entry.tags.toSet()
        )
    }

    private fun cancelMoodEdit(content: GrowthState.Content) {
        _state.value = content.copy(
            editingMoodId = null,
            selectedMood = "🙂",
            newMoodNote = "",
            selectedTags = emptySet()
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun saveMood(content: GrowthState.Content) {
        val editingId = content.editingMoodId
        val moods = if (editingId != null) {
            val existing = content.moods.find { it.id == editingId } ?: return
            val updated = existing.copy(
                mood = content.selectedMood,
                note = content.newMoodNote,
                tags = content.selectedTags.toList()
            )
            sessionStorage.saveMoodRecords(
                sessionStorage.moodRecords().map { record ->
                    if (record.substringBefore(FIELD_SEPARATOR) == editingId) updated.toRecord() else record
                }
            )
            content.moods.map { if (it.id == editingId) updated else it }
        } else {
            val entry = MoodEntryUi(
                id = Uuid.random().toString(),
                mood = content.selectedMood,
                note = content.newMoodNote,
                tags = content.selectedTags.toList(),
                year = today.year,
                month = today.month,
                day = today.day
            )
            sessionStorage.saveMoodRecords(sessionStorage.moodRecords() + entry.toRecord())
            listOf(entry) + content.moods
        }
        _state.value = content.copy(
            moods = moods,
            newMoodNote = "",
            selectedMood = "🙂",
            selectedTags = emptySet(),
            editingMoodId = null,
            moodInsight = computeMoodInsight(moods),
            weeklySummary = computeWeeklySummary(moods),
            moodTrend = computeMoodTrend(moods)
        )
    }

    private suspend fun deleteMood(content: GrowthState.Content, id: String) {
        sessionStorage.saveMoodRecords(
            sessionStorage.moodRecords().filterNot { it.substringBefore(FIELD_SEPARATOR) == id }
        )
        val moods = content.moods.filterNot { it.id == id }
        _state.value = content.copy(
            moods = moods,
            editingMoodId = if (content.editingMoodId == id) null else content.editingMoodId,
            moodInsight = computeMoodInsight(moods),
            weeklySummary = computeWeeklySummary(moods),
            moodTrend = computeMoodTrend(moods)
        )
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
        listOf(id, mood, note, tags.joinToString(TAG_SEPARATOR), year.toString(), month.toString(), day.toString())
            .joinToString(FIELD_SEPARATOR)

    /** Accepts both the current 7-field format and the pre-tags 6-field format, so entries saved
     *  before tags existed don't silently vanish from the list. */
    private fun String.toMoodOrNull(): MoodEntryUi? {
        val parts = split(FIELD_SEPARATOR)
        return when (parts.size) {
            7 -> {
                val year = parts[4].toIntOrNull() ?: return null
                val month = parts[5].toIntOrNull() ?: return null
                val day = parts[6].toIntOrNull() ?: return null
                val tags = parts[3].split(TAG_SEPARATOR).filter { it.isNotBlank() }
                MoodEntryUi(parts[0], parts[1], parts[2], tags, year, month, day)
            }
            6 -> {
                val year = parts[3].toIntOrNull() ?: return null
                val month = parts[4].toIntOrNull() ?: return null
                val day = parts[5].toIntOrNull() ?: return null
                MoodEntryUi(parts[0], parts[1], parts[2], emptyList(), year, month, day)
            }
            else -> null
        }
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
    private suspend fun computeMoodInsight(moods: List<MoodEntryUi>): String? {
        if (moods.size < 3) return null
        val byPersonalDay = moods
            .groupBy { PersonalDayCalculator.personalDay(birthDate, SimpleDate(it.year, it.month, it.day)) }
            .filterValues { it.size >= 2 }
        val best = byPersonalDay.maxByOrNull { (_, entries) -> entries.averageScore() } ?: return null
        if (best.value.averageScore() < 3.5) return null
        return getString(Res.string.mood_insight, best.key)
    }

    private suspend fun computeWeeklySummary(moods: List<MoodEntryUi>): String? {
        val last7 = lastNDays(7)
        val thisWeek = moods.filter { SimpleDate(it.year, it.month, it.day) in last7 }
        if (thisWeek.isEmpty()) return null
        val avg = thisWeek.averageScore()
        val rounded = avg.roundToInt().coerceIn(1, 5)
        val emoji = SCORE_EMOJI.getValue(rounded)
        val label = getString(SCORE_LABELS.getValue(rounded))
        val entriesPhrase = getPluralString(Res.plurals.mood_entries, thisWeek.size, thisWeek.size)
        return getString(Res.string.weekly_summary, emoji, label, entriesPhrase)
    }

    private fun computeMoodTrend(moods: List<MoodEntryUi>): List<Double?> {
        val byDate = moods.groupBy { SimpleDate(it.year, it.month, it.day) }
        val dates = mutableListOf<SimpleDate>()
        var cursor = today
        repeat(TREND_DAYS) {
            dates.add(cursor)
            cursor = DateMath.previousDay(cursor)
        }
        return dates.reversed().map { date -> byDate[date]?.averageScore() }
    }

    private fun lastNDays(n: Int): Set<SimpleDate> {
        val dates = mutableSetOf<SimpleDate>()
        var cursor = today
        repeat(n) {
            dates += cursor
            cursor = DateMath.previousDay(cursor)
        }
        return dates
    }

    private fun List<MoodEntryUi>.averageScore(): Double =
        mapNotNull { MOOD_SCORES[it.mood] }.average()

    private suspend fun load() {
        val moods = sessionStorage.moodRecords().mapNotNull { it.toMoodOrNull() }.reversed()
        val personalDay = PersonalDayCalculator.personalDay(birthDate, today)
        _state.value = (state.value as GrowthState.Content).copy(
            habits = buildHabits(),
            moods = moods,
            personalDayToday = personalDay,
            habitSuggestion = HabitSuggestions.forPersonalDay(personalDay),
            moodInsight = computeMoodInsight(moods),
            weeklySummary = computeWeeklySummary(moods),
            moodTrend = computeMoodTrend(moods)
        )
    }

    init { viewModelScope.launch { load() } }
}
