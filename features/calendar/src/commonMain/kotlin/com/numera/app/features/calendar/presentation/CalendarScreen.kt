package com.numera.app.features.calendar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.Surface
import com.numera.app.features.calendar.presentation.model.CalendarDayUi

@Composable
fun CalendarScreen(state: CalendarState, onAction: (CalendarAction) -> Unit) {
    Scaffold { innerPadding ->
        when (state) {
            is CalendarState.Content -> Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.clickable { onAction(CalendarAction.OnPrevMonth) }.padding(8.dp),
                        text = "‹"
                    )
                    Text(
                        text = "${state.month}.${state.year}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        modifier = Modifier.clickable { onAction(CalendarAction.OnNextMonth) }.padding(8.dp),
                        text = "›"
                    )
                }
                LazyVerticalGrid(columns = GridCells.Fixed(7)) {
                    items(state.days) { day -> DayCell(day) }
                }
            }
        }
    }
}

@Composable
private fun DayCell(day: CalendarDayUi) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(if (day.isToday) Primary else Surface),
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
        }
    }
}
