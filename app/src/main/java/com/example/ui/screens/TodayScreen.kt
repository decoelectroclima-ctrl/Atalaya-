package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CheckinEntity
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

data class FeelingOption(
    val id: String,
    val emoji: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector
)

@Composable
fun TodayScreen(
    viewModel: SoltarViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val checkins by viewModel.checkins.collectAsState()

    val feelings = listOf(
        FeelingOption("urge", "⚡", "Tengo un impulso", "Ganas de escribirle, mirar redes o buscar contacto", Icons.Default.Bolt),
        FeelingOption("rumination", "🌀", "Rumiando en bucle", "Pensando qué hice mal o buscando respuestas", Icons.Default.Loop),
        FeelingOption("missing", "💔", "Extrañando mucho", "Sensación de vacío o nostalgia del pasado", Icons.Default.FavoriteBorder),
        FeelingOption("anxiety", "🌪️", "Angustia / Ansiedad", "Opresión en el pecho, saturación mental", Icons.Default.Air),
        FeelingOption("calm", "🌿", "Tranquilo / Reconstruyendo", "Espacio mental para enfocarme en mí", Icons.Default.SelfImprovement)
    )

    var isCheckinSlidersOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoltarBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
    ) {
        // Stoic Grounding Daily Principle Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("today_quote_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = SoltarAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "PRINCIPIO FUNDAMENTAL",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "«Puedes seguir queriendo a alguien y dejar de organizar tu vida alrededor de esa persona.»",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Esto duele. Lo entendemos. No necesitas resolver tu futuro hoy: solo necesitas cuidar tus acciones de las próximas 24 horas.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        // ¿Cómo estás ahora? Question & Interactive Selector
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("today_how_are_you_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "¿Cómo estás ahora mismo?",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Selecciona lo que sientes para recibir la intervención adecuada:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    feelings.forEach { feeling ->
                        val isSelected = uiState.selectedFeeling == feeling.id
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    if (isSelected) {
                                        viewModel.setSelectedFeeling("")
                                    } else {
                                        viewModel.setSelectedFeeling(feeling.id)
                                    }
                                }
                                .testTag("feeling_${feeling.id}"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) SoltarAmber.copy(alpha = 0.15f) else SoltarSurfaceElevated,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) SoltarAmber else SoltarBorderSubtle
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = feeling.emoji,
                                    fontSize = 20.sp
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = feeling.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = if (isSelected) SoltarAmberLight else TextPrimary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                                    )
                                    Text(
                                        text = feeling.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = if (isSelected) SoltarAmber else TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Contextual Intervention Card (Appears based on selection)
        item {
            AnimatedVisibility(visible = uiState.selectedFeeling.isNotBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("contextual_intervention_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = BorderStroke(1.5.dp, SoltarAmber)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Healing, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                            Text(
                                text = "INTERVENCIÓN SUGERIDA",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        when (uiState.selectedFeeling) {
                            "urge" -> {
                                Text(
                                    text = "Tu cerebro está buscando una dosis inmediata de alivio. Cualquier mensaje que envíes ahora vendrá de la desesperación, no de tu dignidad.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { viewModel.openUrgeSheet() },
                                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("urge_intervention_cta"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed)
                                ) {
                                    Icon(Icons.Default.Timer, contentDescription = null, tint = TextPrimary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Iniciar Protocolo de Contención (20 min)", fontWeight = FontWeight.Bold, color = TextPrimary)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { viewModel.toggleIdealizationModal(true) },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, SoltarBorder)
                                ) {
                                    Text("Recordar la realidad de la relación", color = TextSecondary, fontSize = 13.sp)
                                }
                            }

                            "rumination" -> {
                                Text(
                                    text = "Darle vueltas a lo que pasó no es reflexionar: es un intento inconsciente de controlar lo incontrolable. Vamos a separar hechos de historias.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { viewModel.toggleThoughtModal(true) },
                                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("rumination_thought_cta"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                                ) {
                                    Icon(Icons.Default.Psychology, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Desarmar Pensamiento (TCC)", fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { viewModel.openNoThinkingSheet() },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, SoltarBorder)
                                ) {
                                    Text("Anclar mis 5 sentidos al presente", color = TextSecondary, fontSize = 13.sp)
                                }
                            }

                            "missing" -> {
                                Text(
                                    text = "Extrañar no significa que debas volver. Extrañas la cercanía, la rutina y la versión idealizada, no la realidad que te lastimó.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { viewModel.toggleIdealizationModal(true) },
                                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("missing_idealization_cta"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                                ) {
                                    Icon(Icons.Default.Visibility, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Antídoto de Idealización", fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                OutlinedButton(
                                    onClick = { viewModel.toggleLetterModal(true) },
                                    modifier = Modifier.fillMaxWidth().height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, SoltarBorder)
                                ) {
                                    Text("Escribir carta para no enviar", color = TextSecondary, fontSize = 13.sp)
                                }
                            }

                            "anxiety" -> {
                                Text(
                                    text = "Tu sistema nervioso simpático está hiperactivado. No intentes solucionar tu vida con la mente en alerta: baja primero la activación física.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { viewModel.openNoThinkingSheet() },
                                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("anxiety_grounding_cta"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarSage, contentColor = SoltarBackground)
                                ) {
                                    Icon(Icons.Default.Air, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Respiración Cuadrada & 5-4-3-2-1", fontWeight = FontWeight.Bold)
                                }
                            }

                            "calm" -> {
                                Text(
                                    text = "Este espacio de claridad es oro. Úsalo para invertir en tu propia vida, tus proyectos y tu autonomía.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    lineHeight = 20.sp
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { viewModel.toggleIdentityGoalModal(true) },
                                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("calm_goal_cta"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarSage, contentColor = SoltarBackground)
                                ) {
                                    Icon(Icons.Default.Flag, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Crear Objetivo de Identidad", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Los 3 Focos de Hoy (Acción para el cuerpo, proyecto personal, conexión social)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("today_focus_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Checklist, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                        Text(
                            text = "MIS 3 FOCOS DE HOY",
                            style = MaterialTheme.typography.labelMedium,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Tres acciones no negociables para recuperar tu centro y tu energía:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 1. Cuerpo
                    OutlinedTextField(
                        value = uiState.focusBodyInput,
                        onValueChange = viewModel::setFocusBodyInput,
                        label = { Text("1. Para mi cuerpo y salud física", color = SoltarSage, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("focus_body_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarSage,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 2. Mi Proyecto / Vida
                    OutlinedTextField(
                        value = uiState.focusSelfInput,
                        onValueChange = viewModel::setFocusSelfInput,
                        label = { Text("2. Para mi vida / proyecto personal", color = SoltarAmber, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("focus_self_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. Conexión social
                    OutlinedTextField(
                        value = uiState.focusSocialInput,
                        onValueChange = viewModel::setFocusSocialInput,
                        label = { Text("3. Para mi entorno / red social", color = SoltarBlue, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("focus_social_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarBlue,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.saveTodayCheckin() },
                        modifier = Modifier.fillMaxWidth().height(44.dp).testTag("save_focus_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoltarSurfaceElevated, contentColor = SoltarAmber),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Text("Guardar Focos del Día", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        // Botón Conversar con Acompañante SOLTAR IA
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleAiCompanionSheet(true) }
                    .testTag("today_ai_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SoltarAmber.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = SoltarAmber,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hablar con SOLTAR (Acompañante IA)",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Diálogo sobrio, detección de bucles y contención sin juicios.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = SoltarAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Registro Rápido del Estado (Sliders colapsables)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("today_metrics_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isCheckinSlidersOpen = !isCheckinSlidersOpen },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Termómetro Emocional Diario",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Registra la intensidad para medir tu evolución",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        IconButton(onClick = { isCheckinSlidersOpen = !isCheckinSlidersOpen }) {
                            Icon(
                                imageVector = if (isCheckinSlidersOpen) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = SoltarAmber
                            )
                        }
                    }

                    AnimatedVisibility(visible = isCheckinSlidersOpen) {
                        Column(
                            modifier = Modifier.padding(top = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            MetricSliderRow("Dolor Emocional", uiState.todayPain, viewModel::setMetricPain, UrgeAlertRed)
                            MetricSliderRow("Ansiedad / Inquietud", uiState.todayAnxiety, viewModel::setMetricAnxiety, SoltarTerracotta)
                            MetricSliderRow("Nostalgia / Recuerdos", uiState.todayNostalgia, viewModel::setMetricNostalgia, SoltarAmber)
                            MetricSliderRow("Impulso de Contactar", uiState.todayUrgeToContact, viewModel::setMetricUrge, UrgeAlertRed)
                            MetricSliderRow("Sensación de Autonomía", uiState.todayAutonomy, viewModel::setMetricAutonomy, SoltarSage)

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = {
                                    viewModel.saveTodayCheckin()
                                    isCheckinSlidersOpen = false
                                },
                                modifier = Modifier.fillMaxWidth().height(44.dp).testTag("save_metrics_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                            ) {
                                Text("Guardar Registro Diario", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricSliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    color: androidx.compose.ui.graphics.Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.SemiBold)
            Text("${value.toInt()} / 10", style = MaterialTheme.typography.bodySmall, color = color, fontWeight = FontWeight.Bold)
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
