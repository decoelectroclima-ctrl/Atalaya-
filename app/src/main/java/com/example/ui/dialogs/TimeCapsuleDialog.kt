package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.ai.OnDeviceLlmEngine
import com.example.data.TimeCapsuleEntity
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TimeCapsuleDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Nueva, 1 = Mis Cápsulas
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var days by remember { mutableIntStateOf(30) }

    val capsules by viewModel.timeCapsules.collectAsState()
    val journals by viewModel.journalEntries.collectAsState()
    val now = remember { System.currentTimeMillis() }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    var inspectingCapsule by remember { mutableStateOf<TimeCapsuleEntity?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.88f)
                .testTag("time_capsule_dialog"),
            shape = RoundedCornerShape(20.dp),
            color = SoltarBackground,
            border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
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
                        Icon(Icons.Default.HourglassBottom, contentDescription = null, tint = SoltarAmber)
                        Column {
                            Text(
                                text = "CÁPSULA DEL TIEMPO",
                                style = MaterialTheme.typography.titleMedium,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Cartas selladas & Hallazgos de cambio",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoltarSurface, RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clickable {
                                selectedTab = 0
                                inspectingCapsule = null
                            },
                        shape = RoundedCornerShape(10.dp),
                        color = if (selectedTab == 0) SoltarAmber else androidx.compose.ui.graphics.Color.Transparent
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Nueva Carta",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selectedTab == 0) SoltarBackground else TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp)
                            .clickable { selectedTab = 1 },
                        shape = RoundedCornerShape(10.dp),
                        color = if (selectedTab == 1) SoltarAmber else androidx.compose.ui.graphics.Color.Transparent
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Mis Cápsulas (${capsules.size})",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selectedTab == 1) SoltarBackground else TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tab Content
                if (selectedTab == 0) {
                    // TAB 0: CREAR CÁPSULA
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Escribe una carta a tu yo futuro. Expresa tu dolor, tus miedos y tus promesas de cuidado. Cuando la desbloquees, la IA on-device contrastará tus palabras de hoy con tu evolución real.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Título o Motivo de la Carta") },
                            placeholder = { Text("Ej: Carta para cuando hayan pasado 30 días") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it },
                            label = { Text("Tu Carta al Yo Futuro") },
                            placeholder = { Text("Escribe con total honestidad lo que sientes hoy...") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 6,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Text(
                            text = "Tiempo de sellado (desbloquear en):",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(30, 60, 90).forEach { dayOption ->
                                FilterChip(
                                    selected = days == dayOption,
                                    onClick = { days = dayOption },
                                    label = { Text("$dayOption días") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = SoltarAmber,
                                        selectedLabelColor = SoltarBackground,
                                        containerColor = SoltarSurface,
                                        labelColor = TextPrimary
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                if (title.isNotBlank() && content.isNotBlank()) {
                                    val unlockTime = System.currentTimeMillis() + (days.toLong() * 24 * 3600 * 1000)
                                    viewModel.saveTimeCapsule(title, content, unlockTime)
                                    title = ""
                                    content = ""
                                    selectedTab = 1
                                }
                            },
                            enabled = title.isNotBlank() && content.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Sellar Cápsula", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // TAB 1: MIS CÁPSULAS & HALLAZGO DE CAMBIO REAL
                    if (inspectingCapsule != null) {
                        val cap = inspectingCapsule!!
                        val daysElapsed = ((now - cap.createdAt) / (24 * 3600 * 1000L)).coerceAtLeast(1L).toInt()
                        val realization = remember(cap.id) {
                            OnDeviceLlmEngine.generateTimeCapsuleRealization(cap.content, journals, daysElapsed)
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { inspectingCapsule = null }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = SoltarAmber)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Volver a la lista", color = SoltarAmber)
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SoltarAmber.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "Sellada el ${dateFormat.format(Date(cap.createdAt))}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SoltarAmber,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Text(
                                text = cap.title,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )

                            // Carta original
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                                border = BorderStroke(1.dp, SoltarBorder)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Text(
                                        text = "📜 CARTA ORIGINAL ESCRITA HACE $daysElapsed DÍAS:",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = cap.content,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                        lineHeight = 20.sp
                                    )
                                }
                            }

                            // Hallazgo generado con IA on-device
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                                border = BorderStroke(1.5.dp, SoltarAmber)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber)
                                        Text(
                                            text = "HALLAZGO DE TRANSFORMACIÓN REAL (IA On-Device)",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SoltarAmber,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = realization,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Lista de cápsulas
                        if (capsules.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.MailOutline, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("No tienes cápsulas selladas.", color = TextSecondary)
                                    TextButton(onClick = { selectedTab = 0 }) {
                                        Text("Crear tu primera carta", color = SoltarAmber)
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(capsules) { cap ->
                                    val isReady = now >= cap.unlockAtTimestamp || cap.isUnlocked
                                    val remainingDays = if (now < cap.unlockAtTimestamp) {
                                        ((cap.unlockAtTimestamp - now) / (24 * 3600 * 1000L)).toInt() + 1
                                    } else 0

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                if (isReady) {
                                                    inspectingCapsule = cap
                                                }
                                            },
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isReady) SoltarSurfaceElevated else SoltarSurface
                                        ),
                                        border = BorderStroke(1.dp, if (isReady) SoltarAmber else SoltarBorderSubtle)
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = cap.title,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = TextPrimary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = if (isReady) "✨ Lista para desbloquear y comparar con tu diario" else "🔒 Desbloqueo en $remainingDays días (${dateFormat.format(Date(cap.unlockAtTimestamp))})",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = if (isReady) SoltarAmber else TextSecondary
                                                )
                                            }

                                            if (isReady) {
                                                Button(
                                                    onClick = {
                                                        viewModel.unlockTimeCapsule(cap.id)
                                                        inspectingCapsule = cap
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                                                    shape = RoundedCornerShape(8.dp)
                                                ) {
                                                    Text("Abrir", color = SoltarBackground, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                                }
                                            } else {
                                                Icon(Icons.Default.Lock, contentDescription = null, tint = TextMuted)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
