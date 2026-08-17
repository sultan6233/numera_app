package com.numera.app.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import com.numera.app.core.commonMain.components.NumberWheel
import com.numera.app.core.commonMain.components.shimmer
import com.numera.app.core.commonMain.displayFontFamily
import com.numera.app.features.home.presentation.model.DailyInsightUi
import org.jetbrains.compose.resources.stringResource
import numera.features.home.generated.resources.Res
import numera.features.home.generated.resources.personal_day_label
import numera.features.home.generated.resources.streak_days
import numera.features.home.generated.resources.good_morning_greeting
import numera.features.home.generated.resources.your_day_fallback

@Composable
fun HomeScreen(state: HomeState, onAction: (HomeAction) -> Unit, snackbarHostState: SnackbarHostState) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        when (state) {
            is HomeState.Content -> HomeContent(Modifier.padding(innerPadding), state, onAction)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(modifier: Modifier, state: HomeState.Content, onAction: (HomeAction) -> Unit) {
    PullToRefreshBox(
        isRefreshing = state.loading,
        onRefresh = { onAction(HomeAction.OnRefresh) },
        modifier = modifier.fillMaxSize().padding(horizontal = 20.dp).padding(top = 20.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HomeHeader(state)
            Spacer(modifier = Modifier.height(8.dp))
            if (state.loading && state.insight == null) {
                Box(modifier = Modifier.fillMaxWidth().height(260.dp).shimmer(RoundedCornerShape(24.dp)))
            } else if (state.insight != null) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    NumberWheel(litNumbers = setOf(state.insight.luckyNumber)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = state.insight.luckyNumber.toString(),
                                fontFamily = displayFontFamily(),
                                fontSize = 52.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                            Text(
                                text = stringResource(Res.string.personal_day_label),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                InsightBody(state.insight)
            }
        }
    }
}

@Composable
private fun HomeHeader(state: HomeState.Content) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = state.insight?.greeting ?: stringResource(Res.string.good_morning_greeting),
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = state.userName.ifBlank { stringResource(Res.string.your_day_fallback) },
                fontFamily = displayFontFamily(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        StreakBadge(state.streak)
    }
}

@Composable
private fun StreakBadge(streak: Int) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100))
            .border(1.dp, Outline, RoundedCornerShape(100))
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(6.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(Ember)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = stringResource(Res.string.streak_days, streak), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun InsightBody(insight: DailyInsightUi) {
    Column {
        Text(
            text = insight.headline,
            fontFamily = displayFontFamily(),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(12.dp))
        insight.body.forEach { paragraph ->
            Text(
                text = paragraph,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Surface)
                .border(1.dp, PrimarySoft, RoundedCornerShape(4.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = insight.affirmation,
                fontFamily = displayFontFamily(),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }
    }
}
