package com.example.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AdrianaDatabase
import com.example.data.SoltarRepository
import com.example.data.SoltarSettingsEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

data class AuthUiState(
    val isAuthDialogVisible: Boolean = false,
    val authDialogMode: String = "LOGIN", // "LOGIN" | "REGISTER"
    val pinInput: String = "",
    val confirmPinInput: String = "",
    val authNameInput: String = "",
    val failedAttempts: Int = 0,
    val lockoutUntilMillis: Long = 0L,
    val hasConfiguredPin: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SoltarRepository = SoltarRepository(AdrianaDatabase.getDatabase(application))

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val prefs = getApplication<Application>().getSharedPreferences("atalaya_security_prefs", android.content.Context.MODE_PRIVATE)
            val persistedAttempts = prefs.getInt("failed_attempts", 0)
            val persistedLockout = prefs.getLong("lockout_until", 0L)
            _uiState.update { it.copy(failedAttempts = persistedAttempts, lockoutUntilMillis = persistedLockout) }

            repository.settings.collect { settings ->
                val hasPin = !settings?.pinHash.isNullOrBlank()
                _uiState.update { it.copy(hasConfiguredPin = hasPin) }
            }
        }
    }

    fun openAuthDialog(mode: String = "LOGIN") {
        _uiState.update {
            it.copy(
                isAuthDialogVisible = true,
                authDialogMode = mode,
                pinInput = "",
                confirmPinInput = "",
                authNameInput = ""
            )
        }
    }

    fun closeAuthDialog() {
        _uiState.update { it.copy(isAuthDialogVisible = false) }
    }

    fun setPin(pin: String) {
        if (pin.length <= 4 && pin.all { it.isDigit() }) {
            _uiState.update { it.copy(pinInput = pin) }
        }
    }

    fun setConfirmPin(pin: String) {
        if (pin.length <= 4 && pin.all { it.isDigit() }) {
            _uiState.update { it.copy(confirmPinInput = pin) }
        }
    }

    fun setAuthName(name: String) = _uiState.update { it.copy(authNameInput = name) }

    fun setAuthDialogMode(mode: String) {
        _uiState.update { it.copy(authDialogMode = mode, pinInput = "", confirmPinInput = "") }
    }

    private fun getOrCreateSalt(): ByteArray {
        val prefs = getApplication<Application>().getSharedPreferences("atalaya_security_prefs", android.content.Context.MODE_PRIVATE)
        val storedSaltHex = prefs.getString("pin_salt_hex", null)
        if (!storedSaltHex.isNullOrBlank()) {
            return storedSaltHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        }
        val secureRandom = java.security.SecureRandom()
        val newSalt = ByteArray(16)
        secureRandom.nextBytes(newSalt)
        val hex = newSalt.joinToString("") { "%02x".format(it) }
        prefs.edit().putString("pin_salt_hex", hex).apply()
        return newSalt
    }

    private fun hashPinWithSalt(pin: String, salt: ByteArray): String {
        val spec = PBEKeySpec(pin.toCharArray(), salt, 50000, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        return hash.joinToString("") { "%02x".format(it) }
    }

    private fun hashPinLegacy(pin: String): String {
        val spec = PBEKeySpec(pin.toCharArray(), "salt".toByteArray(), 10000, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun registerPin(onResult: (Boolean, String) -> Unit) {
        val state = _uiState.value
        val pin = state.pinInput
        val confirm = state.confirmPinInput
        val name = state.authNameInput

        if (pin.length != 4 || !pin.all { it.isDigit() }) {
            onResult(false, "El PIN debe tener exactamente 4 dígitos numéricos.")
            return
        }

        if (pin != confirm) {
            onResult(false, "Los PINs no coinciden. Vuelve a introducirlos.")
            return
        }

        val salt = getOrCreateSalt()
        val pinHash = hashPinWithSalt(pin, salt)

        viewModelScope.launch {
            val current = repository.getSettingsOnce() ?: SoltarSettingsEntity()
            repository.saveSettings(
                current.copy(
                    pinHash = pinHash,
                    isLoggedIn = true,
                    biometricLockEnabled = true,
                    userName = if (name.isNotBlank()) name else current.userName.ifBlank { "Viajero" }
                )
            )
            _uiState.update { it.copy(failedAttempts = 0, lockoutUntilMillis = 0L, hasConfiguredPin = true) }
            closeAuthDialog()
            onResult(true, "Cuenta y PIN de seguridad configurados correctamente.")
        }
    }

    fun loginWithPin(onResult: (Boolean, String) -> Unit) {
        val state = _uiState.value
        val now = System.currentTimeMillis()

        // Brute-force lockout check
        if (state.lockoutUntilMillis > now) {
            val remainingSec = ((state.lockoutUntilMillis - now) / 1000).coerceAtLeast(1)
            onResult(false, "Demasiados intentos fallidos. Bloqueado temporalmente por $remainingSec segundos.")
            return
        }

        val pin = state.pinInput
        if (pin.length != 4 || !pin.all { it.isDigit() }) {
            onResult(false, "Introduce los 4 dígitos numéricos del PIN.")
            return
        }

        viewModelScope.launch {
            val current = repository.getSettingsOnce()
            if (current == null || current.pinHash.isBlank()) {
                onResult(false, "No hay ningún PIN configurado. Por favor, realiza el registro inicial.")
                return@launch
            }

            val salt = getOrCreateSalt()
            val computedHash = hashPinWithSalt(pin, salt)
            val legacyHash = hashPinLegacy(pin)

            var isMatch = (current.pinHash == computedHash)
            var needsMigration = false

            if (!isMatch && current.pinHash == legacyHash) {
                isMatch = true
                needsMigration = true
            }

            val prefs = getApplication<Application>().getSharedPreferences("atalaya_security_prefs", android.content.Context.MODE_PRIVATE)

            if (isMatch) {
                // Success: reset attempts and unlock
                prefs.edit().putInt("failed_attempts", 0).putLong("lockout_until", 0L).apply()
                _uiState.update { it.copy(failedAttempts = 0, lockoutUntilMillis = 0L, pinInput = "") }
                
                val updatedSettings = current.copy(
                    isLoggedIn = true,
                    pinHash = if (needsMigration) computedHash else current.pinHash
                )
                repository.saveSettings(updatedSettings)
                closeAuthDialog()
                onResult(true, "Acceso concedido.")
                return@launch
            }

            // Failed attempt handling
            val newAttempts = state.failedAttempts + 1
            val lockoutDuration = (30_000L * (1L shl (newAttempts - 5).coerceAtLeast(0))).coerceAtMost(3600_000L) // exponential backoff after 5 attempts
            
            if (newAttempts >= 5) {
                val lockUntil = now + lockoutDuration
                prefs.edit().putInt("failed_attempts", newAttempts).putLong("lockout_until", lockUntil).apply()
                _uiState.update { it.copy(failedAttempts = newAttempts, lockoutUntilMillis = lockUntil, pinInput = "") }
                val sec = lockoutDuration / 1000
                onResult(false, "Demasiados intentos fallidos. Bloqueado temporalmente por $sec segundos.")
            } else {
                val remaining = 5 - newAttempts
                prefs.edit().putInt("failed_attempts", newAttempts).apply()
                _uiState.update { it.copy(failedAttempts = newAttempts, pinInput = "") }
                onResult(false, "PIN incorrecto. Te quedan $remaining intento(s) antes del bloqueo temporal.")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val current = repository.getSettingsOnce() ?: SoltarSettingsEntity()
            repository.saveSettings(
                current.copy(
                    isLoggedIn = false
                )
            )
            _uiState.update { it.copy(pinInput = "", confirmPinInput = "") }
        }
    }



    fun deleteAccount() {
        viewModelScope.launch {
            val current = repository.getSettingsOnce() ?: SoltarSettingsEntity()
            repository.saveSettings(
                current.copy(
                    isLoggedIn = true,
                    userName = "Viajero",
                    contact1Name = "",
                    contact1Phone = "",
                    contact1Relationship = "",
                    contact2Name = "",
                    contact2Phone = "",
                    contact2Relationship = "",
                    contact3Name = "",
                    contact3Phone = "",
                    contact3Relationship = "",
                    subscriptionTier = "FREE",
                    isTrialActive = false,
                    biometricLockEnabled = false,
                    pinHash = ""
                )
            )
            repository.clearAiMemory()
            _uiState.update {
                it.copy(
                    hasConfiguredPin = false,
                    failedAttempts = 0,
                    lockoutUntilMillis = 0L,
                    pinInput = "",
                    confirmPinInput = ""
                )
            }
        }
    }
}
