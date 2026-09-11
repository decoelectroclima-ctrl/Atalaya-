package com.example.contactocero

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AdrianaDatabase
import com.example.data.SoltarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AnclajeUiState(
    val anclaje: AnclajeContactoCero? = null,
    val textoRenderizado: String = "",
    val moduloActual: ModuloAnclaje = ModuloAnclaje.EMERGENCIA,
    val marcoActual: MarcoAnclaje = MarcoAnclaje.PSICOLOGIA,
    val exName: String = "",
    val isFeatureEnabled: Boolean = true
)

class ContactoCeroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SoltarRepository = SoltarRepository(AdrianaDatabase.getDatabase(application))

    private val _uiState = MutableStateFlow(AnclajeUiState())
    val uiState: StateFlow<AnclajeUiState> = _uiState.asStateFlow()

    init {
        val enabled = ContactoCeroConfig.isEnabled(application)
        _uiState.update { it.copy(isFeatureEnabled = enabled) }

        viewModelScope.launch {
            repository.settings.collect { settings ->
                val marco = mapFrameworkToMarco(settings?.preferredFramework)
                _uiState.update { current ->
                    val shouldReload = current.anclaje == null
                    val updated = current.copy(marcoActual = marco)
                    if (shouldReload) {
                        val anclaje = ContactoCeroEngine.seleccionar(marco, current.moduloActual)
                        updated.copy(
                            anclaje = anclaje,
                            textoRenderizado = ContactoCeroEngine.renderizar(anclaje, current.exName)
                        )
                    } else {
                        updated
                    }
                }
            }
        }
    }

    fun configurar(modulo: ModuloAnclaje, marco: MarcoAnclaje? = null, exName: String = "") {
        _uiState.update { current ->
            val targetMarco = marco ?: current.marcoActual
            val anclaje = ContactoCeroEngine.seleccionar(targetMarco, modulo)
            current.copy(
                moduloActual = modulo,
                marcoActual = targetMarco,
                exName = exName,
                anclaje = anclaje,
                textoRenderizado = ContactoCeroEngine.renderizar(anclaje, exName)
            )
        }
    }

    fun seleccionarOtro() {
        val current = _uiState.value
        val anclaje = ContactoCeroEngine.seleccionar(current.marcoActual, current.moduloActual)
        _uiState.update {
            it.copy(
                anclaje = anclaje,
                textoRenderizado = ContactoCeroEngine.renderizar(anclaje, current.exName)
            )
        }
    }

    fun cambiarMarco(nuevoMarco: MarcoAnclaje) {
        val current = _uiState.value
        val anclaje = ContactoCeroEngine.seleccionar(nuevoMarco, current.moduloActual)
        _uiState.update {
            it.copy(
                marcoActual = nuevoMarco,
                anclaje = anclaje,
                textoRenderizado = ContactoCeroEngine.renderizar(anclaje, current.exName)
            )
        }
    }

    fun cambiarModulo(nuevoModulo: ModuloAnclaje) {
        val current = _uiState.value
        val anclaje = ContactoCeroEngine.seleccionar(current.marcoActual, nuevoModulo)
        _uiState.update {
            it.copy(
                moduloActual = nuevoModulo,
                anclaje = anclaje,
                textoRenderizado = ContactoCeroEngine.renderizar(anclaje, current.exName)
            )
        }
    }

    companion object {
        fun mapFrameworkToMarco(framework: String?): MarcoAnclaje {
            return when (framework?.uppercase()) {
                "CATOLICO" -> MarcoAnclaje.CATOLICO
                "ESTOICO" -> MarcoAnclaje.ESTOICO
                else -> MarcoAnclaje.PSICOLOGIA
            }
        }
    }
}
