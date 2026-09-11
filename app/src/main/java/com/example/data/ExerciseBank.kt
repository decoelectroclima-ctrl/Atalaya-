package com.example.data

enum class ExerciseCategory { CUERPO, PROYECTO_PROPIO, RED_SOCIAL }
enum class ExerciseIntensity { SUAVE, MODERADO, AMBICIOSO }

data class PracticalExercise(
    val id: String,
    val category: ExerciseCategory,
    val framework: SoltarFramework,
    val title: String,
    val instructions: String,
    val whyItHelps: String,
    val estimatedMinutes: Int,
    val intensity: ExerciseIntensity
)

object ExerciseBank {
    val exercises: List<PracticalExercise> = listOf(
        // ==========================================
        // CUERPO (ESTOICO)
        // ==========================================
        PracticalExercise(
            id = "cu_esto_1",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.ESTOICO,
            title = "Camina sin rumbo, sin móvil",
            instructions = "Sal a caminar 20 minutos sin destino fijo y sin el teléfono en la mano. Solo observa: el suelo bajo tus pies, el aire, los sonidos.",
            whyItHelps = "Los estoicos entrenaban la atención al presente como forma de dominar la mente. Caminar sin distracción es un ejercicio de disciplina sobre dónde pones tu atención.",
            estimatedMinutes = 20,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "cu_esto_2",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.ESTOICO,
            title = "Ducha de contraste térmico voluntario",
            instructions = "Termina tu ducha habitual con 60 a 90 segundos de agua completamente fría sobre nuca y pecho, manteniendo respiraciones lentas sin encoger los hombros.",
            whyItHelps = "La incomodidad física deliberada entrena el principio estoico del autodominio: enseña a tu sistema nervioso que puedes experimentar malestar sin perder la calma interna.",
            estimatedMinutes = 5,
            intensity = ExerciseIntensity.MODERADO
        ),
        PracticalExercise(
            id = "cu_esto_3",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.ESTOICO,
            title = "Postura de sobriedad y quietud deliberada",
            instructions = "Siéntate con la espalda recta, pies firmes en el suelo y manos en los muslos durante 10 minutos. Observa el impulso de inquietud corporal y mantén la quietud.",
            whyItHelps = "Séneca enseñaba que la postura externa educa el estado interior. La firmeza corporal proyecta soberanía y calma a los centros cerebrales de alerta.",
            estimatedMinutes = 10,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "cu_esto_4",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.ESTOICO,
            title = "Entrenamiento físico de esfuerzo sobrio",
            instructions = "Realiza 35 minutos de esfuerzo muscular continuo (flexiones, sentadillas o carrera a ritmo firme), resistiendo la fatiga mental sin emitir quejas internas.",
            whyItHelps = "Crisipo y Cleantes veían el esfuerzo corporal como un templado del alma. Agotar el cuerpo disipa la rumiación obsesiva y restaura la soberanía mental.",
            estimatedMinutes = 35,
            intensity = ExerciseIntensity.AMBICIOSO
        ),

        // ==========================================
        // CUERPO (PSICOLOGÍA MODERNA)
        // ==========================================
        PracticalExercise(
            id = "cu_psic_1",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "Regula tu sistema nervioso con respiración",
            instructions = "5 minutos de respiración 4-7-8: inhala 4 segundos, retén 7, exhala 8. Repite 6 veces con calma.",
            whyItHelps = "El dolor de una ruptura activa el sistema nervioso simpático (alerta/amenaza). La respiración lenta activa el parasimpático y baja el cortisol de forma medible.",
            estimatedMinutes = 5,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "cu_psic_2",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "Muévete hasta sudar, aunque sea poco",
            instructions = "20-30 minutos de ejercicio que te haga sudar: correr, bailar en tu cuarto, bici, lo que sea. No tiene que ser bonito, solo real.",
            whyItHelps = "El ejercicio libera BDNF y endorfinas que contrarrestan directamente los síntomas depresivos del duelo. Es de los pocos ejercicios con evidencia clínica tan fuerte como algunos tratamientos farmacológicos leves.",
            estimatedMinutes = 25,
            intensity = ExerciseIntensity.MODERADO
        ),
        PracticalExercise(
            id = "cu_psic_3",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "Relajación muscular progresiva somática",
            instructions = "Tensa durante 5 segundos y suelta abruptamente grupos musculares: pies, pantorrillas, abdomen, hombros y mandíbula, prestando atención al contraste del alivio.",
            whyItHelps = "El duelo post-ruptura retiene tensión muscular crónica inconsciente. Esta técnica de biofeedback somático envía señales de seguridad directa a la amígdala cerebral.",
            estimatedMinutes = 15,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "cu_psic_4",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "Circuito de alta intensidad y descarga emocional",
            instructions = "Completa 20 minutos con intervalos de alta intensidad: 45 segundos de saltos o sprint seguidos de 15 segundos de descanso activo, escuchando música vigorosa.",
            whyItHelps = "La estimulación cardiovascular intensa promueve la secreción de dopamina y noradrenalina, aliviando de forma aguda la sensación de pesadez o apatía.",
            estimatedMinutes = 20,
            intensity = ExerciseIntensity.AMBICIOSO
        ),

        // ==========================================
        // CUERPO (CATÓLICO)
        // ==========================================
        PracticalExercise(
            id = "cu_cat_1",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.CATOLICO,
            title = "Cuida tu cuerpo como templo",
            instructions = "Prepárate una comida de verdad, despacio, sin pantallas. Cocinar y comer con atención es un acto de cuidado hacia ti mismo.",
            whyItHelps = "\"¿No sabéis que sois templo de Dios?\" (1 Cor 3:16). Cuidar el cuerpo no es vanidad, es honrar lo que se te confió.",
            estimatedMinutes = 30,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "cu_cat_2",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.CATOLICO,
            title = "Caminata de alabanza y contemplación",
            instructions = "Camina 25 minutos al aire libre observando los detalles de la creación (el viento, los árboles, la luz) y agradece en silencio tres realidades vivas.",
            whyItHelps = "San Francisco de Asís enseñaba a reencontrar serenidad en las criaturas. Dirigir la mirada hacia la creación de Dios ensancha el corazón y alivia el pecho compungido.",
            estimatedMinutes = 25,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "cu_cat_3",
            category = ExerciseCategory.CUERPO,
            framework = SoltarFramework.CATOLICO,
            title = "Descanso reparador y entrega de la carga",
            instructions = "Apaga dispositivos electrónicos 45 minutos antes de dormir, estira el cuerpo con suavidad y entrega tus tensiones corporales en una oración de reposo.",
            whyItHelps = "\"En paz me acuesto y en seguida me duermo, porque solo tú, Señor, me haces vivir confiado\" (Salmo 4:8). El descanso respetuoso honra la salud otorgada por Dios.",
            estimatedMinutes = 40,
            intensity = ExerciseIntensity.MODERADO
        ),

        // ==========================================
        // PROYECTO PROPIO (ESTOICO)
        // ==========================================
        PracticalExercise(
            id = "pr_esto_1",
            category = ExerciseCategory.PROYECTO_PROPIO,
            framework = SoltarFramework.ESTOICO,
            title = "Dedica 30 minutos a lo que SÍ controlas",
            instructions = "Elige una tarea de tu proyecto personal (curso, trabajo, hobby) que dependa solo de ti, y dedícale 30 minutos sin interrupciones.",
            whyItHelps = "La dicotomía de control de Epicteto: la ruptura no depende de ti, pero tu proyecto sí. Invertir energía ahí es recuperar terreno propio.",
            estimatedMinutes = 30,
            intensity = ExerciseIntensity.MODERADO
        ),
        PracticalExercise(
            id = "pr_esto_2",
            category = ExerciseCategory.PROYECTO_PROPIO,
            framework = SoltarFramework.ESTOICO,
            title = "Auditoría de prioridades y virtudes",
            instructions = "Anota en papel las 3 prioridades fundamentales de tu vida independiente para los próximos 3 meses y elimina una distracción improductiva de tu día.",
            whyItHelps = "Marco Aurelio aconsejaba preguntarse ante cada asunto: \"¿Es esto imprescindible?\". Enfocar tus recursos cognitivos en lo esencial reconstruye tu dignidad.",
            estimatedMinutes = 15,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "pr_esto_3",
            category = ExerciseCategory.PROYECTO_PROPIO,
            framework = SoltarFramework.ESTOICO,
            title = "Bloque de trabajo profundo e inmersión intelectual",
            instructions = "Activa el modo no molestar y dedica 50 minutos exactos de trabajo exigente (código, redacción, análisis o estudio) sin abrir ninguna pestaña externa.",
            whyItHelps = "La consagración a una labor constructiva fortalece el intelecto contra la dispersión melancólica y devuelve el sentido de autorrealización personal.",
            estimatedMinutes = 50,
            intensity = ExerciseIntensity.AMBICIOSO
        ),

        // ==========================================
        // PROYECTO PROPIO (PSICOLOGÍA MODERNA)
        // ==========================================
        PracticalExercise(
            id = "pr_psic_1",
            category = ExerciseCategory.PROYECTO_PROPIO,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "Micro-meta de 15 minutos",
            instructions = "Elige la tarea más pequeña posible de algo que llevas posponiendo, y hazla en 15 minutos. Solo esa, nada más.",
            whyItHelps = "Tras una ruptura la motivación cae en picado. Las micro-metas reconstruyen la sensación de competencia y control sin exigir la energía que aún no tienes.",
            estimatedMinutes = 15,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "pr_psic_2",
            category = ExerciseCategory.PROYECTO_PROPIO,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "Clarificación de valores post-ruptura",
            instructions = "Define 3 valores esenciales que guían tu nueva etapa individual (ej. honestidad, creatividad, autonomía) y asigna un paso práctico hoy a uno de ellos.",
            whyItHelps = "La Terapia de Aceptación y Compromiso (ACT) demuestra que actuar conforme a valores genuinos repara la autoestima y previene el estancamiento depresivo.",
            estimatedMinutes = 20,
            intensity = ExerciseIntensity.MODERADO
        ),
        PracticalExercise(
            id = "pr_psic_3",
            category = ExerciseCategory.PROYECTO_PROPIO,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "Reactivar un proyecto personal postergado",
            instructions = "Dedica 45 minutos a diseñar el plan de acción de un proyecto propio (certificación, viaje, renovación del hogar) que pospusiste mientras estabas en pareja.",
            whyItHelps = "La activación conductual intencional rompe los patrones de indefensión aprendida y te posiciona de nuevo como artífice de tu futuro.",
            estimatedMinutes = 45,
            intensity = ExerciseIntensity.AMBICIOSO
        ),

        // ==========================================
        // PROYECTO PROPIO (CATÓLICO)
        // ==========================================
        PracticalExercise(
            id = "pr_cat_1",
            category = ExerciseCategory.PROYECTO_PROPIO,
            framework = SoltarFramework.CATOLICO,
            title = "Discierne tu vocación en este capítulo",
            instructions = "Escribe durante 10 minutos: ¿qué dones tienes que esta relación quizás dejó en pausa? ¿A qué te está llamando esta nueva etapa?",
            whyItHelps = "El discernimiento no es solo para grandes decisiones — cada etapa de la vida es una invitación a preguntarse para qué fuiste hecho.",
            estimatedMinutes = 10,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "pr_cat_2",
            category = ExerciseCategory.PROYECTO_PROPIO,
            framework = SoltarFramework.CATOLICO,
            title = "Ordenar el espacio de trabajo con propósito",
            instructions = "Dedica 25 minutos a despejar, ordenar y preparar tu lugar de trabajo o lectura como un espacio consagrado a obrar con excelencia.",
            whyItHelps = "El principio monástico \"Ora et labora\" enseña que el orden exterior dispone el ánimo para dar frutos y encontrar paz en la tarea cotidiana.",
            estimatedMinutes = 25,
            intensity = ExerciseIntensity.MODERADO
        ),
        PracticalExercise(
            id = "pr_cat_3",
            category = ExerciseCategory.PROYECTO_PROPIO,
            framework = SoltarFramework.CATOLICO,
            title = "Poner los talentos en marcha",
            instructions = "Dedica 35 minutos a avanzar de forma medible en una obra, estudio o iniciativa formativa que te permita poner tus dones al servicio de los demás.",
            whyItHelps = "La parábola de los talentos (Mateo 25:14-30) nos exhorta a no enterrar nuestras capacidades por miedo o pesadumbre, sino a hacerlas fecundas con esperanza.",
            estimatedMinutes = 35,
            intensity = ExerciseIntensity.AMBICIOSO
        ),

        // ==========================================
        // RED SOCIAL (ESTOICO)
        // ==========================================
        PracticalExercise(
            id = "re_esto_1",
            category = ExerciseCategory.RED_SOCIAL,
            framework = SoltarFramework.ESTOICO,
            title = "Busca compañía que te eleve, no que te compadezca",
            instructions = "Contacta a alguien cuya presencia te haga sentir más fuerte, no más victimizado. Propón quedar o llamar esta semana.",
            whyItHelps = "Séneca advertía sobre elegir bien las compañías: nos volvemos como aquellos con quienes pasamos tiempo. Busca a quien refleje quien quieres volver a ser.",
            estimatedMinutes = 10,
            intensity = ExerciseIntensity.MODERADO
        ),
        PracticalExercise(
            id = "re_esto_2",
            category = ExerciseCategory.RED_SOCIAL,
            framework = SoltarFramework.ESTOICO,
            title = "Gesto de amabilidad sin esperar retribución",
            instructions = "Envía un mensaje breve y sincero de agradecimiento o felicitación a un colega o conocido, sin buscar iniciar una charla extensa ni pedir nada a cambio.",
            whyItHelps = "Marco Aurelio sostenía que nacimos para colaborar. El ejercicio desinteresado de la benevolencia rompe la fijación ensimismada en nuestras propias penas.",
            estimatedMinutes = 5,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "re_esto_3",
            category = ExerciseCategory.RED_SOCIAL,
            framework = SoltarFramework.ESTOICO,
            title = "Conversación presencial sobria y con límites",
            instructions = "Comparte un café o paseo de 30 minutos con un amigo de confianza estableciendo con serenidad que hablarán de ideas, proyectos y vida presente, no de tu expareja.",
            whyItHelps = "Epicteto recordaba que proteger las fronteras de la propia mente es el primer deber de quien cultiva la libertad interior y el respeto propio.",
            estimatedMinutes = 30,
            intensity = ExerciseIntensity.AMBICIOSO
        ),

        // ==========================================
        // RED SOCIAL (PSICOLOGÍA MODERNA)
        // ==========================================
        PracticalExercise(
            id = "re_psic_1",
            category = ExerciseCategory.RED_SOCIAL,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "Un mensaje de vulnerabilidad real",
            instructions = "Envía un mensaje a alguien de confianza contándole de verdad cómo estás, sin minimizar ni un \"estoy bien\" automático.",
            whyItHelps = "El aislamiento agrava el duelo. La conexión social auténtica (no superficial) es uno de los predictores más fuertes de recuperación tras una ruptura.",
            estimatedMinutes = 5,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "re_psic_2",
            category = ExerciseCategory.RED_SOCIAL,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "Paseo o encuentro con tu red de apego seguro",
            instructions = "Coordina una caminata o café presencial de 40 minutos con una persona que sepa escuchar sin juzgar ni dar consejos apresurados.",
            whyItHelps = "La co-regulación del apego seguro mitiga la respuesta de dolor del córtex cingulado anterior generada por el rechazo o la separación afectiva.",
            estimatedMinutes = 40,
            intensity = ExerciseIntensity.MODERADO
        ),
        PracticalExercise(
            id = "re_psic_3",
            category = ExerciseCategory.RED_SOCIAL,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "Involucrarte en una actividad grupal presencial",
            instructions = "Asiste o inscríbete en una actividad grupal compartida (clase de deporte, taller cultural o voluntariado) para interactuar en un contexto neutral.",
            whyItHelps = "Expandir tu red social activa crea nuevas identidades compartidas, reduciendo la dependencia psicológica del antiguo vínculo relacional.",
            estimatedMinutes = 60,
            intensity = ExerciseIntensity.AMBICIOSO
        ),

        // ==========================================
        // RED SOCIAL (CATÓLICO)
        // ==========================================
        PracticalExercise(
            id = "re_cat_1",
            category = ExerciseCategory.RED_SOCIAL,
            framework = SoltarFramework.CATOLICO,
            title = "Sirve a alguien más hoy",
            instructions = "Haz un gesto concreto de servicio a otra persona hoy: ayuda con algo, escucha a alguien que lo necesite, ofrece tu tiempo.",
            whyItHelps = "Salir de uno mismo hacia el otro es, en la tradición cristiana, uno de los caminos más directos para sanar una herida propia.",
            estimatedMinutes = 20,
            intensity = ExerciseIntensity.MODERADO
        ),
        PracticalExercise(
            id = "re_cat_2",
            category = ExerciseCategory.RED_SOCIAL,
            framework = SoltarFramework.CATOLICO,
            title = "Oración de intercesión por quien sufre",
            instructions = "Dedica 10 minutos a rezar por alguien cercano que esté pasando por una dificultad de salud, familiar o anímica, encomendando su bienestar.",
            whyItHelps = "La intercesión fraterna transforma el propio dolor en canal de misericordia, despojándonos de la rumiación egoísta y reconectándonos espiritualmente.",
            estimatedMinutes = 10,
            intensity = ExerciseIntensity.SUAVE
        ),
        PracticalExercise(
            id = "re_cat_3",
            category = ExerciseCategory.RED_SOCIAL,
            framework = SoltarFramework.CATOLICO,
            title = "Visita o llamada fraterna de acompañamiento",
            instructions = "Llama o visita durante 30 minutos a un familiar anciano o conocido en situación de soledad, regalándole tu presencia y atención generosa.",
            whyItHelps = "\"Sobrellevad los unos las cargas de los otros\" (Gálatas 6:2). Consolar a los demás es una fuente profunda de sanación y restauración del alma.",
            estimatedMinutes = 30,
            intensity = ExerciseIntensity.AMBICIOSO
        )
    )

    fun suggest(
        category: ExerciseCategory,
        framework: SoltarFramework,
        maxIntensity: ExerciseIntensity,
        excludeIds: List<String>
    ): PracticalExercise? {
        val intensityOrder = listOf(ExerciseIntensity.SUAVE, ExerciseIntensity.MODERADO, ExerciseIntensity.AMBICIOSO)
        val maxIndex = intensityOrder.indexOf(maxIntensity)
        val pool = exercises.filter {
            it.category == category &&
            it.framework == framework &&
            intensityOrder.indexOf(it.intensity) <= maxIndex &&
            it.id !in excludeIds
        }
        val finalPool = if (pool.isEmpty()) exercises.filter { it.category == category && it.framework == framework } else pool
        return finalPool.randomOrNull()
    }

    fun getById(id: String): PracticalExercise? = exercises.firstOrNull { it.id == id }
}
