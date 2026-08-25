package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun ProcessScreen(
    viewModel: SoltarViewModel,
    modifier: Modifier = Modifier
) {
    val checkins by viewModel.checkins.collectAsState()
    val urgeEpisodes by viewModel.urgeEpisodes.collectAsState()
    val thoughts by viewModel.thoughts.collectAsState()
    val audits by viewModel.audits.collectAsState()
    val idealizations by viewModel.idealizations.collectAsState()
    val letters by viewModel.letters.collectAsState()
    val relapses by viewModel.relapses.collectAsState()

    var selectedFilter by remember { mutableStateOf("Todos") }
    val filterOptions = listOf("Todos", "Impulsos", "Pensamientos", "Auditorías", "Cartas", "Idealización")

    val totalUrgesContained = urgeEpisodes.size
    val totalThoughtsRestructured = thoughts.size
    val totalAuditsSaved = audits.size
    val totalLettersStored = letters.size

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
                    text = "TU PROCESO DE EVOLUCIÓN",
                    style = MaterialTheme.typography.labelMedium,
                    color = SoltarAmber,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Evidencia objetiva de tu avance",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "El dolor no es lineal, pero tu compromiso con tu dignidad deja un rastro claro.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // Summary Metric Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Impulsos Contenidos",
                    value = "$totalUrgesContained",
                    icon = Icons.Default.Bolt,
                    accentColor = UrgeAlertRed
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Bucles Cerrados",
                    value = "$totalThoughtsRestructured",
                    icon = Icons.Default.Psychology,
                    accentColor = SoltarAmber
                )
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Auditorías Reales",
                    value = "$totalAuditsSaved",
                    icon = Icons.Default.Balance,
                    accentColor = SoltarSage
                )
            }
        }

        // Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterOptions) { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter, fontSize = 12.sp) },
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

        // Timeline Items

        // 1. Impulsos
        if (selectedFilter == "Todos" || selectedFilter == "Impulsos") {
            if (urgeEpisodes.isNotEmpty()) {
                item {
                    Text(
                        text = "🛡️ Impulsos Regulados con Éxito",
                        style = MaterialTheme.typography.titleSmall,
                        color = UrgeAlertRed,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(urgeEpisodes) { urge ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Emoción: ${urge.emotion}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = UrgeAlertRed.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "${urge.initialIntensity} ➔ ${urge.finalIntensity} / 10",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = UrgeAlertRed,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Deseo inicial: ${urge.desiredAction}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            if (urge.learning.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "💡 Aprendizaje: ${urge.learning}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoltarAmber,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Pensamientos Reestructurados (TCC)
        if (selectedFilter == "Todos" || selectedFilter == "Pensamientos") {
            if (thoughts.isNotEmpty()) {
                item {
                    Text(
                        text = "🧠 Pensamientos Intrusivos Desarmados",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(thoughts) { thought ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "«${thought.originalThought}»",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = { viewModel.deleteThought(thought.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = TextMuted, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Hecho real: ${thought.fact}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoltarSage
                            )
                            Text(
                                text = "• Interpretación: ${thought.interpretation}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoltarTerracotta
                            )
                            if (thought.concreteAction.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• Acción de anclaje: ${thought.concreteAction}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoltarAmber
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Auditorías de la Relación
        if (selectedFilter == "Todos" || selectedFilter == "Auditorías") {
            if (audits.isNotEmpty()) {
                item {
                    Text(
                        text = "⚖️ Auditorías de Responsabilidad",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarSage,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(audits) { audit ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = audit.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { viewModel.deleteAudit(audit.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = TextMuted, modifier = Modifier.size(18.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Mi responsabilidad: ${audit.myResponsibility}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoltarSage
                            )
                            Text(
                                text = "Su responsabilidad: ${audit.otherResponsibility}",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoltarTerracotta
                            )
                            if (audit.patternIdentified.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Patrón identificado: ${audit.patternIdentified}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoltarAmber
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Cartas No Enviadas
        if (selectedFilter == "Todos" || selectedFilter == "Cartas") {
            if (letters.isNotEmpty()) {
                item {
                    Text(
                        text = "✉️ Cartas Privadas y Selladas",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarBlue,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(letters) { letter ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = letter.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = letter.category,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SoltarBlue
                                    )
                                }
                                if (letter.isClosed) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = SoltarSage.copy(alpha = 0.2f)
                                    ) {
                                        Text(
                                            text = "Sellada 🕯️",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            color = SoltarSageLight,
                                            fontSize = 11.sp
                                        )
                                    }
                                } else {
                                    TextButton(onClick = { viewModel.performLetterCeremony(letter.id) }) {
                                        Text("Sellar 🕯️", color = SoltarAmber, fontSize = 12.sp)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = letter.content,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                maxLines = 4
                            )
                        }
                    }
                }
            }
        }

        // 5. Antídotos de Idealización
        if (selectedFilter == "Todos" || selectedFilter == "Idealización") {
            if (idealizations.isNotEmpty()) {
                item {
                    Text(
                        text = "💡 Antídotos de Idealización Registrados",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarTerracotta,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(idealizations) { pair ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Lo que mi mente romantizaba:", style = MaterialTheme.typography.labelSmall, color = SoltarTerracotta, fontWeight = FontWeight.Bold)
                                IconButton(onClick = { viewModel.deleteIdealization(pair.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = TextMuted, modifier = Modifier.size(16.dp))
                                }
                            }
                            Text(pair.whatIMiss, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("La realidad que también existía:", style = MaterialTheme.typography.labelSmall, color = SoltarSage, fontWeight = FontWeight.Bold)
                            Text(pair.whatIActuallyExperienced, style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                        }
                    }
                }
            }
        }

        // Empty state placeholder if no records
        if (urgeEpisodes.isEmpty() && thoughts.isEmpty() && audits.isEmpty() && letters.isEmpty() && idealizations.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Timeline, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Aún no hay registros en tu proceso",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "A medida que contengas impulsos, cierres bucles de pensamiento y audites la realidad, verás aquí el mapa de tu reconstrucción.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    accentColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
        border = BorderStroke(1.dp, SoltarBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 10.sp, maxLines = 1)
        }
    }
}
