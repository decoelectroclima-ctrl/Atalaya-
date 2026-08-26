package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.SoltarViewModel
import com.example.ui.theme.*

@Composable
fun AuthDialog(
    viewModel: SoltarViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SoltarBackground)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top close button & logo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = TextSecondary)
                    }

                    Surface(
                        color = SoltarAmber.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoltarAmber.copy(alpha = 0.3f))
                    ) {
                        Text(
                            text = "ADRIANA IDENTIDAD",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = SoltarAmber,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Brand Emblem
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SoltarAmber.copy(alpha = 0.15f))
                        .border(1.5.dp, SoltarAmber, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = SoltarAmber,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = when (uiState.authDialogMode) {
                        "LOGIN" -> "Bienvenido/a de nuevo"
                        "REGISTER" -> "Crear tu cuenta en ADRIANA"
                        else -> "Recuperar acceso"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = when (uiState.authDialogMode) {
                        "LOGIN" -> "Accede a tu espacio de acompañamiento, registros y red de apoyo."
                        "REGISTER" -> "Guarda tus avances de autonomía de forma segura y privada."
                        else -> "Introduce el correo electrónico con el que te registraste."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Mode Tabs
                if (uiState.authDialogMode != "FORGOT") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SoltarSurfaceElevated, RoundedCornerShape(12.dp))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val isLogin = uiState.authDialogMode == "LOGIN"
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isLogin) SoltarAmber else Color.Transparent)
                                .clickable { viewModel.setAuthDialogMode("LOGIN") }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Iniciar Sesión",
                                color = if (isLogin) SoltarBackground else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (!isLogin) SoltarAmber else Color.Transparent)
                                .clickable { viewModel.setAuthDialogMode("REGISTER") }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Registrarse",
                                color = if (!isLogin) SoltarBackground else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Form Fields
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SoltarSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SoltarBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Name Field (only in register)
                        if (uiState.authDialogMode == "REGISTER") {
                            OutlinedTextField(
                                value = uiState.authNameInput,
                                onValueChange = { viewModel.setAuthName(it) },
                                label = { Text("Nombre o alias") },
                                placeholder = { Text("Ej. Santiago") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = SoltarAmber)
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_name_field"),
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
                        }

                        // Email Field
                        OutlinedTextField(
                            value = uiState.authEmailInput,
                            onValueChange = { viewModel.setAuthEmail(it) },
                            label = { Text("Correo Electrónico") },
                            placeholder = { Text("ejemplo@correo.com") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = SoltarAmber)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = if (uiState.authDialogMode == "FORGOT") ImeAction.Done else ImeAction.Next
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("auth_email_field"),
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

                        // Password Field (Login and Register)
                        if (uiState.authDialogMode != "FORGOT") {
                            OutlinedTextField(
                                value = uiState.authPasswordInput,
                                onValueChange = { viewModel.setAuthPassword(it) },
                                label = { Text("Contraseña") },
                                placeholder = { Text("Mínimo 6 caracteres") },
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = SoltarAmber)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                                            tint = TextSecondary
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = if (uiState.authDialogMode == "REGISTER") ImeAction.Next else ImeAction.Done
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_password_field"),
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
                        }

                        // Confirm Password (only in register)
                        if (uiState.authDialogMode == "REGISTER") {
                            OutlinedTextField(
                                value = uiState.authConfirmPasswordInput,
                                onValueChange = { viewModel.setAuthConfirmPassword(it) },
                                label = { Text("Confirmar contraseña") },
                                placeholder = { Text("Repite tu contraseña") },
                                leadingIcon = {
                                    Icon(Icons.Default.LockReset, contentDescription = null, tint = SoltarAmber)
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("auth_confirm_password_field"),
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
                        }

                        // Forgot password link (in Login mode)
                        if (uiState.authDialogMode == "LOGIN") {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = { viewModel.setAuthDialogMode("FORGOT") },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = "¿Olvidaste tu contraseña?",
                                        color = SoltarAmber,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        // Submit Button
                        Button(
                            onClick = {
                                when (uiState.authDialogMode) {
                                    "LOGIN" -> viewModel.loginWithEmailPassword()
                                    "REGISTER" -> viewModel.registerWithEmailPassword()
                                    "FORGOT" -> viewModel.sendPasswordResetEmail()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("auth_submit_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SoltarAmber)
                        ) {
                            Text(
                                text = when (uiState.authDialogMode) {
                                    "LOGIN" -> "Iniciar Sesión"
                                    "REGISTER" -> "Crear Cuenta"
                                    else -> "Enviar Instrucciones"
                                },
                                color = SoltarBackground,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        if (uiState.authDialogMode == "FORGOT") {
                            TextButton(
                                onClick = { viewModel.setAuthDialogMode("LOGIN") },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Volver al inicio de sesión", color = TextSecondary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Privacy & Ethics Note
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = SoltarSage,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tus reflexiones y desahogos son privados y están cifrados localmente. Nunca se comparten con terceros.",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
