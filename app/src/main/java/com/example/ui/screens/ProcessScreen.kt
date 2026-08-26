package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoltarSoundManager
import com.example.data.CheckinEntity
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
    val filterOptions = listOf("Todos", "Impulsos", "Pensamientos", "Auditorías", "Cartas", "Idealización", "Recaídas")

    var selectedMetricDays by remember { mutableStateOf(7) }

    val totalUrgesContained = urgeEpisodes.size
    val totalThoughtsRestructured = thoughts.size
    val totalAuditsSaved = audits.size
    val totalLettersStored = letters.size
    val totalRelapses = relapses.size

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoltarBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp)
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
                    text = "El dolor no es lineal, pero tu compromiso con tu dignidad deja un rastro medible.",
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

        // Emotional Evolution Chart (Interactive Line Graph)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("evolution_chart_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "EVOLUCIÓN EMOCIONAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Tendencia de regulación en el tiempo",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        // Day range selector
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf(7, 30).forEach { days ->
                                val isSelected = selectedMetricDays == days
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isSelected) SoltarAmber else SoltarSurfaceElevated,
                                    border = BorderStroke(1.dp, if (isSelected) SoltarAmber else SoltarBorderSubtle),
                                    modifier = Modifier.clickable {
                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                        selectedMetricDays = days
                                    }
                                ) {
                                    Text(
                                        text = "${days}d",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        color = if (isSelected) SoltarBackground else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Multi-line Canvas Chart
                    EvolutionLineChart(
                        checkins = checkins.takeLast(selectedMetricDays),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ChartLegendItem(label = "Dolor", color = UrgeAlertRed)
                        ChartLegendItem(label = "Ansiedad", color = SoltarTerracotta)
                        ChartLegendItem(label = "Nostalgia", color = SoltarAmber)
                        ChartLegendItem(label = "Impulso", color = UrgeAlertRed.copy(alpha = 0.6f))
                        ChartLegendItem(label = "Autonomía", color = SoltarSage)
                    }
                }
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
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            selectedFilter = filter
                        },
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

        // Recaídas registradas (con enfoque compasivo)
        if (selectedFilter == "Todos" || selectedFilter == "Recaídas") {
            if (relapses.isNotEmpty()) {
                item {
                    Text(
                        text = "🤝 Registros de Recaída (Puntos de Información)",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarSage,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(relapses) { relapse ->
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
                                    text = relapse.whatHappened,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = SoltarSage.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "Información útil",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        color = SoltarSage,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("• Detonante: ${relapse.trigger}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text("• Emoción: ${relapse.emotion}", style = MaterialTheme.typography.bodySmall, color = SoltarTerracotta)
                            if (relapse.learning.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("💡 Aprendizaje: ${relapse.learning}", style = MaterialTheme.typography.bodySmall, color = SoltarAmber, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

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
                            Text("• Hecho real: ${thought.fact}", style = MaterialTheme.typography.bodySmall, color = SoltarSage)
                            Text("• Interpretación: ${thought.interpretation}", style = MaterialTheme.typography.bodySmall, color = SoltarTerracotta)
                            if (thought.concreteAction.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("• Acción de anclaje: ${thought.concreteAction}", style = MaterialTheme.typography.bodySmall, color = SoltarAmber)
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
                            Text("Mi responsabilidad: ${audit.myResponsibility}", style = MaterialTheme.typography.bodySmall, color = SoltarSage)
                            Text("Su responsabilidad: ${audit.otherResponsibility}", style = MaterialTheme.typography.bodySmall, color = SoltarTerracotta)
                            if (audit.patternIdentified.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Patrón identificado: ${audit.patternIdentified}", style = MaterialTheme.typography.bodySmall, color = SoltarAmber)
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

        // Empty state placeholder
        if (urgeEpisodes.isEmpty() && thoughts.isEmpty() && audits.isEmpty() && letters.isEmpty() && idealizations.isEmpty() && relapses.isEmpty()) {
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
                            text = "A medida que contengas impulsos, cierres bucles de pensamiento y evalúes tu día, verás aquí la gráfica de tu reconstrucción.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
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

@Composable
private fun ChartLegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(text = label, color = TextSecondary, fontSize = 10.sp)
    }
}

@Composable
private fun EvolutionLineChart(
    checkins: List<CheckinEntity>,
    modifier: Modifier = Modifier
) {
    // If no checkins exist, supply a sample trend baseline
    val dataPoints = remember(checkins) {
        if (checkins.isNotEmpty()) checkins else listOf(
            CheckinEntity(dateKey = "1", pain = 8f, anxiety = 7f, nostalgia = 8f, urgeToContact = 8f, autonomy = 3f),
            CheckinEntity(dateKey = "2", pain = 7f, anxiety = 6f, nostalgia = 7f, urgeToContact = 6f, autonomy = 4f),
            CheckinEntity(dateKey = "3", pain = 5f, anxiety = 5f, nostalgia = 6f, urgeToContact = 4f, autonomy = 5f),
            CheckinEntity(dateKey = "4", pain = 6f, anxiety = 4f, nostalgia = 5f, urgeToContact = 3f, autonomy = 6f),
            CheckinEntity(dateKey = "5", pain = 4f, anxiety = 3f, nostalgia = 5f, urgeToContact = 2f, autonomy = 7f),
            CheckinEntity(dateKey = "6", pain = 3f, anxiety = 3f, nostalgia = 4f, urgeToContact = 2f, autonomy = 8f),
            CheckinEntity(dateKey = "7", pain = 2f, anxiety = 2f, nostalgia = 3f, urgeToContact = 1f, autonomy = 8.5f)
        )
    }

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val paddingLeft = 16f
        val paddingRight = 16f
        val paddingTop = 12f
        val paddingBottom = 16f

        val usableWidth = width - paddingLeft - paddingRight
        val usableHeight = height - paddingTop - paddingBottom

        // Draw horizontal grid lines (0, 5, 10)
        for (i in 0..2) {
            val y = paddingTop + (usableHeight / 2) * i
            drawLine(
                color = SoltarBorderSubtle,
                start = Offset(paddingLeft, y),
                end = Offset(width - paddingRight, y),
                strokeWidth = 1f
            )
        }

        if (dataPoints.size < 2) return@Canvas

        val stepX = usableWidth / (dataPoints.size - 1).coerceAtLeast(1)

        fun drawMetricPath(values: List<Float>, color: Color, strokeWidth: Float = 2.5f) {
            val path = Path()
            values.forEachIndexed { index, value ->
                val x = paddingLeft + index * stepX
                val normalizedY = (10f - value.coerceIn(0f, 10f)) / 10f
                val y = paddingTop + normalizedY * usableHeight
                if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Draw small endpoint circles
            values.forEachIndexed { index, value ->
                val x = paddingLeft + index * stepX
                val normalizedY = (10f - value.coerceIn(0f, 10f)) / 10f
                val y = paddingTop + normalizedY * usableHeight
                drawCircle(
                    color = color,
                    radius = 3.5f,
                    center = Offset(x, y)
                )
            }
        }

        // Draw the 5 emotional dimensions
        drawMetricPath(dataPoints.map { it.pain }, UrgeAlertRed)
        drawMetricPath(dataPoints.map { it.anxiety }, SoltarTerracotta)
        drawMetricPath(dataPoints.map { it.nostalgia }, SoltarAmber)
        drawMetricPath(dataPoints.map { it.urgeToContact }, UrgeAlertRed.copy(alpha = 0.6f))
        drawMetricPath(dataPoints.map { it.autonomy }, SoltarSage, strokeWidth = 3f)
    }
}
