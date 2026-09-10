package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.SoltarAiEngine
import com.example.data.JournalEntryEntity
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TemporalMirrorDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val journalEntries by viewModel.journalEntries.collectAsState()
    
    // Sort journals chronologically
    val sortedJournals = remember(journalEntries) {
        journalEntries.sortedBy { it.timestamp }
    }

    var selectedPastId by remember { mutableStateOf<Long?>(null) }
    var selectedCurrentId by remember { mutableStateOf<Long?>(null) }

    // Default selection: oldest vs newest (or today)
    LaunchedEffect(sortedJournals) {
        if (sortedJournals.isNotEmpty() && selectedPastId == null) {
            selectedPastId = sortedJournals.first().id
            selectedCurrentId = sortedJournals.last().id
        }
    }

    val pastEntry = sortedJournals.find { it.id == selectedPastId } ?: sortedJournals.firstOrNull()
    val currentEntry = sortedJournals.find { it.id == selectedCurrentId } ?: sortedJournals.lastOrNull()

    var analysisResult by remember { mutableStateOf<com.example.ai.LinguisticAnalysisResult?>(null) }

    LaunchedEffect(pastEntry, currentEntry) {
        if (pastEntry != null && currentEntry != null && pastEntry.id != currentEntry.id) {
            analysisResult = SoltarAiEngine.analyzeJournalLinguistic(listOf(currentEntry, pastEntry))
        } else {
            analysisResult = null
        }
    }

    val dateFormat = remember { SimpleDateFormat("dd 'de' MMM, yyyy", Locale("es", "ES")) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(SoltarBackground.copy(alpha = 0.95f)),
        color = SoltarSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SoltarAmber.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.History, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(
                            text = "Espejo Temporal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Tu propia evidencia en tus propias palabras",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextPrimary)
                }
            }

            if (sortedJournals.size < 2) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "", fontSize = 36.sp)
                        Text(
                            text = "Necesitas al menos 2 entradas en tu diario para activar el Espejo Temporal.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                return@Column
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = "Compara cómo escribías antes y cómo escribes ahora. El cambio no es un número: son tus propias palabras demostrando tu transformación.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                // AI Linguistic Analysis Result Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, SoltarAmber)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Análisis Lingüístico Comparativo (ADRIANA AI)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = SoltarAmber,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = analysisResult?.cambioDesdeUltimaEntrada ?: "Selecciona dos entradas con al menos unos días de diferencia para ver tu espejo temporal de lenguaje y avance emocional.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Side by Side Selection & Preview
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Past Entry Card
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(220.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                            border = BorderStroke(1.dp, SoltarBorder)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Pasado (${dateFormat.format(Date(pastEntry?.timestamp ?: 0))})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SoltarSage,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = pastEntry?.title ?: "",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = pastEntry?.content ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    maxLines = 6
                                )
                            }
                        }

                        // Current Entry Card
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(220.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                            border = BorderStroke(1.dp, SoltarAmber)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Actual (${dateFormat.format(Date(currentEntry?.timestamp ?: 0))})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = currentEntry?.title ?: "",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = currentEntry?.content ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                    maxLines = 6
                                )
                            }
                        }
                    }
                }
            }

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar Espejo Temporal", color = SoltarBackground, fontWeight = FontWeight.Bold)
            }
        }
    }
}
