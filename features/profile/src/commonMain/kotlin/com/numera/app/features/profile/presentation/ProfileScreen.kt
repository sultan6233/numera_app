package com.numera.app.features.profile.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numera.app.core.commonMain.components.DefaultNumeraButton
import com.numera.app.core.commonMain.components.DefaultNumeraOutlinedButton

@Composable
fun ProfileScreen(state: ProfileState, onAction: (ProfileAction) -> Unit, snackbarHostState: SnackbarHostState) {
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        when (state) {
            is ProfileState.Content -> Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(20.dp)) {
                Text(text = state.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Text(text = state.birthDateText, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = if (state.subscribed) "Подписка активна" else "Бесплатная версия",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (!state.subscribed) {
                    DefaultNumeraButton(text = "Оформить подписку", onClick = { onAction(ProfileAction.OnUpgradeClick) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
                DefaultNumeraOutlinedButton(
                    text = "Восстановить покупки",
                    onClick = { onAction(ProfileAction.OnRestoreClick) }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Уведомления", fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
                    Switch(
                        checked = state.notificationsEnabled,
                        onCheckedChange = { onAction(ProfileAction.OnNotificationsToggled(it)) }
                    )
                }
            }
        }
    }
}
