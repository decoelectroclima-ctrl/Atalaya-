package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BeginnerLetterEntity
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun BeginnerLetterDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val letters by viewModel.beginnerLetters.collectAsState()
    var letterContentInput by remember { mutableStateOf("") }
    var letterTitleInput by remember { mutableStateOf("Desde el otro lado del desfiladero") }
    var authorStageDays by remember { mutableStateOf(30) }

    // Seed preset peer letters if empty
    LaunchedEffect(letters) {
        if (letters.isEmpty()) {
            viewModel.saveBeginnerLetter(
                BeginnerLetterEntity(
                    authorStageDays = 45,
                    targetStageDays = 3,
                    content = "Sé exactamente lo que sientes hoy. Te duele hasta respirar y parece que el mundo se ha detenido. Te prometo, por mi vida entera, que el dolor agudo cede. No tienes que olvidar hoy, solo tienes que sostenerte 24 horas más. Estás haciendo lo correcto.",
                    encouragementTitle = "De alguien en el Día 45 al Día 3",
                    isIncomingFromPeer = true
                )
            )
            viewModel.saveBeginnerLetter(
                BeginnerLetterEntity(
                    authorStageDays = 90,
                    targetStageDays = 3,
                    content = "Hoy crees que nunca volverás a sonreír sin culpa. Es una mentira que tu mente herida te cuenta. En unas semanas mirarás atrás y verás que este abismo era el inicio de tu soberanía.",
                    encouragementTitle = "Desde el Día 90",
                    isIncomingFromPeer = true
                )
            )
        }
    }

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
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(
                            text = "Cartas al Inicio del Camino",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Convierte tu dolor pasado en apoyo real para otro",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextPrimary)
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Escribe una carta para quien empieza hoy",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            OutlinedTextField(
                                value = letterTitleInput,
                                onValueChange = { letterTitleInput = it },
                                label = { Text("Título de la carta") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SoltarAmber,
                                    unfocusedBorderColor = SoltarBorder
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            OutlinedTextField(
                                value = letterContentInput,
                                onValueChange = { letterContentInput = it },
                                label = { Text("¿Qué te hubiera gustado escuchar en el Día 3?") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SoltarAmber,
                                    unfocusedBorderColor = SoltarBorder
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Button(
                                onClick = {
                                    if (letterContentInput.isNotBlank()) {
                                        viewModel.saveBeginnerLetter(
                                            BeginnerLetterEntity(
                                                authorStageDays = authorStageDays,
                                                targetStageDays = 3,
                                                content = letterContentInput,
                                                encouragementTitle = letterTitleInput,
                                                isIncomingFromPeer = false
                                            )
                                        )
                                        letterContentInput = ""
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Enviar Carta al Ecosistema", color = SoltarBackground, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Cartas recibidas de quienes ya recorrieron este camino:",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(letters) { letter ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, if (letter.isIncomingFromPeer) SoltarSage.copy(alpha = 0.5f) else SoltarAmber.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = letter.encouragementTitle,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Surface(
                                    color = if (letter.isIncomingFromPeer) SoltarSage.copy(alpha = 0.2f) else SoltarAmber.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Desde el Día ${letter.authorStageDays}",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (letter.isIncomingFromPeer) SoltarSage else SoltarAmber,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Text(
                                text = letter.content,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SoltarSurfaceElevated),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cerrar", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}
