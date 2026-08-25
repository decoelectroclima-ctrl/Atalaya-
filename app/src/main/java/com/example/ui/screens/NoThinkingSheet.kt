package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun NoThinkingDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    var breathingPhase by remember { mutableStateOf("Inhala") }
    var breathingProgress by remember { mutableIntStateOf(4) }
    var isBreathingActive by remember { mutableStateOf(true) }

    // Breathing timer cycle (4s in, 4s hold, 4s out, 4s hold)
    LaunchedEffect(isBreathingActive) {
        while (isBreathingActive) {
            breathingPhase = "Inhala profundo"
            for (i in 4 downTo 1) {
                breathingProgress = i
                delay(1000)
            }
            breathingPhase = "Sostén el aire"
            for (i in 4 downTo 1) {
                breathingProgress = i
                delay(1000)
            }
            breathingPhase = "Exhala lento y vacía"
            for (i in 4 downTo 1) {
                breathingProgress = i
                delay(1000)
            }
            breathingPhase = "Pausa en calma"
            for (i in 4 downTo 1) {
                breathingProgress = i
                delay(1000)
            }
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val circleScale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = SoltarBackground,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoltarSurface)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("no_thinking_dialog_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Text(
                        text = "ANCLAJE AL PRESENTE",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarSage,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "«No necesitas resolver nada ahora mismo.»",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Tu mente está saturada. Vamos a salir de los pensamientos y regresar a tus 5 sentidos corporales.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                // Breathing Sphere Visualizer
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(SoltarSurfaceElevated)
                        .border(2.dp, SoltarSage.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .scale(circleScale)
                            .clip(CircleShape)
                            .background(SoltarSage.copy(alpha = 0.25f))
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = breathingPhase,
                            style = MaterialTheme.typography.labelLarge,
                            color = SoltarSageLight,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$breathingProgress s",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // 5-4-3-2-1 Grounding Cards
                Text(
                    text = "TÉCNICA SENSORIAL 5-4-3-2-1",
                    style = MaterialTheme.typography.labelMedium,
                    color = SoltarAmber,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                GroundingStepRow(
                    number = "5",
                    icon = Icons.Default.Visibility,
                    title = "Mira 5 cosas a tu alrededor",
                    subtitle = "Observa colores, texturas o formas en tu habitación."
                )

                GroundingStepRow(
                    number = "4",
                    icon = Icons.Default.TouchApp,
                    title = "Toca 4 superficies distintas",
                    subtitle = "Siente la ropa que llevas, el frío de la mesa o el suelo bajo tus pies."
                )

                GroundingStepRow(
                    number = "3",
                    icon = Icons.Default.Hearing,
                    title = "Escucha 3 sonidos reales",
                    subtitle = "El zumbido del aire, un pájaro, tu propia respiración."
                )

                GroundingStepRow(
                    number = "2",
                    icon = Icons.Default.PanTool,
                    title = "Huele 2 aromas presentes",
                    subtitle = "Un café, el jabón de tus manos o el aire fresco."
                )

                GroundingStepRow(
                    number = "1",
                    icon = Icons.Default.Restaurant,
                    title = "Sabor o sensación en tu boca",
                    subtitle = "Toma un sorbo de agua fría con atención plena."
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("no_thinking_finish_cta"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarSage, contentColor = SoltarBackground)
                ) {
                    Text("Estoy en el presente • Cerrar", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun GroundingStepRow(
    number: String,
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SoltarAmber.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(number, color = SoltarAmber, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }

            Icon(icon, contentDescription = null, tint = SoltarSage, modifier = Modifier.size(20.dp))
        }
    }
}
