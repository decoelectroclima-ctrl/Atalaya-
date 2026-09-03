package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Close
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
fun RelationshipAuditDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showGuidedAssistant by remember { mutableStateOf(false) }

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
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("audit_dialog_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Text(
                        text = "AUDITORÍA DE REALIDAD",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    TextButton(
                        onClick = {
                            viewModel.saveRelationshipAudit()
                            onDismiss()
                        },
                        enabled = uiState.auditTitleInput.isNotBlank(),
                        modifier = Modifier.testTag("audit_save_button")
                    ) {
                        Text(
                            "Guardar",
                            color = if (uiState.auditTitleInput.isNotBlank()) SoltarAmber else TextMuted,
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
                            Icon(Icons.Default.Balance, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                            Text("Reparto de Responsabilidades", color = SoltarAmber, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ni toda la culpa fue tuya, ni la otra persona fue un monstruo sin matices. Separar responsabilidades disuelve la culpa tóxica.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // Asistente Guiado de Señales de Alarma (Red Flags)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (showGuidedAssistant) SoltarAmber else SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                                Text(
                                    text = "Asistencia Guiada de Red Flags",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SoltarAmber,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            TextButton(
                                onClick = { showGuidedAssistant = !showGuidedAssistant },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (showGuidedAssistant) "Ocultar" else "✨ Ayúdame a identificar",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SoltarAmber,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (showGuidedAssistant) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Elige una pregunta exploratoria para identificar señales que a veces normalizamos:",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val prompts = remember { com.example.ai.OnDeviceLlmEngine.getRedFlagGuidedPrompts() }
                            prompts.forEachIndexed { idx, prompt ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = SoltarSurface,
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, SoltarBorder),
                                    onClick = {
                                        val synthesized = com.example.ai.OnDeviceLlmEngine.synthesizeRedFlagFromDescription(prompt)
                                        viewModel.setAuditTitle("Patrón: $synthesized")
                                        viewModel.setAuditOtherResp(synthesized)
                                        viewModel.setAuditMyResp("Haber tolerado o justificado esta conducta repetidamente")
                                        viewModel.setAuditPattern("No permanecer donde este límite sea vulnerado")
                                        showGuidedAssistant = false
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text("${idx + 1}.", color = SoltarAmber, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        Text(prompt, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // Título del evento / patrón
                Column {
                    Text("Evento o Dinámica Conflictiva", style = MaterialTheme.typography.labelMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.auditTitleInput,
                        onValueChange = viewModel::setAuditTitle,
                        placeholder = { Text("Ej: Discusiones sobre falta de tiempo o silencios prolongados", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("audit_input_title"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // 1. Mi Responsabilidad
                Column {
                    Text("1. Mi Responsabilidad Real (Lo que yo hice o toleré)", style = MaterialTheme.typography.labelMedium, color = SoltarSage, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.auditMyRespInput,
                        onValueChange = viewModel::setAuditMyResp,
                        placeholder = { Text("Ej: No comunicar mis límites a tiempo, sobre-adaptarme para evitar el conflicto", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("audit_input_my_resp"),
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

                // 2. La Responsabilidad de la otra persona
                Column {
                    Text("2. La Responsabilidad de la otra persona", style = MaterialTheme.typography.labelMedium, color = SoltarTerracotta, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.auditOtherRespInput,
                        onValueChange = viewModel::setAuditOtherResp,
                        placeholder = { Text("Ej: Evadir conversaciones difíciles, frialdad repentina, falta de claridad", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("audit_input_other_resp"),
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarTerracotta,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // 3. Incompatibilidad o factores compartidos
                Column {
                    Text("3. Dinámica compartida o incompatibilidad de fondo", style = MaterialTheme.typography.labelMedium, color = SoltarBlue, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.auditSharedRespInput,
                        onValueChange = viewModel::setAuditSharedResp,
                        placeholder = { Text("Ej: Ritmos de vida incompatibles, estilos de apego opuestos en momentos de estrés", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("audit_input_shared_resp"),
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarBlue,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // Patrón identificado para mi futuro
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Patrón identificado para no repetir", style = MaterialTheme.typography.labelMedium, color = SoltarAmber, fontWeight = FontWeight.Bold)
                        if (uiState.auditOtherRespInput.isNotBlank() || uiState.auditTitleInput.isNotBlank()) {
                            TextButton(
                                onClick = {
                                    val flags = listOf(
                                        uiState.auditTitleInput,
                                        uiState.auditMyRespInput,
                                        uiState.auditOtherRespInput,
                                        uiState.auditSharedRespInput
                                    ).filter { it.isNotBlank() }
                                    val synthesized = com.example.ai.OnDeviceLlmEngine.synthesizeRedFlagsPattern(flags)
                                    viewModel.setAuditPattern(synthesized)
                                },
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(14.dp))
                                    Text("Sintetizar con IA", style = MaterialTheme.typography.labelSmall, color = SoltarAmber, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.auditPatternInput,
                        onValueChange = viewModel::setAuditPattern,
                        placeholder = { Text("Ej: No quedarme donde tengo que pedir que me tengan en cuenta", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("audit_input_pattern"),
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

                Button(
                    onClick = {
                        viewModel.saveRelationshipAudit()
                        onDismiss()
                    },
                    enabled = uiState.auditTitleInput.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("audit_submit_cta"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                ) {
                    Text("Guardar auditoría en mi proceso", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
