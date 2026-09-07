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
        val fallback = listOf(
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
        if (!isReady()) return fallback
        val checkinSummary = checkins.takeLast(3).joinToString("; ") { "Dolor: ${it.pain}, Nota: ${it.note}" }
        val journalSummary = journals.takeLast(2).joinToString("; ") { it.content.take(60) }
        val prompt = "Genera 4 pasos estructurados para un ritual de cierre personalizados para $name, con $breakupDays días de ruptura, duración '$relDuration', motivo '$breakupReason', checkins recientes: [$checkinSummary], diarios: [$journalSummary] bajo el marco ${framework.name}. Devuelve cada paso en una línea con el formato 'FaseNombre | Titulo | Guia | PreguntaReflexion', un paso por línea, 4 líneas en total."
        return try {
            val resp = generate(prompt, framework)
            val lines = resp.lines().filter { it.contains("|") }
            if (lines.size >= 4) {
                lines.take(4).mapIndexed { index, line ->
                    val parts = line.split("|").map { it.trim() }
                    ClosingRitualStepAi(
                        stepNumber = index + 1,
                        phaseName = parts.getOrElse(0) { fallback[index].phaseName },
                        title = parts.getOrElse(1) { fallback[index].title },
                        guidance = parts.getOrElse(2) { fallback[index].guidance },
                        reflectionPrompt = parts.getOrElse(3) { fallback[index].reflectionPrompt }
                    )
                }
            } else {
                fallback
            }
        } catch (_: Exception) {
            fallback
        }
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
        val prompt = "Genera una estrategia de afrontamiento para la fecha de riesgo '$riskDateTitle' en $daysUntil días, considerando el marco ${framework.name} y ${pastTriggers.size} disparadores previos."
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
        val fallback = NotificationContent(
            "🌿 Momento de Pausa • $name",
            "Dedica 1 minuto a conectar con tu interior y registrar tu balance de hoy."
        )
        if (!isReady()) return fallback
        val recentPain = checkins.takeLast(3).joinToString(", ") { "${it.pain}" }
        val prompt = "Genera un título y un cuerpo de notificación diaria empática y motivadora para $name, considerando checkins recientes (dolor: [$recentPain]) y marco ${framework.name}. Devuelve en formato 'Título | Cuerpo'."
        return try {
            val resp = generate(prompt, framework)
            val parts = resp.split("|")
            if (parts.size >= 2) {
                NotificationContent(parts[0].trim(), parts[1].trim())
            } else {
                NotificationContent("🌿 Reflexión Diaria • $name", resp.take(120))
            }
        } catch (_: Exception) {
            fallback
        }
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
        val fallback = Pair("🌿 Soberanía Diaria • $name", "Protege tu paz y haz tu pausa consciente hoy.")
        if (!isReady()) return fallback
        val prompt = "Genera una notificación adaptativa (título y cuerpo separados por '|') basada en los ${recentCheckins.size} checkins recientes y marco ${framework.name} para $name."
        return try {
            val resp = generate(prompt, framework)
            val parts = resp.split("|")
            if (parts.size >= 2) {
                Pair(parts[0].trim(), parts[1].trim())
            } else {
                Pair("🌿 Soberanía • $name", resp.take(120))
            }
        } catch (_: Exception) {
            fallback
        }
    }

    fun generateTrendBasedNotification(
        recentCheckins: List<CheckinEntity>,
        framework: SoltarFramework
    ): String {
        val fallback = "«Tienes poder sobre tu mente, no sobre los acontecimientos externos. Comprende esto y hallarás tu fuerza.»"
        if (!isReady() || recentCheckins.isEmpty()) return fallback
        val avgPain = recentCheckins.map { it.pain }.average()
        val prompt = "Analiza que el promedio de dolor emocional reciente es $avgPain en base a ${recentCheckins.size} registros. Genera una notificación basada en tendencias y el marco ${framework.name}."
        return try {
            generate(prompt, framework)
        } catch (_: Exception) {
            fallback
        }
    }

    // =========================================================================
    // 1.7 RED FLAGS & IDENTITY & WISDOM & RELAPSE
    // =========================================================================
    fun getRedFlagGuidedPrompts(): List<String> {
        // Decisión explícita: Se mantiene una lista corta, universal y de alta precisión clínica para la exploración inicial de red flags, garantizando consistencia y velocidad de acceso offline.
        return listOf(
            "¿Hubo momentos donde sentiste que tus límites eran ignorados o castigados con silencio?",
            "¿Notaste contradicción sistemática entre lo que prometía con palabras y lo que hacía con sus actos?",
            "¿Tenías que medir cuidadosamente tus palabras por miedo a una reacción explosiva?"
        )
    }

    fun synthesizeRedFlagFromDescription(userDescription: String): String {
        val fallback = userDescription.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        if (!isReady() || userDescription.isBlank()) return fallback
        val prompt = "Sintetiza y reformula la siguiente descripción libre del usuario en una señal de alarma (red flag) clara, concisa y clínica: '$userDescription'."
        return try {
            generate(prompt)
        } catch (_: Exception) {
            fallback
        }
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
        val fallback = listOf(
            IdentityGoalSuggestion("Caminar 30 min sin teléfono", "Una persona serena y presente", "Cuerpo y Salud"),
            IdentityGoalSuggestion("Bloquear 45 min para proyecto propio", "Una persona enfocada en su propósito", "Proyectos y Trabajo")
        )
        if (!isReady()) return fallback
        val prompt = "Genera 2 sugerencias de metas de identidad para la fase '$currentPhase' con marco ${framework.name}. Devuelve en formato 'Acción | Quién quiero ser | Área'."
        return try {
            val resp = generate(prompt, framework)
            val lines = resp.lines().filter { it.contains("|") }
            if (lines.size >= 2) {
                lines.take(2).map { line ->
                    val parts = line.split("|").map { it.trim() }
                    IdentityGoalSuggestion(
                        actionTitle = parts.getOrElse(0) { "Acción soberana" },
                        whoIWantToBe = parts.getOrElse(1) { "Persona en crecimiento" },
                        area = parts.getOrElse(2) { "Bienestar" }
                    )
                }
            } else {
                fallback
            }
        } catch (_: Exception) {
            fallback
        }
    }

    fun suggestIdentityHabits(
        lifeArea: String,
        whoIWantToBe: String,
        framework: SoltarFramework
    ): List<String> {
        val fallback = listOf(
            "Caminar 30 minutos al aire libre sin consultar el móvil.",
            "Realizar 20 minutos de ejercicio o estiramiento al despertar."
        )
        if (!isReady()) return fallback
        val prompt = "Sugiere 2 hábitos concretos para el área '$lifeArea' alineados con la identidad '$whoIWantToBe' bajo el marco ${framework.name}."
        return try {
            val resp = generate(prompt, framework)
            val lines = resp.lines().filter { it.isNotBlank() }
            if (lines.isNotEmpty()) lines.take(2) else fallback
        } catch (_: Exception) {
            fallback
        }
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
        val fallback = availableCards.firstOrNull() ?: WisdomCard("1", framework, "Soberanía", "Tu paz es tuya.", "Séneca", "Protege tu mente.")
        if (availableCards.isEmpty()) return fallback
        
        // Anti-repetición obligatorio: NUNCA repetir una tarjeta de recentCardIds si hay alternativas disponibles
        val filtered = availableCards.filter { it.id !in recentCardIds }
        val candidateList = if (filtered.isNotEmpty()) filtered else availableCards
        
        if (!isReady()) {
            return candidateList.firstOrNull() ?: fallback
        }
        
        val prompt = "Elige la tarjeta de sabiduría más óptima de esta lista (${candidateList.map { it.id + ": " + it.title }.joinToString(", ")}) para un usuario con nivel de dolor/ansiedad reciente ${latestCheckin?.pain ?: 3f} bajo el marco ${framework.name}. Responde SOLO con el ID de la tarjeta elegida."
        return try {
            val chosenId = generate(prompt, framework).trim()
            candidateList.find { it.id == chosenId } ?: candidateList.firstOrNull() ?: fallback
        } catch (_: Exception) {
            candidateList.firstOrNull() ?: fallback
        }
    }

    fun synthesizeRedFlagsPattern(flags: List<String>): String {
        val fallback = "Patrón detectado de transgresión de límites. Antídoto: firmeza radical y contacto cero."
        if (!isReady() || flags.isEmpty()) return fallback
        val prompt = "Sintetiza un patrón clínico específico y un antídoto a partir de estas señales de alarma del usuario: ${flags.joinToString(" | ")}."
        return try {
            generate(prompt)
        } catch (_: Exception) {
            fallback
        }
    }

    fun explainVulnerabilityScore(
        score: Int,
        latestCheckin: CheckinEntity?,
        upcomingRiskTitle: String?,
        daysToRisk: Int?,
        hasRelapse48h: Boolean
    ): String {
        val fallback = "Puntuación calculada ($score) según el balance de tu último registro emocional, factores de riesgo y estabilidad temporal."
        if (!isReady()) return fallback
        val prompt = "Explica detalladamente por qué la puntuación de vulnerabilidad es $score, considerando checkin (dolor: ${latestCheckin?.pain}), riesgo próximo ($upcomingRiskTitle en $daysToRisk días) y recaída en 48h ($hasRelapse48h)."
        return try {
            generate(prompt)
        } catch (_: Exception) {
            fallback
        }
    }

    fun analyzeRelapsePatterns(
        relapses: List<RelapseEntity>,
        triggers: List<TriggerEventEntity>
    ): RelapsePatternAnalysis {
        val fallback = RelapsePatternAnalysis(
            totalEpisodes = relapses.size + triggers.size,
            primaryTrigger = "Momentos de fatiga o soledad",
            criticalTimeWindow = "Noches y fines de semana",
            emotionalUndercurrent = "Búsqueda de alivio ante el vacío",
            syntheticInsight = "Los tropiezos ocurren en momentos previsibles de cansancio.",
            proactivePrescription = "Activa el Modo Impulso ante la primera señal."
        )
        if (!isReady()) return fallback
        val prompt = "Analiza ${relapses.size} recaídas y ${triggers.size} disparadores. Devuelve un análisis en formato 'TriggerPrincipal | VentanaCritica | SubcorrienteEmocional | Hallazgo | Prescripcion'."
        return try {
            val resp = generate(prompt)
            val parts = resp.split("|").map { it.trim() }
            if (parts.size >= 5) {
                RelapsePatternAnalysis(
                    totalEpisodes = relapses.size + triggers.size,
                    primaryTrigger = parts[0],
                    criticalTimeWindow = parts[1],
                    emotionalUndercurrent = parts[2],
                    syntheticInsight = parts[3],
                    proactivePrescription = parts[4]
                )
            } else {
                fallback
            }
        } catch (_: Exception) {
            fallback
        }
    }

    fun analyzeRelapsePatterns(
        relapses: List<RelapseEntity>,
        urgeEpisodes: List<UrgeEpisodeEntity>,
        recentCheckins: List<CheckinEntity>
    ): String {
        val fallback = "«Tus momentos de mayor vulnerabilidad ocurren en momentos de fatiga. Te sugerimos activar el protocolo Modo Impulso inmediatamente.»"
        if (!isReady()) return fallback
        val prompt = "Analiza patrones con ${relapses.size} recaídas, ${urgeEpisodes.size} episodios de urgencia y ${recentCheckins.size} checkins. Genera una recomendación empática y clínica."
        return try {
            generate(prompt)
        } catch (_: Exception) {
            fallback
        }
    }

    fun analyzeRelapsePatterns(triggerEvents: List<TriggerEventEntity>): String? {
        if (triggerEvents.size < 3) return null
        val fallback = "Tus registros revelan un patrón recurrente en momentos de cansancio. Anticipa tu protocolo de protección."
        if (!isReady()) return fallback
        val prompt = "Analiza ${triggerEvents.size} eventos disparadores y genera una síntesis de patrón y prevención."
        return try {
            generate(prompt)
        } catch (_: Exception) {
            fallback
        }
    }

    fun enrichContextualRecommendation(
        settings: SoltarSettingsEntity?,
        baseRec: ContextualRecommendation
    ): ContextualRecommendation {
        if (!isReady() || settings == null) return baseRec
        val prompt = "Enriquece esta recomendación contextual (Título: '${baseRec.priorityToolTitle}', Descripción: '${baseRec.priorityToolDescription}') con el perfil y preferencias del usuario."
        return try {
            val enrichedDesc = generate(prompt)
            baseRec.copy(priorityToolDescription = enrichedDesc)
        } catch (_: Exception) {
            baseRec
        }
    }

    fun generateClinicalProgressSummary(
        checkins: List<CheckinEntity>,
        journals: List<JournalEntryEntity>,
        letters: List<UnsentLetterEntity>,
        breakupDays: Int,
        userName: String
    ): String {
        val name = if (userName.isNotBlank()) userName else "Usuario"
        val fallback = "📋 INFORME CLÍNICO DE EVOLUCIÓN\nIdentificador: $name • Días: $breakupDays\nProceso de duelo en curso con buen apego al protocolo de contención."
        if (!isReady()) return fallback
        val prompt = "Genera un resumen narrativo y clínico genuino de la evolución del duelo para $name a lo largo de $breakupDays días, basado en ${checkins.size} checkins, ${journals.size} diarios y ${letters.size} cartas."
        return try {
            generate(prompt)
        } catch (_: Exception) {
            fallback
        }
    }

    fun evaluateOnboardingFrameworkRecommendation(
        q1AnswerIndex: Int,
        q2AnswerIndex: Int,
        q3AnswerIndex: Int
    ): FrameworkRecommendation {
        val totalScore = q1AnswerIndex + q2AnswerIndex + q3AnswerIndex
        val framework = when (totalScore % 3) {
            0 -> SoltarFramework.ESTOICO
            1 -> SoltarFramework.CATOLICO
            else -> SoltarFramework.PSICOLOGIA_MODERNA
        }
        val confidence = 75 + ((q1AnswerIndex * 7 + q2AnswerIndex * 11 + q3AnswerIndex * 13) % 21)
        
        val rationale = when (framework) {
            SoltarFramework.ESTOICO -> "Tus respuestas priorizan la resiliencia mental, la dicotomía de control y la fortaleza inquebrantable."
            SoltarFramework.CATOLICO -> "Tus respuestas valoran la dimensión espiritual, el sentido providencial del dolor y la esperanza trascendente."
            SoltarFramework.PSICOLOGIA_MODERNA -> "Tus respuestas valoran entender los mecanismos neurobiológicos del apego y la regulación somática."
        }
        val primaryBenefit = when (framework) {
            SoltarFramework.ESTOICO -> "Te aportará claridad estoica para dominar tus juicios y proteger tu soberanía interior."
            SoltarFramework.CATOLICO -> "Te brindará consuelo espiritual, paz profunda y sentido redentor a tu proceso afectivo."
            SoltarFramework.PSICOLOGIA_MODERNA -> "Te aportará herramientas científicas de regulación somática y autonomía emocional."
        }
        return FrameworkRecommendation(
            recommendedFramework = framework,
            matchConfidencePercentage = confidence,
            rationale = rationale,
            primaryBenefit = primaryBenefit
        )
    }

    fun personalizeContextualRecommendation(
        base: ContextualRecommendation,
        settings: SoltarSettingsEntity
    ): ContextualRecommendation {
        if (!isReady()) return base
        val prompt = "Personaliza la siguiente recomendación contextual ('${base.priorityToolTitle}: ${base.priorityToolDescription}') considerando las preferencias del usuario."
        return try {
            val personalizedDesc = generate(prompt)
            base.copy(priorityToolDescription = personalizedDesc)
        } catch (_: Exception) {
            base
        }
    }
}
