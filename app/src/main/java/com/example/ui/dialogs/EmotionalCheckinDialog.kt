package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.SoltarSoundManager
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionalCheckinDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val emotionalStates = listOf("🌟 Muy bien", "🙂 Bien", "😐 Neutral", "🌧️ Mal", "⛈️ Muy mal")
    val predominantEmotions = listOf("Nostalgia", "Ansiedad", "Rabia", "Calma", "Tristeza", "Soledad", "Esperanza")
    val comparisonOptions = listOf("📈 Mejor", "➡️ Igual", "📉 Peor")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .testTag("emotional_checkin_dialog"),
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Recuerda • CHECK-IN EMOCIONAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = "Seguimiento Breve (30–60s)",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                onDismiss()
                            },
                            modifier = Modifier.testTag("checkin_close_button")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = SoltarSurfaceElevated)
                )
            },
            containerColor = SoltarBackground
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                // Header Prompt Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Psychology, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(22.dp))
                                Text(
                                    text = "¿Cómo te encuentras hoy?",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tus respuestas adaptan las recomendaciones de Recuerda y alimentan tu evolución personal sin juicios.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Question 1: Emotional State
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "1. ¿Cómo te encuentras hoy emocionalmente?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(emotionalStates) { state ->
                                val isSelected = uiState.checkinStateInput == state
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                        viewModel.updateCheckinState(state)
                                    },
                                    label = { Text(state, fontSize = 13.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = SoltarSurface,
                                        selectedContainerColor = SoltarAmber,
                                        labelColor = TextSecondary,
                                        selectedLabelColor = SoltarBackground
                                    )
                                )
                            }
                        }
                    }
                }

                // Question 2: First Thoughts
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "2. ¿Cuáles fueron tus primeros pensamientos al despertar?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        OutlinedTextField(
                            value = uiState.checkinFirstThoughtsInput,
                            onValueChange = { viewModel.updateCheckinFirstThoughts(it) },
                            placeholder = { Text("Ej. Acordarme de un mensaje, sensación de vacío...", color = TextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkin_first_thoughts_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }
                }

                // Question 3: Urge to Contact
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = "3. Impulso de contactar hoy (0 a 10):",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${uiState.checkinUrgeInput.toInt()}/10",
                                style = MaterialTheme.typography.bodyLarge,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Slider(
                            value = uiState.checkinUrgeInput,
                            onValueChange = { viewModel.updateCheckinUrge(it) },
                            valueRange = 0f..10f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = SoltarAmber,
                                activeTrackColor = SoltarAmber,
                                inactiveTrackColor = SoltarBorder
                            ),
                            modifier = Modifier.testTag("checkin_urge_slider")
                        )
                    }
                }

                // Question 4: Predominant Emotion
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "4. ¿Qué emoción ha predominado hoy?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(predominantEmotions) { emotion ->
                                val isSelected = uiState.checkinPredominantEmotionInput == emotion
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                        viewModel.updateCheckinPredominantEmotion(emotion)
                                    },
                                    label = { Text(emotion, fontSize = 13.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = SoltarSurface,
                                        selectedContainerColor = SoltarAmber,
                                        labelColor = TextSecondary,
                                        selectedLabelColor = SoltarBackground
                                    )
                                )
                            }
                        }
                    }
                }

                // Question 5: Trigger / Detonante
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "5. ¿Ha ocurrido algo que haya activado tu malestar?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        OutlinedTextField(
                            value = uiState.checkinTriggerInput,
                            onValueChange = { viewModel.updateCheckinTrigger(it) },
                            placeholder = { Text("Ej. Ver una foto, redes sociales, recuerdo nocturno...", color = TextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkin_trigger_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }
                }

                // Question 6: Comparison with Yesterday
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "6. ¿Notas que estás mejor, igual o peor que ayer?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(comparisonOptions) { comp ->
                                val isSelected = uiState.checkinComparisonInput == comp
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                        viewModel.updateCheckinComparison(comp)
                                    },
                                    label = { Text(comp, fontSize = 13.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = SoltarSurface,
                                        selectedContainerColor = SoltarAmber,
                                        labelColor = TextSecondary,
                                        selectedLabelColor = SoltarBackground
                                    )
                                )
                            }
                        }
                    }
                }

                // Question 7: Optional Free Note
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "7. Notas u observaciones libres (Opcional)",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        OutlinedTextField(
                            value = uiState.checkinFreeNoteInput,
                            onValueChange = { viewModel.updateCheckinFreeNote(it) },
                            placeholder = { Text("Escribe cualquier reflexión personal...", color = TextMuted) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkin_freenote_input"),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }
                }

                // Submit Button
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.WARM_CHIME)
                            viewModel.saveEmotionalCheckin()
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("save_checkin_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                    ) {
                        Text(
                            text = "Guardar Check-in Rápido",
                            color = SoltarBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}
