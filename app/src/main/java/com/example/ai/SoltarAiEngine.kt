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

data class SoltarAiResponse(
    val replyText: String,
    val isRuminationDetected: Boolean = false,
    val suggestedAction: String = "",
    val stateDetected: String = "REGULAR" // REGULAR | COMPRENDER | ACEPTAR | DEJAR_DE_PERSEGUIR | RECONSTRUIR | SEGURIDAD
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
# SISTEMA DE IA DE ACOMPAÑAMIENTO: SOLTAR

Eres el asistente de la aplicación SOLTAR. Tu propósito es acompañar a personas que atraviesan una ruptura, duelo afectivo, dependencia emocional o dificultad para dejar de perseguir a una persona.

## PRINCIPIO CENTRAL:
"Puedes seguir queriendo a alguien y dejar de organizar tu vida alrededor de esa persona."

## OBJETIVOS:
1. REGULAR → COMPRENDER → ACEPTAR → DEJAR DE PERSEGUIR → RECONSTRUIR → RECUPERAR AUTONOMÍA.
2. Reducir: rumiación, impulsividad, comprobación, idealización, dependencia del contacto, intolerancia a la incertidumbre.
3. Aumentar: autonomía, regulación emocional, autoestima, actividad, vida social, identidad propia, foco en lo que depende del usuario.

## MARCOS CONCEPTUALES Y REFERENCIAS:
- Teoría del apego (Bowlby, Ainsworth), duelo afectivo, psicología de pareja.
- Regulación emocional (CBT / TCC, ACT, Marian Rojas Estapé).
- Psicoanálisis del duelo (Gabriel Rolón), dependencia y límites (Silvia Congost).
- Pensamiento estoico práctico (Epicteto, Marco Aurelio, Séneca): Dicotomía del control ("¿Depende de mí o no?").

## REGLAS FUNDAMENTALES Y LÍMITES INVIOLABLES:
1. **NO VALIDAR LA RUMIACIÓN:** No respondas alimentando análisis infinitos sobre qué piensa o por qué actuó la expareja.
2. **DETECTOR DE BUCLES:** Si el usuario insiste en buscar explicaciones, descifrar silencios o analizar mensajes, debes decir explícitamente:
   "Ya tenemos suficiente información para analizar esto. Seguir buscando una explicación probablemente está alimentando la rumiación. Vamos a volver a lo que depende de ti." y sugerir una acción física/concreta (caminar, ducharse, trabajar, llamar a un amigo).
3. **NO DIAGNOSTICAR NI ETIQUETAR:** Jamás afirmes como hecho clínico que la otra persona es "narcisista", "evitativa" o "manipuladora".
4. **DISTINGUIR SIEMPRE:**
   - **HECHO:** Lo observable sin juicio.
   - **INTERPRETACIÓN:** Lo que la mente deduce o asume.
   - **HIPÓTESIS:** Posibilidades alternativas sin certeza.
   - **ACCIÓN:** Lo que el usuario sí puede controlar hoy.
5. **EQUILIBRIO:** No demonizar a la otra persona ni culpabilizar al usuario. Reconocer la responsabilidad propia y ajena con ecuanimidad.
6. **SEGURIDAD:** Si hay riesgo de autolesión o crisis grave, prioriza la derivación a líneas de ayuda profesional (024, 988, 112).
    """.trimIndent()

    private fun checkSelfHarmTrigger(input: String): Boolean {
        val lower = input.lowercase()
        val keywords = listOf(
            "suicidio", "suicidarme", "quitarme la vida", "morir", "matarme",
            "autolesion", "no quiero vivir", "acabar con todo", "no vale la pena vivir"
        )
        return keywords.any { lower.contains(it) }
    }

    private fun detectRuminationPattern(input: String, messageCount: Int): Boolean {
        val lower = input.lowercase()
        val ruminationKeywords = listOf(
            "¿por qué me hizo", "por que no me llama", "que estara pensando", "que significa su mensaje",
            "¿y si vuelve?", "y si cambio", "estará con otra", "estara con otro", "revisé su ultima conexion",
            "mire sus historias", "porque me bloqueo", "porque no me contesta", "analizar su actitud"
        )
        val matchesKeyword = ruminationKeywords.any { lower.contains(it) }
        return matchesKeyword || (messageCount > 4 && lower.contains("ex"))
    }

    suspend fun generateResponse(
        userMessage: String,
        conversationHistory: List<Pair<String, String>> = emptyList()
    ): SoltarAiResponse = withContext(Dispatchers.IO) {
        val cleanInput = userMessage.trim().take(1200)

        // Safety filter check
        if (checkSelfHarmTrigger(cleanInput)) {
            return@withContext SoltarAiResponse(
                replyText = """
⚠️ **MENSAJE DE APOYO Y SEGURIDAD**

SOLTAR detecta que estás pasando por un momento de sufrimiento muy intenso. Tu vida y tu integridad son lo más importante.

Por favor, comunícate en este momento con profesionales capacitados que pueden acompañarte:
• **España:** 024 (Línea de Atención a la Conducta Suicida) o 112
• **Estados Unidos & Latinoamérica:** 988 (Línea de Prevención y Crisis) o 911

*SOLTAR es una herramienta de acompañamiento y autorregulación, no un servicio de urgencias médicas ni sustituto de psicoterapia clínica.*
                """.trimIndent(),
                isRuminationDetected = false,
                stateDetected = "SEGURIDAD",
                suggestedAction = "Contactar ayuda profesional de emergencia"
            )
        }

        val isRumination = detectRuminationPattern(cleanInput, conversationHistory.size)

        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d(TAG, "Running Soltar local clinical logic engine (Offline Fallback)")
            return@withContext executeLocalClinicalLogic(cleanInput, isRumination)
        }

        try {
            val fullPrompt = buildString {
                appendLine("HISTORIAL RECIENTE:")
                conversationHistory.takeLast(4).forEach { (sender, text) ->
                    appendLine("$sender: $text")
                }
                appendLine("\nMENSAJE DEL USUARIO:")
                appendLine(cleanInput)
                if (isRumination) {
                    appendLine("\n[NOTA CLÍNICA]: El usuario muestra indicios de rumiación o intento de descifrar a la otra persona. Aplica el límite anti-rumiación y devuélvelo a lo que depende de él.")
                }
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
                        put(JSONObject().put("text", SYSTEM_PROMPT_SOLTAR))
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.35)
                })
            }

            val url = "$API_URL?key=$apiKey"
            val body = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(url).post(body).build()

            val response = client.newCall(request).execute()
            val responseString = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.w(TAG, "Gemini API call failed with code ${response.code}, falling back to local engine.")
                return@withContext executeLocalClinicalLogic(cleanInput, isRumination)
            }

            val root = JSONObject(responseString)
            val candidates = root.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text") ?: ""

            if (text.isNotBlank()) {
                SoltarAiResponse(
                    replyText = text,
                    isRuminationDetected = isRumination,
                    stateDetected = if (isRumination) "DEJAR_DE_PERSEGUIR" else "COMPRENDER",
                    suggestedAction = if (isRumination) "Cerrar la pantalla y realizar 15 min de actividad física" else ""
                )
            } else {
                executeLocalClinicalLogic(cleanInput, isRumination)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error executing Gemini request", e)
            executeLocalClinicalLogic(cleanInput, isRumination)
        }
    }

    private fun executeLocalClinicalLogic(input: String, isRumination: Boolean): SoltarAiResponse {
        val lower = input.lowercase()

        if (isRumination || lower.contains("por qué") || lower.contains("porque hizo") || lower.contains("descifrar")) {
            return SoltarAiResponse(
                replyText = """
**Ya tenemos suficiente información para analizar esto.**

Seguir buscando una explicación o intentar descifrar lo que la otra persona siente solo alimenta el circuito de la rumiación.

Distingamos los hechos de la interpretación:
• **El Hecho:** La relación terminó o hay una distancia concreta en el presente.
• **La Hipótesis:** Las múltiples razones que tu mente intenta inventar para aliviar la incertidumbre.
• **Lo que depende de ti hoy:** Cuidar tu cuerpo, tu descanso y tu atención.

«No tienes que resolver tu historia hoy. Solo tienes que cuidar tu día.»

Vamos a cerrar este bucle por ahora.
                """.trimIndent(),
                isRuminationDetected = true,
                stateDetected = "DEJAR_DE_PERSEGUIR",
                suggestedAction = "Dar un paseo de 15 min al aire libre sin el móvil"
            )
        }

        if (lower.contains("impulso") || lower.contains("escribir") || lower.contains("llamar") || lower.contains("contactar")) {
            return SoltarAiResponse(
                replyText = """
**El impulso no es una orden.**

Sentir ganas intensas de contactar es una respuesta esperable de tu sistema nervioso ante la abstinencia del vínculo. 

Pregúntate con honestidad:
1. **¿Qué esperas conseguir?** (¿Alivio momentáneo o una respuesta que no cambiará la realidad de fondo?).
2. **¿Qué depende de ti?** (Tu propia conducta y tu dignidad).
3. **¿Qué no puedes saber?** (Cómo reaccionará la otra persona).

Puedes sentir el impulso sin obedecerlo. Activa el botón de **MODO IMPULSO** para acompañar los 20 minutos de espera consciente.
                """.trimIndent(),
                isRuminationDetected = false,
                stateDetected = "REGULAR",
                suggestedAction = "Iniciar temporizador de 20 minutos en Modo Impulso"
            )
        }

        if (lower.contains("extraño") || lower.contains("nostalgia") || lower.contains("idealiz")) {
            return SoltarAiResponse(
                replyText = """
**La nostalgia tiende a borrar el contexto.**

Es completamente legítimo extrañar momentos cálidos o la sensación de compañía. Sin embargo, recuerda:
• **Extrañar no significa que la relación fuera viable o sana en su totalidad.**
• **El dolor que sientes es el costo del duelo, no una señal de que debas volver a insistir.**

Como señala la psicología del apego y el duelo afectivo (Gabriel Rolón / Bowlby), deconstruir la idealización no es odiar al otro, sino aceptar la realidad completa de lo que se vivió.
                """.trimIndent(),
                isRuminationDetected = false,
                stateDetected = "ACEPTAR",
                suggestedAction = "Revisar la sección 'Lo que extraño vs Lo que realmente viví'"
            )
        }

        return SoltarAiResponse(
            replyText = """
Te escucho con atención y rigor.

En el proceso de soltar hay una regla fundamental:
*«Puedes seguir queriendo a alguien y dejar de organizar tu vida alrededor de esa persona.»*

Separemos con claridad:
1. **¿Cuál es el hecho observable?**
2. **¿Qué estás interpretando?**
3. **¿Qué paso concreto puedes dar hoy por ti mismo/a?**

Dime en qué foco o área de tu vida quieres concentrarte hoy.
            """.trimIndent(),
            isRuminationDetected = false,
            stateDetected = "RECONSTRUIR",
            suggestedAction = "Completar el check-in diario en la pestaña HOY"
        )
    }
}
