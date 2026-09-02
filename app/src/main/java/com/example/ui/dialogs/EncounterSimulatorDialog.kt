package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun EncounterSimulatorDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    var selectedScenario by remember { mutableStateOf<String?>(null) }
    var chatMessages by remember { mutableStateOf(listOf<Pair<String, String>>()) }
    var userResponse by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (selectedScenario == null) "Simulacro de Encuentro" else "Simulando: $selectedScenario") },
        text = {
            if (selectedScenario == null) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    listOf("Encuentro casual", "Conversación pendiente", "Devolución de objetos", "Conversación emocional", "Poner límites").forEach { scenario ->
                        OutlinedButton(
                            onClick = { selectedScenario = scenario },
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) { Text(scenario) }
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.height(300.dp).verticalScroll(rememberScrollState())) {
                        chatMessages.forEach { (sender, msg) ->
                            Text(text = "$sender: $msg", modifier = Modifier.padding(4.dp))
                        }
                    }
                    OutlinedTextField(
                        value = userResponse,
                        onValueChange = { userResponse = it },
                        label = { Text("Tu respuesta") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            val msg = userResponse
                            userResponse = ""
                            scope.launch {
                                isLoading = true
                                chatMessages = chatMessages + ("Tú" to msg)
                                val response = viewModel.sendEncounterMessage(msg, chatMessages, selectedScenario!!)
                                chatMessages = chatMessages + ("Recuerda" to response.replyText)
                                isLoading = false
                            }
                        },
                        enabled = userResponse.isNotBlank() && !isLoading
                    ) {
                        Text(if (isLoading) "Pensando..." else "Enviar")
                    }
                }
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Cerrar") } }
    )
}
