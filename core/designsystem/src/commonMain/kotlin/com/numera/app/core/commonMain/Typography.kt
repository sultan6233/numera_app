package com.numera.app.core.commonMain

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import numera.core.designsystem.generated.resources.Res
import numera.core.designsystem.generated.resources.inter_bold
import numera.core.designsystem.generated.resources.inter_medium
import numera.core.designsystem.generated.resources.inter_regular
import numera.core.designsystem.generated.resources.playfair_bold
import numera.core.designsystem.generated.resources.playfair_regular

/**
 * Two roles, deliberately not one family stretched thin: Playfair Display (a display serif with
 * real character - old star-atlas/scientific-ledger typesetting) carries numbers and headlines,
 * where the app's personality lives. Inter (a plain grotesque) stays in the background for body
 * copy and controls, where legibility matters more than voice.
 */
@Composable
fun NumeraTypography(): Typography {
    val display = displayFontFamily()
    val body = bodyFontFamily()
    return Typography(
        displayLarge = TextStyle(fontFamily = display, fontSize = 64.sp, fontWeight = FontWeight.Bold),
        headlineLarge = TextStyle(fontFamily = display, fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 34.sp),
        titleLarge = TextStyle(fontFamily = display, fontSize = 22.sp, fontWeight = FontWeight.Bold),
        titleMedium = TextStyle(fontFamily = display, fontSize = 18.sp, fontWeight = FontWeight.Bold),
        bodyLarge = TextStyle(fontFamily = body, fontSize = 16.sp, fontWeight = FontWeight.Normal),
        bodyMedium = TextStyle(fontFamily = body, fontSize = 14.sp, fontWeight = FontWeight.Normal, lineHeight = 20.sp),
        labelLarge = TextStyle(fontFamily = body, fontSize = 14.sp, fontWeight = FontWeight.Medium),
        labelSmall = TextStyle(fontFamily = body, fontSize = 11.sp, fontWeight = FontWeight.Medium),
    )
}

@Composable
fun displayFontFamily() = FontFamily(
    Font(Res.font.playfair_regular, FontWeight.Normal),
    Font(Res.font.playfair_bold, FontWeight.Bold),
)

@Composable
fun bodyFontFamily() = FontFamily(
    Font(Res.font.inter_regular, FontWeight.Normal),
    Font(Res.font.inter_medium, FontWeight.Medium),
    Font(Res.font.inter_bold, FontWeight.Bold),
)
