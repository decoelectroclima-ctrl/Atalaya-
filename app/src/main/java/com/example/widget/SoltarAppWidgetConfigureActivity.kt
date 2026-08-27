package com.example.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AdrianaDatabase
import com.example.data.SoltarFramework
import com.example.ui.theme.LocalSoltarColors
import com.example.ui.theme.SoltarTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SoltarAppWidgetConfigureActivity : ComponentActivity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set result to CANCELED initially so if the user backs out, widget creation is aborted gracefully
        setResult(RESULT_CANCELED)

        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        setContent {
            SoltarTheme {
                WidgetConfigureScreen(
                    appWidgetId = appWidgetId,
                    onSaveConfig = { config ->
                        saveAndFinish(config)
                    },
                    onCancel = {
                        finish()
                    }
                )
            }
        }
    }

    private fun saveAndFinish(config: SoltarWidgetConfig) {
        val context = applicationContext
        val effectiveId = if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) appWidgetId else 0

        // Save persistent config
        SoltarWidgetConfigManager.saveConfig(context, effectiveId, config)

        // Update the widget immediately
        val appWidgetManager = AppWidgetManager.getInstance(context)
        if (effectiveId != AppWidgetManager.INVALID_APPWIDGET_ID && effectiveId != 0) {
            SoltarAppWidgetProvider.updateAppWidget(context, appWidgetManager, effectiveId)
        } else {
            SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
        }

        // Return result
        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, effectiveId)
        }
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WidgetConfigureScreen(
    appWidgetId: Int,
    onSaveConfig: (SoltarWidgetConfig) -> Unit,
    onCancel: () -> Unit
) {
    val soltarColors = LocalSoltarColors.current
    val coroutineScope = rememberCoroutineScope()

    var config by remember { mutableStateOf(SoltarWidgetConfig()) }
    var loaded by remember { mutableStateOf(false) }
    var profileFramework by remember { mutableStateOf(SoltarFramework.PSICOLOGIA_MODERNA) }
    var userDays by remember { mutableIntStateOf(14) }
    var userName by remember { mutableStateOf("Viajero") }

    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(appWidgetId) {
        withContext(Dispatchers.IO) {
            val initialConfig = SoltarWidgetConfigManager.loadConfig(context, appWidgetId)
            try {
                val db = AdrianaDatabase.getDatabase(context)
                val settings = db.soltarSettingsDao().getSettingsOnce()
                if (settings != null) {
                    val diff = System.currentTimeMillis() - settings.breakupDateTimestamp
                    userDays = (java.util.concurrent.TimeUnit.MILLISECONDS.toDays(diff)).coerceAtLeast(0).toInt()
                    if (settings.userName.isNotBlank()) userName = settings.userName
                    profileFramework = try {
                        SoltarFramework.valueOf(settings.preferredFramework)
                    } catch (_: Exception) {
                        SoltarFramework.PSICOLOGIA_MODERNA
                    }
                }
            } catch (_: Exception) {}
            config = initialConfig
            loaded = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Configurar Widget ADRIANA",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            "Personaliza métricas y frases de pantalla",
                            style = MaterialTheme.typography.bodySmall,
                            color = soltarColors.textSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Cancelar",
                            tint = soltarColors.textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = soltarColors.surfaceElevated
                )
            )
        },
        bottomBar = {
            Surface(
                color = soltarColors.surfaceElevated,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancelar", color = soltarColors.textPrimary)
                    }

                    Button(
                        onClick = { onSaveConfig(config) },
                        modifier = Modifier.weight(1.5f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = soltarColors.amber,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Guardar Widget", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = soltarColors.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Live Preview of the Widget
            Text(
                "VISTA PREVIA EN VIVO",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = soltarColors.amber
            )

            WidgetLivePreviewCard(
                config = config,
                days = userDays,
                userName = userName,
                profileFramework = profileFramework
            )

            HorizontalDivider(color = soltarColors.borderSubtle)

            // 2. Quote Source Configuration
            Card(
                colors = CardDefaults.cardColors(containerColor = soltarColors.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, soltarColors.borderSubtle)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FormatQuote, contentDescription = null, tint = soltarColors.amber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Fuente de la Frase Diaria",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = soltarColors.textPrimary
                        )
                    }

                    Text(
                        "Elige qué cosmovisión o mensaje nutre tu widget cada día:",
                        style = MaterialTheme.typography.bodySmall,
                        color = soltarColors.textSecondary
                    )

                    val sources = listOf(
                        SoltarWidgetConfig.SOURCE_PROFILE to "📱 Según mi perfil en la app",
                        SoltarWidgetConfig.SOURCE_STOIC to "🏛️ Estoicismo (Marco Aurelio, Séneca, Epicteto)",
                        SoltarWidgetConfig.SOURCE_PSYCHOLOGY to "🧠 Psicología (Apego, Rolón, Congost, ACT)",
                        SoltarWidgetConfig.SOURCE_CATHOLIC to "✝️ Fe Católica (Proverbios, Salmos, San Agustín)",
                        SoltarWidgetConfig.SOURCE_CUSTOM to "✍️ Mantra o Afirmación Personalizada"
                    )

                    sources.forEach { (key, label) ->
                        val isSelected = config.quoteSource == key
                        Surface(
                            onClick = { config = config.copy(quoteSource = key) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) soltarColors.amber.copy(alpha = 0.15f) else soltarColors.surfaceElevated,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) soltarColors.amber else soltarColors.borderSubtle
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { config = config.copy(quoteSource = key) },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = soltarColors.amber,
                                        unselectedColor = soltarColors.textSecondary
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    label,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isSelected) soltarColors.amber else soltarColors.textPrimary
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = config.quoteSource == SoltarWidgetConfig.SOURCE_CUSTOM) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                "Escribe tu anclaje o frase personal:",
                                style = MaterialTheme.typography.labelSmall,
                                color = soltarColors.amber
                            )
                            OutlinedTextField(
                                value = config.customMantra,
                                onValueChange = { config = config.copy(customMantra = it) },
                                placeholder = { Text("Ej: «Hoy elijo mi paz sobre la urgencia y el impulso.»") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = soltarColors.amber,
                                    unfocusedBorderColor = soltarColors.borderSubtle
                                ),
                                maxLines = 3
                            )
                        }
                    }
                }
            }

            // 3. Metrics & Visibility Toggles
            Card(
                colors = CardDefaults.cardColors(containerColor = soltarColors.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, soltarColors.borderSubtle)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Visibility, contentDescription = null, tint = soltarColors.sage)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Métricas y Elementos Visibles",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = soltarColors.textPrimary
                        )
                    }

                    WidgetSwitchRow(
                        title = "Contador de Días de Soberanía",
                        subtitle = "Muestra el número principal de días sin contacto",
                        checked = config.showDaysCounter,
                        onCheckedChange = { config = config.copy(showDaysCounter = it) }
                    )

                    WidgetSwitchRow(
                        title = "Insignia de Enfoque Filosófico",
                        subtitle = "Etiqueta superior (Estoico, Psicología o Católico)",
                        checked = config.showFrameworkBadge,
                        onCheckedChange = { config = config.copy(showFrameworkBadge = it) }
                    )

                    WidgetSwitchRow(
                        title = "Insignia de Fase Emocional",
                        subtitle = "Etiqueta de etapa (Desintoxicación, Soberanía, Claridad)",
                        checked = config.showPhaseBadge,
                        onCheckedChange = { config = config.copy(showPhaseBadge = it) }
                    )

                    WidgetSwitchRow(
                        title = "Subtexto de Autonomía y Nombre",
                        subtitle = "Muestra '$userName • Autonomía y Paz'",
                        checked = config.showSubtext,
                        onCheckedChange = { config = config.copy(showSubtext = it) }
                    )

                    WidgetSwitchRow(
                        title = "Botón de Ajustes Rápidos ⚙️",
                        subtitle = "Permite reabrir esta configuración tocando el widget",
                        checked = config.showConfigureButton,
                        onCheckedChange = { config = config.copy(showConfigureButton = it) }
                    )
                }
            }

            // 4. Quick Action Buttons
            Card(
                colors = CardDefaults.cardColors(containerColor = soltarColors.surface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, soltarColors.borderSubtle)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = soltarColors.amber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Barra de Acciones Rápidas",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = soltarColors.textPrimary
                        )
                    }

                    WidgetSwitchRow(
                        title = "Mostrar Barra Inferior de Acciones",
                        subtitle = "Botones de acceso rápido a herramientas clave",
                        checked = config.showActionButtons,
                        onCheckedChange = { config = config.copy(showActionButtons = it) }
                    )

                    AnimatedVisibility(visible = config.showActionButtons) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 12.dp, top = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            WidgetSwitchRow(
                                title = "🚨 Botón SOS",
                                subtitle = "Acceso a respiración y contención de urgencia",
                                checked = config.showSosButton,
                                onCheckedChange = { config = config.copy(showSosButton = it) }
                            )

                            WidgetSwitchRow(
                                title = "💬 Botón Coach ADRIANA",
                                subtitle = "Apertura directa del asistente socrático",
                                checked = config.showCoachButton,
                                onCheckedChange = { config = config.copy(showCoachButton = it) }
                            )

                            WidgetSwitchRow(
                                title = "📖 Botón Diario",
                                subtitle = "Registro rápido de reflexiones y auditoría",
                                checked = config.showJournalButton,
                                onCheckedChange = { config = config.copy(showJournalButton = it) }
                            )

                            WidgetSwitchRow(
                                title = "✨ Botón Check-in",
                                subtitle = "Registro diario del balance anímico",
                                checked = config.showCheckinButton,
                                onCheckedChange = { config = config.copy(showCheckinButton = it) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun WidgetSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val soltarColors = LocalSoltarColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = soltarColors.textPrimary
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = soltarColors.textSecondary
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = soltarColors.amber,
                checkedTrackColor = soltarColors.amber.copy(alpha = 0.3f),
                uncheckedThumbColor = soltarColors.textSecondary,
                uncheckedTrackColor = soltarColors.surfaceElevated
            )
        )
    }
}

@Composable
fun WidgetLivePreviewCard(
    config: SoltarWidgetConfig,
    days: Int,
    userName: String,
    profileFramework: SoltarFramework
) {
    val soltarColors = LocalSoltarColors.current

    val phaseBadge = when {
        days < 7 -> "⚡ Desintox"
        days < 30 -> "🛡️ Soberanía"
        days < 60 -> "💡 Claridad"
        else -> "✨ Reconstrucción"
    }

    val frameworkText = when {
        config.quoteSource == SoltarWidgetConfig.SOURCE_CUSTOM -> "✨ Mantra"
        config.quoteSource == SoltarWidgetConfig.SOURCE_STOIC -> "🏛️ Estoico"
        config.quoteSource == SoltarWidgetConfig.SOURCE_CATHOLIC -> "✝️ Católico"
        config.quoteSource == SoltarWidgetConfig.SOURCE_PSYCHOLOGY -> "🧠 Psicología"
        profileFramework == SoltarFramework.ESTOICO -> "🏛️ Estoico"
        profileFramework == SoltarFramework.CATOLICO -> "✝️ Católico"
        else -> "🧠 Psicología"
    }

    val sampleQuote = when {
        config.quoteSource == SoltarWidgetConfig.SOURCE_CUSTOM && config.customMantra.isNotBlank() -> "«${config.customMantra}»"
        config.quoteSource == SoltarWidgetConfig.SOURCE_STOIC -> "«No son las cosas las que atormentan, sino el juicio sobre ellas.» — Epicteto"
        config.quoteSource == SoltarWidgetConfig.SOURCE_CATHOLIC -> "«Guarda tu corazón, porque de él brota la vida.» — Prov 4:23"
        config.quoteSource == SoltarWidgetConfig.SOURCE_PSYCHOLOGY -> "«El contacto cero es el quirófano donde tú sanas.» — Silvia Congost"
        else -> "«Sé dueño de tus decisiones y custodio de tu paz hoy.»"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF161412))
            .border(1.5.dp, soltarColors.amber.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "ADRIANA",
                    color = soltarColors.amber,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )

                if (config.showFrameworkBadge) {
                    Surface(
                        color = Color(0xFF2A241E),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            frameworkText,
                            color = soltarColors.amber,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                if (config.showPhaseBadge) {
                    Surface(
                        color = Color(0xFF222620),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            phaseBadge,
                            color = soltarColors.sage,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                if (config.showConfigureButton) {
                    Surface(
                        color = Color(0xFF2A241E),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "⚙️",
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Days Section
            if (config.showDaysCounter) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "$days",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            if (days == 1) "DÍA DE SOBERANÍA" else "DÍAS DE SOBERANÍA",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (config.showSubtext) {
                            Text(
                                "$userName • Autonomía y Paz",
                                color = Color(0xFFAAAAAA),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Quote
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                sampleQuote,
                color = Color(0xFFCCCCCC),
                fontSize = 10.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                maxLines = 2
            )

            // Action Buttons
            if (config.showActionButtons && (config.showSosButton || config.showCoachButton || config.showJournalButton || config.showCheckinButton)) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (config.showSosButton) {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3261E)),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        ) {
                            Text("🚨 SOS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    if (config.showCoachButton) {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A241E)),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        ) {
                            Text("💬 Coach", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = soltarColors.amber)
                        }
                    }
                    if (config.showJournalButton) {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A241E)),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        ) {
                            Text("📖 Diario", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    if (config.showCheckinButton) {
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A241E)),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        ) {
                            Text("✨ Check-in", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = soltarColors.sage)
                        }
                    }
                }
            }
        }
    }
}
