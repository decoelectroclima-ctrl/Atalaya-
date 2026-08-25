package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Flag
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
fun IdentityGoalDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val areas = listOf("Cuerpo y Salud", "Proyectos y Trabajo", "Amistades y Red", "Mente y Espacio Propio")
    val frequencies = listOf("Diario", "3 veces por semana", "Semanal")

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
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("goal_dialog_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Text(
                        text = "OBJETIVO DE IDENTIDAD",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    TextButton(
                        onClick = {
                            viewModel.saveIdentityGoal()
                            onDismiss()
                        },
                        enabled = uiState.newGoalTitleInput.isNotBlank(),
                        modifier = Modifier.testTag("goal_save_button")
                    ) {
                        Text(
                            "Guardar",
                            color = if (uiState.newGoalTitleInput.isNotBlank()) SoltarAmber else TextMuted,
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
                            Icon(Icons.Default.Flag, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                            Text("Reconstrucción de Autonomía", color = SoltarAmber, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cuando una relación termina, recuperas tiempo y energía. ¿En qué persona eliges convertirte ahora?",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // Área
                Column {
                    Text("Área de Vida", style = MaterialTheme.typography.labelMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        areas.take(2).forEach { area ->
                            val selected = uiState.identityAreaSelected == area
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.setIdentityArea(area) },
                                label = { Text(area, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoltarAmber,
                                    selectedLabelColor = SoltarBackground,
                                    containerColor = SoltarSurfaceElevated,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        areas.drop(2).forEach { area ->
                            val selected = uiState.identityAreaSelected == area
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.setIdentityArea(area) },
                                label = { Text(area, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoltarAmber,
                                    selectedLabelColor = SoltarBackground,
                                    containerColor = SoltarSurfaceElevated,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                }

                // Título de la meta / hábito
                Column {
                    Text("Acción o Hábito Concreto", style = MaterialTheme.typography.labelMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.newGoalTitleInput,
                        onValueChange = viewModel::setNewGoalTitle,
                        placeholder = { Text("Ej: Ir a nadar 30 min, retomar mis clases de dibujo", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("goal_input_title"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Quién elijo ser con esta acción
                Column {
                    Text("Identidad deseada ('Elijo ser una persona que...')", style = MaterialTheme.typography.labelMedium, color = SoltarSage, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.whoIWantToBeInput,
                        onValueChange = viewModel::setWhoIWantToBe,
                        placeholder = { Text("Ej: Que cuida su salud y no descuida sus pasiones por nadie", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("goal_input_who_i_want_to_be"),
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

                // Frecuencia
                Column {
                    Text("Frecuencia", style = MaterialTheme.typography.labelMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        frequencies.forEach { freq ->
                            val selected = uiState.newGoalFrequencyInput == freq
                            FilterChip(
                                selected = selected,
                                onClick = { viewModel.setNewGoalFrequency(freq) },
                                label = { Text(freq, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoltarAmber,
                                    selectedLabelColor = SoltarBackground,
                                    containerColor = SoltarSurfaceElevated,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        viewModel.saveIdentityGoal()
                        onDismiss()
                    },
                    enabled = uiState.newGoalTitleInput.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("goal_submit_cta"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                ) {
                    Text("Registrar objetivo de identidad", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
