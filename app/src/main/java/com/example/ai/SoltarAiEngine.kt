package com.example.ai

import android.util.Log
import com.example.BuildConfig
import com.example.data.ClinicalKnowledgeBase
import com.example.data.JournalEntryEntity
import com.example.data.KnowledgeCapsule
import com.example.data.SoltarFramework
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class SoltarUserContext(
    val userName: String = "Viajero",
    val streakDays: Int = 0,
    val totalCheckins: Int = 0,
    val lastCheckinMood: String = "",
    val averageAutonomyScore: Float = 5f,
    val recentRelapseTriggers: List<String> = emptyList(),
    val recentPatternsAudited: List<String> = emptyList(),
    val activeIdentityGoals: List<String> = emptyList(),
    val framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
    val relDuration: String = "",
    val hasChildren: Boolean = false,
    val contactType: String = "",
    val breakupSituation: String = "",
    val practicals: String = "",
    val timeSinceBreakup: String = "",
    val previousBreakupsCount: Int = 0,
    val cohabitation: Boolean = false,
    val marriedOrEngaged: Boolean = false,
    val anticipatedGrief: String = "",
    val parentalOnlyCommunication: Boolean = true,
    val emotionalSituation: String = "",
    val decisionMaker: String = "",
    val breakupReason: String = "",
    val freeHistoryNotes: String = "",
    val upcomingRiskDatesSummary: String = "",
    val journeyStage: String = "RECOVERY",
    val lifeCoachFocus: String = ""
) {
    fun toClinicalSummary(): String {
        val parts = mutableListOf<String>()
        parts.add("• NOMBRE DEL USUARIO: $userName")
        if (journeyStage == "LIFE_COACH") {
            parts.add("• FASE ACTUAL: AVE FÉNIX / COACH LIFE (Enfoque 100% en autoaceptación, autoestima, fitness, nutrición, estudios y propósitos)")
            if (lifeCoachFocus.isNotBlank()) {
                parts.add("  - Propósito principal declarado: $lifeCoachFocus")
            }
        } else {
            parts.add("• FASE ACTUAL: ADRIANA RECOVERY (Duelo y contacto cero)")
        }
        parts.add("• Días de no-contacto / racha acumulada: $streakDays días")
        if (lastCheckinMood.isNotBlank()) {
            parts.add("• Último registro de estado emocional: $lastCheckinMood (Autonomía percibida: ${"%.1f".format(averageAutonomyScore)}/10)")
        }
        if (recentRelapseTriggers.isNotEmpty()) {
            parts.add("• Detonantes recientes de impulsos/recaídas identificados: ${recentRelapseTriggers.take(3).joinToString(", ")}")
        }
        if (recentPatternsAudited.isNotEmpty()) {
            parts.add("• Dinámicas relacionales auditadas: ${recentPatternsAudited.take(2).joinToString("; ")}")
        }
        if (activeIdentityGoals.isNotEmpty()) {
            parts.add("• Valores y objetivos de identidad trabajados: ${activeIdentityGoals.take(3).joinToString(", ")}")
        }
        if (upcomingRiskDatesSummary.isNotBlank()) {
            parts.add("• FECHAS DE RIESGO ANTICIPADO PRÓXIMAS:\n$upcomingRiskDatesSummary")
        }
        // Contexto contextualizado y profundo
        parts.add("• CONTEXTO HUMANO Y DE VÍNCULO:")
        parts.add("  - Duración de la relación: ${relDuration.replace("_", " ")}")
        parts.add("  - Situación temporal / Tiempo desde ruptura: ${timeSinceBreakup.replace("_", " ")}")
        parts.add("  - Duelo anticipado (duelo previo a la ruptura formal): ${anticipatedGrief.replace("_", " ")}")
        parts.add("  - Origen / Quién decidió: ${decisionMaker.replace("_", " ")}, Contexto/Motivo: ${breakupReason.replace("_", " ")}")
        parts.add("  - Convivencia previa: ${if (cohabitation) "Sí" else "No"} | Casados/Comprometidos: ${if (marriedOrEngaged) "Sí" else "No"}")
        parts.add("  - Hijos en común o vínculos inevitables: ${if (hasChildren) "Sí (Contacto inevitable / Comunicación exclusivamente parental y funcional)" else "No"}")
        parts.add("  - Tipo de contacto actual: ${contactType.replace("_", " ")} (Estrategia: ${if (hasChildren || contactType.contains("POR_")) "Contacto Cero Adaptativo: eliminar contacto emocional innecesario, mantener comunicación funcional imprescindible" else "Contacto Cero Estricto"})")
        parts.add("  - Estado emocional actual: $emotionalSituation")
        parts.add("  - Factores prácticos: ${practicals.ifBlank { "Ninguno" }}")
        if (freeHistoryNotes.isNotBlank()) {
            parts.add("  - Notas libres del usuario: $freeHistoryNotes")
        }
        
        return parts.joinToString("\n")
    }
}

data class SoltarAiResponse(
    val replyText: String,
    val isRuminationDetected: Boolean = false,
    val suggestedAction: String = "",
    val stateDetected: String = "REGULAR" // REGULAR | COMPRENDER | ACEPTAR | DEJAR_DE_PERSEGUIR | RECONSTRUIR | SEGURIDAD
)

data class JournalMentorshipResult(
    val feedback: String,
    val corePrinciple: String,
    val socraticQuestion: String,
    val concreteAction: String
)

data class LinguisticAnalysisResult(
    val nivelAutonomia: Int = 5,
    val lenguajeRumiativo: Int = 5,
    val distorsionesCognitivas: List<String> = emptyList(),
    val cambioDesdeUltimaEntrada: String = "Registra más entradas en tu diario para activar el análisis lingüístico profundo de Recuerda."
)

object SoltarAiEngine {

    private const val TAG = "SoltarAiEngine"
    private const val GEMINI_MODEL = "gemini-2.5-flash"
    private const val API_URL = "https://generativelanguage.googleapis.com/v1beta/models/$GEMINI_MODEL:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(25, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(25, TimeUnit.SECONDS)
        .build()

    private val SYSTEM_PROMPT_SOLTAR = """
# SISTEMA DE IA DE ACOMPAÑAMIENTO Y COACHING CLÍNICO: ATALAYA (Recuerda)

Eres el coach y sistema de acompañamiento reflexivo, riguroso, profundo y transformador de Recuerda. Tu propósito es guiar al usuario a través de rupturas amorosas, duelos vinculares, dependencia afectiva, relaciones intermitentes, impulsos de contacto (craving relacional), rumiación mental obsesiva e idealización del pasado, consolidando su estabilidad somática, autonomía personal y reconstrucción de identidad.

## JERARQUÍA DE CONOCIMIENTO Y MARCO CLÍNICO:
1. **Nivel 1 (Evidencia Científica):** Revisiones sistemáticas, metaanálisis, guías profesionales, apego, TCC, ACT, mindfulness y autocompasión.
2. **Nivel 2 (Modelos Psicológicos):** Indicadores → Contexto → Hipótesis → Preguntas → Intervención → Herramienta → Seguimiento → Reevaluación.
3. **Nivel 3 (Protocolos Recuerda):** Problema → Detección → Contexto → Hipótesis → Intervención → Herramienta → Seguimiento.
4. **Nivel 4 (Marcos de Significado):** Filosofía (Estoicismo) y Espiritualidad (Cristianismo) como lentes opcionales elegidas por el usuario, sin sustituir la base científica.
5. **Nivel 5 (Divulgación):** Referencias secundarias de lenguaje (Rolón, Congost, Rojas Estapé), nunca como autoridad científica primaria.

## FLUJO DE FORMULACIÓN CONTEXTUAL:
Contexto del usuario → Formulación contextual → Hipótesis de trabajo → Intervención → Herramienta → Resultado → Seguimiento → Reevaluación.

## REGLAS CLÍNICAS Y DE SEGURIDAD ABSOLUTAS (NO NEGOCIABLES):
1. **CRISIS Y SALUD MENTAL:** En caso de ideación suicida o autolesión, activa inmediatamente el protocolo de seguridad con líneas de emergencia (024 / 112 / 988 / 911 / 717 003 717).
2. **PROHIBIDO DIAGNÓSTICOS AUTOMÁTICOS:** Nunca diagnostiques al usuario ni a terceros. Está estrictamente prohibido utilizar etiquetas como "narcisista", "manipulador", "evitativo" o "tóxico" como diagnósticos clínicos automáticos.
3. **DISTINCIÓN ESTRICTA:** Distingue siempre entre: Conducta observada → Interpretación posible → Hipótesis de trabajo → Incertidumbre.
4. **NO HACER LECTURA DE MENTE:** Nunca afirmes qué siente, piensa o planea la expareja.
5. **PROHIBIDO ESPIONAJE DIGITAL:** Jamás valides revisar perfiles, redes, estados o conexiones. Higiene digital absoluta.
6. **NO ALIMENTAR FALSAS ESPERANZAS:** El contacto cero busca la paz y dignidad del usuario, no manipular al otro para que regrese.
7. **RECOMENDACIÓN PROFESIONAL:** Reconoce los límites del sistema y recomienda ayuda psicoterapéutica presencial cuando el contexto clínico lo requiera.
    """.trimIndent()

    private val SYSTEM_PROMPT_COACH_LIFE = """
# SISTEMA DE IA DE COACHING DE VIDA Y CRECIMIENTO: AVE FÉNIX (Coach Life)

Eres el mentor y coach de vida de Ave Fénix en Factor / Recuerda. La fase de duelo y dolor por la ruptura ha quedado atrás; ahora el usuario ha renacido y su enfoque está 100% en el crecimiento personal, la autoaceptación, la autoestima, el establecimiento de nuevos propósitos, hábitos, fitness, nutrición, estudios y desarrollo integral.

## CONTEXTO HISTÓRICO Y RESPETO AL ORIGEN:
Tienes acceso al contexto de su relación anterior para entender de dónde viene y por qué esta transformación es un renacer, pero NO te detienes en el pasado. Toda tu energía se canaliza hacia el empoderamiento presente, las nuevas metas del usuario (gimnasio, correr, estudiar, emprender, salud física y mental) y su evolución.

## DIRECTRICES DE COACHING DE VIDA:
1. **Acompañamiento Activo y Práctico:** Si el usuario quiere ir al gimnasio, correr o estudiar, propónle rutinas de entrenamiento, planes de dieta saludable, consejos de disciplina y motivación.
2. **Preguntas de Seguimiento:** Pregúntale activamente por su día: "¿Fuiste hoy al gimnasio?", "¿Cómo te sientes con tu rutina?", "¿Qué tal tu energía hoy?", "¿Te notas mejor?".
3. **Autoestima y Autoaceptación:** Refuerza su valor personal, su dignidad, su capacidad de superación y su evolución física y mental (peso, altura, medidas, bienestar).
4. **Tono:** Cálido, motivador, exigente con cariño, profundamente empático, inspirador y centrado en la acción.
    """.trimIndent()

    fun buildPromptWithFramework(framework: SoltarFramework, userContext: SoltarUserContext = SoltarUserContext()): String {
        val frameworkBlock = when (framework) {
            SoltarFramework.ESTOICO -> """
## ENFOQUE Y TONO DEL COACH: FILOSOFÍA ESTOICA
- **Voz:** Sobria, firme, compasiva, reflexiva y lúcida. Como un filósofo mentor que exige respeto por uno mismo.
- **Enfoque:** Recuérdale la dicotomía del control, que no mendigue afecto y que proteja su ciudadela interior.
            """.trimIndent()

            SoltarFramework.PSICOLOGIA_MODERNA -> """
## ENFOQUE Y TONO DEL COACH: PSICOLOGÍA MODERNA Y APEGO
- **Voz:** Empática, clínica, comprensiva pero firme en los límites. Valida la biología del apego sin permitir la impulsividad.
- **Enfoque:** Explica que el dolor es abstinencia del apego, enfatiza el contacto cero como autocuidado y aplica defusión cognitiva.
            """.trimIndent()

            SoltarFramework.CATOLICO -> """
## ENFOQUE Y TONO DEL COACH: FE, ESPERANZA Y SABIDURÍA CATÓLICA
- **Voz:** Serena, esperanzadora, consoladora y de profunda dignidad humana y espiritual.
- **Enfoque:** Invita a la custodia del corazón, la entrega de las cargas a Dios, el perdón que libera y la paciencia en el desierto.
            """.trimIndent()
        }

        val contextBlock = """
## CONTEXTO CLÍNICO DEL USUARIO (CALIBRAR EMPATÍA Y PROFUNDIDAD):
${userContext.toClinicalSummary()}
        """.trimIndent()

        val basePrompt = if (userContext.journeyStage == "LIFE_COACH") {
            SYSTEM_PROMPT_COACH_LIFE
        } else {
            SYSTEM_PROMPT_SOLTAR
        }

        return "$basePrompt\n\n$frameworkBlock\n\n$contextBlock"
    }

    fun checkSelfHarmTrigger(input: String): Boolean {
        val lower = input.lowercase()
        val keywords = listOf(
            "suicidio", "suicidarme", "quitarme la vida", "morir", "matarme",
            "autolesion", "autolesionarme", "no quiero vivir", "acabar con todo",
            "no vale la pena vivir", "no tiene sentido seguir viviendo", "quiero desaparecer para siempre",
            "cortarme", "hacerme daño", "quiero morir", "deseo morir", "me quiero morir",
            "terminar con mi vida", "no quiero despertar", "ya no aguanto vivir", "dejar de existir",
            "pastillas para no despertar", "ahorcarme", "tirarme de un puente", "cortarme las venas",
            "no le importo a nadie y quiero morir", "mejor muerto", "mejor muerta", "desearia estar muerto",
            "desearía estar muerto", "desearía estar muerta", "acabar con mi sufrimiento para siempre",
            "no encuentro salida", "acabar de una vez", "para qué seguir", "no vale la pena continuar",
            "ojalá no existiera", "quiero dejar de respirar", "irme para siempre", "desaparecer del mundo",
            "terminar con todo esto", "pastillas para dormir y no despertar", "ya no puedo más con esta vida",
            "no quiero sufrir más y quiero morir", "ganas de matarme", "ganas de morir"
        )
        return keywords.any { lower.contains(it) }
    }

    private fun detectRuminationPattern(input: String, messageCount: Int): Boolean {
        val lower = input.lowercase()
        val ruminationKeywords = listOf(
            "¿por qué me hizo", "por que no me llama", "que estara pensando", "que significa su mensaje",
            "¿y si vuelve?", "y si cambio", "estará con otra", "estara con otro", "revisé su ultima conexion",
            "mire sus historias", "porque me bloqueo", "porque no me contesta", "analizar su actitud",
            "por que no me busca", "descifrar", "que querrá decir", "y si me equivoqué", "y si no fue tan malo",
            "vio mi historia", "miró mi estado", "me desbloqueó", "está en línea", "a quién sigue",
            "con quién habla", "le dio like", "publicó una foto", "puso una indirecta", "borró su foto"
        )
        val matchesKeyword = ruminationKeywords.any { lower.contains(it) }
        return matchesKeyword || (messageCount >= 3 && (lower.contains("ex") || lower.contains("él") || lower.contains("ella")))
    }

    suspend fun generateResponse(
        userMessage: String,
        conversationHistory: List<Pair<String, String>> = emptyList(),
        framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
        userContext: SoltarUserContext = SoltarUserContext(),
        systemInstruction: String? = null
    ): SoltarAiResponse = withContext(Dispatchers.IO) {
        val cleanInput = userMessage.trim().take(1500)

        // 1. Safety check
        if (checkSelfHarmTrigger(cleanInput)) {
            return@withContext SoltarAiResponse(
                replyText = """
⚠️ **MENSAJE DE APOYO Y SEGURIDAD**

Atalaya detecta que estás pasando por un momento de dolor y sufrimiento muy intenso. Tu vida, tu bienestar y tu integridad son lo más importante.

Por favor, comunícate en este mismo instante con profesionales y servicios de ayuda inmediata, confidencial y gratuita:
• **España:** 024 (Línea de Atención a la Conducta Suicida), 717 003 717 (Teléfono de la Esperanza) o 112 (Emergencias).
• **Estados Unidos & Latinoamérica:** 988 (Línea de Prevención del Suicidio y Crisis) o 911.
• **México:** 800 911 2000 (Línea de la Vida).
• **Colombia:** 106 / 192.
• **Argentina:** 135 (Centro de Asistencia al Suicida) o (011) 5275-1135.

*Atalaya es una herramienta de autorregulación y acompañamiento reflexivo para duelos, no un servicio de urgencias médicas ni un sustituto de psicoterapia clínica.*
                """.trimIndent(),
                isRuminationDetected = false,
                stateDetected = "SEGURIDAD",
                suggestedAction = "Contactar ayuda profesional de emergencia"
            )
        }

        val isRumination = detectRuminationPattern(cleanInput, conversationHistory.size)

        // 2. Comprobar OnDeviceLlmEngine (Desactivado temporalmente por salvaguarda inmediata - Bloque 1)
        /*
        if (OnDeviceLlmEngine.isReady()) {
            ...
        }
        */

        // 3. Intentar llamar a Gemini con el súper contexto si hay API Key disponible
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (_: Exception) {
            ""
        }

        val isRobolectric = try {
            android.os.Build.FINGERPRINT.contains("robolectric", ignoreCase = true)
        } catch (_: Exception) {
            false
        }

        if (!isRobolectric && apiKey.isNotBlank() && !apiKey.contains("PLACEHOLDER", ignoreCase = true)) {
            try {
                val capsule = ClinicalKnowledgeBase.findRelevantCapsule(cleanInput, framework)
                val systemPrompt = """
${buildPromptWithFramework(framework, userContext)}
${if (systemInstruction != null) "\n## INSTRUCCIÓN ADICIONAL PARA SIMULACRO:\n$systemInstruction\n" else ""}

## CÁPSULA DE CONOCIMIENTO RELEVANTE PARA ESTA INTERVENCIÓN:
• Título: ${capsule.title}
• Autor/Referente: ${capsule.author}
• Cita/Principio: ${capsule.quoteOrSource}
• Diagnóstico clínico de fondo: ${capsule.diagnosisPrinciple}
• Guía de intervención: ${capsule.clinicalGuidance}
• Pregunta socrática: ${capsule.socraticPrompt}
• Micro-acción sugerida: ${capsule.concreteAction}

## INSTRUCCIÓN DEL COACH (FOCO):
Responde como FOCO, el coach y mentor personal de ${userContext.userName}.
REGLA ABSOLUTA: NUNCA te presentes ni firmes como Adriana ni Atalaya. Llama siempre al usuario por su nombre (${userContext.userName}).
Escribe un mensaje de chat breve, cálido y 100% conversacional (3 a 5 frases en total), como alguien que le conoce bien y lleva ${userContext.streakDays} días de proceso.
PROHIBIDO USAR BLOQUES ESTRUCTURADOS, listas con viñetas, encabezados en negrita o emojis como "Principio Rector", "Pregunta de Autoindagación" o "Paso de Acción Inmediata".
Integra de forma fluida y natural la reflexión central, una referencia sutil a la sabiduría de ${capsule.author} si aporta valor, una pregunta socrática y una micro-acción como parte del propio consejo conversacional.
                """.trimIndent()

                val jsonBody = JSONObject().apply {
                    val contents = JSONArray().apply {
                        // System / dev prompt as initial turn or instructions
                        val userPart = JSONObject().apply {
                            val parts = JSONArray().apply {
                                put(JSONObject().apply { put("text", "$systemPrompt\n\nMENSAJE DEL USUARIO:\n$cleanInput") })
                            }
                            put("parts", parts)
                        }
                        put(userPart)
                    }
                    put("contents", contents)
                }

                val request = Request.Builder()
                    .url(API_URL + "?key=$apiKey")
                    .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val bodyString = response.body?.string() ?: ""
                    val root = JSONObject(bodyString)
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val text = candidates.getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")

                        if (text.isNotBlank()) {
                            return@withContext SoltarAiResponse(
                                replyText = text.trim(),
                                isRuminationDetected = isRumination,
                                stateDetected = if (isRumination) "DEJAR_DE_PERSEGUIR" else "COMPRENDER",
                                suggestedAction = capsule.concreteAction
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gemini online call failed, falling back to local expert clinical reasoning engine", e)
            }
        }

        // 3. Motor clínico y de razonamiento local de alta profundidad (Offline & Context-Aware)
        Log.d(TAG, "Executing advanced local clinical coach reasoning")
        return@withContext executeAdvancedLocalClinicalReasoning(cleanInput, isRumination, framework, userContext, systemInstruction)
    }

    fun executeAdvancedLocalClinicalReasoning(
        input: String,
        isRumination: Boolean,
        framework: SoltarFramework,
        userContext: SoltarUserContext,
        systemInstruction: String? = null
    ): SoltarAiResponse {
        val capsule = ClinicalKnowledgeBase.findRelevantCapsule(input, framework)

        // Selección de variante clínica no repetitiva, profunda y adaptada al marco filosófico/clínico
        val (state, headerGreeting, coreText) = ClinicalVariantRegistry.getResolvedVariant(
            input = input,
            isRumination = isRumination,
            framework = framework,
            userContext = userContext
        )

        val name = if (userContext.userName.isNotBlank() && userContext.userName != "Viajero") userContext.userName else "amigo/a"
        val cleanBody = coreText.replace("**", "").trim()
        val quoteRef = if (capsule.quoteOrSource.isNotBlank()) " Como decía ${capsule.author}, «${capsule.quoteOrSource}»." else ""
        val questionPart = if (capsule.socraticPrompt.isNotBlank()) " Pregúntate esto: ¿${capsule.socraticPrompt.removeSuffix("?")}?" else ""
        val actionPart = if (capsule.concreteAction.isNotBlank()) " Para hoy, te sugiero ${capsule.concreteAction.replaceFirstChar { it.lowercase() }}." else ""

        val reply = buildString {
            append("Hola, $name. ")
            if (cleanBody.isNotBlank()) {
                append(cleanBody).append(" ")
            }
            if (quoteRef.isNotBlank()) {
                append(quoteRef.trim()).append(" ")
            }
            if (questionPart.isNotBlank()) {
                append(questionPart.trim()).append(" ")
            }
            if (actionPart.isNotBlank()) {
                append(actionPart.trim())
            }
        }.trim()

        return SoltarAiResponse(
            replyText = reply,
            isRuminationDetected = isRumination,
            stateDetected = state,
            suggestedAction = capsule.concreteAction
        )
    }

    suspend fun generateJournalMentorship(
        journalContent: String,
        moodTag: String = "Reflexión",
        framework: SoltarFramework = SoltarFramework.ESTOICO,
        userContext: SoltarUserContext = SoltarUserContext()
    ): JournalMentorshipResult = withContext(Dispatchers.IO) {
        val cleanInput = journalContent.trim()
        if (cleanInput.isBlank()) {
            return@withContext JournalMentorshipResult(
                feedback = "Para recibir mentoría filosófica y clínica, escribe con honestidad tus pensamientos, emociones o dudas del día.",
                corePrinciple = "«El autoconocimiento comienza cuando nos atrevemos a mirar la verdad frente a la página en blanco.»",
                socraticQuestion = "¿Qué verdad sobre ti mismo/a estás evitando afrontar hoy?",
                concreteAction = "Escribe dos oraciones sinceras sobre lo que realmente estás experimentando en este instante."
            )
        }

        val capsule = ClinicalKnowledgeBase.findRelevantCapsule(cleanInput, framework)

        // 1. Intento con Gemini API
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && !apiKey.contains("PLACEHOLDER", ignoreCase = true)) {
            try {
                val promptText = """
Eres el mentor y coach reflexivo de Recuerda. Analiza la siguiente entrada de diario personal:
---
EMOCIÓN: $moodTag
MARCO FILOSÓFICO: ${framework.name} (${framework.title})
CONTEXTO DEL USUARIO:
${userContext.toClinicalSummary()}

ENTRADA DEL DIARIO:
$cleanInput

CÁPSULA CLÍNICA DE REFERENCIA:
- Referente: ${capsule.author}
- Cita: ${capsule.quoteOrSource}
- Diagnóstico: ${capsule.diagnosisPrinciple}
- Guía: ${capsule.clinicalGuidance}
---

Genera una mentoría filosófica profunda en JSON estricto con estas 4 claves:
{
  "feedback": "Análisis terapéutico y filosófico en 2 o 3 párrafos. Desmonta las distorsiones cognitivas o la idealización, valida la emoción sin alimentar la falsa esperanza y profundiza en los principios del marco ${framework.name}.",
  "corePrinciple": "Cita o máxima de sabiduría atribuida al autor según el marco.",
  "socraticQuestion": "Pregunta socrática profunda para autoindagación honesta.",
  "concreteAction": "Una micro-acción práctica y alcanzable para hoy."
}
Responde ÚNICAMENTE con el objeto JSON válido.
                """.trimIndent()

                val jsonBody = JSONObject().apply {
                    val contents = JSONArray().apply {
                        val contentObj = JSONObject().apply {
                            val parts = JSONArray().apply {
                                put(JSONObject().apply { put("text", promptText) })
                            }
                            put("parts", parts)
                        }
                        put(contentObj)
                    }
                    put("contents", contents)
                }

                val request = Request.Builder()
                    .url(API_URL + "?key=$apiKey")
                    .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val bodyString = response.body?.string() ?: ""
                    val root = JSONObject(bodyString)
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCandidate = candidates.getJSONObject(0)
                        val text = firstCandidate.getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")

                        val cleanJson = text.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
                        val parsed = JSONObject(cleanJson)
                        return@withContext JournalMentorshipResult(
                            feedback = parsed.optString("feedback", "Reflexión generada."),
                            corePrinciple = parsed.optString("corePrinciple", capsule.quoteOrSource),
                            socraticQuestion = parsed.optString("socraticQuestion", capsule.socraticPrompt),
                            concreteAction = parsed.optString("concreteAction", capsule.concreteAction)
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gemini API journal mentorship failed, utilizing local knowledge engine", e)
            }
        }

        // 2. Mentoría local de alta densidad conceptual
        return@withContext JournalMentorshipResult(
            feedback = """
Examinando tus líneas con rigor y compasión:
${capsule.diagnosisPrinciple}

${capsule.clinicalGuidance}

Recuerda que registrar tus vivencias con esta honestidad es la base para desarticular los sesgos de la memoria y recuperar el mando sobre tus decisiones diarias.
            """.trimIndent(),
            corePrinciple = capsule.quoteOrSource + " — " + capsule.author,
            socraticQuestion = capsule.socraticPrompt,
            concreteAction = capsule.concreteAction
        )
    }

    fun analyzeJournalLocally(entries: List<JournalEntryEntity>): LinguisticAnalysisResult {
        if (entries.isEmpty()) {
            return LinguisticAnalysisResult(
                nivelAutonomia = 5,
                lenguajeRumiativo = 5,
                distorsionesCognitivas = emptyList(),
                cambioDesdeUltimaEntrada = "Aún no hay entradas en tu diario. Comienza a escribir para calibrar tu proceso."
            )
        }
        val latest = entries.first().content.lowercase()
        val previous = if (entries.size > 1) entries[1].content.lowercase() else ""

        val posWords = listOf("yo", "puedo", "decido", "libertad", "paz", "tranquilidad", "presente", "aprender", "crecer")
        val negWords = listOf("él", "ella", "sin", "esperando", "culpa", "nunca", "por qué", "extraño", "dependo")

        val posCount = posWords.sumOf { word -> latest.windowed(word.length).count { it == word } }
        val negCount = negWords.sumOf { word -> latest.windowed(word.length).count { it == word } }

        val autonomia = (5 + (posCount - negCount)).coerceIn(0, 10)
        val rumiativo = (5 + (negCount - posCount)).coerceIn(0, 10)

        val distortions = mutableListOf<String>()
        val catastrofismoKeywords = listOf("terrible", "horrible", "catástrofe", "fin del mundo", "no lo soporto", "insoportable", "ruina", "destruido")
        val bwKeywords = listOf("todo", "nada", "nunca", "siempre", "perfecto", "pésimo", "absolutamente")
        val personalizacionKeywords = listOf("por mi culpa", "lo hizo para", "me lo hizo", "es mi responsabilidad", "me odia")

        if (catastrofismoKeywords.any { latest.contains(it) }) distortions.add("Catastrofismo")
        if (bwKeywords.any { latest.contains(it) }) distortions.add("Pensamiento blanco/negro")
        if (personalizacionKeywords.any { latest.contains(it) }) distortions.add("Personalización")

        val cambio = if (previous.isNotBlank()) {
            val prevPos = posWords.sumOf { word -> previous.windowed(word.length).count { it == word } }
            if (posCount > prevPos) {
                "Mayor sentido de agencia y autonomía respecto a tu entrada anterior."
            } else if (posCount < prevPos) {
                "Ligero incremento en la carga emocional o rumiación en comparación con tu registro previo."
            } else {
                "Estabilidad emocional y reflexiva sostenida desde tu último registro."
            }
        } else {
            "Primera entrada registrada. Has dado un paso fundamental hacia la autoconsciencia."
        }

        return LinguisticAnalysisResult(
            nivelAutonomia = autonomia,
            lenguajeRumiativo = rumiativo,
            distorsionesCognitivas = distortions,
            cambioDesdeUltimaEntrada = cambio
        )
    }

    suspend fun analyzeJournalLinguistic(
        entries: List<JournalEntryEntity>,
        userContext: SoltarUserContext = SoltarUserContext()
    ): LinguisticAnalysisResult = withContext(Dispatchers.IO) {
        if (entries.isEmpty()) {
            return@withContext analyzeJournalLocally(entries)
        }

        val latest = entries.first().content
        val previous = if (entries.size > 1) entries[1].content else ""

        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (_: Exception) {
            ""
        }

        val isRobolectric = try {
            android.os.Build.FINGERPRINT.contains("robolectric", ignoreCase = true)
        } catch (_: Exception) {
            false
        }

        if (!isRobolectric && apiKey.isNotBlank() && !apiKey.contains("PLACEHOLDER", ignoreCase = true)) {
            try {
                val prompt = """
Eres el sistema de análisis lingüístico y clínico de Recuerda. Analiza las siguientes entradas de diario de un usuario en proceso de superación de duelo y dependencia afectiva.
Detecta con rigor clínico y devuelve un objeto JSON estricto con exactamente estas claves:
- "nivelAutonomia" (entero de 0 a 10)
- "lenguajeRumiativo" (entero de 0 a 10)
- "distorsionesCognitivas" (lista de strings detectadas entre: "Catastrofismo", "Pensamiento blanco/negro", "Personalización", u otras si aparecen)
- "cambioDesdeUltimaEntrada" (texto breve describiendo la evolución o contraste con la entrada previa).

ÚLTIMA ENTRADA:
$latest

ENTRADA ANTERIOR (si existe):
$previous

Contexto del usuario:
${userContext.toClinicalSummary()}

Responde ÚNICAMENTE con el objeto JSON válido.
                """.trimIndent()

                val jsonBody = JSONObject().apply {
                    val contents = JSONArray().apply {
                        val partObj = JSONObject().apply {
                            val parts = JSONArray().apply {
                                put(JSONObject().apply { put("text", prompt) })
                            }
                            put("parts", parts)
                        }
                        put(partObj)
                    }
                    put("contents", contents)
                }

                val request = Request.Builder()
                    .url(API_URL + "?key=$apiKey")
                    .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val bodyString = response.body?.string() ?: ""
                    val root = JSONObject(bodyString)
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val text = candidates.getJSONObject(0)
                            .getJSONObject("content")
                            .getJSONArray("parts")
                            .getJSONObject(0)
                            .getString("text")

                        val cleanJson = text.trim().removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
                        val parsed = JSONObject(cleanJson)
                        val autonomia = parsed.optInt("nivelAutonomia", 5)
                        val rumiativo = parsed.optInt("lenguajeRumiativo", 5)
                        val distortionsArray = parsed.optJSONArray("distorsionesCognitivas")
                        val distortions = mutableListOf<String>()
                        if (distortionsArray != null) {
                            for (i in 0 until distortionsArray.length()) {
                                distortions.add(distortionsArray.getString(i))
                            }
                        }
                        val cambio = parsed.optString("cambioDesdeUltimaEntrada", "Evolución favorable.")

                        return@withContext LinguisticAnalysisResult(
                            nivelAutonomia = autonomia,
                            lenguajeRumiativo = rumiativo,
                            distorsionesCognitivas = distortions,
                            cambioDesdeUltimaEntrada = cambio
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gemini linguistic analysis failed, falling back to local analyzer", e)
            }
        }

        return@withContext analyzeJournalLocally(entries)
    }
}
