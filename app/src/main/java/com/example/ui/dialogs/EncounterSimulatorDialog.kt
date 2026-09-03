package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.ai.EncounterTone
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun EncounterSimulatorDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    var selectedScenario by remember { mutableStateOf<String?>(null) }
    var selectedTone by remember { mutableStateOf(EncounterTone.COLD) }
    var chatMessages by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var userResponse by remember { mutableStateOf("") }
    var evaluationResult by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val settings by viewModel.settings.collectAsState()
    val exName = "Expareja"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .testTag("encounter_simulator_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = SoltarBackground,
            border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SoltarAmber.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Forum, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text(
                                text = "SIMULACRO DE ENCUENTRO",
                                style = MaterialTheme.typography.titleMedium,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (selectedScenario == null) "Entrenamiento de límites con IA On-Device" else "$selectedScenario • ${selectedTone.label}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (selectedScenario == null) {
                    // CONFIGURATION VIEW (Scenario & Tone)
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                                border = BorderStroke(1.dp, SoltarBorder)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "ENTRENA TUS LÍMITES SIN RIESGO REAL",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "El modelo de IA on-device interpreta el rol de tu expareja con el tono y patrón de conducta que tú elijas. Practica mantener tu compostura y sostener tus límites sanos.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }

                        item {
                            Text(
                                text = "1. Selecciona el patrón de conducta de $exName:",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(EncounterTone.entries) { tone ->
                            val isSelected = selectedTone == tone
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedTone = tone },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) SoltarSurfaceElevated else SoltarSurface
                                ),
                                border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, if (isSelected) SoltarAmber else SoltarBorderSubtle)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { selectedTone = tone },
                                        colors = RadioButtonDefaults.colors(selectedColor = SoltarAmber, unselectedColor = SoltarBorder)
                                    )
                                    Column {
                                        Text(
                                            text = tone.label,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = if (isSelected) SoltarAmber else TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = tone.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "2. Elige el escenario a simular:",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        val scenarios = listOf(
                            "Encuentro casual inesperado en la calle",
                            "Mensaje de tanteo o nostalgia («¿Cómo estás?»)",
                            "Devolución de pertenencias o llaves",
                            "Trámite práctico o logístico indispensable",
                            "Poner límites ante un reproche o invasión"
                        )

                        items(scenarios) { scenario ->
                            OutlinedButton(
                                onClick = {
                                    selectedScenario = scenario
                                    val opening = when (selectedTone) {
                                        EncounterTone.COLD -> "Hola. Me dijeron que necesitabas hablar de esto."
                                        EncounterTone.VICTIM -> "Supongo que ahora sí tienes tiempo para mí..."
                                        EncounterTone.CHARMING -> "¡Hola! Qué casualidad encontrarte por aquí, te ves bien."
                                        EncounterTone.HOSTILE -> "¿Y ahora qué quieres? No tengo todo el día."
                                        EncounterTone.INDIFFERENT -> "Hola. Dime rápido lo que sea."
                                    }
                                    chatMessages = listOf(Pair(exName, opening))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(scenario, color = TextPrimary, fontSize = 13.sp)
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                } else {
                    // CHAT SIMULATION VIEW
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SoltarSurface, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎭 $exName (${selectedTone.label})",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (chatMessages.any { it.first == "Tú" }) {
                                    TextButton(
                                        onClick = {
                                            evaluationResult = com.example.ai.OnDeviceLlmEngine.evaluateEncounterUserBoundaries(chatMessages)
                                        },
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Evaluar (IA)", color = SoltarAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                TextButton(onClick = {
                                    selectedScenario = null
                                    chatMessages = emptyList()
                                    evaluationResult = null
                                }) {
                                    Text("Cambiar", color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(chatMessages) { (sender, msg) ->
                                val isUser = sender == "Tú"
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                                ) {
                                    Text(
                                        text = sender,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isUser) SoltarAmber else TextSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(
                                            topStart = 14.dp,
                                            topEnd = 14.dp,
                                            bottomStart = if (isUser) 14.dp else 2.dp,
                                            bottomEnd = if (isUser) 2.dp else 14.dp
                                        ),
                                        color = if (isUser) SoltarAmber.copy(alpha = 0.2f) else SoltarSurfaceElevated,
                                        border = BorderStroke(1.dp, if (isUser) SoltarAmber else SoltarBorder)
                                    ) {
                                        Text(
                                            text = msg,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextPrimary,
                                            modifier = Modifier.padding(12.dp),
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            if (isLoading) {
                                item {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = SoltarAmber)
                                        Text("Generando respuesta on-device...", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = userResponse,
                                onValueChange = { userResponse = it },
                                placeholder = { Text("Escribe tu respuesta manteniendo límites...", color = TextMuted, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SoltarAmber,
                                    unfocusedBorderColor = SoltarBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                maxLines = 3
                            )

                            Button(
                                onClick = {
                                    val msg = userResponse.trim()
                                    if (msg.isNotBlank()) {
                                        userResponse = ""
                                        scope.launch {
                                            isLoading = true
                                            val updatedHistory = chatMessages + ("Tú" to msg)
                                            chatMessages = updatedHistory
                                            listState.animateScrollToItem(updatedHistory.size - 1)

                                            val response = viewModel.sendEncounterMessage(
                                                message = msg,
                                                history = updatedHistory,
                                                scenario = selectedScenario!!,
                                                tone = selectedTone
                                            )
                                            chatMessages = updatedHistory + Pair(exName, response.replyText)
                                            isLoading = false
                                            listState.animateScrollToItem(chatMessages.size - 1)
                                        }
                                    }
                                },
                                enabled = userResponse.isNotBlank() && !isLoading,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                                modifier = Modifier.height(52.dp)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Enviar", tint = SoltarBackground)
                            }
                        }
                    }
                }
            }
        }

        evaluationResult?.let { eval ->
            AlertDialog(
                onDismissRequest = { evaluationResult = null },
                confirmButton = {
                    TextButton(onClick = { evaluationResult = null }) {
                        Text("Continuar", color = SoltarAmber, fontWeight = FontWeight.Bold)
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber)
                        Text("Evaluación de Límites", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Text(
                        text = eval,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        lineHeight = 20.sp
                    )
                },
                containerColor = SoltarSurfaceElevated,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}
