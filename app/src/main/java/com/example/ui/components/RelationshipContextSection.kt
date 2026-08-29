package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SoltarSettingsEntity
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun RelationshipContextSection(
    viewModel: SoltarViewModel,
    settings: SoltarSettingsEntity?
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
        border = BorderStroke(1.dp, SoltarBorder)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "Mi situación y contexto afectivo",
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "• Duración: ${settings?.relDuration?.replace("_", " ") ?: "6-12 meses"}\n" +
                       "• Tiempo desde ruptura: ${settings?.timeSinceBreakup?.replace("_", " ") ?: "1-3 meses"}\n" +
                       "• Hijos / Vínculos inevitables: ${if (settings?.hasChildren == true) "Sí (Contacto adaptado)" else "No"}\n" +
                       "• Contacto actual: ${settings?.contactType?.replace("_", " ") ?: "Contacto Cero"}\n" +
                       "• Situación: ${settings?.breakupSituation?.replace("_", " ") ?: "Ruptura reciente"}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
            ) {
                Text("Actualizar mi situación completa", color = SoltarBackground, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showDialog && settings != null) {
        var tempRelDuration by remember { mutableStateOf(settings.relDuration) }
        var tempTimeSinceBreakup by remember { mutableStateOf(settings.timeSinceBreakup) }
        var tempPreviousBreakups by remember { mutableStateOf(settings.previousBreakupsCount.toString()) }
        var tempCohabitation by remember { mutableStateOf(settings.cohabitation) }
        var tempMarriedOrEngaged by remember { mutableStateOf(settings.marriedOrEngaged) }
        var tempBreakupSituation by remember { mutableStateOf(settings.breakupSituation) }
        var tempAnticipatedGrief by remember { mutableStateOf(settings.anticipatedGrief) }
        var tempHasChildren by remember { mutableStateOf(settings.hasChildren) }
        var tempInevitableContact by remember { mutableStateOf(settings.inevitableContact) }
        var tempContactFrequency by remember { mutableStateOf(settings.childrenContactFrequency) }
        var tempParentalOnly by remember { mutableStateOf(settings.parentalOnlyCommunication) }
        var tempContactType by remember { mutableStateOf(settings.contactType) }
        var tempEmotionalSituation by remember { mutableStateOf(settings.emotionalSituation) }
        var tempDecisionMaker by remember { mutableStateOf(settings.decisionMaker) }
        var tempBreakupReason by remember { mutableStateOf(settings.breakupReason) }
        var tempPracticals by remember { mutableStateOf(settings.practicals) }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Configuración Avanzada de tu Situación", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 480.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("1. Duración de la relación", style = MaterialTheme.typography.labelLarge, color = SoltarAmber)
                    listOf("MENOS_3_MESES", "3_6_MESES", "6_12_MESES", "1_3_ANIOS", "3_5_ANIOS", "5_10_ANIOS", "MAS_10_ANIOS").forEach { opt ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = tempRelDuration == opt, onClick = { tempRelDuration = opt })
                            Text(opt.replace("_", " "), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Divider()

                    Text("2. Tiempo transcurrido desde la ruptura", style = MaterialTheme.typography.labelLarge, color = SoltarAmber)
                    listOf("MENOS_1_MES", "1_3_MESES", "3_6_MESES", "6_12_MESES", "MAS_1_ANO", "TODAVIA_JUNTOS").forEach { opt ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = tempTimeSinceBreakup == opt, onClick = { tempTimeSinceBreakup = opt })
                            Text(opt.replace("_", " "), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Divider()

                    Text("3. Factores de convivencia y compromiso", style = MaterialTheme.typography.labelLarge, color = SoltarAmber)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = tempCohabitation, onCheckedChange = { tempCohabitation = it })
                        Text("¿Convivían en el mismo hogar?", style = MaterialTheme.typography.bodySmall)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = tempMarriedOrEngaged, onCheckedChange = { tempMarriedOrEngaged = it })
                        Text("¿Estaban casados o comprometidos?", style = MaterialTheme.typography.bodySmall)
                    }

                    Divider()

                    Text("4. Situación de la ruptura", style = MaterialTheme.typography.labelLarge, color = SoltarAmber)
                    listOf("RUPTURA_RECIENTE", "RUPTURA_ANTIGUA", "SEPARACION_AMBIGUA", "DISTANCIAMIENTO", "TODAVIA_JUNTOS_DESCONECTADOS", "PLANTEANDOSE_TERMINAR", "OTRA_PERSONA_TERMINO", "MUTUA", "NO_DEFINIDA").forEach { opt ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = tempBreakupSituation == opt, onClick = { tempBreakupSituation = opt })
                            Text(opt.replace("_", " "), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Divider()

                    Text("5. Duelo anticipado (¿empezaste el duelo antes de terminar?)", style = MaterialTheme.typography.labelLarge, color = SoltarAmber)
                    listOf("SI_LLEVABA_TIEMPO_DECEPCIONANDOME", "SI_ESTABA_AGOTADO", "UN_POCO", "NO", "NO_ESTOY_SEGURO").forEach { opt ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = tempAnticipatedGrief == opt, onClick = { tempAnticipatedGrief = opt })
                            Text(opt.replace("_", " "), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Divider()

                    Text("6. Hijos y Contacto Inevitable", style = MaterialTheme.typography.labelLarge, color = SoltarAmber)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = tempHasChildren, onCheckedChange = { tempHasChildren = it })
                        Text("¿Tienen hijos en común u otros vínculos inevitables?", style = MaterialTheme.typography.bodySmall)
                    }
                    if (tempHasChildren) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = tempParentalOnly, onCheckedChange = { tempParentalOnly = it })
                            Text("¿Comunicación estrictamente parental/funcional?", style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Divider()

                    Text("7. Tipo de Contacto actual (Contacto Cero Absoluto vs Adaptado)", style = MaterialTheme.typography.labelLarge, color = SoltarAmber)
                    Text("Nota: En contacto adaptado (hijos, trabajo, vivienda), el objetivo no es cortar todo contacto sino eliminar contacto emocional innecesario y mantener la comunicación funcional imprescindible.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    listOf("CONTACTO_CERO_REAL", "CONTACTO_OCASIONAL", "CONTACTO_FRECUENTE", "POR_HIJOS_ADAPTADO", "POR_TRABAJO_ADAPTADO", "POR_CONVIVENCIA_TRANSITORIA").forEach { opt ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = tempContactType == opt, onClick = { tempContactType = opt })
                            Text(opt.replace("_", " "), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Divider()

                    Text("8. Estado emocional actual (ej. ansiedad, nostalgia, rabia)", style = MaterialTheme.typography.labelLarge, color = SoltarAmber)
                    OutlinedTextField(
                        value = tempEmotionalSituation,
                        onValueChange = { tempEmotionalSituation = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodySmall
                    )

                    Text("9. Motivo principal / Contexto", style = MaterialTheme.typography.labelLarge, color = SoltarAmber)
                    OutlinedTextField(
                        value = tempBreakupReason,
                        onValueChange = { tempBreakupReason = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodySmall
                    )

                    Text("10. Factores prácticos pendientes (ej. mudanza, piso, mascotas)", style = MaterialTheme.typography.labelLarge, color = SoltarAmber)
                    OutlinedTextField(
                        value = tempPracticals,
                        onValueChange = { tempPracticals = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = MaterialTheme.typography.bodySmall
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateRelationshipContext(
                            relDuration = tempRelDuration,
                            timeSinceBreakup = tempTimeSinceBreakup,
                            previousBreakupsCount = tempPreviousBreakups.toIntOrNull() ?: 0,
                            cohabitation = tempCohabitation,
                            marriedOrEngaged = tempMarriedOrEngaged,
                            breakupSituation = tempBreakupSituation,
                            anticipatedGrief = tempAnticipatedGrief,
                            hasChildren = tempHasChildren,
                            inevitableContact = tempInevitableContact,
                            childrenContactFrequency = tempContactFrequency,
                            childrenCohabitation = "NO_CONVIVENCIA",
                            parentalOnlyCommunication = tempParentalOnly,
                            contactType = tempContactType,
                            emotionalSituation = tempEmotionalSituation,
                            decisionMaker = tempDecisionMaker,
                            breakupReason = tempBreakupReason,
                            practicals = tempPracticals
                        )
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                ) {
                    Text("Guardar Cambios", color = SoltarBackground, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

