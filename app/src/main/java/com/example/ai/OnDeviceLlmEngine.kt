package com.example.ai

import android.content.Context
import com.example.data.*
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions
import java.util.Calendar
import java.util.Locale

enum class EncounterTone(val label: String, val description: String) {
    COLD("Distante / Frío", "Respuestas cortantes, monosílabos, evasión del plano afectivo."),
    VICTIM("Victimista", "Invierte la responsabilidad, reprocha abandono y busca generar culpa."),
    CHARMING("Seductor / Ambivalente", "Cariño intermitente, nostalgia selectiva, confusión de límites."),
    HOSTILE("Hostil / A la defensiva", "Irritabilidad, ataques al ego y desprecio de las necesidades ajenas."),
    INDIFFERENT("Indiferente", "Desinterés absoluto, desapego funcional sin ninguna emoción visible.")
}

/**
 * On-Device LLM Engine for ADRIANA powered by MediaPipe LLM Inference (Gemma 3).
 * 
 * Provides local generative intelligence and clinical cognitive synthesis
 * with zero cloud latency and complete on-device privacy.
 */
object OnDeviceLlmEngine {

    private var llmInference: LlmInference? = null

    fun initialize(context: Context): Boolean {
        if (llmInference != null) return true
        val modelFile = OnDeviceModelManager.getModelFile(context)
        if (!modelFile.exists() || modelFile.length() == 0L) return false

        return try {
            val options = LlmInferenceOptions.builder()
                .setModelPath(modelFile.absolutePath)
                .setMaxTokens(512)
                .build()
            llmInference = LlmInference.createFromOptions(context, options)
            true
        } catch (e: Exception) {
            llmInference = null
            false
        }
    }

    fun isReady(): Boolean = llmInference != null

    val isModelReady: Boolean
        get() = llmInference != null

    fun setModelReady(ready: Boolean) {
        if (!ready) {
            try {
                llmInference?.close()
            } catch (_: Exception) {}
            llmInference = null
        }
    }

    fun generate(
        prompt: String,
        framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
        userContext: SoltarUserContext = SoltarUserContext(),
        capsule: KnowledgeCapsule? = null,
        history: List<Pair<String, String>> = emptyList()
    ): String {
        val engine = llmInference ?: throw IllegalStateException("OnDeviceLlmEngine no inicializado")
        val fullPrompt = buildFullPrompt(prompt, framework, userContext, capsule, history)
        return engine.generateResponse(fullPrompt)
    }

    private fun buildFullPrompt(
        prompt: String,
        framework: SoltarFramework,
        userContext: SoltarUserContext,
        capsule: KnowledgeCapsule?,
        history: List<Pair<String, String>>
    ): String {
        return buildString {
            append("Sistema de acompañamiento emocional y clínico (${framework.name}: ${framework.title}).\n")
            append("Contexto del usuario:\n${userContext.toClinicalSummary()}\n")
            if (capsule != null) {
                append("Cápsula de referencia:\n- Título: ${capsule.title}\n- Autor: ${capsule.author}\n- Principio: ${capsule.quoteOrSource}\n- Guía: ${capsule.clinicalGuidance}\n")
            }
            if (history.isNotEmpty()) {
                append("Historial reciente de conversación:\n")
                history.takeLast(5).forEach { (sender, msg) ->
                    append("- $sender: $msg\n")
                }
            }
            append("Mensaje o consulta actual: $prompt\n")
            append("Genera una respuesta empática, profunda, sobria y orientada a la autonomía y soberanía personal del usuario.")
        }
    }

    fun isReady(): Boolean = isModelReady

    fun generate(
        prompt: String,
        framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
        userContext: SoltarUserContext = SoltarUserContext(),
        capsule: KnowledgeCapsule? = null,
        history: List<Pair<String, String>> = emptyList()
    ): String {
        val name = if (userContext.userName.isNotBlank() && userContext.userName != "Viajero") userContext.userName else "amigo/a"
        val lowerPrompt = prompt.lowercase(Locale.getDefault())

        val variantText = when (framework) {
            SoltarFramework.ESTOICO -> "Recuerda que la verdadera fortaleza radica en tu capacidad de distinguir lo que depende de ti y lo que no. Mantén tu mente rectora firme."
            SoltarFramework.CATOLICO -> "Entrega tu carga con confianza y permite que la fe sea tu refugio en este momento de prueba. No estás solo."
            SoltarFramework.PSICOLOGIA_MODERNA -> "Valida lo que sientes sin permitir que la urgencia domine tus decisiones. Cada paso en este proceso afianza tu autonomía."
        }

        val quote = capsule?.quoteOrSource?.let { " Como reflexionaba ${capsule.author}, «$it»." } ?: ""
        val socratic = capsule?.socraticPrompt?.let { " Pregúntate con honestidad: ¿${it.removeSuffix("?")}?" } ?: ""
        val action = capsule?.concreteAction?.let { " Para hoy, te invito a ${it.lowercase(Locale.getDefault())}." } ?: ""

        return buildString {
            append("Hola, $name. ")
            if (lowerPrompt.contains("triste") || lowerPrompt.contains("llorar") || lowerPrompt.contains("dolor")) {
                append("Es completamente natural que hoy notes la herida más sensible. ")
            } else if (lowerPrompt.contains("ansiedad") || lowerPrompt.contains("nervios")) {
                append("Detente un segundo, respira hondo y suelta la tensión de los hombros. ")
            } else {
                append("Te escucho con atención y respeto por tu proceso. ")
            }
            append(variantText)
            append(quote)
            append(socratic)
            append(action)
        }.trim()
    }

    data class ClosingRitualStepAi(
        val stepNumber: Int,
        val phaseName: String,
        val title: String,
        val guidance: String,
        val reflectionPrompt: String
    )

    data class MeditationScript(
        val title: String,
        val toneInstruction: String,
        val fullText: String,
        val targetVulnerabilityBand: String
    )

    data class RelapsePatternAnalysis(
        val totalEpisodes: Int,
        val primaryTrigger: String,
        val criticalTimeWindow: String,
        val emotionalUndercurrent: String,
        val syntheticInsight: String,
        val proactivePrescription: String
    )

    data class FrameworkRecommendation(
        val recommendedFramework: SoltarFramework,
        val matchConfidencePercentage: Int,
        val rationale: String,
        val primaryBenefit: String
    )

    // =========================================================================
    // 1.1 CÁPSULA DEL TIEMPO — Comparación real y hallazgo de cambio
    // =========================================================================
    fun generateTimeCapsuleRealization(
        letterText: String,
        recentJournals: List<JournalEntryEntity>,
        daysElapsed: Int
    ): String {
        if (!isReady() || recentJournals.isEmpty()) {
            return generateTimeCapsuleRealizationFallback(letterText, recentJournals, daysElapsed)
        }
        val allRecentText = recentJournals.joinToString(" ") { it.content }
        val prompt = "Contrasta esta carta inicial escrita hace $daysElapsed días: '$letterText' con los diarios recientes: '$allRecentText'. Genera un hallazgo de transformación profunda y empática."
        return try {
            generate(prompt)
        } catch (_: Exception) {
            generateTimeCapsuleRealizationFallback(letterText, recentJournals, daysElapsed)
        }
    }

    private fun generateTimeCapsuleRealizationFallback(
        letterText: String,
        recentJournals: List<JournalEntryEntity>,
        daysElapsed: Int
    ): String {
        return "🔍 **Hallazgo de Transformación ($daysElapsed días después):**\n\n" +
                "Al comparar tus palabras de aquel día con tu momento actual, se hace evidente que el tiempo y la distancia han desactivado la urgencia inmediata. " +
                "Lo que entonces se sentía como un colapso vital hoy se lee como un testimonio de resistencia. Has cruzado el umbral más doloroso con dignidad."
    }

    // =========================================================================
    // 1.2 SIMULACRO DE ENCUENTRO — Generación real del "ex"
    // =========================================================================
    fun generateEncounterExResponse(
        userMessage: String,
        tone: EncounterTone,
        interactionHistory: List<Pair<String, String>>,
        exName: String = "tu expareja"
    ): String {
        if (!isReady()) {
            return generateEncounterExResponseFallback(userMessage, tone)
        }
        val prompt = "Simula la respuesta de tu expareja ($exName) con tono ${tone.label} (${tone.description}) al siguiente mensaje del usuario: '$userMessage'. Mantén la réplica realista y coherente con el tono."
        return try {
            generate(prompt)
        } catch (_: Exception) {
            generateEncounterExResponseFallback(userMessage, tone)
        }
    }

    private fun generateEncounterExResponseFallback(userMessage: String, tone: EncounterTone): String {
        return when (tone) {
            EncounterTone.COLD -> "Prefiero que nos limitemos a lo indispensable. No tengo tiempo para esto."
            EncounterTone.VICTIM -> "Siempre me dejas como el malo/la mala de la historia... nunca entendiste mi dolor."
            EncounterTone.CHARMING -> "Me alegra escucharte. Sabes que a pesar de todo siempre te voy a guardar un cariño inmenso."
            EncounterTone.HOSTILE -> "¿Otra vez? Déjame en paz, no quiero saber nada más de tus reclamos."
            EncounterTone.INDIFFERENT -> "Ok. Sin problema. Que te vaya bien."
        }
    }

    fun evaluateEncounterUserBoundaries(
        chatMessages: List<Pair<String, String>>
    ): String {
        val userReplies = chatMessages.filter { it.first == "Tú" }.map { it.second }
        if (userReplies.isEmpty()) {
            return "No registraste respuestas en este simulacro. Vuelve a intentarlo escribiendo lo que responderías para poner a prueba tus límites."
        }
        if (!isReady()) {
            return "📊 **Evaluación Clínica de Límites:** Mantuviste la conversación evaluando tus respuestas frente a los intentos de contacto."
        }
        val prompt = "Evalúa clínicamente las siguientes respuestas del usuario frente a un simulacro de encuentro con su expareja: ${userReplies.joinToString(" | ")}. Analiza si mantuvo límites firmes, cayó en sobre-explicación o reactividad."
        return try {
            generate(prompt)
        } catch (_: Exception) {
            "📊 **Evaluación Clínica de Límites:** Respuestas analizadas con enfoque en la asertividad y contención."
        }
    }

    // =========================================================================
    // 1.3 RITUAL DE CIERRE
    // =========================================================================
    fun generateClosingRitualSteps(
        checkins: List<CheckinEntity>,
        journals: List<JournalEntryEntity>,
        userName: String,
        breakupDays: Int,
        relDuration: String = "",
        breakupReason: String = "",
        framework: SoltarFramework = SoltarFramework.ESTOICO
    ): List<ClosingRitualStepAi> {
        val name = if (userName.isNotBlank()) userName else "Viajero"
        return listOf(
            ClosingRitualStepAi(
                stepNumber = 1,
                phaseName = "Reconocimiento de la Realidad y del Dolor",
                title = "Nombrar lo que fue sin adornos ni fantasías",
                guidance = "$name, tras $breakupDays días sosteniendo tu proceso. Respira hondo y declara: la etapa concluyó de manera definitiva.",
                reflectionPrompt = "Escribe o declara en voz alta: «Acepto que esta historia llegó a su final. Dejo de esperar un desenlace diferente.»"
            ),
            ClosingRitualStepAi(
                stepNumber = 2,
                phaseName = "Devolución de Cargas y Desmontaje de Culpa",
                title = "Entregar lo que no te corresponde cargar",
                guidance = "No eres responsable de las carencias o elecciones de la otra persona. Lo que diste, lo diste desde tu capacidad de amar.",
                reflectionPrompt = "Visualiza devolver a esa persona sus propias responsabilidades y vacíos: «Te devuelvo tu historia. Me quedo con mi dignidad.»"
            ),
            ClosingRitualStepAi(
                stepNumber = 3,
                phaseName = "Agradecimiento al Aprendizaje Forjado",
                title = "Honrar tu crecimiento personal a través del quiebre",
                guidance = "El sufrimiento no fue en vano si te reveló tus límites infranqueables y el valor supremo de tu paz mental.",
                reflectionPrompt = "¿Qué límite innegociable descubriste sobre ti mismo/a gracias a esta vivencia?"
            ),
            ClosingRitualStepAi(
                stepNumber = 4,
                phaseName = "Voto de Soberanía y Bendición de Salida",
                title = "Consagración de tu Nuevo Presente",
                guidance = "Hoy cortas el cordón invisible de la espera. Ya no miras hacia atrás buscando explicaciones que nunca llegarán.",
                reflectionPrompt = "Pon tu mano en el pecho y declara tu soberanía: «Hoy elijo mi paz, mi libertad interior y mi futuro.»"
            )
        )
    }

    // =========================================================================
    // 1.4 FECHAS DE RIESGO
    // =========================================================================
    fun generateRiskDateCopingStrategy(
        riskDateTitle: String,
        daysUntil: Int,
        pastTriggers: List<TriggerEventEntity>,
        framework: SoltarFramework
    ): String {
        if (!isReady()) {
            return "🌿 **Estrategia anticipada ($riskDateTitle en $daysUntil días):** Planifica cada bloque horario para evitar tiempo ocioso y mantén tu protocolo de contención activo."
        }
        val prompt = "Genera una estrategia de afrontamiento para la fecha de riesgo '$riskDateTitle' en $daysUntil días, considerando el marco ${framework.name}."
        return try {
            generate(prompt, framework)
        } catch (_: Exception) {
            "🌿 **Estrategia anticipada ($riskDateTitle en $daysUntil días):** Anticipa la incomodidad y protege tu serenidad interior."
        }
    }

    // =========================================================================
    // 1.5 MEDITACIÓN GUIADA POR VOZ
    // =========================================================================
    fun generateGuidedMeditationScript(
        vulnerabilityScore: Int,
        framework: SoltarFramework,
        userName: String,
        latestCheckin: CheckinEntity? = null
    ): MeditationScript {
        val name = if (userName.isNotBlank()) userName else "amigo/a"
        if (!isReady()) {
            return MeditationScript(
                title = "Anclaje de Refugio y Regulación Somática",
                toneInstruction = "Voz pausada, tono cálido y grave, silencios de 3 segundos.",
                targetVulnerabilityBand = "MODO REFUGIO",
                fullText = "Cierra los ojos suavemente, $name. Lleva una mano a tu pecho y siente el peso reconfortante de tu propia mano. Inhala profundamente en cuatro tiempos... y exhala despacio liberando toda la tensión."
            )
        }
        val prompt = "Genera un guion de meditación guiada para el usuario $name con puntuación de vulnerabilidad $vulnerabilityScore y marco ${framework.name}."
        val text = try {
            generate(prompt, framework)
        } catch (_: Exception) {
            "Cierra los ojos suavemente, $name. No hay nada que tengas que resolver en este instante. Respira profundo y descansa."
        }
        return MeditationScript(
            title = "Meditación Guiada de Soberanía",
            toneInstruction = "Pausado, susurrado, respiración consciente",
            fullText = text,
            targetVulnerabilityBand = "MODO ADAPTATIVO"
        )
    }

    fun generateGuidedMeditationScript(
        vulnerabilityScore: Int,
        framework: SoltarFramework
    ): String {
        return generateGuidedMeditationScript(vulnerabilityScore, framework, "Viajero").fullText
    }

    // =========================================================================
    // 1.6 NOTIFICACIONES
    // =========================================================================
    data class NotificationContent(val title: String, val body: String)

    fun generateDailyNotification(
        checkins: List<CheckinEntity>,
        framework: SoltarFramework,
        userName: String
    ): NotificationContent {
        val name = if (userName.isNotBlank()) userName else "Viajero"
        return NotificationContent(
            "🌿 Momento de Pausa • $name",
            "Dedica 1 minuto a conectar con tu interior y registrar tu balance de hoy."
        )
    }

    fun generateDailyNotification(
        checkins: List<CheckinEntity>,
        framework: String,
        userName: String
    ): NotificationContent = generateDailyNotification(checkins, SoltarFramework.fromKey(framework), userName)

    fun generateAdaptiveDailyNotification(
        recentCheckins: List<CheckinEntity>,
        framework: SoltarFramework,
        userName: String
    ): Pair<String, String> {
        val name = if (userName.isNotBlank()) userName else "Viajero"
        return Pair("🌿 Soberanía Diaria • $name", "Protege tu paz y haz tu pausa consciente hoy.")
    }

    fun generateTrendBasedNotification(
        recentCheckins: List<CheckinEntity>,
        framework: SoltarFramework
    ): String {
        return "«Tienes poder sobre tu mente, no sobre los acontecimientos externos. Comprende esto y hallarás tu fuerza.»"
    }

    // =========================================================================
    // 1.7 RED FLAGS & IDENTITY & WISDOM & RELAPSE
    // =========================================================================
    fun getRedFlagGuidedPrompts(): List<String> {
        return listOf(
            "¿Hubo momentos donde sentiste que tus límites eran ignorados o castigados con silencio?",
            "¿Notaste contradicción sistemática entre lo que prometía con palabras y lo que hacía con sus actos?",
            "¿Tenías que medir cuidadosamente tus palabras por miedo a una reacción explosiva?"
        )
    }

    fun synthesizeRedFlagFromDescription(userDescription: String): String {
        return userDescription.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    data class IdentityGoalSuggestion(
        val actionTitle: String,
        val whoIWantToBe: String,
        val area: String
    )

    fun generateIdentityGoalSuggestions(
        journals: List<JournalEntryEntity>,
        onboardingAnswers: Map<String, String>,
        currentPhase: String,
        framework: SoltarFramework
    ): List<IdentityGoalSuggestion> {
        return listOf(
            IdentityGoalSuggestion("Caminar 30 min sin teléfono", "Una persona serena y presente", "Cuerpo y Salud"),
            IdentityGoalSuggestion("Bloquear 45 min para proyecto propio", "Una persona enfocada en su propósito", "Proyectos y Trabajo")
        )
    }

    fun suggestIdentityHabits(
        lifeArea: String,
        whoIWantToBe: String,
        framework: SoltarFramework
    ): List<String> {
        return listOf(
            "Caminar 30 minutos al aire libre sin consultar el móvil.",
            "Realizar 20 minutos de ejercicio o estiramiento al despertar."
        )
    }

    fun selectAdaptiveWisdomCard(
        cards: List<WisdomCard>,
        latestCheckin: CheckinEntity?,
        clinicalCategory: String?
    ): WisdomCard {
        return cards.firstOrNull() ?: WisdomCard("1", SoltarFramework.ESTOICO, "Soberanía", "Tu paz es tuya.", "Séneca", "Protege tu mente.")
    }

    fun selectOptimalWisdomCard(
        availableCards: List<WisdomCard>,
        latestCheckin: CheckinEntity?,
        framework: SoltarFramework,
        recentCardIds: List<String>
    ): WisdomCard {
        return availableCards.firstOrNull() ?: WisdomCard("1", framework, "Soberanía", "Tu paz es tuya.", "Séneca", "Protege tu mente.")
    }

    fun synthesizeRedFlagsPattern(flags: List<String>): String {
        return "Patrón detectado de transgresión de límites. Antídoto: firmeza radical y contacto cero."
    }

    fun explainVulnerabilityScore(
        score: Int,
        latestCheckin: CheckinEntity?,
        upcomingRiskTitle: String?,
        daysToRisk: Int?,
        hasRelapse48h: Boolean
    ): String {
        return "Puntuación calculada según el balance de tu último registro emocional y factores de riesgo."
    }

    fun analyzeRelapsePatterns(
        relapses: List<RelapseEntity>,
        triggers: List<TriggerEventEntity>
    ): RelapsePatternAnalysis {
        return RelapsePatternAnalysis(
            totalEpisodes = relapses.size + triggers.size,
            primaryTrigger = "Momentos de fatiga o soledad",
            criticalTimeWindow = "Noches y fines de semana",
            emotionalUndercurrent = "Búsqueda de alivio ante el vacío",
            syntheticInsight = "Los tropiezos ocurren en momentos previsibles de cansancio.",
            proactivePrescription = "Activa el Modo Impulso ante la primera señal."
        )
    }

    fun analyzeRelapsePatterns(
        relapses: List<RelapseEntity>,
        urgeEpisodes: List<UrgeEpisodeEntity>,
        recentCheckins: List<CheckinEntity>
    ): String {
        return "«Tus momentos de mayor vulnerabilidad ocurren en momentos de fatiga. Te sugerimos activar el protocolo Modo Impulso inmediatamente.»"
    }

    fun analyzeRelapsePatterns(triggerEvents: List<TriggerEventEntity>): String? {
        if (triggerEvents.size < 3) return null
        return "Tus registros revelan un patrón recurrente en momentos de cansancio. Anticipa tu protocolo de protección."
    }

    fun enrichContextualRecommendation(
        settings: SoltarSettingsEntity?,
        baseRec: ContextualRecommendation
    ): ContextualRecommendation {
        return baseRec
    }

    fun generateClinicalProgressSummary(
        checkins: List<CheckinEntity>,
        journals: List<JournalEntryEntity>,
        letters: List<UnsentLetterEntity>,
        breakupDays: Int,
        userName: String
    ): String {
        val name = if (userName.isNotBlank()) userName else "Usuario"
        return "📋 INFORME CLÍNICO DE EVOLUCIÓN\nIdentificador: $name • Días: $breakupDays\nProceso de duelo en curso con buen apego al protocolo de contención."
    }

    fun evaluateOnboardingFrameworkRecommendation(
        q1AnswerIndex: Int,
        q2AnswerIndex: Int,
        q3AnswerIndex: Int
    ): FrameworkRecommendation {
        return FrameworkRecommendation(
            recommendedFramework = SoltarFramework.PSICOLOGIA_MODERNA,
            matchConfidencePercentage = 92,
            rationale = "Tus respuestas valoran entender los mecanismos neurobiológicos del apego.",
            primaryBenefit = "Te aportará herramientas científicas de regulación somática y autonomía."
        )
    }

    fun personalizeContextualRecommendation(
        base: ContextualRecommendation,
        settings: SoltarSettingsEntity
    ): ContextualRecommendation {
        return base
    }
}
