package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun FounderExperienceDialog(viewModel: SoltarViewModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("La Visión del Fundador", color = SoltarAmber, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "ADRIANA no es solo una herramienta, es un compromiso con tu propia autonomía. " +
                           "Esta aplicación nació del reconocimiento de que el dolor no debe ser gestionado " +
                           "desde la desesperación, sino desde la dignidad y la razón.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Mi intención al crearla fue ofrecerte una estructura que sostenga tu sistema " +
                           "nervioso cuando tú no puedas, hasta que vuelvas a tener el control total.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)) {
                Text("Entendido", color = SoltarBackground)
            }
        }
    )
}
