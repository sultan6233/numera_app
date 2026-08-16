package com.numera.app.features.compatibility.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numera.app.core.commonMain.Primary

@Composable
fun CompatibilityDetailScreen(state: CompatibilityDetailState) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                CompatibilityDetailState.Loading -> CircularProgressIndicator(color = Primary)
                is CompatibilityDetailState.Content -> {
                    Text(text = "Вы и ${state.companionName}", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "${state.score}%", fontSize = 56.sp, fontWeight = FontWeight.Bold, color = Primary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = state.label, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}
