package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun AiCompanionDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    // Natively handle Android hardware & gesture back button
    BackHandler(onBack = onDismiss)

    val uiState by viewModel.uiState.collectAsState()
    val aiMessages by viewModel.aiMessages.collectAsState()
    val listState = rememberLazyListState()

    // Scroll to latest message whenever new messages arrive
    LaunchedEffect(aiMessages.size) {
        if (aiMessages.isNotEmpty()) {
            listState.animateScrollToItem(aiMessages.size)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = SoltarBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Surface(
                color = SoltarSurface,
                tonalElevation = 4.dp,
                border = BorderStroke(1.dp, SoltarBorderSubtle)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("ai_dialog_close")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = SoltarAmber
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "COACH ATALAYA",
                            style = MaterialTheme.typography.titleSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SoltarAmber.copy(alpha = 0.15f),
                            border = BorderStroke(0.5.dp, SoltarAmber.copy(alpha = 0.5f)),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = "Enfoque: ${uiState.preferredFramework.title.uppercase()}",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 9.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.clearAiMemory() },
                        modifier = Modifier.testTag("ai_dialog_clear_memory")
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Reiniciar conversación",
                            tint = TextSecondary
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = SoltarSurface,
                tonalElevation = 8.dp,
                border = BorderStroke(1.dp, SoltarBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    // Quick Prompt Suggestion Chips tailored to framework
                    val suggestions = when (uiState.preferredFramework) {
                        com.example.data.SoltarFramework.ESTOICO -> listOf(
                            "Siento el impulso de escribirle",
                            "¿Cómo aplicar la dicotomía del control hoy?",
                            "Ayúdame a salir de la rumiación",
                            "Tengo rabia por la injusticia de la ruptura",
                            "Quiero fortalecer mi ciudadela interior"
                        )
                        com.example.data.SoltarFramework.CATOLICO -> listOf(
                            "Siento el impulso de buscarle",
                            "¿Cómo custodiar mi corazón hoy?",
                            "Siento dolor y desamparo en este desierto",
                            "Ayúdame a perdonar y soltar el rencor",
                            "Dame una reflexión bíblica para la angustia"
                        )
                        com.example.data.SoltarFramework.PSICOLOGIA_MODERNA -> listOf(
                            "Siento el impulso de escribirle",
                            "Tengo abstinencia y angustia en el pecho",
                            "Siento que caigo en la idealización",
                            "¿Cómo sostener el contacto cero hoy?",
                            "Siento culpa por cómo terminó todo"
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestions.forEach { prompt ->
                            Surface(
                                onClick = { viewModel.setAiInputMessage(prompt) },
                                shape = RoundedCornerShape(20.dp),
                                color = SoltarSurfaceElevated,
                                border = BorderStroke(1.dp, SoltarBorder)
                            ) {
                                Text(
                                    text = prompt,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SoltarAmber,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    // Input Text Field and Send Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = uiState.aiInputMessage,
                            onValueChange = viewModel::setAiInputMessage,
                            placeholder = {
                                Text(
                                    "Escribe aquí lo que sientes...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMuted
                                )
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("ai_input_field"),
                            maxLines = 4,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SoltarAmber,
                                unfocusedBorderColor = SoltarBorder,
                                focusedContainerColor = SoltarSurfaceElevated,
                                unfocusedContainerColor = SoltarSurfaceElevated,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = SoltarAmber
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { viewModel.sendAiMessage() },
                            enabled = uiState.aiInputMessage.isNotBlank() && !uiState.isAiTyping,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    if (uiState.aiInputMessage.isNotBlank() && !uiState.isAiTyping) SoltarAmber
                                    else SoltarSurfaceElevated
                                )
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
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 12.dp)
        ) {
            // Grounding & Clinical Disclaimer Card
            item {
                var isDisclaimerExpanded by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = BorderStroke(1.dp, SoltarBorder)
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
                                border = BorderStroke(1.dp, SoltarBorder)
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

            // Chat Bubbles
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
                        border = BorderStroke(
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
                                    border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.3f))
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
                                        border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.35f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.CheckCircleOutline,
                                                contentDescription = null,
                                                tint = SoltarSage,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = "Paso sugerido: $action",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Medium,
                                                fontSize = 12.sp,
                                                lineHeight = 16.sp
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
