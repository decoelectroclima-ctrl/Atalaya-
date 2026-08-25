package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun ThoughtLabDialog(
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
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("thought_dialog_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Text(
                        text = "DESARMAR RUMIACIÓN",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    TextButton(
                        onClick = {
                            viewModel.saveThoughtAndCloseLoop()
                            onDismiss()
                        },
                        enabled = uiState.thoughtOriginalInput.isNotBlank(),
                        modifier = Modifier.testTag("thought_save_button")
                    ) {
                        Text(
                            "Cerrar Bucle",
                            color = if (uiState.thoughtOriginalInput.isNotBlank()) SoltarAmber else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                            Text("Reestructuración Cognitiva (TCC)", color = SoltarAmber, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Un pensamiento no es un hecho. Escribe la historia que te estás contando para separarla de la realidad objetiva.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // 1. Pensamiento Intrusivo / Bucle
                Column {
                    Text(
                        text = "1. ¿Qué pensamiento o historia se está repitiendo?",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = uiState.thoughtOriginalInput,
                        onValueChange = viewModel::setThoughtOriginal,
                        placeholder = { Text("Ej: 'Nunca volveré a conectar con nadie igual' o 'Si hubiera dicho otra cosa estaríamos juntos'", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("thought_input_original"),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // 2. Hecho Objetivo vs Interpretación
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("2. El Hecho (Neutro)", style = MaterialTheme.typography.labelMedium, color = SoltarSage, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = uiState.thoughtFactInput,
                            onValueChange = viewModel::setThoughtFact,
                            placeholder = { Text("Ej: Terminamos hace 2 semanas", color = TextMuted, fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("thought_input_fact"),
                            minLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarSage,
                                unfocusedBorderColor = SoltarBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("3. Mi Interpretación", style = MaterialTheme.typography.labelMedium, color = SoltarTerracotta, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = uiState.thoughtInterpretationInput,
                            onValueChange = viewModel::setThoughtInterpretation,
                            placeholder = { Text("Ej: Que yo no fui suficiente", color = TextMuted, fontSize = 12.sp) },
                            modifier = Modifier.fillMaxWidth().testTag("thought_input_interpretation"),
                            minLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarTerracotta,
                                unfocusedBorderColor = SoltarBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                // 4. Lo que es imposible que yo sepa (Límites epistémicos)
                Column {
                    Text(
                        text = "4. Lo que es IMPOSIBLE que yo sepa con certeza",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.thoughtCannotKnowInput,
                        onValueChange = viewModel::setThoughtCannotKnow,
                        placeholder = { Text("Ej: Lo que esa persona siente hoy, o qué hubiera pasado en un futuro hipotético", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("thought_input_cannot_know"),
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // 5. Lo que SÍ depende de mí hoy
                Column {
                    Text(
                        text = "5. Lo que SÍ está bajo mi control en este momento",
                        style = MaterialTheme.typography.labelMedium,
                        color = SoltarSage,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.thoughtDependsOnMeInput,
                        onValueChange = viewModel::setThoughtDependsOnMe,
                        placeholder = { Text("Ej: Comer bien, no mandar mensajes que me humillen, dormir temprano", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("thought_input_depends_on_me"),
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarSage,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // 6. Acción concreta para salir de la mente y entrar al cuerpo
                Column {
                    Text(
                        text = "6. Acción concreta inmediata (10-15 minutos)",
                        style = MaterialTheme.typography.labelMedium,
                        color = SoltarAmber,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.thoughtConcreteActionInput,
                        onValueChange = viewModel::setThoughtAction,
                        placeholder = { Text("Ej: Salir a caminar sin teléfono, ducharme con agua tibia, ordenar mi escritorio", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("thought_input_action"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Button(
                    onClick = {
                        viewModel.saveThoughtAndCloseLoop()
                        onDismiss()
                    },
                    enabled = uiState.thoughtOriginalInput.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("thought_submit_cta"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                ) {
                    Text("Cerrar bucle y anclar a la realidad", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
