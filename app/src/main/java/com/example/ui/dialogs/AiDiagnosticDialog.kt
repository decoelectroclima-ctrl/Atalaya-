package com.example.ui.dialogs

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ai.OnDeviceLlmEngine
import com.example.ai.OnDeviceModelManager
import com.example.ai.SoltarAiEngine
import com.example.ai.SoltarUserContext
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiDiagnosticDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val modelState by OnDeviceModelManager.modelState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var testInput by remember { mutableStateOf("Siento un impulso fuerte de escribirle a mi ex hoy.") }
    var isRunningTest by remember { mutableStateOf(false) }
    var testResult by remember { mutableStateOf<String?>(null) }
    var testLatencyMs by remember { mutableLongStateOf(0L) }
    var testEngineType by remember { mutableStateOf("") }
    var testError by remember { mutableStateOf<String?>(null) }

    val modelFile = OnDeviceModelManager.getModelFile(context)
    val fileExists = modelFile.exists()
    val fileSizeMb = if (fileExists) modelFile.length().toDouble() / (1024.0 * 1024.0) else 0.0
    val isEngineReady = OnDeviceLlmEngine.isReady()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .testTag("ai_diagnostic_dialog"),
            containerColor = SoltarBackground,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "DIAGNÓSTICO REAL DE IA",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Auditoría de Inferencia Local y MediaPipe",
                                style = MaterialTheme.typography.bodySmall,
                                color = SoltarAmber,
                                fontSize = 11.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = TextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SoltarSurfaceElevated
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. Tarjeta de Estado del Motor en Tiempo Real
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, if (isEngineReady) SoltarSage else SoltarAmber)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when {
                                                    isEngineReady -> SoltarSage
                                                    modelState is OnDeviceModelManager.ModelState.Downloading -> SoltarAmber
                                                    else -> UrgeAlertRed
                                                }
                                            )
                                    )
                                    Text(
                                        text = "Estado del Motor Local",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Surface(
                                    color = if (isEngineReady) SoltarSage.copy(alpha = 0.15f) else SoltarSurfaceHighlight,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, if (isEngineReady) SoltarSage else SoltarBorderSubtle)
                                ) {
                                    Text(
                                        text = if (isEngineReady) "MEDIA PIPE ACTIVO" else "MODO CONTINGENCIA",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (isEngineReady) SoltarSage else SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            HorizontalDivider(color = SoltarBorderSubtle)

                            // Detalles técnicos
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                DetailRow("Modelo Arquitectura", "Gemma 3 (270M IT Q8)")
                                DetailRow("Motor de Inferencia", "Google MediaPipe LLM Tasks")
                                DetailRow(
                                    "Archivo Local",
                                    if (fileExists) "${"%.1f".format(fileSizeMb)} MB (${modelFile.name})" else "No encontrado"
                                )
                                DetailRow(
                                    "Estado de Ciclo",
                                    when (modelState) {
                                        is OnDeviceModelManager.ModelState.Ready -> "Conectado y Listo"
                                        is OnDeviceModelManager.ModelState.Downloading -> "Descargando (${((modelState as OnDeviceModelManager.ModelState.Downloading).progress * 100).toInt()}%)"
                                        is OnDeviceModelManager.ModelState.Disconnected -> "Desconectado por el usuario"
                                        is OnDeviceModelManager.ModelState.NotDownloaded -> "No descargado"
                                        is OnDeviceModelManager.ModelState.Error -> "Error: ${(modelState as OnDeviceModelManager.ModelState.Error).message}"
                                    }
                                )
                                DetailRow(
                                    "Privacidad y Red",
                                    "100% On-Device (0 bytes enviados a servidores externos)"
                                )
                            }
                        }
                    }
                }

                // 2. Banco de Pruebas en Vivo (Live Test Bench)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Speed, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                                Text(
                                    text = "BANCO DE PRUEBAS EN TIEMPO REAL",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "Envía un mensaje de prueba para verificar la generación de tokens, el tiempo de latencia y la coherencia clínica.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )

                            // Sugerencias rápidas
                            Text("Sugerencias de prueba rápida:", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                QuickPromptChip(
                                    text = "Impulso de escribir",
                                    onClick = { testInput = "Siento un impulso muy fuerte de escribir a mi ex en este momento." }
                                )
                                QuickPromptChip(
                                    text = "Rumiación",
                                    onClick = { testInput = "¿Por qué me reemplazó tan rápido? No puedo dejar de pensar en eso." }
                                )
                                QuickPromptChip(
                                    text = "Saludo",
                                    onClick = { testInput = "Hola Atalaya, ¿cómo estás hoy?" }
                                )
                            }

                            OutlinedTextField(
                                value = testInput,
                                onValueChange = { testInput = it },
                                label = { Text("Mensaje de prueba") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = SoltarAmber,
                                    unfocusedBorderColor = SoltarBorder,
                                    focusedLabelColor = SoltarAmber
                                )
                            )

                            Button(
                                onClick = {
                                    if (testInput.isNotBlank() && !isRunningTest) {
                                        isRunningTest = true
                                        testResult = null
                                        testError = null
                                        coroutineScope.launch {
                                            val start = System.currentTimeMillis()
                                            try {
                                                val userContext = viewModel.buildUserPersonalizationContext()
                                                val response = SoltarAiEngine.generateResponse(
                                                    userMessage = testInput,
                                                    conversationHistory = emptyList(),
                                                    framework = uiState.preferredFramework,
                                                    userContext = userContext,
                                                    context = context
                                                )
                                                val end = System.currentTimeMillis()
                                                testLatencyMs = end - start
                                                testResult = response.replyText
                                                testEngineType = if (isEngineReady) "MediaPipe LLM (Gemma 3 On-Device)" else "Motor Clínico Determinado (Local)"
                                            } catch (e: Exception) {
                                                testError = e.localizedMessage ?: "Error desconocido en inferencia"
                                            } finally {
                                                isRunningTest = false
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isRunningTest && testInput.isNotBlank(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                            ) {
                                if (isRunningTest) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        color = Color.Black,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Generando inferencia...", color = Color.Black, fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Ejecutar Prueba de Inferencia", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Resultado de la prueba
                            if (testResult != null) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = SoltarSurface,
                                    border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "RESPUESTA GENERADA",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = SoltarSage,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "⏱ $testLatencyMs ms • $testEngineType",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = SoltarAmber,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 10.sp
                                            )
                                        }
                                        Text(
                                            text = testResult ?: "",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextPrimary,
                                            fontSize = 12.sp,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }

                            if (testError != null) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    color = SoltarSurface,
                                    border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("ERROR EN LA PRUEBA", color = UrgeAlertRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                        Text(testError ?: "", color = TextSecondary, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Acciones de Mantenimiento y Reinstalación
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Build, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                                Text(
                                    text = "MANTENIMIENTO DEL MODELO",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "Si experimentas lentitud o fallos, puedes forzar una reinstalación limpia sin duplicar archivos.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        OnDeviceModelManager.reinstallModel(context)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SoltarAmber),
                                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.6f))
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Reinstalar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                if (isEngineReady) {
                                    OutlinedButton(
                                        onClick = {
                                            OnDeviceModelManager.disconnectModel(context)
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                                        border = BorderStroke(1.dp, SoltarBorder)
                                    ) {
                                        Icon(Icons.Default.PowerSettingsNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Desconectar", fontSize = 11.sp)
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = {
                                            OnDeviceModelManager.connectModel(context)
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SoltarSage),
                                        border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.6f))
                                    ) {
                                        Icon(Icons.Default.Power, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Reconectar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = {
                                    val log = OnDeviceLlmEngine.readDiagnosticLog(context)
                                    val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(android.content.Intent.EXTRA_TEXT, log)
                                    }
                                    context.startActivity(android.content.Intent.createChooser(shareIntent, "Compartir registro de diagnóstico"))
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                                border = BorderStroke(1.dp, SoltarBorder)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = SoltarAmber)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Compartir registro de diagnóstico", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 12.sp)
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    }
}

@Composable
private fun QuickPromptChip(text: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = SoltarSurfaceHighlight,
        border = BorderStroke(1.dp, SoltarBorderSubtle),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = SoltarAmber,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
