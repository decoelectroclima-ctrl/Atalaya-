package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import com.example.ui.SoltarTab
import com.example.ui.SoltarViewModel
import com.example.ui.screens.*
import com.example.ui.theme.*

sealed class SoltarNavItem(val tab: SoltarTab, val label: String, val icon: ImageVector) {
    object Inicio : SoltarNavItem(SoltarTab.INICIO, "Inicio", Icons.Default.Home)
    object Proceso : SoltarNavItem(SoltarTab.PROCESO, "Proceso", Icons.Default.Timeline)
    object Perfil : SoltarNavItem(SoltarTab.PERFIL, "Perfil", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {

    private val viewModel: SoltarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SoltarTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val context = LocalContext.current
                val snackbarHostState = remember { SnackbarHostState() }

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
                                        text = "SOLTAR",
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
                                    onClick = { viewModel.toggleAiCompanionSheet(true) },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(SoltarSurfaceElevated)
                                        .testTag("topbar_ai_button")
                                ) {
                                    Icon(
                                        Icons.Default.Psychology,
                                        contentDescription = "SOLTAR IA",
                                        tint = SoltarAmber,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            onClick = { viewModel.openUrgeSheet() },
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
                                    onClick = { viewModel.setTab(item.tab) },
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
                        when (uiState.currentTab) {
                            SoltarTab.INICIO -> TodayScreen(viewModel = viewModel)
                            SoltarTab.PROCESO -> ProcessScreen(viewModel = viewModel)
                            SoltarTab.PERFIL -> ProfileScreen(viewModel = viewModel)
                        }
                    }
                }

                // Global Contextual Sheets & Dialogs
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
            }
        }
    }
}
