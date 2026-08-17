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
import org.jetbrains.compose.resources.stringResource
import numera.features.profile.generated.resources.Res
import numera.features.profile.generated.resources.subscription_active
import numera.features.profile.generated.resources.free_version
import numera.features.profile.generated.resources.subscribe_button
import numera.features.profile.generated.resources.restore_purchases
import numera.features.profile.generated.resources.notifications_label

@Composable
fun ProfileScreen(state: ProfileState, onAction: (ProfileAction) -> Unit, snackbarHostState: SnackbarHostState) {
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        when (state) {
            is ProfileState.Content -> Column(modifier = Modifier.padding(innerPadding).fillMaxSize().padding(20.dp)) {
                Text(text = state.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Text(text = state.birthDateText, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = if (state.subscribed) stringResource(Res.string.subscription_active) else stringResource(Res.string.free_version),
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (!state.subscribed) {
                    DefaultNumeraButton(text = stringResource(Res.string.subscribe_button), onClick = { onAction(ProfileAction.OnUpgradeClick) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
                DefaultNumeraOutlinedButton(
                    text = stringResource(Res.string.restore_purchases),
                    onClick = { onAction(ProfileAction.OnRestoreClick) }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(Res.string.notifications_label), fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
                    Switch(
                        checked = state.notificationsEnabled,
                        onCheckedChange = { onAction(ProfileAction.OnNotificationsToggled(it)) }
                    )
                }
            }
        }
    }
}
