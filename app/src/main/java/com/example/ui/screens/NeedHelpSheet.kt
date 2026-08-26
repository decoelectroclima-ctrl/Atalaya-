package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoltarSoundManager
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeedHelpSheet(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SoltarSurface,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = SoltarBorderSubtle
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = UrgeAlertRed.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, UrgeAlertRed.copy(alpha = 0.5f)),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Sos,
                                contentDescription = null,
                                tint = UrgeAlertRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "CENTRO DE APOYO Y CONTENCIÓN",
                            style = MaterialTheme.typography.labelSmall,
                            color = UrgeAlertRed,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "¿Cómo necesitas recibir ayuda ahora?",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(32.dp).testTag("need_help_close")
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tener un impulso o sentir dolor agudo es normal. No tomes decisiones precipitadas. Elige una de estas opciones de contención:",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // =========================================================
            // OPCIÓN 1: RED DE APOYO EMOCIONAL PERSONAL
            // =========================================================
            val contacts = listOfNotNull(
                if (!settings?.contact1Name.isNullOrBlank()) Triple(settings?.contact1Name!!, settings?.contact1Phone ?: "", settings?.contact1Relationship ?: "") else null,
                if (!settings?.contact2Name.isNullOrBlank()) Triple(settings?.contact2Name!!, settings?.contact2Phone ?: "", settings?.contact2Relationship ?: "") else null,
                if (!settings?.contact3Name.isNullOrBlank()) Triple(settings?.contact3Name!!, settings?.contact3Phone ?: "", settings?.contact3Relationship ?: "") else null
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("help_option_support_network"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                border = BorderStroke(1.2.dp, SoltarSage.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = SoltarSage.copy(alpha = 0.15f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Groups, contentDescription = null, tint = SoltarSage, modifier = Modifier.size(22.dp))
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Mi Red de Apoyo Emocional",
                                style = MaterialTheme.typography.titleSmall,
                                color = SoltarSage,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Hablar con alguien de confianza rompe el aislamiento",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (contacts.isNotEmpty()) {
                        contacts.forEach { (name, phone, rel) ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = SoltarSurface,
                                border = BorderStroke(1.dp, SoltarBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(name, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                                        if (rel.isNotBlank()) {
                                            Text(rel, style = MaterialTheme.typography.labelSmall, color = SoltarSage, fontSize = 11.sp)
                                        }
                                    }

                                    if (phone.isNotBlank()) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            // Llamada telefónica
                                            IconButton(
                                                onClick = {
                                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                                    context.startActivity(intent)
                                                },
                                                modifier = Modifier
                                                    .size(38.dp)
                                                    .background(SoltarSage.copy(alpha = 0.15f), CircleShape)
                                            ) {
                                                Icon(Icons.Default.Phone, contentDescription = "Llamar a $name", tint = SoltarSage, modifier = Modifier.size(18.dp))
                                            }

                                            // WhatsApp
                                            IconButton(
                                                onClick = {
                                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                    val clean = phone.replace("+", "").replace(" ", "").replace("-", "").trim()
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$clean"))
                                                    context.startActivity(intent)
                                                },
                                                modifier = Modifier
                                                    .size(38.dp)
                                                    .background(SoltarAmber.copy(alpha = 0.15f), CircleShape)
                                            ) {
                                                Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "WhatsApp a $name", tint = SoltarAmber, modifier = Modifier.size(18.dp))
                                            }

                                            // SMS Rápido
                                            IconButton(
                                                onClick = {
                                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone")).apply {
                                                        putExtra("sms_body", "Hola, necesito apoyo en este momento. ¿Podemos hablar un momento?")
                                                    }
                                                    context.startActivity(intent)
                                                },
                                                modifier = Modifier
                                                    .size(38.dp)
                                                    .background(SoltarBlue.copy(alpha = 0.15f), CircleShape)
                                            ) {
                                                Icon(Icons.Default.Sms, contentDescription = "SMS a $name", tint = SoltarBlue, modifier = Modifier.size(18.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SoltarSurface,
                            border = BorderStroke(1.dp, SoltarBorderSubtle),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Aún no tienes contactos guardados en tu Red de Apoyo.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        onDismiss()
                                        viewModel.openSupportContactDialog(1)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SoltarSage),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.PersonAdd, contentDescription = null, tint = SoltarBackground, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Añadir contactos de apoyo", color = SoltarBackground, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =========================================================
            // OPCIÓN 2: PROTOCOLO SOMÁTICO GENÉRICO (MODO IMPULSO 20 MIN)
            // =========================================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.playSound(SoltarSoundManager.SoundType.URGE_ALERT)
                        onDismiss()
                        viewModel.openUrgeSheet()
                    }
                    .testTag("help_option_urge_protocol"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                border = BorderStroke(1.2.dp, UrgeAlertRed.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = UrgeAlertRed.copy(alpha = 0.15f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = UrgeAlertRed, modifier = Modifier.size(22.dp))
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Protocolo de Urgencia Somática",
                                style = MaterialTheme.typography.titleSmall,
                                color = UrgeAlertRed,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Modo Impulso de 20 minutos con temporizador guiado",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Guía estructurada en 6 fases para surfear la ola dopaminérgica, autorregular el sistema nervioso y proteger tu dignidad.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            viewModel.playSound(SoltarSoundManager.SoundType.URGE_ALERT)
                            onDismiss()
                            viewModel.openUrgeSheet()
                        },
                        modifier = Modifier.fillMaxWidth().height(42.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Iniciar Modo Impulso (20 min)", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // =========================================================
            // OPCIÓN 3: LÍNEAS DE CRISIS Y EMERGENCIAS
            // =========================================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Emergency, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                        Text(
                            text = "LÍNEAS DE CRISIS Y EMERGENCIAS (24/7)",
                            style = MaterialTheme.typography.labelSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Si estás en una situación de sufrimiento extremo o crisis:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        EmergencyCallButton(
                            title = "024 (España)",
                            phone = "024",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:024"))
                                context.startActivity(intent)
                            }
                        )
                        EmergencyCallButton(
                            title = "988 (USA/LatAm)",
                            phone = "988",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:988"))
                                context.startActivity(intent)
                            }
                        )
                        EmergencyCallButton(
                            title = "112 / 911",
                            phone = "112",
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EmergencyCallButton(
    title: String,
    phone: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(38.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, SoltarBorder),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Icon(Icons.Default.Phone, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(title, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}
