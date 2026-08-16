package com.numera.app.core.commonMain.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Hand-drawn line glyphs instead of emoji - a coherent set of instrument-like marks (compass,
 * star, spiral...) that belongs to the same "astronomer's ledger" visual language as the rest
 * of the app, rather than borrowing whatever a font's emoji set happens to render.
 */
enum class NumeraGlyph {
    Compass, Star, Spiral, Duality, Leaf, Case, Sun, Grid, Calendar, Sprout, Person, Lock
}

@Composable
fun NumeraIcon(
    glyph: NumeraGlyph,
    modifier: Modifier = Modifier,
    tint: Color,
    size: Dp = 20.dp,
    strokeWidth: Dp = 1.6.dp
) {
    Canvas(modifier = modifier.size(size)) {
        val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val r = minOf(w, h) / 2f * 0.82f

        when (glyph) {
            NumeraGlyph.Compass -> {
                drawCircle(tint, radius = r, center = Offset(cx, cy), style = stroke)
                drawCircle(tint, radius = strokeWidth.toPx() * 0.6f, center = Offset(cx, cy))
                val needleLen = r * 0.75f
                drawLine(tint, Offset(cx, cy - needleLen), Offset(cx - needleLen * 0.3f, cy + needleLen * 0.5f), stroke.width, StrokeCap.Round)
                drawLine(tint, Offset(cx, cy - needleLen), Offset(cx + needleLen * 0.3f, cy + needleLen * 0.5f), stroke.width, StrokeCap.Round)
            }

            NumeraGlyph.Star -> {
                val points = 8
                for (i in 0 until points) {
                    val angle = (i * (360.0 / points)) * (Math.PI / 180.0)
                    val outer = Offset(cx + r * cos(angle).toFloat(), cy + r * sin(angle).toFloat())
                    val inner = Offset(cx + r * 0.15f * cos(angle).toFloat(), cy + r * 0.15f * sin(angle).toFloat())
                    drawLine(tint, inner, outer, stroke.width, StrokeCap.Round)
                }
            }

            NumeraGlyph.Spiral -> {
                val path = androidx.compose.ui.graphics.Path()
                val turns = 2.2f
                val steps = 60
                for (i in 0..steps) {
                    val t = i / steps.toFloat()
                    val angle = t * turns * 2f * Math.PI.toFloat()
                    val radius = r * t
                    val x = cx + radius * cos(angle)
                    val y = cy + radius * sin(angle)
                    if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(path, tint, style = stroke)
            }

            NumeraGlyph.Duality -> {
                drawCircle(tint, radius = r * 0.55f, center = Offset(cx - r * 0.35f, cy), style = stroke)
                drawCircle(tint, radius = r * 0.55f, center = Offset(cx + r * 0.35f, cy), style = stroke)
            }

            NumeraGlyph.Leaf -> {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(cx, cy - r)
                    quadraticTo(cx + r, cy - r * 0.3f, cx, cy + r)
                    quadraticTo(cx - r, cy - r * 0.3f, cx, cy - r)
                    close()
                }
                drawPath(path, tint, style = stroke)
                drawLine(tint, Offset(cx, cy - r * 0.6f), Offset(cx, cy + r * 0.85f), stroke.width * 0.7f, StrokeCap.Round)
            }

            NumeraGlyph.Case -> {
                val left = cx - r
                val top = cy - r * 0.5f
                val right = cx + r
                val bottom = cy + r * 0.75f
                drawRoundRect(tint, topLeft = Offset(left, top), size = androidx.compose.ui.geometry.Size(right - left, bottom - top), cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.15f), style = stroke)
                val handleTop = top - r * 0.45f
                drawArc(
                    tint,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - r * 0.45f, handleTop),
                    size = androidx.compose.ui.geometry.Size(r * 0.9f, r * 0.9f),
                    style = stroke
                )
                drawLine(tint, Offset(left, cy + r * 0.05f), Offset(right, cy + r * 0.05f), stroke.width * 0.6f)
            }

            NumeraGlyph.Sun -> {
                drawCircle(tint, radius = r * 0.5f, center = Offset(cx, cy), style = stroke)
                for (i in 0 until 8) {
                    val angle = (i * 45.0) * (Math.PI / 180.0)
                    val inner = Offset(cx + r * 0.72f * cos(angle).toFloat(), cy + r * 0.72f * sin(angle).toFloat())
                    val outer = Offset(cx + r * cos(angle).toFloat(), cy + r * sin(angle).toFloat())
                    drawLine(tint, inner, outer, stroke.width, StrokeCap.Round)
                }
            }

            NumeraGlyph.Grid -> {
                val spacing = r * 0.85f
                for (row in -1..1) {
                    for (col in -1..1) {
                        drawCircle(tint, radius = stroke.width * 0.9f, center = Offset(cx + col * spacing, cy + row * spacing))
                    }
                }
            }

            NumeraGlyph.Calendar -> {
                val left = cx - r * 0.85f
                val top = cy - r * 0.65f
                val right = cx + r * 0.85f
                val bottom = cy + r * 0.85f
                drawRoundRect(tint, topLeft = Offset(left, top), size = androidx.compose.ui.geometry.Size(right - left, bottom - top), cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.12f), style = stroke)
                val headerY = top + r * 0.4f
                drawLine(tint, Offset(left, headerY), Offset(right, headerY), stroke.width * 0.7f)
                drawLine(tint, Offset(cx, cy + r * 0.1f), Offset(cx, cy + r * 0.1f), stroke.width * 1.4f, StrokeCap.Round)
            }

            NumeraGlyph.Sprout -> {
                drawLine(tint, Offset(cx, cy + r), Offset(cx, cy - r * 0.2f), stroke.width, StrokeCap.Round)
                val leafPath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(cx, cy - r * 0.2f)
                    quadraticTo(cx - r * 0.9f, cy - r * 0.6f, cx - r * 0.1f, cy - r)
                    quadraticTo(cx + r * 0.2f, cy - r * 0.6f, cx, cy - r * 0.2f)
                }
                drawPath(leafPath, tint, style = stroke)
                val leafPath2 = androidx.compose.ui.graphics.Path().apply {
                    moveTo(cx, cy - r * 0.2f)
                    quadraticTo(cx + r * 0.9f, cy - r * 0.3f, cx + r * 0.5f, cy - r * 0.85f)
                    quadraticTo(cx + r * 0.15f, cy - r * 0.5f, cx, cy - r * 0.2f)
                }
                drawPath(leafPath2, tint, style = stroke)
            }

            NumeraGlyph.Lock -> {
                val bodyTop = cy - r * 0.05f
                drawRoundRect(
                    tint,
                    topLeft = Offset(cx - r * 0.7f, bodyTop),
                    size = androidx.compose.ui.geometry.Size(r * 1.4f, r * 1.0f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(r * 0.15f),
                    style = stroke
                )
                drawArc(
                    tint,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - r * 0.45f, bodyTop - r * 0.75f),
                    size = androidx.compose.ui.geometry.Size(r * 0.9f, r * 0.9f),
                    style = stroke
                )
                drawCircle(tint, radius = stroke.width * 0.9f, center = Offset(cx, bodyTop + r * 0.5f))
            }

            NumeraGlyph.Person -> {
                drawCircle(tint, radius = r * 0.4f, center = Offset(cx, cy - r * 0.45f), style = stroke)
                val shoulders = androidx.compose.ui.graphics.Path().apply {
                    moveTo(cx - r * 0.7f, cy + r * 0.85f)
                    quadraticTo(cx, cy + r * 0.1f, cx + r * 0.7f, cy + r * 0.85f)
                }
                drawPath(shoulders, tint, style = stroke)
            }
        }
    }
}
