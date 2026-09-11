package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ai.EmdrVisualConfig
import com.example.audio.SoltarSoundManager
import com.example.data.SoltarFramework
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.abs

@Composable
fun EmdrVisualDialog(
    viewModel: SoltarViewModel,
    initialConfig: EmdrVisualConfig? = null,
    textoBase: String = "",
    nombreEx: String = "",
    onDismiss: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()

    val resolvedFramework = remember(settings?.preferredFramework) {
        try {
            SoltarFramework.valueOf(settings?.preferredFramework ?: "PSICOLOGIA_MODERNA")
        } catch (_: Exception) {
            SoltarFramework.PSICOLOGIA_MODERNA
        }
    }

    var currentConfig by remember {
        mutableStateOf(
            initialConfig ?: com.example.ai.SoltarAiEngine.calculateLocalEmdrVisualConfig(
                textoBase = textoBase,
                framework = resolvedFramework,
                nombreEx = nombreEx.ifBlank { settings?.userName ?: "" }
            )
        )
    }

    var isGeneratingAi by remember { mutableStateOf(false) }

    // Generar via AI Engine si no vino precargado
    LaunchedEffect(Unit) {
        if (initialConfig == null) {
            isGeneratingAi = true
            try {
                val aiConfig = com.example.ai.SoltarAiEngine.generateEmdrVisualSession(
                    textoBase = textoBase,
                    framework = resolvedFramework,
                    nombreEx = nombreEx.ifBlank { settings?.userName ?: "" }
                )
                currentConfig = aiConfig
            } catch (_: Exception) {
                // Mantiene el fallback local calculado
            } finally {
                isGeneratingAi = false
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        EmdrVisualContent(
            config = currentConfig,
            isGeneratingAi = isGeneratingAi,
            onClose = onDismiss,
            onUpdateSpeed = { newHz, newLabel ->
                currentConfig = currentConfig.copy(
                    animacion = currentConfig.animacion.copy(
                        frecuencia_hz = newHz,
                        velocidad_comercial = newLabel
                    )
                )
            },
            onRegenerateWithText = { newText, newName ->
                viewModel.scopeLaunch {
                    isGeneratingAi = true
                    try {
                        val aiConfig = com.example.ai.SoltarAiEngine.generateEmdrVisualSession(
                            textoBase = newText,
                            framework = resolvedFramework,
                            nombreEx = newName
                        )
                        currentConfig = aiConfig
                    } finally {
                        isGeneratingAi = false
                    }
                }
            }
        )
    }
}

@Composable
fun EmdrVisualContent(
    config: EmdrVisualConfig,
    isGeneratingAi: Boolean,
    onClose: () -> Unit,
    onUpdateSpeed: (Float, String) -> Unit,
    onRegenerateWithText: (String, String) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val fondoColor = remember(config.paleta_colores.color_fondo_hex) {
        config.paleta_colores.parseFondoColor(Color(0xFF0D0D0D))
    }

    val esferaColor = remember(config.paleta_colores.color_esfera_hex) {
        config.paleta_colores.parseEsferaColor(Color(0xFF4A90E2))
    }

    var isPlaying by remember { mutableStateOf(true) }
    var isAudioEnabled by remember { mutableStateOf(true) }
    var cyclesCount by remember { mutableIntStateOf(0) }
    var showEditDialog by remember { mutableStateOf(false) }

    // Posición horizontal normalizada: 0f (extremo izquierdo) a 1f (extremo derecho)
    val spherePosition = remember { Animatable(0.5f) }

    // Frecuencia actual
    val frecuenciaHz = config.animacion.frecuencia_hz.coerceIn(0.4f, 3.0f)
    val sweepDurationMs = remember(frecuenciaHz) {
        ((1000f / (frecuenciaHz * 2f))).toInt().coerceAtLeast(160)
    }

    // Bucle de animación bilateral continua con sincronización de audio estéreo y vibración
    LaunchedEffect(isPlaying, sweepDurationMs, isAudioEnabled) {
        if (!isPlaying) return@LaunchedEffect

        // Comenzar desde el centro hacia la derecha si está recién inicializado
        while (isActive && isPlaying) {
            // 1. Mover hacia la derecha (1.0f)
            spherePosition.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(durationMillis = sweepDurationMs, easing = LinearEasing)
            )
            // Rebote en el extremo derecho: sonido oído derecho + haptic
            if (isAudioEnabled) {
                SoltarSoundManager.playBilateralPannedTone(isLeft = false, freq = 480.0, durationMs = 90)
            }
            try { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) } catch (_: Exception) {}

            // 2. Mover hacia la izquierda (0.0f)
            spherePosition.animateTo(
                targetValue = 0.0f,
                animationSpec = tween(durationMillis = sweepDurationMs, easing = LinearEasing)
            )
            // Rebote en el extremo izquierdo: sonido oído izquierdo + haptic
            if (isAudioEnabled) {
                SoltarSoundManager.playBilateralPannedTone(isLeft = true, freq = 432.0, durationMs = 90)
            }
            try { haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove) } catch (_: Exception) {}

            cyclesCount++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoColor)
            .testTag("emdr_visual_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TOP BAR: Salir, Indicador de Velocidad / Frecuencia, Audio Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(42.dp)
                        .background(Color.White.copy(alpha = 0.08f), CircleShape)
                        .testTag("emdr_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar sesión EMDR",
                        tint = Color.White.copy(alpha = 0.85f)
                    )
                }

                // Badge de Frecuencia / Perfil
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(esferaColor, CircleShape)
                        )
                        Text(
                            text = "${config.animacion.velocidad_comercial} · ${"%.1f".format(frecuenciaHz)} Hz",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                // Audio bilateral toggle
                IconButton(
                    onClick = { isAudioEnabled = !isAudioEnabled },
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            if (isAudioEnabled) esferaColor.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.08f),
                            CircleShape
                        )
                        .testTag("emdr_audio_toggle")
                ) {
                    Icon(
                        imageVector = if (isAudioEnabled) Icons.Default.Headphones else Icons.Default.HeadsetOff,
                        contentDescription = "Audio bilateral alterno",
                        tint = if (isAudioEnabled) esferaColor else Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // GUÍA VISUAL SUPERIOR
            Text(
                text = config.instrucciones.guia_visual,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (isAudioEnabled) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🎧 ${config.instrucciones.alerta_audio}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.55f),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.weight(0.15f))

            // ==========================================
            // PISTA DE ESTIMULACIÓN BILATERAL (ESFERA)
            // ==========================================
            val estilo = config.animacion.estilo_esfera.uppercase()
            val currentPos = spherePosition.value

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentAlignment = Alignment.Center
            ) {
                // Línea de trayectoria sutil
                Canvas(modifier = Modifier.fillMaxWidth().height(2.dp)) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.12f),
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 2.dp.toPx()
                    )
                }

                // Render de la esfera interactiva con estilos GLOW / LINEAL / FADE
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val trackWidth = maxWidth - 48.dp
                    val offsetX = trackWidth * currentPos

                    // Cálculo de opacidad para estilo FADE (se atenúa en los bordes)
                    val fadeAlpha = if (estilo.contains("FADE")) {
                        // Máxima opacidad al centro (pos 0.5), menor a los extremos (0 o 1)
                        val distFromCenter = abs(currentPos - 0.5f) * 2f // 0f en el centro, 1f en bordes
                        (1f - (distFromCenter * 0.55f)).coerceIn(0.35f, 1f)
                    } else {
                        1.0f
                    }

                    // Cálculo de brillo/glow para estilo GLOW (pulso en bordes)
                    val isNearEdge = currentPos < 0.12f || currentPos > 0.88f
                    val glowScale = if (estilo.contains("GLOW") && isNearEdge) 1.25f else 1.0f

                    Box(
                        modifier = Modifier
                            .offset(x = offsetX + 4.dp, y = (maxHeight - 40.dp) / 2)
                            .size((40 * glowScale).dp)
                    ) {
                        // Halo exterior para estilo GLOW
                        if (estilo.contains("GLOW")) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .blur(14.dp)
                                    .background(
                                        Brush.radialGradient(
                                            listOf(esferaColor.copy(alpha = 0.65f), Color.Transparent)
                                        ),
                                        CircleShape
                                    )
                            )
                        }

                        // Cuerpo de la Esfera
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = CircleShape,
                            color = esferaColor.copy(alpha = fadeAlpha),
                            shadowElevation = if (estilo.contains("GLOW")) 12.dp else 2.dp,
                            border = if (estilo.contains("LINEAL")) BorderStroke(1.5.dp, Color.White.copy(alpha = 0.4f)) else null
                        ) {}
                    }
                }
            }

            Spacer(modifier = Modifier.weight(0.15f))

            // ==========================================
            // TEXTO PROCESADO / ANCLAJE COGNITIVO
            // ==========================================
            val textAlpha = config.paleta_colores.opacidad_texto.coerceIn(0.4f, 1.0f)

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                shape = RoundedCornerShape(18.dp),
                color = Color.White.copy(alpha = 0.05f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.10f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "REPROCESAMIENTO BILATERAL",
                        style = MaterialTheme.typography.labelSmall,
                        color = esferaColor,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = config.contenido.texto_procesado,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = textAlpha),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Ciclos bilaterales completados: $cyclesCount",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.45f),
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SELECTOR DE FRECUENCIA / VELOCIDAD
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SpeedChip(
                    label = "Lento (0.8 Hz)",
                    selected = (frecuenciaHz <= 0.9f),
                    activeColor = esferaColor,
                    testTag = "emdr_speed_chip_lento",
                    onClick = { onUpdateSpeed(0.8f, "LENTO") },
                    modifier = Modifier.weight(1f)
                )
                SpeedChip(
                    label = "Medio (1.0 Hz)",
                    selected = (frecuenciaHz in 0.95f..1.15f),
                    activeColor = esferaColor,
                    testTag = "emdr_speed_chip_medio",
                    onClick = { onUpdateSpeed(1.0f, "MEDIO") },
                    modifier = Modifier.weight(1f)
                )
                SpeedChip(
                    label = "Rápido (1.5 Hz)",
                    selected = (frecuenciaHz >= 1.3f),
                    activeColor = esferaColor,
                    testTag = "emdr_speed_chip_rapido",
                    onClick = { onUpdateSpeed(1.5f, "RAPIDO") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CONTROLES INFERIORES: Play/Pause, Personalizar texto, Finalizar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Play / Pause
                FilledTonalButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("emdr_play_pause_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isPlaying) Color.White.copy(alpha = 0.12f) else esferaColor.copy(alpha = 0.28f),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isPlaying) "Pausar" else "Reanudar", fontWeight = FontWeight.SemiBold)
                }

                // Personalizar pensamiento
                OutlinedButton(
                    onClick = { showEditDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Editar Frase", fontSize = 13.sp)
                }

                // Finalizar / Respirar
                Button(
                    onClick = onClose,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = esferaColor,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Finalizar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
        }

        // Dialog para editar frase o nombre
        if (showEditDialog) {
            EditEmdrPhraseDialog(
                currentText = config.contenido.texto_procesado,
                onDismiss = { showEditDialog = false },
                onSave = { newText, newName ->
                    showEditDialog = false
                    onRegenerateWithText(newText, newName)
                }
            )
        }
    }
}

@Composable
private fun SpeedChip(
    label: String,
    selected: Boolean,
    activeColor: Color,
    testTag: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(38.dp)
            .clickable(onClick = onClick)
            .testTag(testTag),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) activeColor.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.05f),
        border = BorderStroke(1.dp, if (selected) activeColor else Color.White.copy(alpha = 0.1f))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = if (selected) Color.White else Color.White.copy(alpha = 0.6f),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun EditEmdrPhraseDialog(
    currentText: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var textInput by remember { mutableStateOf(currentText) }
    var nameInput by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Personalizar Pensamiento EMDR", fontWeight = FontWeight.Bold, color = TextPrimary)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "Introduce el pensamiento o impulso de contacto para someterlo a desensibilización bilateral con la IA:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Texto o impulso a reprocesar") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Nombre de la expareja (opcional)") },
                    placeholder = { Text("Ej: Carlos") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(textInput, nameInput) },
                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
            ) {
                Text("Aplicar y Reprocesar", color = SoltarBackground, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        }
    )
}
