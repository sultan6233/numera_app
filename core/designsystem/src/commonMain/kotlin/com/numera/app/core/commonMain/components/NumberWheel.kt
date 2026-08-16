package com.numera.app.core.commonMain.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.numera.app.core.commonMain.Muted
import com.numera.app.core.commonMain.Outline
import com.numera.app.core.commonMain.Primary
import com.numera.app.core.commonMain.PrimarySoft
import com.numera.app.core.commonMain.Surface
import kotlin.math.cos
import kotlin.math.sin

/**
 * The app's signature motif: the nine Pythagorean digits arranged as a ring, the way a
 * numerologist's own reference chart looks - not a decorative circle. [litNumbers] are numbers
 * the user has actually unlocked/seen; everything else renders as a locked node, so the wheel
 * itself communicates "here is your whole chart, this much of it is visible" without a caption.
 */
@Composable
fun NumberWheel(
    litNumbers: Set<Int>,
    modifier: Modifier = Modifier,
    diameter: Dp = 260.dp,
    centerContent: @Composable () -> Unit = {}
) {
    // Scales with the wheel so smaller wheels (paywall hero, numbers summary) don't crowd their nodes.
    val nodeSize = diameter * 0.153f
    Box(modifier = modifier.size(diameter), contentAlignment = Alignment.Center) {
        val radiusPx = diameter / 2 - nodeSize / 2
        for (n in 1..9) {
            val angleDeg = -90.0 + (n - 1) * (360.0 / 9.0)
            val angleRad = angleDeg * (kotlin.math.PI / 180.0)
            val x = radiusPx * cos(angleRad).toFloat()
            val y = radiusPx * sin(angleRad).toFloat()
            val lit = n in litNumbers
            Box(
                modifier = Modifier
                    .offset(x = x, y = y)
                    .size(nodeSize)
                    .background(if (lit) PrimarySoft else Surface, CircleShape)
                    .border(1.dp, if (lit) Primary else Outline, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = n.toString(),
                    fontSize = 15.sp,
                    fontWeight = if (lit) FontWeight.Bold else FontWeight.Normal,
                    color = if (lit) Primary else Muted
                )
            }
        }
        centerContent()
    }
}
