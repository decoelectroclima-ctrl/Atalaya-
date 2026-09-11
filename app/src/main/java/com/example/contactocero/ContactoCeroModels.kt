package com.example.contactocero

import android.content.Context

enum class MarcoAnclaje(val label: String) {
    CATOLICO("Fe y Espiritualidad"),
    ESTOICO("Filosofía Estoica"),
    PSICOLOGIA("Psicología Moderna")
}

enum class ModuloAnclaje(val tituloModulo: String) {
    EMERGENCIA("Botón de Emergencia"),
    IDENTIDAD_AUTOESTIMA("Identidad y Autoestima"),
    DUELO("Procesamiento del Duelo"),
    RECONCILIACION_INTERIOR("Reconciliación Interior"),
    METAS_RECOMPENSAS("Metas y Recompensas"),
    ENTORNO_DIGITAL("Entorno Digital y Pensamientos"),
    SOLEDAD("Gestión de la Soledad"),
    CULPA("Liberación de Culpa"),
    VINCULOS_TOXICOS("Librarse de Vínculos Tóxicos"),
    REEDIFICACION_FUTURO("Reedificación y Futuro")
}

data class AnclajeContactoCero(
    val id: String,                  // p.ej. "CAT-001", "EST-001", "PSI-001"
    val marco: MarcoAnclaje,
    val modulo: ModuloAnclaje,
    val universal: Boolean = false,  // true = usable como fallback en cualquier módulo
    val titulo: String,
    val referencia: String? = null,  // cita bíblica / máxima / enfoque clínico
    val textoPlantilla: String       // contiene {{ex_name}}; NUNCA persistir ya sustituido
)

object ContactoCeroConfig {
    const val PREFS_NAME = "atalaya_security_prefs"
    const val KEY_FEATURE_ENABLED = "contacto_cero_enabled"

    fun isEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_FEATURE_ENABLED, true)
    }

    fun setEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_FEATURE_ENABLED, enabled).apply()
    }
}
