package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

data class OnboardingStep(
    val badge: String,
    val title: String,
    val subtitle: String,
    val quote: String,
    val icon: ImageVector,
    val accentColor: androidx.compose.ui.graphics.Color
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    viewModel: SoltarViewModel,
    onComplete: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }

    val steps = remember {
        listOf(
            OnboardingStep(
                badge = "FILOSOFÍA KINTSUGI",
                title = "Lo que se rompe puede ser más fuerte con oro",
                subtitle = "En Japón, la cerámica rota se repara con resina de oro. Las grietas no se ocultan: se iluminan. SOLTAR no es una app para olvidar; es un santuario para reconstruir tu dignidad personal.",
                quote = "«No eres débil por sentir dolor; estás atravesando la alquimia de tu propia reconstrucción.»",
                icon = Icons.Default.AutoAwesome,
                accentColor = SoltarAmber
            ),
            OnboardingStep(
                badge = "EL ANCLAJE",
                title = "Contacto Cero: Respeto implacable a tu paz",
                subtitle = "El contacto cero no es manipulación ni orgullo: es el tiempo biológico que tu sistema nervioso necesita para desintoxicarse de la dopamina intermitente y recuperar tu eje.",
                quote = "«Cada hora sin ceder al impulso es una victoria que le devuelve el mando a tu futuro.»",
                icon = Icons.Default.Shield,
                accentColor = SoltarSage
            ),
            OnboardingStep(
                badge = "SISTEMA DE PRECISIÓN",
                title = "Herramientas reales cuando la mente entra en pánico",
                subtitle = "Dispones de un protocolo somático de 20 minutos para impulsos agudos, laboratorio TCC para desarmar bucles, y auditorías objetivas de la relación.",
                quote = "«Las decisiones de tu vida las toma tu dignidad, no la desesperación del momento.»",
                icon = Icons.Default.Psychology,
                accentColor = SoltarTerracotta
            )
        )
    }

    val step = steps[currentStep]

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
            // Top Bar with Skip
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
                        text = "SOLTAR • ${currentStep + 1}/${steps.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoltarAmber,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                if (currentStep < steps.size - 1) {
                    TextButton(
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
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
                targetState = step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "onboarding_step"
            ) { targetStep ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    // Glowing Icon Container
                    Surface(
                        modifier = Modifier.size(96.dp),
                        shape = CircleShape,
                        color = targetStep.accentColor.copy(alpha = 0.12f),
                        border = BorderStroke(1.5.dp, targetStep.accentColor.copy(alpha = 0.5f))
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = targetStep.icon,
                                contentDescription = null,
                                tint = targetStep.accentColor,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = targetStep.badge,
                        style = MaterialTheme.typography.labelMedium,
                        color = targetStep.accentColor,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.4.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = targetStep.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = targetStep.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Text(
                            text = targetStep.quote,
                            style = MaterialTheme.typography.bodySmall,
                            color = SoltarAmberLight,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            // Bottom Controls & Dots
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Page Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    steps.indices.forEach { index ->
                        val isCurrent = index == currentStep
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
                        if (currentStep < steps.size - 1) {
                            viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                            currentStep++
                        } else {
                            viewModel.playSound(SoltarSoundManager.SoundType.WARM_CHIME)
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
                            text = if (currentStep < steps.size - 1) "Siguiente" else "Comenzar mi reconstrucción",
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
