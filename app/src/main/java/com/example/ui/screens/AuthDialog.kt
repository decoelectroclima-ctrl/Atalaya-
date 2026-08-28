package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.auth.AuthViewModel
import com.example.ui.theme.*

@Composable
fun AuthDialog(
    viewModel: AuthViewModel,
    isLockdown: Boolean = false,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showMessage by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = {
            if (!isLockdown) onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = !isLockdown,
            dismissOnClickOutside = !isLockdown
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SoltarBackground)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!isLockdown) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                        }
                    } else {
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(SoltarAmber.copy(alpha = 0.15f))
                        .border(2.dp, SoltarAmber, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = SoltarAmber, modifier = Modifier.size(40.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = if (uiState.authDialogMode == "LOGIN") "Acceso ADRIANA" else "Configurar PIN",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (uiState.authDialogMode == "LOGIN")
                        "Introduce tu PIN de 4 dígitos para acceder a tu diario privado."
                    else
                        "Crea tu PIN de 4 dígitos para proteger tu proceso y privacidad.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.authDialogMode == "REGISTER") {
                    OutlinedTextField(
                        value = uiState.authNameInput,
                        onValueChange = { viewModel.setAuthName(it) },
                        label = { Text("Tu Nombre o Alias") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SoltarAmber)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // PIN Input
                OutlinedTextField(
                    value = uiState.pinInput,
                    onValueChange = { viewModel.setPin(it) },
                    label = { Text(if (uiState.authDialogMode == "LOGIN") "PIN (4 dígitos)" else "Nuevo PIN (4 dígitos)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SoltarAmber)
                )

                if (uiState.authDialogMode == "REGISTER") {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = uiState.confirmPinInput,
                        onValueChange = { viewModel.setConfirmPin(it) },
                        label = { Text("Confirmar PIN") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = SoltarAmber)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        showMessage = null
                        if (uiState.authDialogMode == "LOGIN") {
                            viewModel.loginWithPin { success, msg ->
                                if (success) onDismiss() else showMessage = msg
                            }
                        } else {
                            viewModel.registerPin { success, msg ->
                                if (success) onDismiss() else showMessage = msg
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.8f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                ) {
                    Text(
                        text = if (uiState.authDialogMode == "LOGIN") "Acceder" else "Guardar PIN",
                        color = SoltarBackground,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Toggle Mode Button (Only allowed if no PIN is configured yet and not in lockdown)
                if (!uiState.hasConfiguredPin && !isLockdown) {
                    TextButton(
                        onClick = {
                            showMessage = null
                            if (uiState.authDialogMode == "LOGIN") {
                                viewModel.setAuthDialogMode("REGISTER")
                            } else {
                                viewModel.setAuthDialogMode("LOGIN")
                            }
                        }
                    ) {
                        Text(
                            text = if (uiState.authDialogMode == "LOGIN")
                                "¿No tienes PIN? Crear nuevo PIN / Registrarse"
                            else
                                "¿Ya tienes un PIN configurado? Iniciar Sesión",
                            color = SoltarAmber,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                
                showMessage?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Privacy Note
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = SoltarSage, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tus datos están cifrados localmente.", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
