package com.example.contactocero

import java.util.Random

object ContactoCeroEngine {

    /**
     * Cadena de fallback: exacto (mismo marco+módulo) → canónico universal del mismo marco
     * → canónico universal de cualquier marco → primer anclaje de seguridad.
     */
    fun seleccionar(
        marco: MarcoAnclaje,
        modulo: ModuloAnclaje,
        random: Random = Random()
    ): AnclajeContactoCero {
        // 1. Exacto: entradas del marco + modulo solicitado
        val exactos = ContactoCeroContent.TODOS_LOS_ANCLAJES.filter { it.marco == marco && it.modulo == modulo }
        if (exactos.isNotEmpty()) {
            return exactos[random.nextInt(exactos.size)]
        }

        // 2. Fallback 1: canónico universal del mismo marco
        val universalMismoMarco = ContactoCeroContent.TODOS_LOS_ANCLAJES.filter { it.marco == marco && it.universal }
        if (universalMismoMarco.isNotEmpty()) {
            return universalMismoMarco[random.nextInt(universalMismoMarco.size)]
        }

        // 3. Fallback 2: canónico universal de cualquier marco
        val universalCualquiera = ContactoCeroContent.TODOS_LOS_ANCLAJES.filter { it.universal }
        if (universalCualquiera.isNotEmpty()) {
            return universalCualquiera[random.nextInt(universalCualquiera.size)]
        }

        // 4. Seguridad de respaldo
        return ContactoCeroContent.TODOS_LOS_ANCLAJES.first()
    }

    /**
     * Inyección del token: si exName en blanco, sustituir "{{ex_name}}" por "esa persona"
     * para que el texto nunca quede con placeholders visibles.
     */
    fun renderizar(anclaje: AnclajeContactoCero, exName: String): String {
        val cleanName = exName.trim()
        val replacement = if (cleanName.isNotBlank()) cleanName else "esa persona"
        return anclaje.textoPlantilla.replace("{{ex_name}}", replacement)
    }
}
