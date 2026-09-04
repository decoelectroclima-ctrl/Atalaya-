package com.example.ui.screens

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoltarSoundManager
import com.example.data.SoltarFramework
import com.example.data.SubscriptionPlan
import com.example.data.UserEntitlements
import com.example.data.WisdomBank
import com.example.ui.SoltarViewModel
import com.example.ui.dialogs.SemanticBellAndSoundscapesDialog
import com.example.ui.managers.ProgressManager
import com.example.ui.components.KintsugiHeart
import com.example.ui.components.ProgressiveLandscape
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun TodayScreen(
    viewModel: SoltarViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val checkins by viewModel.checkins.collectAsState()
    val context = LocalContext.current

    val vulnerabilityScore by viewModel.vulnerabilityScore.collectAsState()
    val vulnerabilityMode = when {
        vulnerabilityScore >= 70 -> "REFUGIO"
        vulnerabilityScore >= 35 -> "PRESENTE"
        else -> "EXPLORACION"
    }
    val relapses by viewModel.relapses.collectAsState()
    val now = System.currentTimeMillis()
    val hasRelapse48h = remember(relapses) {
        relapses.any { r -> (now - r.timestamp) < (48L * 3600 * 1000) }
    }
    var showMoreToolsInPresent by remember { mutableStateOf(false) }

    // Live clock ticker for No-Contact Counter
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = System.currentTimeMillis()
        }
    }

    val noContactStart = settings?.breakupDateTimestamp ?: (currentTime - (14L * 24 * 3600 * 1000))
    val initialStartRaw = settings?.initialStartDateTimestamp ?: 0L
    val initialStart = if (initialStartRaw > 0L) initialStartRaw else noContactStart
    val elapsedMillis = (currentTime - noContactStart).coerceAtLeast(0L)
    val totalAccumulatedMillis = (currentTime - initialStart).coerceAtLeast(0L)

    var showSemanticBellDialog by remember { mutableStateOf(false) }

    val totalSeconds = elapsedMillis / 1000
    val days = totalSeconds / (24 * 3600)
    val hours = (totalSeconds % (24 * 3600)) / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    val totalAccumulatedDays = totalAccumulatedMillis / (24 * 3600 * 1000)

    // Milestones
    val (milestoneTitle, nextMilestoneDays, milestoneProgress) = remember(days) {
        when {
            days < 1 -> Triple("Primeras 24 Horas • Anclaje", 1, (totalSeconds.toFloat() / (24 * 3600)).coerceIn(0f, 1f))
            days < 3 -> Triple("Fase Aguda • Contención", 3, (days.toFloat() / 3f).coerceIn(0f, 1f))
            days < 7 -> Triple("7 Días • Desintoxicación", 7, (days.toFloat() / 7f).coerceIn(0f, 1f))
            days < 14 -> Triple("14 Días • Claridad Inicial", 14, (days.toFloat() / 14f).coerceIn(0f, 1f))
            days < 30 -> Triple("30 Días • Reconfiguración Neural", 30, (days.toFloat() / 30f).coerceIn(0f, 1f))
            days < 60 -> Triple("60 Días • Estabilización Emocional", 60, (days.toFloat() / 60f).coerceIn(0f, 1f))
            days < 90 -> Triple("90 Días • Soberanía y Dignidad", 90, (days.toFloat() / 90f).coerceIn(0f, 1f))
            else -> Triple("Reconstrucción Kintsugi • Plena Autonomía", 180, 1.0f)
        }
    }
    val progressStage = remember(days) { ProgressManager.calculateProgressStage(days.toInt()) }

    var isThermometerExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoltarBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp)
    ) {
        // Subtle config status indicator
        item {
            val modelState by com.example.ai.OnDeviceModelManager.modelState.collectAsState()
            when (val state = modelState) {
                is com.example.ai.OnDeviceModelManager.ModelState.Downloading -> {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = SoltarSurfaceElevated,
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Configurando experiencia...",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                            Text(
                                text = "${(state.progress * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                is com.example.ai.OnDeviceModelManager.ModelState.Ready -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "IA Activa",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
                else -> {}
            }
        }

        // Journey Stage Switcher Card (ADRIANA Recovery vs ADRIANA Life Coach)
        item {
            val currentStage = settings?.journeyStage ?: "RECOVERY"
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, if (currentStage == "LIFE_COACH") SoltarAmber.copy(alpha = 0.6f) else SoltarBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(
                                imageVector = if (currentStage == "LIFE_COACH") Icons.Default.EmojiEvents else Icons.Default.Favorite,
                                contentDescription = null,
                                tint = SoltarAmber
                            )
                            Text(
                                text = if (currentStage == "LIFE_COACH") "ADRIANA Life Coach" else "ADRIANA Recovery",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Surface(
                            color = if (currentStage == "LIFE_COACH") SoltarAmber.copy(alpha = 0.2f) else SoltarSage.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = if (currentStage == "LIFE_COACH") "Crecimiento" else "Sanación",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (currentStage == "LIFE_COACH") SoltarAmber else SoltarSage,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = if (currentStage == "LIFE_COACH") 
                            "Has recorrido un largo camino. Ahora trabajamos en quién quieres ser: hábitos, autoestima, propósito y disciplina diaria."
                        else 
                            "Acompañamiento en duelo, contacto cero, regulación emocional y reconstrucción de rutinas.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.setJourneyStage("RECOVERY") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (currentStage == "RECOVERY") SoltarSurfaceElevated else Color.Transparent
                            ),
                            border = BorderStroke(1.dp, if (currentStage == "RECOVERY") SoltarAmber else SoltarBorder)
                        ) {
                            Text("1. Recovery", style = MaterialTheme.typography.labelSmall, color = if (currentStage == "RECOVERY") SoltarAmber else TextSecondary)
                        }

                        Button(
                            onClick = { viewModel.setJourneyStage("LIFE_COACH") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentStage == "LIFE_COACH") SoltarAmber else SoltarSurfaceElevated
                            )
                        ) {
                            Text("2. Life Coach", style = MaterialTheme.typography.labelSmall, color = if (currentStage == "LIFE_COACH") SoltarBackground else TextSecondary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Ave Fénix Coach Life Dashboard (When journeyStage == "LIFE_COACH")
        val currentStage = settings?.journeyStage ?: "RECOVERY"
        if (currentStage == "LIFE_COACH") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = SoltarAmber)
                            Text(
                                text = "Ave Fénix • Tu Nuevo Renacer",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "El duelo y el dolor han quedado atrás. Esta es tu nueva etapa de autoconocimiento, autoaceptación, autoestima y propósitos (fitness, nutrición, estudios, hábitos y crecimiento personal).",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Button(
                            onClick = { viewModel.toggleAiCompanionSheet(true) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Hablar con FOCO / Adriana (Coach Life)", color = SoltarBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Coach Goals & Purpose Section
            item {
                var newGoalTitle by remember { mutableStateOf("") }
                var newGoalCategory by remember { mutableStateOf("FITNESS") }
                var newGoalTarget by remember { mutableStateOf("4 días/sem") }
                val goals by viewModel.coachGoals.collectAsState()

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("🎯 Mis Metas y Propósitos", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                        
                        OutlinedTextField(
                            value = newGoalTitle,
                            onValueChange = { newGoalTitle = it },
                            label = { Text("Nuevo propósito (ej: Gimnasio, Estudiar, Meditar)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newGoalTarget,
                                onValueChange = { newGoalTarget = it },
                                label = { Text("Meta (ej: 4x sem)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )
                            Button(
                                onClick = {
                                    if (newGoalTitle.isNotBlank()) {
                                        viewModel.saveCoachGoal(newGoalTitle, newGoalCategory, newGoalTarget)
                                        newGoalTitle = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Text("Añadir", color = SoltarBackground, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (goals.isEmpty()) {
                            Text("No tienes metas registradas aún. ¡Añade tu primer propósito de renacer!", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                goals.forEach { goal ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(SoltarSurfaceElevated, RoundedCornerShape(10.dp))
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            Checkbox(
                                                checked = goal.isCompleted,
                                                onCheckedChange = { viewModel.toggleCoachGoal(goal.id, it) }
                                            )
                                            Column {
                                                Text(
                                                    text = goal.title,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = if (goal.isCompleted) TextSecondary else TextPrimary,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                                if (goal.targetValue.isNotBlank()) {
                                                    Text(text = "Meta: ${goal.targetValue}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                                }
                                            }
                                        }
                                        IconButton(onClick = { viewModel.deleteCoachGoal(goal.id) }) {
                                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFEF4444))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Body & Evolution Metrics Tracker
            item {
                var weightInput by remember { mutableStateOf("") }
                var heightInput by remember { mutableStateOf("") }
                var waistInput by remember { mutableStateOf("") }
                var armInput by remember { mutableStateOf("") }
                var legInput by remember { mutableStateOf("") }
                var notesInput by remember { mutableStateOf("") }
                val metrics by viewModel.bodyMetrics.collectAsState()

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("⚖️ Evolución Física y Corporal", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text("Registra tu peso, altura y medidas para seguir tu transformación física.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = weightInput,
                                onValueChange = { weightInput = it },
                                label = { Text("Peso (kg)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = heightInput,
                                onValueChange = { heightInput = it },
                                label = { Text("Altura (cm)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = waistInput,
                                onValueChange = { waistInput = it },
                                label = { Text("Cintura (cm)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = armInput,
                                onValueChange = { armInput = it },
                                label = { Text("Brazo (cm)") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true
                            )
                        }

                        Button(
                            onClick = {
                                val w = weightInput.toFloatOrNull() ?: 0f
                                val h = heightInput.toFloatOrNull() ?: 0f
                                val wa = waistInput.toFloatOrNull() ?: 0f
                                val ar = armInput.toFloatOrNull() ?: 0f
                                val lg = legInput.toFloatOrNull() ?: 0f
                                viewModel.saveBodyMetric(w, h, wa, ar, lg, notesInput)
                                weightInput = ""
                                waistInput = ""
                                armInput = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Registrar Medidas de Hoy", color = SoltarBackground, fontWeight = FontWeight.Bold)
                        }

                        if (metrics.isNotEmpty()) {
                            Text("Historial reciente:", style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold)
                            metrics.take(3).forEach { m ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().background(SoltarSurfaceElevated, RoundedCornerShape(8.dp)).padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Fecha: ${m.dateKey}", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                                    Text(text = "Peso: ${m.weightKg} kg | Cintura: ${m.waistCm} cm", style = MaterialTheme.typography.bodySmall, color = SoltarAmber, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Daily Check-in ("Check-in de Crecimiento")
            item {
                var wentToGym by remember { mutableStateOf(false) }
                var studiedOrWorked by remember { mutableStateOf(false) }
                var energyLevel by remember { mutableStateOf(8f) }
                var checkinNote by remember { mutableStateOf("") }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("☀️ Check-in Diario de Renacer", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("🏋️‍♂️ ¿Fuiste hoy al gimnasio o hiciste ejercicio?", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            Switch(checked = wentToGym, onCheckedChange = { wentToGym = it })
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("📚 ¿Estudiaste o avanzaste en tus metas?", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            Switch(checked = studiedOrWorked, onCheckedChange = { studiedOrWorked = it })
                        }

                        Text("⚡ Nivel de Energía (${energyLevel.toInt()}/10)", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                        Slider(
                            value = energyLevel,
                            onValueChange = { energyLevel = it },
                            valueRange = 1f..10f,
                            steps = 9
                        )

                        OutlinedTextField(
                            value = checkinNote,
                            onValueChange = { checkinNote = it },
                            label = { Text("Nota o reflexión del día...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Button(
                            onClick = {
                                viewModel.saveCoachCheckin(wentToGym, studiedOrWorked, "Enérgico", energyLevel.toInt(), checkinNote)
                                checkinNote = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Guardar Check-in de Hoy", color = SoltarBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            return@LazyColumn
        }
        item {
            val realAssessment by viewModel.realVulnerabilityAssessment.collectAsState()
            var isFactorsExpanded by remember { mutableStateOf(false) }

            val mode = realAssessment.mode
            val score = realAssessment.score
            val themeRed = Color(0xFFEF4444)
            val themeRedBg = Color(0xFFFEF2F2)
            val themeGreen = Color(0xFF10B981)
            val themeGreenBg = Color(0xFFECFDF5)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("vulnerability_assessment_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when(mode) {
                        "REFUGIO" -> themeRedBg
                        "PRESENTE" -> SoltarSurfaceElevated
                        else -> themeGreenBg
                    }
                ),
                border = BorderStroke(1.dp, when(mode) {
                    "REFUGIO" -> themeRed.copy(alpha = 0.8f)
                    "PRESENTE" -> SoltarAmber.copy(alpha = 0.8f)
                    else -> themeGreen.copy(alpha = 0.7f)
                })
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .animateContentSize()
                ) {
                    // Header row: Mode title, icon, and score badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when(mode) {
                                            "REFUGIO" -> themeRed.copy(alpha = 0.15f)
                                            "PRESENTE" -> SoltarAmber.copy(alpha = 0.15f)
                                            else -> themeGreen.copy(alpha = 0.15f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when(mode) {
                                        "REFUGIO" -> Icons.Default.Shield
                                        "PRESENTE" -> Icons.Default.SelfImprovement
                                        else -> Icons.Default.Explore
                                    },
                                    contentDescription = null,
                                    tint = when(mode) {
                                        "REFUGIO" -> themeRed
                                        "PRESENTE" -> SoltarAmber
                                        else -> themeGreen
                                    },
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = when(mode) {
                                        "REFUGIO" -> "MODO REFUGIO"
                                        "PRESENTE" -> "MODO PRESENTE"
                                        else -> "MODO EXPLORACIÓN"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = when(mode) {
                                        "REFUGIO" -> Color(0xFF991B1B)
                                        "PRESENTE" -> SoltarAmber
                                        else -> Color(0xFF065F46)
                                    }
                                )
                                Text(
                                    text = realAssessment.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // Score Pill
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = when(mode) {
                                "REFUGIO" -> themeRed
                                "PRESENTE" -> SoltarAmber
                                else -> themeGreen
                            }
                        ) {
                            Text(
                                text = "$score%",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Real Vulnerability Progress Bar
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Nivel de Vulnerabilidad Neural y Emocional",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "$score de 100",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { score / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = when(mode) {
                                "REFUGIO" -> themeRed
                                "PRESENTE" -> SoltarAmber
                                else -> themeGreen
                            },
                            trackColor = SoltarSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Primary Explanation grounded in user data
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SoltarSurface.copy(alpha = 0.7f),
                        border = BorderStroke(1.dp, SoltarBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "💡", fontSize = 14.sp)
                            Column {
                                Text(
                                    text = realAssessment.primaryExplanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Recomendación: ${realAssessment.clinicalRecommendation}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = when(mode) {
                                        "REFUGIO" -> themeRed
                                        "PRESENTE" -> SoltarAmber
                                        else -> themeGreen
                                    },
                                    lineHeight = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Check-in Call-to-Action if not logged today
                    if (!realAssessment.hasLoggedToday) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                viewModel.openEmotionalCheckin()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when(mode) {
                                    "REFUGIO" -> themeRed
                                    else -> SoltarAmber
                                }
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("vulnerability_checkin_button"),
                            contentPadding = PaddingValues(vertical = 10.dp, horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Registrar Check-in de Hoy para Calibrar",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Expandable Factor Breakdown Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                isFactorsExpanded = !isFactorsExpanded
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isFactorsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isFactorsExpanded) "Ocultar desglose de cálculo" else "Ver factores activos (${realAssessment.factors.size})",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = "${realAssessment.protectiveCount} protectores • ${realAssessment.riskCount} de riesgo",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }

                    // Expanded Factors List
                    AnimatedVisibility(visible = isFactorsExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            realAssessment.factors.forEach { factor ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (factor.isRisk) themeRedBg.copy(alpha = 0.7f) else themeGreenBg.copy(alpha = 0.7f),
                                    border = BorderStroke(
                                        0.5.dp,
                                        if (factor.isRisk) themeRed.copy(alpha = 0.4f) else themeGreen.copy(alpha = 0.4f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = if (factor.isRisk) Icons.Default.Warning else Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = if (factor.isRisk) themeRed else themeGreen,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Column {
                                                Text(
                                                    text = factor.title,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (factor.isRisk) Color(0xFF991B1B) else Color(0xFF065F46)
                                                )
                                                Text(
                                                    text = factor.description,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = TextSecondary,
                                                    fontSize = 10.sp,
                                                    lineHeight = 13.sp
                                                )
                                            }
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = if (factor.isRisk) themeRed.copy(alpha = 0.15f) else themeGreen.copy(alpha = 0.15f)
                                        ) {
                                            Text(
                                                text = factor.impactText,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = if (factor.isRisk) themeRed else themeGreen,
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Anticipated Risk Date Proactive Banner
        item {
            val riskDates by viewModel.riskDates.collectAsState()
            val triggers by viewModel.triggerEvents.collectAsState()
            val nowCal = remember { Calendar.getInstance() }
            val currentYr = nowCal.get(Calendar.YEAR)
            
            val upcomingRisk = remember(riskDates) {
                riskDates.mapNotNull { rd ->
                    val target = Calendar.getInstance().apply {
                        set(Calendar.YEAR, currentYr)
                        set(Calendar.MONTH, rd.month - 1)
                        set(Calendar.DAY_OF_MONTH, rd.day)
                    }
                    if (target.timeInMillis < nowCal.timeInMillis) {
                        target.add(Calendar.YEAR, 1)
                    }
                    val days = ((target.timeInMillis - nowCal.timeInMillis) / (1000L * 3600 * 24)).toInt()
                    if (days in 0..rd.reminderDaysBefore) {
                        Triple(rd, days, target.timeInMillis)
                    } else null
                }.minByOrNull { it.second }
            }

            if (upcomingRisk != null) {
                val (rd, days, _) = upcomingRisk
                val aiStrategy = remember(rd.id, days, triggers) {
                    com.example.ai.OnDeviceLlmEngine.generateRiskDateCopingStrategy(
                        riskDateTitle = rd.title,
                        daysUntil = days,
                        pastTriggers = triggers,
                        framework = uiState.preferredFramework
                    )
                }

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
                            Icon(Icons.Default.WarningAmber, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(22.dp))
                            Text(
                                text = if (days == 0) "🚨 ALERTA • HOY ES ${rd.title.uppercase()}" else "🛡️ PREVENCIÓN DE RIESGO • ${rd.title.uppercase()} EN $days DÍAS",
                                style = MaterialTheme.typography.titleSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (days == 0) "Hoy se cumple ${rd.title}. El riesgo de impulso es alto." else "Se acerca ${rd.title} en $days días. Nos anticipamos al momento difícil para sostener tu soberanía.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = aiStrategy,
                            style = MaterialTheme.typography.bodySmall,
                            color = SoltarAmber,
                            lineHeight = 18.sp
                        )

                        if (rd.customStrategy.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tu nota personal: ${rd.customStrategy}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoltarSage,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }

        // 1. Dynamic Wisdom Card (Rotates per framework, with interactive refresh)
        if (vulnerabilityMode != "REFUGIO") {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    KintsugiHeart(progressStage = progressStage, vulnerabilityScore = vulnerabilityScore)
                    ProgressiveLandscape(
                        progressStage = progressStage,
                        vulnerabilityScore = vulnerabilityScore,
                        onTapSun = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            // Abrir Diario
                            viewModel.openJournalModal()
                        },
                        onTapTree = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            // Abrir Red Flags (RelationshipAuditDialog)
                            viewModel.toggleAuditModal(true)
                        },
                        onTapMountain = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            // Abrir Biblioteca de Sabiduría
                            viewModel.rotateWisdomCard(uiState.preferredFramework)
                        }
                    )
                }
            }
        }
        
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                        viewModel.openEmotionalCheckin()
                    }
                    .testTag("today_emotional_checkin_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                border = BorderStroke(1.dp, SoltarAmber)
            ) {
                // ... (código existente del check-in)
            }
        }

        if (vulnerabilityMode != "REFUGIO") {
            item {
                val settings by viewModel.settings.collectAsState()
                val recommendation = remember(settings) {
                    com.example.ai.ContextualExperienceEngine.analyzeContext(settings)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contextual_recommendation_card"),
                    // ... (código existente de la recomendación)
                ) {
                    // ...
                }
            }
        }
        item {
            val wisdomCard = uiState.currentWisdomCard ?: WisdomBank.getRandomCard(uiState.preferredFramework, emptyList())

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wisdom_compass_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("✨", fontSize = 14.sp)
                            Text(
                                text = wisdomCard.title,
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(android.content.Intent.EXTRA_TEXT, "${wisdomCard.quote} — ${wisdomCard.author}")
                                    }
                                    context.startActivity(android.content.Intent.createChooser(shareIntent, "Compartir sabiduría"))
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Compartir sabiduría",
                                    tint = SoltarAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            IconButton(
                                onClick = {
                                    viewModel.rotateWisdomCard(uiState.preferredFramework)
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Rotar sabiduría",
                                    tint = SoltarAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = wisdomCard.quote,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "— ${wisdomCard.author}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.End)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = SoltarBorderSubtle)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = wisdomCard.reflection,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // --- BOTÓN SOS (SIEMPRE VISIBLE) ---
        item {
            Button(
                onClick = {
                    viewModel.playSound(SoltarSoundManager.SoundType.URGE_ALERT)
                    viewModel.openUrgeSheet()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("sos_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed)
            ) {
                Text("MODO IMPULSO / SOS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        // 2. HERO FEATURE: No-Contact Counter (Solo visible si no es REFUGIO)
        if (vulnerabilityMode != "REFUGIO") {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("no_contact_hero_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(SoltarBorder, SoltarAmber.copy(alpha = 0.6f), SoltarBorder)))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = SoltarAmber.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = milestoneTitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SoltarAmber,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                    val calendar = Calendar.getInstance().apply { timeInMillis = noContactStart }
                                    DatePickerDialog(
                                        context,
                                        { _, y, m, d ->
                                            val selectedCal = Calendar.getInstance().apply { set(y, m, d, 0, 0, 0) }
                                            viewModel.updateNoContactStartDate(selectedCal.timeInMillis)
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    ).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.EditCalendar, contentDescription = "Ajustar fecha", tint = TextSecondary, modifier = Modifier.size(18.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "DÍAS DE CONTACTO CERO",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary,
                            letterSpacing = 1.2.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        val lastRelapseTimestamp = remember(relapses) {
                            relapses.maxByOrNull { it.timestamp }?.timestamp ?: 0L
                        }
                        val daysSinceLastRelapse = if (lastRelapseTimestamp > 0L) {
                            ((currentTime - lastRelapseTimestamp).coerceAtLeast(0L)) / (24 * 3600 * 1000)
                        } else {
                            -1L
                        }

                        Text(
                            text = if (daysSinceLastRelapse >= 0L)
                                "$days días de contacto cero • $daysSinceLastRelapse días desde la última recaída"
                            else
                                "$days días de contacto cero (Sin recaídas registradas)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Counter Grid (Days, Hours, Min, Sec)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CounterUnit(value = "$days", label = "DÍAS", highlight = true)
                            Text(":", color = SoltarAmber, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            CounterUnit(value = String.format("%02d", hours), label = "HORAS")
                            Text(":", color = SoltarBorder, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            CounterUnit(value = String.format("%02d", minutes), label = "MIN")
                            Text(":", color = SoltarBorder, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            CounterUnit(value = String.format("%02d", seconds), label = "SEG")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress to next milestone
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Objetivo: $nextMilestoneDays días", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 11.sp)
                                Text("${(milestoneProgress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = SoltarAmber, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { milestoneProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = SoltarAmber,
                                trackColor = SoltarSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Actions (Solo registrar recaída, el botón SOS ya está fuera)
                        OutlinedButton(
                            onClick = {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                viewModel.toggleRelapseModal(true)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("relapse_modal_trigger_button"),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = UrgeAlertRed)
                        ) {
                            Text("Registrar recaída", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        // Quick Journal Access
        if (vulnerabilityMode != "REFUGIO") {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.openJournalModal()
                        }
                        .testTag("journal_quick_access_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SoltarAmber.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditNote,
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "DIARIO PERSONAL & MENTORÍA",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Registra tus pensamientos y recibe sabiduría reflexiva",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Semantic Bell & Calm Soundscapes (Pro Feature)
        if (vulnerabilityMode != "REFUGIO") {
            item {
                val entitlements = UserEntitlements.fromSettings(settings)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            if (entitlements.isPremium) {
                                showSemanticBellDialog = true
                            } else {
                                viewModel.openPaywall(SubscriptionPlan.MONTHLY)
                            }
                        }
                        .testTag("semantic_bell_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, if (entitlements.isPremium) SoltarAmber else SoltarBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SoltarAmber.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SelfImprovement,
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "CAMPANA & PAISAJES DE CALMA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SoltarAmber,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                if (!entitlements.isPremium) {
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = SoltarAmber.copy(alpha = 0.2f)
                                    ) {
                                        Text("PRO", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = SoltarAmber, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                }
                            }
                            Text(
                                text = "Regulación auditiva 528Hz y entornos sonoros inmersivos",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Icon(
                            imageVector = if (entitlements.isPremium) Icons.Default.ChevronRight else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (entitlements.isPremium) TextSecondary else SoltarAmber,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // 3. Core Question: "¿Cómo estás ahora?"
        if (vulnerabilityMode != "REFUGIO") {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("feeling_state_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "¿CÓMO ESTÁS EN ESTE MOMENTO?",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Elige tu estado para recibir la intervención precisa:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val feelings = listOf(
                        "🚨 Ansiedad / Ganas de escribir",
                        "💭 Mente en bucle",
                        "🥀 Nostalgia / Idealización",
                        "⚖️ Confusión / Culpabilidad",
                        "🌿 En calma / Reconstrucción"
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(feelings) { feeling ->
                            val isSelected = uiState.selectedFeeling == feeling
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                    viewModel.setSelectedFeeling(if (isSelected) "" else feeling)
                                },
                                label = { Text(feeling, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoltarAmber,
                                    selectedLabelColor = SoltarBackground,
                                    containerColor = SoltarSurfaceElevated,
                                    labelColor = TextPrimary
                                )
                            )
                        }
                    }

                    // Dynamic Intervention Card
                    AnimatedVisibility(
                        visible = uiState.selectedFeeling.isNotBlank(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Spacer(modifier = Modifier.height(14.dp))
                        when (uiState.selectedFeeling) {
                            "🚨 Ansiedad / Ganas de escribir" -> InterventionBanner(
                                title = "Protocolo de Urgencia Somática",
                                description = "Tu cuerpo tiene un pico de dopamina. No actúes ahora. Inicia el protocolo de 20 minutos.",
                                ctaText = "Abrir Modo Impulso (20 min)",
                                icon = Icons.Default.Bolt,
                                accentColor = UrgeAlertRed,
                                onClick = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.URGE_ALERT)
                                    viewModel.openUrgeSheet()
                                }
                            )
                            "💭 Mente en bucle" -> InterventionBanner(
                                title = "Desarmar Pensamientos Intrusivos",
                                description = "Separa hechos objetivos de interpretaciones catastróficas con el laboratorio TCC.",
                                ctaText = "Abrir Laboratorio de Pensamiento",
                                icon = Icons.Default.Psychology,
                                accentColor = SoltarAmber,
                                onClick = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                    viewModel.toggleThoughtModal(true)
                                }
                            )
                            "🥀 Nostalgia / Idealización" -> InterventionBanner(
                                title = "Antídoto de Realidad",
                                description = "Tu memoria borra lo malo y amplifica lo bueno. Revisa el contraste de realidad.",
                                ctaText = "Ver Antídoto de Idealización",
                                icon = Icons.Default.Visibility,
                                accentColor = SoltarTerracotta,
                                onClick = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                    viewModel.toggleIdealizationModal(true)
                                }
                            )
                            "⚖️ Confusión / Culpabilidad" -> InterventionBanner(
                                title = "Auditoría de 3 Responsabilidades",
                                description = "Ni toda la culpa es tuya, ni la otra persona es un monstruo. Claridad y ecuanimidad.",
                                ctaText = "Auditar la Relación",
                                icon = Icons.Default.Balance,
                                accentColor = SoltarSage,
                                onClick = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                    viewModel.toggleAuditModal(true)
                                }
                            )
                            "🌿 En calma / Reconstrucción" -> InterventionBanner(
                                title = "Reconectar con tu Autonomía",
                                description = "Aprovecha la serenidad para avanzar en tus metas de identidad y proyectos personales.",
                                ctaText = "Ver Metas de Identidad",
                                icon = Icons.Default.Flag,
                                accentColor = SoltarSage,
                                onClick = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                    viewModel.toggleIdentityGoalModal(true)
                                }
                            )
                        }
                    }
                }
            }
        }
        }

        // 4. THREE TOOL FAMILIES
        item {
            Text(
                text = "HERRAMIENTAS DE PRECISIÓN",
                style = MaterialTheme.typography.labelMedium,
                color = SoltarAmber,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }

        // Family A: Cuando quiero contactar
        item {
            ToolFamilyCard(
                familyBadge = "PROTECCIÓN DE DIGNIDAD",
                familyTitle = "Cuando quiero contactar",
                accentColor = UrgeAlertRed,
                tools = listOf(
                    ToolItem(
                        title = "Modo Impulso (20 min)",
                        subtitle = "Protocolo somático guiado en 6 fases",
                        icon = Icons.Default.Bolt,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.URGE_ALERT)
                            viewModel.openUrgeSheet()
                        }
                    ),
                    ToolItem(
                        title = "Antídoto de Idealización",
                        subtitle = "Contraste entre lo que extrañas y la realidad",
                        icon = Icons.Default.Visibility,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.toggleIdealizationModal(true)
                        }
                    ),
                    ToolItem(
                        title = "Carta Privada Sellada",
                        subtitle = "Escribe todo sin enviarlo y haz la ceremonia",
                        icon = Icons.Default.MailOutline,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.toggleLetterModal(true)
                        }
                    ),
                    ToolItem(
                        title = "Simulacro de Encuentro",
                        subtitle = "Practica conversaciones y límites",
                        icon = Icons.Default.People,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.toggleEncounterSimulator(true)
                        }
                    ),
                    ToolItem(
                        title = "Cápsula del Tiempo",
                        subtitle = "Carta al yo futuro con fecha de desbloqueo",
                        icon = Icons.Default.HourglassBottom,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.toggleTimeCapsuleModal(true)
                        }
                    )
                )
            )
        }

        // Family B: Cuando doy vueltas
        item {
            ToolFamilyCard(
                familyBadge = "REGULACIÓN COGNITIVA",
                familyTitle = "Cuando doy vueltas a la cabeza",
                accentColor = SoltarAmber,
                tools = listOf(
                    ToolItem(
                        title = "Laboratorio de Pensamientos",
                        subtitle = "Hechos vs Interpretaciones (TCC)",
                        icon = Icons.Default.Psychology,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.toggleThoughtModal(true)
                        }
                    ),
                    ToolItem(
                        title = "No quiero pensar más",
                        subtitle = "Anclaje sensorial 5-4-3-2-1 y respiración",
                        icon = Icons.Default.Air,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.CALM_BELL)
                            viewModel.openNoThinkingSheet()
                        }
                    ),
                    ToolItem(
                        title = "Auditoría de la Relación",
                        subtitle = "Ecuanimidad en 3 columnas de responsabilidad",
                        icon = Icons.Default.Balance,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.toggleAuditModal(true)
                        }
                    )
                )
            )
        }

        // Family C: Cuando estoy en calma
        item {
            ToolFamilyCard(
                familyBadge = "RECONSTRUCCIÓN PERSONAL",
                familyTitle = "Cuando estoy en calma",
                accentColor = SoltarSage,
                tools = listOf(
                    ToolItem(
                        title = "Diario Personal & Mentoría",
                        subtitle = "Escribe tus pensamientos y recibe guía estoica y clínica",
                        icon = Icons.Default.AutoAwesome,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.openJournalModal()
                        }
                    ),
                    ToolItem(
                        title = "Metas de Identidad y Valores",
                        subtitle = "Quién elijo ser hoy y mis límites",
                        icon = Icons.Default.Flag,
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.toggleIdentityGoalModal(true)
                        }
                    )
                )
            )
        }

        // 5. 3 Focos Diarios (Cuerpo, Proyecto, Red)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("three_focuses_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "LOS 3 FOCOS DEL DÍA",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Acciones simples para devolver la energía a tu propia vida:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FocusActionRow(
                        title = "1. Mi Cuerpo",
                        value = uiState.focusBodyInput,
                        onValueChange = { viewModel.setFocusBodyInput(it) },
                        placeholder = "Ej. Caminar 20 min sin mirar el móvil",
                        accent = SoltarAmber
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FocusActionRow(
                        title = "2. Mi Proyecto Propio",
                        value = uiState.focusSelfInput,
                        onValueChange = { viewModel.setFocusSelfInput(it) },
                        placeholder = "Ej. Avanzar 30 min en mi estudio o trabajo",
                        accent = SoltarSage
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FocusActionRow(
                        title = "3. Mi Red Social",
                        value = uiState.focusSocialInput,
                        onValueChange = { viewModel.setFocusSocialInput(it) },
                        placeholder = "Ej. Enviar un audio a un amigo de confianza",
                        accent = SoltarBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.saveTodayCheckin()
                        },
                        modifier = Modifier.fillMaxWidth().height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Text("Guardar Focos del Día", color = SoltarAmber, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Support Hub (B1, B2, B3, B5)
        item {
            com.example.ui.components.SupportHubComponent(viewModel = viewModel)
        }

        // 6. Termómetro Emocional Diario (Colapsable / Limpio)
        item {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
                    .testTag("thermometer_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isThermometerExpanded = !isThermometerExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TERMÓMETRO EMOCIONAL DIARIO",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = if (isThermometerExpanded) "Toca para plegar" else "Califica tus 5 dimensiones hoy",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        IconButton(onClick = { isThermometerExpanded = !isThermometerExpanded }) {
                            Icon(
                                imageVector = if (isThermometerExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = SoltarAmber
                            )
                        }
                    }

                    if (isThermometerExpanded) {
                        Spacer(modifier = Modifier.height(14.dp))
                        SliderMetricRow(label = "Dolor / Desamor", value = uiState.todayPain, onValueChange = { viewModel.setMetricPain(it) }, color = UrgeAlertRed)
                        SliderMetricRow(label = "Ansiedad", value = uiState.todayAnxiety, onValueChange = { viewModel.setMetricAnxiety(it) }, color = SoltarTerracotta)
                        SliderMetricRow(label = "Nostalgia", value = uiState.todayNostalgia, onValueChange = { viewModel.setMetricNostalgia(it) }, color = SoltarAmber)
                        SliderMetricRow(label = "Impulso de contactar", value = uiState.todayUrgeToContact, onValueChange = { viewModel.setMetricUrge(it) }, color = UrgeAlertRed)
                        SliderMetricRow(label = "Sensación de Autonomía", value = uiState.todayAutonomy, onValueChange = { viewModel.setMetricAutonomy(it) }, color = SoltarSage)

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.playSound(SoltarSoundManager.SoundType.CALM_BELL)
                                viewModel.saveTodayCheckin()
                            },
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                        ) {
                            Text("Guardar Evaluación Emocional", color = SoltarBackground, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showSemanticBellDialog) {
        SemanticBellAndSoundscapesDialog(
            viewModel = viewModel,
            onDismiss = { showSemanticBellDialog = false }
        )
    }
}

@Composable
private fun CounterUnit(
    value: String,
    label: String,
    highlight: Boolean = false
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (highlight) SoltarAmber.copy(alpha = 0.15f) else SoltarSurface,
            border = BorderStroke(1.dp, if (highlight) SoltarAmber else SoltarBorderSubtle)
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = if (highlight) SoltarAmber else TextPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun InterventionBanner(
    title: String,
    description: String,
    ctaText: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = accentColor.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                Text(title, style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = TextSecondary, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
            ) {
                Text(ctaText, color = SoltarBackground, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class ToolItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
private fun ToolFamilyCard(
    familyBadge: String,
    familyTitle: String,
    accentColor: Color,
    tools: List<ToolItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
        border = BorderStroke(1.dp, SoltarBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Text(
                    text = familyBadge,
                    style = MaterialTheme.typography.labelSmall,
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = familyTitle,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tools.forEach { tool ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { tool.onClick() },
                        shape = RoundedCornerShape(10.dp),
                        color = SoltarSurfaceElevated,
                        border = BorderStroke(1.dp, SoltarBorderSubtle)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = accentColor.copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(tool.icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(tool.title, style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Text(tool.subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                            }

                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FocusActionRow(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    accent: Color
) {
    Column {
        Text(title, style = MaterialTheme.typography.labelSmall, color = accent, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = TextMuted, fontSize = 12.sp) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accent,
                unfocusedBorderColor = SoltarBorderSubtle,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )
    }
}

@Composable
private fun SliderMetricRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    color: Color
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
            Text("${value.toInt()} / 10", style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..10f,
            steps = 9,
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color,
                inactiveTrackColor = SoltarBorder
            )
        )
    }
}
