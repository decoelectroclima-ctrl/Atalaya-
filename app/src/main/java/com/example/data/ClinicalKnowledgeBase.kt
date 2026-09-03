package com.example.data

/**
 * Base de conocimiento clínico y marco de intervención reorganizado según la jerarquía rigurosa:
 * NIVEL 1: Evidencia científica (revisiones sistemáticas, metaanálisis, guías APA/WHO, apego, TCC, ACT, regulación emocional).
 * NIVEL 2: Modelos psicológicos (indicadores, contexto, hipótesis, intervención, herramienta, seguimiento).
 * NIVEL 3: Protocolos Recuerda (Problema → detección → contexto → hipótesis → intervención → herramienta → seguimiento).
 * NIVEL 4: Marcos de significado (lentes filosóficas y espirituales opcionales: Estoicismo, Humanismo/Existencialismo, Cristianismo).
 * NIVEL 5: Divulgación (referencias secundarias de lenguaje y perspectiva: Rolón, Congost, Rojas Estapé).
 *
 * REGLAS CLÍNICAS Y DE SEGURIDAD:
 * - Prohibido diagnosticar automáticamente al usuario o a terceros.
 * - Prohibido utilizar etiquetas como "narcisista", "manipulador", "evitativo" o "tóxico" como diagnósticos clínicos.
 * - Distinción obligatoria: Conducta observada → Interpretación posible → Hipótesis de trabajo → Incertidumbre.
 */
data class KnowledgeCapsule(
    val id: String,
    val framework: SoltarFramework,
    val category: String, // RUMIACION | IMPULSO | DUELO | AUTOESTIMA | LIMITES | SOLEDAD | CULPA | RECONSTRUCCION
    val title: String,
    val author: String,
    val quoteOrSource: String,
    val diagnosisPrinciple: String,
    val clinicalGuidance: String,
    val socraticPrompt: String,
    val concreteAction: String,
    // Atributos de la nueva Jerarquía de Conocimiento y Protocolos Recuerda
    val evidenceLevel: String = "Nivel 1: Evidencia científica consolidada (APA, Bowlby, TCC, ACT, Neff)",
    val psychologicalModel: String = "Modelo Cognitivo-Conductual y de Regulación de Apego Adulto",
    val adrianaProtocol: String = "Problema → Detección → Contexto → Hipótesis → Intervención → Herramienta → Seguimiento",
    val meaningLens: String = "Lente opcional de interpretación",
    val divulgationRef: String = "Referencia secundaria de divulgación"
)

object ClinicalKnowledgeBase {

    val capsules: List<KnowledgeCapsule> = listOf(
        // =========================================================================
        // MARCO ESTOICO (Nivel 4: Lente opcional de interpretación)
        // =========================================================================
        KnowledgeCapsule(
            id = "est_rum_1",
            framework = SoltarFramework.ESTOICO,
            category = "RUMIACION",
            title = "La Impresión Cataléptica y la Suspensión del Juicio",
            author = "Epicteto (Disertaciones I.27)",
            quoteOrSource = "«No te dejes arrebatar por la vivacidad de la impresión; dile: 'Espera un momento; déjame ver quién eres y qué representas. Déjame ponerte a prueba'.»",
            diagnosisPrinciple = "La mente en duelo proyecta escenarios de catástrofe y duda que confunde con certezas.",
            clinicalGuidance = "Interpón un intervalo entre la primera impresión (phantasia) y el asentimiento racional (synkatathesis). Lo que la otra persona piensa o siente es un indiferente externo (adiaphoron).",
            socraticPrompt = "¿Estás sufriendo por un hecho demostrado o por la historia que tu imaginación ha fabricado sobre ese hecho?",
            concreteAction = "Escribe en dos columnas separadas: 'Hechos verificables con testigos' vs 'Interpretaciones mías'.",
            meaningLens = "Lente Estoica (Marco de Significado Opcional)",
            divulgationRef = "Filosofía clásica grecorromana"
        ),
        KnowledgeCapsule(
            id = "est_rum_2",
            framework = SoltarFramework.ESTOICO,
            category = "RUMIACION",
            title = "Borrar la Vana Imaginación",
            author = "Marco Aurelio (Meditaciones VII.29)",
            quoteOrSource = "«Borra la vana imaginación; frena el impulso; apaga el apetito; mantén en tu poder la facultad rectora (hegemonikón).»",
            diagnosisPrinciple = "El bucle obsesivo se alimenta cuando la atención se enfoca en el pasado irrecuperable.",
            clinicalGuidance = "El principio rector (hegemonikón) es el único soberano de tu mente. Permitir que la memoria de un desaire gobierne tus horas es entregar tu soberanía a quien ya no está.",
            socraticPrompt = "Si esta hora fuera la última de tu día, ¿desearías haberla invertido analizando silencios ajenos?",
            concreteAction = "Pausa la rumiación de inmediato y realiza 10 respiraciones profundas contando 4 segundos al inhalar y 6 al exhalar.",
            meaningLens = "Lente Estoica (Marco de Significado Opcional)",
            divulgationRef = "Filosofía clásica grecorromana"
        ),
        KnowledgeCapsule(
            id = "est_imp_1",
            framework = SoltarFramework.ESTOICO,
            category = "IMPULSO",
            title = "La Dicotomía del Control en la Comunicación",
            author = "Epicteto (Enquiridión I)",
            quoteOrSource = "«De las cosas que existen, unas dependen de nosotros y otras no. De nosotros dependen el juicio, el impulso, el deseo y la aversión... No dependen el cuerpo, la reputación, los afectos ni las decisiones de los otros.»",
            diagnosisPrinciple = "El deseo de escribir surge de la ilusión de poder cambiar la mente de la otra persona.",
            clinicalGuidance = "Enviar un mensaje busca influir en una voluntad ajena (incontrolable) a costa de tu templanza (controlable). Cada vez que cedes al impulso, debilitas tu disciplina interior.",
            socraticPrompt = "Si envías ese mensaje y recibes indiferencia o rechazo, ¿cómo quedará tu tranquilidad comparada con la de ahora?",
            concreteAction = "Aplica la regla de las 24 horas: no tomes ninguna acción comunicativa hasta que el sol vuelva a salir.",
            meaningLens = "Lente Estoica (Marco de Significado Opcional)",
            divulgationRef = "Filosofía clásica grecorromana"
        ),
        KnowledgeCapsule(
            id = "est_due_1",
            framework = SoltarFramework.ESTOICO,
            category = "DUELO",
            title = "Amor Fati y la Transitoriedad de los Bienes Prestados",
            author = "Epicteto (Enquiridión XI)",
            quoteOrSource = "«Nunca digas de nada: 'Lo he perdido', sino 'Lo he devuelto'. ¿Murió tu vínculo? Ha sido restituido a la naturaleza.»",
            diagnosisPrinciple = "El sufrimiento se agrava cuando consideramos a las personas o relaciones como posesiones eternas.",
            clinicalGuidance = "Las relaciones son préstamos de la existencia, no propiedades. Agradecer lo vivido sin exigir su permanencia forzosa es la esencia de la madurez filosófica y del Amor Fati.",
            socraticPrompt = "¿Puedes agradecer la enseñanza de ese ciclo sin pretender retener lo que ya caducó?",
            concreteAction = "Anota tres aprendizajes de carácter que esta dificultad te está obligando a forjar.",
            meaningLens = "Lente Estoica (Marco de Significado Opcional)",
            divulgationRef = "Filosofía clásica grecorromana"
        ),

        // =========================================================================
        // MARCO PSICOLOGÍA MODERNA (Nivel 1: Evidencia científica & Nivel 2: Modelos Psicológicos)
        // =========================================================================
        KnowledgeCapsule(
            id = "psi_apg_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "IMPULSO",
            title = "Neurobiología del Apego y Síndrome de Abstinencia",
            author = "Teoría del Apego (John Bowlby, Mary Ainsworth & Helen Fisher)",
            quoteOrSource = "«La protesta por la separación es una respuesta biológica evolutiva de alarma ante la pérdida de la figura vincular; el cerebro la procesa en las mismas áreas que el dolor físico.»",
            diagnosisPrinciple = "La urgencia desesperada de llamar o buscar al ex se siente como 'amor verdadero' cuando es abstinencia dopaminérgica y oxitocínica.",
            clinicalGuidance = "Tu sistema nervioso simpático está hiperactivado buscando el ansiolítico conocido (la otra persona). Comprender la química desmitifica el impulso: no es una señal cósmica de destino, es neurobiología adaptativa en desintoxicación.",
            socraticPrompt = "Si reconoces que este ardor en el pecho es abstinencia química de tu sistema de apego, ¿puedes cuidarte como a alguien convaleciente?",
            concreteAction = "Aplica el protocolo Somático TIPP: sumerge la cara en agua fría o colócate hielo en la nuca durante 30 segundos para activar el reflejo de buceo vagal.",
            evidenceLevel = "Nivel 1: Revisiones sistemáticas en neurobiología del apego y neuroimagen del desamor (Fisher et al., 2010)",
            psychologicalModel = "Modelo Biopsicosocial del Apego y Regulación Somática del Sistema Nervioso",
            adrianaProtocol = "Craving Relacional → Detección de alerta simpática → Contexto de abstinencia → Hipótesis química vs afectiva → Intervención TIPP → Modo Impulso → Reevaluación a 20 min",
            meaningLens = "Lente de la Psicología Científica y Neurobiología",
            divulgationRef = "Amir Levine (Maneras de Amar)"
        ),
        KnowledgeCapsule(
            id = "psi_apg_2",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "IMPULSO",
            title = "La Trampa del Refuerzo Intermitente",
            author = "Psicología Conductual & Teoría del Aprendizaje",
            quoteOrSource = "«El cerebro se vuelve adicto a la incertidumbre: la recompensa impredecible dispara la mayor liberación de dopamina, confundiendo ansiedad con pasión.»",
            diagnosisPrinciple = "Interpretar las señales tibias, likes esporádicos o mensajes ambiguos como esperanza de futuro.",
            clinicalGuidance = "El refuerzo intermitente genera un apego ansioso disfuncional. La inconsistencia de la otra persona no es misterio seductor, es falta de compromiso y desinterés real.",
            socraticPrompt = "¿Estás dispuesto/a a aceptar migajas intermitentes a cambio de tu tranquilidad diaria?",
            concreteAction = "Silencia o bloquea notificaciones de redes para interrumpir el circuito de recompensa intermitente.",
            evidenceLevel = "Nivel 1: Psicología Experimental del Aprendizaje y Condicionamiento Operante",
            psychologicalModel = "Modelo Conductual de Refuerzo Variable y Extinción de Respuesta",
            adrianaProtocol = "Estímulo ambiguo → Detección de craving → Contexto de refuerzo intermitente → Hipótesis de inconsistencia → Intervención de corte digital → Bloqueo/Silenciamiento → Seguimiento",
            meaningLens = "Lente Conductual Científica",
            divulgationRef = "Silvia Congost (Autoestima y Dependencia)"
        ),
        KnowledgeCapsule(
            id = "psi_lim_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "LIMITES",
            title = "Límites Protectores y Prevención de Dependencia",
            author = "Psicología Clínica Basada en Evidencia",
            quoteOrSource = "«El contacto cero no es una estrategia para que el otro vuelva ni para castigarle; es el quirófano aséptico que tú necesitas para desinflamar la herida.»",
            diagnosisPrinciple = "Mantener contacto 'cordial' o revisar redes mantiene encendida la llama de la esperanza tóxica y el sufrimiento.",
            clinicalGuidance = "Diferencia con nitidez: el amor suma bienestar, la dependencia usa al otro para calmar el vacío existencial. Cortar los estímulos visuales y comunicativos es el acto de mayor salud mental.",
            socraticPrompt = "¿Estás dispuesto a soportar la incomodidad temporal de la soledad para ganar la libertad definitiva de tu vida?",
            concreteAction = "Elimina aplicaciones de rastreo, archiva conversaciones y guarda fotos en una carpeta oculta o disco externo inaccesible en el día a día.",
            evidenceLevel = "Nivel 1: Guías clínicas de deshabituación y manejo de duelos complicados",
            psychologicalModel = "Modelo de Regulación de Estímulos y Aceptación Contextual",
            adrianaProtocol = "Impulso de contacto → Detección de estímulo visual → Contexto de vulnerabilidad → Hipótesis de recaída → Intervención de higiene digital → Contacto Cero Estricto o Adaptativo → Seguimiento",
            meaningLens = "Lente Psicoterapéutica Contemporánea",
            divulgationRef = "Silvia Congost"
        ),
        KnowledgeCapsule(
            id = "psi_act_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "RUMIACION",
            title = "Defusión Cognitiva y Valores Vitales",
            author = "Terapia de Aceptación y Compromiso (ACT - Steven Hayes)",
            quoteOrSource = "«Los pensamientos son solo eventos mentales pasajeros, como hojas en un río; no son órdenes que debas acatar ni hechos que debas resolver.»",
            diagnosisPrinciple = "Fusionarse con pensamientos como 'nunca encontraré a nadie' o 'arruiné mi única oportunidad'.",
            clinicalGuidance = "Practica la defusión: antepone 'Noto que mi mente está produciendo el pensamiento de que...' Esto crea un espacio de observación que reduce la reactividad emocional y te devuelve al volante de tus valores.",
            socraticPrompt = "¿Qué valor personal (dignidad, salud, generosidad, proyectos) quieres honrar hoy con tu comportamiento?",
            concreteAction = "Elige una acción coherente con tus valores (ej. entrenar, cocinar sano, leer) y realízala a pesar del desánimo mental.",
            evidenceLevel = "Nivel 1: Ensayos clínicos controlados de ACT en rumiación y trastornos afectivos (Hayes et al., 2013)",
            psychologicalModel = "Modelo de Flexibilidad Psicológica y Hexaflex ACT",
            adrianaProtocol = "Pensamiento intrusivo → Detección de fusión cognitiva → Contexto de rumiación → Hipótesis de no-equivalencia pensamiento-hecho → Intervención de defusión → Laboratorio de Pensamientos → Reevaluación",
            meaningLens = "Lente de Contextual Behavioral Science",
            divulgationRef = "Steven Hayes"
        ),
        KnowledgeCapsule(
            id = "psi_due_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "DUELO",
            title = "El Modelo de Proceso Dual del Duelo",
            author = "Stroebe & Schut (Modelo de Proceso Dual)",
            quoteOrSource = "«El duelo saludable oscila naturalmente entre la orientación a la pérdida (sentir y llorar) y la orientación a la restauración (construir nuevos roles y rutinas).»",
            diagnosisPrinciple = "La resistencia a sentir tristeza lleva a conductas impulsivas, relaciones rebote o negación.",
            clinicalGuidance = "Llorar y nombrar la falta no es debilidad, es el trabajo psíquico indispensable para desinvestir la libido puesta en el vínculo. El duelo no es lineal: oscila de forma completamente normal.",
            socraticPrompt = "¿Te estás permitiendo despedir la relación con la tristeza digna que merece, o estás huyendo de ella?",
            concreteAction = "Escribe una carta de despedida honesta en tu diario personal (sin enviarla jamás) agradeciendo lo bueno y reconociendo el final definitivo.",
            evidenceLevel = "Nivel 1: Revisiones sistemáticas de psicología del duelo y adaptación (Stroebe & Schut, 1999/2010)",
            psychologicalModel = "Modelo de Proceso Dual de Afrontamiento del Duelo (Loss-Oriented & Restoration-Oriented)",
            adrianaProtocol = "Dolor agudo → Detección de evitación o rumiación → Contexto de pérdida → Hipótesis de oscilación natural → Intervención de expresión y diario → Diario Personal → Seguimiento evolutivo",
            meaningLens = "Lente Científica del Duelo",
            divulgationRef = "Gabriel Rolón"
        ),
        KnowledgeCapsule(
            id = "psi_neu_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "SOLEDAD",
            title = "Regulación de Cortisol y Eje HHA en Estrés Post-Ruptura",
            author = "Neurobiología del Estrés y Psiconeuroendocrinología",
            quoteOrSource = "«Vivir en constante alerta por una expareja mantiene el cortisol elevado, alterando el sueño, la digestión y la capacidad de pensar con claridad.»",
            diagnosisPrinciple = "La incertidumbre prolongada desgasta la fisiología provocando fatiga crónica y desregulación emocional.",
            clinicalGuidance = "Para reparar la mente hay que reparar el cuerpo: regular la exposición a la luz solar matutina, reducir pantallas nocturnas, asegurar proteína y movimiento físico constante para bajar la carga alostática.",
            socraticPrompt = "¿Cómo has tratado a tu cuerpo en las últimas 24 horas a nivel de sueño, comida y movimiento?",
            concreteAction = "Sal a caminar 20 minutos bajo la luz natural del día sin audífonos ni distracciones.",
            evidenceLevel = "Nivel 1: Estudios de neuroendocrinología del estrés crónico y carga alostática",
            psychologicalModel = "Modelo Biológico del Eje Hipotalámico-Pituitario-Adrenal (HHA)",
            adrianaProtocol = "Fatiga o hiperalerta → Detección somática → Contexto de estrés crónico → Hipótesis de sobrecarga alostática → Intervención de higiene circadiana y caminata → Check-in somático → Reevaluación",
            meaningLens = "Lente Neurobiológica",
            divulgationRef = "Marian Rojas Estapé (Cómo hacer que te pasen cosas buenas)"
        ),

        // =========================================================================
        // MARCO CATÓLICO (Nivel 4: Lente opcional de significado y fe)
        // =========================================================================
        KnowledgeCapsule(
            id = "cat_cor_1",
            framework = SoltarFramework.CATOLICO,
            category = "LIMITES",
            title = "La Custodia del Corazón y el Templo Interior",
            author = "Sabiduría Bíblica (Proverbios 4:23)",
            quoteOrSource = "«Por encima de todo lo que guardes, guarda tu corazón, porque de él brota la vida.»",
            diagnosisPrinciple = "Permitir que afectos desordenados, humillaciones o insistencias ciegas marchiten la dignidad del alma.",
            clinicalGuidance = "Tu corazón es sagrado y ha sido creado para la plenitud y la verdad, no para la mendicidad afectiva ni el desprecio. Protegerlo con prudencia (contacto cero y distancia) no es rencor, es reverencia y cuidado de lo que Dios te ha confiado.",
            socraticPrompt = "¿Cuidar tu corazón hoy te acerca más a la paz y a la vocación que Dios soñó para ti?",
            concreteAction = "Haz una pausa de oración en silencio: pon tus manos sobre el pecho y entrega tu dolor pidiendo gracia para custodiar tu paz.",
            meaningLens = "Lente Espiritual Cristiana (Marco de Significado Opcional)",
            divulgationRef = "Tradición patrística y espiritual cristiana"
        ),
        KnowledgeCapsule(
            id = "cat_tie_1",
            framework = SoltarFramework.CATOLICO,
            category = "DUELO",
            title = "El Tiempo Oportuno y los Tiempos de Providencia",
            author = "Eclesiastés (Qohélet 3:1-6)",
            quoteOrSource = "«Todo tiene su momento oportuno; hay un tiempo para todo lo que se hace bajo el cielo: tiempo de abrazar y tiempo de abstenerse de abrazar; tiempo de buscar y tiempo de dar por perdido.»",
            diagnosisPrinciple = "Luchar con desesperación contra el fin de una etapa en lugar de aceptar la estación espiritual del desierto.",
            clinicalGuidance = "El desierto no es un castigo, es el lugar de purificación donde se aprende a no idolatrar a las criaturas y a descubrir la roca inconmovible del Creador. Hay temporadas de soltar para poder recibir lo que está por venir.",
            socraticPrompt = "¿Puedes reconocer con humildad que esta estación de tu vida requiere soltar para madurar?",
            concreteAction = "Lee con calma el capítulo 3 de Eclesiastés y medita en el valor del silencio regenerador.",
            meaningLens = "Lente Espiritual Cristiana (Marco de Significado Opcional)",
            divulgationRef = "Sabiduría Bíblica"
        ),
        KnowledgeCapsule(
            id = "cat_ign_1",
            framework = SoltarFramework.CATOLICO,
            category = "IMPULSO",
            title = "No Hacer Mudanza en Tiempo de Desolación",
            author = "San Ignacio de Loyola (Ejercicios Espirituales, Regla 5)",
            quoteOrSource = "«En tiempo de desolación nunca hacer mudanza, mas estar firme y constante en los propósitos anteriores.»",
            diagnosisPrinciple = "Tomar decisiones impulsivas (escribir, llamar, rogar) cuando el alma está turbada o angustiada.",
            clinicalGuidance = "Cuando hay dolor y confusión, las pasiones buscan que rompas tus propósitos de dignidad. Mantente firme en tu decisión previa de contacto cero hasta que retorne la luz y la paz.",
            socraticPrompt = "¿Vas a quebrar una decisión tomada en calma solo porque hoy arrecia la tormenta?",
            concreteAction = "Reza el Salmo 91 o haz 5 minutos de silencio de entrega pidiendo fidelidad a tus propósitos.",
            meaningLens = "Lente Espiritual Cristiana (Marco de Significado Opcional)",
            divulgationRef = "Espiritualidad Ignaciana"
        ),
        KnowledgeCapsule(
            id = "cat_hope_1",
            framework = SoltarFramework.CATOLICO,
            category = "DUELO",
            title = "La Renovación en la Esperanza",
            author = "Lamentaciones 3:22-24",
            quoteOrSource = "«El gran amor del Señor nunca se acaba, y su compasión jamás se agota. Cada mañana se renuevan sus bondades; ¡muy grande es su fidelidad!»",
            diagnosisPrinciple = "Sentir que el duelo es un pozo sin fondo y que el futuro ha perdido su color.",
            clinicalGuidance = "El duelo es una travesía, no un destino final. La fidelidad de Dios se experimenta en la capacidad de empezar de nuevo cada mañana, confiando en que el consuelo llegará conforme tu corazón se disponga a recibirlo.",
            socraticPrompt = "¿Qué pequeña bondad has podido identificar hoy a pesar de tu dolor?",
            concreteAction = "Al despertar, agradece tres pequeñas cosas que te dan vida hoy antes de revisar el móvil.",
            meaningLens = "Lente Espiritual Cristiana (Marco de Significado Opcional)",
            divulgationRef = "Sabiduría Bíblica"
        ),
        KnowledgeCapsule(
            id = "cat_peace_1",
            framework = SoltarFramework.CATOLICO,
            category = "RUMIACION",
            title = "La Paz que sobrepasa el Entendimiento",
            author = "Filipenses 4:6-7",
            quoteOrSource = "«No se inquieten por nada; más bien, en toda ocasión, con oración y ruego, presenten sus peticiones a Dios y denle gracias. Y la paz de Dios, que sobrepasa todo entendimiento, cuidará sus corazones.»",
            diagnosisPrinciple = "La ansiedad intentando resolver y entender obsesivamente las causas de la ruptura.",
            clinicalGuidance = "La paz de Dios no es una respuesta lógica a tu problema, es un refugio que protege tu interior de la desintegración emocional causada por la rumiación ansiosa.",
            socraticPrompt = "¿Puedes entregarle a Dios la necesidad de 'entender' lo que no está bajo tu control hoy?",
            concreteAction = "Realiza la oración de entrega: 'Señor, te entrego mi necesidad de controlar esto, dame tu paz que sobrepasa mi entendimiento'.",
            meaningLens = "Lente Espiritual Cristiana (Marco de Significado Opcional)",
            divulgationRef = "Sabiduría Bíblica"
        )
    )

    fun findRelevantCapsule(
        input: String,
        framework: SoltarFramework,
        category: String? = null
    ): KnowledgeCapsule {
        val lower = input.lowercase()
        val frameworkCapsules = capsules.filter { it.framework == framework }
        
        if (category != null) {
            val byCat = frameworkCapsules.filter { it.category.equals(category, ignoreCase = true) }
            if (byCat.isNotEmpty()) return byCat.random()
        }

        val targetCategory = when {
            lower.contains("escribir") || lower.contains("llamar") || lower.contains("mensaje") || lower.contains("impulso") || lower.contains("buscarlo") || lower.contains("buscarla") || lower.contains("desesperad") -> "IMPULSO"
            lower.contains("por qué") || lower.contains("descifrar") || lower.contains("pensando") || lower.contains("analizar") || lower.contains("bucle") || lower.contains("rumi") -> "RUMIACION"
            lower.contains("idealiz") || lower.contains("perfecto") || lower.contains("único") || lower.contains("nadie como") || lower.contains("extraño") -> "RUMIACION"
            lower.contains("límite") || lower.contains("contacto cero") || lower.contains("bloque") || lower.contains("redes") || lower.contains("ver su foto") -> "LIMITES"
            lower.contains("triste") || lower.contains("llor") || lower.contains("duele") || lower.contains("duelo") || lower.contains("pérdida") || lower.contains("nostalgia") -> "DUELO"
            lower.contains("culpa") || lower.contains("perdón") || lower.contains("rencor") || lower.contains("odio") || lower.contains("rabia") -> "CULPA"
            lower.contains("soledad") || lower.contains("solo") || lower.contains("sola") || lower.contains("vacío") || lower.contains("desamparo") -> "SOLEDAD"
            lower.contains("autoestima") || lower.contains("no valgo") || lower.contains("inútil") || lower.contains("rechazo") || lower.contains("vergüenza") -> "AUTOESTIMA"
            else -> "RECONSTRUCCION"
        }

        val matched = frameworkCapsules.filter { it.category == targetCategory }
        if (matched.isNotEmpty()) return matched.random()

        return frameworkCapsules.firstOrNull() ?: capsules.first()
    }
}
