package com.numera.app.core.commonMain.session

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

@Composable
actual fun rememberPlatformSettings(): Settings {
    return remember { NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults) }
}
