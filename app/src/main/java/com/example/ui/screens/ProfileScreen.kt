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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.audio.SoltarSoundManager
import com.example.data.SoltarFramework
import com.example.data.SubscriptionPlan
import com.example.data.UserEntitlements
import com.example.ui.SoltarViewModel
import com.example.ui.auth.AuthViewModel
import com.example.ui.theme.*

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

    val entitlements = remember(settings) { UserEntitlements.fromSettings(settings) }
    var showResetConfirmDialog by remember { mutableStateOf(false) }
    var showDeleteAccountConfirmDialog by remember { mutableStateOf(false) }

    // Dialogs
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
                            "Tienes acceso ilimitado al Coach ADRIANA, auditorías profundas, ceremonias de cartas y biorregulación somática completa."
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
                        onClick = { viewModel.openPaywall(SubscriptionPlan.PREMIUM_ONE_TIME) },
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
                                text = "💡 Cómo añadirlo: Ve a la pantalla de inicio de tu teléfono, mantén presionado un espacio vacío, selecciona 'Widgets', busca 'ADRIANA' y arrástralo a tu pantalla.",
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

                    Spacer(modifier = Modifier.height(8.dp))

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
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Text(
                        text = "ADRIANA v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})",
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
