package com.numera.app.features.numbers.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.numera.app.core.commonMain.Success
import com.numera.app.core.commonMain.Surface
import com.numera.app.core.commonMain.components.BackButton
import com.numera.app.core.commonMain.displayFontFamily

@Composable
fun KarmicAnalysisScreen(state: KarmicAnalysisState, onBack: () -> Unit) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(horizontal = 20.dp)) {
            BackButton(onClick = onBack)
            when (state) {
                KarmicAnalysisState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Primary) }

                is KarmicAnalysisState.Content -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
                ) {
                    item {
                        Text(
                            text = "Кармический анализ",
                            fontFamily = displayFontFamily(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item { SectionLabel("Кармические долги") }
                    if (state.debts.isEmpty()) {
                        item { EmptyNote("Активных кармических долгов не найдено — ваш путь в этой сфере складывался без резких проверок.") }
                    } else {
                        items(state.debts) { KarmicRow(it.number.toString(), it.text) }
                    }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                    item { SectionLabel("Кармические уроки") }
                    item {
                        Text(
                            text = "Числа, которых нет среди букв вашего имени — качества, которые не даны по умолчанию и требуют осознанной практики.",
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                    if (state.lessons.isEmpty()) {
                        item { EmptyNote("В вашем имени представлены все числа от 1 до 9 — редкий и сбалансированный набор.") }
                    } else {
                        items(state.lessons) { KarmicRow(it.number.toString(), it.text) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun KarmicRow(number: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Surface)
            .border(1.dp, Outline, RoundedCornerShape(4.dp))
            .padding(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(PrimarySoft),
            contentAlignment = Alignment.Center
        ) {
            Text(text = number, fontFamily = displayFontFamily(), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Primary)
        }
        Text(
            text = text,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun EmptyNote(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Surface)
            .border(1.dp, Outline, RoundedCornerShape(4.dp))
            .padding(14.dp)
    ) {
        Text(text = text, fontSize = 13.sp, lineHeight = 19.sp, color = Success)
    }
}
