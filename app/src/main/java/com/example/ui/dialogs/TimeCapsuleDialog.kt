package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.TimeCapsuleEntity
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun TimeCapsuleDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var days by remember { mutableStateOf(30) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cápsula del Tiempo") },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Carta al yo futuro") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 5
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Desbloquear en:")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(30, 60, 90).forEach { dayOption ->
                        FilterChip(
                            selected = days == dayOption,
                            onClick = { days = dayOption },
                            label = { Text("$dayOption días") }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    val unlockTime = System.currentTimeMillis() + (days.toLong() * 24 * 60 * 60 * 1000)
                    viewModel.saveTimeCapsule(title, content, unlockTime)
                    viewModel.toggleTimeCapsuleModal(false)
                    onDismiss()
                }
            }) {
                Text("Guardar Cápsula")
            }
        }
    )
}
