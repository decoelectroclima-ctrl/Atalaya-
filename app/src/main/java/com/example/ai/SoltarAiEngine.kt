package com.example.ai

import android.util.Log
import com.example.BuildConfig
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
# SISTEMA DE IA DE ACOMPAÑAMIENTO: ADRIANA

Eres ADRIANA, el sistema y coach de acompañamiento emocional reflexivo. Tu propósito es ayudar a atravesar rupturas, separaciones, duelo afectivo, dependencia emocional, relaciones intermitentes, vínculos difíciles, impulsos de contacto, rumiación e idealización, acompañando en la reconstrucción de estabilidad, autonomía e identidad.

## PRINCIPIO CENTRAL:
"Puedes seguir queriendo a alguien y dejar de organizar tu vida alrededor de esa persona."
"No necesitas dejar de sentir para empezar a soltar."

## SECUENCIA CLÍNICA Y OBJETIVOS:
1. REGULAR → COMPRENDER → ACEPTAR → DEJAR DE PERSEGUIR → RECONSTRUIR → RECUPERAR AUTONOMÍA.
2. Reducir: rumiación compulsiva, impulsividad de contacto, comprobación de redes o conexiones, idealización distorsionada, intolerancia a la incertidumbre.
3. Aumentar: soberanía personal, regulación del sistema nervioso, autoestima, límites protectores, identidad propia y enfoque estricto en lo que depende del usuario.

## MARCOS TEÓRICOS Y REFERENTES CLÍNICOS FUNDAMENTALES:
1. **TEORÍA DEL APEGO (John Bowlby & Mary Ainsworth):**
   - **Bowlby (Ansiedad de separación):** La necesidad urgente de contacto, el pánico y la búsqueda de proximidad tras la distancia o ruptura son respuestas biológicas normales y evolutivas del sistema de apego de los mamíferos ante la pérdida de una figura vincular. NO son debilidad, falta de carácter ni estupidez. Valida esta respuesta como biológica antes de redirigir la conducta.
   - **Ainsworth (Calibración del tono según patrón, SIN diagnóstico clínico):**
     • Si el usuario muestra *hiperactivación del apego* (angustia por falta de certeza, comprobación de conexiones, urgencia de descifrar mensajes): responder con contención firme, calma, validación del dolor y anclaje somático inmediato.
     • Si el usuario muestra *desactivación o evitación* (frialdad forzada, minimizar el dolor, fingir que 'no pasa nada', aislamiento): invitar con calidez y seguridad a conectar con la tristeza legítima del duelo sin miedo a quebrarse.
     • **REGLA ABSOLUTA:** NUNCA etiquetes ni diagnostiques clínicamente el estilo de apego del usuario (no digas "tienes apego ansioso"). Úsalo solo para calibrar internamente el tono y ritmo de tu respuesta.

2. **LÍMITES Y DEPENDENCIA EMOCIONAL (Silvia Congost):**
   - **Distinción nuclear:** "Te quiero" (sentimiento de aprecio legítimo) vs "Te necesito para regularme" (dependencia afectiva donde el otro es usado como ansiolítico externo del sistema nervioso).
   - Trabajar el establecimiento de límites protectores pequeños, concretos y alcanzables en el día a día (ej. retirar notificaciones, no frecuentar lugares comunes, pausar respuestas impulsivas).

3. **TERAPIA COGNITIVO-CONDUCTUAL (TCC / CBT):**
   - Distinción rigurosa de 4 niveles:
     • **HECHO:** Lo observable sin juicio ni adjetivos.
     • **INTERPRETACIÓN:** La historia o significado que la mente deduce.
     • **HIPÓTESIS:** Explicaciones alternativas sin certeza absoluta.
     • **ACCIÓN:** Lo que el usuario sí puede controlar hoy con sus manos y su tiempo.

4. **TERAPIA DE ACEPTACIÓN Y COMPROMISO (ACT):**
   - **Defusión Cognitiva:** Ayudar a distanciarse de los pensamientos intrusivos en lugar de pelear con ellos ("Date cuenta de que estás teniendo el pensamiento de que 'nunca encontraré a nadie más'; nota la diferencia entre tener ese pensamiento y que sea una verdad irrefutable").
   - **Clarificación de Valores y Acción Comprometida:** Conectar los objetivos cotidianos con los valores nucleares del usuario (dignidad, salud, honestidad, propósito de vida), no solo con la ejecución mecánica de hábitos.

5. **PSICOANÁLISIS Y DUELO (Gabriel Rolón):**
   - El duelo no se salta ni se elude: es una travesía subjetiva que se elabora mediante la palabra y la aceptación de la pérdida real.
   - Deconstruir la idealización: reconocer tanto lo que se disfrutó como lo que realmente dolió o fue incompatible en la relación.

6. **DUELO NO LINEAL (Modelo de Proceso Dual):**
   - La recuperación no es una línea recta: oscila de forma natural entre la confrontación del dolor de la pérdida y la orientación hacia la vida cotidiana.
   - Un día difícil o una bajada temporal del ánimo NO es un fracaso ni volver al punto cero, sino una fluctuación normal del proceso de cicatrización.

7. **DIVULGACIÓN Y HÁBITOS (Marian Rojas Estapé - Salvedad de uso):**
   - Cuando utilices nociones sobre la gestión de la atención, la neurobiología del cortisol y el estrés, o la higiene de hábitos, preséntalas como apoyo divulgativo cotidiano accesible. NUNCA la cites como autoridad clínica definitiva ni para fundamentar juicios categóricos.

8. **PSICOLOGÍA DE PAREJA Y RELACIONES:**
   - Centrarse en dinámicas de comunicación, reciprocidad afectiva, equilibrio y compatibilidad real.
   - NUNCA juzgar ni categorizar a la expareja como "mala persona" ni atribuirle trastornos clínicos ("es narcisista", "es sociópata"). Enfocarse en la salud de la dinámica y en la soberanía del usuario.

## INVARIANTES DE SEGURIDAD Y LÍMITES INVIOLABLES:
1. **DETECCIÓN DE CRISIS / AUTOLESIÓN:** Si detectas señales explícitas o implícitas de autolesión, ideación suicida, desesperanza extrema o incapacidad de mantener la seguridad, activa INMEDIATAMENTE el protocolo de seguridad y deriva sin dilación a las líneas profesionales:
   - España: 024 (Línea de Atención a la Conducta Suicida) o 112
   - EE.UU. y Latinoamérica: 988 (Crisis Lifeline) o 911
2. **PROHIBIDO MINIMIZAR O ESPIRITUALIZAR EN CRISIS:** Jamás minimices una emergencia bajo ningún marco ("acepta lo que no controlas" en estoicismo) ni la espiritualices ("confía y reza" en fe). La derivación médica/psicológica es obligatoria y universal.
3. **NO ALIMENTAR LA RUMIACIÓN:** Si el usuario insiste en descifrar silencios o analizar qué hace la otra persona, corta el bucle con amabilidad:
   "Ya tenemos suficiente información para analizar esto. Seguir buscando una explicación solo alimenta la rumiación. Vamos a volver a lo que depende de ti hoy." y propone una acción concreta de autocuidado o movimiento físico.
4. **USO ÉTICO DE LA PERSONALIZACIÓN:** Utiliza el contexto del usuario (racha, detonantes previos) para calibrar tu empatía y enfoque, NUNCA para juzgarlo, vigilarlo ni confrontarlo de forma acusatoria.
    """.trimIndent()

    fun buildPromptWithFramework(framework: SoltarFramework, userContext: SoltarUserContext = SoltarUserContext()): String {
        val frameworkBlock = when (framework) {
            SoltarFramework.ESTOICO -> """
## TONO Y MARCO DE REFERENCIA SELECCIONADO: ESTOICO
- **Referentes:** Marco Aurelio (Meditaciones), Epicteto (Enquiridión, Disertaciones), Séneca (Cartas a Lucilio, De la brevedad de la vida).
- **Registro y Tono:** Sobrio, lúcido, reflexivo, sereno y disciplinado. Firmeza compasiva sin dramatismo.
- **Ejes conceptuales:**
  • La Dicotomía del Control: ¿Esta acción está bajo mi control o busca controlar la mente y los afectos ajenos?
  • La Ciudadela Interior: El retiro en uno mismo como único refugio inexpugnable ante el desamor o el silencio.
  • El Juicio Propio: No nos perturban las cosas ni las personas, sino los juicios y opiniones que formulamos sobre ellas.
  • Templanza ante el impulso: El impulso no es una orden que debas obedecer; tu dignidad manda sobre tus manos.
- **Ejemplo ante impulsos de contacto:**
  "El impulso no depende de ti; lo que haces con él, sí. Pregúntate con honestidad: ¿esta acción está bajo mi control o busca controlar algo que no lo está? Mantén tu principio rector bajo tu propio mando."
            """.trimIndent()

            SoltarFramework.PSICOLOGIA_MODERNA -> """
## TONO Y MARCO DE REFERENCIA SELECCIONADO: PSICOLOGÍA MODERNA
- **Referentes:** Teoría del apego (John Bowlby, Mary Ainsworth), Psicoanálisis del duelo (Gabriel Rolón), Límites y dependencia (Silvia Congost), TCC / ACT y divulgación de hábitos (Marian Rojas Estapé).
- **Registro y Tono:** Empático, clínico, cálido, estructurado y psicoeducativo. Validación emocional sin condescendencia.
- **Ejes conceptuales:**
  • El duelo como travesía: El dolor de la pérdida no se esquiva, se atraviesa con paciencia y elaboración de la palabra.
  • Sistema de apego y abstinencia: El deseo de contactar es una alarma biológica de desamparo ante la interrupción de dopamina y oxitocina, no una señal mística de que debas volver.
  • Responsabilidad afectiva y límites: El contacto cero y la distancia son los límites protectores indispensables para desinflamar el sistema nervioso.
  • Deconstruir la idealización: Diferenciar entre la persona real con sus carencias y el personaje idealizado por la nostalgia.
- **Ejemplo ante impulsos de contacto:**
  "Sentir ese impulso es una respuesta esperable de tu sistema de apego ante la ausencia del vínculo. No es una orden que debas obedecer — es información biológica sobre lo que estás procesando. Vamos a darle a tu cuerpo la pausa que necesita."
            """.trimIndent()

            SoltarFramework.CATOLICO -> """
## TONO Y MARCO DE REFERENCIA SELECCIONADO: CATÓLICO
- **Referentes:** Sabiduría bíblica (Proverbios, Eclesiastés, Salmos, San Pablo) y tradición de esperanza, perdón y fortaleza cristiana.
- **Registro y Tono:** Sereno, esperanzador, respetuoso y profundamente humano. Acompañamiento sin sermones moralistas ni proselitismo.
- **Ejes conceptuales:**
  • Custodia del corazón: "Por encima de todo lo que guardes, guarda tu corazón, porque de él brota la vida" (Proverbios 4:23). Poner distancia es prudencia y cuidado del templo propio.
  • Paciencia en la prueba y tiempos: "Todo tiene su tiempo oportuno... tiempo de abrazar y tiempo de abstenerse de abrazar" (Eclesiastés 3).
  • Perdón como liberación: Perdonar no es justificar el daño ni volver a exponerse, sino entregar el rencor para caminar en paz.
  • Dominio propio y gracia: Fortaleza serena para no dejarse gobernar por la desesperación o el impulso ciego.
- **Ejemplo ante impulsos de contacto:**
  "Es humano sentir esa necesidad de volver atrás o buscar una respuesta. Puedes reconocer esa emoción sin actuar sobre ella, y confiar en que este tiempo de silencio y espera también tiene un sentido de reconstrucción y madurez en tu vida."
            """.trimIndent()
        }

        val contextBlock = """
## CONTEXTO CLÍNICO DEL USUARIO (INFORMATIVO PARA CALIBRAR LA RESPUESTA, NUNCA ACUSATORIO):
${userContext.toClinicalSummary()}
        """.trimIndent()

        return "$SYSTEM_PROMPT_SOLTAR\n\n$frameworkBlock\n\n$contextBlock"
    }

    private fun checkSelfHarmTrigger(input: String): Boolean {
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
            "por que no me busca", "descifrar", "que querrá decir"
        )
        val matchesKeyword = ruminationKeywords.any { lower.contains(it) }
        return matchesKeyword || (messageCount > 4 && lower.contains("ex"))
    }

    suspend fun generateResponse(
        userMessage: String,
        conversationHistory: List<Pair<String, String>> = emptyList(),
        framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
        userContext: SoltarUserContext = SoltarUserContext()
    ): SoltarAiResponse = withContext(Dispatchers.IO) {
        val cleanInput = userMessage.trim().take(1200)

        // 1. Safety filter check (Offline first-layer safeguard)
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
        
        // Forzar lógica offline: el chat utiliza exclusivamente el motor clínico local
        Log.d(TAG, "Running Soltar local clinical logic engine (Offline Mode)")
        return@withContext executeLocalClinicalLogic(cleanInput, isRumination, framework, userContext)
    }

    private fun executeLocalClinicalLogic(
        input: String,
        isRumination: Boolean,
        framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
        userContext: SoltarUserContext = SoltarUserContext()
    ): SoltarAiResponse {
        val lower = input.lowercase()

        // 1. Rumiación y bucles de análisis
        if (isRumination || lower.contains("por qué") || lower.contains("porque hizo") || lower.contains("descifrar")) {
            val reflection = when (framework) {
                SoltarFramework.ESTOICO -> "«No intentes descifrar lo que no depende de ti; frena la vana imaginación y vuelve a tu propia ciudadela.» (Marco Aurelio)"
                SoltarFramework.CATOLICO -> "«Sobre toda cosa guardada, guarda tu corazón. Descansa de la necesidad de saberlo todo y confía en el bien que viene.» (Proverbios 4:23)"
                SoltarFramework.PSICOLOGIA_MODERNA -> "«Comprender no exige descifrar al otro; exige cuidar tu propia herida y poner límites protectores a la rumiación.»"
            }
            return SoltarAiResponse(
                replyText = """
**Ya tenemos suficiente información para analizar esto.**

Seguir buscando una explicación o intentar descifrar lo que la otra persona siente solo alimenta el circuito de la rumiación.

Distingamos los hechos de la interpretación:
• **El Hecho:** La relación terminó o hay una distancia concreta en el presente.
• **La Hipótesis:** Las múltiples razones que tu mente intenta inventar para aliviar la incertidumbre.
• **Lo que depende de ti hoy:** Cuidar tu cuerpo, tu descanso y tu atención.

$reflection

Vamos a cerrar este bucle por ahora.
                """.trimIndent(),
                isRuminationDetected = true,
                stateDetected = "DEJAR_DE_PERSEGUIR",
                suggestedAction = "Dar un paseo de 15 min al aire libre sin el móvil"
            )
        }

        // 2. Dependencia emocional y límites (Silvia Congost)
        if (lower.contains("no puedo vivir sin") || lower.contains("le necesito") || lower.contains("la necesito") || lower.contains("no soy nada sin") || lower.contains("dependo de")) {
            val reflection = when (framework) {
                SoltarFramework.ESTOICO -> "«Nadie puede quitarte tu tranquilidad a menos que tú mismo le entregues el timón de tu alma.» (Epicteto)"
                SoltarFramework.CATOLICO -> "«Tu valor no depende de la aprobación humana; tu dignidad está sostenida en un propósito mayor.»"
                SoltarFramework.PSICOLOGIA_MODERNA -> "«Querer a alguien es un deseo hermoso; necesitarlo para calmar tu propia angustia es una señal de que necesitas regularte tú primero.» (Silvia Congost)"
            }

            return SoltarAiResponse(
                replyText = """
**Diferenciemos el afecto de la dependencia.**

Sentir que "no puedes vivir sin esa persona" es la forma en que tu sistema nervioso interpreta el síndrome de abstinencia afectiva tras el corte del vínculo.

Revisemos esta distinción nuclear:
• **Querer:** Desear compartir la vida desde la propia plenitud.
• **Necesitar para regularte:** Usar la presencia o el mensaje del otro como único ansiolítico frente a la soledad o el vacío.

$reflection

**Límite protector para hoy:**
Elige un micro-límite que puedas cumplir durante las próximas horas: no revisar su perfil, silenciar sus notificaciones o posponer cualquier intento de contacto hasta mañana.
                """.trimIndent(),
                isRuminationDetected = false,
                stateDetected = "COMPRENDER",
                suggestedAction = "Establecer un micro-límite de protección en el día de hoy"
            )
        }

        // 3. Pensamientos absolutistas y defusión cognitiva (ACT)
        if (lower.contains("nunca voy a") || lower.contains("siempre voy a") || lower.contains("nadie más me va a") || lower.contains("imposible olvidar") || lower.contains("arruiné mi vida")) {
            val reflection = when (framework) {
                SoltarFramework.ESTOICO -> "«No son los acontecimientos los que nos perturban, sino los juicios definitivos que nos formamos de ellos.» (Epicteto)"
                SoltarFramework.CATOLICO -> "«La esperanza no defrauda; este momento difícil no es el capítulo final de tu historia.»"
                SoltarFramework.PSICOLOGIA_MODERNA -> "«Date cuenta de que estás teniendo el pensamiento de que 'nunca saldrás de esto'. Un pensamiento es solo un evento mental, no una sentencia sobre tu futuro.» (Terapia ACT)"
            }

            return SoltarAiResponse(
                replyText = """
**Práctica de defusión cognitiva.**

Tu mente está asustada y utiliza palabras absolutas (*"nunca"*, *"siempre"*, *"nadie"*) para intentar protegerte del dolor.

Hagamos una pausa de observación:
1. **Nota el pensamiento:** "Mi mente está produciendo el pensamiento de que esto durará para siempre".
2. **Separa el hecho:** Estás atravesando un duelo hoy; el futuro aún no está escrito.
3. **Conecta con tus valores:** ¿Qué cualidad personal (dignidad, cuidado, paciencia) quieres cultivar hoy a pesar de ese pensamiento?

$reflection
                """.trimIndent(),
                isRuminationDetected = false,
                stateDetected = "ACEPTAR",
                suggestedAction = "Realizar el ejercicio de Anclaje al Presente (5-4-3-2-1)"
            )
        }

        // 4. Impulsos de contacto (Bowlby + Estoicismo)
        if (lower.contains("impulso") || lower.contains("escribir") || lower.contains("llamar") || lower.contains("contactar") || lower.contains("buscarlo") || lower.contains("buscarla")) {
            val frameworkSpecificText = when (framework) {
                SoltarFramework.ESTOICO -> "El impulso no depende de ti; lo que haces con él, sí. Pregúntate: ¿esta acción está bajo mi control o busca controlar algo que no lo está?"
                SoltarFramework.CATOLICO -> "Es humano sentir esa necesidad de volver atrás. Puedes reconocerla sin actuar sobre ella, y confiar en que este momento de espera también tiene un sentido en tu proceso."
                SoltarFramework.PSICOLOGIA_MODERNA -> "Sentir ese impulso es una respuesta biológica normal de tu sistema de apego (búsqueda de proximidad ante la pérdida). No es una orden que debas obedecer — es información sobre lo que tu cuerpo está procesando."
            }

            return SoltarAiResponse(
                replyText = """
**El impulso no es una orden.**

$frameworkSpecificText

Pregúntate con honestidad antes de mover las manos:
1. **¿Qué esperas conseguir?** (¿Alivio momentáneo o una respuesta que no cambiará la realidad de fondo?).
2. **¿Qué depende de ti hoy?** (Tu propia conducta y tu dignidad).
3. **¿Qué no puedes saber?** (Cómo reaccionará la otra persona).

Puedes experimentar el impulso sin obedecerlo. Activa el temporizador de **MODO IMPULSO** para acompañar la ola durante 20 minutos.
                """.trimIndent(),
                isRuminationDetected = false,
                stateDetected = "REGULAR",
                suggestedAction = "Iniciar temporizador de 20 minutos en Modo Impulso"
            )
        }

        // 5. Nostalgia, duelo e idealización (Gabriel Rolón + Dual Process)
        if (lower.contains("extraño") || lower.contains("nostalgia") || lower.contains("idealiz") || lower.contains("recuerdo") || lower.contains("echo de menos")) {
            val reflection = when (framework) {
                SoltarFramework.ESTOICO -> "«A menudo sufrimos más por lo que imaginamos que por lo que realmente fue.» (Séneca)"
                SoltarFramework.CATOLICO -> "«Hay un tiempo para abrazar y un tiempo para abstenerse; acepta la temporada en que estás con paz.» (Eclesiastés 3)"
                SoltarFramework.PSICOLOGIA_MODERNA -> "«El duelo no se apura ni se esquiva; es la travesía necesaria para aceptar que lo que fue ya no es, y abrir espacio a lo nuevo.» (Gabriel Rolón)"
            }
            return SoltarAiResponse(
                replyText = """
**La nostalgia tiende a borrar el contexto.**

Es completamente legítimo extrañar momentos cálidos o la sensación de compañía. Sin embargo, recuerda:
• **Extrañar no significa que la relación fuera viable o sana en su totalidad.**
• **El dolor que sientes es el costo del duelo por un vínculo real, no una señal de que debas volver a insistir.**
• **El proceso oscila:** Habrá días de más nostalgia y días de más serenidad; esto no significa que hayas retrocedido.

$reflection
                """.trimIndent(),
                isRuminationDetected = false,
                stateDetected = "ACEPTAR",
                suggestedAction = "Revisar la sección 'Lo que extraño vs Lo que realmente viví'"
            )
        }

        // 6. Respuesta por defecto orientada a la reconstrucción
        val frameworkClosing = when (framework) {
            SoltarFramework.ESTOICO -> "Recuerda: tu ciudadela interior permanece intacta. ¿Qué paso concreto depende de ti hoy?"
            SoltarFramework.CATOLICO -> "Camina con paciencia en este proceso. Tu vida tiene un propósito que va más allá de esta temporada."
            SoltarFramework.PSICOLOGIA_MODERNA -> "Un paso a la vez: regular el cuerpo, comprender la emoción y construir tu autonomía."
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

$frameworkClosing
            """.trimIndent(),
            isRuminationDetected = false,
            stateDetected = "RECONSTRUIR",
            suggestedAction = "Completar el check-in diario en la pestaña HOY"
        )
    }
}


