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
import org.jetbrains.compose.resources.stringResource
import numera.features.numbers.generated.resources.Res
import numera.features.numbers.generated.resources.your_map_title
import numera.features.numbers.generated.resources.tool_karmic_analysis
import numera.features.numbers.generated.resources.tool_life_cycles
import numera.features.numbers.generated.resources.tool_numerology_tools
import numera.features.numbers.generated.resources.compatibility_with_loved_ones
import numera.features.numbers.generated.resources.personal_year_label
import numera.features.numbers.generated.resources.personal_month_label
import numera.features.numbers.generated.resources.lucky_numbers_label
import numera.features.numbers.generated.resources.lucky_color_label
import numera.features.numbers.generated.resources.color_red
import numera.features.numbers.generated.resources.color_orange
import numera.features.numbers.generated.resources.color_yellow
import numera.features.numbers.generated.resources.color_green
import numera.features.numbers.generated.resources.color_blue
import numera.features.numbers.generated.resources.color_indigo
import numera.features.numbers.generated.resources.color_violet
import numera.features.numbers.generated.resources.color_brown
import numera.features.numbers.generated.resources.color_gold
import numera.features.numbers.generated.resources.color_silver
import numera.features.numbers.generated.resources.color_emerald
import numera.features.numbers.generated.resources.color_white

private fun colorNameRes(key: String) = when (key) {
    "red" -> Res.string.color_red
    "orange" -> Res.string.color_orange
    "yellow" -> Res.string.color_yellow
    "green" -> Res.string.color_green
    "blue" -> Res.string.color_blue
    "indigo" -> Res.string.color_indigo
    "violet" -> Res.string.color_violet
    "brown" -> Res.string.color_brown
    "gold" -> Res.string.color_gold
    "silver" -> Res.string.color_silver
    "emerald" -> Res.string.color_emerald
    else -> Res.string.color_white
}

private fun glyphFor(type: String): NumeraGlyph = when (type) {
    NumberType.LIFE_PATH -> NumeraGlyph.Compass
    NumberType.EXPRESSION -> NumeraGlyph.Star
    NumberType.SOUL_URGE -> NumeraGlyph.Spiral
    NumberType.PERSONALITY -> NumeraGlyph.Duality
    NumberType.HEALTH -> NumeraGlyph.Leaf
    NumberType.BUSINESS -> NumeraGlyph.Case
    NumberType.MATURITY -> NumeraGlyph.Sun
    NumberType.HIDDEN_PASSION -> NumeraGlyph.Sprout
    NumberType.CORNERSTONE -> NumeraGlyph.Grid
    NumberType.CAPSTONE -> NumeraGlyph.Person
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
                    text = stringResource(Res.string.your_map_title),
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
                        item {
                            CyclesRow(
                                personalYear = state.personalYear,
                                personalYearTheme = state.personalYearTheme,
                                personalMonth = state.personalMonth,
                                personalMonthTheme = state.personalMonthTheme
                            )
                        }
                        item {
                            LuckyNumbersRow(numbers = state.luckyNumbers, color = state.luckyColor)
                        }
                        item { Spacer(modifier = Modifier.height(4.dp)) }
                        items(state.cards) { card ->
                            NumberCard(card, onClick = { onAction(NumbersAction.OnCardClick(card.type)) })
                        }
                        item {
                            CompatibilityEntryCard(onClick = { onAction(NumbersAction.OnCompatibilityClick) })
                        }
                        item {
                            ToolEntryCard(
                                glyph = NumeraGlyph.Spiral,
                                title = stringResource(Res.string.tool_karmic_analysis),
                                onClick = { onAction(NumbersAction.OnKarmicAnalysisClick) }
                            )
                        }
                        item {
                            ToolEntryCard(
                                glyph = NumeraGlyph.Calendar,
                                title = stringResource(Res.string.tool_life_cycles),
                                onClick = { onAction(NumbersAction.OnLifeCyclesClick) }
                            )
                        }
                        item {
                            ToolEntryCard(
                                glyph = NumeraGlyph.Grid,
                                title = stringResource(Res.string.tool_numerology_tools),
                                onClick = { onAction(NumbersAction.OnToolsClick) }
                            )
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
    ToolEntryCard(glyph = NumeraGlyph.Duality, title = stringResource(Res.string.compatibility_with_loved_ones), onClick = onClick)
}

@Composable
private fun ToolEntryCard(glyph: NumeraGlyph, title: String, onClick: () -> Unit) {
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
            NumeraIcon(glyph = glyph, tint = Primary, size = 20.dp)
        }
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
        Text(text = "→", fontSize = 16.sp, color = Primary)
    }
}

@Composable
private fun CyclesRow(personalYear: Int, personalYearTheme: String, personalMonth: Int, personalMonthTheme: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        CycleCard(label = stringResource(Res.string.personal_year_label), value = personalYear, theme = personalYearTheme, modifier = Modifier.weight(1f))
        CycleCard(label = stringResource(Res.string.personal_month_label), value = personalMonth, theme = personalMonthTheme, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun CycleCard(label: String, value: Int, theme: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Surface)
            .border(1.dp, Outline, RoundedCornerShape(4.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = value.toString(), fontFamily = displayFontFamily(), fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Primary)
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = theme, fontSize = 11.sp, lineHeight = 15.sp, color = Muted, maxLines = 3)
    }
}

@Composable
private fun LuckyNumbersRow(numbers: List<Int>, color: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Surface)
            .border(1.dp, Outline, RoundedCornerShape(4.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.lucky_numbers_label, numbers.joinToString(", ")),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = stringResource(Res.string.lucky_color_label, stringResource(colorNameRes(color))), fontSize = 12.sp, color = Primary)
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
