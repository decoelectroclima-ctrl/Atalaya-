package com.example.data

enum class SoltarFramework(
    val key: String,
    val title: String,
    val description: String,
    val badge: String,
    val focusLabel: String
) {
    ESTOICO(
        key = "ESTOICO",
        title = "Estoico",
        description = "Claridad y disciplina interior. Marco Aurelio, Epicteto, Séneca.",
        badge = "FILOSOFÍA ESTOICA",
        focusLabel = "Enfoque Estoico"
    ),
    PSICOLOGIA_MODERNA(
        key = "PSICOLOGIA_MODERNA",
        title = "Psicología moderna",
        description = "Comprensión emocional y vínculos. Enfoque terapéutico contemporáneo.",
        badge = "PSICOLOGÍA Y APEGO",
        focusLabel = "Enfoque Psicológico"
    ),
    CATOLICO(
        key = "CATOLICO",
        title = "Católico",
        description = "Fe, esperanza y sabiduría bíblica como acompañamiento.",
        badge = "FE Y ESPERANZA",
        focusLabel = "Enfoque de Fe y Sabiduría"
    );

    companion object {
        fun fromKey(key: String?): SoltarFramework {
            return entries.firstOrNull { it.key.equals(key, ignoreCase = true) } ?: PSICOLOGIA_MODERNA
        }
    }
}

enum class UserGender(
    val key: String,
    val title: String,
    val emoji: String,
    val subtitle: String,
    val clinicalGuidance: String,
    val description: String = subtitle
) {
    MAN(
        key = "MAN",
        title = "Hombre",
        emoji = "👨",
        subtitle = "Enfoque adaptado a la psicología, dinámicas y desapego masculino.",
        clinicalGuidance = "El usuario es hombre. Utiliza concordancia gramatical masculina en español (ej. 'fuerte, centrado, enfocado, preparado, soberano, dueño de tus decisiones'). Aborda los patrones de rumiación, orgullo herido, impulsos de rescate/proveedor y canalización de energía (Modo Guerra, disciplina física y mental, respeto inquebrantable a su propio valor, cero mendicidad)."
    ),
    WOMAN(
        key = "WOMAN",
        title = "Mujer",
        emoji = "👩",
        subtitle = "Enfoque adaptado a la psicología, dinámicas y desapego femenino.",
        clinicalGuidance = "La usuaria es mujer. Utiliza concordancia gramatical femenina en español (ej. 'fuerte, centrada, enfocada, preparada, soberana, dueña de tus decisiones'). Aborda los patrones de hiperresponsabilidad afectiva, culpa internalizada, idealización del potencial del otro, desmantelamiento de la sumisión y recuperación de su trono y autonomía emocional."
    ),
    NOT_SPECIFIED(
        key = "NOT_SPECIFIED",
        title = "Sin especificar",
        emoji = "⚪",
        subtitle = "Acompañamiento neutral y universal.",
        clinicalGuidance = "El usuario no ha especificado su género. Emplea un lenguaje respetuoso, equilibrado y adaptable."
    );

    companion object {
        fun fromKey(key: String?): UserGender {
            return entries.firstOrNull { it.key.equals(key, ignoreCase = true) } ?: NOT_SPECIFIED
        }
    }
}

data class WisdomCard(
    val id: String,
    val framework: SoltarFramework,
    val title: String,
    val quote: String,
    val author: String,
    val reflection: String
)

object WisdomBank {

    val cards: List<WisdomCard> = listOf(
        // ==========================================
        // 1. ESTOICO (Marco Aurelio, Epicteto, Séneca - Dominio público / Paráfrasis)
        // ==========================================
        WisdomCard(
            id = "est_1",
            framework = SoltarFramework.ESTOICO,
            title = "LA DICOTOMÍA DEL CONTROL",
            quote = "«No tienes poder sobre las acciones o los afectos ajenos; pero tienes soberanía absoluta sobre tu silencio, tus pasos y tu propia dignidad.»",
            author = "Epicteto (Enquiridión)",
            reflection = "Cuando sientas la tentación de buscar una explicación, recuerda: lo que la otra persona decide está fuera de tu ciudadela interior."
        ),
        WisdomCard(
            id = "est_2",
            framework = SoltarFramework.ESTOICO,
            title = "EL RETIRO EN UNO MISMO",
            quote = "«En ningún lugar puede un ser humano encontrar un retiro más apacible y libre de turbaciones que en su propia alma.»",
            author = "Marco Aurelio (Meditaciones)",
            reflection = "No busques calmar tu tormenta acudiendo a quien la provocó. El único refugio seguro hoy eres tú mismo/a."
        ),
        WisdomCard(
            id = "est_3",
            framework = SoltarFramework.ESTOICO,
            title = "EL DOLOR DE LA IMAGINACIÓN",
            quote = "«A menudo sufrimos más por lo que imaginamos que por lo que realmente sucede en la realidad.»",
            author = "Séneca (Cartas a Lucilio)",
            reflection = "La mente inventa historias sobre el pasado y el futuro. Vuelve a los hechos observables del día presente."
        ),
        WisdomCard(
            id = "est_4",
            framework = SoltarFramework.ESTOICO,
            title = "ACEPTACIÓN DE LO INEVITABLE",
            quote = "«No pretendas que los sucesos ocurran como tú deseas; desea más bien que ocurran como suceden y tu vida transcurrirá en paz.»",
            author = "Epicteto (Disertaciones)",
            reflection = "Dejar de luchar contra el final de la relación es el primer paso para dejar de sufrir en vano."
        ),
        WisdomCard(
            id = "est_5",
            framework = SoltarFramework.ESTOICO,
            title = "EL VALOR DEL TIEMPO PROPIO",
            quote = "«No es que tengamos poco tiempo, sino que perdemos mucho insistiendo en lo que ya no nos pertenece.»",
            author = "Séneca (De la brevedad de la vida)",
            reflection = "Cada minuto que no dedicas a perseguir el pasado es un minuto ganado para reconstruir tu presente."
        ),
        WisdomCard(
            id = "est_6",
            framework = SoltarFramework.ESTOICO,
            title = "LA CIUDADELA INCONMOVIBLE",
            quote = "«Borra la vana imaginación; frena el impulso desmedido; apaga el deseo ciego; mantén tu principio rector bajo tu propio mando.»",
            author = "Marco Aurelio (Meditaciones)",
            reflection = "El impulso de escribir pasará si no lo alimentas. Tu dignidad no negocia con la urgencia del momento."
        ),
        WisdomCard(
            id = "est_7",
            framework = SoltarFramework.ESTOICO,
            title = "FORJAR EL CARÁCTER",
            quote = "«El fuego prueba al oro; la adversidad forja a las personas de temple.»",
            author = "Séneca (De la providencia)",
            reflection = "Esta ruptura no te destruye: es la forja donde estás aprendiendo a no volver a mendigar atención."
        ),
        WisdomCard(
            id = "est_8",
            framework = SoltarFramework.ESTOICO,
            title = "FIRMEZA ANTE EL IMPULSO",
            quote = "«Si alguien te agravia o se aleja, recuerda que el daño solo reside en el juicio que tú hagas de ello.»",
            author = "Marco Aurelio (Meditaciones)",
            reflection = "El silencio ajeno no define tu valor. Tu valor se demuestra en cómo te sostienes ante ese silencio."
        ),

        // ==========================================
        // 2. PSICOLOGÍA MODERNA (Apego, duelo y responsabilidad afectiva - Rolón, Bowlby, Congost)
        // ==========================================
        WisdomCard(
            id = "psi_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "EL DUELO COMO TRAVESÍA",
            quote = "«El dolor de una pérdida no se esquiva ni se tapa: se atraviesa con paciencia. No hay atajos para reconstruir un corazón que amó de verdad.»",
            author = "Inspirado en el enfoque de Gabriel Rolón",
            reflection = "Sentir tristeza o extrañar no significa que debas retroceder. Es el costo natural de elaborar una despedida."
        ),
        WisdomCard(
            id = "psi_2",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "EL SISTEMA DE APEGO",
            quote = "«La necesidad compulsiva de buscar contacto es una alarma biológica de desamparo, no una prueba de amor predestinado.»",
            author = "Inspirado en la teoría del apego (Bowlby)",
            reflection = "Tu cerebro busca calmar la abstinencia química. Reconoce la alarma en tu cuerpo sin obedecer la orden de escribir."
        ),
        WisdomCard(
            id = "psi_3",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "LÍMITES Y AUTOESTIMA",
            quote = "«Querer a alguien nunca debe costar la renuncia a uno mismo. Donde no hay reciprocidad, quedarse es una forma de abandono propio.»",
            author = "Inspirado en el enfoque de Silvia Congost",
            reflection = "El contacto cero es el límite protector que tu dignidad necesita para no volver a desgastarse."
        ),
        WisdomCard(
            id = "psi_4",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "DECONSTRUIR LA IDEALIZACIÓN",
            quote = "«La nostalgia suele recordar el personaje que imaginamos, no a la persona real con la que convivimos.»",
            author = "Psicología Cognitiva del Vínculo",
            reflection = "Cuando la mente pinte el pasado de color rosa, recuerda también los silencios, la frialdad y las dudas que viviste."
        ),
        WisdomCard(
            id = "psi_5",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "REGULACIÓN DEL CORTISOL",
            quote = "«Comprobar la última conexión o mirar redes sociales reinicia el pico de estrés y posterga la cicatrización emocional.»",
            author = "Neurobiología del Vínculo (Marian Rojas Estapé)",
            reflection = "Proteger tus ojos del rastro ajeno es un acto de higiene mental indispensable para devolverle la calma a tu cuerpo."
        ),
        WisdomCard(
            id = "psi_6",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "LA RESPONSABILIDAD AFECTIVA",
            quote = "«Aceptar que una historia terminó no es fracasar: es tener el coraje de no insistir donde la puerta ya está cerrada.»",
            author = "Elaboración Terapéutica del Duelo",
            reflection = "No necesitas un último mensaje aclaratorio. El cierre real es una decisión interna que tomas contigo mismo/a."
        ),
        WisdomCard(
            id = "psi_7",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "EL ESPACIO PARA UNO MISMO",
            quote = "«Soltar no es olvidar ni borrar; es ubicar lo vivido en un lugar de tu historia donde ya no te impida caminar hacia adelante.»",
            author = "Psicología Humanista y Sistémica",
            reflection = "El vínculo que hoy merece toda tu atención, dedicación y cuidado incondicional es el que tienes contigo."
        ),
        WisdomCard(
            id = "psi_8",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "AUTONOMÍA AFECTIVA",
            quote = "«La soledad temida se transforma en serenidad cuando descubres que puedes ser tu propio lugar seguro.»",
            author = "Clínica de la Dependencia Emocional",
            reflection = "Aprender a estar en paz en tu propia compañía es la mayor garantía de que no volverás a elegir por desesperación."
        ),
        WisdomCard(
            id = "psi_bus_card_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "AMOR SIN POSESIVIDAD",
            quote = "«Amar significa permitir que la persona amada sea quien decida ser, sin nuestras demandas ni cadenas. El amor empieza por la propia plenitud y se entrega como un don libre.»",
            author = "Dr. Leo Buscaglia (El Arte de Amar)",
            reflection = "No mendigues afecto ni intentes retener a quien quiere marcharse. El amor que ata no es amor; soltar en libertad es el mayor homenaje al amor y a tu propia vida."
        ),
        WisdomCard(
            id = "psi_tnh_card_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "ABRAZAR EL SUFRIMIENTO",
            quote = "«Tienes que amar de tal manera que la persona que amas se sienta libre. Soltar nos da libertad, y la libertad es la única condición para la felicidad. Abraza tu dolor como una madre sostiene a su hijo que llora.»",
            author = "Thich Nhat Hanh (Mindfulness y Compasión)",
            reflection = "Inhala calma y acoge la tristeza en tu pecho sin juicio. Cuando dejas de pelear contra lo que sientes, el dolor se transforma en profunda sabiduría."
        ),
        WisdomCard(
            id = "psi_leo_card_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "EL PODER DE LA RETIRADA",
            quote = "«Tu mayor fuerza reside en saber retirarte en silencio cuando no te valoran. La necesidad ahuyenta; la soberanía personal inspira respeto innegociable.»",
            author = "Leo Quins (Desapego y Soberanía)",
            reflection = "No compitas por la atención de quien duda de ti. Retira tu presencia y devuélvete el valor que estabas regalando."
        ),
        WisdomCard(
            id = "psi_tem_card_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "MODO GUERRA Y CERO RUEGOS",
            quote = "«Una persona con dignidad jamás ruega por amor. Bájale del pedestal, súbete a tu propio trono y transforma ese dolor en disciplina, hierro y metas.»",
            author = "El Temach / T Mach (Modo Guerra)",
            reflection = "El contacto cero es sagrado: no se vigila, no se pide perdón por existir y no se suplica. Hoy tu único proyecto eres tú."
        ),
        WisdomCard(
            id = "psi_tem_card_2",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "LA FORJA DEL VALOR Y LA DISCIPLINA",
            quote = "«El valor no se pide prestado, se forja cada día en el gimnasio, en tus libros y en tus proyectos. No compitas con palabras; sé un ejemplo viviente de honor y progreso.»",
            author = "El Temach / T Mach (Disciplina y Enfoque)",
            reflection = "Canaliza toda la energía de la frustración en construir la versión más fuerte y admirable de ti mismo. Tu futuro no espera."
        ),
        WisdomCard(
            id = "psi_tri_card_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "CERO REACCIÓN A LAS MIGAJAS",
            quote = "«Las migajas de atención y los mensajes vacíos no son interés real: son la prueba de que te quieren disponible en su banco de suplentes. El silencio sereno es tu victoria.»",
            author = "El Rincón del Trillo (David Trillo)",
            reflection = "No reacciones ante tanteos tibios. Quien decidió marcharse debe asumir el peso de la ausencia total."
        ),
        WisdomCard(
            id = "psi_bar_card_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            title = "EL VALOR NO NEGOCIABLE",
            quote = "«La disponibilidad infinita abarata tu presencia. Cuando dejas de perseguir, devuelves a la otra persona la responsabilidad de sus actos y recuperas tu centro.»",
            author = "Robert Barrett (Dinámicas Relacionales)",
            reflection = "Frenar la persecución desarma la dinámica de poder. Tu valor nunca se demuestra insistiendo, sino sosteniéndote en tu propia calma."
        ),

        // ==========================================
        // 3. CATÓLICO (Proverbios, Sabiduría Bíblica y Tradición - Esperanza y Perdón)
        // ==========================================
        WisdomCard(
            id = "cat_1",
            framework = SoltarFramework.CATOLICO,
            title = "CUIDAR EL CORAZÓN",
            quote = "«Por encima de todo lo que guardes, guarda tu corazón, porque de él brota la vida.»",
            author = "Proverbios 4:23",
            reflection = "Poner distancia no es dureza de espíritu: es custodiar tu paz interior y el templo que Dios te ha confiado."
        ),
        WisdomCard(
            id = "cat_2",
            framework = SoltarFramework.CATOLICO,
            title = "UN TIEMPO PARA CADA COSA",
            quote = "«Todo tiene su momento oportuno; hay un tiempo para abrazar y un tiempo para abstenerse de abrazar; un tiempo para buscar y un tiempo para dar por perdido.»",
            author = "Eclesiastés 3:1, 5-6",
            reflection = "Acepta la estación en la que te encuentras. Hay temporadas de siembra, de fruto y de soltar lo que ya cumplió su propósito."
        ),
        WisdomCard(
            id = "cat_3",
            framework = SoltarFramework.CATOLICO,
            title = "LA FUERZA EN EL DESIERTO",
            quote = "«Los que esperan en el Señor renovarán sus fuerzas; volarán con alas de águila, correrán y no se fatigarán, caminarán y no se cansarán.»",
            author = "Isaías 40:31",
            reflection = "La espera consciente y el silencio no son vacíos: son el terreno donde se forja una fe madura y una nueva fortaleza."
        ),
        WisdomCard(
            id = "cat_4",
            framework = SoltarFramework.CATOLICO,
            title = "CONFIANZA Y PRUDENCIA",
            quote = "«El prudente ve el peligro y se aparta; los ingenuos pasan adelante y sufren el daño.»",
            author = "Proverbios 22:3",
            reflection = "El contacto cero es un acto de profunda prudencia para no exponerte a heridas innecesarias."
        ),
        WisdomCard(
            id = "cat_5",
            framework = SoltarFramework.CATOLICO,
            title = "EL PERDÓN Y LA LIBERTAD",
            quote = "«Perdonar no significa justificar lo que dolió, sino entregar la carga del rencor para que tu alma vuelva a caminar ligera.»",
            author = "Tradición de Sabiduría Cristiana",
            reflection = "Perdonar te libera a ti del lazo del resentimiento. Puedes desearle el bien a la otra persona desde la distancia y en paz."
        ),
        WisdomCard(
            id = "cat_6",
            framework = SoltarFramework.CATOLICO,
            title = "LA PAZ INTERIOR",
            quote = "«La paz os dejo, mi paz os doy; no os la doy yo como el mundo la da. No se turbe vuestro corazón ni tenga miedo.»",
            author = "Juan 14:27",
            reflection = "La verdadera paz no depende de que el teléfono suene o de una respuesta ajena, sino del descanso en Dios."
        ),
        WisdomCard(
            id = "cat_7",
            framework = SoltarFramework.CATOLICO,
            title = "RECONSTRUIR CON ESPERANZA",
            quote = "«Él sana a los quebrantados de corazón y venda sus heridas.»",
            author = "Salmo 147:3",
            reflection = "Como el arte del kintsugi, las grietas del alma pueden ser restauradas con gracia, paciencia y propósito."
        ),
        WisdomCard(
            id = "cat_8",
            framework = SoltarFramework.CATOLICO,
            title = "DOMINIO PROPIO",
            quote = "«Porque no nos ha dado Dios espíritu de cobardía, sino de poder, de amor y de dominio propio.»",
            author = "2 Timoteo 1:7",
            reflection = "Tienes la gracia y la capacidad para resistir el impulso momentáneo y elegir el camino de la vida y la dignidad."
        ),
        WisdomCard(
            id = "cat_9",
            framework = SoltarFramework.CATOLICO,
            title = "ALGO NUEVO",
            quote = "«No recuerden las cosas pasadas ni se fijen en lo antiguo. Miren que hago algo nuevo; ya está brotando, ¿no lo notan?»",
            author = "Isaías 43:18-19",
            reflection = "Dios está obrando en tu presente. No te quedes atrapado en el pasado; abre tus ojos a la novedad que Él está preparando para ti."
        ),
        WisdomCard(
            id = "cat_10",
            framework = SoltarFramework.CATOLICO,
            title = "PAZ ANTE LA ANSIEDAD",
            quote = "«No se inquieten por nada; más bien, en toda ocasión, con oración y ruego, presenten sus peticiones a Dios y denle gracias. Y la paz de Dios, que sobrepasa todo entendimiento, cuidará sus corazones.»",
            author = "Filipenses 4:6-7",
            reflection = "Entrega tu ansiedad en oración. La paz de Dios es un regalo que protege tu interior más allá de lo que la lógica humana puede explicar."
        ),
        WisdomCard(
            id = "cat_11",
            framework = SoltarFramework.CATOLICO,
            title = "NADA TE SEPARA",
            quote = "«Estoy convencido de que... ni cosa alguna en toda la creación podrá apartarnos del amor que Dios nos ha manifestado en Cristo Jesús.»",
            author = "Romanos 8:38-39",
            reflection = "Tu valor no disminuye por una ruptura. Estás sostenido/a por un amor incondicional que ninguna circunstancia humana puede alterar."
        ),
        WisdomCard(
            id = "cat_12",
            framework = SoltarFramework.CATOLICO,
            title = "RENOVACIÓN DIARIA",
            quote = "«El gran amor del Señor nunca se acaba, y su compasión jamás se agota. Cada mañana se renuevan sus bondades.»",
            author = "Lamentaciones 3:22-23",
            reflection = "Aunque el duelo sea profundo, cada nuevo amanecer es una oportunidad para experimentar la fidelidad y el consuelo renovador de Dios."
        ),
        WisdomCard(
            id = "cat_13",
            framework = SoltarFramework.CATOLICO,
            title = "AMPARO SEGURO",
            quote = "«Dios es nuestro amparo y fortaleza, nuestro pronto auxilio en los problemas.»",
            author = "Salmo 46:1",
            reflection = "En medio de la tormenta de tus emociones, busca refugio en la presencia segura y firme de Dios. Él sostiene tu ser."
        ),
        WisdomCard(
            id = "cat_14",
            framework = SoltarFramework.CATOLICO,
            title = "CERCANÍA EN EL DOLOR",
            quote = "«El Señor está cerca de los quebrantados de corazón, y salva a los de espíritu abatido.»",
            author = "Salmo 34:18",
            reflection = "No estás solo/a en tu tristeza. En tu mayor vulnerabilidad, la presencia de Dios es más íntima y restauradora."
        ),
        // Nuevas Tarjetas Católicas (Batch 2)
        WisdomCard(
            id = "cat_41",
            framework = SoltarFramework.CATOLICO,
            title = "AMOR QUE TODO LO PUEDE",
            quote = "«El amor es paciente, es servicial; el amor no es envidioso, no hace alarde, no se envanece.»",
            author = "1 Corintios 13:4",
            reflection = "Usa esta medida para evaluar tus acciones diarias contigo mismo/a."
        ),
        WisdomCard(
            id = "cat_42",
            framework = SoltarFramework.CATOLICO,
            title = "CONFIANZA TOTAL",
            quote = "«Confía en el Señor con todo tu corazón, y no te apoyes en tu propio entendimiento.»",
            author = "Proverbios 3:5",
            reflection = "Soltar significa dejar de intentar entenderlo todo y confiar en la providencia."
        ),
        WisdomCard(
            id = "cat_43",
            framework = SoltarFramework.CATOLICO,
            title = "ESPÍRITU DE VALENTÍA",
            quote = "«No temas, porque yo estoy contigo; no desmayes, porque yo soy tu Dios.»",
            author = "Isaías 41:10",
            reflection = "Tu fuerza no es tuya, es la fuerza de Dios que habita en ti."
        ),
        WisdomCard(
            id = "cat_44",
            framework = SoltarFramework.CATOLICO,
            title = "PAZ QUE DA VIDA",
            quote = "«La paz os dejo, mi paz os doy.»",
            author = "Juan 14:27",
            reflection = "La paz de Dios no se encuentra en las circunstancias, se encuentra en Él."
        ),
        WisdomCard(
            id = "cat_45",
            framework = SoltarFramework.CATOLICO,
            title = "NUEVA CREACIÓN",
            quote = "«Si alguien está en Cristo, es una nueva creación; lo viejo ha pasado, ha llegado lo nuevo.»",
            author = "2 Corintios 5:17",
            reflection = "Eres una nueva persona. Lo que pasó no define quién eres ahora."
        ),
        WisdomCard(
            id = "cat_46",
            framework = SoltarFramework.CATOLICO,
            title = "EL SEÑOR ES MI PASTOR",
            quote = "«El Señor es mi pastor, nada me faltará.»",
            author = "Salmo 23:1",
            reflection = "Él provee lo necesario para tu sanación."
        ),
        WisdomCard(
            id = "cat_47",
            framework = SoltarFramework.CATOLICO,
            title = "SABIDURÍA PARA EL PRESENTE",
            quote = "«Enséñanos a contar nuestros días, para que traigamos al corazón sabiduría.»",
            author = "Salmo 90:12",
            reflection = "Vive cada día con propósito, soltando el pasado que ya no está."
        ),
        WisdomCard(
            id = "cat_48",
            framework = SoltarFramework.CATOLICO,
            title = "FUERZA Y CORAJE",
            quote = "«Esfuérzate y sé valiente; no temas ni desmayes, porque el Señor tu Dios estará contigo.»",
            author = "Josué 1:9",
            reflection = "El coraje es necesario para dejar ir lo que te daña."
        ),
        WisdomCard(
            id = "cat_49",
            framework = SoltarFramework.CATOLICO,
            title = "AMOR INCONDICIONAL",
            quote = "«Porque tanto amó Dios al mundo que dio a su Hijo único.»",
            author = "Juan 3:16",
            reflection = "Tu valor es inmenso. No permitas que nadie te haga sentir menos."
        ),
        WisdomCard(
            id = "cat_50",
            framework = SoltarFramework.CATOLICO,
            title = "DESCANSAR EN LA GRACIA",
            quote = "«Bástate mi gracia, porque mi poder se perfecciona en la debilidad.»",
            author = "2 Corintios 12:9",
            reflection = "No tienes que ser perfecto/a. Tu debilidad es el lugar de la obra de Dios."
        ),
        WisdomCard(
            id = "cat_51",
            framework = SoltarFramework.CATOLICO,
            title = "SEGUIR ADELANTE",
            quote = "«Olvidando lo que queda atrás y esforzándome por lo que tengo delante, prosigo a la meta.»",
            author = "Filipenses 3:13-14",
            reflection = "Soltar es mirar hacia la meta, no hacia el pasado."
        ),
        WisdomCard(
            id = "cat_52",
            framework = SoltarFramework.CATOLICO,
            title = "CUIDADO DIVINO",
            quote = "«Echen toda su ansiedad sobre él porque él tiene cuidado de ustedes.»",
            author = "1 Pedro 5:7",
            reflection = "Deja que Dios se encargue de lo que te angustia."
        ),
        WisdomCard(
            id = "cat_53",
            framework = SoltarFramework.CATOLICO,
            title = "LA FUERZA DEL SEÑOR",
            quote = "«El Señor es mi fuerza y mi escudo; en él confía mi corazón.»",
            author = "Salmo 28:7",
            reflection = "Confía en que Él te sostiene en este proceso."
        ),
        WisdomCard(
            id = "cat_54",
            framework = SoltarFramework.CATOLICO,
            title = "LUZ PARA EL CAMINO",
            quote = "«Tu palabra es una lámpara a mis pies y una luz en mi camino.»",
            author = "Salmo 119:105",
            reflection = "La sabiduría bíblica te dará claridad cuando el camino parezca confuso."
        ),
        WisdomCard(
            id = "cat_55",
            framework = SoltarFramework.CATOLICO,
            title = "PERDÓN QUE LIBERA",
            quote = "«Soportándoos unos a otros, y perdonándoos unos a otros; como Cristo os perdonó, así también hacedlo vosotros.»",
            author = "Colosenses 3:13",
            reflection = "El perdón es un camino de sanación personal, no una justificación del otro."
        ),
        WisdomCard(
            id = "cat_56",
            framework = SoltarFramework.CATOLICO,
            title = "LA PAZ DE DIOS",
            quote = "«Y la paz de Dios, que sobrepasa todo entendimiento, guardará sus corazones y sus pensamientos en Cristo Jesús.»",
            author = "Filipenses 4:7",
            reflection = "La paz de Dios está contigo, incluso cuando no entiendes el porqué de las cosas."
        ),
        WisdomCard(
            id = "cat_57",
            framework = SoltarFramework.CATOLICO,
            title = "NUEVA ESPERANZA",
            quote = "«Porque yo sé los planes que tengo para ustedes, planes de bienestar y no de calamidad, para darles un futuro y una esperanza.»",
            author = "Jeremías 29:11",
            reflection = "Dios tiene un futuro bueno para ti, confía en su plan."
        ),
        WisdomCard(
            id = "cat_58",
            framework = SoltarFramework.CATOLICO,
            title = "VALOR EN EL SEÑOR",
            quote = "«El Señor está conmigo; no tengo miedo. ¿Qué me puede hacer el hombre?»",
            author = "Salmo 118:6",
            reflection = "Tu seguridad está en Dios, no en las opiniones de otros."
        ),
        WisdomCard(
            id = "cat_59",
            framework = SoltarFramework.CATOLICO,
            title = "AMOR VERDADERO",
            quote = "«En esto se manifestó el amor de Dios en nosotros, en que Dios envió a su Hijo único al mundo para que vivamos por medio de él.»",
            author = "1 Juan 4:9",
            reflection = "Tu vida tiene un propósito divino."
        ),
        WisdomCard(
            id = "cat_60",
            framework = SoltarFramework.CATOLICO,
            title = "SABIDURÍA Y CONSEJO",
            quote = "«El temor del Señor es el principio de la sabiduría.»",
            author = "Proverbios 9:10",
            reflection = "Honrar a Dios es el inicio de entender la vida con claridad."
        ),
        WisdomCard(
            id = "cat_61",
            framework = SoltarFramework.CATOLICO,
            title = "FUERZA RENOVADA",
            quote = "«Pero los que esperan en Jehová tendrán nuevas fuerzas; levantarán alas como las águilas; correrán, y no se cansarán; caminarán, y no se fatigarán.»",
            author = "Isaías 40:31",
            reflection = "Renueva tus fuerzas en el Señor cada día."
        ),
        WisdomCard(
            id = "cat_62",
            framework = SoltarFramework.CATOLICO,
            title = "PAZ Y SEGURIDAD",
            quote = "«En paz me acostaré, y asimismo dormiré; porque solo tú, Jehová, me haces vivir confiado.»",
            author = "Salmo 4:8",
            reflection = "Duerme en la seguridad de que Dios cuida de ti."
        ),
        WisdomCard(
            id = "cat_63",
            framework = SoltarFramework.CATOLICO,
            title = "EL SEÑOR TE SOSTIENE",
            quote = "«Echa sobre Jehová tu carga, y él te sustentará; no dejará para siempre caído al justo.»",
            author = "Salmo 55:22",
            reflection = "Entrégale tus cargas, Él te sostendrá."
        ),
        WisdomCard(
            id = "cat_64",
            framework = SoltarFramework.CATOLICO,
            title = "AMOR ETERNO",
            quote = "«Con amor eterno te he amado; por eso te he prolongado mi misericordia.»",
            author = "Jeremías 31:3",
            reflection = "Su amor por ti no tiene límites ni final."
        ),
        WisdomCard(
            id = "cat_65",
            framework = SoltarFramework.CATOLICO,
            title = "SABIDURÍA DE DIOS",
            quote = "«La sabiduría es un árbol de vida para los que se echan mano de ella; bienaventurado el que la retiene.»",
            author = "Proverbios 3:18",
            reflection = "Aprende de esta experiencia y retén la sabiduría que te deja."
        ),
        WisdomCard(
            id = "cat_66",
            framework = SoltarFramework.CATOLICO,
            title = "LUZ EN LA OSCURIDAD",
            quote = "«Dios es luz, y no hay ningunas tinieblas en él.»",
            author = "1 Juan 1:5",
            reflection = "En Dios siempre encontrarás claridad, nunca confusión."
        ),
        WisdomCard(
            id = "cat_67",
            framework = SoltarFramework.CATOLICO,
            title = "FORTALEZA EN EL SEÑOR",
            quote = "«El Señor es mi luz y mi salvación; ¿de quién temeré? El Señor es la fortaleza de mi vida; ¿de quién me he de atemorizar?»",
            author = "Salmo 27:1",
            reflection = "No hay nada que temer si Dios está contigo."
        ),
        WisdomCard(
            id = "cat_68",
            framework = SoltarFramework.CATOLICO,
            title = "DESCANSAR EN ÉL",
            quote = "«Vengan a mí todos ustedes que están cansados y agobiados, y yo les daré descanso.»",
            author = "Mateo 11:28",
            reflection = "Él te ofrece descanso cuando el peso de la vida es demasiado."
        ),
        WisdomCard(
            id = "cat_69",
            framework = SoltarFramework.CATOLICO,
            title = "PAZ INTERIOR",
            quote = "«La paz os dejo, mi paz os doy; yo no os la doy como el mundo la da. No se turbe vuestro corazón, ni tenga miedo.»",
            author = "Juan 14:27",
            reflection = "La paz que el mundo no puede darte, Dios te la entrega hoy."
        ),
        WisdomCard(
            id = "cat_70",
            framework = SoltarFramework.CATOLICO,
            title = "VALOR DIVINO",
            quote = "«Ustedes fueron comprados por precio; no se hagan esclavos de los hombres.»",
            author = "1 Corintios 7:23",
            reflection = "Tu valor viene de Dios, no de la aprobación humana."
        ),
        WisdomCard(
            id = "cat_71",
            framework = SoltarFramework.CATOLICO,
            title = "FE QUE MUEVE MONTAÑAS",
            quote = "«Si tuvieran fe como un grano de mostaza, dirían a este monte: Pásate de aquí allá, y se pasará; y nada les será imposible.»",
            author = "Mateo 17:20",
            reflection = "La fe pequeña es suficiente para grandes cambios."
        ),
        WisdomCard(
            id = "cat_72",
            framework = SoltarFramework.CATOLICO,
            title = "AMOR DE DIOS",
            quote = "«En esto consiste el amor: no en que nosotros hayamos amado a Dios, sino en que él nos amó a nosotros.»",
            author = "1 Juan 4:10",
            reflection = "Su amor te precede y te envuelve siempre."
        ),
        WisdomCard(
            id = "cat_73",
            framework = SoltarFramework.CATOLICO,
            title = "SABIDURÍA PRÁCTICA",
            quote = "«Mejor es el paciente que el arrogante.»",
            author = "Eclesiastés 7:8",
            reflection = "La paciencia es tu mejor aliada en este camino de sanación."
        ),
        WisdomCard(
            id = "cat_74",
            framework = SoltarFramework.CATOLICO,
            title = "CONFIANZA TOTAL",
            quote = "«Fíate de Jehová de todo tu corazón, y no te apoyes en tu propia prudencia.»",
            author = "Proverbios 3:5",
            reflection = "Suelta tu necesidad de controlarlo todo."
        ),
        WisdomCard(
            id = "cat_75",
            framework = SoltarFramework.CATOLICO,
            title = "FORTALEZA EN LA PRUEBA",
            quote = "«Consideren como un gran gozo, hermanos míos, cuando se hallen en diversas pruebas.»",
            author = "Santiago 1:2",
            reflection = "Tu prueba está forjando tu carácter y acercándote más a Dios."
        ),
        WisdomCard(
            id = "cat_76",
            framework = SoltarFramework.CATOLICO,
            title = "AMOR AL PRÓJIMO",
            quote = "«Ama a tu prójimo como a ti mismo.»",
            author = "Marcos 12:31",
            reflection = "Recuerda que también eres parte de ese prójimo que debes amar y cuidar."
        ),
        WisdomCard(
            id = "cat_77",
            framework = SoltarFramework.CATOLICO,
            title = "DIOS NOS CONOCE",
            quote = "«Señor, tú me examinas y me conoces. Tú conoces mi sentarme y mi levantarme.»",
            author = "Salmo 139:1-2",
            reflection = "Dios entiende perfectamente lo que estás sintiendo ahora mismo."
        ),
        WisdomCard(
            id = "cat_78",
            framework = SoltarFramework.CATOLICO,
            title = "FUERZA Y CORAJE",
            quote = "«¡Sé fuerte y valiente! ¡No tengas miedo ni te desanimes! Porque el Señor tu Dios te acompañará dondequiera que vayas.»",
            author = "Josué 1:9",
            reflection = "No caminas solo/a, Dios está a tu lado en cada paso."
        ),
        WisdomCard(
            id = "cat_79",
            framework = SoltarFramework.CATOLICO,
            title = "PAZ EN EL CORAZÓN",
            quote = "«Que la paz de Cristo reine en sus corazones.»",
            author = "Colosenses 3:15",
            reflection = "Deja que Su paz gobierne tus emociones más intensas."
        ),
        WisdomCard(
            id = "cat_80",
            framework = SoltarFramework.CATOLICO,
            title = "DIOS ES BUENO",
            quote = "«Bueno es el Señor con los que en él esperan, con el alma que lo busca.»",
            author = "Lamentaciones 3:25",
            reflection = "Su bondad se manifestará en tu vida conforme busques sanar."
        ),
        WisdomCard(
            id = "cat_81",
            framework = SoltarFramework.CATOLICO,
            title = "VALOR DEL TIEMPO",
            quote = "«Aprende a vivir el día a día, no te angusties por el mañana.»",
            author = "Inspirado en Mateo 6:34",
            reflection = "Céntrate en sanar hoy, el mañana tiene su propio cuidado."
        ),
        WisdomCard(
            id = "cat_82",
            framework = SoltarFramework.CATOLICO,
            title = "DIOS ES TU ROCA",
            quote = "«El Señor es mi roca, mi fortaleza y mi libertador.»",
            author = "Salmo 18:2",
            reflection = "Apóyate en Él como tu base sólida en este proceso."
        ),
        WisdomCard(
            id = "cat_83",
            framework = SoltarFramework.CATOLICO,
            title = "ESPERANZA EN LA AFLICCIÓN",
            quote = "«Los que siembran con lágrimas, cosecharán con regocijo.»",
            author = "Salmo 126:5",
            reflection = "Tu dolor actual es semilla de una alegría futura."
        ),
        WisdomCard(
            id = "cat_84",
            framework = SoltarFramework.CATOLICO,
            title = "AMOR INCONDICIONAL",
            quote = "«Porque el Señor es bueno; para siempre es su misericordia.»",
            author = "Salmo 100:5",
            reflection = "Su misericordia te acompaña en cada momento de debilidad."
        ),
        WisdomCard(
            id = "cat_85",
            framework = SoltarFramework.CATOLICO,
            title = "SABIDURÍA DIVINA",
            quote = "«El principio de la sabiduría es el temor del Señor.»",
            author = "Salmo 111:10",
            reflection = "Reconocer a Dios es el inicio de entender tu propia vida."
        ),
        WisdomCard(
            id = "cat_86",
            framework = SoltarFramework.CATOLICO,
            title = "PAZ QUE DA VIDA",
            quote = "«Dichosos los que trabajan por la paz, porque serán llamados hijos de Dios.»",
            author = "Mateo 5:9",
            reflection = "Trabaja por tu propia paz interior, eres hijo/a de Dios."
        ),
        WisdomCard(
            id = "cat_87",
            framework = SoltarFramework.CATOLICO,
            title = "LUZ PARA TU CAMINO",
            quote = "«El camino de los justos es como la luz de la aurora, que va aumentando en resplandor hasta que el día es perfecto.»",
            author = "Proverbios 4:18",
            reflection = "Tu sanación es progresiva, cada día un poco más de luz."
        ),
        WisdomCard(
            id = "cat_88",
            framework = SoltarFramework.CATOLICO,
            title = "CONFIANZA TOTAL",
            quote = "«En Dios he puesto mi confianza; no temeré.»",
            author = "Salmo 56:11",
            reflection = "Tu confianza en Dios elimina el miedo que paraliza."
        ),
        WisdomCard(
            id = "cat_89",
            framework = SoltarFramework.CATOLICO,
            title = "AMOR QUE TRANSFORMA",
            quote = "«El amor de Dios ha sido derramado en nuestros corazones.»",
            author = "Romanos 5:5",
            reflection = "Ese amor es el motor de tu transformación y sanación."
        ),
        WisdomCard(
            id = "cat_90",
            framework = SoltarFramework.CATOLICO,
            title = "ESPERANZA ETERNA",
            quote = "«Mi esperanza está en ti, Señor.»",
            author = "Salmo 39:7",
            reflection = "Pon tu esperanza en Dios, Él no falla nunca."
        ),
    )

    fun getRandomCard(framework: SoltarFramework, recentIds: List<String>): WisdomCard {
        val frameworkCards = cards.filter { it.framework == framework }
        if (frameworkCards.isEmpty()) return cards.first()

        val candidateCards = frameworkCards.filterNot { recentIds.contains(it.id) }
        val pool = if (candidateCards.isNotEmpty()) candidateCards else frameworkCards
        return pool.random()
    }

    fun getCardById(id: String): WisdomCard? {
        return cards.firstOrNull { it.id == id }
    }
}
