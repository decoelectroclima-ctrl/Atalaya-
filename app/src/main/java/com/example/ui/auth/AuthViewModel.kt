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
    val authNameInput: String = ""
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SoltarRepository = SoltarRepository(AdrianaDatabase.getDatabase(application))

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

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
        if (pin.length <= 4) {
            _uiState.update { it.copy(pinInput = pin) }
        }
    }

    fun setConfirmPin(pin: String) {
        if (pin.length <= 4) {
            _uiState.update { it.copy(confirmPinInput = pin) }
        }
    }

    fun setAuthName(name: String) = _uiState.update { it.copy(authNameInput = name) }

    fun setAuthDialogMode(mode: String) = _uiState.update { it.copy(authDialogMode = mode) }

    private fun hashPin(pin: String): String {
        val spec = PBEKeySpec(pin.toCharArray(), "salt".toByteArray(), 10000, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hash = factory.generateSecret(spec).encoded
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun registerPin(onResult: (Boolean, String) -> Unit) {
        val pin = _uiState.value.pinInput
        val confirm = _uiState.value.confirmPinInput
        val name = _uiState.value.authNameInput

        if (pin.length != 4 || pin != confirm) {
            onResult(false, "El PIN debe ser de 4 dígitos y coincidir.")
            return
        }

        val pinHash = hashPin(pin)

        viewModelScope.launch {
            val current = repository.settings.first() ?: SoltarSettingsEntity()
            repository.saveSettings(current.copy(pinHash = pinHash, isLoggedIn = true, userName = name.ifBlank { "Viajero" }))
            closeAuthDialog()
            onResult(true, "PIN configurado correctamente.")
        }
    }

    fun loginWithPin(onResult: (Boolean, String) -> Unit) {
        val pin = _uiState.value.pinInput
        viewModelScope.launch {
            val current = repository.settings.first()

            if (current == null || current.pinHash != hashPin(pin)) {
                onResult(false, "PIN incorrecto.")
                return@launch
            }

            repository.saveSettings(current.copy(isLoggedIn = true))
            closeAuthDialog()
            onResult(true, "Acceso concedido.")
        }
    }

    fun logout() {
        viewModelScope.launch {
            val current = repository.settings.first() ?: SoltarSettingsEntity()
            repository.saveSettings(
                current.copy(
                    isLoggedIn = false
                )
            )
            // playSound is not available in AuthViewModel, need to handle that.
            // For now, skip sound or call it differently.
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            val current = repository.settings.first() ?: SoltarSettingsEntity()
            repository.saveSettings(
                current.copy(
                    isLoggedIn = false,
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
                    pinHash = ""
                )
            )
            repository.clearAiMemory()
        }
    }
}
