package com.numera.app.features.calendar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numera.app.core.commonMain.Background
import com.numera.app.core.commonMain.Ember
import com.numera.app.core.commonMain.Muted
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.Success
import com.numera.app.core.commonMain.Surface
import com.numera.app.core.commonMain.components.DefaultNumeraButton
import com.numera.app.core.commonMain.displayFontFamily
import com.numera.app.features.calendar.domain.DayEnergyMeanings
import com.numera.app.features.calendar.presentation.model.CalendarDayUi
import org.jetbrains.compose.resources.stringResource
import numera.features.calendar.generated.resources.Res
import numera.features.calendar.generated.resources.weekday_mon
import numera.features.calendar.generated.resources.weekday_tue
import numera.features.calendar.generated.resources.weekday_wed
import numera.features.calendar.generated.resources.weekday_thu
import numera.features.calendar.generated.resources.weekday_fri
import numera.features.calendar.generated.resources.weekday_sat
import numera.features.calendar.generated.resources.weekday_sun
import numera.features.calendar.generated.resources.legend_action
import numera.features.calendar.generated.resources.legend_pause
import numera.features.calendar.generated.resources.legend_stability
import numera.features.calendar.generated.resources.legend_special
import numera.features.calendar.generated.resources.close_button
import numera.features.calendar.generated.resources.day_detail_title
import numera.features.calendar.generated.resources.calendar_locked_title
import numera.features.calendar.generated.resources.calendar_locked_description
import numera.features.calendar.generated.resources.subscribe_button

private val WEEKDAY_LABEL_RES = listOf(
    Res.string.weekday_mon, Res.string.weekday_tue, Res.string.weekday_wed, Res.string.weekday_thu,
    Res.string.weekday_fri, Res.string.weekday_sat, Res.string.weekday_sun
)

@Composable
fun CalendarScreen(state: CalendarState, onAction: (CalendarAction) -> Unit) {
    Scaffold { innerPadding ->
        when (state) {
            is CalendarState.Content -> Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                CalendarContent(
                    state = state,
                    onAction = onAction,
                    modifier = if (state.subscribed) Modifier else Modifier.blur(18.dp)
                )
                if (!state.subscribed) {
                    LockOverlay(onUnlockClick = { onAction(CalendarAction.OnUnlockClick) })
                }
                state.selectedDay?.let { day ->
                    DayDetailDialog(day = day, onDismiss = { onAction(CalendarAction.OnDismissDayDetail) })
                }
            }
        }
    }
}

@Composable
private fun CalendarContent(state: CalendarState.Content, onAction: (CalendarAction) -> Unit, modifier: Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.clickable { onAction(CalendarAction.OnPrevMonth) }.padding(8.dp),
                text = "‹",
                fontSize = 20.sp,
                color = Primary
            )
            Text(
                text = state.monthLabel,
                fontFamily = displayFontFamily(),
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                modifier = Modifier.clickable { onAction(CalendarAction.OnNextMonth) }.padding(8.dp),
                text = "›",
                fontSize = 20.sp,
                color = Primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            WEEKDAY_LABEL_RES.forEach { labelRes ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(labelRes), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.weight(1f)) {
            items(state.leadingBlanks) { Box(modifier = Modifier.aspectRatio(1f)) }
            items(state.days) { day -> DayCell(day, onClick = { onAction(CalendarAction.OnDayClick(day)) }) }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Legend()
    }
}

@Composable
private fun DayCell(day: CalendarDayUi, onClick: () -> Unit) {
    val accent = energyAccent(day.personalDay)
    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (day.isToday) Primary else Surface)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.day.toString(),
                fontSize = 12.sp,
                color = if (day.isToday) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = day.personalDay.toString(),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (day.isToday) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
            )
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(5.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (day.isToday) MaterialTheme.colorScheme.background.copy(alpha = 0.6f) else accent)
            )
        }
    }
}

private fun energyAccent(personalDay: Int): Color = when (personalDay) {
    1, 3, 5, 8 -> Success
    4, 7 -> Ember
    11, 22, 33 -> Primary
    else -> Muted
}

@Composable
private fun Legend() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        LegendItem(Success, stringResource(Res.string.legend_action))
        LegendItem(Ember, stringResource(Res.string.legend_pause))
        LegendItem(Muted, stringResource(Res.string.legend_stability))
        LegendItem(Primary, stringResource(Res.string.legend_special))
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(6.dp).clip(RoundedCornerShape(50)).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun DayDetailDialog(day: CalendarDayUi, onDismiss: () -> Unit) {
    val meaning = DayEnergyMeanings.meaningFor(day.personalDay)
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Text(modifier = Modifier.clickable(onClick = onDismiss).padding(8.dp), text = stringResource(Res.string.close_button))
        },
        title = { Text(stringResource(Res.string.day_detail_title, day.day, day.personalDay)) },
        text = {
            Column {
                Text(text = stringResource(meaning.title), fontFamily = displayFontFamily(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(meaning.text), fontSize = 13.sp, lineHeight = 19.sp)
            }
        }
    )
}

@Composable
private fun BoxScope.LockOverlay(onUnlockClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background.copy(alpha = 0.6f))
            .clickable(onClick = onUnlockClick)
    )
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(28.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Surface)
            .border(1.dp, Primary, RoundedCornerShape(4.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.calendar_locked_title),
            fontFamily = displayFontFamily(),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.calendar_locked_description),
            fontSize = 13.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        DefaultNumeraButton(text = stringResource(Res.string.subscribe_button), onClick = onUnlockClick)
    }
}
