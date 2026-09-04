package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UrgeModeDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SoltarBackground)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header & Phase Indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Text(
                        text = "MODO IMPULSO • FASE ${uiState.urgePhase}/6",
                        style = MaterialTheme.typography.labelMedium,
                        color = UrgeAlertRed,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )

                    TextButton(onClick = { viewModel.completeAndSaveUrgeEpisode() }) {
                        Text("Guardar", color = SoltarAmber, fontWeight = FontWeight.Bold)
                    }
                }

                // Progress Bar
                LinearProgressIndicator(
                    progress = { uiState.urgePhase / 6f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = UrgeAlertRed,
                    trackColor = SoltarBorder
                )

                Spacer(modifier = Modifier.height(20.dp))

                when (uiState.urgePhase) {
                    1 -> PhaseOneTimer(viewModel = viewModel)
                    2 -> PhaseTwoEmotions(viewModel = viewModel)
                    3 -> PhaseThreeAction(viewModel = viewModel)
                    4 -> PhaseFourExpectation(viewModel = viewModel)
                    5 -> PhaseFiveReality(viewModel = viewModel)
                    6 -> PhaseSixClosure(viewModel = viewModel)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Phase Navigation Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (uiState.urgePhase > 1) {
                        OutlinedButton(
                            onClick = { viewModel.prevUrgePhase() },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
                        ) {
                            Text("Anterior", color = TextPrimary)
                        }
                    }

                    if (uiState.urgePhase < 6) {
                        Button(
                            onClick = { viewModel.nextUrgePhase() },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("urge_next_phase_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed)
                        ) {
                            Text("Siguiente Fase", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.completeAndSaveUrgeEpisode() },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("urge_finish_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarSage)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SoltarBackground)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Registrar y Salir", color = SoltarBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun PhaseOneTimer(viewModel: SoltarViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val minutes = uiState.urgeTimerSecondsRemaining / 60
    val seconds = uiState.urgeTimerSecondsRemaining % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(UrgeAlertBackground)
                .border(2.dp, UrgeAlertRed, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PanTool,
                contentDescription = null,
                tint = UrgeAlertRed,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "PARA.",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "«El impulso no es una orden.»",
            style = MaterialTheme.typography.titleMedium,
            color = SoltarAmber,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tu cerebro está experimentando un pico de dopamina y cortisol. El impulso tiene una curva fisiológica: sube, alcanza una cresta y desciende en unos 20 minutos si no lo alimentas.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Large 20 min Timer Display
        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .background(SoltarSurfaceElevated)
                .border(3.dp, if (uiState.isUrgeTimerRunning) UrgeAlertRed else SoltarBorder, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.displayMedium,
                    color = if (uiState.isUrgeTimerRunning) TextPrimary else TextMuted,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (uiState.isUrgeTimerRunning) "En curso (20 min)" else "Pausado",
                    style = MaterialTheme.typography.labelSmall,
                    color = SoltarAmber
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Intelligent Somatic Prescription Card (Powered by On-Device AI / Clinical Engine)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = SoltarSurfaceElevated,
            border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                    Text(
                        text = "Protocolo Somático Inteligente (IA)",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                val intensity = uiState.urgeInitialIntensity
                val somaticTip = when {
                    intensity >= 8 -> "Intensidad crítica detectada ($intensity/10). Prescripción: Respiración Fisiológica (doble inhalación profunda por la nariz, exhalación larga por la boca) durante 60 segundos para activar el nervio vago y desacelerar el ritmo cardíaco. Anclaje físico: agua fría en las muñecas."
                    intensity >= 5 -> "Intensidad moderada ($intensity/10). Prescripción: Respiración cuadrada 4-4-4-4. Identifica dónde se localiza la tensión corporal (pecho, garganta, estómago) y respira directamente hacia esa zona."
                    else -> "Intensidad leve ($intensity/10). Prescripción: Observación compasiva sin juicio. Deja que el impulso cruce tu mente como una nube en el horizonte sin engancharte a su narrativa."
                }
                Text(
                    text = somaticTip,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Timer Controls
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { viewModel.toggleUrgeTimer() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (uiState.isUrgeTimerRunning) SoltarSurfaceHighlight else UrgeAlertRed
                )
            ) {
                Icon(
                    imageVector = if (uiState.isUrgeTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = TextPrimary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (uiState.isUrgeTimerRunning) "Pausar" else "Reanudar")
            }
        }

        // Red Flags Reminder during Urge / SOS
        val redFlags by viewModel.redFlags.collectAsState()
        if (redFlags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = UrgeAlertBackground),
                border = androidx.compose.foundation.BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Flag, contentDescription = null, tint = UrgeAlertRed, modifier = Modifier.size(18.dp))
                        Text("Tus Red Flags (Recuerda por qué saliste)", style = MaterialTheme.typography.titleSmall, color = UrgeAlertRed, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    redFlags.take(4).forEach { flag ->
                        Text(
                            text = "• ${flag.reason}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Support Network Quick Access
        val settings by viewModel.settings.collectAsState()
        val context = LocalContext.current
        val hasSupportContact = !settings?.contact1Name.isNullOrBlank() || !settings?.contact2Name.isNullOrBlank() || !settings?.contact3Name.isNullOrBlank()

        if (hasSupportContact) {
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SoltarSage.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Groups, contentDescription = null, tint = SoltarSage, modifier = Modifier.size(18.dp))
                        Text("Acude a tu Red de Apoyo", style = MaterialTheme.typography.titleSmall, color = SoltarSage, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Llama o escribe a una persona de confianza antes de actuar:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val contacts = listOfNotNull(
                        if (!settings?.contact1Name.isNullOrBlank()) Triple(settings?.contact1Name!!, settings?.contact1Phone ?: "", settings?.contact1Relationship ?: "") else null,
                        if (!settings?.contact2Name.isNullOrBlank()) Triple(settings?.contact2Name!!, settings?.contact2Phone ?: "", settings?.contact2Relationship ?: "") else null,
                        if (!settings?.contact3Name.isNullOrBlank()) Triple(settings?.contact3Name!!, settings?.contact3Phone ?: "", settings?.contact3Relationship ?: "") else null
                    )

                    contacts.forEach { (name, phone, rel) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(name, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                                if (rel.isNotBlank()) {
                                    Text(rel, style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 10.sp)
                                }
                            }
                            if (phone.isNotBlank()) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    IconButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Phone, contentDescription = "Llamar", tint = SoltarSage, modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(
                                        onClick = {
                                            val clean = phone.replace("+", "").replace(" ", "").replace("-", "").trim()
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$clean"))
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "WhatsApp", tint = SoltarAmber, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhaseTwoEmotions(viewModel: SoltarViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val emotions = listOf(
        "Nostalgia", "Ansiedad", "Soledad", "Miedo", "Rabia",
        "Necesidad de respuesta", "Esperanza", "Deseo", "Tristeza", "Culpa", "Otro"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "FASE 2: ¿Qué estás sintiendo ahora mismo?",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ponle nombre a la emoción sin juzgarla. Nombrar la emoción reduce la reactividad de la amígdala.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(20.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            emotions.forEach { emotion ->
                val isSelected = uiState.urgeEmotion.equals(emotion, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setUrgeEmotion(emotion) },
                    label = { Text(emotion, color = if (isSelected) SoltarBackground else TextPrimary) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SoltarAmber,
                        containerColor = SoltarSurfaceElevated
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhaseThreeAction(viewModel: SoltarViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val actions = listOf(
        "Escribir un mensaje", "Llamar", "Mirar sus redes sociales",
        "Buscar información", "Comprobar su conexión/estado", "Preguntarle a un conocido", "Otra conducta"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "FASE 3: ¿Qué quieres hacer?",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Identifica la conducta compulsiva o de comprobación que tu cerebro quiere ejecutar.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(20.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            actions.forEach { act ->
                val isSelected = uiState.urgeDesiredAction.equals(act, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setUrgeDesiredAction(act) },
                    label = { Text(act, color = if (isSelected) SoltarBackground else TextPrimary) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SoltarTerracotta,
                        containerColor = SoltarSurfaceElevated
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhaseFourExpectation(viewModel: SoltarViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val expectations = listOf(
        "Alivio momentáneo", "Una respuesta tranquilizadora", "Saber qué siente",
        "Reconciliación", "Sentirme importante", "Reducir la incertidumbre",
        "Comprobar si todavía le importo"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "FASE 4: ¿Qué esperas conseguir?",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "¿Qué fantasía o resultado le promete tu mente a este impulso?",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(20.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            expectations.forEach { exp ->
                val isSelected = uiState.urgeExpectedOutcome.equals(exp, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.setUrgeExpectedOutcome(exp) },
                    label = { Text(exp, color = if (isSelected) SoltarBackground else TextPrimary) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = SoltarSage,
                        containerColor = SoltarSurfaceElevated
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }
    }
}

@Composable
fun PhaseFiveReality(viewModel: SoltarViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "FASE 5: REALIDAD",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Separa con rigor lo que es un hecho de las hipótesis y juicios que tu mente inventa.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(16.dp))

        UrgeTextField(
            label = "¿Qué hecho tienes?",
            value = uiState.urgeFact,
            onValueChange = { viewModel.setUrgeFact(it) },
            placeholder = "Ej. No hablamos desde hace 10 días."
        )

        Spacer(modifier = Modifier.height(12.dp))

        UrgeTextField(
            label = "¿Qué estás interpretando?",
            value = uiState.urgeInterpretation,
            onValueChange = { viewModel.setUrgeInterpretation(it) },
            placeholder = "Ej. Que ya me olvidó y que nunca le importé."
        )

        Spacer(modifier = Modifier.height(12.dp))

        UrgeTextField(
            label = "¿Qué NO puedes saber?",
            value = uiState.urgeCannotKnow,
            onValueChange = { viewModel.setUrgeCannotKnow(it) },
            placeholder = "Ej. Qué piensa, qué siente o qué hará mañana."
        )

        Spacer(modifier = Modifier.height(12.dp))

        UrgeTextField(
            label = "¿Qué depende de ti?",
            value = uiState.urgeDependsOnMe,
            onValueChange = { viewModel.setUrgeDependsOnMe(it) },
            placeholder = "Ej. Cuidar mi dignidad, respirar y continuar con mi tarde."
        )
    }
}

@Composable
fun PhaseSixClosure(viewModel: SoltarViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.VerifiedUser,
            contentDescription = null,
            tint = SoltarSage,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "FASE 6: CIERRE DEL IMPULSO",
            style = MaterialTheme.typography.titleMedium,
            color = SoltarSage,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "«Puedes sentir el impulso sin obedecerlo.»",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
            border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Registro de Intensidad",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Intensidad Inicial:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text(text = "${uiState.urgeInitialIntensity}/10", color = UrgeAlertRed, fontWeight = FontWeight.Bold)
                }
                Slider(
                    value = uiState.urgeInitialIntensity.toFloat(),
                    onValueChange = { viewModel.setUrgeInitialIntensity(it.toInt()) },
                    valueRange = 0f..10f,
                    steps = 9,
                    colors = SliderDefaults.colors(thumbColor = UrgeAlertRed, activeTrackColor = UrgeAlertRed)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Intensidad Final (Ahora):", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text(text = "${uiState.urgeFinalIntensity}/10", color = SoltarSage, fontWeight = FontWeight.Bold)
                }
                Slider(
                    value = uiState.urgeFinalIntensity.toFloat(),
                    onValueChange = { viewModel.setUrgeFinalIntensity(it.toInt()) },
                    valueRange = 0f..10f,
                    steps = 9,
                    colors = SliderDefaults.colors(thumbColor = SoltarSage, activeTrackColor = SoltarSage)
                )

                Spacer(modifier = Modifier.height(10.dp))

                UrgeTextField(
                    label = "Detonante (¿Qué disparó este impulso?)",
                    value = uiState.urgeTrigger,
                    onValueChange = { viewModel.setUrgeTrigger(it) },
                    placeholder = "Ej. Domingo por la noche, soledad, escuchar una canción"
                )

                Spacer(modifier = Modifier.height(10.dp))

                UrgeTextField(
                    label = "Aprendizaje de este episodio",
                    value = uiState.urgeLearning,
                    onValueChange = { viewModel.setUrgeLearning(it) },
                    placeholder = "Ej. El impulso pasó en 15 min sin necesidad de romper mi tranquilidad."
                )
            }
        }
    }
}

@Composable
fun UrgeTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = SoltarAmber, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextMuted, fontSize = 13.sp) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SoltarAmber,
                unfocusedBorderColor = SoltarBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedContainerColor = SoltarSurface,
                unfocusedContainerColor = SoltarSurface
            ),
            shape = RoundedCornerShape(10.dp)
        )
    }
}
