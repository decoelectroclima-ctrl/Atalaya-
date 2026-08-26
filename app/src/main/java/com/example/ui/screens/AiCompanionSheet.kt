package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun AiCompanionDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val aiMessages by viewModel.aiMessages.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.ime,
            containerColor = SoltarBackground,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoltarSurface)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("ai_dialog_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ACOMPAÑANTE ADRIANA",
                            style = MaterialTheme.typography.titleSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Presencia sobria • Sin juicios ni falsas promesas",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }

                    IconButton(
                        onClick = { viewModel.clearAiMemory() },
                        modifier = Modifier.testTag("ai_dialog_clear_memory")
                    ) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Reiniciar conversación", tint = TextSecondary)
                    }
                }
            },
            bottomBar = {
                Surface(
                    color = SoltarSurface,
                    tonalElevation = 8.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder),
                    modifier = Modifier.imePadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
                        OutlinedTextField(
                            value = uiState.aiInputMessage,
                            onValueChange = viewModel::setAiInputMessage,
                            placeholder = {
                                Text(
                                    "Escribe lo que sientes o lo que te cuesta soltar...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("ai_input_field")
                                .focusRequester(focusRequester),
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = SoltarAmber
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        LaunchedEffect(Unit) {
                            focusRequester.requestFocus()
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { viewModel.sendAiMessage() },
                            enabled = uiState.aiInputMessage.isNotBlank() && !uiState.isAiTyping,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (uiState.aiInputMessage.isNotBlank() && !uiState.isAiTyping) SoltarAmber else SoltarSurfaceElevated)
                                .testTag("ai_send_button")
                        ) {
                            if (uiState.isAiTyping) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = SoltarAmber,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Enviar",
                                    tint = if (uiState.aiInputMessage.isNotBlank()) SoltarBackground else TextMuted
                                )
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 20.dp)
            ) {
                // Grounding & Clinical Disclaimer Card
                item {
                    var isDisclaimerExpanded by remember { mutableStateOf(false) }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = SoltarAmber,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Marco: ${uiState.preferredFramework.title}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                IconButton(
                                    onClick = { isDisclaimerExpanded = !isDisclaimerExpanded },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        if (isDisclaimerExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = "Expandir aviso clínico",
                                        tint = TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "ADRIANA acompaña tu proceso desde el rigor y la compasión, sin validar falsas ilusiones ni alimentar rumiaciones.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )

                            if (isDisclaimerExpanded) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = SoltarSurface,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
                                ) {
                                    Text(
                                        text = "⚖️ Aviso ético y clínico: ADRIANA es una herramienta de autorregulación reflexiva. No proporciona diagnósticos médicos ni sustituye la psicoterapia clínica o la atención de emergencias (024 / 112 / 988).",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextMuted,
                                        modifier = Modifier.padding(10.dp),
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                items(aiMessages) { msg ->
                    val isUser = msg.sender == "user"
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 16.dp,
                                topEnd = 16.dp,
                                bottomStart = if (isUser) 16.dp else 4.dp,
                                bottomEnd = if (isUser) 4.dp else 16.dp
                            ),
                            color = if (isUser) SoltarAmber.copy(alpha = 0.18f) else SoltarSurfaceElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isUser) SoltarAmber.copy(alpha = 0.4f) else SoltarBorder
                            ),
                            modifier = Modifier.widthIn(max = 320.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                if (!isUser) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Psychology,
                                            contentDescription = null,
                                            tint = SoltarAmber,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "ADRIANA",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SoltarAmber,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                }

                                Text(
                                    text = msg.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    lineHeight = 22.sp
                                )

                                if (msg.detectedRumination) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = UrgeAlertRed.copy(alpha = 0.15f),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.3f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Loop,
                                                contentDescription = null,
                                                tint = UrgeAlertRed,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Text(
                                                text = "Alerta de rumiación detectada: ¿Podemos enfocarnos en lo que sí puedes controlar ahora?",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextPrimary,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }

                                msg.suggestedAction?.let { action ->
                                    if (action.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = SoltarSage.copy(alpha = 0.15f),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, SoltarSage.copy(alpha = 0.3f))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(8.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.CheckCircleOutline,
                                                    contentDescription = null,
                                                    tint = SoltarSage,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Text(
                                                    text = "Paso sugerido: $action",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = SoltarSageLight,
                                                    fontSize = 11.sp
                                                )
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
