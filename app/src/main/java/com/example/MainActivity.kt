package com.example

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.SoltarSoundManager
import com.example.ui.SoltarTab
import com.example.ui.SoltarViewModel
import com.example.ui.auth.AuthViewModel
import com.example.ui.screens.*
import com.example.ui.theme.*

sealed class SoltarNavItem(val tab: SoltarTab, val label: String, val icon: ImageVector) {
    object Inicio : SoltarNavItem(SoltarTab.INICIO, "Inicio", Icons.Default.Home)
    object Proceso : SoltarNavItem(SoltarTab.PROCESO, "Proceso", Icons.Default.Timeline)
    object Perfil : SoltarNavItem(SoltarTab.PERFIL, "Perfil", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {

    private val viewModel: SoltarViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Notification Channels and Daily Schedule from User Settings
        com.example.notifications.SoltarNotificationHelper.createNotificationChannels(this)
        com.example.notifications.SoltarNotificationHelper.rescheduleFromSettings(this)
        com.example.widget.SoltarAppWidgetProvider.notifyWidgetDataChanged(this)

        handleIntent(intent)

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            SoltarTheme(themeMode = uiState.themeMode) {
                val context = LocalContext.current
                val snackbarHostState = remember { SnackbarHostState() }
                var showExitDialog by remember { mutableStateOf(false) }

                // Notification runtime permission launcher for Android 13+
                val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
                    contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
                ) { isGranted ->
                    if (isGranted) {
                        viewModel.showNotification("🔔 Notificaciones de acompañamiento activadas")
                    }
                }

                LaunchedEffect(Unit) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                        if (!com.example.notifications.SoltarNotificationHelper.hasNotificationPermission(context)) {
                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()
                val isMandatoryJournalPending by viewModel.isMandatoryJournalPending.collectAsStateWithLifecycle()

                val isAnyModalOpen = uiState.isNeedHelpSheetVisible ||
                        uiState.isUrgeSheetVisible ||
                        uiState.isNoThinkingSheetVisible ||
                        uiState.isAiCompanionSheetVisible ||
                        uiState.isThoughtModalVisible ||
                        uiState.isAuditModalVisible ||
                        uiState.isIdealizationModalVisible ||
                        uiState.isLetterModalVisible ||
                        uiState.isIdentityGoalModalVisible ||
                        uiState.isRelapseModalVisible ||
                        uiState.isConversationAnalyzerVisible ||
                        uiState.isAuthDialogVisible ||
                        authUiState.isAuthDialogVisible ||
                        uiState.isPaywallVisible ||
                        uiState.isSupportContactDialogVisible ||
                        uiState.isOnboardingVisible

                // Root Exit Confirmation BackHandler
                BackHandler(enabled = !isAnyModalOpen) {
                    showExitDialog = true
                }

                if (showExitDialog) {
                    AlertDialog(
                        onDismissRequest = { showExitDialog = false },
                        title = {
                            Text(
                                text = "¿Seguro que quieres salir?",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        text = {
                            Text(
                                text = "Tu progreso y tiempo en Contacto Cero quedan guardados con total seguridad. Vuelve cuando lo necesites.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                lineHeight = 20.sp
                            )
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showExitDialog = false
                                    (context as? Activity)?.finish()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SoltarSurfaceElevated),
                                border = BorderStroke(1.dp, SoltarBorder)
                            ) {
                                Text("Salir", color = TextMuted, fontSize = 13.sp)
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                    showExitDialog = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                            ) {
                                Text("Quedarme", color = SoltarBackground, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        },
                        containerColor = SoltarSurface,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag("exit_confirmation_dialog")
                    )
                }

                LaunchedEffect(uiState.notificationMessage) {
                    uiState.notificationMessage?.let { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        viewModel.clearNotification()
                    }
                }

                val navItems = listOf(
                    SoltarNavItem.Inicio,
                    SoltarNavItem.Proceso,
                    SoltarNavItem.Perfil
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(SoltarBackground)
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = SoltarBackground,
                        topBar = {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = SoltarBackground,
                                border = BorderStroke(1.dp, SoltarBorderSubtle)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .statusBarsPadding()
                                        .padding(horizontal = 20.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Recuerda",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = SoltarAmber,
                                            fontWeight = FontWeight.ExtraBold,
                                            letterSpacing = 2.sp
                                        )
                                        Text(
                                            text = "Acompañamiento sobrio y regulación emocional",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextMuted,
                                            fontSize = 10.sp
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                            viewModel.toggleAiCompanionSheet(true)
                                        },
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(SoltarSurfaceElevated)
                                            .testTag("topbar_ai_button")
                                    ) {
                                        Icon(
                                            Icons.Default.Psychology,
                                            contentDescription = "Recuerda Coach",
                                            tint = SoltarAmber,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        },
                        floatingActionButton = {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    viewModel.playSound(SoltarSoundManager.SoundType.URGE_ALERT)
                                    viewModel.openNeedHelpSheet()
                                },
                                containerColor = UrgeAlertRed,
                                contentColor = TextPrimary,
                                shape = RoundedCornerShape(16.dp),
                                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                                modifier = Modifier
                                    .padding(bottom = 12.dp)
                                    .testTag("fab_need_help")
                            ) {
                                Icon(
                                    Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "NECESITO AYUDA",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    letterSpacing = 1.sp
                                )
                            }
                        },
                        bottomBar = {
                            NavigationBar(
                                containerColor = SoltarSurface,
                                contentColor = SoltarAmberLight,
                                tonalElevation = 8.dp
                            ) {
                                navItems.forEach { item ->
                                    val selected = uiState.currentTab == item.tab
                                    NavigationBarItem(
                                        selected = selected,
                                        onClick = {
                                            if (!selected) {
                                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                viewModel.setTab(item.tab)
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                item.icon,
                                                contentDescription = item.label,
                                                tint = if (selected) SoltarAmber else TextMuted
                                            )
                                        },
                                        label = {
                                            Text(
                                                text = item.label,
                                                fontSize = 11.sp,
                                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (selected) SoltarAmber else TextMuted
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            indicatorColor = SoltarSurfaceElevated
                                        ),
                                        modifier = Modifier.testTag("nav_${item.label.lowercase()}")
                                    )
                                }
                            }
                        },
                        snackbarHost = { SnackbarHost(snackbarHostState) }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SoltarBackground)
                                .padding(innerPadding)
                        ) {
                            if (isMandatoryJournalPending) {
                                com.example.ui.screens.MandatoryJournalPendingScreen(viewModel = viewModel)
                            } else {
                                when (uiState.currentTab) {
                                    SoltarTab.INICIO -> TodayScreen(viewModel = viewModel)
                                    SoltarTab.PROCESO -> ProcessScreen(viewModel = viewModel)
                                    SoltarTab.PERFIL -> ProfileScreen(viewModel = viewModel, authViewModel = authViewModel)
                                }
                            }
                        }
                    }

                    // First Launch Onboarding Flow
                    if (uiState.isOnboardingVisible) {
                        OnboardingScreen(
                            viewModel = viewModel,
                            onComplete = { viewModel.setOnboardingCompleted(true) }
                        )
                    }

                    // Global Contextual Sheets & Dialogs
                    if (uiState.isNeedHelpSheetVisible) {
                        NeedHelpSheet(
                            viewModel = viewModel,
                            onDismiss = { viewModel.closeNeedHelpSheet() }
                        )
                    }

                    if (uiState.isEmotionalCheckinVisible) {
                        com.example.ui.dialogs.EmotionalCheckinDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.closeEmotionalCheckin() }
                        )
                    }

                    if (uiState.isJournalModalVisible) {
                        com.example.ui.dialogs.PersonalJournalDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.closeJournalModal() }
                        )
                    }

                    if (uiState.isUrgeSheetVisible) {
                        UrgeModeDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.closeUrgeSheet() }
                        )
                    }

                    if (uiState.isNoThinkingSheetVisible) {
                        NoThinkingDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.closeNoThinkingSheet() }
                        )
                    }

                    if (uiState.isAiCompanionSheetVisible) {
                        AiCompanionDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleAiCompanionSheet(false) }
                        )
                    }

                    if (uiState.isTimeCapsuleModalVisible) {
                        com.example.ui.dialogs.TimeCapsuleDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleTimeCapsuleModal(false) }
                        )
                    }

                    if (uiState.isEncounterSimulatorVisible) {
                        com.example.ui.dialogs.EncounterSimulatorDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleEncounterSimulator(false) }
                        )
                    }

                    if (uiState.isThoughtModalVisible) {
                        ThoughtLabDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleThoughtModal(false) }
                        )
                    }

                    if (uiState.isAuditModalVisible) {
                        RelationshipAuditDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleAuditModal(false) }
                        )
                    }

                    if (uiState.isIdealizationModalVisible) {
                        IdealizationDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleIdealizationModal(false) }
                        )
                    }

                    if (uiState.isLetterModalVisible) {
                        UnsentLetterDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleLetterModal(false) }
                        )
                    }

                    if (uiState.isIdentityGoalModalVisible) {
                        IdentityGoalDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleIdentityGoalModal(false) }
                        )
                    }

                    if (uiState.isRelapseModalVisible) {
                        RelapseDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleRelapseModal(false) }
                        )
                    }

                    if (uiState.isAuthDialogVisible || authUiState.isAuthDialogVisible) {
                        AuthDialog(
                            viewModel = authViewModel,
                            isLockdown = uiState.isAuthDialogVisible,
                            onDismiss = {
                                if (!uiState.isAuthDialogVisible) {
                                    authViewModel.closeAuthDialog()
                                    viewModel.toggleAuthDialog(false)
                                }
                            }
                        )
                    }

                    if (uiState.isPaywallVisible) {
                        PaywallDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.closePaywall() }
                        )
                    }

                    if (uiState.isSupportContactDialogVisible) {
                        SupportContactDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.closeSupportContactDialog() }
                        )
                    }

                    if (uiState.isFounderExperienceVisible) {
                        com.example.ui.dialogs.FounderExperienceDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleFounderExperience(false) }
                        )
                    }

                    if (uiState.isConversationAnalyzerVisible) {
                        ConversationAnalyzerDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.closeConversationAnalyzer() }
                        )
                    }

                    if (uiState.isWisdomLibraryVisible) {
                        com.example.ui.dialogs.WisdomLibraryDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleWisdomLibraryDialog(false) }
                        )
                    }

                    if (uiState.isClosingRitualVisible) {
                        com.example.ui.dialogs.ClosingRitualDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleClosingRitualDialog(false) }
                        )
                    }

                    if (uiState.isVoluntaryExitVisible) {
                        com.example.ui.dialogs.VoluntaryExitDialog(
                            viewModel = viewModel,
                            onDismiss = { viewModel.toggleVoluntaryExitDialog(false) }
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: android.content.Intent?) {
        val action = intent?.getStringExtra(com.example.widget.SoltarAppWidgetProvider.EXTRA_OPEN_ACTION)
        when (action) {
            com.example.widget.SoltarAppWidgetProvider.ACTION_URGE_MODE -> {
                viewModel.openUrgeMode()
            }
            com.example.widget.SoltarAppWidgetProvider.ACTION_JOURNAL -> {
                viewModel.openJournalModal()
            }
            com.example.widget.SoltarAppWidgetProvider.ACTION_CHECKIN -> {
                viewModel.openEmotionalCheckin()
            }
            com.example.widget.SoltarAppWidgetProvider.ACTION_COACH -> {
                viewModel.toggleAiCompanionSheet(true)
            }
        }
    }
}
