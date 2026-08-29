package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.SoltarSoundManager
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RelapseDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("relapse_dialog_close")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Text(
                        text = "REGISTRO DE RECAÍDA SIN JUICIO",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.size(48.dp))
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(SoltarBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Compassionate Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = SoltarSage, modifier = Modifier.size(28.dp))
                        Column {
                            Text(
                                text = "Una recaída no es el fin del camino",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "El dolor y la adicción dopaminérgica son intensos. No te juzgues. Convertir este tropiezo en información consciente es lo que protege tu futuro.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // Date and Perspective Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Fecha y Perspectiva del Suceso",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("es", "ES")) }
                        val currentDateStr = dateFormat.format(Date(uiState.relapseTimestamp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Cuándo ocurrió:", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text(text = currentDateStr, style = MaterialTheme.typography.bodyMedium, color = SoltarAmber, fontWeight = FontWeight.Bold)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { viewModel.setRelapseTimestamp(System.currentTimeMillis()) },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("Ahora", fontSize = 11.sp)
                                }
                                OutlinedButton(
                                    onClick = { viewModel.setRelapseTimestamp(System.currentTimeMillis() - 24L * 3600 * 1000) },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("Ayer", fontSize = 11.sp)
                                }
                            }
                        }

                        HorizontalDivider(color = SoltarBorderSubtle)

                        Text(
                            text = "¿Cómo sentiste esta recaída en tu proceso?",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = !uiState.relapseIsRestarting,
                                onClick = { viewModel.setRelapseIsRestarting(false) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.clickable { viewModel.setRelapseIsRestarting(false) }) {
                                Text(text = "Tropiezo consciente (Reafirma evolución)", style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                                Text(text = "No reinicia el contador. Es un punto a tener en cuenta para aprender.", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = uiState.relapseIsRestarting,
                                onClick = { viewModel.setRelapseIsRestarting(true) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.clickable { viewModel.setRelapseIsRestarting(true) }) {
                                Text(text = "Siento que volví a cero (Reiniciar contador)", style = MaterialTheme.typography.bodySmall, color = SoltarAmber, fontWeight = FontWeight.Bold)
                                Text(text = "Reiniciar la fecha de inicio del contacto cero.", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            }
                        }
                    }
                }

                // 1. ¿Qué ocurrió?
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "1. ¿Qué ocurrió objetivamente?",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Describe el hecho sin insultarte ni exagerar (ej. «Le envié un mensaje de texto preguntando si estaba bien»).",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.relapseWhatHappenedInput,
                            onValueChange = { viewModel.setRelapseWhatHappened(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("relapse_what_happened_input"),
                            placeholder = { Text("Escribe lo sucedido con objetividad...", color = TextMuted) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            minLines = 2
                        )
                    }
                }

                // 2. Detonante & Emoción
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "2. Detonante y Estado Emocional",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "¿Cuál fue el detonante? (ej. noche solitaria, alcohol, ver un recuerdo)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        OutlinedTextField(
                            value = uiState.relapseTriggerInput,
                            onValueChange = { viewModel.setRelapseTrigger(it) },
                            modifier = Modifier.fillMaxWidth().testTag("relapse_trigger_input"),
                            placeholder = { Text("Ej. Salí de fiesta y al volver a casa me sentí solo...", color = TextMuted) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Text(
                            text = "¿Qué emoción sentías en ese momento exacto?",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        OutlinedTextField(
                            value = uiState.relapseEmotionInput,
                            onValueChange = { viewModel.setRelapseEmotion(it) },
                            modifier = Modifier.fillMaxWidth().testTag("relapse_emotion_input"),
                            placeholder = { Text("Ej. Angustia profunda, vacío en el pecho, nostalgia...", color = TextMuted) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                // 3. Pensamiento trampa & Consecuencia
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "3. El Engaño de la Mente vs Consecuencia Real",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "¿Qué te dijo tu mente para justificar romper el contacto?",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        OutlinedTextField(
                            value = uiState.relapseThoughtInput,
                            onValueChange = { viewModel.setRelapseThought(it) },
                            modifier = Modifier.fillMaxWidth().testTag("relapse_thought_input"),
                            placeholder = { Text("Ej. «Un mensaje inocente no cambiará nada, necesito saber si aún me recuerda»", color = TextMuted) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Text(
                            text = "¿Cuál fue la consecuencia real y cómo te sentiste después?",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        OutlinedTextField(
                            value = uiState.relapseConsequenceInput,
                            onValueChange = { viewModel.setRelapseConsequence(it) },
                            modifier = Modifier.fillMaxWidth().testTag("relapse_consequence_input"),
                            placeholder = { Text("Ej. Respuesta cortante a las 8 horas. Sentí un bajón de autoestima y más ansiedad.", color = TextMuted) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                // 4. Blindaje y Aprendizaje
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "4. Aprendizaje para blindarte la próxima vez",
                            style = MaterialTheme.typography.titleSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "¿Qué barrera concreta vas a poner para cuando aparezca ese mismo detonante?",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.relapseLearningInput,
                            onValueChange = { viewModel.setRelapseLearning(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("relapse_learning_input"),
                            placeholder = { Text("Ej. Dejar el móvil fuera de mi habitación a partir de las 23:00 y abrir el Modo Impulso.", color = TextMuted) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorderSubtle,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            minLines = 2
                        )
                    }
                }

                // CTA Button
                Button(
                    onClick = {
                        viewModel.saveRelapseLog()
                        onDismiss()
                    },
                    enabled = uiState.relapseWhatHappenedInput.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("relapse_save_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = SoltarBackground)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Guardar aprendizaje y reiniciar con compasión",
                        color = SoltarBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
