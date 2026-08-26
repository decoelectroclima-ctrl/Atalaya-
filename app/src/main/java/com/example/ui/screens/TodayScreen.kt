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
import com.example.data.WisdomBank
import com.example.ui.SoltarViewModel
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

    // Live clock ticker for No-Contact Counter
    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = System.currentTimeMillis()
        }
    }

    val noContactStart = settings?.breakupDateTimestamp ?: (currentTime - (14L * 24 * 3600 * 1000))
    val elapsedMillis = (currentTime - noContactStart).coerceAtLeast(0L)

    val totalSeconds = elapsedMillis / 1000
    val days = totalSeconds / (24 * 3600)
    val hours = (totalSeconds % (24 * 3600)) / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

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

    var isThermometerExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoltarBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp)
    ) {
        // 1. Dynamic Wisdom Card (Rotates per framework, with interactive refresh)
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

                        IconButton(
                            onClick = {
                                viewModel.rotateWisdomCard(uiState.preferredFramework)
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Rotar sabiduría",
                                tint = SoltarAmberLight,
                                modifier = Modifier.size(16.dp)
                            )
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

        // 2. HERO FEATURE: No-Contact Counter
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
                        text = "TIEMPO EN CONTACTO CERO",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.SemiBold
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

                    // Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                viewModel.toggleRelapseModal(true)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("relapse_modal_trigger_button"),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.5f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = UrgeAlertRed)
                        ) {
                            Text("Registrar recaída", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = {
                                viewModel.playSound(SoltarSoundManager.SoundType.URGE_ALERT)
                                viewModel.openUrgeSheet()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("quick_urge_button"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                        ) {
                            Text("Siento el impulso", color = SoltarBackground, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Quick Journal Access
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

        // 3. Core Question: "¿Cómo estás ahora?"
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
