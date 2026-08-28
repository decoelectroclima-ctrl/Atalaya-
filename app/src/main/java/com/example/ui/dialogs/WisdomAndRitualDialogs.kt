package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.WisdomBank
import com.example.ui.SoltarViewModel

@Composable
fun WisdomLibraryDialog(viewModel: SoltarViewModel, onDismiss: () -> Unit) {
    val settings = viewModel.settings.collectAsState().value
    val framework = com.example.data.SoltarFramework.fromKey(settings?.preferredFramework)
    val cards = WisdomBank.cards.filter { it.framework == framework }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Biblioteca de Sabiduría") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                cards.forEach { card ->
                    Card(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(card.title, style = MaterialTheme.typography.titleMedium)
                            Text(card.quote, style = MaterialTheme.typography.bodyMedium)
                            Text("- ${card.author}", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        },
        confirmButton = { Button(onClick = onDismiss) { Text("Cerrar") } }
    )
}

@Composable
fun ClosingRitualDialog(viewModel: SoltarViewModel, onDismiss: () -> Unit) {
    var step by remember { mutableStateOf(0) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ritual de Cierre") },
        text = {
            Column {
                when(step) {
                    0 -> Text("Respira profundo y reflexiona sobre lo aprendido.")
                    1 -> Text("Agradece lo vivido y suelta el peso del pasado.")
                    2 -> Text("Define un límite claro para tu bienestar.")
                    3 -> Text("Visualiza tu futuro con esperanza.")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (step < 3) step++ else onDismiss()
            }) { Text(if (step < 3) "Siguiente" else "Finalizar") }
        }
    )
}
