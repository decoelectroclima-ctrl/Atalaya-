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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoltarSoundManager
import com.example.data.SoltarFramework
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

sealed class OnboardingPage {
    data class Info(
        val badge: String,
        val title: String,
        val subtitle: String,
        val quote: String,
        val icon: ImageVector,
        val accentColor: androidx.compose.ui.graphics.Color
    ) : OnboardingPage()

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
    var selectedFramework by remember(uiState.preferredFramework) { mutableStateOf(uiState.preferredFramework) }

    val pages: List<OnboardingPage> = remember {
        listOf(
            OnboardingPage.Info(
                badge = "FILOSOFÍA KINTSUGI",
                title = "Lo que se rompe puede ser más fuerte con oro",
                subtitle = "En Japón, la cerámica rota se repara con resina de oro. Las grietas no se ocultan: se iluminan. ADRIANA no es una app para olvidar; es un espacio para reconstruir tu dignidad y soberanía personal.",
                quote = "«No eres débil por sentir dolor; estás atravesando la alquimia de tu propia reconstrucción.»",
                icon = Icons.Default.AutoAwesome,
                accentColor = SoltarAmber
            ),
            OnboardingPage.FrameworkSelector,
            OnboardingPage.Info(
                badge = "EL ANCLAJE",
                title = "Contacto Cero: Respeto implacable a tu paz",
                subtitle = "El contacto cero no es manipulación ni orgullo: es el tiempo biológico que tu sistema nervioso necesita para desintoxicarse de la dopamina intermitente y recuperar tu eje.",
                quote = "«Cada hora sin ceder al impulso es una victoria que le devuelve el mando a tu futuro.»",
                icon = Icons.Default.Shield,
                accentColor = SoltarSage
            ),
            OnboardingPage.Info(
                badge = "SISTEMA DE PRECISIÓN",
                title = "Herramientas reales cuando la mente entra en pánico",
                subtitle = "Dispones de un protocolo somático de 20 minutos para impulsos agudos, laboratorio TCC para desarmar bucles, y auditorías objetivas de la relación.",
                quote = "«Las decisiones de tu vida las toma tu dignidad, no la desesperación del momento.»",
                icon = Icons.Default.Psychology,
                accentColor = SoltarTerracotta
            )
        )
    }

    val totalSteps = pages.size
    val currentPage = pages[currentStepIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoltarBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Step Counter & Skip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = SoltarSurfaceElevated,
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Text(
                        text = "ADRIANA • ${currentStepIndex + 1}/$totalSteps",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoltarAmber,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                if (currentStepIndex < totalSteps - 1) {
                    TextButton(
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            viewModel.setFramework(selectedFramework)
                            viewModel.setOnboardingCompleted(true)
                            onComplete()
                        }
                    ) {
                        Text("Saltar", color = TextSecondary, fontSize = 13.sp)
                    }
                }
            }

            // Main Animated Content
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "onboarding_page"
            ) { page ->
                when (page) {
                    is OnboardingPage.Info -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(90.dp),
                                shape = CircleShape,
                                color = page.accentColor.copy(alpha = 0.12f),
                                border = BorderStroke(1.5.dp, page.accentColor.copy(alpha = 0.5f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = page.icon,
                                        contentDescription = null,
                                        tint = page.accentColor,
                                        modifier = Modifier.size(42.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = page.badge,
                                style = MaterialTheme.typography.labelMedium,
                                color = page.accentColor,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.4.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = page.title,
                                style = MaterialTheme.typography.headlineSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                lineHeight = 28.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = page.subtitle,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                                border = BorderStroke(1.dp, SoltarBorder)
                            ) {
                                Text(
                                    text = page.quote,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoltarAmberLight,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp),
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    is OnboardingPage.FrameworkSelector -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
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
                                textAlign = TextAlign.Center,
                                lineHeight = 26.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Elige la perspectiva que guiará tus reflexiones diarias y las respuestas de tu coach. Podrás cambiarla cuando quieras desde tu Perfil sin afectar tu progreso.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                SoltarFramework.entries.forEach { framework ->
                                    val isSelected = selectedFramework == framework
                                    val borderColor = if (isSelected) SoltarAmber else SoltarBorder
                                    val bgColor = if (isSelected) SoltarAmber.copy(alpha = 0.08f) else SoltarSurface

                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                selectedFramework = framework
                                                viewModel.setFramework(framework)
                                            },
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(containerColor = bgColor),
                                        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(14.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = {
                                                    selectedFramework = framework
                                                    viewModel.setFramework(framework)
                                                },
                                                colors = RadioButtonDefaults.colors(
                                                    selectedColor = SoltarAmber,
                                                    unselectedColor = TextMuted
                                                )
                                            )

                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = framework.title,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = if (isSelected) SoltarAmber else TextPrimary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.height(3.dp))
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
                                .background(if (isCurrent) SoltarAmber else SoltarBorder)
                        )
                    }
                }

                // CTA Button
                Button(
                    onClick = {
                        if (currentStepIndex < totalSteps - 1) {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            if (currentPage is OnboardingPage.FrameworkSelector) {
                                viewModel.setFramework(selectedFramework)
                            }
                            currentStepIndex++
                        } else {
                            viewModel.playSound(SoltarSoundManager.SoundType.WARM_CHIME)
                            viewModel.setFramework(selectedFramework)
                            viewModel.setOnboardingCompleted(true)
                            onComplete()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("onboarding_primary_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (currentStepIndex < totalSteps - 1) "Siguiente" else "Comenzar mi reconstrucción",
                            color = SoltarBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = SoltarBackground,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

