package com.numera.app.core.commonMain.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Radial-gradient glow used for "peak moment" emphasis (e.g. around the day's main number).
 * Uses a plain gradient rather than a real blur - minSdk 28 predates Android's BlurEffect
 * API (31+), so a single cross-platform gradient implementation is simpler than an
 * expect/actual blur fallback for a purely decorative effect.
 */
@Composable
fun BoxScope.GlowLayer(
    color: Color,
    alpha: Float = 0.25f,
    scale: Float = 1.8f,
    offsetY: Dp = 8.dp,
    shape: Shape = RoundedCornerShape(percent = 50),
    animated: Boolean = false
) {
    val currentAlpha = if (animated) {
        val infiniteTransition = rememberInfiniteTransition(label = "glow")
        val animatedAlpha by infiniteTransition.animateFloat(
            initialValue = alpha * 0.5f,
            targetValue = alpha,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "glowAlpha"
        )
        animatedAlpha
    } else {
        alpha
    }

    Box(
        modifier = Modifier
            .matchParentSize()
            .offset(y = offsetY)
            .graphicsLayer(scaleX = scale, scaleY = scale, clip = false)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = currentAlpha), Color.Transparent)
                ),
                shape = shape
            )
    )
}
