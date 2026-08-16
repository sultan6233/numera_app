package com.numera.app.features.paywall.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.numera.app.core.commonMain.components.NumberWheel
import com.numera.app.core.commonMain.components.NumeraIcon
import com.numera.app.core.commonMain.displayFontFamily
import com.numera.app.features.paywall.presentation.model.PlanUi
import com.numera.app.features.paywall.presentation.model.ValueItemUi

@Composable
fun PaywallScreen(state: PaywallState, onAction: (PaywallAction) -> Unit, snackbarHostState: SnackbarHostState) {
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        when (state) {
            is PaywallState.Content -> PaywallContent(Modifier.padding(innerPadding), state, onAction)
        }
    }
}

@Composable
private fun PaywallContent(modifier: Modifier, state: PaywallState.Content, onAction: (PaywallAction) -> Unit) {
    Column(modifier = modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(8.dp))
        NumberHero(state.lifePathNumber, state.expressionNumber)
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Это ваша карта. Мы раскрыли два числа из девяти.",
            fontFamily = displayFontFamily(),
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        ValueList(state.valueItems)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(state.plans) { plan ->
                PlanRow(
                    plan = plan,
                    selected = plan.productId == state.selectedProductId,
                    onClick = { onAction(PaywallAction.OnPlanSelected(plan.productId)) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        DefaultNumeraButton(
            text = ctaText(state.lifePathNumber),
            isLoading = state.isPurchasing,
            onClick = { onAction(PaywallAction.OnPurchaseClick) }
        )
        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.clickable { onAction(PaywallAction.OnRestoreClick) },
                text = "Восстановить покупки",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                modifier = Modifier.clickable { onAction(PaywallAction.OnSkipClick) },
                text = "Продолжить бесплатно",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun ctaText(lifePath: Int?): String =
    if (lifePath != null) "Открыть остальные 8 чисел" else "Продолжить"

@Composable
private fun NumberHero(lifePath: Int?, expression: Int?) {
    val lit = setOfNotNull(lifePath, expression)
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        NumberWheel(litNumbers = lit, diameter = 168.dp) {
            Text(
                text = lifePath?.toString() ?: "?",
                fontFamily = displayFontFamily(),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }
    }
}

@Composable
private fun ValueList(items: List<ValueItemUi>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(PrimarySoft),
                    contentAlignment = Alignment.Center
                ) {
                    NumeraIcon(glyph = item.glyph, tint = Primary, size = 15.dp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = item.label,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun PlanRow(plan: PlanUi, selected: Boolean, onClick: () -> Unit) {
    val borderColor = if (selected) Primary else Outline
    val backgroundColor = if (selected) PrimarySoft else Surface
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(width = if (selected) 2.dp else 1.dp, color = borderColor, shape = RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = plan.title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
            plan.badge?.let {
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = it, fontSize = 11.sp, color = Primary, fontWeight = FontWeight.Bold)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = plan.price,
                fontFamily = displayFontFamily(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = plan.perDayNote ?: plan.period,
                fontSize = 12.sp,
                color = if (plan.perDayNote != null) Primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
