package com.example.ui.dialogs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.JournalEntryEntity
import com.example.data.SoltarFramework
import com.example.audio.SoltarSoundManager
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalJournalDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val journalEntries by viewModel.journalEntries.collectAsState()
    val context = LocalContext.current

    var activeTab by remember { mutableStateOf(if (uiState.selectedJournalEntry != null) 1 else 0) } // 0 = Redactar, 1 = Historial
    var viewingEntryDetail by remember { mutableStateOf(uiState.selectedJournalEntry) }

    val moodTags = listOf(
        "🌿 Calma",
        "🥀 Nostalgia",
        "⚡ Ansiedad",
        "💡 Claridad",
        "🌧️ Duelo",
        "✨ Gratitud",
        "🛡️ Valentía",
        "🌪️ Confusión"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "DIARIO PERSONAL",
                                style = MaterialTheme.typography.labelSmall,
                                color = SoltarAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.2.sp
                            )
                            Text(
                                text = if (viewingEntryDetail != null) "Reflexión & Mentoría" else "Registro & Mentoría Filosófica",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (viewingEntryDetail != null) {
                                    viewingEntryDetail = null
                                    viewModel.selectJournalEntry(null)
                                } else {
                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                    onDismiss()
                                }
                            },
                            modifier = Modifier.testTag("journal_back_button")
                        ) {
                            Icon(
                                imageVector = if (viewingEntryDetail != null) Icons.AutoMirrored.Filled.ArrowBack else Icons.Default.Close,
                                contentDescription = "Cerrar o volver",
                                tint = TextSecondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = SoltarSurfaceElevated
                    )
                )
            },
            containerColor = SoltarBackground
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // If not in full detail view, show Tab Selector
                if (viewingEntryDetail == null) {
                    TabRow(
                        selectedTabIndex = activeTab,
                        containerColor = SoltarSurface,
                        contentColor = SoltarAmber,
                        divider = { HorizontalDivider(color = SoltarBorder) }
                    ) {
                        Tab(
                            selected = activeTab == 0,
                            onClick = {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                activeTab = 0
                            },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Escribir", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        )
                        Tab(
                            selected = activeTab == 1,
                            onClick = {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                activeTab = 1
                            },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Book, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Mis Entradas (${journalEntries.size})", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        )
                    }
                }

                // Main Content
                Box(modifier = Modifier.fillMaxSize()) {
                    if (viewingEntryDetail != null) {
                        // Detail View of a specific journal entry
                        JournalEntryDetailView(
                            entry = viewingEntryDetail!!,
                            isGeneratingMentorship = uiState.isGeneratingJournalMentorship,
                            onRegenerateFramework = { framework ->
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                viewModel.requestMentorshipForExistingEntry(viewingEntryDetail!!, framework)
                            },
                            onDelete = {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                viewModel.deleteJournalEntry(viewingEntryDetail!!.id)
                                viewingEntryDetail = null
                            },
                            onCopy = { textToCopy ->
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Diario Adriana", textToCopy)
                                clipboard.setPrimaryClip(clip)
                                viewModel.showNotification("📋 Copiado al portapapeles")
                            }
                        )
                    } else if (activeTab == 0) {
                        // Compose / New Entry Tab
                        JournalWriteView(
                            viewModel = viewModel,
                            uiState = uiState,
                            moodTags = moodTags,
                            onEntrySaved = {
                                activeTab = 1
                            }
                        )
                    } else {
                        // History / List Tab
                        JournalHistoryListView(
                            entries = journalEntries,
                            onSelectEntry = { entry ->
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                viewingEntryDetail = entry
                                viewModel.selectJournalEntry(entry)
                            },
                            onAddNew = {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                activeTab = 0
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JournalWriteView(
    viewModel: SoltarViewModel,
    uiState: com.example.ui.SoltarUiState,
    moodTags: List<String>,
    onEntrySaved: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ESPACIO DE DESAHOGO Y CLARIDAD",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Vuelca tus pensamientos sin juicio. Al guardar, puedes solicitar una mentoría filosófica basada en tu escrito.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // 1. Selector de Estado / Emoción
        item {
            Column {
                Text(
                    text = "¿CÓMO TE SIENTES AL ESCRIBIR ESTO?",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(moodTags) { mood ->
                        val isSelected = uiState.journalInputMood == mood
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                viewModel.setJournalInputMood(mood)
                            },
                            label = { Text(mood, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoltarAmber,
                                selectedLabelColor = SoltarBackground,
                                containerColor = SoltarSurfaceElevated,
                                labelColor = TextPrimary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) SoltarAmber else SoltarBorder
                            )
                        )
                    }
                }
            }
        }

        // 2. Selector de Marco de Mentoría Filosófica
        item {
            Column {
                Text(
                    text = "ESTILO DE MENTORÍA FILOSÓFICA:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val frameworks = listOf(
                        Triple(SoltarFramework.ESTOICO, "🏛️ Estoicismo", "Dicotomía del control y fortaleza"),
                        Triple(SoltarFramework.PSICOLOGIA_MODERNA, "🧠 Psicología", "Apego, duelo y límites"),
                        Triple(SoltarFramework.CATOLICO, "🕊️ Trascendente", "Esperanza y custodia del corazón")
                    )

                    frameworks.forEach { (fw, label, _) ->
                        val isSelected = uiState.journalInputFramework == fw
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                                    viewModel.setJournalInputFramework(fw)
                                },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) SoltarAmber.copy(alpha = 0.15f) else SoltarSurface,
                            border = BorderStroke(1.dp, if (isSelected) SoltarAmber else SoltarBorder)
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) SoltarAmber else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Título Opcional
        item {
            OutlinedTextField(
                value = uiState.journalInputTitle,
                onValueChange = { viewModel.setJournalInputTitle(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("journal_title_input"),
                label = { Text("Título de tu reflexión (opcional)") },
                placeholder = { Text("Ej. Noche de dudas, Una pequeña victoria, etc.") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SoltarAmber,
                    unfocusedBorderColor = SoltarBorder,
                    focusedContainerColor = SoltarSurface,
                    unfocusedContainerColor = SoltarSurface
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // 4. Contenido del Diario
        item {
            OutlinedTextField(
                value = uiState.journalInputContent,
                onValueChange = { viewModel.setJournalInputContent(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 180.dp, max = 320.dp)
                    .testTag("journal_content_input"),
                label = { Text("Tus pensamientos y vivencias") },
                placeholder = {
                    Text("Escribe libremente todo lo que está pasando por tu mente. No te juzgues ni censures tus emociones...")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SoltarAmber,
                    unfocusedBorderColor = SoltarBorder,
                    focusedContainerColor = SoltarSurface,
                    unfocusedContainerColor = SoltarSurface
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // 5. Botones de Acción
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        viewModel.playSound(SoltarSoundManager.SoundType.WARM_CHIME)
                        viewModel.saveJournalEntry(
                            title = uiState.journalInputTitle,
                            content = uiState.journalInputContent,
                            moodTag = uiState.journalInputMood,
                            framework = uiState.journalInputFramework,
                            requestMentorship = true
                        )
                        onEntrySaved()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("save_and_mentorship_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                    enabled = uiState.journalInputContent.isNotBlank() && !uiState.isGeneratingJournalMentorship
                ) {
                    if (uiState.isGeneratingJournalMentorship) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = SoltarBackground,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generando mentoría...", color = SoltarBackground, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarBackground)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar y Recibir Mentoría Filosófica", color = SoltarBackground, fontWeight = FontWeight.Bold)
                    }
                }

                OutlinedButton(
                    onClick = {
                        viewModel.playSound(SoltarSoundManager.SoundType.TAP)
                        viewModel.saveJournalEntry(
                            title = uiState.journalInputTitle,
                            content = uiState.journalInputContent,
                            moodTag = uiState.journalInputMood,
                            framework = uiState.journalInputFramework,
                            requestMentorship = false
                        )
                        onEntrySaved()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_private_only_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SoltarBorder),
                    enabled = uiState.journalInputContent.isNotBlank()
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Guardar solo como apunte privado", color = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun JournalHistoryListView(
    entries: List<JournalEntryEntity>,
    onSelectEntry: (JournalEntryEntity) -> Unit,
    onAddNew: () -> Unit
) {
    if (entries.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = SoltarAmber.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Tu diario está en blanco",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Registrar lo que sientes te ayuda a separar los hechos de la angustia y a construir un archivo de tu evolución.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onAddNew,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = SoltarBackground)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Escribir mi primera reflexión", color = SoltarBackground, fontWeight = FontWeight.Bold)
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "HISTORIAL DE REFLEXIONES (${entries.size})",
                        style = MaterialTheme.typography.labelSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    TextButton(onClick = onAddNew) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = SoltarAmber)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Nueva entrada", color = SoltarAmber, fontSize = 13.sp)
                    }
                }
            }

            items(entries, key = { it.id }) { entry ->
                JournalEntryCard(
                    entry = entry,
                    onClick = { onSelectEntry(entry) }
                )
            }
        }
    }
}

@Composable
private fun JournalEntryCard(
    entry: JournalEntryEntity,
    onClick: () -> Unit
) {
    val dateStr = remember(entry.timestamp) {
        SimpleDateFormat("d MMM yyyy, HH:mm", Locale.forLanguageTag("es-ES")).format(Date(entry.timestamp))
    }

    val hasMentorship = entry.aiFeedback.isNotBlank()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("journal_entry_item_${entry.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SoltarSurface),
        border = BorderStroke(1.dp, if (hasMentorship) SoltarAmber.copy(alpha = 0.4f) else SoltarBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = SoltarSurfaceElevated
                    ) {
                        Text(
                            text = entry.moodTag,
                            style = MaterialTheme.typography.labelSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    if (hasMentorship) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SoltarAmber.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Mentoría", fontSize = 11.sp, color = SoltarAmber, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }

            if (entry.title.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                maxLines = 3,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            if (entry.aiCorePrinciple.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = SoltarBorder.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.aiCorePrinciple,
                    style = MaterialTheme.typography.bodySmall,
                    color = SoltarAmber,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun JournalEntryDetailView(
    entry: JournalEntryEntity,
    isGeneratingMentorship: Boolean,
    onRegenerateFramework: (SoltarFramework) -> Unit,
    onDelete: () -> Unit,
    onCopy: (String) -> Unit
) {
    val dateStr = remember(entry.timestamp) {
        SimpleDateFormat("d 'de' MMMM 'de' yyyy, HH:mm", Locale.forLanguageTag("es-ES")).format(Date(entry.timestamp))
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("¿Eliminar entrada de diario?") },
            text = { Text("Esta reflexión y su mentoría se eliminarán de tu registro local.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    onDelete()
                }) {
                    Text("Eliminar", color = UrgeAlertRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            },
            containerColor = SoltarSurfaceElevated
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado de la Entrada
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SoltarSurfaceElevated,
                        border = BorderStroke(1.dp, SoltarBorder)
                    ) {
                        Text(
                            text = entry.moodTag,
                            style = MaterialTheme.typography.labelSmall,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    Row {
                        IconButton(
                            onClick = {
                                val fullText = buildString {
                                    if (entry.title.isNotBlank()) appendLine(entry.title)
                                    appendLine(dateStr)
                                    appendLine(entry.content)
                                    if (entry.aiFeedback.isNotBlank()) {
                                        appendLine("\n--- MENTORÍA FILOSÓFICA ---")
                                        appendLine(entry.aiCorePrinciple)
                                        appendLine(entry.aiFeedback)
                                        if (entry.aiSocraticQuestion.isNotBlank()) {
                                            appendLine("\nPregunta socrática: ${entry.aiSocraticQuestion}")
                                        }
                                    }
                                }
                                onCopy(fullText)
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", tint = TextSecondary)
                        }
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = TextMuted)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )

                if (entry.title.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = entry.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Texto del Usuario
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                border = BorderStroke(1.dp, SoltarBorder)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "TU ESCRITO:",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = entry.content,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        lineHeight = 24.sp
                    )
                }
            }
        }

        // Bloque de Mentoría Filosófica
        item {
            if (entry.aiFeedback.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(SoltarAmber.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = SoltarAmber,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "MENTORÍA FILOSÓFICA",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = SoltarAmber,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "Perspectiva ${entry.philosophicalFramework}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }

                        // Cita / Principio rector
                        if (entry.aiCorePrinciple.isNotBlank()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = SoltarAmber.copy(alpha = 0.08f),
                                border = BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.25f))
                            ) {
                                Text(
                                    text = entry.aiCorePrinciple,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = SoltarAmber,
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }

                        // Cuerpo de la reflexión
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = entry.aiFeedback,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary,
                            lineHeight = 22.sp
                        )

                        // Pregunta Socrática
                        if (entry.aiSocraticQuestion.isNotBlank()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                                border = BorderStroke(1.dp, SoltarSage.copy(alpha = 0.4f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.HelpOutline,
                                            contentDescription = null,
                                            tint = SoltarSage,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "PREGUNTA DE AUTOINDAGACIÓN",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SoltarSage,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = entry.aiSocraticQuestion,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Micro-acción sugerida
                        if (entry.aiConcreteAction.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.CheckCircleOutline,
                                    contentDescription = null,
                                    tint = SoltarAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Micro-acción: ${entry.aiConcreteAction}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Selector de re-enfoque filosófico
                        Spacer(modifier = Modifier.height(18.dp))
                        HorizontalDivider(color = SoltarBorder)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "CAMBIAR PERSPECTIVA FILOSÓFICA:",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val options = listOf(
                                SoltarFramework.ESTOICO to "🏛️ Estoica",
                                SoltarFramework.PSICOLOGIA_MODERNA to "🧠 Psicología",
                                SoltarFramework.CATOLICO to "🕊️ Trascendente"
                            )
                            options.forEach { (fw, label) ->
                                val isCurrent = entry.philosophicalFramework == fw.name
                                OutlinedButton(
                                    onClick = { onRegenerateFramework(fw) },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, if (isCurrent) SoltarAmber else SoltarBorder),
                                    enabled = !isGeneratingMentorship,
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        color = if (isCurrent) SoltarAmber else TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Si la entrada se guardó como privada sin mentoría
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = SoltarAmber,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Esta entrada no tiene mentoría activa",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Puedes solicitar una retroalimentación reflexiva ahora mismo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onRegenerateFramework(SoltarFramework.ESTOICO) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber),
                            enabled = !isGeneratingMentorship
                        ) {
                            if (isGeneratingMentorship) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = SoltarBackground)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Analizando...", color = SoltarBackground)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SoltarBackground, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Solicitar Mentoría Filosófica", color = SoltarBackground, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
