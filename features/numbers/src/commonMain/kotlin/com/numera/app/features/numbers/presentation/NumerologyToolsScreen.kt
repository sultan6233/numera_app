package com.numera.app.features.numbers.presentation

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.numera.app.core.commonMain.Outline
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.PrimarySoft
import com.numera.app.core.commonMain.Surface
import com.numera.app.core.commonMain.components.DefaultNumeraButton
import com.numera.app.core.commonMain.displayFontFamily
import com.numera.app.features.numbers.domain.NumberMeanings

@Composable
fun NumerologyToolsScreen(state: NumerologyToolsState, onAction: (NumerologyToolsAction) -> Unit) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(20.dp)) {
            Text(
                text = "Инструменты нумерологии",
                fontFamily = displayFontFamily(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Рассчитайте число для названия бизнеса, адреса или телефона — по тем же таблицам, что и для личного имени.",
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ModeChip(
                    text = "Имя / бизнес",
                    selected = state.mode == ToolMode.NAME,
                    onClick = { onAction(NumerologyToolsAction.OnModeChanged(ToolMode.NAME)) },
                    modifier = Modifier.weight(1f)
                )
                ModeChip(
                    text = "Телефон / адрес",
                    selected = state.mode == ToolMode.DIGITS,
                    onClick = { onAction(NumerologyToolsAction.OnModeChanged(ToolMode.DIGITS)) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.input,
                onValueChange = { onAction(NumerologyToolsAction.OnInputChanged(it)) },
                placeholder = {
                    Text(if (state.mode == ToolMode.NAME) "Например, Ромашка" else "Например, +7 900 123-45-67")
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            DefaultNumeraButton(
                text = "Рассчитать",
                onClick = { onAction(NumerologyToolsAction.OnCalculateClick) },
                enabled = state.input.isNotBlank()
            )
            Spacer(modifier = Modifier.height(20.dp))
            state.result?.let { ResultCard(it) }
        }
    }
}

@Composable
private fun ModeChip(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (selected) PrimarySoft else Surface)
            .border(1.dp, if (selected) Primary else Outline, RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ResultCard(value: Int) {
    val meaning = NumberMeanings.meaningFor(value)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Surface)
            .border(1.dp, Outline, RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(PrimarySoft)
                    .border(1.dp, Primary, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = value.toString(), fontFamily = displayFontFamily(), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Primary)
            }
            Text(text = meaning.title, fontFamily = displayFontFamily(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = meaning.essence, fontSize = 13.sp, lineHeight = 19.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
