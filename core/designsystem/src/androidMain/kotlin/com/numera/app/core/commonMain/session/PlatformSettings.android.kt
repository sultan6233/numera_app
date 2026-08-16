package com.numera.app.core.commonMain.session

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

@Composable
actual fun rememberPlatformSettings(): Settings {
    val context = LocalContext.current
    return remember(context) {
        SharedPreferencesSettings(context.getSharedPreferences("numera_settings", Context.MODE_PRIVATE))
    }
}
