package com.numera.app.features.numbers.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numera.app.core.commonMain.Ember
import com.numera.app.core.commonMain.Outline
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.PrimarySoft
import com.numera.app.core.commonMain.Success
import com.numera.app.core.commonMain.Surface
import com.numera.app.core.commonMain.components.BackButton
import com.numera.app.core.commonMain.displayFontFamily
import com.numera.app.features.numbers.domain.NumberMeaning
import org.jetbrains.compose.resources.stringResource
import numera.features.numbers.generated.resources.Res
import numera.features.numbers.generated.resources.strengths_label
import numera.features.numbers.generated.resources.growth_points_label
import numera.features.numbers.generated.resources.how_to_use_label

@Composable
fun NumberDetailScreen(state: NumberDetailState, onBack: () -> Unit) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(horizontal = 20.dp)) {
            BackButton(onClick = onBack)
            when (state) {
                NumberDetailState.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }

                is NumberDetailState.Content -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
                ) {
                    item { Header(state) }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                    item { EssenceBlock(state.meaning) }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item { SectionLabel(stringResource(Res.string.strengths_label)) }
                    items(state.meaning.strengths) { TraitRow(stringResource(it), Success) }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item { SectionLabel(stringResource(Res.string.growth_points_label)) }
                    items(state.meaning.challenges) { TraitRow(stringResource(it), Ember) }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                    item { AdviceBlock(stringResource(state.meaning.advice)) }
                }
            }
        }
    }
}

@Composable
private fun Header(state: NumberDetailState.Content) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(PrimarySoft)
                .border(1.dp, Primary, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = state.value.toString(),
                fontFamily = displayFontFamily(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }
        Column {
            Text(text = state.categoryLabel, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = stringResource(state.meaning.title),
                fontFamily = displayFontFamily(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun EssenceBlock(meaning: NumberMeaning) {
    Text(
        text = stringResource(meaning.essence),
        fontSize = 15.sp,
        lineHeight = 23.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
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
private fun TraitRow(text: String, accent: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Box(
            modifier = Modifier
                .padding(top = 7.dp)
                .width(6.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(accent)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun AdviceBlock(advice: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Surface)
            .border(1.dp, Outline, RoundedCornerShape(4.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = stringResource(Res.string.how_to_use_label),
                fontFamily = displayFontFamily(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = advice,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
