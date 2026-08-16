package com.numera.app.core.commonMain.session

import androidx.compose.runtime.Composable
import com.russhwolf.settings.Settings

@Composable
expect fun rememberPlatformSettings(): Settings
