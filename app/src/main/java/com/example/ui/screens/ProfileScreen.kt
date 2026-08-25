package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: SoltarViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val identityGoals by viewModel.identityGoals.collectAsState()

    var showResetConfirmDialog by remember { mutableStateOf(false) }

    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("¿Restablecer datos locales?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Esta acción borrará tus registros locales y el historial de la IA, garantizando tu privacidad y tu derecho al olvido. Esta acción no se puede deshacer.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.fullDataReset()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed)
                ) {
                    Text("Borrar Todo", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetConfirmDialog = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = SoltarSurfaceElevated,
            shape = RoundedCornerShape(16.dp)
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoltarBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp)
    ) {
        // Header
        item {
            Column {
                Text(
                    text = "IDENTIDAD Y VALORES",
                    style = MaterialTheme.typography.labelMedium,
                    color = SoltarAmber,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Quién elijo ser hoy",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "El fin de una relación es el inicio de tu recuperación personal.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Identity Contrast Card (Quién era vs Quién elijo ser)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Pilares de mi Identidad",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = SoltarSurfaceElevated,
                            border = BorderStroke(1.dp, SoltarBorderSubtle)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("En la relación:", style = MaterialTheme.typography.labelSmall, color = SoltarTerracotta, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• Pérdida de límites\n• Dependencia del humor ajeno\n• Descuido de proyectos", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = SoltarSurfaceElevated,
                            border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Hoy elijo ser:", style = MaterialTheme.typography.labelSmall, color = SoltarSage, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• Dueño de mi tiempo\n• Firme con mis límites\n• Presente en mi cuerpo", style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Metas de Identidad Activas
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Metas de Autonomía",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { viewModel.toggleIdentityGoalModal(true) }) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar meta", tint = SoltarAmber)
                        }
                    }

                    if (identityGoals.isEmpty()) {
                        Text(
                            text = "No has registrado metas de autonomía todavía. Toca el botón '+' para agregar una.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            identityGoals.forEach { goal ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = SoltarSurfaceElevated,
                                    border = BorderStroke(1.dp, if (goal.isCompleted) SoltarSage.copy(alpha = 0.5f) else SoltarBorderSubtle)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Checkbox(
                                            checked = goal.isCompleted,
                                            onCheckedChange = { viewModel.toggleGoalCompleted(goal.id, goal.isCompleted) },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = SoltarSage,
                                                uncheckedColor = SoltarBorder
                                            )
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = goal.goalTitle,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = if (goal.isCompleted) TextSecondary else TextPrimary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = "${goal.area} • ${goal.goalFrequency}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = SoltarAmber,
                                                fontSize = 11.sp
                                            )
                                        }
                                        IconButton(onClick = { viewModel.deleteIdentityGoal(goal.id) }) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = TextMuted, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Límites Innegociables
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Mis Límites Innegociables",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val limits = listOf(
                        "No rogar por claridad o atención.",
                        "No revisar redes sociales en momentos de vulnerabilidad o de noche.",
                        "No volver a un lugar donde tuve que apagar mi luz para encajar.",
                        "Respetar mi dolor sin convertirlo en auto-sabotaje."
                    )

                    limits.forEach { limit ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🛡️", fontSize = 13.sp)
                            Text(limit, style = MaterialTheme.typography.bodySmall, color = TextSecondary, lineHeight = 18.sp)
                        }
                    }
                }
            }
        }

        // Privacidad & Seguridad (Derecho al Olvido)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Privacidad y Derecho al Olvido",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tus datos viven únicamente en este dispositivo. Puedes reiniciar la memoria en cualquier momento.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = { viewModel.clearAiMemory() },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = SoltarAmber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reiniciar historial del chat IA", color = SoltarAmber, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { showResetConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertBackground)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = UrgeAlertRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Borrar todos los datos locales", color = UrgeAlertRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}
