package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SubscriptionPlan
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

import androidx.compose.ui.platform.LocalContext
import android.app.Activity

@Composable
fun PaywallDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val currentTier = settings?.subscriptionTier ?: "FREE"
    val isTrial = settings?.isTrialActive == true
    
    val premiumProductDetails by viewModel.premiumProductDetails.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SoltarBackground)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Surface(
                        color = SoltarAmber.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = SoltarAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ADRIANA PREMIUM",
                                style = MaterialTheme.typography.labelMedium,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hero Title
                Text(
                    text = "Acompañamiento sin límites para recuperar tu centro",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Una herramienta ética y rigurosa diseñada para devolverte la estabilidad, la dignidad y la autonomía.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Plan Selector Cards
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Plan Premium
                    PlanCard(
                        plan = SubscriptionPlan.PREMIUM_ONE_TIME,
                        isSelected = uiState.selectedSubscriptionPlan == SubscriptionPlan.PREMIUM_ONE_TIME,
                        onSelect = { viewModel.selectSubscriptionPlan(SubscriptionPlan.PREMIUM_ONE_TIME) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Premium Features List
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "¿Qué incluye ADRIANA Premium?",
                            style = MaterialTheme.typography.titleMedium,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold
                        )

                        FeatureRow(
                            icon = Icons.Default.ChatBubbleOutline,
                            title = "Coach ADRIANA Ilimitado",
                            subtitle = "Sin topes diarios de conversación para contener momentos de rumiación o angustia."
                        )
                        FeatureRow(
                            icon = Icons.Default.Science,
                            title = "Laboratorio Cognitivo y Auditorías",
                            subtitle = "Registro ilimitado de pensamientos, patrones vinculares y antídotos de idealización."
                        )
                        FeatureRow(
                            icon = Icons.Default.MailOutline,
                            title = "Cartas No Enviadas y Ceremonias",
                            subtitle = "Desahogo seguro con ritos simbólicos de cierre y archivo protegido."
                        )
                        FeatureRow(
                            icon = Icons.Default.Shield,
                            title = "Modo Impulso y Red de Apoyo 24/7",
                            subtitle = "Temporizador regulatorio de 20 min y llamada directa a tus contactos de confianza."
                        )
                        FeatureRow(
                            icon = Icons.Default.GraphicEq,
                            title = "Campanas Somáticas y Paisajes de Calma",
                            subtitle = "Sonidos basados en frecuencias de biorregulación para desacelerar la taquicardia."
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Primary CTA Button
                Button(
                    onClick = {
                        activity?.let {
                            premiumProductDetails?.let { details ->
                                viewModel.launchPurchase(it, details)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("paywall_purchase_button"),
                    enabled = !uiState.isProcessingPayment && premiumProductDetails != null,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                ) {
                    if (uiState.isProcessingPayment) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = SoltarBackground,
                            strokeWidth = 2.5.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Procesando...", color = SoltarBackground, fontWeight = FontWeight.Bold)
                    } else {
                        Text(
                            text = premiumProductDetails?.oneTimePurchaseOfferDetails?.formattedPrice
                                ?: premiumProductDetails?.subscriptionOfferDetails?.get(0)?.pricingPhases?.pricingPhaseList?.get(0)?.formattedPrice
                                ?: "Suscribirme",
                            color = SoltarBackground,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom actions (Restore & Terms)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { viewModel.restorePurchases() }) {
                        Text("Restaurar compras", color = TextSecondary, style = MaterialTheme.typography.bodySmall)
                    }

                    if (currentTier.startsWith("PREMIUM") || isTrial) {
                        TextButton(onClick = { viewModel.manageSubscriptionInGooglePlay(context) }) {
                            Text("Gestionar suscripción en Google Play", color = UrgeAlertRed, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Security & Dignity Footer
                Text(
                    text = "🔒 Separación estricta: Tus datos emocionales se procesan bajo cifrado local y jamás se cruzan con datos de facturación. Facturación gestionada mediante Google Play.",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun PlanCard(
    plan: SubscriptionPlan,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("plan_card_${plan.tierKey}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) SoltarSurfaceElevated else SoltarSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) SoltarAmber else SoltarBorder
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    if (plan.savingsBadge != null) {
                        Surface(
                            color = SoltarSage.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SoltarSage.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = plan.savingsBadge,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarSage,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(
                        text = plan.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = SoltarAmber,
                        unselectedColor = TextSecondary
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = plan.priceDisplay,
                    style = MaterialTheme.typography.headlineMedium,
                    color = SoltarAmber,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = plan.periodLabel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plan.billingDetail,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun FeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(SoltarAmber.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SoltarAmber,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}
