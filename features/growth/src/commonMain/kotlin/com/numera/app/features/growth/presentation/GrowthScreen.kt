package com.numera.app.features.growth.presentation

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.Surface
import com.numera.app.core.commonMain.components.DefaultNumeraButton
import com.numera.app.features.growth.presentation.model.GrowthTab
import com.numera.app.features.growth.presentation.model.HabitUi
import com.numera.app.features.growth.presentation.model.MoodEntryUi

private val MOOD_OPTIONS = listOf("😞", "😐", "🙂", "😄", "🤩")

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
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = state.newHabitTitle,
                onValueChange = { onAction(GrowthAction.OnNewHabitTitleChanged(it)) },
                placeholder = { Text("Новая привычка") }
            )
            Spacer(modifier = Modifier.height(8.dp))
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
        Checkbox(checked = habit.done, onCheckedChange = { onAction(GrowthAction.OnToggleHabit(habit.id)) })
        Text(text = habit.title, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun MoodTab(state: GrowthState.Content, onAction: (GrowthAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
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
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = entry.note, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
    }
}
