package com.example.ai

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object AtalayaAiEngine {

    private const val TAG = "AtalayaAiEngine"
    private const val GEMINI_MODEL = "gemini-3.5-flash"
    private const val API_URL = "https://generativelanguage.googleapis.com/v1beta/models/$GEMINI_MODEL:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val SYSTEM_PROMPT_MAESTRO = """
# SYSTEM PROMPT MAESTRO: ATALAYA CORE ENGINE (PROD_VERSION_1.0_FULL)

## 1. MISION Y NATURALEZA DEL SISTEMA
Eres "ATALAYA Core", el orquestador inteligente, mentor clínico-filosófico y regulador somático autónomo de la aplicación ATALAYA. Tu función no es chatear como un bot comercial ni dar consejos vacíos de autoayuda. Tu objetivo es interceptar bucles de rumiación, regular el sistema nervioso autónomo del usuario, guiar el duelo afectivo, proteger el protocolo de contacto cero y facilitar la reconstrucción de la identidad mediante una triangulación conceptual inquebrantable.

## 2. ARQUITECTURA CONCEPTUAL: LA TRIANGULACIÓN TRANS DISCIPLINARIA
En cada interacción, análisis de diario o protocolo de emergencia, DEBES ejecutar un cruce de variables seleccionando quirúrgicamente entre estas cuatro fuentes epistemológicas:
1. **Estoicismo Clásico (Marco Aurelio, Séneca, Epicteto):** Dicotomía del control, ciudadela interior, premeditatio malorum, amor fati, juicio objetivo sin carga emocional. Para devolver la soberanía mental y cortar la victimización.
2. **Neurociencia Aplicada y Neuroplasticidad (Dispenza / Huberman):** Adicción química al bucle emocional (dopamina/cortisol/oxitocina), desintegración de huellas sinápticas, regulación del sistema nervioso. Para explicar la fisiología del dolor y desmitificar el "amor obsesivo".
3. **Psicoanálisis y Clínica del Duelo (Gabriel Rolón / Freud / Lacan):** Travesía de la herida, deconstrucción de la idealización (el fantasma del ex), el dolor como costo del amor. Para abordar la melancolía y aceptar la pérdida.
4. **Poesía Sapiencial y Lamento Estructural (Los Salmos):** El grito existencial desde la intemperie, la dignidad en el desamparo, fortaleza espiritual ante la traición. Para ofrecer una catarsis poética profunda.

## 3. MOTOR DE ADAPTACIÓN BIOLÓGICA Y CONTEXTUAL
Evalúa siempre las variables enviadas:
- NEURODIVERGENCIA (ADHD/ASD): Formato de baja carga cognitiva, frases cortas, viñetas claras, micro-pasos.
- BIOMETRICS_HRV (Estrés elevado): Prioriza la regulación somática INMEDIATA (suspiro fisiológico, respiración 4-7-8).
- ENDOCRINE_CYCLE (Fase lútea/premenstrual): REDUCE la severidad estoica 50%, AUMENTA la autocompasión biológica y validación neuroquímica.

## 4. PROTOCOLOS LÓGICOS DE ACTUACIÓN
- PROTOCOLO A (BOTÓN SOS / CONTACTO CERO): 1. Freno Somático (Suspiro 90s), 2. Explicación Neuroquímica (Absorción de dopamina), 3. Fricción Cognitiva Estoica ("¿Este mensaje te devuelve la dignidad o se la entrega a la otra persona?"), 4. Redirección al Cripto-Buzón.
- PROTOCOLO B (AUDITORÍA DE REALIDAD): Confrontación con los hechos dolorosos reales del pasado vs la fantasía nostálgica. Análisis de utilidad de Epicteto.
- PROTOCOLO C (ESCUDO DE FECHAS CRÍTICAS Y LOGÍSTICA): Plantillas ultra-neutrales frías sin emoción ni reproche.

## 5. FORMATO DE SALIDA (DEBES RESPONDER EN JSON VALIDO CON ESTA ESTRUCTURA EXACTA)
```json
{
  "user_state_detected": "Somatic_Panic | Rumination | Idealization | Neutral",
  "recommended_action": "Somatic_Breathing | Stoic_Reframing | Reality_Audit",
  "intervention": {
    "somatic_exercise": "Physiological_Sigh_90s",
    "triangulated_response": "Texto claro, riguroso y consolador estructurado en Markdown...",
    "kintsugi_milestone_unlocked": false
  },
  "ui_trigger": {
    "background_mode": "Obsidian_Slate_#121214",
    "accent_color": "Kintsugi_Gold_#D4AF37",
    "show_sos_overlay": false
  }
}
```

## 6. REGLAS INVIOLABLES
1. NUNCA uses frases hechas ("Todo pasa por algo", "El tiempo lo cura todo").
2. NUNCA valides la rumiación tóxica ni analices al ex. Redirige a la conducta y soberanía del usuario.
3. MANTÉN la dignidad del usuario como un adulto soberano.
4. RESPETA la estética Kintsugi: las cicatrices reestructuradas son el verdadero oro.
    """.trimIndent()

    private fun sanitizeInput(input: String): String {
        return input.replace(Regex("<[^>]*>"), "")
            .replace(Regex("(?i)system prompt"), "[redacted]")
            .trim()
            .take(1500)
    }

    private fun checkSelfHarmTrigger(input: String): Boolean {
        val lower = input.lowercase()
        val keywords = listOf(
            "suicidio", "suicidarme", "quitarme la vida", "morir", "matarme",
            "autolesion", "no quiero vivir", "acabar con todo", "no vale la pena vivir"
        )
        return keywords.any { lower.contains(it) }
    }

    suspend fun processIntervention(
        userInput: String,
        contextVariables: UserContextVariables,
        isSosMode: Boolean = false,
        recordedFacts: List<String> = emptyList()
    ): AtalayaInterventionResponse = withContext(Dispatchers.IO) {
        val sanitizedInput = sanitizeInput(userInput)

        // Medical Crisis & Self-Harm Shield Check
        if (checkSelfHarmTrigger(sanitizedInput)) {
            Log.w(TAG, "Medical crisis / self-harm safety filter triggered!")
            return@withContext AtalayaInterventionResponse(
                userStateDetected = "Medical_Crisis",
                recommendedAction = "Emergency_Helpline",
                somaticExercise = "Physiological_Sigh_90s",
                triangulatedResponse = """
## ⚠️ INTERVENCIÓN DE SEGURIDAD Y SALUD MENTAL

ATALAYA detecta un estado de crisis profunda. Tu vida y tu integridad son sagradas e irremplazables. El tono filosófico queda suspendido.

Por favor, ponte en contacto inmediato con profesionales capacitados que pueden acompañarte en este instante:

- **España:** 024 (Línea de Atención a la Conducta Suicida) o 112
- **Estados Unidos & América Latina:** 988 (Suicide & Crisis Lifeline)
- **Emergencias Generales:** 911

*ATALAYA es una herramienta de desarrollo personal y filosofía aplicada, no un tratamiento psiquiátrico ni un servicio de emergencias médicas.*
                """.trimIndent(),
                kintsugiMilestoneUnlocked = false,
                showSosOverlay = true,
                systemStatus = SystemStatus(status = "EMERGENCY_OVERRIDE"),
                safetyFilter = SafetyFilterResult(
                    flaggedForHarm = true,
                    emergencyHelplineTriggered = true
                )
            )
        }

        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API key missing or placeholder. Running fallback ATALAYA offline engine.")
            return@withContext executeFallbackEngine(sanitizedInput, contextVariables, isSosMode, recordedFacts)
        }

        try {
            val fullPrompt = buildString {
                appendLine("--- VARIABLES DE ENTRADA DEL USUARIO ---")
                appendLine(contextVariables.toPromptString())
                if (recordedFacts.isNotEmpty()) {
                    appendLine("- [Variable: RECORDED_FACTS]: ${recordedFacts.joinToString(" | ")}")
                }
                if (isSosMode) {
                    appendLine("- [Variable: MODE]: MODOS_SOS_CONTACTO_CERO_ACTIVADO")
                }
                appendLine("--- MENSAJE DEL USUARIO ---")
                appendLine(sanitizedInput)
                appendLine("\nIMPORTANTE: Devuelve ÚNICAMENTE un objeto JSON válido con la estructura acordada.")
            }

            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", fullPrompt))
                        })
                    })
                })
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", SYSTEM_PROMPT_MAESTRO))
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.4)
                    put("responseMimeType", "application/json")
                })
            }

            val url = "$API_URL?key=$apiKey"
            val body = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(url).post(body).build()

            val response = client.newCall(request).execute()
            val responseString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(TAG, "Gemini API HTTP Error: ${response.code} $responseString")
                return@withContext executeFallbackEngine(userInput, contextVariables, isSosMode, recordedFacts)
            }

            val root = JSONObject(responseString)
            val candidates = root.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text") ?: ""

            parseJsonResponse(text, userInput, isSosMode)
        } catch (e: Exception) {
            Log.e(TAG, "Error querying Gemini API", e)
            executeFallbackEngine(userInput, contextVariables, isSosMode, recordedFacts)
        }
    }

    private fun parseJsonResponse(
        jsonString: String,
        userInput: String,
        isSosMode: Boolean
    ): AtalayaInterventionResponse {
        return try {
            val cleanJson = jsonString.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            val json = JSONObject(cleanJson)

            val state = json.optString("user_state_detected", if (isSosMode) "Somatic_Panic" else "Rumination")
            val action = json.optString("recommended_action", "Stoic_Reframing")
            val interventionObj = json.optJSONObject("intervention")
            val somaticEx = interventionObj?.optString("somatic_exercise") ?: "Physiological_Sigh_90s"
            val textResp = interventionObj?.optString("triangulated_response")
                ?: "Corta el impulso. Tu sistema nervioso busca la dosis dopaminérgica del pasado."
            val milestone = interventionObj?.optBoolean("kintsugi_milestone_unlocked", false) ?: false

            val uiTrigger = json.optJSONObject("ui_trigger")
            val showOverlay = uiTrigger?.optBoolean("show_sos_overlay", isSosMode) ?: isSosMode

            AtalayaInterventionResponse(
                userStateDetected = state,
                recommendedAction = action,
                somaticExercise = somaticEx,
                triangulatedResponse = textResp,
                kintsugiMilestoneUnlocked = milestone,
                showSosOverlay = showOverlay
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing JSON response from Gemini", e)
            AtalayaInterventionResponse(
                userStateDetected = if (isSosMode) "Somatic_Panic" else "Rumination",
                recommendedAction = "Stoic_Reframing",
                somaticExercise = "Physiological_Sigh_90s",
                triangulatedResponse = jsonString.ifBlank { "Respira hondo. Recupera tu soberanía." },
                kintsugiMilestoneUnlocked = false,
                showSosOverlay = isSosMode
            )
        }
    }

    private fun executeFallbackEngine(
        userInput: String,
        context: UserContextVariables,
        isSosMode: Boolean,
        recordedFacts: List<String>
    ): AtalayaInterventionResponse {
        val lower = userInput.lowercase()

        val isPanic = isSosMode || lower.contains("escribir") || lower.contains("llamar") || lower.contains("sos") || lower.contains("ex") || context.hrvLevel < 40
        val isIdealizing = lower.contains("extraño") || lower.contains("nadie me va a amar") || lower.contains("nostalgia") || lower.contains("fotos")

        val state = when {
            isPanic -> "Somatic_Panic"
            isIdealizing -> "Idealization"
            else -> "Rumination"
        }

        val action = when {
            isPanic -> "Somatic_Breathing"
            isIdealizing -> "Reality_Audit"
            else -> "Stoic_Reframing"
        }

        val responseText = buildString {
            if (isPanic) {
                append("## PROTOCOLO A: FRENO SOMÁTICO Y SOBERANÍA MENTAL\n\n")
                append("**1. Regulación Fisiológica (90s):**\n")
                append("Ejecuta 3 Suspiros Fisiológicos continuos (dos inhalaciones nasales rápidas seguidas de una exhalación bucal prolongada).\n\n")
                append("**2. Decodificación Neuroquímica:**\n")
                append("El deseo ferviente de enviar ese mensaje **no es amor ni una señal del destino**. Es un pico de abstinencia dopaminérgica en tu circuito de recompensa.\n\n")
                append("**3. Pregunta Estoica de Dignidad:**\n")
                append("*«¿Este impulso te devuelve la soberanía sobre tu vida o se la entrega intacta a quien ya eligió soltarte?»*\n\n")
                append("**4. Acción Inmediata:**\n")
                append("Escribe las palabras exactas que querías enviar dentro del **Cripto-Buzón ATALAYA**. Se someterán a autodestrucción programada.")
            } else if (isIdealizing) {
                append("## PROTOCOLO B: AUDITORÍA DE REALIDAD (ROLÓN & MARCO AURELIO)\n\n")
                append("**1. Dismantelando la Amnesia Selectiva:**\n")
                append("La memoria bajo duelo tiende a borrar las cicatrices y edificar una estatua de humo. No extrañas al individuo real; extrañas la fantasía idealizada que proyectaste.\n\n")
                if (recordedFacts.isNotEmpty()) {
                    append("**2. Hechos Registrados en tu Historial:**\n")
                    recordedFacts.take(3).forEach { fact ->
                        append("- *\"$fact\"*\n")
                    }
                    append("\n")
                }
                append("**3. Análisis de Utilidad (Epicteto):**\n")
                append("*«Mira objetivamente: ¿Buscar esa información o revivir el recuerdo fortalece tu ciudadela interior o te re-engancha al dolor?»*")
            } else {
                append("## TRIANGULACIÓN CONCEPTUAL ATALAYA\n\n")
                append("**1. Perspectiva Estoica (Séneca):**\n")
                append("«Sufrimos más a menudo en la imaginación que en la realidad.» Separa el hecho objetivo de tu juicio emocional.\n\n")
                append("**2. Dimensión Neurobiológica:**\n")
                append("Tu cerebro está reconfigurando huellas sinápticas. Cada vez que evitas el bucle de rumiación, debilitas el camino neural de la adicción afectiva.\n\n")
                if (context.isLateLutealPhase) {
                    append("\n*Nota Biológica:* Tu fase premenstrual actual eleva la sensibilidad a los neurotransmisores. Trátate con rigor estoico en las acciones, pero con profunda autocompasión biológica.")
                }
            }
        }

        return AtalayaInterventionResponse(
            userStateDetected = state,
            recommendedAction = action,
            somaticExercise = if (isPanic) "Physiological_Sigh_90s" else null,
            triangulatedResponse = responseText,
            kintsugiMilestoneUnlocked = isPanic,
            showSosOverlay = isSosMode,
            systemStatus = SystemStatus(status = "DEGRADED_OFFLINE"),
            fallbackResponse = FallbackResponse(useLocalCache = true, statusMessage = "Modo Resiliencia Local Activo (Fallback Local Edge)")
        )
    }
}
