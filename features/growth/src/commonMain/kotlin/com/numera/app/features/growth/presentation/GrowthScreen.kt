package com.numera.app.features.growth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numera.app.core.commonMain.Muted
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.Success
import com.numera.app.core.commonMain.Surface
import com.numera.app.core.commonMain.components.DefaultNumeraButton
import com.numera.app.features.growth.presentation.model.GrowthTab
import com.numera.app.features.growth.presentation.model.HabitUi
import com.numera.app.features.growth.presentation.model.MoodEntryUi

private val MOOD_OPTIONS = listOf("😞", "😐", "🙂", "😄", "🤩")
private val SHORT_MONTHS = listOf("янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек")

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
                text = if (tab == GrowthTab.HABITS) "Привычки" else "Настроение",
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
        InsightCard(text = "Число дня ${state.personalDayToday}: ${state.habitSuggestion}", accent = Primary)
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = state.newHabitTitle,
                onValueChange = { onAction(GrowthAction.OnNewHabitTitleChanged(it)) },
                placeholder = { Text("Новая привычка") }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        DefaultNumeraButton(text = "Добавить", onClick = { onAction(GrowthAction.OnAddHabit) })
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.habits) { habit -> HabitRow(habit, onAction) }
        }
    }
}

@Composable
private fun HabitRow(habit: HabitUi, onAction: (GrowthAction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = habit.doneToday, onCheckedChange = { onAction(GrowthAction.OnToggleHabit(habit.id)) })
        Column(modifier = Modifier.weight(1f)) {
            Text(text = habit.title, color = MaterialTheme.colorScheme.onBackground)
            if (habit.streak > 0) {
                Text(
                    text = "${habit.streak} ${dayWord(habit.streak)} подряд",
                    fontSize = 12.sp,
                    color = Primary
                )
            }
        }
        Text(
            modifier = Modifier.clickable { onAction(GrowthAction.OnDeleteHabit(habit.id)) }.padding(8.dp),
            text = "×",
            fontSize = 18.sp,
            color = Muted
        )
    }
}

private fun dayWord(count: Int): String {
    val mod100 = count % 100
    val mod10 = count % 10
    return when {
        mod100 in 11..14 -> "дней"
        mod10 == 1 -> "день"
        mod10 in 2..4 -> "дня"
        else -> "дней"
    }
}

@Composable
private fun MoodTab(state: GrowthState.Content, onAction: (GrowthAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        state.moodInsight?.let {
            InsightCard(text = it, accent = Success)
            Spacer(modifier = Modifier.height(12.dp))
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
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.newMoodNote,
            onValueChange = { onAction(GrowthAction.OnMoodNoteChanged(it)) },
            placeholder = { Text("Что сегодня повлияло на настроение?") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        DefaultNumeraButton(text = "Сохранить", onClick = { onAction(GrowthAction.OnSaveMood) })
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.moods) { entry -> MoodRow(entry) }
        }
    }
}

@Composable
private fun MoodRow(entry: MoodEntryUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = entry.mood, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = entry.note,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )
        Text(text = "${entry.day} ${SHORT_MONTHS[entry.month - 1]}", fontSize = 11.sp, color = Muted)
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
