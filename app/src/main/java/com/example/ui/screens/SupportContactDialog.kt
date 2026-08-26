package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun SupportContactDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SoltarSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Contacto de Apoyo #${uiState.editingContactIndex}",
                        style = MaterialTheme.typography.titleMedium,
                        color = SoltarAmber,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }
                }

                Text(
                    text = "Añade a una persona de confianza (amigo íntimo, familiar, mentor o terapeuta) a quien acudir en momentos de crisis o impulso de contacto.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                // Name Input
                OutlinedTextField(
                    value = uiState.contactNameInput,
                    onValueChange = { viewModel.setContactName(it) },
                    label = { Text("Nombre o Alias") },
                    placeholder = { Text("Ej. Carlos") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = SoltarAmber)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("support_contact_name_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SoltarAmber,
                        unfocusedBorderColor = SoltarBorder,
                        focusedLabelColor = SoltarAmber,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                // Phone Input
                OutlinedTextField(
                    value = uiState.contactPhoneInput,
                    onValueChange = { viewModel.setContactPhone(it) },
                    label = { Text("Teléfono / WhatsApp") },
                    placeholder = { Text("Ej. +34 600 112 233") },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = SoltarAmber)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("support_contact_phone_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SoltarAmber,
                        unfocusedBorderColor = SoltarBorder,
                        focusedLabelColor = SoltarAmber,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                // Relationship / Role Input
                OutlinedTextField(
                    value = uiState.contactRelationshipInput,
                    onValueChange = { viewModel.setContactRelationship(it) },
                    label = { Text("Relación / Vínculo") },
                    placeholder = { Text("Ej. Amigo de confianza, Hermana, Terapeuta") },
                    leadingIcon = {
                        Icon(Icons.Default.Group, contentDescription = null, tint = SoltarAmber)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("support_contact_relation_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SoltarAmber,
                        unfocusedBorderColor = SoltarBorder,
                        focusedLabelColor = SoltarAmber,
                        unfocusedLabelColor = TextSecondary,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (uiState.contactNameInput.isNotBlank()) {
                        OutlinedButton(
                            onClick = {
                                viewModel.deleteSupportContact(uiState.editingContactIndex)
                                onDismiss()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, UrgeAlertRed)
                        ) {
                            Text("Borrar", color = UrgeAlertRed)
                        }
                    }

                    Button(
                        onClick = { viewModel.saveSupportContact() },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("support_contact_save_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                    ) {
                        Text("Guardar", color = SoltarBackground, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
