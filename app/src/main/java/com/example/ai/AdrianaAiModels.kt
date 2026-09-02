package com.example.ai

import org.json.JSONObject

enum class SubscriptionTier(val label: String, val badge: String) {
    GRATUITO("Gratuito (Base)", "1 Nivel Kintsu / 3 Píldoras día"),
    REC_PASO("Recuerda Paso (Mensual)", "Acceso Ilimitado + Biblioteca Base"),
    REC_CORE("Recuerda Core (Anual / Pro)", "Acceso Total + Audios + Personalización Gold")
}

data class ReadingPill(
    val id: String,
    val author: String,
    val category: String, // Estoicismo | Neurociencia | Psicoanálisis | Salmos
    val textBody: String
)

data class ReadingChapter(
    val id: String,
    val title: String,
    val author: String,
    val estimatedReadTimeMin: Int,
    val category: String,
    val textBody: String,
    val isCoreOnly: Boolean = false
)

data class KintsuGameConfig(
    val levelName: String,
    val difficultyLevel: String = "Somatic_Calm_Easy",
    val forcedBreathingTempoBpm: Int = 6,
    val goldSeamsToFill: Int = 3,
    val rewardHaptic: String = "heavy_confirmation"
)

data class SystemStatus(
    val status: String = "HEALTHY", // HEALTHY | DEGRADED_OFFLINE | EMERGENCY_OVERRIDE
    val rateLimitRemaining: Int = 10,
    val encryptionActive: Boolean = true
)

data class SafetyFilterResult(
    val flaggedForHarm: Boolean = false,
    val emergencyHelplineTriggered: Boolean = false,
    val emergencyHelplineNumber: String = "024 (España) / 988 (EE.UU./LatAm) / 112 (Emergencias)"
)

data class FallbackResponse(
    val useLocalCache: Boolean = false,
    val offlineAudioAsset: String = "binaural_432hz_hum",
    val statusMessage: String = "Sovereignty maintained. System active."
)

data class AdrianaInterventionResponse(
    val userStateDetected: String, // Somatic_Panic | Rumination | Idealization | Neutral | Medical_Crisis
    val recommendedAction: String, // Somatic_Breathing | Stoic_Reframing | Reality_Audit | Emergency_Helpline
    val somaticExercise: String?,
    val triangulatedResponse: String,
    val kintsugiMilestoneUnlocked: Boolean,
    val showSosOverlay: Boolean,
    val moduleTrigger: String? = null,
    val activeReadingPill: ReadingPill? = null,
    val kintsuConfig: KintsuGameConfig? = null,
    val systemStatus: SystemStatus = SystemStatus(),
    val safetyFilter: SafetyFilterResult = SafetyFilterResult(),
    val fallbackResponse: FallbackResponse = FallbackResponse()
)

typealias AtalayaInterventionResponse = AdrianaInterventionResponse

data class UserContextVariables(
    val demographics: String = "Adulto (18+), En proceso de duelo y reconstrucción",
    val hasAdhd: Boolean = true,
    val hasAsd: Boolean = false,
    val hrvLevel: Int = 35, // ms (<40 is high stress)
    val restingHeartRate: Int = 88, // bpm (>80 is high stress)
    val isLateLutealPhase: Boolean = false
) {
    fun toPromptString(): String {
        return """
            - [Variable: USER_DEMOGRAPHICS]: $demographics
            - [Variable: NEURODIVERGENCIA]: ADHD = $hasAdhd, ASD = $hasAsd
            - [Variable: BIOMETRICS_HRV]: HRV = $hrvLevel ms, Resting HR = $restingHeartRate bpm (Estrés somático: ${if (hrvLevel < 40 || restingHeartRate > 80) "ELEVADO - PRIORIZAR REGULACIÓN SOMÁTICA" else "Normal"})
            - [Variable: ENDOCRINE_CYCLE]: Fase Lútea Tardía / Premenstrual = $isLateLutealPhase (Ajuste autocompasión: ${if (isLateLutealPhase) "SI (+50% Autocompasión biológica)" else "NO"})
        """.trimIndent()
    }
}
