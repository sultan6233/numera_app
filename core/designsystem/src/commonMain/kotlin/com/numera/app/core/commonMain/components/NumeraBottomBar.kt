package com.numera.app.core.commonMain.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.numera.app.core.commonMain.CalendarEnergy
import com.numera.app.core.commonMain.Growth
import com.numera.app.core.commonMain.Home
import com.numera.app.core.commonMain.Muted
import com.numera.app.core.commonMain.Numbers
import com.numera.app.core.commonMain.OnBackground
import com.numera.app.core.commonMain.Outline
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.PrimarySoft
import com.numera.app.core.commonMain.Profile
import com.numera.app.core.commonMain.Surface

private data class BottomBarTab(val route: NavKey, val glyph: NumeraGlyph, val label: String)

private val TABS = listOf(
    BottomBarTab(Home, NumeraGlyph.Sun, "Сегодня"),
    BottomBarTab(Numbers, NumeraGlyph.Grid, "Числа"),
    BottomBarTab(CalendarEnergy, NumeraGlyph.Calendar, "Календарь"),
    BottomBarTab(Growth, NumeraGlyph.Sprout, "Рост"),
    BottomBarTab(Profile, NumeraGlyph.Person, "Профиль")
)

@Composable
fun NumeraBottomBar(
    currentRoute: NavKey?,
    onTabClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Surface)
            .border(1.dp, Outline, RoundedCornerShape(24.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TABS.forEach { tab ->
            BottomBarItem(
                tab = tab,
                selected = currentRoute == tab.route,
                onClick = { onTabClick(tab.route) }
            )
        }
    }
}

@Composable
private fun BottomBarItem(tab: BottomBarTab, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) PrimarySoft else androidx.compose.ui.graphics.Color.Transparent,
        animationSpec = tween(200),
        label = "bottomBarItemBg"
    )
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        NumeraIcon(glyph = tab.glyph, tint = if (selected) Primary else Muted, size = 20.dp)
        Text(
            text = tab.label,
            color = if (selected) OnBackground else Muted,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}
