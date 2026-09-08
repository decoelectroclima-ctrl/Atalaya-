package com.example.ui.managers

import com.example.ai.OnDeviceLlmEngine
import com.example.data.CheckinEntity
import com.example.data.RelapseEntity
import com.example.data.SoltarSettingsEntity

object JourneyStageEvaluator {
    data class EvaluationResult(
        val shouldUpgradeToLifeCoach: Boolean,
        val shouldPromptRecoveryRegression: Boolean,
        val transitionMessage: String?,
        val wasBlockedByQualitativeCheck: Boolean = false
    )

    enum class QualitativeSignal { ESTABILIDAD_GENUINA, NEGACION_O_EVITACION, RESIGNACION_FORZADA, NO_EVALUADO }

    private fun assessQualitativeState(recentFreeText: List<String>): QualitativeSignal {
        val joined = recentFreeText.filter { it.isNotBlank() }.takeLast(5).joinToString("\n---\n")
        if (joined.isBlank() || !OnDeviceLlmEngine.isReady()) return QualitativeSignal.NO_EVALUADO
        val prompt = """
            Analiza los siguientes fragmentos recientes de journaling/cartas de una persona en proceso de duelo de ruptura.
            Clasifica el estado predominante en UNA sola palabra exacta de esta lista:
            ESTABILIDAD_GENUINA, NEGACION_O_EVITACION, RESIGNACION_FORZADA
            - ESTABILIDAD_GENUINA: acepta la realidad, expresa autonomia real, no idealiza ni minimiza el dolor pasado.
            - NEGACION_O_EVITACION: evita hablar del tema, minimiza artificialmente, "ya no pienso en eso" sin elaboracion.
            - RESIGNACION_FORZADA: lenguaje de rendicion o resignacion forzada ("ya no tiene caso", "da igual"), no de paz real.
            Responde UNICAMENTE con la palabra de la categoria, sin explicacion.
            Texto:
            $joined
        """.trimIndent()
        return try {
            val raw = OnDeviceLlmEngine.generate(prompt).trim().uppercase()
            when {
                raw.contains("ESTABILIDAD_GENUINA") -> QualitativeSignal.ESTABILIDAD_GENUINA
                raw.contains("NEGACION") -> QualitativeSignal.NEGACION_O_EVITACION
                raw.contains("RESIGNACION") -> QualitativeSignal.RESIGNACION_FORZADA
                else -> QualitativeSignal.NO_EVALUADO
            }
        } catch (e: Exception) { QualitativeSignal.NO_EVALUADO }
    }

    suspend fun evaluate(
        settings: SoltarSettingsEntity?,
        checkins: List<CheckinEntity>,
        relapses: List<RelapseEntity>,
        hasCompletedClosingRitual: Boolean,
        recentFreeText: List<String> = emptyList()
    ): EvaluationResult {
        if (settings == null) return EvaluationResult(false, false, null)
        val currentStage = settings.journeyStage
        val now = System.currentTimeMillis()

        if (currentStage == "RECOVERY") {
            val recentCheckins = checkins.take(14)
            val hasEnoughHistory = recentCheckins.size >= 5
            val lowPainSustained = recentCheckins.isNotEmpty() &&
                (recentCheckins.map { it.pain + it.anxiety + it.rumination }.average() < 12.0)
            val goodAutonomy = recentCheckins.isNotEmpty() &&
                (recentCheckins.map { it.autonomy }.average() >= 5.0)
            val closingRitualDone = hasCompletedClosingRitual
            val recentRetrogradeRelapse = relapses.any { r ->
                (now - r.timestamp) < (28L * 24 * 3600 * 1000) && (r.interpretation == "retroceso" || r.isRestartingFromZero)
            }
            val minimumTimeElapsed = (now - settings.breakupDateTimestamp) > (60L * 24 * 3600 * 1000)
            val realDataSupportsHealing = hasEnoughHistory && lowPainSustained && goodAutonomy
            val quantitativeGreenLight = closingRitualDone && !recentRetrogradeRelapse && minimumTimeElapsed && realDataSupportsHealing

            if (quantitativeGreenLight) {
                val qualitative = assessQualitativeState(recentFreeText)
                if (qualitative == QualitativeSignal.NEGACION_O_EVITACION || qualitative == QualitativeSignal.RESIGNACION_FORZADA) {
                    return EvaluationResult(
                        shouldUpgradeToLifeCoach = false,
                        shouldPromptRecoveryRegression = false,
                        transitionMessage = "Tus numeros muestran avance, pero tu propio proceso escrito sugiere que aun hay algo pendiente de mirar de frente. Sigamos un poco mas en Recovery antes de dar el salto.",
                        wasBlockedByQualitativeCheck = true
                    )
                }
                return EvaluationResult(
                    shouldUpgradeToLifeCoach = true,
                    shouldPromptRecoveryRegression = false,
                    transitionMessage = "Has recorrido un largo camino. Ahora podemos trabajar en quien quieres ser."
                )
            }
        } else if (currentStage == "LIFE_COACH") {
            val severeRecentRelapse = relapses.any { r ->
                (now - r.timestamp) < (7L * 24 * 3600 * 1000) && (r.interpretation == "retroceso" || r.isRestartingFromZero)
            }
            if (severeRecentRelapse) {
                return EvaluationResult(false, true, null)
            }
        }
        return EvaluationResult(false, false, null)
    }
}
