package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SoltarAmber
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextMuted
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.min

/**
 * progressRatio: 0f (recien roto, caja minima) a 1f (progreso real maximo,
 * segun el mismo vulnerabilityScore/streakDays que ya usa ProgressManager).
 * La caja NUNCA se encoge una vez alcanzado un progressRatio mayor - eso lo
 * controla quien llama a este composable, pasando siempre el maximo historico,
 * no el valor del dia (igual que el kintsugi no retrocede por un mal dia).
 */
@Composable
fun GriefSpaceVisualization(
    progressRatio: Float, // 0f..1f, ya calculado como maximo historico alcanzado
    modifier: Modifier = Modifier
) {
    val density = androidx.compose.ui.platform.LocalDensity.current

    // La caja crece de un tamaño minimo a uno maximo segun el progreso real
    val boxWidthDp by animateFloatAsState(
        targetValue = 140f + (340f * progressRatio.coerceIn(0f, 1f)),
        animationSpec = tween(durationMillis = 800, easing = LinearEasing),
        label = "boxWidth"
    )
    val boxHeightDp = 170f // altura fija, solo el ancho representa "espacio ganado"

    var ballPos by remember { mutableStateOf(Offset(20f, 70f)) }
    var ballVelocity by remember { mutableStateOf(Offset(2.2f, 1.7f)) }
    var hitFlash by remember { mutableFloatStateOf(0f) }

    val ballRadius = 8f
    // "DUELO" ocupa el centro de la caja, con un radio de colision fijo
    val griefLabelRadius = 34f

    LaunchedEffect(Unit) {
        while (true) {
            withFrameMillisSafe {
                val boxWpx = with(density) { boxWidthDp.dp.toPx() }
                val boxHpx = with(density) { boxHeightDp.dp.toPx() }
                val griefCenter = Offset(boxWpx / 2f, boxHpx / 2f)

                var newX = ballPos.x + ballVelocity.x
                var newY = ballPos.y + ballVelocity.y
                var newVx = ballVelocity.x
                var newVy = ballVelocity.y

                if (newX - ballRadius < 0f || newX + ballRadius > boxWpx) {
                    newVx = -newVx
                    newX = newX.coerceIn(ballRadius, max(ballRadius, boxWpx - ballRadius))
                }
                if (newY - ballRadius < 0f || newY + ballRadius > boxHpx) {
                    newVy = -newVy
                    newY = newY.coerceIn(ballRadius, boxHpx - ballRadius)
                }

                // Colision con DUELO: rebote simple + destello breve
                val distToGrief = kotlin.math.hypot(newX - griefCenter.x, newY - griefCenter.y)
                if (distToGrief < griefLabelRadius + ballRadius) {
                    val dx = newX - griefCenter.x
                    val dy = newY - griefCenter.y
                    val len = max(1f, kotlin.math.hypot(dx, dy))
                    newVx = (dx / len) * kotlin.math.hypot(newVx, newVy)
                    newVy = (dy / len) * kotlin.math.hypot(newVx, newVy)
                    hitFlash = 1f
                }

                ballPos = Offset(newX, newY)
                ballVelocity = Offset(newVx, newVy)
                if (hitFlash > 0f) hitFlash = max(0f, hitFlash - 0.08f)
            }
        }
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .width(boxWidthDp.dp)
                .height(boxHeightDp.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Marco de la caja
                drawRect(
                    color = Color(0xFFBFAE9A),
                    style = Stroke(width = 2f)
                )
                // Etiqueta DUELO en el centro, con un leve destello al ser golpeada
                val griefColor = Color(0xFF8F5A1E).copy(alpha = 0.55f + (hitFlash * 0.45f))
                drawCircle(
                    color = griefColor,
                    radius = griefLabelRadius,
                    center = Offset(size.width / 2f, size.height / 2f)
                )
                // Pelota
                drawCircle(
                    color = Color(0xFFE7A94F),
                    radius = ballRadius,
                    center = ballPos
                )
            }
            Text(
                text = "DUELO",
                style = TextStyle(fontSize = 10.sp, color = Color.White),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "No necesitas dejar de sentir. Necesitas recuperar espacio.",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            textAlign = TextAlign.Center
        )
    }
}

// Compose aun no tiene un "withFrameMillis" con delay incorporado de forma
// sencilla dentro de un while(true) sin importar frameClock manualmente - usa
// esta funcion auxiliar simple basada en delay(16) (~60fps aproximado), mas
// liviana de implementar de forma segura para este caso:
private suspend fun withFrameMillisSafe(block: () -> Unit) {
    block()
    delay(16)
}
