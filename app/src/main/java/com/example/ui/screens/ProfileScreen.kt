package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.ai.OnDeviceLlmEngine
import com.example.audio.SoltarSoundManager
import com.example.data.SoltarFramework
import com.example.data.SubscriptionPlan
import com.example.data.UserEntitlements
import com.example.ui.SoltarViewModel
import com.example.ui.auth.AuthViewModel
import com.example.ui.theme.*
import com.example.ui.components.RelationshipContextSection

@Composable
fun ProfileScreen(
    viewModel: SoltarViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val identityGoals by viewModel.identityGoals.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val triggerEvents by viewModel.triggerEvents.collectAsState()
    val relapses by viewModel.relapses.collectAsState()

    val relapsePatternAnalysis = remember(triggerEvents) {
        OnDeviceLlmEngine.analyzeRelapsePatterns(triggerEvents)
    }

    val entitlements = remember(settings) { UserEntitlements.fromSettings(settings) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteAccountConfirmDialog by remember { mutableStateOf(false) }
    var showMandatoryJournalTimeDialog by remember { mutableStateOf(false) }
    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = { Text("¿Restablecer datos locales?", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Esta acción borrará tus registros locales y el historial de la IA, garantizando tu privacidad y tu derecho al olvido.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.fullDataReset()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed)
                ) {
                    Text("Borrar Todo", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetConfirmDialog = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = SoltarSurfaceElevated,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showDeleteAccountConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountConfirmDialog = false },
            title = { Text("¿Eliminar cuenta y perfil?", color = UrgeAlertRed, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Se borrarán tus credenciales de acceso, tu red de apoyo y todos tus registros asociados. Volverás a una sesión anónima.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        authViewModel.deleteAccount()
                        showDeleteAccountConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed)
                ) {
                    Text("Eliminar Cuenta", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteAccountConfirmDialog = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = SoltarSurfaceElevated,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Modal para Configurar Hora de Recordatorio Diario
    if (uiState.isTimePickerDialogVisible) {
        var tempHour by remember(uiState.reminderHourInput) { mutableIntStateOf(uiState.reminderHourInput) }
        var tempMinute by remember(uiState.reminderMinuteInput) { mutableIntStateOf(uiState.reminderMinuteInput) }

        AlertDialog(
            onDismissRequest = { viewModel.toggleReminderTimeDialog(false) },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Alarm, contentDescription = null, tint = SoltarAmber)
                    Text("Hora del Recordatorio", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Selecciona la hora a la que prefieres recibir tu cita inspiradora y llamado al check-in diario:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Digital Clock Display Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = SoltarSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = String.format(java.util.Locale.getDefault(), "%02d:%02d", tempHour, tempMinute),
                                fontSize = 38.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoltarAmber,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = if (tempHour in 5..11) "🌅 Mañana"
                                else if (tempHour in 12..18) "🌤️ Tarde"
                                else if (tempHour in 19..22) "🌙 Noche (Recomendado)"
                                else "🌌 Madrugada",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Steppers for Hour & Minute
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Hour Stepper
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = SoltarSurfaceElevated,
                            border = BorderStroke(1.dp, SoltarBorderSubtle)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Hora", fontSize = 11.sp, color = TextSecondary)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    IconButton(
                                        onClick = { tempHour = (tempHour - 1 + 24) % 24 },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                    Text(
                                        text = String.format(java.util.Locale.getDefault(), "%02d", tempHour),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = TextPrimary
                                    )
                                    IconButton(
                                        onClick = { tempHour = (tempHour + 1) % 24 },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                }
                            }
                        }

                        // Minute Stepper
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = SoltarSurfaceElevated,
                            border = BorderStroke(1.dp, SoltarBorderSubtle)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Minuto", fontSize = 11.sp, color = TextSecondary)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    IconButton(
                                        onClick = { tempMinute = (tempMinute - 5 + 60) % 60 },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                    Text(
                                        text = String.format(java.util.Locale.getDefault(), "%02d", tempMinute),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = TextPrimary
                                    )
                                    IconButton(
                                        onClick = { tempMinute = (tempMinute + 5) % 60 },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Opciones rápidas:", fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))

                    // Quick Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SuggestionChip(
                            onClick = { tempHour = 8; tempMinute = 30 },
                            label = { Text("08:30", fontSize = 11.sp) }
                        )
                        SuggestionChip(
                            onClick = { tempHour = 14; tempMinute = 0 },
                            label = { Text("14:00", fontSize = 11.sp) }
                        )
                        SuggestionChip(
                            onClick = { tempHour = 21; tempMinute = 0 },
                            label = { Text("21:00 ⭐", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        )
                        SuggestionChip(
                            onClick = { tempHour = 22; tempMinute = 30 },
                            label = { Text("22:30", fontSize = 11.sp) }
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveReminderSchedule(tempHour, tempMinute)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                ) {
                    Text("Guardar y Programar", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.toggleReminderTimeDialog(false) }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = SoltarSurfaceElevated,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Dialog para Hora de Diario Obligatorio
    if (showMandatoryJournalTimeDialog) {
        var tempHour by remember(uiState.mandatoryJournalHourInput) { mutableIntStateOf(uiState.mandatoryJournalHourInput) }
        var tempMinute by remember(uiState.mandatoryJournalMinuteInput) { mutableIntStateOf(uiState.mandatoryJournalMinuteInput) }

        AlertDialog(
            onDismissRequest = { showMandatoryJournalTimeDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = SoltarTerracotta)
                    Text("Hora del Diario Obligatorio", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Configura la hora fija en la que se bloqueará la app si no has completado tu entrada de diario diario:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = SoltarSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = String.format(java.util.Locale.getDefault(), "%02d:%02d", tempHour, tempMinute),
                                fontSize = 38.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SoltarTerracotta
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Hora: $tempHour", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Row {
                            OutlinedButton(onClick = { if (tempHour > 0) tempHour-- }) { Text("-") }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(onClick = { if (tempHour < 23) tempHour++ }) { Text("+") }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Minutos: $tempMinute", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Row {
                            OutlinedButton(onClick = { if (tempMinute >= 15) tempMinute -= 15 else tempMinute = 0 }) { Text("-15") }
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedButton(onClick = { if (tempMinute <= 45) tempMinute += 15 else tempMinute = 0 }) { Text("+15") }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateMandatoryJournalTime(tempHour, tempMinute)
                        showMandatoryJournalTimeDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarTerracotta)
                ) {
                    Text("Guardar Hora", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showMandatoryJournalTimeDialog = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = SoltarSurfaceElevated,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // Dialog para Notificación Personalizada
    if (uiState.isCustomNotificationDialogVisible) {
        var tempHour by remember(uiState.customNotificationHourInput) { mutableIntStateOf(uiState.customNotificationHourInput) }
        var tempMinute by remember(uiState.customNotificationMinuteInput) { mutableIntStateOf(uiState.customNotificationMinuteInput) }
        var tempTitle by remember(uiState.customNotificationTitleInput) { mutableStateOf(uiState.customNotificationTitleInput) }
        var tempMessage by remember(uiState.customNotificationMessageInput) { mutableStateOf(uiState.customNotificationMessageInput) }

        AlertDialog(
            onDismissRequest = { viewModel.dismissCustomNotificationDialog() },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(
                        imageVector = if (uiState.editingCustomNotificationId != null) Icons.Default.EditNotifications else Icons.Default.AlarmAdd,
                        contentDescription = null,
                        tint = SoltarAmber
                    )
                    Text(
                        if (uiState.editingCustomNotificationId != null) "Editar Recordatorio" else "Nuevo Recordatorio Programable",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Configura la hora y el mensaje empático para acompañar tu proceso:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    // Reloj Digital
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = SoltarSurface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = String.format(java.util.Locale.getDefault(), "%02d:%02d", tempHour, tempMinute),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoltarAmber,
                                letterSpacing = 2.sp
                            )
                            val timePeriod = if (tempHour in 5..11) "🌅 Mañana"
                            else if (tempHour in 12..18) "🌤️ Tarde"
                            else if (tempHour in 19..22) "🌙 Noche"
                            else "🌌 Madrugada"
                            Text(
                                text = timePeriod,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Selectores de Hora y Minuto
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Hora
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = SoltarSurface,
                            border = BorderStroke(1.dp, SoltarBorderSubtle)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Hora", fontSize = 11.sp, color = TextSecondary)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { tempHour = (tempHour - 1 + 24) % 24 },
                                        contentPadding = PaddingValues(0.dp),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                    Text(
                                        text = String.format(java.util.Locale.getDefault(), "%02d", tempHour),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = TextPrimary
                                    )
                                    OutlinedButton(
                                        onClick = { tempHour = (tempHour + 1) % 24 },
                                        contentPadding = PaddingValues(0.dp),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                }
                            }
                        }

                        // Minuto
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            color = SoltarSurface,
                            border = BorderStroke(1.dp, SoltarBorderSubtle)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Minuto", fontSize = 11.sp, color = TextSecondary)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { tempMinute = (tempMinute - 5 + 60) % 60 },
                                        contentPadding = PaddingValues(0.dp),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("-5", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                    Text(
                                        text = String.format(java.util.Locale.getDefault(), "%02d", tempMinute),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = TextPrimary
                                    )
                                    OutlinedButton(
                                        onClick = { tempMinute = (tempMinute + 5) % 60 },
                                        contentPadding = PaddingValues(0.dp),
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Text("+5", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                                    }
                                }
                            }
                        }
                    }

                    // Título
                    OutlinedTextField(
                        value = tempTitle,
                        onValueChange = { tempTitle = it },
                        label = { Text("Título o Etiqueta") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorderSubtle
                        )
                    )

                    // Mensaje
                    OutlinedTextField(
                        value = tempMessage,
                        onValueChange = { tempMessage = it },
                        label = { Text("Mensaje que verás en tu pantalla") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorderSubtle
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.setCustomNotificationHourInput(tempHour)
                        viewModel.setCustomNotificationMinuteInput(tempMinute)
                        viewModel.setCustomNotificationTitleInput(tempTitle)
                        viewModel.setCustomNotificationMessageInput(tempMessage)
                        viewModel.saveCustomNotificationFromDialog()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                ) {
                    Text("Guardar Recordatorio", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.dismissCustomNotificationDialog() }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = SoltarSurfaceElevated,
            shape = RoundedCornerShape(16.dp)
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SoltarBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 120.dp)
    ) {
        // Top Section Title
        item {
            Column {
                Text(
                    text = "IDENTIDAD Y PERFIL",
                    style = MaterialTheme.typography.labelMedium,
                    color = SoltarAmber,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Mi Espacio y Autonomía",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tu recuperación se construye paso a paso con límites claros y dignidad.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        // User Account Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_account_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Avatar Initial
                        val userName = settings?.userName?.ifBlank { "Viajero" } ?: "Viajero"
                        val userInitial = userName.firstOrNull()?.uppercase() ?: "A"
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(SoltarAmber.copy(alpha = 0.15f))
                                .border(1.5.dp, SoltarAmber, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userInitial,
                                style = MaterialTheme.typography.headlineSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = userName,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                            val userEmail = settings?.userEmail ?: ""
                            if (userEmail.isNotBlank()) {
                                Text(
                                    text = userEmail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (settings?.isLoggedIn == true) SoltarSage.copy(alpha = 0.15f) else SoltarBorderSubtle
                            ) {
                                Text(
                                    text = if (settings?.isLoggedIn == true) "● Cuenta Verificada" else "○ Modo Local Invitado",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (settings?.isLoggedIn == true) SoltarSage else TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (settings?.isLoggedIn == true) {
                            OutlinedButton(
                                onClick = { authViewModel.logout() },
                                modifier = Modifier.weight(1f).height(42.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, SoltarBorder)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Cerrar Sesión", color = TextSecondary, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = { showDeleteAccountConfirmDialog = true },
                                modifier = Modifier.weight(1f).height(42.dp),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.5f))
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = UrgeAlertRed, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Eliminar Cuenta", color = UrgeAlertRed, fontSize = 12.sp)
                            }
                        } else {
                            Button(
                                onClick = { authViewModel.openAuthDialog("LOGIN") },
                                modifier = Modifier.weight(1f).height(42.dp).testTag("profile_login_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                            ) {
                                Text("Iniciar Sesión", color = SoltarBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            OutlinedButton(
                                onClick = { authViewModel.openAuthDialog("REGISTER") },
                                modifier = Modifier.weight(1f).height(42.dp).testTag("profile_register_button"),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, SoltarAmber)
                            ) {
                                Text("Registrarse", color = SoltarAmber, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Process Counter & Streak Summary Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_streak_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                        Text(
                            text = "Estadísticas de tu Proceso",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val currentTime = System.currentTimeMillis()
                    val noContactStart = settings?.breakupDateTimestamp ?: (currentTime - (14L * 24 * 3600 * 1000))
                    val initialStartRaw = settings?.initialStartDateTimestamp ?: 0L
                    val initialStart = if (initialStartRaw > 0L) initialStartRaw else noContactStart
                    val elapsedMillis = (currentTime - noContactStart).coerceAtLeast(0L)
                    val totalAccumulatedMillis = (currentTime - initialStart).coerceAtLeast(0L)
                    val currentDays = elapsedMillis / (1000 * 3600 * 24)
                    val totalDays = totalAccumulatedMillis / (1000 * 3600 * 24)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Racha Actual", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("$currentDays días", style = MaterialTheme.typography.titleMedium, color = SoltarAmber, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Días Totales", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("$totalDays días", style = MaterialTheme.typography.titleMedium, color = SoltarSage, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Anticipated Risk Dates Calendar
        item {
            AnticipatedRiskDatesSection(viewModel = viewModel)
        }

        // Legal & Contact Section
        item {
            LegalAndContactSection(viewModel = viewModel)
        }

        // Historial de Tropiezos / Recaídas y Análisis de Patrones
        if (relapsePatternAnalysis != null || relapses.isNotEmpty() || triggerEvents.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_relapse_history_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.History,
                                contentDescription = null,
                                tint = SoltarSage,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Historial de Tropiezos y Recaídas",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Hallazgo adicional de patrones si el modelo On-Device detecta recurrencia
                        if (relapsePatternAnalysis != null) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("profile_relapse_pattern_insight"),
                                shape = RoundedCornerShape(12.dp),
                                color = SoltarAmber.copy(alpha = 0.1f),
                                border = BorderStroke(1.2.dp, SoltarAmber.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        Icons.Default.Insights,
                                        contentDescription = null,
                                        tint = SoltarAmber,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Patrón Identificado a lo Largo del Tiempo (IA On-Device)",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SoltarAmber,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 0.5.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = relapsePatternAnalysis,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextPrimary,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Text(
                            text = "Episodios registrados con enfoque compasivo: ${relapses.size} recaídas, ${triggerEvents.size} detonantes superados. Cada tropiezo es información valiosa para fortalecer tus límites conscientes, nunca un motivo de culpa.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Subscription & Monetization Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("subscription_status_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (entitlements.isPremium) SoltarSurfaceElevated else SoltarSurface
                ),
                border = BorderStroke(
                    width = if (entitlements.isPremium) 1.5.dp else 1.dp,
                    color = if (entitlements.isPremium) SoltarAmber else SoltarBorder
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
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
                                imageVector = if (entitlements.isPremium) Icons.Default.Star else Icons.Default.LockOpen,
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Plan Actual",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (entitlements.isPremium) SoltarAmber.copy(alpha = 0.15f) else SoltarSurfaceElevated,
                            border = BorderStroke(1.dp, if (entitlements.isPremium) SoltarAmber else SoltarBorderSubtle)
                        ) {
                            Text(
                                text = if (entitlements.isPremium) {
                                    if (entitlements.isTrial) "PREMIUM (7 DÍAS PRUEBA)" else "PREMIUM ACTIVO"
                                } else "ADRIANA FREE",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (entitlements.isPremium) SoltarAmber else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (entitlements.isPremium) {
                            "Tienes acceso ilimitado al Coach Recuerda, auditorías profundas, ceremonias de cartas y biorregulación somática completa."
                        } else {
                            "Acceso esencial a contador, check-in básico y 5 consultas diarias con el Coach. Mejora tu plan para acompañamiento ilimitado."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.openPaywall(SubscriptionPlan.MONTHLY) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("manage_subscription_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (entitlements.isPremium) SoltarSurfaceHighlight else SoltarAmber
                        )
                    ) {
                        Text(
                            text = if (entitlements.isPremium) "Gestionar Plan / Suscripción" else "Ver Planes • 7 Días Gratis",
                            color = if (entitlements.isPremium) SoltarAmber else SoltarBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Support Network Card (Red de Apoyo - 3 Contacts)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("support_network_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
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
                                imageVector = Icons.Default.Groups,
                                contentDescription = null,
                                tint = SoltarSage,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Red de Apoyo Emocional",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SoltarSage.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = "HASTA 3 CONTACTOS",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarSage,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "En momentos de impulso o vulnerabilidad, acudir a una persona segura rompe el aislamiento y protege tu dignidad.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Contact Slots
                    val c1Name = settings?.contact1Name ?: ""
                    val c1Phone = settings?.contact1Phone ?: ""
                    val c1Rel = settings?.contact1Relationship ?: ""

                    val c2Name = settings?.contact2Name ?: ""
                    val c2Phone = settings?.contact2Phone ?: ""
                    val c2Rel = settings?.contact2Relationship ?: ""

                    val c3Name = settings?.contact3Name ?: ""
                    val c3Phone = settings?.contact3Phone ?: ""
                    val c3Rel = settings?.contact3Relationship ?: ""

                    SupportContactItem(
                        index = 1,
                        name = c1Name,
                        phone = c1Phone,
                        relationship = c1Rel,
                        onEdit = { viewModel.openSupportContactDialog(1) },
                        onCall = { triggerPhoneCall(context, c1Phone) },
                        onSMS = { triggerSms(context, c1Phone) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SupportContactItem(
                        index = 2,
                        name = c2Name,
                        phone = c2Phone,
                        relationship = c2Rel,
                        onEdit = { viewModel.openSupportContactDialog(2) },
                        onCall = { triggerPhoneCall(context, c2Phone) },
                        onSMS = { triggerSms(context, c2Phone) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SupportContactItem(
                        index = 3,
                        name = c3Name,
                        phone = c3Phone,
                        relationship = c3Rel,
                        onEdit = { viewModel.openSupportContactDialog(3) },
                        onCall = { triggerPhoneCall(context, c3Phone) },
                        onSMS = { triggerSms(context, c3Phone) }
                    )
                }
            }
        }

        // Frameworks & Perspectives Configuration
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("framework_selector_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
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
                                imageVector = Icons.Default.Explore,
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Marco de Referencia y Enfoques",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SoltarAmber.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = uiState.preferredFramework.title.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Selecciona tu marco principal. La IA y tus reflexiones se adaptarán a su vocabulario con rigor y respeto.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 17.sp,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SoltarFramework.entries.forEach { framework ->
                            val isSelected = uiState.preferredFramework == framework
                            val borderColor = if (isSelected) SoltarAmber else SoltarBorderSubtle
                            val bgColor = if (isSelected) SoltarAmber.copy(alpha = 0.08f) else SoltarSurfaceElevated

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (!isSelected) {
                                            viewModel.setFramework(framework)
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = bgColor),
                                border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            if (!isSelected) {
                                                viewModel.setFramework(framework)
                                            }
                                        },
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = SoltarAmber,
                                            unselectedColor = TextMuted
                                        )
                                    )

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = framework.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (isSelected) SoltarAmber else TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = framework.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Sound & Preferences Settings Card
        item {
            RelationshipContextSection(viewModel, settings)
        }

        // --- Ayuda y contacto ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("help_contact_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Ayuda y contacto",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    // Reportar problema
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:support@adrianaapp.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Reportar problema - Recuerda v${BuildConfig.VERSION_NAME}")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Reportar un problema")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    // Sugerir mejora
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:support@adrianaapp.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Sugerir una mejora - Recuerda")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Sugerir una mejora")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Contactar soporte
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:support@adrianaapp.com")
                                putExtra(Intent.EXTRA_SUBJECT, "Contacto soporte - Recuerda")
                            }
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                    ) {
                        Text("Contactar con soporte", color = Color.Black)
                    }
                }
            }
        }

        // --- Ceremonias y Sabiduría (C4/C5/D3) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ceremonies_wisdom_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Sabiduría, Rituales y Cierre",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = { viewModel.toggleWisdomLibraryDialog(true) },
                        modifier = Modifier.fillMaxWidth().testTag("open_wisdom_library_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.MenuBook, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Biblioteca de Sabiduría Viva (C4)", color = TextPrimary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { viewModel.toggleClosingRitualDialog(true) },
                        modifier = Modifier.fillMaxWidth().testTag("open_closing_ritual_button"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.SelfImprovement, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ritual de Cierre Guiado (C5)", color = TextPrimary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.toggleVoluntaryExitDialog(true) },
                        modifier = Modifier.fillMaxWidth().testTag("voluntary_exit_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed.copy(alpha = 0.2f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = UrgeAlertRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cerrar mi proceso y concluir (D3)", color = UrgeAlertRed, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        
        // --- Info Aplicación ---
        item {
            Text(
                text = "Versión: ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("theme_and_appearance_card"),
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = when (uiState.themeMode.uppercase()) {
                                    "LIGHT" -> Icons.Default.LightMode
                                    "DARK" -> Icons.Default.DarkMode
                                    else -> Icons.Default.BrightnessAuto
                                },
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Apariencia & Modo Visual",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SoltarAmber.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = when (uiState.themeMode.uppercase()) {
                                    "LIGHT" -> "MODO CLARO"
                                    "DARK" -> "MODO OSCURO"
                                    else -> "SISTEMA"
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Selecciona la paleta que mejor acompañe tu lectura y momento del día:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3 Theme Option Selector Tiles
                    val currentMode = uiState.themeMode.uppercase()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 1. Light Mode Tile (Default)
                        val isLightSelected = currentMode == "LIGHT"
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setThemeMode("LIGHT") }
                                .testTag("theme_selector_light"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isLightSelected) SoltarSurfaceHighlight else SoltarSurfaceElevated,
                            border = BorderStroke(
                                if (isLightSelected) 1.5.dp else 1.dp,
                                if (isLightSelected) SoltarAmber else SoltarBorderSubtle
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isLightSelected) SoltarAmber.copy(alpha = 0.2f) else SoltarSurface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.LightMode,
                                        contentDescription = null,
                                        tint = if (isLightSelected) SoltarAmber else TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Claro",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isLightSelected) SoltarAmber else TextPrimary,
                                    fontWeight = if (isLightSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Porcelana",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // 2. Dark Mode Tile
                        val isDarkSelected = currentMode == "DARK"
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setThemeMode("DARK") }
                                .testTag("theme_selector_dark"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isDarkSelected) SoltarSurfaceHighlight else SoltarSurfaceElevated,
                            border = BorderStroke(
                                if (isDarkSelected) 1.5.dp else 1.dp,
                                if (isDarkSelected) SoltarAmber else SoltarBorderSubtle
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isDarkSelected) SoltarAmber.copy(alpha = 0.2f) else SoltarSurface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.DarkMode,
                                        contentDescription = null,
                                        tint = if (isDarkSelected) SoltarAmber else TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Oscuro",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isDarkSelected) SoltarAmber else TextPrimary,
                                    fontWeight = if (isDarkSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Obsidiana",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }

                        // 3. System Mode Tile
                        val isSystemSelected = currentMode == "SYSTEM"
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.setThemeMode("SYSTEM") }
                                .testTag("theme_selector_system"),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSystemSelected) SoltarSurfaceHighlight else SoltarSurfaceElevated,
                            border = BorderStroke(
                                if (isSystemSelected) 1.5.dp else 1.dp,
                                if (isSystemSelected) SoltarAmber else SoltarBorderSubtle
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (isSystemSelected) SoltarAmber.copy(alpha = 0.2f) else SoltarSurface),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.BrightnessAuto,
                                        contentDescription = null,
                                        tint = if (isSystemSelected) SoltarAmber else TextMuted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Automático",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSystemSelected) SoltarAmber else TextPrimary,
                                    fontWeight = if (isSystemSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "Sistema",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = SoltarBorderSubtle)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Direct Quick Toggle (Modo Claro / Modo Oscuro)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = if (currentMode == "LIGHT") Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = SoltarAmber
                            )
                            Column {
                                Text(
                                    text = if (currentMode == "LIGHT") "Modo Claro Activado" else "Modo Oscuro Activado",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (currentMode == "LIGHT") "Fondo porcelana y calidez dorada" else "Fondo obsidiana y sosiego visual",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Switch(
                            checked = currentMode == "LIGHT",
                            onCheckedChange = { isLight ->
                                viewModel.setThemeMode(if (isLight) "LIGHT" else "DARK")
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SoltarBackground,
                                checkedTrackColor = SoltarAmber,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = SoltarSurfaceElevated
                            ),
                            modifier = Modifier.testTag("quick_light_mode_switch")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = SoltarBorderSubtle)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Sound Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = if (uiState.isSoundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                                contentDescription = null,
                                tint = if (uiState.isSoundEnabled) SoltarAmber else TextMuted
                            )
                            Column {
                                Text("Efectos de sonido y campanas", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                                Text("Campana tibetana y pulsos acústicos", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                            }
                        }

                        Switch(
                            checked = uiState.isSoundEnabled,
                            onCheckedChange = { viewModel.toggleSoundEnabled(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SoltarBackground,
                                checkedTrackColor = SoltarAmber,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = SoltarSurfaceElevated
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = SoltarBorderSubtle)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Replay Onboarding
                    TextButton(
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.setOnboardingCompleted(false)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                            Text("Revisar guía de inicio y filosofía", color = SoltarAmber, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Notificaciones y Widget de Escritorio
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("notifications_and_widget_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
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
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Notificaciones & Widget",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SoltarAmber.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "ACTIVO",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Acompañamiento discreto en tu dispositivo para mantener tu compromiso y tener acceso de emergencia al instante.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 17.sp,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 1. Notificaciones y Alarmas Programadas
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = SoltarSurfaceElevated,
                        border = BorderStroke(1.dp, SoltarBorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Encabezado Recordatorio Diario
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Alarm,
                                        contentDescription = null,
                                        tint = SoltarAmber,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Column {
                                        val timeStr = String.format(
                                            java.util.Locale.getDefault(),
                                            "%02d:%02d hs",
                                            uiState.reminderHourInput,
                                            uiState.reminderMinuteInput
                                        )
                                        Text(
                                            "Recordatorio Diario ($timeStr)",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            "Cita inspiradora y llamada al check-in",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Switch(
                                    checked = uiState.notificationsEnabled,
                                    onCheckedChange = { viewModel.toggleNotificationsEnabled(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = SoltarAmber,
                                        checkedTrackColor = SoltarAmber.copy(alpha = 0.3f),
                                        uncheckedThumbColor = TextMuted,
                                        uncheckedTrackColor = SoltarSurface
                                    )
                                )
                            }

                            if (uiState.notificationsEnabled) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Surface(
                                        color = SoltarAmber.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.3f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(Icons.Default.AccessTime, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(14.dp))
                                            Text(
                                                text = String.format(java.util.Locale.getDefault(), "%02d:%02d hs", uiState.reminderHourInput, uiState.reminderMinuteInput),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SoltarAmber
                                            )
                                        }
                                    }

                                    OutlinedButton(
                                        onClick = { viewModel.toggleReminderTimeDialog(true) },
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.6f)),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = SoltarAmber)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Cambiar Hora", color = SoltarAmber, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Sub-sección integrada: Recordatorios Programables adicionales
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = SoltarSurface,
                                    border = BorderStroke(1.dp, SoltarBorderSubtle)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AlarmAdd,
                                                    contentDescription = null,
                                                    tint = SoltarAmber,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Column {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                                    ) {
                                                        Text(
                                                            "Otros Horarios & Recordatorios",
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = TextPrimary,
                                                            fontWeight = FontWeight.Bold
                                                        )
                                                        Surface(
                                                            shape = CircleShape,
                                                            color = SoltarAmber.copy(alpha = 0.18f)
                                                        ) {
                                                            Text(
                                                                text = "${uiState.customNotifications.count { it.enabled }} activos",
                                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                                fontSize = 9.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = SoltarAmber
                                                            )
                                                        }
                                                    }
                                                    Text(
                                                        "Avisos diarios adicionales para tu paz mental",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = TextSecondary,
                                                        fontSize = 10.sp
                                                    )
                                                }
                                            }

                                            IconButton(
                                                onClick = { viewModel.openAddCustomNotificationDialog() },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(Icons.Default.AddCircle, contentDescription = "Añadir recordatorio", tint = SoltarAmber, modifier = Modifier.size(20.dp))
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        if (uiState.customNotifications.isEmpty()) {
                                            Column(
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    "No tienes otros horarios programados.",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = TextMuted,
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 11.sp
                                                )
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Button(
                                                    onClick = { viewModel.restoreDefaultPresetReminders() },
                                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                                                    shape = RoundedCornerShape(8.dp),
                                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                                ) {
                                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black, modifier = Modifier.size(13.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Cargar Horarios Sugeridos", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        } else {
                                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                uiState.customNotifications.forEach { item ->
                                                    Surface(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        shape = RoundedCornerShape(8.dp),
                                                        color = if (item.enabled) SoltarSurfaceElevated else SoltarSurfaceElevated.copy(alpha = 0.5f),
                                                        border = BorderStroke(
                                                            1.dp,
                                                            if (item.enabled) SoltarBorder else SoltarBorderSubtle
                                                        )
                                                    ) {
                                                        Column(modifier = Modifier.padding(8.dp)) {
                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                verticalAlignment = Alignment.CenterVertically,
                                                                horizontalArrangement = Arrangement.SpaceBetween
                                                            ) {
                                                                Row(
                                                                    verticalAlignment = Alignment.CenterVertically,
                                                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                                                    modifier = Modifier.weight(1f)
                                                                ) {
                                                                    Surface(
                                                                        shape = RoundedCornerShape(4.dp),
                                                                        color = if (item.enabled) SoltarAmber.copy(alpha = 0.15f) else SoltarSurface,
                                                                        border = BorderStroke(1.dp, if (item.enabled) SoltarAmber.copy(alpha = 0.4f) else SoltarBorderSubtle)
                                                                    ) {
                                                                        Text(
                                                                            text = String.format(java.util.Locale.getDefault(), "%02d:%02d", item.hour, item.minute),
                                                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                                                            fontWeight = FontWeight.Bold,
                                                                            fontSize = 11.sp,
                                                                            color = if (item.enabled) SoltarAmber else TextMuted
                                                                        )
                                                                    }

                                                                    Text(
                                                                        text = item.title,
                                                                        style = MaterialTheme.typography.bodySmall,
                                                                        fontWeight = FontWeight.Bold,
                                                                        color = if (item.enabled) TextPrimary else TextMuted,
                                                                        maxLines = 1
                                                                    )
                                                                }

                                                                Switch(
                                                                    checked = item.enabled,
                                                                    onCheckedChange = { viewModel.toggleCustomNotificationEnabled(item.id, it) },
                                                                    colors = SwitchDefaults.colors(
                                                                        checkedThumbColor = SoltarAmber,
                                                                        checkedTrackColor = SoltarAmber.copy(alpha = 0.3f),
                                                                        uncheckedThumbColor = TextMuted,
                                                                        uncheckedTrackColor = SoltarSurface
                                                                    ),
                                                                    modifier = Modifier.scale(0.8f)
                                                                )
                                                            }

                                                            if (item.message.isNotBlank()) {
                                                                Spacer(modifier = Modifier.height(3.dp))
                                                                Text(
                                                                    text = item.message,
                                                                    style = MaterialTheme.typography.bodySmall,
                                                                    color = if (item.enabled) TextSecondary else TextMuted,
                                                                    fontSize = 10.sp,
                                                                    lineHeight = 14.sp
                                                                )
                                                            }

                                                            Spacer(modifier = Modifier.height(4.dp))

                                                            Row(
                                                                modifier = Modifier.fillMaxWidth(),
                                                                horizontalArrangement = Arrangement.End,
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                TextButton(
                                                                    onClick = {
                                                                        viewModel.triggerTestCustomNotification(item.title, item.message)
                                                                    },
                                                                    contentPadding = PaddingValues(horizontal = 5.dp, vertical = 1.dp)
                                                                ) {
                                                                    Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(12.dp), tint = SoltarAmber)
                                                                    Spacer(modifier = Modifier.width(3.dp))
                                                                    Text("Probar", fontSize = 10.sp, color = SoltarAmber)
                                                                }

                                                                TextButton(
                                                                    onClick = {
                                                                        viewModel.openEditCustomNotificationDialog(item)
                                                                    },
                                                                    contentPadding = PaddingValues(horizontal = 5.dp, vertical = 1.dp)
                                                                ) {
                                                                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(12.dp), tint = TextSecondary)
                                                                    Spacer(modifier = Modifier.width(3.dp))
                                                                    Text("Editar", fontSize = 10.sp, color = TextSecondary)
                                                                }

                                                                TextButton(
                                                                    onClick = {
                                                                        viewModel.deleteCustomNotification(item.id)
                                                                    },
                                                                    contentPadding = PaddingValues(horizontal = 5.dp, vertical = 1.dp)
                                                                ) {
                                                                    Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(12.dp), tint = SoltarTerracotta)
                                                                    Spacer(modifier = Modifier.width(3.dp))
                                                                    Text("Eliminar", fontSize = 10.sp, color = SoltarTerracotta)
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Button(
                                                    onClick = { viewModel.openAddCustomNotificationDialog() },
                                                    modifier = Modifier.weight(1f),
                                                    shape = RoundedCornerShape(6.dp),
                                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Black)
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Añadir Horario", color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                }

                                                OutlinedButton(
                                                    onClick = { viewModel.restoreDefaultPresetReminders() },
                                                    shape = RoundedCornerShape(6.dp),
                                                    border = BorderStroke(1.dp, SoltarBorder),
                                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(12.dp), tint = SoltarAmber)
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Plantillas", color = TextPrimary, fontSize = 10.sp)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = SoltarBorderSubtle
                            )

                            // 2. Acompañamiento Empático (3 días sin registro)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolunteerActivism,
                                        contentDescription = null,
                                        tint = SoltarSage,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Column {
                                        Text(
                                            "Acompañamiento tras 3 días",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            "Mensaje empático sin juicio si no registras datos",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Switch(
                                    checked = uiState.inactivityAlertsEnabled,
                                    onCheckedChange = { viewModel.toggleInactivityAlertsEnabled(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = SoltarSage,
                                        checkedTrackColor = SoltarSage.copy(alpha = 0.3f),
                                        uncheckedThumbColor = TextMuted,
                                        uncheckedTrackColor = SoltarSurface
                                    )
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = SoltarBorderSubtle
                            )

                            // 3. Celebración de Hitos de Contacto Cero
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = SoltarAmber,
                                    modifier = Modifier.size(22.dp)
                                )
                                Column {
                                    Text(
                                        "Hitos de Soberanía y Contacto Cero",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        "Notificaciones automáticas en los días 1, 3, 7, 14, 21, 30, 60, 90, 180 y 365",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Prueba de canales de notificación en vivo:",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.triggerTestDailyReminder() },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarSurface),
                                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.4f)),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                ) {
                                    Text("🔔 Diario", color = SoltarAmber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { viewModel.triggerTestInactivityReminder() },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarSurface),
                                    border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.4f)),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                ) {
                                    Text("🌿 Empatía (3d)", color = SoltarSage, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { viewModel.triggerTestMilestoneReminder(7) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarSurface),
                                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.4f)),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                ) {
                                    Text("🎉 Hito (7d)", color = SoltarAmber, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Widget de Pantalla de Inicio
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = SoltarSurfaceElevated,
                        border = BorderStroke(1.dp, SoltarBorderSubtle)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Widgets,
                                    contentDescription = null,
                                    tint = SoltarSage,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text("Widget de Pantalla de Inicio", style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                    Text("Racha de días, cita viva y botón SOS", style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "💡 Cómo añadirlo: Ve a la pantalla de inicio de tu teléfono, mantén presionado un espacio vacío, selecciona 'Widgets', busca 'Recuerda' y arrástralo a tu pantalla.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                        val intent = Intent(context, com.example.widget.SoltarAppWidgetConfigureActivity::class.java)
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                                ) {
                                    Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Personalizar", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                        com.example.widget.SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
                                        viewModel.showNotification("🔄 Widget sincronizado con éxito")
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, SoltarBorder)
                                ) {
                                    Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextSecondary)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Sincronizar", color = TextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Metas de Identidad Activas
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Metas de Autonomía",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.toggleIdentityGoalModal(true)
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar meta", tint = SoltarAmber)
                        }
                    }

                    if (identityGoals.isEmpty()) {
                        Text(
                            text = "No has registrado metas de autonomía todavía. Toca el botón '+' para agregar una.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            identityGoals.forEach { goal ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = SoltarSurfaceElevated,
                                    border = BorderStroke(1.dp, if (goal.isCompleted) SoltarSage.copy(alpha = 0.5f) else SoltarBorderSubtle)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Checkbox(
                                            checked = goal.isCompleted,
                                            onCheckedChange = {
                                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                viewModel.toggleGoalCompleted(goal.id, goal.isCompleted)
                                            },
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = SoltarSage,
                                                uncheckedColor = SoltarBorder
                                            )
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = goal.goalTitle,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = if (goal.isCompleted) TextSecondary else TextPrimary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = "${goal.area} • ${goal.goalFrequency}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = SoltarAmber,
                                                fontSize = 11.sp
                                            )
                                        }
                                        IconButton(onClick = { viewModel.deleteIdentityGoal(goal.id) }) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = TextMuted, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }



        // Privacidad & Seguridad (Derecho al Olvido)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Privacidad y Derecho al Olvido",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tus datos viven únicamente en este dispositivo. Puedes reiniciar la memoria en cualquier momento.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = { viewModel.clearAiMemory() },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = null, tint = SoltarAmber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reiniciar historial del chat IA", color = SoltarAmber, fontSize = 13.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    
                    OutlinedButton(
                        onClick = { /* TODO: Implement Export Logic */ },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Text("Exportar mis datos de forma segura")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { /* TODO: Implement Import Logic */ },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Text("Importar mis datos")
                    }
                }
            }
        }
        
        
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                    Button(
                        onClick = { showResetConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertBackground)
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = UrgeAlertRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Borrar todos los datos locales", color = UrgeAlertRed, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
        }

        // Footer Version
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = SoltarSurfaceHighlight,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, SoltarBorder),
                    modifier = Modifier.clickable { viewModel.toggleFounderExperience(true) }
                ) {
                    Text(
                        text = "Atalaya v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Acompañamiento Emocional Riguroso y Ético",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun SupportContactItem(
    index: Int,
    name: String,
    phone: String,
    relationship: String,
    onEdit: () -> Unit,
    onCall: () -> Unit,
    onSMS: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = SoltarSurfaceElevated,
        border = BorderStroke(1.dp, if (name.isNotBlank()) SoltarSage.copy(alpha = 0.3f) else SoltarBorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (name.isNotBlank()) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    if (relationship.isNotBlank()) {
                        Text(
                            text = relationship,
                            style = MaterialTheme.typography.bodySmall,
                            color = SoltarSage,
                            fontSize = 11.sp
                        )
                    }
                    if (phone.isNotBlank()) {
                        Text(
                            text = phone,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                } else {
                    Text(
                        text = "Contacto #$index (Sin configurar)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                    Text(
                        text = "Toca para asignar a alguien de confianza",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (name.isNotBlank() && phone.isNotBlank()) {
                    IconButton(onClick = onCall) {
                        Icon(Icons.Default.Phone, contentDescription = "Llamar", tint = SoltarSage, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onSMS) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "SMS", tint = SoltarAmber, modifier = Modifier.size(20.dp))
                    }
                }
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = if (name.isNotBlank()) Icons.Default.Edit else Icons.Default.AddCircleOutline,
                        contentDescription = "Configurar",
                        tint = SoltarAmber,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

private fun triggerPhoneCall(context: Context, phone: String) {
    if (phone.isBlank()) return
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${phone.trim()}")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir el marcador telefónico", Toast.LENGTH_SHORT).show()
    }
}

private fun triggerSms(context: Context, phone: String) {
    if (phone.isBlank()) return
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:${phone.trim()}")
            putExtra("sms_body", "Hola, necesito apoyo en este momento.")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir la app de SMS", Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun LegalAndContactSection(viewModel: SoltarViewModel) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
        border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                Text(
                    text = "INFORMACIÓN LEGAL Y TRANSPARENCIA",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Recuerda cumple con las normativas de transparencia de IA y protección de datos. Titular: Javier Jiménez Fernández.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 12.sp
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { viewModel.togglePrivacyPolicy(true) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SoltarAmber),
                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Privacidad", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { viewModel.toggleTermsConditions(true) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SoltarAmber),
                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
                ) {
                    Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Términos", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            HorizontalDivider(color = SoltarBorderSubtle)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Contacto Oficial & Soporte", style = MaterialTheme.typography.labelMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("adriana.app.soltar@gmail.com", style = MaterialTheme.typography.bodySmall, color = SoltarAmber, fontSize = 12.sp)
                }
                IconButton(onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:adriana.app.soltar@gmail.com?subject=Soporte%20Adriana")
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "adriana.app.soltar@gmail.com", Toast.LENGTH_LONG).show()
                    }
                }) {
                    Icon(Icons.Default.Email, contentDescription = "Enviar Correo", tint = SoltarAmber, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun AnticipatedRiskDatesSection(viewModel: SoltarViewModel) {
    val riskDates by viewModel.riskDates.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
        border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.EventNote, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(20.dp))
                    Text(
                        text = "FECHAS DE RIESGO ANTICIPADO",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { viewModel.toggleRiskDateModal(true) }) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Añadir Fecha", tint = SoltarAmber)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Nadie avisa antes de que llegue el momento difícil. Introduce fechas clave (cumpleaños del ex, aniversario, Navidad, San Valentín). Adriana te avisará 5-7 días antes con una estrategia preparada, y el coach conocerá el contexto sin que tengas que explicarlo.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (riskDates.isEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = SoltarSurface,
                    border = BorderStroke(1.dp, SoltarBorderSubtle)
                ) {
                    Text(
                        text = "No has configurado fechas de riesgo. Toca '+' para añadir la primera (ej. Cumpleaños del ex, Aniversario).",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (rd in riskDates) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = SoltarSurface,
                            border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = rd.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Fecha: ${rd.day} de ${getMonthName(rd.month)} • Aviso previo: ${rd.reminderDaysBefore} días",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SoltarAmber,
                                        fontSize = 11.sp
                                    )
                                    if (rd.customStrategy.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Estrategia: ${rd.customStrategy}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary,
                                            fontSize = 11.sp,
                                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                        )
                                    }
                                }
                                IconButton(onClick = { viewModel.deleteRiskDate(rd.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = UrgeAlertRed, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog para Crear/Añadir Fecha de Riesgo
    if (uiState.isRiskDateModalVisible) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleRiskDateModal(false) },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = SoltarAmber)
                    Text("Nueva Fecha de Riesgo Anticipado", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Selecciona un hito o introduce uno personalizado. Adriana se anticipará para proteger tu paz.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )

                    // Presets
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (preset in listOf("Cumpleaños del ex", "Aniversario", "San Valentín")) {
                                OutlinedButton(
                                    onClick = { viewModel.setRiskDateTitle(preset) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SoltarAmber),
                                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f)),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(preset, fontSize = 10.sp)
                                }
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            for (preset in listOf("Fecha de inicio", "Navidad")) {
                                OutlinedButton(
                                    onClick = { viewModel.setRiskDateTitle(preset) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SoltarAmber),
                                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f)),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(preset, fontSize = 10.sp)
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = uiState.riskDateTitleInput,
                        onValueChange = { viewModel.setRiskDateTitle(it) },
                        label = { Text("Título de la fecha (ej. Cumpleaños)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorder,
                            focusedLabelColor = SoltarAmber
                        )
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = uiState.riskDateDayInput.toString(),
                            onValueChange = { viewModel.setRiskDateDay(it.toIntOrNull() ?: 1) },
                            label = { Text("Día (1-31)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = uiState.riskDateMonthInput.toString(),
                            onValueChange = { viewModel.setRiskDateMonth(it.toIntOrNull() ?: 1) },
                            label = { Text("Mes (1-12)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Estrategia / Plan de contención", style = MaterialTheme.typography.labelMedium, color = TextPrimary)
                        TextButton(
                            onClick = {
                                val strategy = com.example.ai.OnDeviceLlmEngine.generateRiskDateCopingStrategy(
                                    riskDateTitle = uiState.riskDateTitleInput.ifBlank { "Fecha de Riesgo" },
                                    daysUntil = 3,
                                    pastTriggers = viewModel.triggerEvents.value,
                                    framework = uiState.preferredFramework
                                ).replace("**", "")
                                viewModel.setRiskDateStrategy(strategy)
                            },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sugerir plan (IA)", color = SoltarAmber, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    OutlinedTextField(
                        value = uiState.riskDateStrategyInput,
                        onValueChange = { viewModel.setRiskDateStrategy(it) },
                        label = { Text("Plan preparado") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej. Contacto cero estricto, cena con amigos, apagar móvil.") },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarAmber,
                            unfocusedBorderColor = SoltarBorder,
                            focusedLabelColor = SoltarAmber
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.saveRiskDate() },
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                ) {
                    Text("Guardar Fecha Clave", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.toggleRiskDateModal(false) }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = SoltarSurfaceElevated,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

private fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        12 -> "Diciembre"
        else -> "Mes $month"
    }
}
