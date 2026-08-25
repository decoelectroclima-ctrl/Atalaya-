package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun IdealizationDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = SoltarBackground,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoltarSurface)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("idealization_dialog_close")) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Text(
                        text = "ANTÍDOTO DE IDEALIZACIÓN",
                        style = MaterialTheme.typography.titleSmall,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    TextButton(
                        onClick = {
                            viewModel.saveIdealizationPair()
                            onDismiss()
                        },
                        enabled = uiState.idealizationMissInput.isNotBlank() && uiState.idealizationRealityInput.isNotBlank(),
                        modifier = Modifier.testTag("idealization_save_button")
                    ) {
                        Text(
                            "Guardar",
                            color = if (uiState.idealizationMissInput.isNotBlank() && uiState.idealizationRealityInput.isNotBlank()) SoltarAmber else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(4.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurfaceElevated),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Visibility, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(18.dp))
                            Text("Fantasía Selectiva vs Realidad Total", color = SoltarAmber, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Cuando extrañas, tu mente solo reproduce los momentos cumbre y borra la soledad o el malestar que sentías en lo cotidiano. Este ejercicio restituye la memoria completa.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                // 1. Lo que la mente romantiza
                Column {
                    Text(
                        text = "1. Lo que mi mente extraña o romantiza hoy",
                        style = MaterialTheme.typography.labelMedium,
                        color = SoltarTerracotta,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.idealizationMissInput,
                        onValueChange = viewModel::setIdealizationMiss,
                        placeholder = { Text("Ej: 'Las risas de los primeros meses' o 'Sentirme acompañado los domingos'", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("idealization_input_miss"),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarTerracotta,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // 2. La realidad completa que también existía
                Column {
                    Text(
                        text = "2. La realidad completa que también ocurría",
                        style = MaterialTheme.typography.labelMedium,
                        color = SoltarSage,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = uiState.idealizationRealityInput,
                        onValueChange = viewModel::setIdealizationReality,
                        placeholder = { Text("Ej: La ansiedad constante de no saber cuándo cambiaría su humor, los silencios hirientes, o sentir que remaba solo", color = TextMuted, fontSize = 13.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("idealization_input_reality"),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SoltarSage,
                            unfocusedBorderColor = SoltarBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Button(
                    onClick = {
                        viewModel.saveIdealizationPair()
                        onDismiss()
                    },
                    enabled = uiState.idealizationMissInput.isNotBlank() && uiState.idealizationRealityInput.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("idealization_submit_cta"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber, contentColor = SoltarBackground)
                ) {
                    Text("Anclar contraste a la realidad", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
