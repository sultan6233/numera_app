package com.numera.app.features.compatibility.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.numera.app.core.commonMain.components.BackButton
import com.numera.app.core.numerology.model.CompatibilityBreakdownItem
import org.jetbrains.compose.resources.stringResource
import numera.features.compatibility.generated.resources.Res
import numera.features.compatibility.generated.resources.you_and_label
import numera.features.compatibility.generated.resources.compat_life_path
import numera.features.compatibility.generated.resources.compat_expression
import numera.features.compatibility.generated.resources.compat_soul_urge
import numera.features.compatibility.generated.resources.compat_personality
import numera.features.compatibility.generated.resources.compat_label_strong
import numera.features.compatibility.generated.resources.compat_label_good
import numera.features.compatibility.generated.resources.compat_label_needs_work

@Composable
fun CompatibilityDetailScreen(state: CompatibilityDetailState, onBack: () -> Unit) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) { BackButton(onClick = onBack) }
            Spacer(modifier = Modifier.height(8.dp))
            when (state) {
                CompatibilityDetailState.Loading -> CircularProgressIndicator(color = Primary)
                is CompatibilityDetailState.Content -> {
                    Text(text = stringResource(Res.string.you_and_label, state.companionName), fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "${state.score}%", fontSize = 56.sp, fontWeight = FontWeight.Bold, color = Primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = stringResource(compatibilityLabelRes(state.labelKey)), fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                    if (state.breakdown.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(28.dp))
                        Column(modifier = Modifier.fillMaxWidth()) {
                            state.breakdown.forEach { BreakdownRow(it) }
                        }
                    }
                }
            }
        }
    }
}

private fun compatibilityLabelRes(labelKey: String) = when (labelKey) {
    "strong" -> Res.string.compat_label_strong
    "good" -> Res.string.compat_label_good
    else -> Res.string.compat_label_needs_work
}

private fun compatibilityCategoryRes(categoryKey: String) = when (categoryKey) {
    "life_path" -> Res.string.compat_life_path
    "expression" -> Res.string.compat_expression
    "soul_urge" -> Res.string.compat_soul_urge
    else -> Res.string.compat_personality
}

@Composable
private fun BreakdownRow(item: CompatibilityBreakdownItem) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(compatibilityCategoryRes(item.categoryKey)), fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground)
            Text(text = "${item.score}%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Primary)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(Outline)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(item.score / 100f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Primary)
            )
        }
    }
}
