package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoltarSoundManager
import kotlinx.coroutines.delay

class OctagonShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val w = size.width
        val h = size.height
        val cut = w * 0.22f
        val path = Path().apply {
            moveTo(cut, 0f)
            lineTo(w - cut, 0f)
            lineTo(w, cut)
            lineTo(w, h - cut)
            lineTo(w - cut, h)
            lineTo(cut, h)
            lineTo(0f, h - cut)
            lineTo(0f, cut)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun AdrianaIntroScreen(
    onAnimationFinished: () -> Unit = {},
    onInteract: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var hasInteracted by remember { mutableStateOf(false) }

    val brandAlpha = remember { Animatable(0f) }
    val brandOffsetY = remember { Animatable(20f) }

    val infiniteTransition = rememberInfiniteTransition(label = "adriana_intro_transition")
    
    val heartbeatScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1450
                1.0f at 0
                1.06f at 110
                0.98f at 220
                1.04f at 330
                1.0f at 550
                1.0f at 1450
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "heartbeat_scale"
    )

    val haloScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_scale"
    )

    val haloAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_alpha"
    )

    val context = LocalContext.current
    LaunchedEffect(hasInteracted) {
        if (!hasInteracted) {
            val vibrator = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vm = context.getSystemService(android.content.Context.VIBRATOR_MANAGER_SERVICE) as? android.os.VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? android.os.Vibrator
            }

            while (true) {
                SoltarSoundManager.playSound(SoltarSoundManager.SoundType.HEARTBEAT)
                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        val timings = longArrayOf(0, 120, 80, 150)
                        val amplitudes = intArrayOf(0, 255, 0, 255)
                        vibrator?.vibrate(android.os.VibrationEffect.createWaveform(timings, amplitudes, -1))
                    } else {
                        @Suppress("DEPRECATION")
                        vibrator?.vibrate(longArrayOf(0, 120, 80, 150), -1)
                    }
                } catch (_: Exception) {}
                delay(1450)
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(600)
        brandOffsetY.animateTo(0f, animationSpec = tween(800, easing = FastOutSlowInEasing))
    }

    LaunchedEffect(Unit) {
        delay(600)
        brandAlpha.animateTo(1f, animationSpec = tween(800, easing = LinearEasing))
    }

    val bgCenter = Color(0xFFFFFDF9)
    val bgMid = Color(0xFFF4F0EB)
    val bgEdge = Color(0xFFEBE4DC)
    val textColor = Color(0xFF302B2C)
    val goldColor = Color(0xFFC7A24D)
    val goldLight = Color(0xFFEFD58A)
    val redDark = Color(0xFF4A0B12)
    val redMid = Color(0xFF8F1825)
    val redLight = Color(0xFFBD3542)
    val haloColor = Color(0x1A8F1825)

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        hasInteracted = true
                        onInteract()
                    }
                )
            }
            .background(
                Brush.radialGradient(
                    colors = listOf(bgCenter, bgMid, bgEdge),
                    radius = 1200f
                )
            )
            .testTag("adriana_intro_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Octagonal Container with Halo & Layers matching original design
            Box(
                modifier = Modifier
                    .size(290.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background Halo
                Canvas(
                    modifier = Modifier
                        .size(340.dp)
                        .graphicsLayer {
                            scaleX = haloScale
                            scaleY = haloScale
                            alpha = haloAlpha
                        }
                ) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(haloColor, Color.Transparent),
                            radius = size.width / 2
                        )
                    )
                }

                // Outer Octagon Layer
                Surface(
                    modifier = Modifier
                        .size(290.dp),
                    shape = OctagonShape(),
                    color = Color(0xFFEFE8DE),
                    shadowElevation = 10.dp
                ) {}

                // Inner Octagon Layer
                Surface(
                    modifier = Modifier
                        .size(236.dp),
                    shape = OctagonShape(),
                    color = Color(0xFFFAF7F2),
                    shadowElevation = 4.dp,
                    border = BorderStroke(1.dp, Color(0xFFE5DDD0))
                ) {}

                // Beating Heart Canvas with Kintsugi details (cracks, stitches, knots)
                Canvas(
                    modifier = Modifier
                        .size(210.dp)
                        .graphicsLayer {
                            scaleX = heartbeatScale
                            scaleY = heartbeatScale
                            shadowElevation = 20f
                            clip = false
                        }
                ) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2
                    val cy = h / 2

                    drawKintsugiHeart(
                        cx = cx,
                        cy = cy,
                        redLight = redLight,
                        redMid = redMid,
                        redDark = redDark,
                        bgColor = Color(0xFFFAF7F2),
                        gold = goldColor,
                        goldLight = goldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Brand Typography Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = brandAlpha.value
                        translationY = brandOffsetY.value
                    }
            ) {
                Text(
                    text = "A D R I A N A",
                    color = textColor,
                    fontSize = 34.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 8.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, goldColor, Color.Transparent)
                            )
                        )
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "volver a ti",
                    color = Color(0xFF756B6D),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 3.5.sp
                )
            }
        }
    }
}

/**
 * Draws the layered 3D Heart, ceramic fracture paths, gold threads and glowing kintsugi knots.
 */
private fun DrawScope.drawKintsugiHeart(
    cx: Float,
    cy: Float,
    redLight: Color,
    redMid: Color,
    redDark: Color,
    bgColor: Color,
    gold: Color,
    goldLight: Color
) {
    val heartPath = Path().apply {
        val topY = cy - 65f
        val bottomY = cy + 78f
        val widthOffset = 76f
        val topCurve = 40f

        moveTo(cx, cy - 22f)
        cubicTo(cx - 30f, topY - topCurve, cx - widthOffset, topY + 10f, cx - widthOffset, cy)
        cubicTo(cx - widthOffset, cy + 44f, cx - 26f, cy + 62f, cx, bottomY)
        cubicTo(cx + 26f, cy + 62f, cx + widthOffset, cy + 44f, cx + widthOffset, cy)
        cubicTo(cx + widthOffset, topY + 10f, cx + 30f, topY - topCurve, cx, cy - 22f)
        close()
    }

    // Shadow underneath heart
    drawPath(
        path = heartPath,
        color = Color(0x38400005)
    )

    // Main Heart Body with Rich Radial Lighting Gradient
    drawPath(
        path = heartPath,
        brush = Brush.radialGradient(
            colors = listOf(redLight, redMid, redDark),
            center = Offset(cx - 20f, cy - 25f),
            radius = 95f
        )
    )

    // Soft Upper-Left Light Reflection Highlight
    drawOval(
        brush = Brush.linearGradient(
            colors = listOf(Color(0x35FFFFFF), Color.Transparent),
            start = Offset(cx - 45f, cy - 38f),
            end = Offset(cx - 15f, cy - 8f)
        ),
        topLeft = Offset(cx - 42f, cy - 40f),
        size = Size(30f, 42f)
    )

    // KINTSUGI CERAMIC CRACKS
    val mainCrackPath = Path().apply {
        moveTo(cx - 2f, cy - 50f)
        lineTo(cx + 5f, cy - 32f)
        lineTo(cx - 6f, cy - 15f)
        lineTo(cx + 7f, cy + 3f)
        lineTo(cx - 5f, cy + 22f)
        lineTo(cx + 6f, cy + 40f)
        lineTo(cx, cy + 62f)
    }

    val leftCrackPath = Path().apply {
        moveTo(cx - 6f, cy - 15f)
        lineTo(cx - 27f, cy - 22f)
        lineTo(cx - 40f, cy - 12f)
        lineTo(cx - 52f, cy - 17f)
    }

    val rightCrackPath = Path().apply {
        moveTo(cx + 7f, cy + 3f)
        lineTo(cx + 29f, cy - 3f)
        lineTo(cx + 38f, cy + 10f)
        lineTo(cx + 54f, cy + 2f)
    }

    val bottomCrackPath = Path().apply {
        moveTo(cx - 5f, cy + 22f)
        lineTo(cx - 20f, cy + 34f)
        lineTo(cx - 35f, cy + 30f)
        lineTo(cx - 45f, cy + 42f)
    }

    // Draw crack depth shadows
    val crackStrokeShadow = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawPath(mainCrackPath, Color(0x66400005), style = crackStrokeShadow)
    drawPath(leftCrackPath, Color(0x66400005), style = crackStrokeShadow)
    drawPath(rightCrackPath, Color(0x66400005), style = crackStrokeShadow)
    drawPath(bottomCrackPath, Color(0x66400005), style = crackStrokeShadow)

    // Draw crack body (background porcelain tone)
    val crackStrokeMain = Stroke(width = 1.8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawPath(mainCrackPath, bgColor, style = crackStrokeMain)
    drawPath(leftCrackPath, bgColor, style = crackStrokeMain)
    drawPath(rightCrackPath, bgColor, style = crackStrokeMain)
    drawPath(bottomCrackPath, bgColor, style = crackStrokeMain)

    // GOLDEN THREADS & SEAMS
    val goldThreadBrush = Brush.linearGradient(
        colors = listOf(gold, goldLight, gold)
    )
    val threadStroke = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)

    fun drawStitch(start: Offset, end: Offset) {
        drawLine(
            color = Color(0x80C7A24D),
            start = start,
            end = end,
            strokeWidth = 4.5f,
            cap = StrokeCap.Round
        )
        drawLine(
            brush = goldThreadBrush,
            start = start,
            end = end,
            strokeWidth = 2.2f,
            cap = StrokeCap.Round
        )
    }

    drawStitch(Offset(cx - 15f, cy - 35f), Offset(cx + 18f, cy - 29f))
    drawStitch(Offset(cx - 20f, cy - 12f), Offset(cx + 15f, cy - 18f))
    drawStitch(Offset(cx - 17f, cy + 7f), Offset(cx + 22f, cy + 0f))
    drawStitch(Offset(cx - 18f, cy + 28f), Offset(cx + 15f, cy + 18f))
    drawStitch(Offset(cx - 34f, cy - 27f), Offset(cx - 25f, cy - 7f))

    drawPath(mainCrackPath, brush = goldThreadBrush, style = threadStroke)
    drawPath(leftCrackPath, brush = goldThreadBrush, style = Stroke(width = 1.6f, cap = StrokeCap.Round))
    drawPath(rightCrackPath, brush = goldThreadBrush, style = Stroke(width = 1.6f, cap = StrokeCap.Round))
    drawPath(bottomCrackPath, brush = goldThreadBrush, style = Stroke(width = 1.6f, cap = StrokeCap.Round))

    // GOLDEN KNOTS
    val knotPoints = listOf(
        Offset(cx - 15f, cy - 35f),
        Offset(cx + 18f, cy - 29f),
        Offset(cx - 20f, cy - 12f),
        Offset(cx + 15f, cy - 18f),
        Offset(cx - 17f, cy + 7f),
        Offset(cx + 22f, cy + 0f),
        Offset(cx - 18f, cy + 28f),
        Offset(cx - 34f, cy - 27f),
        Offset(cx + 5f, cy - 32f),
        Offset(cx + 7f, cy + 3f)
    )

    for (pt in knotPoints) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xB3EFD58A), Color.Transparent),
                center = pt,
                radius = 6f
            ),
            radius = 5.5f,
            center = pt
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, goldLight, gold),
                center = Offset(pt.x - 0.8f, pt.y - 0.8f),
                radius = 2.2f
            ),
            radius = 2.2f,
            center = pt
        )
    }
}
