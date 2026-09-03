package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.LocalContext
import com.example.ai.OnDeviceLlmEngine
import com.example.audio.SoltarSoundManager
import com.example.audio.SoltarTtsManager
import com.example.data.SoltarFramework
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SemanticBellAndSoundscapesDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val checkins by viewModel.checkins.collectAsState()
    val vulnerabilityScore by viewModel.vulnerabilityScore.collectAsState()
    
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Campana Semántica, 1 = Paisajes de Calma, 2 = Voz Guiada

    // Semantic Bell state
    var isBellPlaying by remember { mutableStateOf(false) }
    var bellRoundsRemaining by remember { mutableIntStateOf(0) }
    var selectedDurationMinutes by remember { mutableIntStateOf(1) } // 1, 3, 5 mins

    // Soundscape state
    var activeSoundscape by remember { mutableStateOf<SoltarSoundManager.SoundscapeType?>(null) }

    // TTS state
    var isTtsSpeaking by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        SoltarTtsManager.initialize(context)
        SoltarTtsManager.onSpeakingStateChanged = { speaking ->
            isTtsSpeaking = speaking
        }
    }

    // Timer coroutine for semantic bell
    LaunchedEffect(bellRoundsRemaining, isBellPlaying) {
        if (isBellPlaying && bellRoundsRemaining > 0) {
            SoltarSoundManager.playSound(SoltarSoundManager.SoundType.CALM_BELL)
            delay(10000L) // strike every 10 seconds during regulation session
            bellRoundsRemaining--
            if (bellRoundsRemaining <= 0) {
                isBellPlaying = false
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            SoltarSoundManager.stopSoundscape()
            SoltarTtsManager.stop()
        }
    }

    Dialog(
        onDismissRequest = {
            SoltarSoundManager.stopSoundscape()
            SoltarTtsManager.stop()
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.925f)
                .fillMaxHeight(0.85f)
                .testTag("semantic_bell_dialog"),
            shape = RoundedCornerShape(24.dp),
            color = SoltarBackground,
            border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SoltarAmber.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = SoltarAmber)
                        }
                        Column {
                            Text(
                                text = "SALA DE REGULACIÓN PRO",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Campana & Paisajes Sonoros",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            SoltarSoundManager.stopSoundscape()
                            onDismiss()
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoltarSurface, RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TabButton(
                        title = "Campana",
                        selected = selectedTab == 0,
                        modifier = Modifier.weight(1f)
                    ) {
                        selectedTab = 0
                        SoltarSoundManager.stopSoundscape()
                        SoltarTtsManager.stop()
                        isBellPlaying = false
                    }
                    TabButton(
                        title = "Paisajes",
                        selected = selectedTab == 1,
                        modifier = Modifier.weight(1f)
                    ) {
                        selectedTab = 1
                        SoltarTtsManager.stop()
                        isBellPlaying = false
                        bellRoundsRemaining = 0
                    }
                    TabButton(
                        title = "Voz Guiada",
                        selected = selectedTab == 2,
                        modifier = Modifier.weight(1f)
                    ) {
                        selectedTab = 2
                        SoltarSoundManager.stopSoundscape()
                        isBellPlaying = false
                        bellRoundsRemaining = 0
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Content
                if (selectedTab == 0) {
                    // CAMPANA SEMÁNTICA
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                                border = BorderStroke(1.dp, SoltarBorder)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "FRECUENCIA 528HZ • ANCLAJE SENSORIAL",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "La campana semántica emite un armónico afinado para interromper el ciclo rumiante, anclarte en el presente y regular tu sistema nervioso ante picos de ansiedad o impulso.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        lineHeight = 20.sp
                                    )
                                }
                            }
                        }

                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(10.dp))
                                
                                // Pulsing bell indicator
                                Box(
                                    modifier = Modifier
                                        .size(110.dp)
                                        .clip(CircleShape)
                                        .background(if (isBellPlaying) SoltarAmber.copy(alpha = 0.25f) else SoltarSurface)
                                        .clickable {
                                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                            if (isBellPlaying) {
                                                isBellPlaying = false
                                                bellRoundsRemaining = 0
                                            } else {
                                                isBellPlaying = true
                                                bellRoundsRemaining = selectedDurationMinutes * 6 // 6 strikes per minute approx
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isBellPlaying) Icons.Default.Pause else Icons.Default.NotificationsActive,
                                        contentDescription = null,
                                        tint = SoltarAmber,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = if (isBellPlaying) "Sesión Activa ($bellRoundsRemaining campanadas restantes)" else "Pulsa para iniciar campana",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = "Duración de la sesión:",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    listOf(1, 3, 5).forEach { mins ->
                                        FilterChip(
                                            selected = selectedDurationMinutes == mins,
                                            onClick = {
                                                selectedDurationMinutes = mins
                                                if (!isBellPlaying) {
                                                    SoltarSoundManager.playSound(SoltarSoundManager.SoundType.CALM_BELL)
                                                }
                                            },
                                            label = { Text("$mins min") },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = SoltarAmber,
                                                selectedLabelColor = SoltarBackground,
                                                containerColor = SoltarSurface,
                                                labelColor = TextPrimary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // PAISAJES DE CALMA
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "Elige un entorno sonoro para sostener tu calma y regular tu atención:",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        val landscapes = listOf(
                            Triple(SoltarSoundManager.SoundscapeType.KINTSUGI_RAIN, "Lluvia Kintsugi", "Gotas suaves sobre cerámica y filtración armónica"),
                            Triple(SoltarSoundManager.SoundscapeType.OCEAN_WAVES, "Olas de Sostén", "Marea rítmica para respiración y contención del impulso"),
                            Triple(SoltarSoundManager.SoundscapeType.FOREST_CALM, "Bosque de Calma", "Cuencos tibetanos y brisa sutil entre pinos"),
                            Triple(SoltarSoundManager.SoundscapeType.DEEP_SILENCE, "Viento y Silencio", "Frecuencias puras 528Hz de reconstrucción neural")
                        )

                        items(landscapes.size) { index ->
                            val (type, title, desc) = landscapes[index]
                            val isPlaying = activeSoundscape == type

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                        if (isPlaying) {
                                            SoltarSoundManager.stopSoundscape()
                                            activeSoundscape = null
                                        } else {
                                            SoltarSoundManager.startSoundscape(type)
                                            activeSoundscape = type
                                        }
                                    },
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isPlaying) SoltarSurfaceElevated else SoltarSurface
                                ),
                                border = BorderStroke(1.dp, if (isPlaying) SoltarAmber else SoltarBorderSubtle)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = title,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = if (isPlaying) SoltarAmber else TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = desc,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(if (isPlaying) SoltarAmber else SoltarSurfaceElevated),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = if (isPlaying) SoltarBackground else SoltarAmber,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (selectedTab == 2) {
                    // TAB 2: MEDITACIÓN GUIADA POR VOZ (IA On-Device)
                    val framework = uiState.preferredFramework
                    val userName = settings?.userName ?: ""
                    val latestCheckin = checkins.firstOrNull()
                    val script = remember(vulnerabilityScore, framework, latestCheckin) {
                        OnDeviceLlmEngine.generateGuidedMeditationScript(
                            vulnerabilityScore = vulnerabilityScore.toInt(),
                            framework = framework,
                            userName = userName,
                            latestCheckin = latestCheckin
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                                border = BorderStroke(1.5.dp, SoltarAmber)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = SoltarAmber)
                                        Text(
                                            text = script.targetVulnerabilityBand,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SoltarAmber,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = script.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Cadencia: ${script.toneInstruction}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = {
                                            if (isTtsSpeaking) {
                                                SoltarTtsManager.stop()
                                            } else {
                                                SoltarTtsManager.speakMeditation(
                                                    text = script.fullText,
                                                    vulnerabilityScore = vulnerabilityScore
                                                )
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isTtsSpeaking) Color(0xFFEF4444) else SoltarAmber,
                                            contentColor = if (isTtsSpeaking) Color.White else SoltarBackground
                                        )
                                    ) {
                                        Icon(
                                            imageVector = if (isTtsSpeaking) Icons.Default.Stop else Icons.Default.VolumeUp,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (isTtsSpeaking) "Detener Voz Guiada" else "Escuchar Meditación Guiada (Voz AI)",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                                border = BorderStroke(1.dp, SoltarBorder)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "TEXTO DE LA MEDITACIÓN:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = script.fullText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                        lineHeight = 24.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        SoltarSoundManager.stopSoundscape()
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarSurfaceElevated)
                ) {
                    Text("Volver", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun TabButton(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(38.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (selected) SoltarAmber else Color.Transparent
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) SoltarBackground else TextSecondary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
