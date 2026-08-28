package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun SupportHubComponent(
    viewModel: SoltarViewModel,
    modifier: Modifier = Modifier
) {
    var redFlagText by remember { mutableStateOf("") }
    var postText by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SoltarSurface),
            border = BorderStroke(1.dp, SoltarBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = SoltarAmber)
                    Text(
                        text = "CENTRO DE APOYO Y SEGURIDAD",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // B1: Conversation Analyzer
                Button(
                    onClick = { viewModel.openConversationAnalyzer() },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarSurfaceElevated),
                    border = BorderStroke(1.dp, SoltarAmber)
                ) {
                    Icon(Icons.Default.Psychology, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Analizador de Conversaciones (B1)", color = SoltarAmber, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // B2: Red Flags Manager
                Text(
                    text = "B2 • MIS RED FLAGS (Señales de Alerta)",
                    style = MaterialTheme.typography.titleSmall,
                    color = UrgeAlertRed,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Registra conductas o promesas incumplidas para recordarlas cuando aparezca el impulso o la idealización.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = redFlagText,
                    onValueChange = { redFlagText = it },
                    label = { Text("Ej: Me culpabilizaba de sus reacciones...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = UrgeAlertRed,
                        unfocusedBorderColor = SoltarBorder,
                        focusedLabelColor = UrgeAlertRed,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (redFlagText.isNotBlank()) {
                            viewModel.addRedFlag(redFlagText.trim())
                            redFlagText = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Añadir Red Flag", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                val redFlags by viewModel.redFlags.collectAsState()
                if (redFlags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    redFlags.forEach { flag ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = UrgeAlertBackground),
                            border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Flag, contentDescription = null, tint = UrgeAlertRed, modifier = Modifier.size(16.dp))
                                    Text(flag.reason, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontSize = 13.sp)
                                }
                                IconButton(
                                    onClick = { viewModel.removeRedFlag(flag) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar Red Flag", tint = TextMuted, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // B3: Peer Support / Muro Anónimo
                Text(
                    text = "B3 • MURO ANÓNIMO DE APOYO ENTRE PARES",
                    style = MaterialTheme.typography.titleSmall,
                    color = SoltarSage,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Muro anónimo local y comunitario (Sin perfiles, nombres ni datos personales — Almacenamiento seguro local cifrado). Comparte logros (ej. 'Hoy he llegado a 30 días') o apoya con un ❤️.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = postText,
                    onValueChange = { postText = it },
                    label = { Text("Comparte anónimamente (ej. Hoy completé 14 días)...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SoltarSage,
                        unfocusedBorderColor = SoltarBorder,
                        focusedLabelColor = SoltarSage,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        if (postText.isNotBlank()) {
                            viewModel.addPeerSupportPost(postText.trim())
                            postText = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarSage)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = SoltarBackground, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Publicar Anónimamente", color = SoltarBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                val posts by viewModel.peerSupportPosts.collectAsState()
                if (posts.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    posts.forEach { post ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                            border = BorderStroke(1.dp, SoltarBorder)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Anónimo • " + java.text.SimpleDateFormat("dd MMM, HH:mm", java.util.Locale.getDefault()).format(java.util.Date(post.timestamp)),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = post.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(
                                        onClick = { viewModel.likePeerSupportPost(post.id) },
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text("❤️ ${post.likes}", color = UrgeAlertRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
