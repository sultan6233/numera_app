package com.numera.app.features.compatibility.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numera.app.core.commonMain.Outline
import com.numera.app.core.commonMain.Surface
import com.numera.app.core.commonMain.components.DefaultNumeraButton
import com.numera.app.features.compatibility.presentation.model.CompanionUi

@Composable
fun CompatibilityScreen(state: CompatibilityState, onAction: (CompatibilityAction) -> Unit) {
    Scaffold { innerPadding ->
        when (state) {
            is CompatibilityState.Content -> Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
                Text(text = "Совместимость", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.companions) { companion ->
                        CompanionRow(companion, onClick = { onAction(CompatibilityAction.OnCompanionClick(companion.id)) })
                    }
                }
                DefaultNumeraButton(text = "Добавить человека", onClick = { onAction(CompatibilityAction.OnAddClick) })

                if (state.showAddDialog) {
                    AddCompanionDialog(state, onAction)
                }
            }
        }
    }
}

@Composable
private fun CompanionRow(companion: CompanionUi, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Surface)
            .border(1.dp, Outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(text = companion.name, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun AddCompanionDialog(state: CompatibilityState.Content, onAction: (CompatibilityAction) -> Unit) {
    AlertDialog(
        onDismissRequest = { onAction(CompatibilityAction.OnDismissDialog) },
        confirmButton = {
            Text(
                modifier = Modifier.clickable { onAction(CompatibilityAction.OnSaveClick) }.padding(8.dp),
                text = "Сохранить"
            )
        },
        dismissButton = {
            Text(
                modifier = Modifier.clickable { onAction(CompatibilityAction.OnDismissDialog) }.padding(8.dp),
                text = "Отмена"
            )
        },
        title = { Text("Новый человек") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.newName,
                    onValueChange = { onAction(CompatibilityAction.OnNameChanged(it)) },
                    placeholder = { Text("Имя") }
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        modifier = Modifier.padding(0.dp),
                        value = state.newDay,
                        onValueChange = { onAction(CompatibilityAction.OnDayChanged(it)) },
                        placeholder = { Text("ДД") }
                    )
                    OutlinedTextField(
                        value = state.newMonth,
                        onValueChange = { onAction(CompatibilityAction.OnMonthChanged(it)) },
                        placeholder = { Text("ММ") }
                    )
                    OutlinedTextField(
                        value = state.newYear,
                        onValueChange = { onAction(CompatibilityAction.OnYearChanged(it)) },
                        placeholder = { Text("ГГГГ") }
                    )
                }
            }
        }
    )
}
