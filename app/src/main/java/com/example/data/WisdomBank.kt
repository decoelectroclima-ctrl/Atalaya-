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
        )
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
