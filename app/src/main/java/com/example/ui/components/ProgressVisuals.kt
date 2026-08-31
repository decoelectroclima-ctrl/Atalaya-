package com.example.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.ui.theme.*

@Composable
fun KintsugiHeart(progressStage: Int, vulnerabilityScore: Int = 40, modifier: Modifier = Modifier) {
    val goldColor = RawDarkAmber
    val darkCrackColor = Color(0xFF2C3E50)
    val heartColor = Color(0xFFE57373)

    Canvas(modifier = modifier.size(110.dp)) {
        val w = size.width
        val h = size.height

        // Heart base path
        val heartPath = Path().apply {
            moveTo(w / 2f, h * 0.8f)
            cubicTo(0f, h * 0.3f, w * 0.15f, 0f, w / 2f, h * 0.35f)
            cubicTo(w * 0.85f, 0f, w, h * 0.3f, w / 2f, h * 0.8f)
        }

        val alphaMultiplier = if (vulnerabilityScore >= 70) 0.8f else 1.0f

        // Draw heart background/fill
        drawPath(heartPath, color = heartColor.copy(alpha = (0.25f + (progressStage * 0.08f)) * alphaMultiplier))
        drawPath(heartPath, color = Color.White, style = Stroke(width = 3.5f))

        val crack1Start = Offset(w * 0.5f, h * 0.25f)
        val crack1End = Offset(w * 0.5f, h * 0.75f)

        val crack2Start = Offset(w * 0.3f, h * 0.4f)
        val crack2End = Offset(w * 0.65f, h * 0.55f)

        val crack3Start = Offset(w * 0.4f, h * 0.6f)
        val crack3End = Offset(w * 0.7f, h * 0.35f)

        val crack4Start = Offset(w * 0.25f, h * 0.3f)
        val crack4End = Offset(w * 0.4f, h * 0.5f)

        if (progressStage == 1) {
            drawLine(darkCrackColor, crack1Start, crack1End, strokeWidth = 3f)
            drawLine(darkCrackColor, crack2Start, crack2End, strokeWidth = 2.5f)
        } else {
            drawLine(goldColor, crack1Start, crack1End, strokeWidth = 2f + (progressStage * 0.4f))
            if (progressStage >= 3) {
                drawLine(goldColor, crack2Start, crack2End, strokeWidth = 2f + (progressStage * 0.3f))
            }
            if (progressStage >= 5) {
                drawLine(goldColor, crack3Start, crack3End, strokeWidth = 2.5f + (progressStage * 0.3f))
            }
            if (progressStage >= 7) {
                drawLine(goldColor, crack4Start, crack4End, strokeWidth = 3f + (progressStage * 0.2f))
            }
            if (progressStage >= 6) {
                drawCircle(goldColor, radius = 4f, center = crack1End)
                drawCircle(goldColor, radius = 3.5f, center = crack2End)
            }
            if (progressStage == 8) {
                drawCircle(goldColor, radius = 4.5f, center = crack3End)
                drawCircle(goldColor, radius = 4f, center = crack1Start)
            }
        }
    }
}

@Composable
fun ProgressiveLandscape(progressStage: Int, vulnerabilityScore: Int = 40, modifier: Modifier = Modifier) {
    val animDuration = if (vulnerabilityScore >= 70) 1400 else if (vulnerabilityScore >= 35) 900 else 500
    Crossfade(
        targetState = progressStage,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = animDuration),
        modifier = modifier
    ) { stage ->
        val bgColors = when (stage) {
            1 -> listOf(Color(0xFF2C3E50), Color(0xFF1A252F)) // Stormy fog
            2 -> listOf(Color(0xFF34495E), Color(0xFF2C3E50)) // Dim twilight
            3 -> listOf(Color(0xFF4A6B82), Color(0xFF34495E)) // Dawn breaking
            4 -> listOf(Color(0xFF6B9AC4), Color(0xFF4A6B82)) // Morning light
            5 -> listOf(Color(0xFF85B3D1), Color(0xFF6B9AC4)) // Clear day
            6 -> listOf(Color(0xFFA3CDEF), Color(0xFF85B3D1)) // Bright sunny day
            7 -> listOf(Color(0xFFC2E0FF), Color(0xFFA3CDEF)) // Radiant horizon
            else -> listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD)) // Serene masterpiece sky
        }

        Canvas(modifier = Modifier.size(200.dp, 100.dp)) {
            val w = size.width
            val h = size.height

            // Sky background gradient
            drawRect(
                brush = Brush.verticalGradient(
                    colors = bgColors,
                    startY = 0f,
                    endY = h
                )
            )

            // Sun element starting from stage 3 onwards
            if (stage >= 3) {
                val sunRadius = 12f + (stage * 2f)
                val sunAlpha = 0.4f + (stage * 0.07f)
                drawCircle(
                    color = Color(0xFFFFD54F).copy(alpha = sunAlpha.coerceIn(0f, 1f)),
                    radius = sunRadius,
                    center = Offset(w * 0.75f, h * 0.35f)
                )
            }

            // Distant mountains (stage >= 2)
            if (stage >= 2) {
                val mountainPath1 = Path().apply {
                    moveTo(0f, h * 0.75f)
                    lineTo(w * 0.35f, h * 0.4f)
                    lineTo(w * 0.7f, h * 0.7f)
                    lineTo(w, h * 0.5f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(mountainPath1, color = Color(0xFF475569).copy(alpha = 0.6f))
            }

            // Foreground hills (stage >= 4)
            if (stage >= 4) {
                val hillPath = Path().apply {
                    moveTo(0f, h * 0.85f)
                    cubicTo(w * 0.25f, h * 0.55f, w * 0.6f, h * 0.8f, w, h * 0.65f)
                    lineTo(w, h)
                    lineTo(0f, h)
                    close()
                }
                drawPath(hillPath, color = Color(0xFF334155).copy(alpha = 0.8f))
            }

            // Trees / Life elements (stage >= 6)
            if (stage >= 6) {
                drawCircle(Color(0xFF10B981).copy(alpha = 0.9f), radius = 6f, center = Offset(w * 0.2f, h * 0.78f))
                drawCircle(Color(0xFF059669).copy(alpha = 0.9f), radius = 8f, center = Offset(w * 0.25f, h * 0.76f))
                drawCircle(Color(0xFF10B981).copy(alpha = 0.9f), radius = 5f, center = Offset(w * 0.75f, h * 0.8f))
            }
        }
    }
}

