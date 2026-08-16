package com.numera.app.core.commonMain

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

@Composable
fun NumeraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        // darkColorScheme, not lightColorScheme: this app has no light variant, and stock
        // Material3 components (OutlinedTextField, Checkbox) read roles like onSurface/onPrimary
        // that this palette never had reason to define until they were needed here. Basing on
        // lightColorScheme left those roles at Material's near-black light-theme defaults, which
        // is invisible typed text on this app's dark Surface - basing on darkColorScheme instead
        // makes every unset role default to something already legible on a dark background.
        colorScheme = darkColorScheme(
            primary = Primary,
            onPrimary = Background,
            background = Background,
            onBackground = OnBackground,
            onSurfaceVariant = Muted,
            surface = Surface,
            onSurface = OnBackground,
            surfaceVariant = SurfaceHighlight,
            outline = Outline,
            tertiary = Success,
            error = Error
        ),
        typography = NumeraTypography(),
        content = content
    )
}
