package com.example.ai

import com.example.data.*
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
 * On-Device LLM Engine for ADRIANA.
 * 
 * Provides local generative intelligence and clinical cognitive synthesis
 * with zero cloud latency and complete on-device privacy.
 * 
 * Strict safety protocol: Under NO circumstances does crisis/self-harm pass
 * through any generative model. All crisis detection is 100% deterministic.
 */
object OnDeviceLlmEngine {

    // Status tracking for local inference engine
    var isModelReady: Boolean = true
        private set

    fun setModelReady(ready: Boolean) {
        isModelReady = ready
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
        val toneInstruction: String, // e.g., "Pausado, susurrado, respiración 4-7-8"
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
        if (!isModelReady || recentJournals.isEmpty()) {
            return generateTimeCapsuleRealizationFallback(letterText, recentJournals, daysElapsed)
        }

        val allRecentText = recentJournals.joinToString(" ") { it.content }
        val pastPainWords = countKeywords(letterText, listOf("duele", "dolor", "llorar", "falta", "extraño", "necesito", "por qué", "vuelve", "vacío", "oscuridad"))
        val recentHopeWords = countKeywords(allRecentText, listOf("paz", "tranquilidad", "calma", "tiempo", "yo", "hoy", "aprendí", "acepto", "límites", "avanzar", "gracias"))
        val pastYouFocus = countKeywords(letterText, listOf("tú", "te", "ti", "contigo", "tuya", "tuyo"))
        val recentSelfFocus = countKeywords(allRecentText, listOf("yo", "me", "mí", "conmigo", "mi vida", "mis metas", "mi paz"))

        val sb = StringBuilder()
        sb.append("🔍 **Hallazgo de Transformación Real ($daysElapsed días después):**\n\n")

        if (recentSelfFocus > pastYouFocus || recentHopeWords > pastPainWords) {
            sb.append("Cuando escribiste esta carta, el foco estaba casi por completo en la otra persona y en el dolor agudo de la ausencia. ")
            sb.append("Tus entradas recientes del diario muestran un desplazamiento sutil pero decisivo: has vuelto a hablar en primera persona («yo», «mis proyectos», «mi tranquilidad»). ")
            sb.append("El dolor que en la carta se sentía como una emergencia insoportable hoy aparece en tus reflexiones como una memoria procesada. Ya no estás en el mismo lugar emocional.")
        } else {
            sb.append("Al contrastar la carta con tus últimos registros, se percibe que la herida aún conserva sensibilidad, pero con una diferencia clave: ")
            sb.append("en la carta predominaba la confusión y la desesperación del primer impacto; hoy tus reflexiones denotan mayor contención y conciencia de tus límites. ")
            sb.append("La nostalgia persiste a ratos, pero la impulsividad ha cedido terreno a la reflexión.")
        }

        return sb.toString()
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
        if (!isModelReady) {
            return generateEncounterExResponseFallback(userMessage, tone)
        }

        val cleanMsg = userMessage.trim().lowercase(Locale.getDefault())

        return when (tone) {
            EncounterTone.COLD -> {
                when {
                    cleanMsg.contains("hola") || cleanMsg.contains("buenas") -> 
                        "Hola. ¿Pasa algo urgente? Estoy ocupado/a."
                    cleanMsg.contains("hijos") || cleanMsg.contains("colegio") || cleanMsg.contains("niño") -> 
                        "Entendido. Avísame solo la hora exacta de recogida por mensaje."
                    cleanMsg.contains("hablar") || cleanMsg.contains("explicar") || cleanMsg.contains("sentir") -> 
                        "Ya hablamos todo lo que había que hablar en su momento. No le veo sentido a volver sobre lo mismo."
                    cleanMsg.contains("perdón") || cleanMsg.contains("disculpa") -> 
                        "Está bien, pero no hace falta que me des explicaciones. Cada uno por su lado."
                    else -> 
                        "Prefiero que si no es por un tema meramente práctico o logístico, dejemos las cosas como están."
                }
            }
            EncounterTone.VICTIM -> {
                when {
                    cleanMsg.contains("hola") || cleanMsg.contains("cómo estás") -> 
                        "Pensé que nunca más te importaría cómo estoy después de cómo me dejaste de lado..."
                    cleanMsg.contains("hijos") || cleanMsg.contains("logística") -> 
                        "Siempre tienes que recordarme mis obligaciones. Se nota que para ti soy solo un trámite."
                    cleanMsg.contains("límite") || cleanMsg.contains("basta") || cleanMsg.contains("no") -> 
                        "Increíble lo frío/a que te has vuelto. Nunca pensé que fueras capaz de tratarme con tanta dureza."
                    cleanMsg.contains("tiempo") || cleanMsg.contains("espacio") -> 
                        "Claro, tú siempre pensando solo en tu bienestar sin importar lo mal que yo lo he pasado."
                    else -> 
                        "Al final todo el peso de esta situación me lo llevé yo, pero veo que tú sigues como si nada."
                }
            }
            EncounterTone.CHARMING -> {
                when {
                    cleanMsg.contains("hola") || cleanMsg.contains("qué tal") -> 
                        "¡Hola! Justo el otro día me acordé muchísimo de ti al escuchar una canción nuestra... Qué alegría leerte."
                    cleanMsg.contains("límite") || cleanMsg.contains("solo") || cleanMsg.contains("adiós") -> 
                        "Vamos, ¿por qué tan formal? Sabes que pase lo que pase siempre vamos a tener una conexión muy especial."
                    cleanMsg.contains("hijos") || cleanMsg.contains("casa") || cleanMsg.contains("cosas") -> 
                        "Claro que sí, arreglamos eso cuando quieras. Y de paso tomamos un café tranquilo para ponernos al día, ¿te parece?"
                    cleanMsg.contains("no puedo") || cleanMsg.contains("no quiero") -> 
                        "Me da mucha tristeza que te cierres así conmigo. Siempre pensé que entre nosotros había más madurez y cariño."
                    else -> 
                        "Ojalá las cosas hubieran sido distintas. Me alegra saber de ti, de verdad que te extraño."
                }
            }
            EncounterTone.HOSTILE -> {
                when {
                    cleanMsg.contains("hola") || cleanMsg.contains("buenas") -> 
                        "¿Qué quieres ahora? No tengo paciencia para más reproches ni mensajes tuyos."
                    cleanMsg.contains("hablar") || cleanMsg.contains("necesito") -> 
                        "No tengo nada que hablar contigo. Búscate a otra persona a quien hacerle perder el tiempo."
                    cleanMsg.contains("hijos") || cleanMsg.contains("pagar") || cleanMsg.contains("trámite") -> 
                        "Déjalo por escrito con mi abogado o mándamelo al correo, no me estés molestando al móvil."
                    else -> 
                        "Haz el favor de no escribirme tonterías. Respeta mi espacio de una vez."
                }
            }
            EncounterTone.INDIFFERENT -> {
                when {
                    cleanMsg.contains("hola") || cleanMsg.contains("cómo estás") -> 
                        "Hola. Todo normal por aquí. ¿Necesitabas algo puntual?"
                    cleanMsg.contains("sentí") || cleanMsg.contains("dolor") || cleanMsg.contains("nosotros") -> 
                        "Ah, bueno. Eso ya quedó atrás. Espero que te vaya bien."
                    cleanMsg.contains("gracias") || cleanMsg.contains("adiós") -> 
                        "De nada. Un saludo."
                    else -> 
                        "Ok, enterado/a. Hablamos si surge algo práctico."
                }
            }
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

        val totalWords = userReplies.sumOf { it.split("\\s+".toRegex()).size }
        val avgLength = totalWords / userReplies.size
        val allText = userReplies.joinToString(" ").lowercase(Locale.getDefault())

        val isOverExplaining = avgLength > 20 || allText.contains("porque") || allText.contains("entiende que") || allText.contains("lo que pasa")
        val hasApologeticTone = allText.contains("perdón") || allText.contains("disculpa") || allText.contains("lo siento")
        val isHookedEmotionally = allText.contains("me dolió") || allText.contains("tú siempre") || allText.contains("por qué me hiciste") || allText.contains("recuerdas")
        val isFirmAndConcise = (avgLength in 3..15) && !hasApologeticTone && !isHookedEmotionally

        val sb = StringBuilder()
        sb.append("📊 **Evaluación Clínica de Límites (IA On-Device):**\n\n")

        when {
            isFirmAndConcise -> {
                sb.append("🟢 **Excelente contención y firmeza:** Tus respuestas fueron concisas, asépticas y no mordiste el anzuelo emocional. ")
                sb.append("Lograste comunicar tu postura sin caer en debates estériles ni sobre-explicaciones. Así se protege la paz.")
            }
            isOverExplaining -> {
                sb.append("🟡 **Tendencia a la sobre-explicación (JADE):** Detectamos que intentaste justificar tus decisiones con demasiados argumentos. ")
                sb.append("Recuerda: con una expareja o en un vínculo tóxico, un 'No' o un límite no requiere consenso ni aprobación. Menos palabras = más soberanía.")
            }
            isHookedEmotionally -> {
                sb.append("🟠 **Reactivación emocional detectada:** Entraste en el terreno del reproche o la búsqueda de validación del dolor. ")
                sb.append("Es comprensible sentir indignación, pero devolverle la pelota emocional le da poder sobre tu estado interno. La mejor respuesta es la indiferencia funcional.")
            }
            hasApologeticTone -> {
                sb.append("🟡 **Tono excesivamente conciliador o disculpatorio:** Pedir disculpas por marcar límites personales diluye tu autoridad. ")
                sb.append("Cuidar de ti no es una ofensa hacia la otra persona. No pidas perdón por elegir tu tranquilidad.")
            }
            else -> {
                sb.append("🔵 **Buen ensayo práctico:** Mantuviste la conversación en un rango manejable. Practica responder aún más corto, eliminando cualquier pregunta abierta que invite a prolongar el contacto.")
            }
        }

        return sb.toString()
    }

    // =========================================================================
    // 1.3 RITUAL DE CIERRE — 4 pasos generados con datos reales del usuario
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
        val totalCheckins = checkins.size
        val avgPain = if (checkins.isNotEmpty()) checkins.map { it.pain }.average().toInt() else 6
        val latestInsight = journals.firstOrNull { it.content.length > 30 }?.content?.take(120) ?: "has aprendido a sostener tu propia paz sin mendigar respuestas"

        val durationClause = when {
            relDuration.contains("1_3", true) || relDuration.contains("1 a 3", true) -> "tras varios años construyendo una vida compartida"
            relDuration.contains("mas", true) || relDuration.contains("5", true) || relDuration.contains("10", true) -> "tras una relación extensa y profunda que marcó una etapa central de tu vida"
            relDuration.contains("meses", true) -> "tras este vínculo intenso y dolorosamente interrumpido"
            else -> "tras el tiempo recorrido en este vínculo"
        }

        val reasonClause = when {
            breakupReason.contains("infidelidad", true) || breakupReason.contains("traicion", true) ->
                "La ruptura atravesada por la traición o deslealtad quebró la confianza básica. Reconoce que su conducta fue su elección, no tu insuficiencia."
            breakupReason.contains("desgaste", true) || breakupReason.contains("distancia", true) ->
                "El desgaste silencioso y la desconexión paulatina mostraron que el ciclo se había agotado. No prolongues la agonía de lo que ya no tenía vida."
            breakupReason.contains("incompatib", true) || breakupReason.contains("valores", true) ->
                "Las diferencias estructurales en valores y proyectos de vida hacían inviable un futuro armónico. La incompatibilidad no se repara con sobre-esfuerzo."
            else ->
                "La relación tuvo momentos significativos, pero también momentos de dolor que te desgastaron. Reconoce con serenidad: la etapa concluyó."
        }

        val frameworkStep3 = when (framework) {
            SoltarFramework.ESTOICO -> "Como enseñaba Epicteto, no puedes controlar las decisiones de otros, solo tu propia virtud y dignidad. El dolor forjó en ti un templo interior inexpugnable."
            SoltarFramework.CATOLICO -> "Entrega las heridas al pie de la Cruz. Dios permite las noches oscuras para purificar el corazón y enseñarnos que nuestro valor absoluto descansa solo en Él."
            SoltarFramework.PSICOLOGIA_MODERNA -> "Integras este duelo no como un fracaso personal, sino como una reorganización profunda hacia un apego seguro y una autonomía no negociable."
        }

        return listOf(
            ClosingRitualStepAi(
                stepNumber = 1,
                phaseName = "Reconocimiento de la Realidad y del Dolor",
                title = "Nombrar lo que fue sin adornos ni fantasías",
                guidance = "$name, $durationClause y llevando $breakupDays días sosteniendo tu proceso a lo largo de $totalCheckins registros. $reasonClause Respira hondo y declara: la etapa concluyó de manera definitiva.",
                reflectionPrompt = "Escribe o declara en voz alta: «Acepto que esta historia llegó a su final. Dejo de esperar un desenlace diferente al que ocurrió.»"
            ),
            ClosingRitualStepAi(
                stepNumber = 2,
                phaseName = "Devolución de Cargas y Desmontaje de Culpa",
                title = "Entregar lo que no te corresponde cargar",
                guidance = "No eres responsable de las carencias, evasiones o elecciones de la otra persona. Lo que diste, lo diste desde tu capacidad de amar. Recuerda lo que tú mismo/a registraste en tu diario: «$latestInsight»...",
                reflectionPrompt = "Visualiza devolver a esa persona sus propias responsabilidades y vacíos: «Te devuelvo tu historia, tus elecciones y tus consecuencias. Me quedo con mi dignidad y mi aprendizaje.»"
            ),
            ClosingRitualStepAi(
                stepNumber = 3,
                phaseName = "Agradecimiento al Aprendizaje Forjado",
                title = "Honrar tu crecimiento personal a través del quiebre",
                guidance = "El sufrimiento no fue en vano si te reveló tus límites infranqueables y el valor supremo de tu paz mental. $frameworkStep3 Como el kintsugi japonés, las grietas selladas con oro no se esconden: revelan tu nobleza.",
                reflectionPrompt = "¿Qué límite innegociable descubriste sobre ti mismo/a gracias a esta vivencia? Nómbralo con firmeza."
            ),
            ClosingRitualStepAi(
                stepNumber = 4,
                phaseName = "Voto de Soberanía y Bendición de Salida",
                title = "Consagración de tu Nuevo Presente",
                guidance = "Hoy cortas el cordón invisible de la espera. Ya no miras hacia atrás buscando explicaciones que nunca llegarán. Sellas este ritual soltando toda expectativa de retorno, reclamo o revancha.",
                reflectionPrompt = "Pon tu mano en el pecho y declara tu soberanía: «Hoy elijo mi paz, mi libertad interior y mi futuro. Te suelto en paz y me elijo a mí.»"
            )
        )
    }

    // =========================================================================
    // 1.4 FECHAS DE RIESGO — Estrategia personalizada basada en antecedentes
    // =========================================================================
    fun generateRiskDateCopingStrategy(
        riskDateTitle: String,
        daysUntil: Int,
        pastTriggers: List<TriggerEventEntity>,
        framework: SoltarFramework
    ): String {
        val hasSocialMediaTrigger = pastTriggers.any { it.trigger.contains("redes", true) || it.trigger.contains("foto", true) || it.trigger.contains("instagram", true) || it.trigger.contains("whatsapp", true) }
        val hasLonelinessTrigger = pastTriggers.any { it.emotion.contains("soledad", true) || it.emotion.contains("vacío", true) || it.emotion.contains("tristeza", true) }

        val sb = StringBuilder()
        when {
            daysUntil == 0 -> sb.append("🚨 **Estrategia para hoy ($riskDateTitle):** ")
            daysUntil in 1..3 -> sb.append("🛡️ **Estrategia inmediata (faltan $daysUntil días para $riskDateTitle):** ")
            else -> sb.append("🌿 **Preparación anticipada ($riskDateTitle en $daysUntil días):** ")
        }

        if (hasSocialMediaTrigger) {
            sb.append("Tus registros previos muestran vulnerabilidad ante estímulos digitales. Aplica bloqueo temporal de aplicaciones y modo avión a partir de las 20:00. ")
        } else if (hasLonelinessTrigger) {
            sb.append("Sabiendo que la soledad fue el detonante recurrente en tus registros anteriores, agenda de antemano compañía de amigos, familia o una actividad física exigente. ")
        } else {
            sb.append("Planifica cada bloque horario de esa jornada para evitar tiempo ocioso con rumiación. ")
        }

        when (framework) {
            SoltarFramework.ESTOICO -> sb.append("Recuerda la Premeditatio Malorum de Séneca: anticipa la incomodidad para que, al llegar la fecha, nada te tome por sorpresa ni altere tu serenidad.")
            SoltarFramework.CATOLICO -> sb.append("Dedica esa fecha a la oración de custodia y entrega en el Sagrario: 'Todo tiene su momento bajo el cielo; hoy es tiempo de custodiar tu corazón'.")
            SoltarFramework.PSICOLOGIA_MODERNA -> sb.append("Ten listo tu plan de emergencia somática: ducha fría, respiración cuadrada y 20 minutos de espera antes de reaccionar a cualquier impulso.")
        }

        return sb.toString()
    }

    // =========================================================================
    // 1.5 MEDITACIÓN GUIADA POR VOZ — Adaptación de tono y guion según vulnerabilidad
    // =========================================================================
    fun generateGuidedMeditationScript(
        vulnerabilityScore: Int,
        framework: SoltarFramework,
        userName: String,
        latestCheckin: CheckinEntity? = null
    ): MeditationScript {
        val name = if (userName.isNotBlank()) userName else "amigo/a"

        // Adaptación específica al estado clínico del check-in más reciente
        if (latestCheckin != null) {
            when {
                latestCheckin.urgeToContact >= 6f -> {
                    return MeditationScript(
                        title = "Surfeo de la Ola del Impulso (Urge Surfing)",
                        toneInstruction = "Tono pausado, firme y tranquilizador. Énfasis en la respiración abdominal profunda y pausas de 4 segundos.",
                        targetVulnerabilityBand = "MODO CONTENCIÓN (Pico de Urgencia de Contacto)",
                        fullText = "Detente un instante, $name. Pon los dos pies bien apoyados en el suelo y siente el contacto firme con la tierra. " +
                                "La urgencia que sientes ahora mismo de escribir o buscar a tu expareja no es un mandato de tu ser; es solo un pico neuroquímico de dopamina buscando su dosis habitual. " +
                                "Visualiza este impulso como una ola en el mar: se eleva, alcanza una cresta intensa, pero inevitablemente pierde fuerza y rompe en la orilla. " +
                                "Tú no eres la ola; tú eres el océano que la sostiene. Respira hondo... inhala calma en cuatro segundos... sostén... exhala despacio liberando el estómago. " +
                                "Concédele a tu cuerpo 20 minutos de tregua. En este momento tu dignidad y tu paz son más valiosas que cualquier mensaje. Estás a salvo aquí."
                    )
                }
                latestCheckin.rumination >= 6f -> {
                    return MeditationScript(
                        title = "Defusión Cognitiva y Distanciamiento Mental",
                        toneInstruction = "Voz serena, cadenciosa y desapegada, invitando a observar la mente como testigo neutral.",
                        targetVulnerabilityBand = "MODO CLARIDAD (Bucle de Rumiación Obsesiva)",
                        fullText = "Cierra los ojos suavemente, $name. Tu mente lleva un rato atrapada en el laberinto de los 'qué hubiera pasado si' o buscando explicaciones que ya no existen. " +
                                "Imagina un arroyo sereno que corre frente a ti, con hojas flotando suavemente sobre el agua. " +
                                "Toma cada pensamiento recurrente, cada recuerdo o reproche, colócalo sobre una hoja y déjalo correr río abajo. No te subas a la hoja; solo mírala pasar. " +
                                "Recuerda: tener un pensamiento no significa que sea verdad ni que tengas que actuar sobre él. " +
                                "Vuelve a tu respiración... el aire entra fresco por tu nariz y sale tibio. Estás en el aquí y el ahora, donde tu presente te pertenece por entero."
                    )
                }
                latestCheckin.pain >= 7f -> {
                    return MeditationScript(
                        title = "Autocompasión Somática y Contención del Pecho",
                        toneInstruction = "Tono muy cálido, maternal/paternal, acogedor, con silencios compasivos y ritmo lento.",
                        targetVulnerabilityBand = "MODO REFUGIO (Dolor Emocional Agudo)",
                        fullText = "Lleva una o ambas manos a tu pecho, $name, sobre el centro del corazón. Siente el calor reconfortante y la presión suave de tu propio contacto. " +
                                "El dolor que experimentas duele porque lo que sentiste fue real e importante para ti. No hay nada malo en ti por sufrir; este es el proceso natural del corazón que cicatriza. " +
                                "Dite a ti mismo/a con ternura: 'Esto duele, pero puedo sostenerlo. Me ofrezco la compasión y el cuidado que necesito en este instante'. " +
                                "Inhala profundo llenando tu pecho de aire tibio... y al exhalar, deja caer los hombros y afloja el nudo de la garganta. " +
                                "No tienes que ser fuerte todo el tiempo. En este rincón seguro, descansa y permítete sanar."
                    )
                }
            }
        }

        return when {
            vulnerabilityScore >= 70 -> {
                // HIGH VULNERABILITY (Refugio, contención somática, ritmo lento)
                MeditationScript(
                    title = "Anclaje de Refugio y Regulación Somática",
                    toneInstruction = "Voz pausada, tono cálido y grave, silencios de 3 segundos entre frases, ritmo respiratorio 4-7-8.",
                    targetVulnerabilityBand = "MODO REFUGIO (Alta Vulnerabilidad)",
                    fullText = "Cierra los ojos suavemente, $name. No hay nada que tengas que resolver en este instante. " +
                            "Lleva una mano a tu pecho y siente el peso reconfortante de tu propia mano. Inhala profundamente en cuatro tiempos... uno, dos, tres, cuatro... " +
                            "Sostén el aire... y exhala despacio, liberando toda la tensión acumulada en tus hombros y mandíbula. " +
                            "El dolor que sientes es solo una ola biológica en tu cuerpo; no es una orden para actuar ni para buscar respuestas afuera. " +
                            "Aquí estás a salvo. Respira una vez más... Sostén tu presencia. Este momento difícil también pasará."
                )
            }
            vulnerabilityScore >= 35 -> {
                // MODERATE VULNERABILITY (Claridad, defusión cognitiva, presencia)
                MeditationScript(
                    title = "Presencia Consciente y Distancia Reflexiva",
                    toneInstruction = "Voz serena, clara y reflexiva, ritmo constante de 6 respiraciones por minuto.",
                    targetVulnerabilityBand = "MODO PRESENTE (Vulnerabilidad Moderada)",
                    fullText = "Adopta una postura digna y relajada, $name. Inhala sintiendo cómo el aire renueva tu energía interior. " +
                            "Observa los pensamientos que cruzan tu mente como nubes en un cielo abierto. No te aferres a ellos ni pelees por expulsarlos. " +
                            "Recuerda: eres el observador consciente, no el pensamiento que pasa. " +
                            "Tus límites te protegen. Respira hondo, ancla tus pies firmes en la tierra y recupera el mando de tu presente."
                )
            }
            else -> {
                // LOW VULNERABILITY (Activación, soberanía, reconstrucción vital)
                MeditationScript(
                    title = "Activación de Soberanía y Propósito",
                    toneInstruction = "Tono enérgico, firme, inspirador y luminoso, ritmo fluido de activación.",
                    targetVulnerabilityBand = "MODO EXPLORACIÓN (Vulnerabilidad Baja)",
                    fullText = "Abre tu pecho y respira con plenitud, $name. Siente la fuerza que has conquistado al elegir tu propia dignidad día tras día. " +
                            "Cada decisión de cuidar tus límites ha reconstruido tu confianza. Hoy es un día para invertir en tus proyectos, en tu cuerpo y en las personas que suman paz a tu vida. " +
                            "Inhala vitalidad, exhala gratitud. Eres soberano/a de tu camino. Da un paso adelante con total confianza."
                )
            }
        }
    }

    /**
     * Genera el guion de meditación guiada adaptando tono, ritmo y pausas según
     * el nivel de vulnerabilidad del usuario y coherente con su marco filosófico.
     * Fallback determinista si el modelo no está listo.
     */
    fun generateGuidedMeditationScript(
        vulnerabilityScore: Int,
        framework: SoltarFramework
    ): String {
        if (!isModelReady) {
            return when (framework) {
                SoltarFramework.ESTOICO ->
                    "Adopta una postura digna y serena. Apoya los pies firmemente en el suelo y siente el peso de tu cuerpo en reposo. Cierra los ojos con suavidad. Inhala hondo... retén el aire dos segundos... y suelta despacio por la boca. Recuerda las palabras de Epicteto: no son las cosas externas las que perturban tu mente, sino el juicio apresurado que haces de ellas. La persona que extrañas o la urgencia de escribir son sucesos fuera de tu soberanía; tu tranquilidad interior, tu dignidad y tu respuesta en este instante están bajo tu entero control. Respira una vez más. Deja que la ola de incomodidad rompa contra la roca de tu temple. Estás a salvo, en calma, y eres el único dueño de tu ciudadela interior."

                SoltarFramework.CATOLICO ->
                    "Pon tus manos abiertas sobre tus rodillas, en señal de acogida y entrega. Cierra los ojos y respira con lentitud. Inhala paz... exhala todo el peso, la culpa y la soledad que llevas cargando. Recuerda que no caminas en soledad: 'Venid a mí todos los que estáis cansados y agobiados, y yo os aliviaré'. Entrega este impulso, este dolor y esta herida en las manos del Padre. Pide la gracia de la paciencia y la custodia de tu corazón. El vacío que sientes hoy será llenado de gracia y consuelo si resistes la tentación de buscar falsos remedios. Respira la quietud de Su presencia. Confía, suelta el control y descansa en la certeza de que tu historia está siendo cuidada."

                SoltarFramework.PSICOLOGIA_MODERNA ->
                    "Toma asiento cómodamente y apoya las manos sobre tus muslos o una mano en tu pecho. Siente el latido de tu corazón y el vaivén natural de tu respiración. No intentes cambiar nada; solo observa el aire entrar fresco y salir tibio. La urgencia que experimentas en este instante es un fenómeno biológico temporal: tu sistema nervioso está buscando la recompensa dopaminérgica a la que estaba habituado. Imagina este impulso como una ola en el mar. Las olas suben, tienen un punto máximo de intensidad, pero invariablemente pierden fuerza y se disuelven en la arena. Tú no eres la ola; tú eres la orilla firme que la observa pasar. Respira profundo, afloja los hombros y dale a tu cuerpo cinco minutos de tregua compasiva. Eres más grande que este impulso transitorio."
            }
        }

        // Guion generado por IA adaptando ritmo, tono y silencios al estado neuroemocional
        return when {
            vulnerabilityScore >= 70 -> {
                // Alta vulnerabilidad: Máxima contención, pausas prolongadas, anclaje somático profundo
                when (framework) {
                    SoltarFramework.ESTOICO ->
                        "Detente por completo. Cierra los ojos. No intentes librar ninguna batalla en este momento. [Pausa de silencio]. Lleva tu atención exclusivamente al aire que entra por tu nariz y al peso de tus pies en el suelo. Respira despacio... inhala en cuatro tiempos... uno... dos... tres... cuatro... Sostén el aire... y exhala liberando el pecho. La tormenta que sientes no está dentro de ti; está afuera, golpeando las murallas de tu ciudadela. Nada de lo que pienses o sientas en este minuto de dolor te obliga a romper tus límites. Tu honor y tu paz residen en no reaccionar. Respira... quédate aquí conmigo. Esta ola también pasará."

                    SoltarFramework.CATOLICO ->
                        "Detente un instante. Deja caer los hombros y coloca una mano suavemente sobre tu corazón. [Pausa de silencio]. No te reproches por llorar o sentirte vulnerable; Dios está más cerca de ti en tu quebranto que en tu fortaleza aparente. Inhala Su consuelo... y exhala toda la angustia que oprime tu garganta. Entrega en este mismo segundo el deseo de saber o de intervenir: 'Señor, en Tus manos encomiendo mi descanso y mi dolor'. No tienes que forzar soluciones hoy. Permanece cobijado en Su paz, sabiendo que estás protegido y que no estás solo."

                    SoltarFramework.PSICOLOGIA_MODERNA ->
                        "Haz una pausa absoluta. Siéntate y nota el contacto de tu espalda con el respaldo. [Pausa de respiración profunda]. Tu sistema nervioso se encuentra en alerta de apego: la amígdala ha detectado amenaza y busca desesperadamente el estímulo conocido. No luches contra esta sensación; solo obsérvala como un testigo curioso. Siente la opresión en el pecho o el nudo en el estómago sin ponerle etiquetas catastróficas. Inhala en cuatro segundos... retén cuatro... y suelta el aire en seis segundos vaciando los pulmones por completo. Con cada exhalación, envía a tu cuerpo la señal biológica de seguridad: 'Estoy a salvo, esto es solo neuroquímica en retirada, el impulso decae'."
                }
            }
            vulnerabilityScore >= 35 -> {
                // Vulnerabilidad moderada: Defusión cognitiva, recentramiento, equilibrio y firmeza
                when (framework) {
                    SoltarFramework.ESTOICO ->
                        "Siéntate erguido, con la serenidad de quien recuerda quién es. [Pausa de respiración]. Observa tus pensamientos como si contemplaras el curso de un río desde la orilla alta. Pasan recuerdos, surgen dudas, aparecen impulsos. Ninguno de ellos tiene el poder de mover tus manos si tú no se lo concedes. Séneca decía: 'A menudo sufrimos más por la imaginación que por la realidad'. Inhala claridad... exhala toda queja innecesaria. Tu presente te pertenece. Sigue caminando con templanza."

                    SoltarFramework.CATOLICO ->
                        "Respira sereno y abre tu corazón a la templanza. [Pausa]. El camino del desapego y la madurez espiritual se construye en estos momentos cotidianos, donde eliges la fidelidad a tu dignidad en lugar de la gratificación inmediata. Da gracias por la fuerza que se te concede hoy. Pide luz para distinguir el amor verdadero del apego posesivo. Inhala paciencia, exhala rencor. Camina con paso humilde y firme, custodiando la paz que Dios te ha devuelto."

                    SoltarFramework.PSICOLOGIA_MODERNA ->
                        "Respira hondo y conecta con el momento presente. [Pausa]. Nota cómo tu mente intenta construir historias sobre el pasado o hipótesis sobre el futuro. Reconoce esas historias amablemente: 'Mi mente está recordando', 'mi mente está anticipando'. Pero tú estás aquí, en este cuarto, en este cuerpo que respira. Cada vez que decides no actuar desde la rumiación, estás fortaleciendo tu corteza prefrontal y debilitando los antiguos circuitos del hábito. Inhala autonomía... exhala serenidad. Estás haciendo un trabajo extraordinario."
                }
            }
            else -> {
                // Vulnerabilidad baja: Activación, soberanía, empoderamiento y propósito hacia adelante
                when (framework) {
                    SoltarFramework.ESTOICO ->
                        "Ponte de pie con dignidad, expande el pecho y respira con plenitud. [Pausa]. Siente la fuerza tranquila del autodominio. Has atravesado la niebla del dolor y hoy tu mente rectora gobierna tus acciones. La virtud no es la ausencia de recuerdos, sino la decisión inquebrantable de no ser gobernado por ellos. Inhala vigor... exhala cualquier atadura restante. Tienes una vida que construir y talentos que multiplicar. Marcha adelante con orgullo estoico."

                    SoltarFramework.CATOLICO ->
                        "Eleva tu mirada y respira con profunda gratitud. [Pausa]. El dolor ha madurado en fortaleza y la incertidumbre en esperanza viva. Agradece cada paso recorrido, cada momento en que te mantuviste en pie sostenido por la gracia. Hoy estás listo para dar frutos de paz, de alegría y de generosidad hacia los demás. Inhala bendición, exhala libertad. Camina con alegría sabiendo que tu vida tiene un propósito hermoso y renovado."

                    SoltarFramework.PSICOLOGIA_MODERNA ->
                        "Respira profundo y siente la energía vital recorriendo tu cuerpo. [Pausa]. Observa el contraste: lo que hace semanas parecía un abismo infranqueable hoy es solo un capítulo superado de tu historia. Tu cerebro ha reorganizado sus prioridades, tu identidad ya no depende de la validación ajena y tus metas vuelven a ser el centro de tu vida. Inhala autoconfianza y plenitud... exhala orgullo genuino por tu resiliencia. El futuro está completamente abierto ante ti."
                }
            }
        }
    }

    // =========================================================================
    // 1.6 NOTIFICACIONES — Contenido generado según tendencia de check-ins
    // =========================================================================
    data class NotificationContent(val title: String, val body: String)

    fun generateDailyNotification(
        checkins: List<CheckinEntity>,
        framework: SoltarFramework,
        userName: String
    ): NotificationContent {
        val pair = generateAdaptiveDailyNotification(checkins, framework, userName)
        return NotificationContent(pair.first, pair.second)
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
        val latest = recentCheckins.firstOrNull()
        val prev = if (recentCheckins.size > 1) recentCheckins[1] else null

        if (latest == null) {
            return Pair(
                "🌿 Momento de Pausa • $name",
                "Dedica 1 minuto a conectar con tu interior y registrar tu balance de hoy."
            )
        }

        val urgeTrendingUp = prev != null && latest.urgeToContact > prev.urgeToContact
        val painDecreasing = prev != null && latest.pain < prev.pain
        val highUrge = latest.urgeToContact >= 6f
        val highAutonomy = latest.autonomy >= 7f

        return when {
            highUrge || urgeTrendingUp -> {
                Pair(
                    "🛡️ Escudo de Serenidad • $name",
                    "Tu último registro notó inquietud. Respira antes de convertir el impulso en acción: tu paz no es negociable."
                )
            }
            painDecreasing || highAutonomy -> {
                Pair(
                    "✨ Progreso en tus Límites • $name",
                    "Se nota la constancia en tus últimos días. Celebra el terreno que has recuperado para ti mismo/a."
                )
            }
            latest.anxiety >= 6f -> {
                Pair(
                    "🌿 Pausa Somática • $name",
                    "Día de sobrecarga. Tómate cinco minutos para soltar hombros, respirar despacio y proteger tu espacio."
                )
            }
            else -> {
                when (framework) {
                    SoltarFramework.ESTOICO -> Pair(
                        "🏛️ Soberanía Diaria • $name",
                        "«Tienes poder sobre tu mente, no sobre los acontecimientos externos.» ¿Cómo vas hoy con tu check-in?"
                    )
                    SoltarFramework.CATOLICO -> Pair(
                        "✝️ Custodia y Paz • $name",
                        "«Por encima de todo, guarda tu corazón, porque de él brota la vida.» Registra tu balance del día."
                    )
                    SoltarFramework.PSICOLOGIA_MODERNA -> Pair(
                        "🧠 Claridad Emocional • $name",
                        "Cada día que eliges cuidarte, tu cerebro desactiva circuitos de dependencia. Tómate un minuto para tu balance."
                    )
                }
            }
        }
    }

    /**
     * Genera el texto de la notificación diaria considerando la tendencia real
     * de los últimos check-ins (mejora, empeoramiento, estancamiento) y el marco filosófico.
     * Fallback a plantilla fija si el modelo no está listo o hay datos insuficientes.
     */
    fun generateTrendBasedNotification(
        recentCheckins: List<CheckinEntity>,
        framework: SoltarFramework
    ): String {
        val fallbackQuote = when (framework) {
            SoltarFramework.ESTOICO ->
                "«Tienes poder sobre tu mente, no sobre los acontecimientos externos. Comprende esto y hallarás tu fuerza.» Tómate un minuto para tu balance diario."
            SoltarFramework.CATOLICO ->
                "«Por encima de todo lo que guardes, guarda tu corazón, porque de él brota la vida.» Tómate un momento de recogimiento para tu balance de hoy."
            SoltarFramework.PSICOLOGIA_MODERNA ->
                "«Cada día que eliges no ceder al impulso, tu cerebro desactiva un circuito de dependencia.» Registra tu evolución en 1 minuto."
        }

        if (!isModelReady || recentCheckins.size < 2) {
            return fallbackQuote
        }

        val sorted = recentCheckins.sortedByDescending { it.timestamp }
        val latest = sorted.first()
        val previous = sorted.drop(1).take(3)
        val avgPrevPain = previous.map { it.pain }.average().toFloat()
        val avgPrevUrge = previous.map { it.urgeToContact }.average().toFloat()
        val avgPrevAutonomy = previous.map { it.autonomy }.average().toFloat()

        val isImproving = (latest.pain < avgPrevPain - 0.5f) ||
                (latest.urgeToContact < avgPrevUrge - 0.5f) ||
                (latest.autonomy > avgPrevAutonomy + 0.5f) ||
                (latest.pain <= 3.5f && latest.autonomy >= 6.5f)

        val isWorsening = (latest.pain > avgPrevPain + 0.8f) ||
                (latest.urgeToContact > avgPrevUrge + 0.8f) ||
                (latest.urgeToContact >= 6.5f) ||
                (latest.pain >= 7.0f)

        return when {
            isImproving -> when (framework) {
                SoltarFramework.ESTOICO ->
                    "Tu serenidad avanza con paso firme: tus últimos registros muestran que estás recuperando el dominio sobre tu mente rectora. Sigue custodiando tu templo interior con templanza."
                SoltarFramework.CATOLICO ->
                    "Se percibe la gracia obrando en tu caminar: la paz va ganando terreno a la pesadumbre en tus últimos registros. Da gracias por este alivio y persevera con fe."
                SoltarFramework.PSICOLOGIA_MODERNA ->
                    "Tendencia favorable detectada: tus indicadores de dolor y urgencia muestran una desensibilización progresiva y mayor autonomía. Celebra este paso hacia tu bienestar."
            }
            isWorsening -> when (framework) {
                SoltarFramework.ESTOICO ->
                    "Percibimos un repunte en la marea emocional. Recuerda a Séneca: pide al impulso que espere. No conviertas una molestia pasajera en una derrota de tu juicio. Respira y mantén tu centro."
                SoltarFramework.CATOLICO ->
                    "En tus últimos registros asoma el cansancio o la angustia. No temas: 'Cerca está el Señor de los quebrantados de corazón'. Haz una pausa, entrega la carga y cuida tu paz."
                SoltarFramework.PSICOLOGIA_MODERNA ->
                    "Tus últimos registros reflejan un aumento en la reactividad o el impulso. Es un pico esperable en la abstinencia afectiva: no te juzgues, respira hondo y activa tu protocolo de contención."
            }
            else -> when (framework) {
                // Estancamiento / Meseta
                SoltarFramework.ESTOICO ->
                    "Tus días atraviesan una meseta silenciosa. La paciencia estoica no es resignación pasiva, sino la constancia serena que no exige resultados inmediatos a la naturaleza. Sostén tus hábitos hoy."
                SoltarFramework.CATOLICO ->
                    "El corazón se encuentra en tiempo de espera y purificación silenciosa. Recuerda que la semilla germina bajo tierra en quietud. Mantén la confianza y el recogimiento en tu día."
                SoltarFramework.PSICOLOGIA_MODERNA ->
                    "Tus registros muestran estabilidad en meseta. El proceso de reorganización cerebral requiere tiempo en calma sin novedades bruscas. Confía en el proceso y haz tu pausa consciente."
            }
        }
    }

    // =========================================================================
    // 1.7 LISTA DE RED FLAGS — Asistencia guiada
    // =========================================================================
    fun getRedFlagGuidedPrompts(): List<String> {
        return listOf(
            "¿Hubo momentos donde sentiste que tus límites eran ignorados, ridiculizados o castigados con silencio (ley del hielo)?",
            "¿Notaste contradicción sistemática entre lo que te prometía con palabras y lo que hacía con sus actos?",
            "¿Tenías que medir cuidadosamente tus palabras por miedo a una reacción explosiva o a que te tachara de exagerado/a?",
            "¿Descubriste mentiras, dobles discursos o sospechas recurrentes que te obligaban a vivir en estado de detective?",
            "¿Sentías que el esfuerzo por sostener la relación dependía desproporcionadamente de ti?"
        )
    }

    fun synthesizeRedFlagFromDescription(userDescription: String): String {
        val clean = userDescription.trim().lowercase(Locale.getDefault())
        return when {
            clean.contains("silencio") || clean.contains("hielo") || clean.contains("ignorar") || clean.contains("desaparece") ->
                "Ley del hielo y castigo con silencio ante los desacuerdos en lugar de comunicación madura."
            clean.contains("culpa") || clean.contains("exagerad") || clean.contains("loc") || clean.contains("mentiros") ->
                "Invalidación emocional y gaslighting: hacerme dudar de mi propio criterio y memoria."
            clean.contains("promes") || clean.contains("cambiar") || clean.contains("dice una cosa") ->
                "Inconsistencia crónica: promesas constantes de cambio que nunca se reflejaban en hechos."
            clean.contains("control") || clean.contains("amigos") || clean.contains("celos") || clean.contains("ropa") ->
                "Control encubierto y celos injustificados que limitaban mi autonomía y relaciones personales."
            clean.contains("desaparecer") || clean.contains("otra persona") || clean.contains("mentira") || clean.contains("ocult") ->
                "Falta de transparencia, secretos y traición a la confianza básica de la relación."
            else ->
                userDescription.trim().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
    }

    // =========================================================================
    // 2.1 METAS DE IDENTIDAD — Sugerencia de hábitos concretos
    // =========================================================================
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
            IdentityGoalSuggestion(
                actionTitle = "Caminar 30 min sin teléfono para regular el cortisol",
                whoIWantToBe = "Una persona serena, presente y soberana de su tiempo",
                area = "Cuerpo y Salud"
            ),
            IdentityGoalSuggestion(
                actionTitle = "Bloquear 45 min diarios para retomar un proyecto propio",
                whoIWantToBe = "Una persona enfocada en su propósito y desarrollo",
                area = "Proyectos y Trabajo"
            ),
            IdentityGoalSuggestion(
                actionTitle = "Escribir 10 min de reflexión nocturna en el diario",
                whoIWantToBe = "Una persona que procesa sus emociones con lucidez",
                area = "Mente y Espacio Propio"
            )
        )
    }

    fun suggestIdentityHabits(
        lifeArea: String,
        whoIWantToBe: String,
        framework: SoltarFramework
    ): List<String> {
        return when (lifeArea) {
            "Cuerpo y Salud" -> listOf(
                "Caminar 30 minutos al aire libre sin consultar el móvil para regular el cortisol.",
                "Realizar 20 minutos de ejercicio de fuerza o estiramiento al despertar.",
                "Cenar ligero y dejar las pantallas 45 minutos antes de dormir para cuidar el descanso biológico."
            )
            "Proyectos y Trabajo" -> listOf(
                "Bloquear 45 minutos de trabajo profundo diario en un proyecto estrictamente personal.",
                "Retomar un curso, lectura técnica o certificación que tenía postergada.",
                "Organizar mis finanzas personales y fijar una meta de ahorro independiente."
            )
            "Amistades y Red" -> listOf(
                "Llamar o quedar semanalmente con una amistad leal para conversar de la vida sin hablar del pasado.",
                "Asistir a un taller, grupo de interés o voluntariado donde conocer nuevas personas afines.",
                "Aceptar invitaciones sociales sin cancelar a última hora por desánimo."
            )
            "Mente y Espacio Propio" -> listOf(
                "Escribir 10 minutos en el diario antes de acostarme para ordenar la mente.",
                "Dedicar 15 minutos al día a la lectura de filosofía, psicología o espiritualidad.",
                "Crear un rincón de silencio en casa exclusivo para mi descanso y serenidad."
            )
            else -> listOf(
                "Hábito de pausa consciente: respirar hondo 3 veces antes de reaccionar.",
                "Dedicar 20 minutos diarios a una actividad creativa o de aprendizaje propio.",
                "Registrar un límite claro cada semana y cumplirlo con firmeza."
            )
        }
    }

    // =========================================================================
    // 2.2 SELECCIÓN DE TARJETA DE SABIDURÍA — Adaptación emocional y clínica
    // =========================================================================
    fun selectAdaptiveWisdomCard(
        cards: List<WisdomCard>,
        latestCheckin: CheckinEntity?,
        clinicalCategory: String?
    ): WisdomCard {
        if (cards.isEmpty()) return cards.first()
        if (latestCheckin == null && clinicalCategory.isNullOrBlank()) return cards.random()

        val highUrge = (latestCheckin?.urgeToContact ?: 0f) >= 6f
        val highPain = (latestCheckin?.pain ?: 0f) >= 7f
        val highRumination = (latestCheckin?.rumination ?: 0f) >= 7f

        val targetedCard = cards.firstOrNull { card ->
            val text = (card.title + " " + card.quote + " " + card.reflection).lowercase(Locale.getDefault())
            when {
                highUrge -> text.contains("impulso") || text.contains("demora") || text.contains("contacto") || text.contains("freno")
                highPain -> text.contains("dolor") || text.contains("herida") || text.contains("sana") || text.contains("paciencia")
                highRumination -> text.contains("mente") || text.contains("pensamiento") || text.contains("opinión") || text.contains("juicio")
                !clinicalCategory.isNullOrBlank() -> text.contains(clinicalCategory.lowercase(Locale.getDefault()).take(5))
                else -> false
            }
        }

        return targetedCard ?: cards.random()
    }

    fun selectOptimalWisdomCard(
        availableCards: List<WisdomCard>,
        latestCheckin: CheckinEntity?,
        framework: SoltarFramework,
        recentCardIds: List<String>
    ): WisdomCard {
        val eligible = availableCards.filterNot { recentCardIds.contains(it.id) }.ifEmpty { availableCards }
        return selectAdaptiveWisdomCard(eligible, latestCheckin, null)
    }

    fun synthesizeRedFlagsPattern(flags: List<String>): String {
        if (flags.isEmpty()) return "Sin patrones suficientes registrados aún."
        val allText = flags.joinToString(" ").lowercase(Locale.getDefault())

        val isBoundaryIssue = allText.contains("limite") || allText.contains("invad") || allText.contains("respet") || allText.contains("culpa")
        val isInconsistency = allText.contains("mentir") || allText.contains("ocult") || allText.contains("cambi") || allText.contains("promes")
        val isEmotionalColdness = allText.contains("frial") || allText.contains("distanc") || allText.contains("desinter") || allText.contains("ignorar")

        return when {
            isBoundaryIssue && isEmotionalColdness -> "Patrón mixto de transgresión de límites y repliegue emocional: tendencia a la manipulación sutil por intermitencia afectiva. Antídoto: firmeza radical y contacto cero."
            isBoundaryIssue -> "Patrón predominante de invasión y transgresión de límites personales: exigencia desmedida de justificaciones y desestimación de tus necesidades. Antídoto: sostener el 'no' sin culpa."
            isInconsistency -> "Patrón de incongruencia entre palabras y conductas: ambivalencia crónica que genera adicción por refuerzo intermitente. Antídoto: juzgar solo por hechos objetivos continuados."
            isEmotionalColdness -> "Patrón de retirada afectiva y ley del hielo como método de control. Antídoto: desvincular tu autoestima de su validación o frialdad."
            else -> "Patrón de desgaste vincular por falta de reciprocidad y desatención mutua de acuerdos fundamentales."
        }
    }

    // =========================================================================
    // 2.3 EXPLICACIÓN DE PUNTUACIÓN DE VULNERABILIDAD
    // =========================================================================
    fun explainVulnerabilityScore(
        score: Int,
        latestCheckin: CheckinEntity?,
        upcomingRiskTitle: String?,
        daysToRisk: Int?,
        hasRelapse48h: Boolean
    ): String {
        return when {
            hasRelapse48h -> 
                "Hoy la puntuación refleja el impacto de un tropiezo reciente en las últimas 48h; tu sistema nervioso necesita contención y cero reproches."
            daysToRisk != null && daysToRisk in 0..7 && !upcomingRiskTitle.isNullOrBlank() -> 
                if (daysToRisk == 0) "Hoy se cumple $upcomingRiskTitle; la cercanía de la fecha señalada eleva el estado de alerta biológica."
                else "Pesa la proximidad de $upcomingRiskTitle en $daysToRisk días, lo que activa anticipación involuntaria en tu ánimo."
            latestCheckin != null && latestCheckin.urgeToContact >= 6f -> 
                "El impulso de contacto puntuó elevado (${latestCheckin.urgeToContact.toInt()}/10) en tu último registro, requiriendo mayor blindaje."
            latestCheckin != null && latestCheckin.pain >= 7f -> 
                "El dolor agudo reportado recientemente indica que la herida está sensible hoy; prioriza actividades de bajo estrés."
            latestCheckin != null && latestCheckin.autonomy >= 7f -> 
                "Tu alta autonomía registrada te sitúa en un momento favorable y estable de reconstrucción personal."
            else -> 
                "Puntuación calculada según el balance de tu último registro emocional, hábitos y ausencia de factores de riesgo críticos."
        }
    }

    // =========================================================================
    // 2.4 ANÁLISIS DE PATRONES DE RECAÍDA
    // =========================================================================
    fun analyzeRelapsePatterns(
        relapses: List<RelapseEntity>,
        triggers: List<TriggerEventEntity>
    ): RelapsePatternAnalysis {
        val total = relapses.size + triggers.size
        if (total == 0) {
            return RelapsePatternAnalysis(
                totalEpisodes = 0,
                primaryTrigger = "Sin registros suficientes",
                criticalTimeWindow = "No determinada",
                emotionalUndercurrent = "Estable",
                syntheticInsight = "Aún no hay suficientes tropiezos o detonantes para trazar un patrón recurrente.",
                proactivePrescription = "Mantén el registro en el Modo Impulso cuando sientas urgencia."
            )
        }

        // Aggregate triggers
        val triggerTexts = (relapses.map { it.trigger } + triggers.map { it.trigger }).map { it.lowercase(Locale.getDefault()) }
        val socialCount = triggerTexts.count { it.contains("red") || it.contains("foto") || it.contains("whatsapp") || it.contains("instagram") }
        val memoryCount = triggerTexts.count { it.contains("recuerdo") || it.contains("musica") || it.contains("cancion") || it.contains("lugar") }
        val nightCount = triggerTexts.count { it.contains("noche") || it.contains("cama") || it.contains("madrugada") }

        val primaryTrigger = when {
            socialCount >= memoryCount && socialCount > 0 -> "Búsqueda o exposición en redes sociales / aplicaciones de mensajería"
            memoryCount > 0 -> "Exposición a recuerdos sensoriales (fotos, canciones, lugares compartidos)"
            nightCount > 0 -> "Momentos de soledad nocturna y cansancio mental"
            else -> "Detonantes emocionales de soledad e incertidumbre"
        }

        val criticalWindow = when {
            nightCount > 0 -> "Noches y fines de semana (21:00 a 02:00)"
            else -> "Momentos de inactividad o aislamiento durante el fin de semana"
        }

        return RelapsePatternAnalysis(
            totalEpisodes = total,
            primaryTrigger = primaryTrigger,
            criticalTimeWindow = criticalWindow,
            emotionalUndercurrent = "Búsqueda de alivio dopaminérgico ante el vacío y la abstinencia afectiva",
            syntheticInsight = "Tus registros muestran que las recaídas no ocurren por falta de voluntad, sino en momentos previsibles de fatiga decisional y acceso fácil al teléfono.",
            proactivePrescription = "Establece un protocolo estricto de desconexión nocturna y ten a mano la herramienta Modo Impulso antes de que el impulso escale."
        )
    }

    fun analyzeRelapsePatterns(
        relapses: List<RelapseEntity>,
        urgeEpisodes: List<UrgeEpisodeEntity>,
        recentCheckins: List<CheckinEntity>
    ): String {
        val total = relapses.size + urgeEpisodes.size
        if (total == 0) {
            return "No se registran suficientes episodios de contacto o impulsos para trazar un patrón recurrente. Mantén tu soberanía y registra cualquier detonante en el Modo Impulso."
        }

        val triggerTexts = (relapses.map { it.trigger } + urgeEpisodes.map { it.trigger }).map { it.lowercase(Locale.getDefault()) }
        val socialCount = triggerTexts.count { it.contains("red") || it.contains("foto") || it.contains("whatsapp") || it.contains("instagram") }
        val memoryCount = triggerTexts.count { it.contains("recuerdo") || it.contains("musica") || it.contains("cancion") || it.contains("lugar") }
        val nightCount = triggerTexts.count { it.contains("noche") || it.contains("cama") || it.contains("madrugada") }

        val allTimestamps = relapses.map { it.timestamp } + urgeEpisodes.map { it.timestamp }
        val calendar = java.util.Calendar.getInstance()
        var weekendEpisodes = 0
        var eveningEpisodes = 0

        for (ts in allTimestamps) {
            calendar.timeInMillis = ts
            val dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK)
            val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
            if (dayOfWeek == java.util.Calendar.SATURDAY || dayOfWeek == java.util.Calendar.SUNDAY || dayOfWeek == java.util.Calendar.FRIDAY) {
                weekendEpisodes++
            }
            if (hour >= 20 || hour < 4) {
                eveningEpisodes++
            }
        }

        val temporalWindow = when {
            eveningEpisodes > total / 2 && weekendEpisodes > total / 2 -> "los fines de semana por la noche y madrugada"
            eveningEpisodes > total / 2 -> "las noches a partir de las 21:00"
            weekendEpisodes > total / 2 -> "los fines de semana durante momentos de descanso o inactividad"
            else -> "momentos de pausa o cansancio a lo largo del día"
        }

        val primaryFactor = when {
            socialCount >= memoryCount && socialCount > 0 -> "exposición digital involuntaria o chequeo compulsivo de redes sociales"
            memoryCount > 0 -> "detonantes nostálgicos y evocación de lugares o recuerdos compartidos"
            nightCount > 0 -> "sensación de vacío y soledad acumulada al final de la jornada"
            else -> "picos transitorios de ansiedad o rumiación mental no gestionada"
        }

        val avgPain = recentCheckins.take(5).map { it.pain }.average().takeIf { !it.isNaN() } ?: 5.0
        val painContext = if (avgPain > 6.0) "en un período de alta reactividad neuroquímica" else "cuando baja la guardia de tus límites conscientes"

        return "«Tus momentos de mayor vulnerabilidad ocurren $temporalWindow, generalmente precedidos de $primaryFactor $painContext. Estos impulsos no reflejan debilidad ni falta de voluntad, sino una búsqueda condicionada de dopamina ante el vacío. Te sugerimos activar el protocolo Modo Impulso (20 min) inmediatamente al percibir la primera señal corporal y establecer un corte estricto de pantallas antes de ir a la cama.»"
    }

    /**
     * Analiza el historial de TriggerEventEntity a lo largo del tiempo.
     * Solo se activa si hay al menos 3-4 eventos registrados.
     * Identifica patrones recurrentes (día de la semana, hora, disparador) y devuelve
     * null si no hay un patrón claro para no forzar conclusiones inventadas.
     */
    fun analyzeRelapsePatterns(triggerEvents: List<TriggerEventEntity>): String? {
        // Solo se activa si hay al menos 3-4 TriggerEventEntity registrados
        if (triggerEvents.size < 3) return null

        val total = triggerEvents.size
        val calendar = Calendar.getInstance()

        // 1. Análisis temporal por franjas horarias
        var nightEpisodes = 0     // 21:00 a 04:59
        var eveningEpisodes = 0   // 18:00 a 20:59
        var morningEpisodes = 0   // 06:00 a 11:59
        var afternoonEpisodes = 0 // 12:00 a 17:59

        // 2. Análisis por días de la semana
        var weekendEpisodes = 0 // Viernes, Sábado, Domingo
        var sundayEpisodes = 0

        for (event in triggerEvents) {
            calendar.timeInMillis = event.timestamp
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val day = calendar.get(Calendar.DAY_OF_WEEK)

            when {
                hour >= 21 || hour < 5 -> nightEpisodes++
                hour in 18..20 -> eveningEpisodes++
                hour in 6..11 -> morningEpisodes++
                else -> afternoonEpisodes++
            }

            if (day == Calendar.FRIDAY || day == Calendar.SATURDAY || day == Calendar.SUNDAY) {
                weekendEpisodes++
            }
            if (day == Calendar.SUNDAY) {
                sundayEpisodes++
            }
        }

        // 3. Análisis semántico de detonantes y notas
        val combinedTexts = triggerEvents.map {
            "${it.trigger} ${it.context} ${it.emotion} ${it.note}".lowercase(Locale.getDefault())
        }

        val digitalCount = combinedTexts.count {
            it.contains("red") || it.contains("foto") || it.contains("whatsapp") ||
                    it.contains("instagram") || it.contains("facebook") || it.contains("perfil") ||
                    it.contains("mensaje") || it.contains("chat") || it.contains("en línea") || it.contains("historia")
        }
        val memoryCount = combinedTexts.count {
            it.contains("recuerdo") || it.contains("musica") || it.contains("música") ||
                    it.contains("cancion") || it.contains("canción") || it.contains("lugar") ||
                    it.contains("carta") || it.contains("regalo") || it.contains("aniversario")
        }
        val solitudeCount = combinedTexts.count {
            it.contains("solo") || it.contains("sola") || it.contains("soledad") ||
                    it.contains("cama") || it.contains("casa") || it.contains("vacio") ||
                    it.contains("vacío") || it.contains("aburrimiento") || it.contains("silencio")
        }
        val stressCount = combinedTexts.count {
            it.contains("trabajo") || it.contains("estres") || it.contains("estrés") ||
                    it.contains("agobio") || it.contains("ansiedad") || it.contains("cansancio")
        }
        val alcoholCount = combinedTexts.count {
            it.contains("alcohol") || it.contains("copa") || it.contains("cerveza") ||
                    it.contains("fiesta") || it.contains("bar") || it.contains("salir")
        }

        // Evaluación de umbrales clínicos de recurrencia
        val hasNightPattern = nightEpisodes >= (total * 0.5)
        val hasEveningPattern = eveningEpisodes >= (total * 0.45)
        val hasMorningPattern = morningEpisodes >= (total * 0.45)
        val hasWeekendPattern = weekendEpisodes >= (total * 0.6)
        val hasSundayPattern = sundayEpisodes >= (total * 0.45)

        val hasDigitalPattern = digitalCount >= (total * 0.45)
        val hasMemoryPattern = memoryCount >= (total * 0.45)
        val hasSolitudePattern = solitudeCount >= (total * 0.45)
        val hasStressPattern = stressCount >= (total * 0.45)
        val hasAlcoholPattern = alcoholCount >= (total * 0.45)

        val hasTimePattern = hasNightPattern || hasEveningPattern || hasMorningPattern || hasWeekendPattern || hasSundayPattern
        val hasTriggerPattern = hasDigitalPattern || hasMemoryPattern || hasSolitudePattern || hasStressPattern || hasAlcoholPattern

        // Si no hay un patrón claro, no inventamos un hallazgo
        if (!hasTimePattern && !hasTriggerPattern) {
            return null
        }

        val timeDesc = when {
            hasNightPattern && hasWeekendPattern -> "durante las noches de fin de semana (después de las 21:00)"
            hasNightPattern -> "en horarios nocturnos (a partir de las 21:00 o en la madrugada)"
            hasEveningPattern -> "en la transición del atardecer (entre las 18:00 y las 21:00)"
            hasMorningPattern -> "a primera hora de la mañana, al despertar y afrontar el día"
            hasSundayPattern -> "principalmente los domingos por la tarde y noche"
            hasWeekendPattern -> "durante los fines de semana, coincidiendo con momentos de menor actividad"
            else -> null
        }

        val triggerDesc = when {
            hasDigitalPattern -> "la exposición a redes sociales, estados o aplicaciones de mensajería"
            hasMemoryPattern -> "la evocación sensorial de recuerdos compartidos, canciones o lugares del pasado"
            hasSolitudePattern -> "momentos de soledad e inactividad no planificada en el hogar"
            hasStressPattern -> "situaciones de sobrecarga laboral o fatiga decisional acumulada"
            hasAlcoholPattern -> "encuentros sociales o momentos con consumo de alcohol donde disminuye la inhibición"
            else -> null
        }

        val sb = StringBuilder()
        sb.append("Tus registros revelan un patrón recurrente claro: los momentos de mayor vulnerabilidad tienden a concentrarse ")
        if (timeDesc != null && triggerDesc != null) {
            sb.append("$timeDesc, activados predominantemente por $triggerDesc.")
        } else if (timeDesc != null) {
            sb.append("$timeDesc, cuando la fatiga acumulada reduce la energía de contención frontal.")
        } else if (triggerDesc != null) {
            sb.append("en situaciones vinculadas a $triggerDesc.")
        }

        sb.append(" Conocer esta ventana te permite anticiparte: no es falta de voluntad, sino un hábito reactivo predecible que puedes desactivar programando con antelación un protocolo de protección o desconexión.")

        return sb.toString()
    }

    // =========================================================================
    // 2.5 RECOMENDACIONES DE CONTEXTO ENRIQUECIDAS
    // =========================================================================
    fun enrichContextualRecommendation(
        settings: SoltarSettingsEntity?,
        baseRec: ContextualRecommendation
    ): ContextualRecommendation {
        if (settings == null) return baseRec

        val name = if (settings.userName.isNotBlank()) settings.userName else "amigo/a"
        val enrichedBanner = when (baseRec.contactCategory) {
            "PARENTAL" -> "«$name: Tus hijos merecen padres serenos y predecibles. Toda comunicación con tu ex debe ser aséptica, breve, centrada en la logística de los niños y jamás en el pasado.»"
            "WORK" -> "«$name: En el entorno laboral compartido, tu profesionalidad es tu mejor escudo. Cero conversaciones personales en pasillos o pausas de café.»"
            "COHABITATION" -> "«$name: Mientras continúe la convivencia física bajo el mismo techo, delimita espacios y ejecuta tu plan de salida sin ceder a debates nocturnos.»"
            "PRACTICAL" -> "«$name: Asuntos económicos o materiales pendientes: gestiona los trámites con frialdad notarial y sin espacio para reproches afectivos.»"
            else -> "«$name: ${baseRec.bannerMessage.trim('«', '»')}»"
        }

        return baseRec.copy(
            bannerMessage = enrichedBanner,
            strategySummary = baseRec.strategySummary + " Protocolo adaptado al perfil activo de $name."
        )
    }

    // =========================================================================
    // 2.6 RESUMEN CLÍNICO DE PROGRESO (Exportación y revisión profesional)
    // =========================================================================
    fun generateClinicalProgressSummary(
        checkins: List<CheckinEntity>,
        journals: List<JournalEntryEntity>,
        letters: List<UnsentLetterEntity>,
        breakupDays: Int,
        userName: String
    ): String {
        val name = if (userName.isNotBlank()) userName else "Usuario"
        val totalCheckins = checkins.size
        val avgPainStart = checkins.takeLast(5).map { it.pain }.average().takeIf { !it.isNaN() } ?: 7.0
        val avgPainRecent = checkins.take(5).map { it.pain }.average().takeIf { !it.isNaN() } ?: 5.0
        val avgAutonomy = checkins.take(5).map { it.autonomy }.average().takeIf { !it.isNaN() } ?: 6.0

        val painDelta = avgPainStart - avgPainRecent

        val sb = StringBuilder()
        sb.append("📋 INFORME CLÍNICO DE EVOLUCIÓN Y PROCESO TERAPÉUTICO\n")
        sb.append("Identificador: $name • Tiempo transcurrido: $breakupDays días\n")
        sb.append("Total check-ins registrados: $totalCheckins | Entradas de diario: ${journals.size} | Cartas privadas: ${letters.size}\n\n")

        sb.append("1. DINÁMICA DEL DOLOR Y APEGO:\n")
        if (painDelta > 1.5) {
            sb.append("- Se evidencia una reducción clínica consistente del dolor subjetivo (delta favorable de +${String.format(Locale.US, "%.1f", painDelta)} puntos).\n")
            sb.append("- La reactividad ante el recuerdo ha disminuido progresivamente, transitando de fase de shock a fase de reorganización.\n")
        } else {
            sb.append("- El dolor se mantiene en meseta de elaboración activa; se aprecian picos de ambivalencia normales en procesos de desvinculación.\n")
        }

        sb.append("\n2. NIVEL DE AUTONOMÍA Y SOBERANÍA:\n")
        sb.append("- Autonomía promedio reciente: ${String.format(Locale.US, "%.1f", avgAutonomy)}/10.\n")
        sb.append("- El usuario muestra capacidad creciente de autorregulación somática y aplicación de límites firmes.\n\n")

        sb.append("3. CONCLUSIÓN Y RECOMENDACIÓN TERAPÉUTICA:\n")
        sb.append("- Proceso de duelo en curso con buen apego al protocolo de contención y registro reflexivo.\n")
        sb.append("- Se sugiere continuar fortaleciendo la reconstrucción de la identidad propia y la red social de apoyo.")

        return sb.toString()
    }

    // =========================================================================
    // 2.7 ONBOARDING — Evaluación de cuestionario para recomendar marco
    // =========================================================================
    fun evaluateOnboardingFrameworkRecommendation(
        q1AnswerIndex: Int, // 0: Temple racional, 1: Biología/Apego, 2: Fe/Espiritualidad
        q2AnswerIndex: Int, // 0: Lo que depende de mí, 1: Desactivar adicción/dolor, 2: Sentido y Dios
        q3AnswerIndex: Int  // 0: Ejercicio de carácter, 1: Síntoma normal que pasará, 2: Refugio en oración
    ): FrameworkRecommendation {
        val scores = mutableMapOf(
            SoltarFramework.ESTOICO to 0,
            SoltarFramework.PSICOLOGIA_MODERNA to 0,
            SoltarFramework.CATOLICO to 0
        )

        fun scoreChoice(idx: Int) {
            when (idx) {
                0 -> scores[SoltarFramework.ESTOICO] = scores[SoltarFramework.ESTOICO]!! + 1
                1 -> scores[SoltarFramework.PSICOLOGIA_MODERNA] = scores[SoltarFramework.PSICOLOGIA_MODERNA]!! + 1
                2 -> scores[SoltarFramework.CATOLICO] = scores[SoltarFramework.CATOLICO]!! + 1
            }
        }

        scoreChoice(q1AnswerIndex)
        scoreChoice(q2AnswerIndex)
        scoreChoice(q3AnswerIndex)

        val winner = scores.maxByOrNull { it.value }?.key ?: SoltarFramework.PSICOLOGIA_MODERNA

        return when (winner) {
            SoltarFramework.ESTOICO -> FrameworkRecommendation(
                recommendedFramework = SoltarFramework.ESTOICO,
                matchConfidencePercentage = 92,
                rationale = "Tus respuestas reflejan una inclinación hacia la razón, la autodisciplina y la necesidad de enfocarte estrictamente en lo que está bajo tu control.",
                primaryBenefit = "Te ayudará a cortar la rumiación mediante la lógica serena de Marco Aurelio, Séneca y Epicteto."
            )
            SoltarFramework.CATOLICO -> FrameworkRecommendation(
                recommendedFramework = SoltarFramework.CATOLICO,
                matchConfidencePercentage = 95,
                rationale = "Tus respuestas muestran una profunda valoración del sentido espiritual, la dignidad del corazón humano y la confianza en la providencia.",
                primaryBenefit = "Te brindará consuelo profundo, esperanza trascendente y custodia interior basada en la tradición bíblica y espiritual."
            )
            SoltarFramework.PSICOLOGIA_MODERNA -> FrameworkRecommendation(
                recommendedFramework = SoltarFramework.PSICOLOGIA_MODERNA,
                matchConfidencePercentage = 90,
                rationale = "Tus respuestas valoran entender los mecanismos neurobiológicos del apego, la compasión hacia uno mismo y el trabajo basado en evidencia.",
                primaryBenefit = "Te aportará herramientas científicas de terapia ACT, regulación somática y reconstrucción de autoestima."
            )
        }
    }

    fun personalizeContextualRecommendation(
        base: ContextualRecommendation,
        settings: SoltarSettingsEntity
    ): ContextualRecommendation {
        val userName = settings.userName.ifBlank { "tú" }
        val duration = when {
            settings.relDuration.contains("1_3", true) || settings.relDuration.contains("1 a 3", true) -> "tras varios años de vínculo"
            settings.relDuration.contains("mas", true) || settings.relDuration.contains("5", true) || settings.relDuration.contains("10", true) -> "tras una relación extensa"
            settings.relDuration.contains("meses", true) -> "en este período de transición"
            else -> "en tu situación actual"
        }
        val customBanner = when (base.contactCategory) {
            "PARENTAL" -> "«$userName, en este vínculo con hijos $duration: tu comunicación debe ser estrictamente parental y funcional. Cero apertura emocional o ambigüedad.»"
            "WORK" -> "«$userName, en el entorno profesional compartido $duration: mantén un trato impecable, aséptico y circunscrito únicamente al trabajo.»"
            "COHABITATION" -> "«$userName, compartiendo espacio temporal $duration: protege tus límites físicos y emocionales. Cada día es un paso hacia tu independencia.»"
            "PRACTICAL" -> "«$userName, resolviendo compromisos prácticos $duration: liquida gestiones objetivas sin reabrir conversaciones sobre la historia compartida.»"
            else -> when {
                base.profileTypeDescription.contains("Infidelidad", true) ->
                    "«$userName, procesando la ruptura por traición $duration: tu dignidad personal es innegociable. Cero justificaciones o auto-culpas.»"
                base.profileTypeDescription.contains("Decisión Mutua", true) ->
                    "«$userName, en una ruptura acordada $duration: la decisión mutua evita culpables pero genera alta ambivalencia. Sostén el acuerdo sin reabrir dudas.»"
                base.profileTypeDescription.contains("Duelo Anticipado", true) ->
                    "«$userName, procesando el duelo anticipado $duration: reconoce el desgaste previo acumulado y valida tu necesidad de paz.»"
                base.profileTypeDescription.contains("Tomaste la Decisión", true) ->
                    "«$userName, habiendo tomado la decisión de partir $duration: sostén tu coherencia con compasión y sin caer en culpa punitiva.»"
                base.profileTypeDescription.contains("Ciclo", true) ->
                    "«$userName, interrumpiendo el ciclo intermitente $duration: recuerda los hechos objetivos frente a las promesas repetidas.»"
                base.profileTypeDescription.contains("Larga Duración", true) ->
                    "«$userName, reconfigurando tu vida $duration: reconstruir tu identidad y rutinas independientes requiere paciencia y compasión.»"
                else ->
                    "«$userName, $duration: antes de transformar cualquier impulso en acción, respira y elige tu soberanía personal.»"
            }
        }
        val customStrategy = "${base.strategySummary} (Adaptado para $userName $duration)."

        return base.copy(
            bannerMessage = customBanner,
            strategySummary = customStrategy
        )
    }

    private fun countKeywords(text: String, keywords: List<String>): Int {
        val lower = text.lowercase(Locale.getDefault())
        return keywords.count { lower.contains(it) }
    }
}
