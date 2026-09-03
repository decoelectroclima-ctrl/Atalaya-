package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoltarSoundManager
import com.example.data.SoltarFramework
import com.example.ui.SoltarViewModel
import com.example.ui.components.AdrianaIntroScreen
import com.example.ui.theme.*

sealed class OnboardingPage {
    data object IntroHero : OnboardingPage()
    data object AccountRegistration : OnboardingPage()
    data object ContextCurrentSituation : OnboardingPage()
    data object ContextDuration : OnboardingPage()
    data object ContextTimePassed : OnboardingPage()
    data object ContextBreakupOrigin : OnboardingPage()
    data object ContextChildren : OnboardingPage()
    data object ContextAnticipatedGrief : OnboardingPage()
    data object ContextEmotionalState : OnboardingPage()
    data object FrameworkSelector : OnboardingPage()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: SoltarViewModel,
    onComplete: () -> Unit
) {
    var currentStepIndex by remember { mutableStateOf(0) }
    val uiState by viewModel.uiState.collectAsState()

    // Form states for 10 progressive steps
    var userNameInput by remember { mutableStateOf("Viajero") }
    var userEmailInput by remember { mutableStateOf("") }
    var selectedBreakupSituation by remember { mutableStateOf("RUPTURA_RECIENTE") }
    var selectedRelDuration by remember { mutableStateOf("6_12_MESES") }
    var selectedTimeSinceBreakup by remember { mutableStateOf("1_3_meses") }
    var selectedBreakupReason by remember { mutableStateOf("desgaste") }
    var selectedDecisionMaker by remember { mutableStateOf("OTRA_PERSONA") }
    var selectedHasChildren by remember { mutableStateOf(false) }
    var selectedCohabitation by remember { mutableStateOf(false) }
    var selectedAnticipatedGrief by remember { mutableStateOf("NO") }
    var selectedEmotionalSituation by remember { mutableStateOf("ansiedad, tristeza, confusión") }
    var selectedContactType by remember { mutableStateOf("CONTACTO_CERO_REAL") }
    var selectedFramework by remember(uiState.preferredFramework) { mutableStateOf(uiState.preferredFramework) }

    // AI Framework Recommendation questionnaire
    var showAiFrameworkSurvey by remember { mutableStateOf(false) }
    var q1Choice by remember { mutableStateOf<Int?>(null) }
    var q2Choice by remember { mutableStateOf<Int?>(null) }
    var q3Choice by remember { mutableStateOf<Int?>(null) }
    var frameworkRec by remember { mutableStateOf<com.example.ai.OnDeviceLlmEngine.FrameworkRecommendation?>(null) }

    val pages = remember {
        listOf(
            OnboardingPage.IntroHero,
            OnboardingPage.AccountRegistration,
            OnboardingPage.ContextCurrentSituation,
            OnboardingPage.ContextDuration,
            OnboardingPage.ContextTimePassed,
            OnboardingPage.ContextBreakupOrigin,
            OnboardingPage.ContextChildren,
            OnboardingPage.ContextAnticipatedGrief,
            OnboardingPage.ContextEmotionalState,
            OnboardingPage.FrameworkSelector
        )
    }

    val totalSteps = pages.size
    val currentPage = pages[currentStepIndex]
    val isIntroPage = currentPage is OnboardingPage.IntroHero

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isIntroPage) com.example.ui.theme.WarmPorcelainBg else SoltarBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Step Counter & Emergency Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isIntroPage) Color(0xFFEBE4DC) else SoltarSurfaceElevated,
                    border = BorderStroke(1.dp, if (isIntroPage) Color(0xFFD5CDC3) else SoltarBorder)
                ) {
                    Text(
                        text = "Recuerda • Paso ${currentStepIndex + 1} de $totalSteps",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isIntroPage) Color(0xFF8F1825) else SoltarAmber,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Emergency / Need Help button accessible during onboarding
                TextButton(
                    onClick = {
                        viewModel.playSound(SoltarSoundManager.SoundType.URGE_ALERT)
                        viewModel.openNeedHelpSheet()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = UrgeAlertRed)
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Ayuda", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Main Animated Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentPage,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "onboarding_page"
                ) { page ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        when (page) {
                            is OnboardingPage.IntroHero -> {
                                AdrianaIntroScreen(
                                    modifier = Modifier.fillMaxWidth().height(400.dp)
                                )
                            }

                            is OnboardingPage.AccountRegistration -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Surface(
                                        modifier = Modifier.size(72.dp),
                                        shape = CircleShape,
                                        color = SoltarAmber.copy(alpha = 0.12f),
                                        border = BorderStroke(1.5.dp, SoltarAmber.copy(alpha = 0.5f))
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = SoltarAmber,
                                                modifier = Modifier.size(36.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Text(
                                        text = "REGISTRO Y PERFIL BÁSICO",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.4.sp
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Crea tu cuenta para comenzar con Recuerda",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Recuerda requiere un registro (incluida la versión FREE) para proteger tus datos de duelo y mantener tu progreso de contacto cero de forma segura.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 20.sp
                                    )

                                    Spacer(modifier = Modifier.height(24.dp))

                                    OutlinedTextField(
                                        value = userNameInput,
                                        onValueChange = { userNameInput = it },
                                        label = { Text("Tu Nombre o Pseudónimo") },
                                        modifier = Modifier.fillMaxWidth().testTag("onboarding_name_input"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = SoltarAmber,
                                            unfocusedBorderColor = SoltarBorder,
                                            focusedLabelColor = SoltarAmber
                                        ),
                                        singleLine = true
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    OutlinedTextField(
                                        value = userEmailInput,
                                        onValueChange = { userEmailInput = it },
                                        label = { Text("Correo electrónico (para tu cuenta)") },
                                        modifier = Modifier.fillMaxWidth().testTag("onboarding_email_input"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = SoltarAmber,
                                            unfocusedBorderColor = SoltarBorder,
                                            focusedLabelColor = SoltarAmber
                                        ),
                                        singleLine = true
                                    )
                                }
                            }

                            is OnboardingPage.ContextCurrentSituation -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "SITUACIÓN ACTUAL",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.4.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "¿Cuál es tu situación respecto a la relación?",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    val situations = listOf(
                                        "RUPTURA_RECIENTE" to "Ruptura reciente / shock emocional",
                                        "SEPARACION_AMBIGUA" to "Separación ambigua / contacto intermitente",
                                        "TODAVIA_JUNTOS_DESCONECTADOS" to "Todavía conviviendo / en proceso de terminar",
                                        "PLANTEANDOSE_TERMINAR" to "Planteándose terminar la relación"
                                    )

                                    situations.forEach { (key, label) ->
                                        val isSelected = selectedBreakupSituation == key
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clickable {
                                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                    selectedBreakupSituation = key
                                                },
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) SoltarAmber.copy(alpha = 0.12f) else SoltarSurface,
                                            border = BorderStroke(width = if (isSelected) 1.5.dp else 1.dp, color = if (isSelected) SoltarAmber else SoltarBorder)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = {
                                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                        selectedBreakupSituation = key
                                                    },
                                                    colors = RadioButtonDefaults.colors(selectedColor = SoltarAmber)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(text = label, style = MaterialTheme.typography.bodyMedium, color = if (isSelected) SoltarAmber else TextPrimary, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                            }
                                        }
                                    }
                                }
                            }

                            is OnboardingPage.ContextDuration -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "CONTEXTO DE LA RELACIÓN",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.4.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "¿Cuánto tiempo duró la relación?",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    val durations = listOf(
                                        "MENOS_3_MESES" to "Menos de 3 meses",
                                        "3_6_MESES" to "3 a 6 meses",
                                        "6_12_MESES" to "6 a 12 meses",
                                        "1_3_ANIOS" to "1 a 3 años",
                                        "3_5_ANIOS" to "3 a 5 años",
                                        "5_10_ANIOS" to "5 a 10 años",
                                        "MAS_10_ANIOS" to "Más de 10 años"
                                    )

                                    durations.forEach { (key, label) ->
                                        val isSelected = selectedRelDuration == key
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clickable {
                                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                    selectedRelDuration = key
                                                },
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) SoltarAmber.copy(alpha = 0.12f) else SoltarSurface,
                                            border = BorderStroke(width = if (isSelected) 1.5.dp else 1.dp, color = if (isSelected) SoltarAmber else SoltarBorder)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = {
                                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                        selectedRelDuration = key
                                                    },
                                                    colors = RadioButtonDefaults.colors(selectedColor = SoltarAmber)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(text = label, style = MaterialTheme.typography.bodyMedium, color = if (isSelected) SoltarAmber else TextPrimary, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                            }
                                        }
                                    }
                                }
                            }

                            is OnboardingPage.ContextTimePassed -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "MOMENTO ACTUAL",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.4.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "¿Cuánto tiempo ha pasado desde la ruptura?",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    val times = listOf(
                                        "MENOS_1_MES" to "Menos de 1 mes (Fase aguda / shock)",
                                        "1_3_meses" to "1 a 3 meses",
                                        "3_6_meses" to "3 a 6 meses",
                                        "6_12_meses" to "6 a 12 meses",
                                        "MAS_1_ANO" to "Más de 1 año",
                                        "TODAVIA_JUNTOS" to "Todavía juntos / Proceso de separación"
                                    )

                                    times.forEach { (key, label) ->
                                        val isSelected = selectedTimeSinceBreakup == key
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clickable {
                                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                    selectedTimeSinceBreakup = key
                                                },
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) SoltarAmber.copy(alpha = 0.12f) else SoltarSurface,
                                            border = BorderStroke(width = if (isSelected) 1.5.dp else 1.dp, color = if (isSelected) SoltarAmber else SoltarBorder)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = {
                                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                        selectedTimeSinceBreakup = key
                                                    },
                                                    colors = RadioButtonDefaults.colors(selectedColor = SoltarAmber)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(text = label, style = MaterialTheme.typography.bodyMedium, color = if (isSelected) SoltarAmber else TextPrimary, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                            }
                                        }
                                    }
                                }
                            }

                            is OnboardingPage.ContextChildren -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "VÍNCULOS E HIJOS",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.4.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "¿Tienen hijos en común u otros vínculos inevitables?",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Recuerda adapta su protocolo si existen hijos para gestionar contacto cero funcional (estrictamente parental).",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))

                                    val options = listOf(
                                        false to "No tenemos hijos ni vínculos inevitables",
                                        true to "Sí, tenemos hijos u otros vínculos inevitables"
                                    )

                                    options.forEach { (value, label) ->
                                        val isSelected = selectedHasChildren == value
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 6.dp)
                                                .clickable {
                                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                    selectedHasChildren = value
                                                },
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (isSelected) SoltarAmber.copy(alpha = 0.12f) else SoltarSurface,
                                            border = BorderStroke(width = if (isSelected) 1.5.dp else 1.dp, color = if (isSelected) SoltarAmber else SoltarBorder)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = {
                                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                        selectedHasChildren = value
                                                    },
                                                    colors = RadioButtonDefaults.colors(selectedColor = SoltarAmber)
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                                Text(text = label, style = MaterialTheme.typography.bodyMedium, color = if (isSelected) SoltarAmber else TextPrimary, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }

                            is OnboardingPage.ContextBreakupOrigin -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "ORIGEN Y DECISIÓN",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.4.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "¿Quién tomó la decisión de terminar y cuál fue el detonante?",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(text = "Quién decidió:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val decs = listOf(
                                        "OTRA_PERSONA" to "La otra persona decidió terminar",
                                        "YO" to "Yo tomé la decisión",
                                        "MUTUA" to "Fue de mutuo acuerdo"
                                    )
                                    decs.forEach { (key, label) ->
                                        val isSelected = selectedDecisionMaker == key
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp)
                                                .clickable { selectedDecisionMaker = key },
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) SoltarAmber.copy(alpha = 0.12f) else SoltarSurface,
                                            border = BorderStroke(1.dp, if (isSelected) SoltarAmber else SoltarBorder)
                                        ) {
                                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                                RadioButton(selected = isSelected, onClick = { selectedDecisionMaker = key })
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(label, style = MaterialTheme.typography.bodySmall, color = if (isSelected) SoltarAmber else TextPrimary)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(text = "Motivo principal:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    val reasons = listOf(
                                        "desgaste" to "Desgaste y pérdida de conexión",
                                        "infidelidad" to "Infidelidad o traición de confianza",
                                        "distanciamiento" to "Distanciamiento gradual o proyectos distintos"
                                    )
                                    reasons.forEach { (key, label) ->
                                        val isSelected = selectedBreakupReason == key
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp)
                                                .clickable { selectedBreakupReason = key },
                                            shape = RoundedCornerShape(8.dp),
                                            color = if (isSelected) SoltarAmber.copy(alpha = 0.12f) else SoltarSurface,
                                            border = BorderStroke(1.dp, if (isSelected) SoltarAmber else SoltarBorder)
                                        ) {
                                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                                RadioButton(selected = isSelected, onClick = { selectedBreakupReason = key })
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(label, style = MaterialTheme.typography.bodySmall, color = if (isSelected) SoltarAmber else TextPrimary)
                                            }
                                        }
                                    }
                                }
                            }

                            is OnboardingPage.ContextAnticipatedGrief -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "DUELO ANTICIPADO",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.4.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "¿Empezaste a hacer el duelo antes de la ruptura formal?",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    val griefs = listOf(
                                        "NO" to "No, fue completamente inesperado",
                                        "SI_LLEVABA_TIEMPO_DECEPCIONANDOME" to "Sí, llevaba tiempo decepcionándome o sufriendo en silencio",
                                        "SI_ESTABA_AGOTADO" to "Sí, estaba agotado emocionalmente antes de terminar"
                                    )

                                    griefs.forEach { (key, label) ->
                                        val isSelected = selectedAnticipatedGrief == key
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp)
                                                .clickable { selectedAnticipatedGrief = key },
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) SoltarAmber.copy(alpha = 0.12f) else SoltarSurface,
                                            border = BorderStroke(1.dp, if (isSelected) SoltarAmber else SoltarBorder)
                                        ) {
                                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                                RadioButton(selected = isSelected, onClick = { selectedAnticipatedGrief = key })
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(label, style = MaterialTheme.typography.bodyMedium, color = if (isSelected) SoltarAmber else TextPrimary)
                                            }
                                        }
                                    }
                                }
                            }

                            is OnboardingPage.ContextEmotionalState -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "ESTADO EMOCIONAL",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.4.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "¿Cómo describes tu estado emocional o lo que más necesitas ahora?",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))

                                    OutlinedTextField(
                                        value = selectedEmotionalSituation,
                                        onValueChange = { selectedEmotionalSituation = it },
                                        label = { Text("Ej. Ansiedad nocturna, nostalgia, ganas de escribirle, confusión...") },
                                        modifier = Modifier.fillMaxWidth().height(120.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = SoltarAmber,
                                            unfocusedBorderColor = SoltarBorder,
                                            focusedLabelColor = SoltarAmber
                                        )
                                    )
                                }
                            }

                            is OnboardingPage.FrameworkSelector -> {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "MARCO DE SABIDURÍA",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.4.sp
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "¿Desde qué enfoque prefieres acompañar tu proceso?",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Elige la perspectiva que guiará tus reflexiones diarias y las respuestas de tu coach.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    SoltarFramework.values().forEach { framework ->
                                        val isSelected = selectedFramework == framework
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 5.dp)
                                                .clickable {
                                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                    selectedFramework = framework
                                                }
                                                .testTag("framework_option_${framework.name.lowercase()}"),
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (isSelected) SoltarAmber.copy(alpha = 0.12f) else SoltarSurface,
                                            border = BorderStroke(
                                                width = if (isSelected) 1.5.dp else 1.dp,
                                                color = if (isSelected) SoltarAmber else SoltarBorder
                                            )
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(14.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                RadioButton(
                                                    selected = isSelected,
                                                    onClick = {
                                                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                        selectedFramework = framework
                                                    },
                                                    colors = RadioButtonDefaults.colors(
                                                        selectedColor = SoltarAmber,
                                                        unselectedColor = SoltarBorder
                                                    )
                                                )
                                                Column {
                                                    Text(
                                                        text = framework.title,
                                                        style = MaterialTheme.typography.titleSmall,
                                                        color = if (isSelected) SoltarAmber else TextPrimary,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = framework.description,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = TextSecondary,
                                                        lineHeight = 17.sp,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    if (!showAiFrameworkSurvey && frameworkRec == null) {
                                        OutlinedButton(
                                            onClick = { showAiFrameworkSurvey = true },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .testTag("ai_framework_quiz_btn"),
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(1.dp, SoltarAmber)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(16.dp))
                                                Text("¿Indeciso? Descubre tu marco ideal con IA", color = SoltarAmber, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                            }
                                        }
                                    } else if (showAiFrameworkSurvey && frameworkRec == null) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                                            border = BorderStroke(1.dp, SoltarAmber)
                                        ) {
                                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                    Icon(Icons.Default.Psychology, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                                                    Text("Cuestionario de Afinidad (IA On-Device)", style = MaterialTheme.typography.titleSmall, color = SoltarAmber, fontWeight = FontWeight.Bold)
                                                }

                                                Text("1. ¿Qué tipo de voz necesitas ahora?", style = MaterialTheme.typography.labelSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                                                listOf(
                                                    "Firme y racional: enfocarme en lo que controlo",
                                                    "Científica y compasiva: entender mi cerebro y apego",
                                                    "Profunda y espiritual: refugio de fe y sentido"
                                                ).forEachIndexed { index, option ->
                                                    FilterChip(
                                                        selected = q1Choice == index,
                                                        onClick = { q1Choice = index },
                                                        label = { Text(option, fontSize = 11.sp) }
                                                    )
                                                }

                                                Text("2. ¿Cuál es tu mayor desafío actual?", style = MaterialTheme.typography.labelSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                                                listOf(
                                                    "Aceptar la realidad y no desear que sea distinta",
                                                    "El dolor punzante y las ganas compulsivas de buscarle",
                                                    "El vacío de sentido y la necesidad de perdón interior"
                                                ).forEachIndexed { index, option ->
                                                    FilterChip(
                                                        selected = q2Choice == index,
                                                        onClick = { q2Choice = index },
                                                        label = { Text(option, fontSize = 11.sp) }
                                                    )
                                                }

                                                Text("3. ¿Qué actitud deseas cultivar?", style = MaterialTheme.typography.labelSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                                                listOf(
                                                    "Fortaleza de carácter y sobriedad interior",
                                                    "Autocompasión y reconstrucción de autoestima",
                                                    "Confianza trascendente y serenidad del corazón"
                                                ).forEachIndexed { index, option ->
                                                    FilterChip(
                                                        selected = q3Choice == index,
                                                        onClick = { q3Choice = index },
                                                        label = { Text(option, fontSize = 11.sp) }
                                                    )
                                                }

                                                if (q1Choice != null && q2Choice != null && q3Choice != null) {
                                                    Button(
                                                        onClick = {
                                                            val rec = com.example.ai.OnDeviceLlmEngine.evaluateOnboardingFrameworkRecommendation(
                                                                q1Choice!!, q2Choice!!, q3Choice!!
                                                            )
                                                            frameworkRec = rec
                                                            selectedFramework = rec.recommendedFramework
                                                        },
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .testTag("ai_evaluate_framework_btn"),
                                                        shape = RoundedCornerShape(10.dp),
                                                        colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                                                    ) {
                                                        Text("Obtener recomendación de marco", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                    }
                                                }
                                            }
                                        }
                                    } else if (frameworkRec != null) {
                                        val rec = frameworkRec!!
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 8.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                                            border = BorderStroke(1.5.dp, SoltarAmber)
                                        ) {
                                            Column(modifier = Modifier.padding(14.dp)) {
                                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                                                    Text("Recomendación: ${rec.recommendedFramework.title} (${rec.matchConfidencePercentage}% afinidad)", style = MaterialTheme.typography.titleSmall, color = SoltarAmber, fontWeight = FontWeight.Bold)
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(rec.rationale, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text("💡 ${rec.primaryBenefit}", style = MaterialTheme.typography.bodySmall, color = SoltarSage, fontWeight = FontWeight.SemiBold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Controls & Dots
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Page Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    pages.indices.forEach { index ->
                        val isCurrent = index == currentStepIndex
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .width(if (isCurrent) 24.dp else 6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(
                                    if (isCurrent) {
                                        if (isIntroPage) Color(0xFF8F1825) else SoltarAmber
                                    } else {
                                        if (isIntroPage) Color(0xFFD5CDC3) else SoltarBorder
                                    }
                                )
                        )
                    }
                }

                // CTA Button
                Button(
                    onClick = {
                        if (currentStepIndex < totalSteps - 1) {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            currentStepIndex++
                        } else {
                            viewModel.playSound(SoltarSoundManager.SoundType.WARM_CHIME)
                            viewModel.completeOnboardingFlow(
                                userName = userNameInput,
                                userEmail = userEmailInput,
                                relDuration = selectedRelDuration,
                                timeSinceBreakup = selectedTimeSinceBreakup,
                                hasChildren = selectedHasChildren,
                                contactType = selectedContactType,
                                framework = selectedFramework,
                                breakupSituation = selectedBreakupSituation,
                                anticipatedGrief = selectedAnticipatedGrief,
                                emotionalSituation = selectedEmotionalSituation,
                                decisionMaker = selectedDecisionMaker,
                                breakupReason = selectedBreakupReason,
                                freeHistoryNotes = "",
                                cohabitation = selectedCohabitation,
                                marriedOrEngaged = false,
                                previousBreakupsCount = 0
                            )
                            onComplete()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("onboarding_primary_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isIntroPage) Color(0xFF8F1825) else SoltarAmber
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = when {
                                isIntroPage -> "Comenzar"
                                currentStepIndex < totalSteps - 1 -> "Siguiente"
                                else -> "Comenzar mi reconstrucción"
                            },
                            color = if (isIntroPage) Color.White else SoltarBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = if (isIntroPage) Color.White else SoltarBackground,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
