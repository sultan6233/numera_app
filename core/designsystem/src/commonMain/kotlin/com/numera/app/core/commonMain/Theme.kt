package com.numera.app.core.commonMain

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun NumeraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Primary,
            background = Background,
            onBackground = OnBackground,
            onSurfaceVariant = Muted,
            surface = Surface,
            surfaceVariant = SurfaceHighlight,
            outline = Outline,
            tertiary = Success,
            error = Error
        ),
        typography = NumeraTypography(),
        content = content
    )
}
