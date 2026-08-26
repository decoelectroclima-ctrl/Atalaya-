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
                        onWhatsApp = { triggerWhatsApp(context, c1Phone) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SupportContactItem(
                        index = 2,
                        name = c2Name,
                        phone = c2Phone,
                        relationship = c2Rel,
                        onEdit = { viewModel.openSupportContactDialog(2) },
                        onCall = { triggerPhoneCall(context, c2Phone) },
                        onWhatsApp = { triggerWhatsApp(context, c2Phone) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    SupportContactItem(
                        index = 3,
                        name = c3Name,
                        phone = c3Phone,
                        relationship = c3Rel,
                        onEdit = { viewModel.openSupportContactDialog(3) },
                        onCall = { triggerPhoneCall(context, c3Phone) },
                        onWhatsApp = { triggerWhatsApp(context, c3Phone) }
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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Preferencias de la Experiencia",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

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
    onWhatsApp: () -> Unit
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
                    IconButton(onClick = onWhatsApp) {
                        Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "WhatsApp", tint = SoltarAmber, modifier = Modifier.size(20.dp))
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

private fun triggerWhatsApp(context: Context, phone: String) {
    if (phone.isBlank()) return
    try {
        val cleanNumber = phone.replace("+", "").replace(" ", "").replace("-", "").trim()
        val url = "https://wa.me/$cleanNumber"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
    }
}
