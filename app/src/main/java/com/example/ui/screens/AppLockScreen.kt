package com.example.ui.screens

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

@Composable
fun AppLockScreen(onUnlocked: () -> Unit) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    var errorMessage by remember { mutableStateOf<String?>(null) }

    fun launchPrompt() {
        if (activity == null) return
        val biometricManager = BiometricManager.from(activity)
        val allowedAuthenticators = BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
        val canAuth = biometricManager.canAuthenticate(allowedAuthenticators)
        if (canAuth != BiometricManager.BIOMETRIC_SUCCESS) {
            // El dispositivo no tiene ningun bloqueo configurado (ni huella ni PIN del sistema).
            // No podemos exigir un bloqueo que el propio telefono no soporta - dejamos pasar,
            // no atrapamos a nadie por una limitacion del hardware/configuracion del dispositivo.
            onUnlocked()
            return
        }
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Recuerda está bloqueada")
            .setSubtitle("Usa tu huella, rostro o el bloqueo de tu teléfono para continuar")
            .setAllowedAuthenticators(allowedAuthenticators)
            .build()
        val executor = ContextCompat.getMainExecutor(activity)
        val prompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onUnlocked()
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                errorMessage = "No se pudo desbloquear. Inténtalo de nuevo."
            }
            override fun onAuthenticationFailed() {
                errorMessage = "No reconocido. Inténtalo de nuevo."
            }
        })
        prompt.authenticate(promptInfo)
    }

    LaunchedEffect(Unit) { launchPrompt() }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Recuerda está bloqueada", style = MaterialTheme.typography.titleMedium)
            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { errorMessage = null; launchPrompt() }) {
                Text("Desbloquear")
            }
        }
    }
}
