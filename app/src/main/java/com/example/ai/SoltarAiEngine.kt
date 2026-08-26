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

    suspend fun generateJournalMentorship(
        journalContent: String,
        moodTag: String = "Reflexión",
        framework: SoltarFramework = SoltarFramework.ESTOICO,
        userContext: SoltarUserContext = SoltarUserContext()
    ): JournalMentorshipResult = withContext(Dispatchers.IO) {
        val cleanInput = journalContent.trim()
        if (cleanInput.isBlank()) {
            return@withContext JournalMentorshipResult(
                feedback = "Para recibir mentoría filosófica, escribe libremente tus pensamientos, dudas o lo que estás sintiendo hoy.",
                corePrinciple = "«El autoconocimiento comienza con la honestidad ante la propia página en blanco.»",
                socraticQuestion = "¿Qué verdad sobre ti mismo estás evitando mirar hoy?",
                concreteAction = "Escribe al menos dos frases sobre lo que realmente sientes en este momento."
            )
        }

        // Intento con Gemini API si la clave está configurada
        val apiKey = try {
            BuildConfig::class.java.getField("GEMINI_API_KEY").get(null) as? String ?: ""
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && !apiKey.contains("PLACEHOLDER", ignoreCase = true)) {
            try {
                val promptText = """
Sos un mentor filosófico y terapeuta reflexivo en la app ADRIANA (filosofía práctica, estoicismo, teoría del apego, TCC y lucidez existencial).
El usuario acaba de escribir la siguiente entrada en su DIARIO PERSONAL:
---
ESTADO/EMOCIÓN: $moodTag
MARCO PREFERIDO: ${framework.name}
ENTRADA DEL DIARIO:
$cleanInput
---

Genera una retroalimentación reflexiva profunda, concisa, lúcida y compasiva en formato JSON estricto con exactamente estas 4 claves:
{
  "feedback": "Análisis reflexivo estructurado en 2 o 3 párrafos breves. Valida la emoción sin alimentar la ilusión ni la rumiación. Ayuda a distinguir hechos de interpretaciones o juicios. Aplica la sabiduría del marco ${framework.name}.",
  "corePrinciple": "Una máxima o principio rector memorable atribuido a un referente clave (Marco Aurelio, Epicteto, Séneca, Viktor Frankl, Gabriel Rolón, Silvia Congost o Proverbios) según el marco.",
  "socraticQuestion": "Una pregunta socrática de autoindagación honesta para que el usuario medite hoy.",
  "concreteAction": "Una micro-acción práctica de soberanía personal o autocuidado para hoy."
}
Responde ÚNICAMENTE con el objeto JSON válido, sin bloques de código extra ni rodeos.
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
                            corePrinciple = parsed.optString("corePrinciple", "«Sé dueño de tus juicios.»"),
                            socraticQuestion = parsed.optString("socraticQuestion", "¿Qué depende de ti en este instante?"),
                            concreteAction = parsed.optString("concreteAction", "Tomar 5 minutos de respiración consciente.")
                        )
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Gemini API failed for journal, falling back to local engine", e)
            }
        }

        // Motor local filosófico y clínico de alta profundidad
        return@withContext executeLocalPhilosophicalMentorship(cleanInput, moodTag, framework, userContext)
    }

    private fun executeLocalPhilosophicalMentorship(
        input: String,
        moodTag: String,
        framework: SoltarFramework,
        userContext: SoltarUserContext
    ): JournalMentorshipResult {
        val lower = input.lowercase()

        val isPainOrLoss = lower.contains("duele") || lower.contains("dolor") || lower.contains("triste") || lower.contains("llor") || lower.contains("falta") || moodTag.contains("Duelo", ignoreCase = true)
        val isAngerOrInjustice = lower.contains("rabia") || lower.contains("bronca") || lower.contains("injusto") || lower.contains("mentira") || lower.contains("engañ") || moodTag.contains("Rabia", ignoreCase = true)
        val isAnxietyOrImpulse = lower.contains("ansiedad") || lower.contains("escribir") || lower.contains("buscar") || lower.contains("redes") || moodTag.contains("Ansiedad", ignoreCase = true) || moodTag.contains("Impulso", ignoreCase = true)
        val isGratitudeOrPeace = lower.contains("gracias") || lower.contains("paz") || lower.contains("calma") || lower.contains("bien") || moodTag.contains("Calma", ignoreCase = true) || moodTag.contains("Gratitud", ignoreCase = true)

        return when (framework) {
            SoltarFramework.ESTOICO -> {
                when {
                    isAnxietyOrImpulse -> JournalMentorshipResult(
                        feedback = """
Examinando tus líneas a la luz de la razón estoica:
El deseo de intervenir o buscar una respuesta ajena nace de concederle a lo exterior un poder sobre tu estado interno. Todo impulso es una primera impresión (phantasia); la sabiduría radica en interponer la razón antes de convertirlo en asentimiento.

No permitas que la incertidumbre derribe tu ciudadela interior. Lo que otra persona piense, haga o responda pertenece al reino de los indiferentes ajenos; tu calma y tu decoro son tu único territorio soberano.
                        """.trimIndent(),
                        corePrinciple = "«No son las cosas las que atormentan a los hombres, sino la opinión que se forman de ellas.» — Epicteto (Enquiridión, V)",
                        socraticQuestion = "¿Estás buscando paz interior o estás intentando forzar un resultado que no está bajo tu control?",
                        concreteAction = "Escribe en un papel las 3 cosas que SÍ dependen de tus manos en las próximas 3 horas y ejecútalas sin mirar atrás."
                    )
                    isPainOrLoss -> JournalMentorshipResult(
                        feedback = """
Tu texto refleja la fricción natural de aceptar la transitoriedad. Para los estoicos, el dolor por la pérdida no se niega ni se finge; se reconoce con dignidad sin sumarle juicios de catástrofe.

Has amado con sinceridad, y el vacío actual es testimonio de lo vivido. Sin embargo, recordar que todo en la naturaleza es prestado te ayuda a honrar el pasado sin hacer de la nostalgia una cadena perpetua.
                        """.trimIndent(),
                        corePrinciple = "«Acepta las cosas a las que el destino te ha atado y ama a las personas con las que te ha tocado vivir, pero hazlo de todo corazón.» — Marco Aurelio (Meditaciones, VI, 39)",
                        socraticQuestion = "¿Qué parte de este sufrimiento nace del hecho real y qué parte nace de tu resistencia a aceptarlo?",
                        concreteAction = "Dedica 10 minutos a una caminata en silencio, observando el entorno con plena presencia sin consultar el teléfono."
                    )
                    isAngerOrInjustice -> JournalMentorshipResult(
                        feedback = """
La indignación en tus palabras es comprensible, pero la ira es una pasión que castiga más a quien la alberga que a quien la provocó. Séneca nos recuerda que molestarse por la falta de rectitud ajena es tan inútil como enojarse con la lluvia por mojar.

Reclamar justicia al pasado no cambia los hechos. El mayor desquite contra una ofensa es no parecerte jamás a quien te dañó y resguardar tu propia nobleza de carácter.
                        """.trimIndent(),
                        corePrinciple = "«La mejor venganza es no ser como tu enemigo.» — Marco Aurelio (Meditaciones, VI, 6)",
                        socraticQuestion = "Si sueltas el rencor hoy, ¿qué espacio vital y mental recuperarías de inmediato?",
                        concreteAction = "Rompe simbólicamente o quema de forma segura una hoja con los reclamos que no tienen solución en el presente."
                    )
                    else -> JournalMentorshipResult(
                        feedback = """
Tu diario refleja un momento de autoobservación valioso. La práctica cotidiana de registrar los pensamientos permite examinarlos con la ecuanimidad de un testigo sereno en lugar de dejarse arrastrar por ellos.

Mantén tu enfoque en cultivar las virtudes cardinales: prudencia para discernir, templanza para gobernar los impulsos, fortaleza para sostener el dolor necesario y justicia para tratarte a ti mismo y a los demás con ecuanimidad.
                        """.trimIndent(),
                        corePrinciple = "«La felicidad de tu vida depende de la calidad de tus pensamientos.» — Marco Aurelio (Meditaciones, III)",
                        socraticQuestion = "¿En qué acción cotidiana puedes demostrar hoy tu mayor nivel de autonomía y respeto propio?",
                        concreteAction = "Completa una tarea pendiente que hayas pospuesto para reafirmar tu disciplina personal."
                    )
                }
            }

            SoltarFramework.PSICOLOGIA_MODERNA -> {
                when {
                    isAnxietyOrImpulse -> JournalMentorshipResult(
                        feedback = """
Leyendo tu escrito desde la psicología del apego y la regulación emocional:
Lo que sientes no es una señal intuitiva de que debas romper la distancia; es la alarma biológica de tu sistema nervioso protestando por el corte del circuito de recompensa y proximidad.

Silvia Congost nos recuerda la importancia de distinguir el cariño de la necesidad de regular la angustia. Poner límites protectores no es frialdad, es el autocuidado indispensable para que tu mente desinflame la hiperactivación.
                        """.trimIndent(),
                        corePrinciple = "«El contacto cero no es para castigar al otro, es el espacio de seguridad que necesitas para sanar tu propia herida.» — Silvia Congost",
                        socraticQuestion = "¿Qué necesitas darte a ti mismo en este momento en lugar de esperar que otra persona calme tu malestar?",
                        concreteAction = "Aplica la técnica de estimulación somática: lava tu rostro con agua fría o realiza 4 ciclos de respiración diafragmática 4-7-8."
                    )
                    isPainOrLoss -> JournalMentorshipResult(
                        feedback = """
Tu entrada expresa la tristeza legítima del duelo vincular. Gabriel Rolón explica con maestría que el duelo no se esquiva ni se acelera mediante distracciones superficiales; es la lenta elaboración donde cada lágrima recoloca la historia en su lugar.

Permítete sentir la melancolía sin concluir que tu vida se ha detenido para siempre. El dolor que sientes hoy es la medida del significado que tuvo, pero no determina el límite de tu futuro.
                        """.trimIndent(),
                        corePrinciple = "«El duelo no se supera olvidando, sino recordando sin que el recuerdo destruya el presente.» — Gabriel Rolón",
                        socraticQuestion = "¿Puedes sostener tu tristeza hoy con la misma ternura y paciencia con la que cuidarías a un buen amigo?",
                        concreteAction = "Prepárate una bebida caliente y descansa 15 minutos sin pantallas ni estímulos invasivos."
                    )
                    else -> JournalMentorshipResult(
                        feedback = """
Escribir con esta apertura es un ejercicio fundamental de defusión cognitiva y elaboración reflexiva (TCC/ACT). Al poner en palabras tus vivencias, logras que los pensamientos dejen de ser verdades absolutas y pasen a ser eventos mentales observables.

Estás transitando un proceso no lineal: habrá días de claridad y días de cansancio. Valora cada paso donde eliges tu bienestar y tu coherencia interna por encima de la inercia del pasado.
                        """.trimIndent(),
                        corePrinciple = "«No necesitas dejar de sentir para empezar a soltar y reconstruirte.»",
                        socraticQuestion = "¿Qué valor nuclear tuyo (dignidad, salud, creatividad, paz) quieres alimentar hoy con tus decisiones?",
                        concreteAction = "Identifica una pequeña meta del día y anótala en tus Metas de Identidad."
                    )
                }
            }

            SoltarFramework.CATOLICO -> {
                when {
                    isAnxietyOrImpulse -> JournalMentorshipResult(
                        feedback = """
En tus palabras se percibe la inquietud del corazón en medio de la prueba. En momentos de incertidumbre o deseos de regresar sobre lo andado, la virtud de la paciencia y el dominio propio son tu mayor escudo.

La custodia del corazón te invita a no exponerte a aquello que perturba tu paz. Confía en que este tiempo de desierto también es un tiempo de maduración y fortalecimiento interior.
                        """.trimIndent(),
                        corePrinciple = "«Por encima de todo lo que guardes, guarda tu corazón, porque de él brota la vida.» — Proverbios 4:23",
                        socraticQuestion = "¿Estás dispuesto a entregar tu necesidad de control para recibir la serenidad que necesitas hoy?",
                        concreteAction = "Haz un momento de silencio reflexivo o pausa de oración pidiendo fortaleza y serenidad."
                    )
                    isPainOrLoss -> JournalMentorshipResult(
                        feedback = """
Tu dolor es digno de respeto y consuelo. En la tradición sapiencial, el sufrimiento nunca es estéril si se atraviesa con esperanza y humildad. 

Aceptar que hay un tiempo para abrazar y un tiempo para despedirse es el camino para encontrar la paz. Tu dignidad está sostenida en un amor más grande y en un propósito que trasciende esta herida temporal.
                        """.trimIndent(),
                        corePrinciple = "«Todo tiene su momento oportuno; hay un tiempo para plantar y un tiempo para cosechar, un tiempo para llorar y un tiempo para sanar.» — Eclesiastés 3:1-4",
                        socraticQuestion = "¿En qué aspecto de tu vida puedes empezar a sembrar hoy nuevas semillas de bien y gratitud?",
                        concreteAction = "Realiza un acto desinteresado de bondad o apoyo hacia un familiar o amigo en el día de hoy."
                    )
                    else -> JournalMentorshipResult(
                        feedback = """
Tu reflexión muestra sinceridad y búsqueda de bien. En el camino de la vida, cada desprendimiento es también una invitación a purificar nuestras intenciones y valorar lo que verdaderamente edifica el alma.

Camina con esperanza. La paz no es la ausencia de dificultades, sino la certeza de que tu vida está llamada a la plenitud y al crecimiento en la verdad.
                        """.trimIndent(),
                        corePrinciple = "«La esperanza no defrauda, porque el amor ha sido derramado en nuestros corazones.» — Romanos 5:5",
                        socraticQuestion = "¿Qué agradecimiento sincero puedes elevar hoy en medio de tus circunstancias?",
                        concreteAction = "Escribe tres motivos de gratitud por dones cotidianos que tienes en tu presente."
                    )
                }
            }
        }
    }
}


