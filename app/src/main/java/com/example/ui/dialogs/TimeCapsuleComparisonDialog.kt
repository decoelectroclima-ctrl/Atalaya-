package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ai.OnDeviceLlmEngine
import com.example.data.JournalEntryEntity
import com.example.data.UnsentLetterEntity
import com.example.ui.theme.*

@Composable
fun TimeCapsuleComparisonDialog(
    letter: UnsentLetterEntity,
    recentJournals: List<JournalEntryEntity>,
    onDismiss: () -> Unit
) {
    val daysElapsed = ((System.currentTimeMillis() - letter.timestamp) / (1000L * 3600 * 24)).toInt().coerceAtLeast(0)

    val realizationText = remember(letter.id, recentJournals.size) {
        OnDeviceLlmEngine.generateTimeCapsuleRealization(
            letterText = letter.content,
            recentJournals = recentJournals,
            daysElapsed = daysElapsed
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SoltarSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, SoltarAmber)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.HourglassTop, contentDescription = null, tint = SoltarAmber)
                        Column {
                            Text(
                                text = "Cápsula del Tiempo",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Hallazgo de Cambio Real (IA On-Device)",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Carta original resumen
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "✉️ «${letter.title}»",
                                style = MaterialTheme.typography.labelLarge,
                                color = SoltarBlue,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Escrita hace $daysElapsed días • Categoría: ${letter.category}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = letter.content,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                maxLines = 6
                            )
                        }
                    }

                    // Hallazgo del Modelo On-Device
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, SoltarAmber)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                                Text(
                                    text = "Comparación Lingüística y Emocional",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = SoltarAmber,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = realizationText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    Text(
                        text = "Este hallazgo se procesó 100% de forma local y privada en tu dispositivo, comparando la carta con ${recentJournals.size} reflexiones de tu diario.",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                ) {
                    Text("Comprendido", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
