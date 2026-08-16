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
import com.numera.app.core.commonMain.Ember
import com.numera.app.core.commonMain.Outline
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.PrimarySoft
import com.numera.app.core.commonMain.Surface
import com.numera.app.core.commonMain.components.BackButton
import com.numera.app.core.commonMain.displayFontFamily

@Composable
fun LifeCyclesScreen(state: LifeCyclesState, onBack: () -> Unit) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(horizontal = 20.dp)) {
            BackButton(onClick = onBack)
            when (state) {
                LifeCyclesState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Primary) }

                is LifeCyclesState.Content -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
                ) {
                    item {
                        Text(
                            text = "Жизненные циклы",
                            fontFamily = displayFontFamily(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    item { Spacer(modifier = Modifier.height(6.dp)) }
                    item {
                        Text(
                            text = "Четыре возрастных этапа, у каждого — своя точка опоры (пик) и своя сложность (вызов).",
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    items(state.stages) { StageCard(it) }
                }
            }
        }
    }
}

@Composable
private fun StageCard(stage: LifeStageUi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Surface)
            .border(1.dp, Outline, RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        Text(
            text = stage.ageRangeLabel.uppercase(),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        NumberLine(label = "Пик", number = stage.pinnacleNumber, text = stage.pinnacleText, accent = Primary, accentSoft = PrimarySoft)
        Spacer(modifier = Modifier.height(12.dp))
        NumberLine(label = "Вызов", number = stage.challengeNumber, text = stage.challengeText, accent = Ember, accentSoft = Ember.copy(alpha = 0.15f))
    }
}

@Composable
private fun NumberLine(label: String, number: Int, text: String, accent: androidx.compose.ui.graphics.Color, accentSoft: androidx.compose.ui.graphics.Color) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(accentSoft),
            contentAlignment = Alignment.Center
        ) {
            Text(text = number.toString(), fontFamily = displayFontFamily(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = accent)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accent)
            Text(text = text, fontSize = 13.sp, lineHeight = 19.sp, color = MaterialTheme.colorScheme.onBackground)
        }
    }
}
