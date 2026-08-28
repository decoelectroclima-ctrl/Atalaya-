package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ai.ConversationAnalyzerManager
import com.example.ai.SoltarAiEngine
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ConversationAnalyzerDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    var conversationText by remember { mutableStateOf("") }
    var analysisResult by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SoltarBackground)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }
                    Text(
                        text = "ANALIZADOR DE CONVERSACIONES (B1)",
                        style = MaterialTheme.typography.labelMedium,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = SoltarAmber)
                            Text("Detección de Patrones y Dinámicas", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Pega a continuación el texto de la conversación. El motor de IA analizará de forma objetiva buscando manipulación, gaslighting, invalidación o control, diferenciando hechos observables de interpretaciones sin emitir diagnósticos clínicos.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = conversationText,
                            onValueChange = { conversationText = it },
                            label = { Text("Texto de la conversación...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorder,
                                focusedLabelColor = SoltarAmber,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (conversationText.isBlank()) {
                                    errorMessage = "Por favor, introduce texto para analizar."
                                    return@Button
                                }
                                isLoading = true
                                errorMessage = null
                                analysisResult = null
                                scope.launch {
                                    try {
                                        val analyzer = ConversationAnalyzerManager(SoltarAiEngine, viewModel.repository)
                                        val result = analyzer.analyzeConversation(conversationText)
                                        analysisResult = result.rawAnalysis
                                    } catch (e: Exception) {
                                        errorMessage = "Error en el análisis: ${e.localizedMessage}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = SoltarBackground)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Analizando patrones...", color = SoltarBackground, fontWeight = FontWeight.Bold)
                            } else {
                                Text("Iniciar Análisis de Conversación", color = SoltarBackground, fontWeight = FontWeight.Bold)
                            }
                        }

                        errorMessage?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(it, color = UrgeAlertRed, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                if (analysisResult != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, SoltarSage)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Resultado del Análisis:",
                                style = MaterialTheme.typography.titleMedium,
                                color = SoltarSage,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = analysisResult ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
