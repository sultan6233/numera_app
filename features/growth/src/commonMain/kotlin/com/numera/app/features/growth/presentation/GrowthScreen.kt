package com.numera.app.features.growth.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numera.app.core.commonMain.Ember
import com.numera.app.core.commonMain.Error
import com.numera.app.core.commonMain.Muted
import com.numera.app.core.commonMain.OnBackground
import com.numera.app.core.commonMain.Outline
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.Success
import com.numera.app.core.commonMain.Surface
import com.numera.app.core.commonMain.components.DefaultNumeraButton
import com.numera.app.core.commonMain.components.DefaultNumeraOutlinedButton
import com.numera.app.features.growth.domain.MoodTags
import com.numera.app.features.growth.presentation.model.GrowthTab
import com.numera.app.features.growth.presentation.model.HabitUi
import com.numera.app.features.growth.presentation.model.MoodEntryUi
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.pluralStringResource
import numera.features.growth.generated.resources.Res
import numera.features.growth.generated.resources.tab_habits
import numera.features.growth.generated.resources.tab_mood
import numera.features.growth.generated.resources.habit_today_insight
import numera.features.growth.generated.resources.new_habit_placeholder
import numera.features.growth.generated.resources.add_button
import numera.features.growth.generated.resources.swipe_hint_habits
import numera.features.growth.generated.resources.delete_habit_title
import numera.features.growth.generated.resources.delete_habit_text
import numera.features.growth.generated.resources.editing_mood_entry
import numera.features.growth.generated.resources.mood_note_placeholder
import numera.features.growth.generated.resources.update_button
import numera.features.growth.generated.resources.save_button
import numera.features.growth.generated.resources.cancel_button
import numera.features.growth.generated.resources.swipe_hint_mood
import numera.features.growth.generated.resources.delete_mood_title
import numera.features.growth.generated.resources.delete_mood_text
import numera.features.growth.generated.resources.mood_trend_title
import numera.features.growth.generated.resources.delete_action
import numera.features.growth.generated.resources.streak_days
import numera.features.growth.generated.resources.month_short_1
import numera.features.growth.generated.resources.month_short_2
import numera.features.growth.generated.resources.month_short_3
import numera.features.growth.generated.resources.month_short_4
import numera.features.growth.generated.resources.month_short_5
import numera.features.growth.generated.resources.month_short_6
import numera.features.growth.generated.resources.month_short_7
import numera.features.growth.generated.resources.month_short_8
import numera.features.growth.generated.resources.month_short_9
import numera.features.growth.generated.resources.month_short_10
import numera.features.growth.generated.resources.month_short_11
import numera.features.growth.generated.resources.month_short_12

private val MOOD_OPTIONS = listOf("😞", "😐", "🙂", "😄", "🤩")
private val SHORT_MONTH_RES = listOf(
    Res.string.month_short_1, Res.string.month_short_2, Res.string.month_short_3, Res.string.month_short_4,
    Res.string.month_short_5, Res.string.month_short_6, Res.string.month_short_7, Res.string.month_short_8,
    Res.string.month_short_9, Res.string.month_short_10, Res.string.month_short_11, Res.string.month_short_12
)

@Composable
fun GrowthScreen(state: GrowthState, onAction: (GrowthAction) -> Unit) {
    Scaffold { innerPadding ->
        when (state) {
            is GrowthState.Content -> Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
                TabRow(state.tab, onAction)
                Spacer(modifier = Modifier.height(16.dp))
                when (state.tab) {
                    GrowthTab.HABITS -> HabitsTab(state, onAction)
                    GrowthTab.MOOD -> MoodTab(state, onAction)
                }
            }
        }
    }
}

@Composable
private fun TabRow(selected: GrowthTab, onAction: (GrowthAction) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        GrowthTab.entries.forEach { tab ->
            Text(
                modifier = Modifier.clickable { onAction(GrowthAction.OnTabSelected(tab)) },
                text = if (tab == GrowthTab.HABITS) stringResource(Res.string.tab_habits) else stringResource(Res.string.tab_mood),
                fontSize = 16.sp,
                fontWeight = if (tab == selected) FontWeight.Bold else FontWeight.Normal,
                color = if (tab == selected) Primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HabitsTab(state: GrowthState.Content, onAction: (GrowthAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        InsightCard(text = stringResource(Res.string.habit_today_insight, state.personalDayToday, state.habitSuggestion), accent = Primary)
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = state.newHabitTitle,
                onValueChange = { onAction(GrowthAction.OnNewHabitTitleChanged(it)) },
                placeholder = { Text(stringResource(Res.string.new_habit_placeholder)) }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        DefaultNumeraButton(text = stringResource(Res.string.add_button), onClick = { onAction(GrowthAction.OnAddHabit) })
        Spacer(modifier = Modifier.height(16.dp))
        if (state.habits.isNotEmpty()) {
            Text(
                text = stringResource(Res.string.swipe_hint_habits),
                fontSize = 11.sp,
                color = Muted,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.habits, key = { it.id }) { habit ->
                SwipeToDeleteRow(
                    onDelete = { onAction(GrowthAction.OnDeleteHabit(habit.id)) },
                    confirmTitle = stringResource(Res.string.delete_habit_title),
                    confirmText = stringResource(Res.string.delete_habit_text, habit.title)
                ) {
                    Checkbox(checked = habit.doneToday, onCheckedChange = { onAction(GrowthAction.OnToggleHabit(habit.id)) })
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = habit.title, color = MaterialTheme.colorScheme.onBackground)
                        if (habit.streak > 0) {
                            Text(
                                text = pluralStringResource(Res.plurals.streak_days, habit.streak, habit.streak),
                                fontSize = 12.sp,
                                color = Primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodTab(state: GrowthState.Content, onAction: (GrowthAction) -> Unit) {
    val editing = state.editingMoodId != null
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Column {
                state.weeklySummary?.let {
                    InsightCard(text = it, accent = Primary)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                state.moodInsight?.let {
                    InsightCard(text = it, accent = Success)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (state.moodTrend.isNotEmpty()) {
                    MoodTrendChart(trend = state.moodTrend)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                if (editing) {
                    Text(
                        text = stringResource(Res.string.editing_mood_entry),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MOOD_OPTIONS.forEach { mood ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (mood == state.selectedMood) Primary else Surface)
                                .clickable { onAction(GrowthAction.OnMoodSelected(mood)) }
                                .padding(10.dp)
                        ) {
                            Text(text = mood, fontSize = 20.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                TagsRow(
                    selected = state.selectedTags,
                    onToggle = { onAction(GrowthAction.OnTagToggled(it)) }
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.newMoodNote,
                    onValueChange = { onAction(GrowthAction.OnMoodNoteChanged(it)) },
                    placeholder = { Text(stringResource(Res.string.mood_note_placeholder)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    DefaultNumeraButton(
                        modifier = Modifier.weight(1f),
                        text = if (editing) stringResource(Res.string.update_button) else stringResource(Res.string.save_button),
                        onClick = { onAction(GrowthAction.OnSaveMood) }
                    )
                    if (editing) {
                        DefaultNumeraOutlinedButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(Res.string.cancel_button),
                            onClick = { onAction(GrowthAction.OnCancelMoodEdit) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (state.moods.isNotEmpty()) {
                    Text(
                        text = stringResource(Res.string.swipe_hint_mood),
                        fontSize = 11.sp,
                        color = Muted,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
        }
        items(state.moods, key = { it.id }) { entry ->
            SwipeToDeleteRow(
                onDelete = { onAction(GrowthAction.OnDeleteMood(entry.id)) },
                onClick = { onAction(GrowthAction.OnMoodEntryClick(entry.id)) },
                confirmTitle = stringResource(Res.string.delete_mood_title),
                confirmText = stringResource(Res.string.delete_mood_text)
            ) {
                Text(text = entry.mood, fontSize = 20.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    if (entry.note.isNotBlank()) {
                        Text(text = entry.note, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                    }
                    if (entry.tags.isNotEmpty()) {
                        val tagLabels = entry.tags.map { stringResource(MoodTags.labelFor(it)) }
                        Text(
                            text = tagLabels.joinToString(" · "),
                            color = Primary,
                            fontSize = 11.sp
                        )
                    }
                }
                Text(text = "${entry.day} ${stringResource(SHORT_MONTH_RES[entry.month - 1])}", fontSize = 11.sp, color = Muted)
            }
        }
    }
}

@Composable
private fun TagsRow(selected: Set<String>, onToggle: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MoodTags.ALL.forEach { tag ->
            val isSelected = tag.id in selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (isSelected) Primary else Surface)
                    .border(1.dp, if (isSelected) Primary else Outline, RoundedCornerShape(50))
                    .clickable { onToggle(tag.id) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(tag.labelRes),
                    fontSize = 12.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/** Bar chart of average mood score per day for the last 14 days - today's bar is always
 *  highlighted in Primary so it's easy to spot the trend leading up to now. */
@Composable
private fun MoodTrendChart(trend: List<Double?>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(Res.string.mood_trend_title),
            fontSize = 11.sp,
            color = Muted,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Canvas(modifier = Modifier.fillMaxWidth().height(56.dp)) {
            if (trend.isEmpty()) return@Canvas
            val barWidth = size.width / trend.size
            val gap = barWidth * 0.28f
            trend.forEachIndexed { index, score ->
                val isToday = index == trend.lastIndex
                val heightFraction = score?.let { (it / 5.0).toFloat().coerceIn(0.12f, 1f) } ?: 0.05f
                val barHeight = size.height * heightFraction
                val barColor = when {
                    score == null -> Outline
                    isToday -> Primary
                    score >= 3.5 -> Success
                    score <= 2.0 -> Ember
                    else -> Primary
                }
                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(index * barWidth + gap / 2f, size.height - barHeight),
                    size = Size(barWidth - gap, barHeight),
                    cornerRadius = CornerRadius(3f, 3f)
                )
            }
        }
    }
}

/**
 * Swipe-left-to-delete with a confirmation dialog before anything actually happens - a swipe
 * past the threshold reveals a red "Удалить" panel and asks; canceling snaps the row back
 * instead of leaving it stranded mid-swipe. An optional [onClick] lets the row double as a tap
 * target (used by mood entries to open edit mode) alongside the swipe gesture.
 */
@Composable
private fun SwipeToDeleteRow(
    onDelete: () -> Unit,
    confirmTitle: String,
    confirmText: String,
    onClick: (() -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {
    // Two pieces of state on purpose: dragOffsetPx updates synchronously (no coroutine) so it's
    // always exactly current the instant the finger lifts, and animatedOffset only takes over
    // once dragging stops, for the settle animation. Driving both the live drag AND the settle
    // through a single Animatable via scope.launch{} per delta (the previous approach) let those
    // fire-and-forget launches race the settle animation - sometimes the animation is what's
    // catching up to the finger, but a stray delta arriving after the settle already started
    // could stomp its target back to a stale value, so the row occasionally lands with no visible
    // animation, and worse, the threshold check in onDragStopped could read that same stale value
    // and decide "not far enough" even though the finger clearly passed it.
    var isDragging by remember { mutableStateOf(false) }
    var dragOffsetPx by remember { mutableFloatStateOf(0f) }
    val animatedOffset = remember { Animatable(0f) }
    var showConfirm by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val deleteThresholdPx = with(density) { 88.dp.toPx() }
    val maxDragPx = with(density) { 140.dp.toPx() }
    val currentOffset = if (isDragging) dragOffsetPx else animatedOffset.value

    fun resetOffset() {
        scope.launch { animatedOffset.animateTo(0f) }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(12.dp))
                .background(Error),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(Res.string.delete_action), color = OnBackground, fontSize = 13.sp, modifier = Modifier.padding(end = 20.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(currentOffset.roundToInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        dragOffsetPx = (dragOffsetPx + delta).coerceIn(-maxDragPx, 0f)
                    },
                    onDragStarted = {
                        isDragging = true
                        dragOffsetPx = animatedOffset.value
                    },
                    onDragStopped = {
                        isDragging = false
                        val finalOffset = dragOffsetPx
                        animatedOffset.snapTo(finalOffset)
                        if (finalOffset < -deleteThresholdPx) {
                            animatedOffset.animateTo(-maxDragPx)
                            showConfirm = true
                        } else {
                            animatedOffset.animateTo(0f)
                        }
                    }
                )
                .clip(RoundedCornerShape(12.dp))
                .background(Surface)
                .clickable(enabled = onClick != null) { onClick?.invoke() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = {
                showConfirm = false
                resetOffset()
            },
            confirmButton = {
                Text(
                    modifier = Modifier.clickable {
                        showConfirm = false
                        onDelete()
                    }.padding(8.dp),
                    text = stringResource(Res.string.delete_action),
                    color = Error
                )
            },
            dismissButton = {
                Text(
                    modifier = Modifier.clickable {
                        showConfirm = false
                        resetOffset()
                    }.padding(8.dp),
                    text = stringResource(Res.string.cancel_button)
                )
            },
            title = { Text(confirmTitle) },
            text = { Text(confirmText) }
        )
    }
}

@Composable
private fun InsightCard(text: String, accent: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Surface)
            .border(1.dp, accent, RoundedCornerShape(4.dp))
            .padding(12.dp)
    ) {
        Text(text = text, fontSize = 13.sp, lineHeight = 19.sp, color = MaterialTheme.colorScheme.onBackground)
    }
}
