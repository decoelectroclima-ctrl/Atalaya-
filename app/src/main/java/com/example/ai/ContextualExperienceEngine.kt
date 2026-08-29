package com.example.ai

import com.example.data.SoltarSettingsEntity

data class ContextualRecommendation(
    val profileTypeDescription: String,
    val bannerMessage: String,
    val priorityToolTitle: String,
    val priorityToolDescription: String,
    val strategySummary: String,
    val recommendedFocus: List<String>,
    val contactCategory: String = "NONE"
)

object ContextualExperienceEngine {

    fun analyzeContext(settings: SoltarSettingsEntity?): ContextualRecommendation {
        if (settings == null) {
            return ContextualRecommendation(
                profileTypeDescription = "Perfil Estándar de Duelo",
                bannerMessage = "«Antes de convertir el impulso en una acción, detente.»",
                priorityToolTitle = "Modo Impulso (20 min)",
                priorityToolDescription = "Atraviesa el pico de dopamina sin contactar.",
                strategySummary = "Contacto cero estricto, regulación somática y pausa.",
                recommendedFocus = listOf("Regulación de impulso", "Antídoto de idealización", "Carta privada"),
                contactCategory = "NONE"
            )
        }

        val hasParental = settings.hasChildren || settings.contactType.contains("HIJOS", true)
        val hasCohabitation = settings.cohabitation || settings.contactType.contains("CONVIVENCIA", true)
        val hasWork = settings.practicals.contains("trabajo", true) || settings.practicals.contains("laboral", true) || settings.contactType.contains("TRABAJO", true)
        val hasPractical = settings.practicals.contains("negocio", true) || settings.practicals.contains("vivienda", true) || settings.practicals.contains("economia", true) || settings.practicals.contains("legal", true) || settings.practicals.contains("piso", true)
        
        val contactCount = listOf(hasParental, hasCohabitation, hasWork, hasPractical, settings.inevitableContact).count { it }
        
        val contactCategory = when {
            contactCount > 1 -> "MULTIPLE"
            hasParental -> "PARENTAL"
            hasCohabitation -> "COHABITATION"
            hasWork -> "WORK"
            hasPractical -> "PRACTICAL"
            settings.inevitableContact -> "OTHER"
            else -> "NONE"
        }

        val isInfidelity = settings.breakupReason.contains("infidelidad", true) || settings.breakupReason.contains("infidelity", true)
        val hasAnticipatedGrief = settings.anticipatedGrief.contains("si", true) || settings.anticipatedGrief.contains("meses", true) || settings.anticipatedGrief.contains("desgaste", true) || settings.anticipatedGrief.contains("decepcion", true)
        val isDecisionMakerSelf = settings.decisionMaker.contains("self", true) || settings.decisionMaker.contains("yo", true) || settings.decisionMaker.contains("fui", true) || settings.decisionMaker.contains("decidi", true)
        val isDecisionMakerMutual = settings.decisionMaker.contains("mutual", true) || settings.decisionMaker.contains("mutuo", true)
        val hasCycles = settings.previousBreakupsCount > 1
        val isLongRelation = settings.relDuration.contains("1_3_anios", true) || settings.relDuration.contains("3_5_anios", true) || settings.relDuration.contains("5_10_anios", true) || settings.relDuration.contains("mas", true) || settings.relDuration.contains("anos", true)

        return when {
            contactCategory == "PARENTAL" -> ContextualRecommendation(
                profileTypeDescription = "Perfil Parental (Hijos a Cargo)",
                bannerMessage = "«Contacto funcional con tus hijos + límites emocionales estrictos con tu expareja. La logística parental no justifica la cercanía afectiva.»",
                priorityToolTitle = "Simulacro de Encuentro (Comunicación Funcional Parental)",
                priorityToolDescription = "Practica separar los horarios, colegios y logística de los hijos de cualquier intento de validación emocional.",
                strategySummary = "Contacto Cero Adaptativo Parental: comunicación puramente funcional sobre los hijos, cero conversación sobre la relación pasada, establecimiento de canal escrito único.",
                recommendedFocus = listOf("Simulacro de encuentro", "Límites parentales", "Regulación pre/post comunicación"),
                contactCategory = "PARENTAL"
            )

            contactCategory == "WORK" -> ContextualRecommendation(
                profileTypeDescription = "Perfil con Entorno Laboral Compartido",
                bannerMessage = "«Entorno profesional compartido: profesionalidad aséptica, límites de horario y cero conversaciones informales o emocionales.»",
                priorityToolTitle = "Simulacro de Encuentro (Entorno Profesional)",
                priorityToolDescription = "Ensaya cómo mantener el trato estrictamente laboral y distante durante la jornada de trabajo.",
                strategySummary = "Contacto Cero Laboral: comunicación profesional indispensable, evitar pasillos o charlas no requeridas, separación radical entre el rol laboral y el vínculo afectivo.",
                recommendedFocus = listOf("Simulacro laboral", "Límites de comunicación profesional", "Modo Impulso"),
                contactCategory = "WORK"
            )

            contactCategory == "COHABITATION" -> ContextualRecommendation(
                profileTypeDescription = "Perfil con Convivencia Bajo el Mismo Techo",
                bannerMessage = "«No puedes aplicar contacto cero físico mientras compartes hogar. Distancia emocional, espacios delimitados y plan de salida.»",
                priorityToolTitle = "Simulacro de Convivencia Táctica",
                priorityToolDescription = "Organiza horarios, habitaciones separadas, pertenencias y la planificación temporal de la mudanza.",
                strategySummary = "Convivencia Táctica: minimización del tiempo compartido en casa, comunicación exclusivamente práctica, protección del espacio propio y ejecución del plan de salida.",
                recommendedFocus = listOf("Organización de espacios", "Límites prácticos", "Plan de salida temporal"),
                contactCategory = "COHABITATION"
            )

            contactCategory == "PRACTICAL" -> ContextualRecommendation(
                profileTypeDescription = "Perfil con Obligaciones Prácticas / Económicas",
                bannerMessage = "«Negocio, vivienda o trámites pendientes: contacto estrictamente acotado a la resolución legal y material.»",
                priorityToolTitle = "Auditoría de Obligaciones Prácticas",
                priorityToolDescription = "Separa la liquidación de compromisos materiales de cualquier ambigüedad afectiva.",
                strategySummary = "Contacto Práctico Acotado: canales y tiempos definidos exclusivamente para finiquitar obligaciones, prohibido el intercambio sobre el pasado.",
                recommendedFocus = listOf("Auditoría de la relación", "Límites materiales", "Carta privada"),
                contactCategory = "PRACTICAL"
            )

            isInfidelity -> ContextualRecommendation(
                profileTypeDescription = "Perfil con Ruptura por Infidelidad / Traición",
                bannerMessage = "«El dolor de la traición exige procesar la rabia, reconstruir tu valor personal y establecer límites inquebrantables sin diagnósticos infundados.»",
                priorityToolTitle = "Antídoto de Idealización y Límites",
                priorityToolDescription = "Trabaja el shock, desmonta la comparación y afirma tu dignidad inquebrantable.",
                strategySummary = "Validación de la traición y la pérdida de confianza, protección de la autoestima, cero auto-culpa, firmeza radical en los límites.",
                recommendedFocus = listOf("Antídoto de idealización", "Auditoría de la relación", "Laboratorio de pensamientos"),
                contactCategory = contactCategory
            )

            hasAnticipatedGrief -> ContextualRecommendation(
                profileTypeDescription = "Perfil con Duelo Anticipado (Desgaste prolongado)",
                bannerMessage = "«Parte de tu duelo ocurrió mientras aún estabas en la relación. Reconoce tu desgaste emocional acumulado.»",
                priorityToolTitle = "Diario Personal & Mentoría Filosófica",
                priorityToolDescription = "Procesa la ambivalencia y el agotamiento previo a la formalización de la separación.",
                strategySummary = "Reconocimiento del agotamiento emocional previo, validación de la ambivalencia, reconstrucción de la identidad propia.",
                recommendedFocus = listOf("Diario personal", "Laboratorio de pensamientos", "Metas de identidad"),
                contactCategory = contactCategory
            )

            hasCycles -> ContextualRecommendation(
                profileTypeDescription = "Perfil con Ciclo de Rupturas y Reconciliaciones Previas",
                bannerMessage = "«Este ciclo ya ocurrió antes. Analiza qué cambió realmente en las conductas frente a la repetición del patrón.»",
                priorityToolTitle = "Laboratorio de Pensamientos (Análisis de Patrón)",
                priorityToolDescription = "Revisa los detonantes de las reconciliaciones anteriores y distingue deseo de evidencia objetiva.",
                strategySummary = "Interrupción del ciclo de refuerzo intermitente, examen riguroso de hechos frente a promesas cíclicas.",
                recommendedFocus = listOf("Laboratorio de pensamientos", "Auditoría de patrones", "Modo Impulso"),
                contactCategory = contactCategory
            )

            isDecisionMakerMutual -> ContextualRecommendation(
                profileTypeDescription = "Perfil con Decisión Mutua de Ruptura",
                bannerMessage = "«Una ruptura acordada evita culpables, pero deja alta ambivalencia. Sostén la decisión conjunta sin buscar excusas para volver.»",
                priorityToolTitle = "Laboratorio de Pensamientos (Ambivalencia y Acuerdo)",
                priorityToolDescription = "Trabaja la aceptación de una decisión compartida y resiste la tentación de confundir cordialidad con reconciliación.",
                strategySummary = "Elaboración de la ambivalencia compartida, aceptación de límites, prevención de recaídas por nostalgia amigable.",
                recommendedFocus = listOf("Laboratorio de pensamientos", "Auditoría de la relación", "Metas de identidad"),
                contactCategory = contactCategory
            )

            isDecisionMakerSelf -> ContextualRecommendation(
                profileTypeDescription = "Perfil donde Tomaste la Decisión de Terminar",
                bannerMessage = "«Decidir marcharte también genera duelo y culpa. Sostén tu coherencia con compasión y firmeza.»",
                priorityToolTitle = "Auditoría de Responsabilidades y Coherencia",
                priorityToolDescription = "Examina los motivos legítimos de tu decisión sin caer en culpa punitiva.",
                strategySummary = "Elaboración de la culpa por tomar la iniciativa, validación de motivos, alineación con valores personales.",
                recommendedFocus = listOf("Auditoría de la relación", "Metas de identidad", "Carta privada"),
                contactCategory = contactCategory
            )

            isLongRelation -> ContextualRecommendation(
                profileTypeDescription = "Perfil con Relación de Larga Duración",
                bannerMessage = "«Una relación larga implica reestructurar rutinas, identidad compartida, proyectos y red social. Ten paciencia con tu proceso.»",
                priorityToolTitle = "Metas de Identidad y Reconstrucción",
                priorityToolDescription = "Reconecta con tus propios proyectos, hábitos independientes y red de apoyo.",
                strategySummary = "Reconfiguración de identidad post-vínculo prolongado, descentralización de la vida en común, creación de nuevos hábitos.",
                recommendedFocus = listOf("Metas de identidad", "Diario personal", "Cápsula del tiempo"),
                contactCategory = contactCategory
            )

            else -> ContextualRecommendation(
                profileTypeDescription = "Perfil de Duelo Estándar (Contacto Cero Estricto)",
                bannerMessage = "«Antes de convertir el impulso en una acción, detente y protege tu paz.»",
                priorityToolTitle = "Modo Impulso (20 min)",
                priorityToolDescription = "Atraviesa el pico neuroquímico sin romper el contacto cero.",
                strategySummary = "Contacto cero estricto, desactivación de rumiación, reconexión con la propia autonomía.",
                recommendedFocus = listOf("Modo Impulso", "Antídoto de idealización", "Cápsula del tiempo"),
                contactCategory = "NONE"
            )
        }
    }

    fun enrichUserContextForAi(settings: SoltarSettingsEntity?): SoltarUserContext {
        if (settings == null) return SoltarUserContext()
        return SoltarUserContext(
            relDuration = settings.relDuration,
            hasChildren = settings.hasChildren,
            contactType = settings.contactType,
            breakupSituation = settings.breakupSituation,
            practicals = settings.practicals,
            timeSinceBreakup = settings.timeSinceBreakup,
            previousBreakupsCount = settings.previousBreakupsCount,
            cohabitation = settings.cohabitation,
            marriedOrEngaged = settings.marriedOrEngaged,
            anticipatedGrief = settings.anticipatedGrief,
            parentalOnlyCommunication = settings.parentalOnlyCommunication,
            emotionalSituation = settings.emotionalSituation,
            decisionMaker = settings.decisionMaker,
            breakupReason = settings.breakupReason,
            freeHistoryNotes = settings.freeHistoryNotes
        )
    }
}
