package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoltarSoundManager
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * Animated Kintsugi Heart Intro Screen for ADRIANA based on the designed HTML/CSS spec.
 *
 * Visual Components:
 * - Warm background with soft radial lighting (#FFFDF9 -> #F4F0EB -> #EBE4DC)
 * - Pulsing soft red halo behind the heart (2.2s pulse)
 * - Rhythmic heartbeat animation (1.45s loop with dual pulsation)
 * - 3D Heart with radial gradient (Red Light #BD3542 -> Red #8F1825 -> Dark Red #4A0B12)
 * - Curvature and depth highlights with blur
 * - Ceramic Kintsugi Cracks matching background color and depth shadows
 * - Golden Thread (Kintsugi seams #C7A24D, #EFD58A) across fracture lines
 * - Golden Connection Knots with glowing radial centers
 * - Elegant typography: "ADRIANA" with wide tracking (Serif) + Golden separator + "volver a ti"
 */
@Composable
fun AdrianaIntroScreen(
    onAnimationFinished: () -> Unit = {},
    onInteract: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var hasInteracted by remember { mutableStateOf(false) }

    // Entrance animation for brand text
    val brandAlpha = remember { Animatable(0f) }
    val brandOffsetY = remember { Animatable(20f) }

    // Heartbeat pulsation animation (1.45s cycle matching CSS heartbeat keyframes)
    val infiniteTransition = rememberInfiniteTransition(label = "adriana_intro_transition")
    
    val heartbeatScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1450
                1.0f at 0
                1.055f at 116 // 8%
                0.985f at 232 // 16%
                1.035f at 348 // 24%
                1.0f at 551   // 38%
                1.0f at 1450
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "heartbeat_scale"
    )

    // Halo pulse animation (2.2s cycle matching CSS haloPulse)
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

    LaunchedEffect(hasInteracted) {
        if (!hasInteracted) {
            // Play heartbeat thuds in sync with the visual loading animation cycles
            while (true) {
                SoltarSoundManager.playSound(SoltarSoundManager.SoundType.HEARTBEAT)
                delay(1450)
            }
        }
    }

    LaunchedEffect(Unit) {
        // Delay 600ms before showing brand name
        delay(600)
        brandOffsetY.animateTo(0f, animationSpec = tween(800, easing = FastOutSlowInEasing))
    }

    LaunchedEffect(Unit) {
        delay(600)
        brandAlpha.animateTo(1f, animationSpec = tween(800, easing = LinearEasing))
    }

    // Colors according to CSS specification
    val bgCenter = Color(0xFFFFFDF9)
    val bgMid = Color(0xFFF4F0EB)
    val bgEdge = Color(0xFFEBE4DC)
    val textColor = Color(0xFF302B2C)
    val taglineColor = Color(0xFF756B6D)
    val goldColor = Color(0xFFC7A24D)
    val goldLight = Color(0xFFEFD58A)
    val redDark = Color(0xFF4A0B12)
    val redMid = Color(0xFF8F1825)
    val redLight = Color(0xFFBD3542)
    val haloColor = Color(0x1A8F1825) // rgba(143,24,37, 0.10)

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
            modifier = Modifier.fillMaxWidth()
        ) {
            // Heart & Halo Container
            Box(
                modifier = Modifier
                    .size(280.dp),
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

                // Heart Canvas with Kintsugi details
                Canvas(
                    modifier = Modifier
                        .size(240.dp)
                        .graphicsLayer {
                            scaleX = heartbeatScale
                            scaleY = heartbeatScale
                            // Subtle 3D drop shadow
                            shadowElevation = 24f
                            shape = CircleShape
                            clip = false
                        }
                ) {
                    val w = size.width
                    val h = size.height
                    val cx = w / 2
                    val cy = h / 2

                    // 1. Draw Heart Base
                    drawKintsugiHeart(
                        cx = cx,
                        cy = cy,
                        redLight = redLight,
                        redMid = redMid,
                        redDark = redDark,
                        bgColor = bgMid,
                        gold = goldColor,
                        goldLight = goldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Brand Typography Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .graphicsLayer {
                        alpha = brandAlpha.value
                        translationY = brandOffsetY.value
                    }
            ) {
                // Name: "ADRIANA" with letter spacing
                Text(
                    text = "A D R I A N A",
                    color = textColor,
                    fontSize = 34.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 8.sp
                )

                // Gold Line Separator
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

                // Tagline: "volver a ti"
                Text(
                    text = "volver a ti",
                    color = taglineColor,
                    fontSize = 15.sp,
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
        // High quality vector heart formula
        val topY = cy - 65f
        val bottomY = cy + 78f
        val widthOffset = 76f
        val topCurve = 42f

        moveTo(cx, cy - 25f)
        cubicTo(cx - 30f, topY - topCurve, cx - widthOffset, topY + 10f, cx - widthOffset, cy)
        cubicTo(cx - widthOffset, cy + 45f, cx - 25f, cy + 65f, cx, bottomY)
        cubicTo(cx + 25f, cy + 65f, cx + widthOffset, cy + 45f, cx + widthOffset, cy)
        cubicTo(cx + widthOffset, topY + 10f, cx + 30f, topY - topCurve, cx, cy - 25f)
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
            center = Offset(cx - 25f, cy - 30f),
            radius = 110f
        )
    )

    // Soft Upper-Left Light Reflection Highlight
    drawOval(
        brush = Brush.linearGradient(
            colors = listOf(Color(0x35FFFFFF), Color.Transparent),
            start = Offset(cx - 55f, cy - 45f),
            end = Offset(cx - 20f, cy - 10f)
        ),
        topLeft = Offset(cx - 52f, cy - 48f),
        size = Size(36f, 52f)
    )

    // ----------------------------------------------------
    // KINTSUGI CERAMIC CRACKS (Fractures matching background)
    // ----------------------------------------------------
    val mainCrackPath = Path().apply {
        moveTo(cx - 2f, cy - 60f)
        lineTo(cx + 6f, cy - 38f)
        lineTo(cx - 7f, cy - 18f)
        lineTo(cx + 8f, cy + 4f)
        lineTo(cx - 6f, cy + 26f)
        lineTo(cx + 7f, cy + 48f)
        lineTo(cx, cy + 74f)
    }

    val leftCrackPath = Path().apply {
        moveTo(cx - 7f, cy - 18f)
        lineTo(cx - 32f, cy - 26f)
        lineTo(cx - 48f, cy - 14f)
        lineTo(cx - 62f, cy - 20f)
    }

    val rightCrackPath = Path().apply {
        moveTo(cx + 8f, cy + 4f)
        lineTo(cx + 34f, cy - 4f)
        lineTo(cx + 46f, cy + 12f)
        lineTo(cx + 64f, cy + 2f)
    }

    val bottomCrackPath = Path().apply {
        moveTo(cx - 6f, cy + 26f)
        lineTo(cx - 24f, cy + 40f)
        lineTo(cx - 42f, cy + 36f)
        lineTo(cx - 54f, cy + 50f)
    }

    // Draw crack depth shadows
    val crackStrokeShadow = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawPath(mainCrackPath, Color(0x66400005), style = crackStrokeShadow)
    drawPath(leftCrackPath, Color(0x66400005), style = crackStrokeShadow)
    drawPath(rightCrackPath, Color(0x66400005), style = crackStrokeShadow)
    drawPath(bottomCrackPath, Color(0x66400005), style = crackStrokeShadow)

    // Draw crack body (background porcelain tone)
    val crackStrokeMain = Stroke(width = 2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    drawPath(mainCrackPath, bgColor, style = crackStrokeMain)
    drawPath(leftCrackPath, bgColor, style = crackStrokeMain)
    drawPath(rightCrackPath, bgColor, style = crackStrokeMain)
    drawPath(bottomCrackPath, bgColor, style = crackStrokeMain)

    // ----------------------------------------------------
    // GOLDEN THREADS & SEAMS (Kintsugi Gold Resins)
    // ----------------------------------------------------
    val goldThreadBrush = Brush.linearGradient(
        colors = listOf(gold, goldLight, gold)
    )
    val threadStroke = Stroke(width = 3.0f, cap = StrokeCap.Round, join = StrokeJoin.Round)

    // Cross-stitches across the fracture
    fun drawStitch(start: Offset, end: Offset) {
        // Gold shadow glow
        drawLine(
            color = Color(0x80C7A24D),
            start = start,
            end = end,
            strokeWidth = 5.5f,
            cap = StrokeCap.Round
        )
        // Gold thread
        drawLine(
            brush = goldThreadBrush,
            start = start,
            end = end,
            strokeWidth = 2.8f,
            cap = StrokeCap.Round
        )
    }

    // Gold Stitch 1 (Top)
    drawStitch(Offset(cx - 18f, cy - 42f), Offset(cx + 22f, cy - 35f))

    // Gold Stitch 2 (Upper mid)
    drawStitch(Offset(cx - 24f, cy - 14f), Offset(cx + 18f, cy - 22f))

    // Gold Stitch 3 (Center)
    drawStitch(Offset(cx - 20f, cy + 8f), Offset(cx + 26f, cy + 0f))

    // Gold Stitch 4 (Lower)
    drawStitch(Offset(cx - 22f, cy + 34f), Offset(cx + 18f, cy + 22f))

    // Gold Stitch 5 (Left branch stitch)
    drawStitch(Offset(cx - 40f, cy - 32f), Offset(cx - 30f, cy - 8f))

    // Gold Seam flowing inside main crack
    drawPath(mainCrackPath, brush = goldThreadBrush, style = threadStroke)
    drawPath(leftCrackPath, brush = goldThreadBrush, style = Stroke(width = 2.0f, cap = StrokeCap.Round))
    drawPath(rightCrackPath, brush = goldThreadBrush, style = Stroke(width = 2.0f, cap = StrokeCap.Round))
    drawPath(bottomCrackPath, brush = goldThreadBrush, style = Stroke(width = 2.0f, cap = StrokeCap.Round))

    // ----------------------------------------------------
    // GOLDEN KNOTS (Glowing anchor points at seam joints)
    // ----------------------------------------------------
    val knotPoints = listOf(
        Offset(cx - 18f, cy - 42f),
        Offset(cx + 22f, cy - 35f),
        Offset(cx - 24f, cy - 14f),
        Offset(cx + 18f, cy - 22f),
        Offset(cx - 20f, cy + 8f),
        Offset(cx + 26f, cy + 0f),
        Offset(cx - 22f, cy + 34f),
        Offset(cx - 40f, cy - 32f),
        Offset(cx + 6f, cy - 38f),
        Offset(cx + 8f, cy + 4f)
    )

    for (pt in knotPoints) {
        // Outer glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xB3EFD58A), Color.Transparent),
                center = pt,
                radius = 7f
            ),
            radius = 6.5f,
            center = pt
        )
        // Solid gold center
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, goldLight, gold),
                center = Offset(pt.x - 1f, pt.y - 1f),
                radius = 3.5f
            ),
            radius = 3.2f,
            center = pt
        )
    }
}
