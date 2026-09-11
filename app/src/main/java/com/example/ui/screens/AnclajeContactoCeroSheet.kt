package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.contactocero.AnclajeContactoCero
import com.example.contactocero.ContactoCeroEngine
import com.example.contactocero.MarcoAnclaje
import com.example.contactocero.ModuloAnclaje
import com.example.ui.theme.*

@Composable
fun AnclajeContactoCeroSheet(
    onDismiss: () -> Unit,
    initialMarco: MarcoAnclaje = MarcoAnclaje.PSICOLOGIA,
    initialModulo: ModuloAnclaje = ModuloAnclaje.EMERGENCIA,
    exName: String = "",
    onOpenEmdr: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var marcoActual by remember { mutableStateOf(initialMarco) }
    var moduloActual by remember { mutableStateOf(initialModulo) }
    var anclajeActual by remember(marcoActual, moduloActual) {
        mutableStateOf(ContactoCeroEngine.seleccionar(marcoActual, moduloActual))
    }

    val textoRenderizado = remember(anclajeActual, exName) {
        ContactoCeroEngine.renderizar(anclajeActual, exName)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .clickable(enabled = false) {}
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .testTag("anclaje_contacto_cero_sheet"),
                color = SoltarBackground,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    // Header Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "ANCLAJE DE CONTACTO CERO",
                                style = MaterialTheme.typography.labelMedium,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.1.sp
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("close_anclaje_sheet_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Framework Selector Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MarcoAnclaje.entries.forEach { marco ->
                            val isSelected = marco == marcoActual
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    marcoActual = marco
                                    anclajeActual = ContactoCeroEngine.seleccionar(marco, moduloActual)
                                },
                                label = {
                                    Text(
                                        text = when (marco) {
                                            MarcoAnclaje.CATOLICO -> "Fe"
                                            MarcoAnclaje.ESTOICO -> "Estoico"
                                            MarcoAnclaje.PSICOLOGIA -> "Psicología"
                                        },
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SoltarAmber,
                                    selectedLabelColor = SoltarBackground,
                                    containerColor = SoltarSurfaceElevated,
                                    labelColor = TextSecondary
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Scrollable Card Content
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                            border = BorderStroke(1.dp, SoltarBorder)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = moduloActual.tituloModulo.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SoltarSage,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = anclajeActual.titulo,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )

                                if (!anclajeActual.referencia.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = anclajeActual.referencia ?: "",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SoltarAmber,
                                        fontStyle = FontStyle.Italic
                                    )
                                }

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = SoltarBorder
                                )

                                Text(
                                    text = textoRenderizado,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    lineHeight = 24.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (onOpenEmdr != null) {
                        FilledTonalButton(
                            onClick = {
                                onDismiss()
                                onOpenEmdr(textoRenderizado)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("reprocesar_emdr_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = SoltarSurfaceElevated,
                                contentColor = SoltarAmber
                            ),
                            border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.4f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Reprocesar con EMDR Visual", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    // Action Buttons: "Otro" and "Lo sostengo"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                anclajeActual = ContactoCeroEngine.seleccionar(marcoActual, moduloActual)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("otro_anclaje_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, SoltarBorder)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = TextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Otro", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(1.4f)
                                .height(50.dp)
                                .testTag("sostener_anclaje_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarSage)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SoltarBackground,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Lo sostengo", color = SoltarBackground, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
