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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.numera.app.core.commonMain.Muted
import com.numera.app.core.commonMain.Outline
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.PrimarySoft
import com.numera.app.core.commonMain.Surface
import com.numera.app.core.commonMain.components.NumberWheel
import com.numera.app.core.commonMain.components.NumeraGlyph
import com.numera.app.core.commonMain.components.NumeraIcon
import com.numera.app.core.commonMain.components.shimmer
import com.numera.app.core.commonMain.displayFontFamily
import com.numera.app.features.numbers.presentation.model.NumberCardUi
import com.numera.app.features.numbers.presentation.model.NumberType

private fun glyphFor(type: String): NumeraGlyph = when (type) {
    NumberType.LIFE_PATH -> NumeraGlyph.Compass
    NumberType.EXPRESSION -> NumeraGlyph.Star
    NumberType.SOUL_URGE -> NumeraGlyph.Spiral
    NumberType.PERSONALITY -> NumeraGlyph.Duality
    NumberType.HEALTH -> NumeraGlyph.Leaf
    NumberType.BUSINESS -> NumeraGlyph.Case
    else -> NumeraGlyph.Grid
}

@Composable
fun NumbersScreen(state: NumbersState, onAction: (NumbersAction) -> Unit) {
    Scaffold { innerPadding ->
        when (state) {
            is NumbersState.Content -> Column(
                modifier = Modifier.padding(innerPadding).fillMaxSize().padding(horizontal = 20.dp).padding(top = 20.dp)
            ) {
                Text(
                    text = "Ваша карта",
                    fontFamily = displayFontFamily(),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (!state.loading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        NumberWheel(
                            litNumbers = state.cards.filter { !it.locked }.map { it.value.reducedDigit() }.toSet(),
                            diameter = 200.dp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (state.loading) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        repeat(4) {
                            Box(modifier = Modifier.fillMaxWidth().height(72.dp).shimmer(RoundedCornerShape(4.dp)))
                        }
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(state.cards) { card ->
                            NumberCard(card, onClick = { onAction(NumbersAction.OnCardClick(card.type)) })
                        }
                        item {
                            CompatibilityEntryCard(onClick = { onAction(NumbersAction.OnCompatibilityClick) })
                        }
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }
                }
            }
        }
    }
}

/** Wheel nodes only go 1-9 - master numbers (11/22/33) light their reduced digit instead. */
private fun Int.reducedDigit(): Int {
    var n = this
    while (n > 9) n = n.toString().sumOf { it.digitToInt() }
    return n
}

@Composable
private fun CompatibilityEntryCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(PrimarySoft)
            .border(1.dp, Primary, RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Surface),
            contentAlignment = Alignment.Center
        ) {
            NumeraIcon(glyph = NumeraGlyph.Duality, tint = Primary, size = 20.dp)
        }
        Text(
            text = "Совместимость с близкими",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        Text(text = "→", fontSize = 16.sp, color = Primary)
    }
}

@Composable
private fun NumberCard(card: NumberCardUi, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Surface)
            .border(1.dp, Outline, RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(PrimarySoft),
            contentAlignment = Alignment.Center
        ) {
            NumeraIcon(glyph = glyphFor(card.type), tint = Primary, size = 20.dp)
        }
        Text(
            text = card.label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        if (card.locked) {
            NumeraIcon(glyph = NumeraGlyph.Lock, tint = Muted, size = 16.dp)
        } else {
            Text(
                text = card.value.toString(),
                fontFamily = displayFontFamily(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }
    }
}
