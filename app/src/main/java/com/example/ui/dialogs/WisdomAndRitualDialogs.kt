package com.example.ui.dialogs

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.SoltarFramework
import com.example.data.WisdomBank
import com.example.data.WisdomCard
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun WisdomLibraryDialog(viewModel: SoltarViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val checkins by viewModel.checkins.collectAsState()
    
    val startTs = settings?.breakupDateTimestamp ?: (System.currentTimeMillis() - (14L * 24 * 3600 * 1000))
    val days = ((System.currentTimeMillis() - startTs) / (24 * 3600 * 1000L)).coerceAtLeast(0L)
    val isMilestoneReached = days >= 30 || checkins.size >= 5

    var contributionInput by remember { mutableStateOf("") }
    var contributedCards by remember { mutableStateOf(listOf<WisdomCard>()) }
    var successMsg by remember { mutableStateOf<String?>(null) }

    val savedContributions by viewModel.wisdomContributions.collectAsState()

    val framework = uiState.preferredFramework
    val cards = WisdomBank.cards.filter { it.framework == framework }
    val filteredContributions = savedContributions.filter { it.framework == framework.key }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
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
                    Text(
                        text = "BIBLIOTECA DE SABIDURÍA VIVA (C4)",
                        style = MaterialTheme.typography.labelMedium,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Milestone contribution section (Banco Personal / Vivo)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, if (isMilestoneReached) SoltarAmber else SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (isMilestoneReached) "✨ Hito Alcanzado: Banco Personal de Sabiduría" else "🔒 Banco Personal (Se desbloquea al alcanzar 30 días o 5 check-ins)",
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isMilestoneReached) SoltarAmber else TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Guarda tus propias frases de resiliencia y claridad en tu banco personal para fortalecer tu proceso de reconstrucción.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )

                        if (isMilestoneReached) {
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = contributionInput,
                                onValueChange = { contributionInput = it },
                                label = { Text("Escribe tu frase de sabiduría...") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    if (contributionInput.isNotBlank()) {
                                        viewModel.saveWisdomContribution(framework.key, contributionInput.trim())
                                        contributionInput = ""
                                        successMsg = "✨ ¡Frase guardada en tu banco personal!"
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                            ) {
                                Text("Guardar en mi Banco Personal", color = SoltarBackground, fontWeight = FontWeight.Bold)
                            }
                            successMsg?.let {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(it, color = SoltarSage, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display Saved Contributions from Room
                if (filteredContributions.isNotEmpty()) {
                    Text("Tus Aportaciones Personales Guardadas:", style = MaterialTheme.typography.titleSmall, color = SoltarAmber, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    filteredContributions.forEach { item ->
                        val card = WisdomCard(
                            id = "saved_${item.id}",
                            framework = framework,
                            title = "Mi Banco Personal",
                            quote = item.quote,
                            author = item.author,
                            reflection = item.reflection
                        )
                        WisdomCardItem(card = card, context = context)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text("Sabiduría del Marco (${framework.title}):", style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                cards.forEach { card ->
                    WisdomCardItem(card = card, context = context)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun WisdomCardItem(card: WisdomCard, context: android.content.Context) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
        border = BorderStroke(1.dp, SoltarBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(card.title, style = MaterialTheme.typography.labelSmall, color = SoltarAmber, fontWeight = FontWeight.Bold)
                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "«${card.quote}» — ${card.author} (ADRIANA App)")
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Compartir sabiduría"))
                    },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Compartir tarjeta", tint = SoltarAmber, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(card.quote, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, lineHeight = 22.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text("- ${card.author}", style = MaterialTheme.typography.labelSmall, color = TextSecondary, modifier = Modifier.align(Alignment.End))
            Spacer(modifier = Modifier.height(4.dp))
            Text(card.reflection, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 12.sp)
        }
    }
}

@Composable
fun ClosingRitualDialog(viewModel: SoltarViewModel, onDismiss: () -> Unit) {
    val settings = viewModel.settings.collectAsState().value
    val checkins = viewModel.checkins.collectAsState().value
    val framework = SoltarFramework.fromKey(settings?.preferredFramework)

    val startTs = settings?.breakupDateTimestamp ?: (System.currentTimeMillis() - (14L * 24 * 3600 * 1000))
    val days = ((System.currentTimeMillis() - startTs) / (24 * 3600 * 1000L)).coerceAtLeast(0L)
    
    // Gating C5: desbloqueado solo si racha >= 3 días o al menos 3 check-ins recientes
    val isUnlocked = days >= 3 || checkins.size >= 3

    var step by remember { mutableIntStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isUnlocked) "Ritual de Cierre • ${framework.title}" else "🔒 Ritual Bloqueado") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                if (!isUnlocked) {
                    Text(
                        "El ritual de cierre es una ceremonia profunda que requiere haber recorrido al menos 3 días de proceso o acumulado check-ins estables para consolidar tu soberanía interior.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 22.sp
                    )
                } else {
                    when (framework) {
                        SoltarFramework.ESTOICO -> {
                            when (step) {
                                0 -> StepContent("Paso 1: Dicotomía de Control", "Reconoce con absoluta claridad qué dependía de ti en la relación y qué era completamente ajeno a tu voluntad. Libera la carga de lo que no pudiste gobernar.")
                                1 -> StepContent("Paso 2: Amor Fati (Aceptar el destino)", "Observa la ruptura no como una injusticia cruel, sino como el material estóico sobre el cual construirás tu fortaleza, templanza y sabiduría.")
                                2 -> StepContent("Paso 3: Apatheia (Soberanía de pasiones)", "Examina tus impulsos de búsqueda o nostalgia. Detente a sentir la emoción sin otorgarle el poder de dictar tus acciones.")
                                3 -> StepContent("Paso 4: La Ciudadela Interior", "Sella el ritual reafirmando que tu paz mental y tu dignidad son tu posesión más valiosa y nadie puede arrebatártelas.")
                            }
                        }
                        SoltarFramework.PSICOLOGIA_MODERNA -> {
                            when (step) {
                                0 -> StepContent("Paso 1: Procesamiento Emocional del Duelo", "Permítete sentir la tristeza y la abstinencia del apego sin juzgarte. Valida que el dolor es el trabajo biológico de reorganización cerebral.")
                                1 -> StepContent("Paso 2: Regulación del Sistema Nervioso", "Inhala profundamente exhalando el estrés acumulado. Tu cuerpo está saliendo del estado de alerta y alarma por separación.")
                                2 -> StepContent("Paso 3: Restructuración Cognitiva y Límites", "Identifica las narrativas idealizadas y sustitúyelas por el registro objetivo de los hechos vividos y las incompatibilidades reales.")
                                3 -> StepContent("Paso 4: Integración e Identidad Autónoma", "Consolida tu compromiso contigo mismo/a, reconectando con tus proyectos, valores y autonomía personal.")
                            }
                        }
                        SoltarFramework.CATOLICO -> {
                            when (step) {
                                0 -> StepContent("Paso 1: Examen de Conciencia y Entrega", "Coloca ante Dios tus cargas, tus heridas y tus expectativas no cumplidas. Entrégaselas en oración con confianza absoluta.")
                                1 -> StepContent("Paso 2: Perdón y Liberación", "Perdona de corazón a la otra persona y perdónate a ti mismo/a, liberando todo resentimiento para que tu alma recupere la paz.")
                                2 -> StepContent("Paso 3: Custodia del Corazón", "Decide guardar tu corazón con esperanza, sabiendo que tu dignidad como hijo/a de Dios está intacta y protegida.")
                                3 -> StepContent("Paso 4: Renovación en el Desierto", "Acepta este tiempo de prueba como un espacio de gracia donde tu fe y tu propósito se purifican y renuevan.")
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (isUnlocked) {
                Button(
                    onClick = {
                        if (step < 3) step++ else onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                ) {
                    Text(if (step < 3) "Siguiente Paso" else "Finalizar Ritual", color = SoltarBackground, fontWeight = FontWeight.Bold)
                }
            } else {
                Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)) {
                    Text("Entendido", color = SoltarBackground, fontWeight = FontWeight.Bold)
                }
            }
        }
    )
}

@Composable
fun VoluntaryExitDialog(viewModel: SoltarViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var reason by remember { mutableStateOf("Proceso completado con éxito") }
    var feedback by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar mi proceso de forma voluntaria", color = UrgeAlertRed, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    "Lamentamos verte partir, pero respetamos profundamente tu decisión de concluir este ciclo. Por favor, ayúdanos con una encuesta 100% anónima:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Motivo principal:", style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                listOf("Proceso de duelo superado", "Prefiero herramientas offline / físicas", "Descanso digital total", "Otro motivo").forEach { opt ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { reason = opt },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = reason == opt, onClick = { reason = opt })
                        Text(opt, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = feedback,
                    onValueChange = { feedback = it },
                    label = { Text("Comentarios constructivos (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.fullDataReset()
                    onDismiss()
                    val uninstallIntent = Intent(Intent.ACTION_DELETE).apply {
                        data = Uri.parse("package:${context.packageName}")
                    }
                    try {
                        context.startActivity(uninstallIntent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Datos borrados. Puedes desinstalar la app desde ajustes del sistema.", Toast.LENGTH_LONG).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = UrgeAlertRed)
            ) {
                Text("Borrar datos y desinstalar", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        },
        containerColor = SoltarSurfaceElevated,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun StepContent(title: String, desc: String) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleMedium, color = SoltarAmber, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = desc, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, lineHeight = 22.sp)
    }
}
