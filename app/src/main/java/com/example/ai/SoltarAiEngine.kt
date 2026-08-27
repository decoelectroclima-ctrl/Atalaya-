package com.example.ai

import android.util.Log
import com.example.BuildConfig
import com.example.data.ClinicalKnowledgeBase
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
    val streakDays: Int = 0,
    val totalCheckins: Int = 0,
    val lastCheckinMood: String = "",
    val averageAutonomyScore: Float = 5f,
    val recentRelapseTriggers: List<String> = emptyList(),
    val recentPatternsAudited: List<String> = emptyList(),
    val activeIdentityGoals: List<String> = emptyList(),
    val framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA
) {
    fun toClinicalSummary(): String {
        val parts = mutableListOf<String>()
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
        return if (parts.isEmpty()) "Sin datos previos registrados." else parts.joinToString("\n")
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
# SISTEMA DE IA DE ACOMPAÑAMIENTO Y COACHING CLÍNICO: ADRIANA

Eres ADRIANA, el sistema y coach de acompañamiento emocional reflexivo, riguroso, profundo y transformador. Tu propósito es guiar al usuario a través de rupturas amorosas, duelos vinculares, dependencia afectiva, relaciones intermitentes, impulsos de contacto (craving relacional), rumiación mental obsessiva e idealización del pasado, consolidando su estabilidad somática, autonomía personal y reconstrucción de identidad.

## PRINCIPIOS DE VIDA Y PILARES FUNDAMENTALES:
1. "Puedes seguir queriendo a alguien y dejar de organizar tu vida alrededor de esa persona."
2. "No necesitas dejar de sentir para empezar a soltar y recuperar tu dignidad."
3. "El desamor duele, pero la insistencia donde no hay reciprocidad destruye la autoestima."

## PROTOCOLO Y SECUENCIA DE COACHING CLÍNICO:
1. REGULAR EL SISTEMA NERVIOSO (Anclaje somático) → COMPRENDER LA EMOCIÓN (Sin juzgar) → ACEPTAR LA REALIDAD (Fin del autoengaño) → DEJAR DE PERSEGUIR (Contacto cero estricto) → RECONSTRUIR IDENTIDAD → RECUPERAR AUTONOMÍA SOBERANA.
2. Desactivar patrones tóxicos: rumiación obsesiva ("¿por qué lo hizo?"), espionaje digital (redes/conexiones), justificación de desaires, fantasía de rescate.
3. Potenciar: autodominio, dignidad, respeto por el dolor propio, límites inquebrantables y enfoque radical en lo que depende exclusivamente de uno mismo.

## TRIPLE ENFOQUE FILOSÓFICO Y PSICOLÓGICO:
- **ESTOICISMO (Marco Aurelio, Epicteto, Séneca):** Dicotomía del control, ciudadela interior, hegemonikón (facultad rectora), suspensión de juicios precipitados y Amor Fati.
- **PSICOLOGÍA MODERNA (Bowlby, Silvia Congost, Gabriel Rolón, TCC/ACT, Steven Hayes, Marian Rojas Estapé):** Regulación del sistema de apego (desamparo biológico y abstinencia dopaminérgica), límites protectores definitivos, duelo como travesía de la palabra, defusión cognitiva y equilibrio del cortisol.
- **CATÓLICO (Proverbios, Eclesiastés, Salmos, San Pablo, San Agustín):** Custodia del corazón (Pr 4:23), tiempo oportuno y desierto de maduración (Ecl 3), perdón como liberación del rencor y gracia para el dominio propio (2 Tim 1:7).

## REGLAS DE SEGURIDAD ABSOLUTAS:
1. En caso de ideación suicida o autolesión, deriva de inmediato a las líneas oficiales (024 / 112 / 988 / 911).
2. Prohibido validar el espionaje o alimentar la rumiación. Corta siempre con preguntas de soberanía y micro-acciones prácticas.
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

        return "$SYSTEM_PROMPT_SOLTAR\n\n$frameworkBlock\n\n$contextBlock"
    }

    fun checkSelfHarmTrigger(input: String): Boolean {
        val lower = input.lowercase()
        val keywords = listOf(
            "suicidio", "suicidarme", "quitarme la vida", "morir", "matarme",
            "autolesion", "autolesionarme", "no quiero vivir", "acabar con todo",
            "no vale la pena vivir", "no tiene sentido seguir viviendo", "quiero desaparecer para siempre",
            "cortarme", "hacerme daño"
        )
        return keywords.any { lower.contains(it) }
    }

    private fun detectRuminationPattern(input: String, messageCount: Int): Boolean {
        val lower = input.lowercase()
        val ruminationKeywords = listOf(
            "¿por qué me hizo", "por que no me llama", "que estara pensando", "que significa su mensaje",
            "¿y si vuelve?", "y si cambio", "estará con otra", "estara con otro", "revisé su ultima conexion",
            "mire sus historias", "porque me bloqueo", "porque no me contesta", "analizar su actitud",
            "por que no me busca", "descifrar", "que querrá decir", "y si me equivoqué", "y si no fue tan malo"
        )
        val matchesKeyword = ruminationKeywords.any { lower.contains(it) }
        return matchesKeyword || (messageCount > 3 && lower.contains("ex"))
    }

    suspend fun generateResponse(
        userMessage: String,
        conversationHistory: List<Pair<String, String>> = emptyList(),
        framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
        userContext: SoltarUserContext = SoltarUserContext()
    ): SoltarAiResponse = withContext(Dispatchers.IO) {
        val cleanInput = userMessage.trim().take(1500)

        // 1. Safety check
        if (checkSelfHarmTrigger(cleanInput)) {
            return@withContext SoltarAiResponse(
                replyText = """
⚠️ **MENSAJE DE APOYO Y SEGURIDAD**

ADRIANA detecta que estás pasando por un momento de sufrimiento muy intenso. Tu vida y tu integridad son lo más importante.

Por favor, comunícate en este momento con profesionales capacitados que pueden acompañarte confidencialmente:
• **España:** 024 (Línea de Atención a la Conducta Suicida) o 112 (Emergencias)
• **Estados Unidos & Latinoamérica:** 988 (Línea de Prevención y Crisis) o 911

*ADRIANA es una herramienta de autorregulación y acompañamiento reflexivo, no un servicio de urgencias médicas ni sustituto de psicoterapia clínica.*
                """.trimIndent(),
                isRuminationDetected = false,
                stateDetected = "SEGURIDAD",
                suggestedAction = "Contactar ayuda profesional de emergencia"
            )
        }

        val isRumination = detectRuminationPattern(cleanInput, conversationHistory.size)

        // 2. Intentar llamar a Gemini con el súper contexto si hay API Key disponible
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && !apiKey.contains("PLACEHOLDER", ignoreCase = true)) {
            try {
                val capsule = ClinicalKnowledgeBase.findRelevantCapsule(cleanInput, framework)
                val systemPrompt = """
${buildPromptWithFramework(framework, userContext)}

## CÁPSULA DE CONOCIMIENTO RELEVANTE PARA ESTA INTERVENCIÓN:
• Título: ${capsule.title}
• Autor/Referente: ${capsule.author}
• Cita/Principio: ${capsule.quoteOrSource}
• Diagnóstico clínico de fondo: ${capsule.diagnosisPrinciple}
• Guía de intervención: ${capsule.clinicalGuidance}
• Pregunta socrática: ${capsule.socraticPrompt}
• Micro-acción sugerida: ${capsule.concreteAction}

## INSTRUCCIÓN DEL COACH:
Responde como el coach ADRIANA en español, con calidez, rigor, máxima empatía y profundidad terapéutica/filosófica.
Estructura tu respuesta en:
1. **Validación y Diagnóstico Lúcido** (Reconoce la emoción sin justificar la conducta desadaptativa).
2. **Razonamiento Profundo del Marco ${framework.title}** (Usa la sabiduría y los principios clínicos pertinentes).
3. **Pregunta Socrática de Confrontación Amorosa** (Para que el usuario mire hacia su propio poder).
4. **Micro-Paso de Acción Inmediata**.

Sé conciso pero contundente (máximo 250-300 palabras).
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
        return@withContext executeAdvancedLocalClinicalReasoning(cleanInput, isRumination, framework, userContext)
    }

    private fun executeAdvancedLocalClinicalReasoning(
        input: String,
        isRumination: Boolean,
        framework: SoltarFramework,
        userContext: SoltarUserContext
    ): SoltarAiResponse {
        val capsule = ClinicalKnowledgeBase.findRelevantCapsule(input, framework)
        val lower = input.lowercase()

        // Identificación de patrón contextual
        val (state, headerGreeting, coreAnalysis) = when {
            isRumination || lower.contains("por qué") || lower.contains("porque hizo") || lower.contains("descifrar") || lower.contains("analizar") -> {
                Triple(
                    "DEJAR_DE_PERSEGUIR",
                    "**Frenemos el bucle: Ya tienes suficiente información para comprender esto.**",
                    """
Seguir intentando descifrar las intenciones, silencios o contradicciones de la otra persona solo mantiene encendido el circuito de la rumiación.

Distingamos los hechos observables de la fantasía:
• **El Hecho:** El vínculo se rompió o la distancia es un hecho real en el presente.
• **La Hipótesis:** Las mil explicaciones que tu mente inventa intentando calmar la incertidumbre.
• **La Soberanía:** Lo único que puedes gobernar hoy son tus decisiones, tu descanso y tu atención.

${capsule.clinicalGuidance}
                    """.trimIndent()
                )
            }

            lower.contains("impulso") || lower.contains("escribir") || lower.contains("llamar") || lower.contains("contactar") || lower.contains("buscarlo") || lower.contains("buscarla") || lower.contains("mensaje") -> {
                Triple(
                    "REGULAR",
                    "**El impulso es solo una ola neuroquímica; no es una orden que debas obedecer.**",
                    """
Lo que sientes en el pecho no es una señal mística de que debas romper la distancia. Es la respuesta biológica de alarma y abstinencia de tu sistema nervioso ante la pérdida de la figura vincular.

Antes de mover las manos, analicemos con rigor:
1. **¿Qué buscas realmente?** Un alivio fugaz de 10 minutos a cambio de reiniciar semanas de cicatrización emocional.
2. **¿Qué no depende de ti?** Cómo responderá o qué sentirá la otra persona.
3. **¿Qué sí depende de ti?** Tu honor, tu palabra y tu templanza en este momento exacto.

${capsule.clinicalGuidance}
                    """.trimIndent()
                )
            }

            lower.contains("no puedo vivir sin") || lower.contains("le necesito") || lower.contains("la necesito") || lower.contains("no soy nada sin") || lower.contains("dependo de") || lower.contains("vacío") -> {
                Triple(
                    "COMPRENDER",
                    "**Diferenciemos el afecto legítimo de la dependencia emocional.**",
                    """
Sentir que "no puedes vivir sin esa persona" es la forma en que tu cerebro traduce el miedo al desamparo.

Revisemos esta distinción fundamental:
• **Amor:** Desear compartir la vida desde la propia plenitud y dignidad.
• **Dependencia:** Usar la presencia del otro como único ansiolítico para no sentir la soledad.

Tu valor como ser humano no está hipotecado a la aprobación de nadie.

${capsule.clinicalGuidance}
                    """.trimIndent()
                )
            }

            lower.contains("extraño") || lower.contains("nostalgia") || lower.contains("idealiz") || lower.contains("recuerdo") || lower.contains("echo de menos") || lower.contains("duele") || lower.contains("triste") -> {
                Triple(
                    "ACEPTAR",
                    "**La nostalgia tiende a embellecer el pasado y borrar las heridas reales.**",
                    """
Es totalmente legítimo y humano extrañar momentos cálidos o la sensación de refugio. Sin embargo, no permitas que la memoria selectiva te engañe:
• **Extrañar no significa que la relación fuera viable ni sana.**
• **El dolor que sientes es el trabajo psíquico de despedir una etapa, no una invitación a volver.**
• **El duelo oscila:** Habrá días de calma y días de oleaje; esto no es un retroceso, es cicatrización.

${capsule.clinicalGuidance}
                    """.trimIndent()
                )
            }

            lower.contains("culpa") || lower.contains("perdón") || lower.contains("rencor") || lower.contains("injusto") || lower.contains("odio") || lower.contains("rabia") -> {
                Triple(
                    "ACEPTAR",
                    "**Sostener el rencor o la culpa es seguir atado/a a lo que ya pasó.**",
                    """
La culpa te atrapa en la fantasía de que podías haberlo previsto todo. El rencor te hace rehén de la persona que te hirió.

Ni el autorreproche ni la amargura tienen el poder de reescribir la historia. El verdadero cierre no viene de que te pidan perdón; viene de decidir que tu presente no le pertenece al daño del ayer.

${capsule.clinicalGuidance}
                    """.trimIndent()
                )
            }

            lower.contains("límite") || lower.contains("contacto cero") || lower.contains("bloque") || lower.contains("redes") || lower.contains("espiar") || lower.contains("ver su") -> {
                Triple(
                    "REGULAR",
                    "**El límite firme es el espacio aséptico donde tu herida puede sanar.**",
                    """
Revisar perfiles, estados o buscar intermediarios es mantener una microdosis de toxicidad y alerta en tu sistema nervioso.

El contacto cero y la distancia absoluta no son un castigo para el otro: son la muralla protectora que le pones a tu salud mental y a tu dignidad.

${capsule.clinicalGuidance}
                    """.trimIndent()
                )
            }

            lower.contains("autoestima") || lower.contains("no valgo") || lower.contains("inútil") || lower.contains("rechazo") || lower.contains("vergüenza") || lower.contains("fracaso") -> {
                Triple(
                    "COMPRENDER",
                    "**El desinterés o la incapacidad de alguien de amarte no define tu valor.**",
                    """
Tu valor intrínseco no fluctúa según el trato que recibes de una persona desregulada o incompatible.

La herida del rechazo confunde 'no haber sido elegido/a por alguien' con 'no ser valioso/a'. Eres una persona completa, con dignidad y capacidad intacta de florecer.

${capsule.clinicalGuidance}
                    """.trimIndent()
                )
            }

            lower.contains("soledad") || lower.contains("solo") || lower.contains("sola") || lower.contains("vacío") || lower.contains("desamparo") -> {
                Triple(
                    "ACEPTAR",
                    "**La soledad no es un abismo de castigo, sino el espacio para reencontrarte.**",
                    """
El silencio que queda tras una ruptura asusta porque revela cuánto te habías abandonado para complacer al otro.

Estar contigo mismo/a no es estar vacío/a; es recuperar el espacio sagrado donde tú vuelves a ser el protagonista de tu propia existencia.

${capsule.clinicalGuidance}
                    """.trimIndent()
                )
            }

            else -> {
                Triple(
                    "RECONSTRUIR",
                    "**Acompaño tu proceso con rigor, presencia y claridad.**",
                    """
En el camino de soltar y reconstruirte hay una verdad innegociable:
*«Puedes seguir queriendo a alguien y, al mismo tiempo, dejar de organizar tu vida alrededor de esa persona.»*

Hoy estás dando un paso más hacia tu soberanía afectiva. Cada vez que eliges tu tranquilidad por encima de la desesperación, forjas una versión de ti más libre y madura.

${capsule.clinicalGuidance}
                    """.trimIndent()
                )
            }
        }

        // Ensamblar respuesta clínica completa y estructurada
        val reply = """
$headerGreeting

$coreAnalysis

---
💡 **Principio Rector (${capsule.author}):**
${capsule.quoteOrSource}

❓ **Pregunta de Autoindagación:**
${capsule.socraticPrompt}

🎯 **Paso de Acción Inmediata:**
${capsule.concreteAction}
        """.trimIndent()

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
Eres el mentor y coach reflexivo de ADRIANA. Analiza la siguiente entrada de diario personal:
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
}
