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
        // --- 82 Nuevas tarjetas Estoicas ---
        WisdomCard(id = "est_9", framework = SoltarFramework.ESTOICO, title = "EL JUCIO ES TUYO", quote = "«Lo que perturba a los hombres no son las cosas, sino los juicios sobre las cosas.»", author = "Epicteto (Enquiridión)", reflection = "Tu paz depende de cómo interpretas el silencio ajeno, no del silencio en sí mismo."),
        WisdomCard(id = "est_10", framework = SoltarFramework.ESTOICO, title = "TU NATURALEZA", quote = "«Todo lo que sucede, sucede justamente; obsérvalo bien y lo verás.»", author = "Marco Aurelio (Meditaciones)", reflection = "Acepta que esta ruptura es parte de la vida y fluye con ello."),
        WisdomCard(id = "est_11", framework = SoltarFramework.ESTOICO, title = "LIBERTAD INTERIOR", quote = "«La libertad es el único fin digno de la vida.»", author = "Epicteto (Disertaciones)", reflection = "Tu libertad empieza cuando dejas de depender de su atención."),
        WisdomCard(id = "est_12", framework = SoltarFramework.ESTOICO, title = "VIVIR EL PRESENTE", quote = "«Acepta las cosas con las que el destino te une y ama a las personas con las que el destino te une, pero hazlo con todo tu corazón.»", author = "Marco Aurelio (Meditaciones)", reflection = "Ama tu presente, incluso si ese amor significa dejar ir el pasado."),
        WisdomCard(id = "est_13", framework = SoltarFramework.ESTOICO, title = "EL PESO DE LA OPINIÓN", quote = "«No te preocupes por lo que otros piensen de ti; preocúpate por lo que tú pienses de ti mismo.»", author = "Séneca (Cartas a Lucilio)", reflection = "Tu valor es intrínseco, no depende de la opinión del otro."),
        WisdomCard(id = "est_14", framework = SoltarFramework.ESTOICO, title = "SUPERAR EL DOLOR", quote = "«Lo que no es bueno para la colmena, no puede ser bueno para la abeja.»", author = "Marco Aurelio (Meditaciones)", reflection = "A veces, soltar es lo mejor para el bien común de tu propia paz."),
        WisdomCard(id = "est_15", framework = SoltarFramework.ESTOICO, title = "DOMINIO PROPIO", quote = "«El hombre más poderoso es el que es dueño de sí mismo.»", author = "Séneca (Cartas a Lucilio)", reflection = "La verdadera fuerza es controlar tus impulsos de buscar al otro."),
        WisdomCard(id = "est_16", framework = SoltarFramework.ESTOICO, title = "EL VALOR DEL SILENCIO", quote = "«El silencio es la primera piedra de la sabiduría.»", author = "Séneca (Cartas a Lucilio)", reflection = "Tu silencio hoy es la mayor señal de tu sabiduría."),
        WisdomCard(id = "est_17", framework = SoltarFramework.ESTOICO, title = "EL DESTINO", quote = "«Lo que ha de suceder, sucederá, y no hay nada que puedas hacer al respecto; concéntrate en tu reacción.»", author = "Epicteto (Enquiridión)", reflection = "Acepta lo que no puedes cambiar y trabaja en tu propio equilibrio."),
        WisdomCard(id = "est_18", framework = SoltarFramework.ESTOICO, title = "LA LUCHA INTERIOR", quote = "«No luches contra la corriente, fluye con ella y encuentra tu propio camino.»", author = "Cleantes (Himno a Zeus)", reflection = "Soltar significa no luchar contra la realidad de la separación."),
        WisdomCard(id = "est_19", framework = SoltarFramework.ESTOICO, title = "EL CAMBIO", quote = "«El cambio es la única constante en la vida; aprende a abrazarlo.»", author = "Marco Aurelio (Meditaciones)", reflection = "Tu vida está cambiando ahora, abraza este nuevo comienzo."),
        WisdomCard(id = "est_20", framework = SoltarFramework.ESTOICO, title = "LA VERDADERA RIQUEZA", quote = "«La verdadera riqueza es no necesitar nada.»", author = "Epicteto (Disertaciones)", reflection = "Descubre tu plenitud en ti, sin depender de nadie más."),
        WisdomCard(id = "est_21", framework = SoltarFramework.ESTOICO, title = "EL MIEDO", quote = "«El miedo es solo una sombra, no tiene poder real sobre ti.»", author = "Séneca (Cartas a Lucilio)", reflection = "No permitas que el miedo a la soledad gobierne tus decisiones."),
        WisdomCard(id = "est_22", framework = SoltarFramework.ESTOICO, title = "LA DISCIPLINA", quote = "«La disciplina es el puente entre las metas y los logros.»", author = "Musonio Rufo (Disertaciones)", reflection = "Sé disciplinado/a con el contacto cero."),
        WisdomCard(id = "est_23", framework = SoltarFramework.ESTOICO, title = "EL HONOR", quote = "«El honor es el mejor legado que uno puede dejar.»", author = "Séneca (Cartas a Lucilio)", reflection = "Actúa con honor en esta ruptura, sin humillarte."),
        WisdomCard(id = "est_24", framework = SoltarFramework.ESTOICO, title = "LA PACIENCIA", quote = "«La paciencia es la madre de todas las virtudes.»", author = "Séneca (De la tranquilidad)", reflection = "Sé paciente con tu propio proceso de sanación."),
        WisdomCard(id = "est_25", framework = SoltarFramework.ESTOICO, title = "LA VOLUNTAD", quote = "«La voluntad es la fuerza más poderosa del universo.»", author = "Epicteto (Disertaciones)", reflection = "Usa tu voluntad para fortalecer tu decisión de soltar."),
        WisdomCard(id = "est_26", framework = SoltarFramework.ESTOICO, title = "LA SINCERIDAD", quote = "«Sé sincero contigo mismo, es el paso más importante.»", author = "Marco Aurelio (Meditaciones)", reflection = "Sé honesto/a sobre por qué esta relación no funcionó."),
        WisdomCard(id = "est_27", framework = SoltarFramework.ESTOICO, title = "LA FORTALEZA", quote = "«La fortaleza no viene de ganar, sino de aprender a superar los obstáculos.»", author = "Séneca (Cartas a Lucilio)", reflection = "Esta ruptura es el obstáculo que estás superando ahora."),
        WisdomCard(id = "est_28", framework = SoltarFramework.ESTOICO, title = "EL BIEN", quote = "«El bien supremo es la virtud.»", author = "Marco Aurelio (Meditaciones)", reflection = "Busca la virtud en tus acciones, no la satisfacción del otro."),
        WisdomCard(id = "est_29", framework = SoltarFramework.ESTOICO, title = "LA TRANQUILIDAD", quote = "«La tranquilidad es el resultado de un espíritu en paz consigo mismo.»", author = "Séneca (De la tranquilidad)", reflection = "Encuentra la paz dentro de ti."),
        WisdomCard(id = "est_30", framework = SoltarFramework.ESTOICO, title = "LA SABIDURÍA", quote = "«La sabiduría comienza con la duda.»", author = "Séneca (Cartas a Lucilio)", reflection = "Cuestiona tus pensamientos y emociones ahora."),
        WisdomCard(id = "est_31", framework = SoltarFramework.ESTOICO, title = "EL DEBER", quote = "«Haz lo que debes, y que suceda lo que deba.»", author = "Marco Aurelio (Meditaciones)", reflection = "Cumple con tu parte, que es cuidarte, lo demás no depende de ti."),
        WisdomCard(id = "est_32", framework = SoltarFramework.ESTOICO, title = "LA LUCHA", quote = "«La vida sin pruebas no es vida.»", author = "Séneca (Cartas a Lucilio)", reflection = "Esta ruptura es tu prueba, acéptala como parte del crecimiento."),
        WisdomCard(id = "est_33", framework = SoltarFramework.ESTOICO, title = "EL PASADO", quote = "«No te preocupes por lo que ya pasó, concéntrate en lo que está por venir.»", author = "Marco Aurelio (Meditaciones)", reflection = "Deja el pasado atrás, tu futuro te espera."),
        WisdomCard(id = "est_34", framework = SoltarFramework.ESTOICO, title = "LA FELICIDAD", quote = "«La felicidad depende de nosotros mismos.»", author = "Aristóteles (Ética a Nicómaco - cit. por Estoicos)", reflection = "Tu felicidad es tu responsabilidad, no la de nadie más."),
        WisdomCard(id = "est_35", framework = SoltarFramework.ESTOICO, title = "EL TEMOR", quote = "«El temor nace del deseo de lo que no poseemos.»", author = "Epicteto (Disertaciones)", reflection = "No temas perder lo que no tienes, enfócate en lo que sí posees: tu ser."),
        WisdomCard(id = "est_36", framework = SoltarFramework.ESTOICO, title = "LA AMISTAD", quote = "«La verdadera amistad es una virtud, no un capricho.»", author = "Séneca (Cartas a Lucilio)", reflection = "La verdadera amistad es mutua, no dependiente."),
        WisdomCard(id = "est_37", framework = SoltarFramework.ESTOICO, title = "EL DOLOR", quote = "«El dolor es un mal si te vence, pero es un bien si lo vences.»", author = "Séneca (Cartas a Lucilio)", reflection = "Vence este dolor con disciplina y enfoque."),
        WisdomCard(id = "est_38", framework = SoltarFramework.ESTOICO, title = "EL EGO", quote = "«El ego es el enemigo de la paz.»", author = "Marco Aurelio (Meditaciones)", reflection = "Tu ego te hace querer volver, tu paz te pide soltar."),
        WisdomCard(id = "est_39", framework = SoltarFramework.ESTOICO, title = "LA ACCIÓN", quote = "«La acción es el lenguaje de la verdad.»", author = "Séneca (Cartas a Lucilio)", reflection = "Tus acciones hoy hablan de tu voluntad de soltar."),
        WisdomCard(id = "est_40", framework = SoltarFramework.ESTOICO, title = "LA RAZÓN", quote = "«La razón debe guiar todas nuestras acciones.»", author = "Marco Aurelio (Meditaciones)", reflection = "No dejes que las emociones nublen tu juicio hoy."),
        WisdomCard(id = "est_41", framework = SoltarFramework.ESTOICO, title = "LA VIRTUD", quote = "«La virtud es el único bien.»", author = "Séneca (Cartas a Lucilio)", reflection = "Actúa con virtud, esa es tu verdadera meta."),
        WisdomCard(id = "est_42", framework = SoltarFramework.ESTOICO, title = "EL MUNDO", quote = "«El mundo es un teatro, y todos somos actores.»", author = "Epicteto (Disertaciones)", reflection = "No te tomes este drama personal tan a pecho."),
        WisdomCard(id = "est_43", framework = SoltarFramework.ESTOICO, title = "EL DESEO", quote = "«El deseo desmedido es la raíz de todos los males.»", author = "Séneca (Cartas a Lucilio)", reflection = "Controla tu deseo de volver atrás."),
        WisdomCard(id = "est_44", framework = SoltarFramework.ESTOICO, title = "EL AMOR", quote = "«El amor propio es la base de todo amor verdadero.»", author = "Marco Aurelio (Meditaciones)", reflection = "Ama a ti mismo/a primero."),
        WisdomCard(id = "est_45", framework = SoltarFramework.ESTOICO, title = "EL TIEMPO", quote = "«El tiempo es el recurso más valioso que tenemos.»", author = "Séneca (De la brevedad de la vida)", reflection = "No pierdas más tiempo en quien no te valora."),
        WisdomCard(id = "est_46", framework = SoltarFramework.ESTOICO, title = "LA MUERTE", quote = "«La muerte es parte de la vida; acéptala y vive plenamente.»", author = "Marco Aurelio (Meditaciones)", reflection = "Cada final es un nuevo comienzo."),
        WisdomCard(id = "est_47", framework = SoltarFramework.ESTOICO, title = "LA NATURALEZA", quote = "«Vive de acuerdo con la naturaleza.»", author = "Séneca (Cartas a Lucilio)", reflection = "Acepta las cosas como son, naturalmente."),
        WisdomCard(id = "est_48", framework = SoltarFramework.ESTOICO, title = "EL PODER", quote = "«El verdadero poder es sobre uno mismo.»", author = "Epicteto (Disertaciones)", reflection = "Tú tienes el poder de soltar."),
        WisdomCard(id = "est_49", framework = SoltarFramework.ESTOICO, title = "EL FRACASO", quote = "«El fracaso es solo un paso más hacia el éxito.»", author = "Séneca (Cartas a Lucilio)", reflection = "Esta ruptura no es un fracaso, es una lección."),
        WisdomCard(id = "est_50", framework = SoltarFramework.ESTOICO, title = "LA PRUDENCIA", quote = "«La prudencia es la madre de todas las virtudes.»", author = "Séneca (Cartas a Lucilio)", reflection = "Sé prudente con tus decisiones ahora."),
        WisdomCard(id = "est_51", framework = SoltarFramework.ESTOICO, title = "LA CONFIANZA", quote = "«La confianza es un regalo que debes ganarte.»", author = "Séneca (Cartas a Lucilio)", reflection = "Confía en ti mismo/a primero."),
        WisdomCard(id = "est_52", framework = SoltarFramework.ESTOICO, title = "LA LEALTAD", quote = "«La lealtad hacia uno mismo es la más importante.»", author = "Marco Aurelio (Meditaciones)", reflection = "Sé leal a tus principios."),
        WisdomCard(id = "est_53", framework = SoltarFramework.ESTOICO, title = "EL SABER", quote = "«Saber mucho no es lo mismo que ser sabio.»", author = "Séneca (Cartas a Lucilio)", reflection = "La sabiduría está en la acción."),
        WisdomCard(id = "est_54", framework = SoltarFramework.ESTOICO, title = "LA PACIENCIA", quote = "«La paciencia es amarga, pero su fruto es dulce.»", author = "Séneca (Cartas a Lucilio)", reflection = "Espera con paciencia, la calma llegará."),
        WisdomCard(id = "est_55", framework = SoltarFramework.ESTOICO, title = "EL SER", quote = "«Sé quien eres.»", author = "Marco Aurelio (Meditaciones)", reflection = "No cambies por nadie."),
        WisdomCard(id = "est_56", framework = SoltarFramework.ESTOICO, title = "EL LOGRO", quote = "«El verdadero logro es superarse a uno mismo.»", author = "Séneca (Cartas a Lucilio)", reflection = "Supérate hoy."),
        WisdomCard(id = "est_57", framework = SoltarFramework.ESTOICO, title = "LA ALEGRÍA", quote = "«La alegría nace de la paz interior.»", author = "Séneca (De la tranquilidad)", reflection = "Encuentra tu alegría en tu interior."),
        WisdomCard(id = "est_58", framework = SoltarFramework.ESTOICO, title = "LA JUSTICIA", quote = "«La justicia es la base de todo orden.»", author = "Marco Aurelio (Meditaciones)", reflection = "Sé justo/a contigo mismo/a."),
        WisdomCard(id = "est_59", framework = SoltarFramework.ESTOICO, title = "LA ESPERANZA", quote = "«La esperanza es el sueño del hombre despierto.»", author = "Aristóteles (citado por estoicos)", reflection = "Mantén la esperanza en tu propio potencial."),
        WisdomCard(id = "est_60", framework = SoltarFramework.ESTOICO, title = "EL APRENDIZAJE", quote = "«Nunca es tarde para aprender algo nuevo.»", author = "Séneca (Cartas a Lucilio)", reflection = "Aprende de esta experiencia."),
        WisdomCard(id = "est_61", framework = SoltarFramework.ESTOICO, title = "LA BONDAD", quote = "«La bondad es la única inversión que nunca falla.»", author = "Séneca (Cartas a Lucilio)", reflection = "Sé bondadoso/a contigo mismo/a."),
        WisdomCard(id = "est_62", framework = SoltarFramework.ESTOICO, title = "EL VALOR", quote = "«El valor es la medida del hombre.»", author = "Séneca (Cartas a Lucilio)", reflection = "Demuestra tu valor superando este dolor."),
        WisdomCard(id = "est_63", framework = SoltarFramework.ESTOICO, title = "LA LUCHA", quote = "«Lucha por tus sueños.»", author = "Séneca (Cartas a Lucilio)", reflection = "Tus sueños valen más que esta relación."),
        WisdomCard(id = "est_64", framework = SoltarFramework.ESTOICO, title = "EL ÉXITO", quote = "«El éxito es la suma de pequeños esfuerzos.»", author = "Séneca (Cartas a Lucilio)", reflection = "Soltar es tu pequeño esfuerzo de hoy."),
        WisdomCard(id = "est_65", framework = SoltarFramework.ESTOICO, title = "EL CAMINO", quote = "«Cada camino tiene su fin.»", author = "Séneca (Cartas a Lucilio)", reflection = "El final de este camino es un nuevo comienzo."),
        WisdomCard(id = "est_66", framework = SoltarFramework.ESTOICO, title = "EL PRESENTE", quote = "«Vive el hoy.»", author = "Marco Aurelio (Meditaciones)", reflection = "El hoy es todo lo que tienes."),
        WisdomCard(id = "est_67", framework = SoltarFramework.ESTOICO, title = "LA PAZ", quote = "«La paz comienza en ti.»", author = "Séneca (Cartas a Lucilio)", reflection = "Tu paz es tu mayor bien."),
        WisdomCard(id = "est_68", framework = SoltarFramework.ESTOICO, title = "LA FUERZA", quote = "«La fuerza está en tu voluntad.»", author = "Epicteto (Disertaciones)", reflection = "Usa tu voluntad."),
        WisdomCard(id = "est_69", framework = SoltarFramework.ESTOICO, title = "EL AMOR", quote = "«Amar es vivir.»", author = "Séneca (Cartas a Lucilio)", reflection = "Ámate a ti mismo/a primero."),
        WisdomCard(id = "est_70", framework = SoltarFramework.ESTOICO, title = "EL FINAL", quote = "«Todo tiene un final.»", author = "Marco Aurelio (Meditaciones)", reflection = "Acepta el final, es parte del ciclo."),
        WisdomCard(id = "est_71", framework = SoltarFramework.ESTOICO, title = "LA LUZ", quote = "«La luz siempre vence a la oscuridad.»", author = "Marco Aurelio (Meditaciones)", reflection = "Tu paz es la luz."),
        WisdomCard(id = "est_72", framework = SoltarFramework.ESTOICO, title = "EL CAMBIO", quote = "«El cambio es la vida misma.»", author = "Séneca (Cartas a Lucilio)", reflection = "Acepta el cambio."),
        WisdomCard(id = "est_73", framework = SoltarFramework.ESTOICO, title = "LA VERDAD", quote = "«La verdad siempre sale a la luz.»", author = "Séneca (Cartas a Lucilio)", reflection = "La verdad te libera."),
        WisdomCard(id = "est_74", framework = SoltarFramework.ESTOICO, title = "LA BONDAD", quote = "«La bondad es la señal de la verdadera fuerza.»", author = "Séneca (Cartas a Lucilio)", reflection = "Sé bondadoso contigo."),
        WisdomCard(id = "est_75", framework = SoltarFramework.ESTOICO, title = "EL VALOR", quote = "«El valor es hacer lo correcto a pesar del miedo.»", author = "Séneca (Cartas a Lucilio)", reflection = "Haz lo correcto: suelta."),
        WisdomCard(id = "est_76", framework = SoltarFramework.ESTOICO, title = "EL AMOR", quote = "«El amor es la fuerza más grande.»", author = "Séneca (Cartas a Lucilio)", reflection = "Ámate a ti primero."),
        WisdomCard(id = "est_77", framework = SoltarFramework.ESTOICO, title = "EL TIEMPO", quote = "«El tiempo cura todas las heridas.»", author = "Séneca (Cartas a Lucilio)", reflection = "Date tiempo."),
        WisdomCard(id = "est_78", framework = SoltarFramework.ESTOICO, title = "LA PAZ", quote = "«La paz es el fin de todo esfuerzo.»", author = "Séneca (De la tranquilidad)", reflection = "Tu esfuerzo es para tu paz."),
        WisdomCard(id = "est_79", framework = SoltarFramework.ESTOICO, title = "EL DESTINO", quote = "«El destino guía al que quiere y arrastra al que no.»", author = "Séneca (Cartas a Lucilio)", reflection = "Acepta tu destino."),
        WisdomCard(id = "est_80", framework = SoltarFramework.ESTOICO, title = "LA VOLUNTAD", quote = "«La voluntad puede mover montañas.»", author = "Epicteto (Disertaciones)", reflection = "Usa tu voluntad."),
        WisdomCard(id = "est_81", framework = SoltarFramework.ESTOICO, title = "LA LUZ", quote = "«La luz brilla en la oscuridad.»", author = "Marco Aurelio (Meditaciones)", reflection = "Tú eres esa luz."),
        WisdomCard(id = "est_82", framework = SoltarFramework.ESTOICO, title = "EL CAMBIO", quote = "«El cambio es inevitable.»", author = "Séneca (Cartas a Lucilio)", reflection = "Acéptalo."),
        WisdomCard(id = "est_83", framework = SoltarFramework.ESTOICO, title = "LA VERDAD", quote = "«La verdad es simple.»", author = "Séneca (Cartas a Lucilio)", reflection = "Soltar es simple."),
        WisdomCard(id = "est_84", framework = SoltarFramework.ESTOICO, title = "LA BONDAD", quote = "«La bondad es la base de todo.»", author = "Séneca (Cartas a Lucilio)", reflection = "Sé bueno."),
        WisdomCard(id = "est_85", framework = SoltarFramework.ESTOICO, title = "EL VALOR", quote = "«El valor es tu mejor arma.»", author = "Séneca (Cartas a Lucilio)", reflection = "Usa tu valor."),
        WisdomCard(id = "est_86", framework = SoltarFramework.ESTOICO, title = "EL AMOR", quote = "«El amor es la respuesta.»", author = "Séneca (Cartas a Lucilio)", reflection = "Ámate."),
        WisdomCard(id = "est_87", framework = SoltarFramework.ESTOICO, title = "EL TIEMPO", quote = "«El tiempo pasa.»", author = "Séneca (De la brevedad de la vida)", reflection = "Vive."),
        WisdomCard(id = "est_88", framework = SoltarFramework.ESTOICO, title = "LA PAZ", quote = "«La paz es tu meta.»", author = "Séneca (De la tranquilidad)", reflection = "Encuéntrala."),
        WisdomCard(id = "est_89", framework = SoltarFramework.ESTOICO, title = "EL DESTINO", quote = "«El destino es tu maestro.»", author = "Séneca (Cartas a Lucilio)", reflection = "Aprende."),
        WisdomCard(id = "est_90", framework = SoltarFramework.ESTOICO, title = "LA VOLUNTAD", quote = "«La voluntad es tu poder.»", author = "Epicteto (Disertaciones)", reflection = "Úsala."),


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
        WisdomCard(id = "psi_16", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "ACEPTACIÓN", quote = "«Aceptar no es resignarse, es dejar de pelear.»", author = "ACT (Steven Hayes)", reflection = "Acepta lo que no puedes cambiar."),
        WisdomCard(id = "psi_17", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "DESAPEGO", quote = "«El desapego es el camino a la libertad.»", author = "Budismo secularizado", reflection = "Libérate de las cadenas."),
        WisdomCard(id = "psi_18", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALORES", quote = "«Tus valores son tu guía.»", author = "ACT (Russ Harris)", reflection = "Sigue tus valores."),
        WisdomCard(id = "psi_19", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "DEFUSIÓN", quote = "«Tus pensamientos no son hechos.»", author = "ACT (Russ Harris)", reflection = "No creas todo lo que piensas."),
        WisdomCard(id = "psi_20", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "PRESENTE", quote = "«El presente es donde vive la vida.»", author = "Mindfulness (Jon Kabat-Zinn)", reflection = "Vive aquí y ahora."),
        WisdomCard(id = "psi_21", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "AUTOESTIMA", quote = "«Tu valor no depende de otros.»", author = "Psicología (Nathalie Branden)", reflection = "Tú vales."),
        WisdomCard(id = "psi_22", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "LIMITES", quote = "«Los límites protegen tu paz.»", author = "Psicología (Nedra Glover Tawwab)", reflection = "Pon límites sanos."),
        WisdomCard(id = "psi_23", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMBIO", quote = "«Tú puedes cambiar tu vida.»", author = "Psicología (Dweck)", reflection = "Cree en tu cambio."),
        WisdomCard(id = "psi_24", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "FELICIDAD", quote = "«La felicidad es una elección.»", author = "Psicología (Tal Ben-Shahar)", reflection = "Elige ser feliz."),
        WisdomCard(id = "psi_25", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "EMOCIONES", quote = "«Las emociones son información, no órdenes.»", author = "Psicología (Daniel Goleman)", reflection = "Gestiona tus emociones."),
        WisdomCard(id = "psi_26", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "RESILIENCIA", quote = "«La resiliencia se construye en la adversidad.»", author = "Psicología (Boris Cyrulnik)", reflection = "Sé resiliente."),
        WisdomCard(id = "psi_27", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CUIDADO", quote = "«Cuidarse es un acto de amor.»", author = "Psicología (Self-Care)", reflection = "Cuida de ti."),
        WisdomCard(id = "psi_28", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CONEXIÓN", quote = "«La conexión real comienza contigo.»", author = "Psicología (Brené Brown)", reflection = "Conecta contigo."),
        WisdomCard(id = "psi_29", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALENTÍA", quote = "«La valentía es necesaria para el cambio.»", author = "Psicología (Brené Brown)", reflection = "Sé valiente."),
        WisdomCard(id = "psi_30", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "COMPASIÓN", quote = "«Sé compasivo contigo mismo.»", author = "Psicología (Kristin Neff)", reflection = "Ten autocompasión."),
        WisdomCard(id = "psi_31", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "EXPECTATIVAS", quote = "«Bajemos las expectativas.»", author = "Psicología (Walter Riso)", reflection = "Menos expectativas, más paz."),
        WisdomCard(id = "psi_32", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "DEPENDENZA", quote = "«La dependencia es una trampa.»", author = "Psicología (Silvia Congost)", reflection = "Rompe tus cadenas."),
        WisdomCard(id = "psi_33", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "AUTOEXIGENCIA", quote = "«Menos exigencia, más bienestar.»", author = "Psicología (Walter Riso)", reflection = "Sé más amable."),
        WisdomCard(id = "psi_34", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "DIGNIDAD", quote = "«Tu dignidad es tu tesoro.»", author = "Psicología (Silvia Congost)", reflection = "Protégela."),
        WisdomCard(id = "psi_35", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "PAZ", quote = "«La paz es tu derecho.»", author = "Psicología (Walter Riso)", reflection = "Busca tu paz."),
        WisdomCard(id = "psi_36", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "MIEDO", quote = "«Supera tus miedos.»", author = "Psicología (Walter Riso)", reflection = "No los dejes vencer."),
        WisdomCard(id = "psi_37", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tu valor es único.»", author = "Psicología (Silvia Congost)", reflection = "Recuérdalo."),
        WisdomCard(id = "psi_38", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "DECISIÓN", quote = "«Decide por ti.»", author = "Psicología (Walter Riso)", reflection = "Toma las riendas."),
        WisdomCard(id = "psi_39", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "LIBERTAD", quote = "«La libertad empieza en tu mente.»", author = "Psicología (Silvia Congost)", reflection = "Sé libre."),
        WisdomCard(id = "psi_40", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "FELICIDAD", quote = "«La felicidad es tu meta.»", author = "Psicología (Walter Riso)", reflection = "Persíguela."),
        WisdomCard(id = "psi_41", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "EQUILIBRIO", quote = "«Busca el equilibrio.»", author = "Psicología (Silvia Congost)", reflection = "Es vital."),
        WisdomCard(id = "psi_42", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "PRESENTE", quote = "«Vive el aquí y ahora.»", author = "Psicología (Walter Riso)", reflection = "Es lo que tienes."),
        WisdomCard(id = "psi_43", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "FORTALEZA", quote = "«Tu fortaleza es interna.»", author = "Psicología (Silvia Congost)", reflection = "Confía en ella."),
        WisdomCard(id = "psi_44", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CONFIANZA", quote = "«La confianza se construye.»", author = "Psicología (Walter Riso)", reflection = "Empieza por ti."),
        WisdomCard(id = "psi_45", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALORACIÓN", quote = "«Valórate tú primero.»", author = "Psicología (Silvia Congost)", reflection = "Siempre."),
        WisdomCard(id = "psi_46", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CLARIDAD", quote = "«Busca la claridad.»", author = "Psicología (Walter Riso)", reflection = "Es necesaria."),
        WisdomCard(id = "psi_47", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CRECIMIENTO", quote = "«Cresce cada día.»", author = "Psicología (Silvia Congost)", reflection = "Es tu derecho."),
        WisdomCard(id = "psi_48", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMBIO", quote = "«El cambio es parte de ti.»", author = "Psicología (Walter Riso)", reflection = "Aceptalo."),
        WisdomCard(id = "psi_49", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tu valor es incuestionable.»", author = "Psicología (Silvia Congost)", reflection = "Recuérdalo."),
        WisdomCard(id = "psi_50", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "ESPERANZA", quote = "«La esperanza es tu motor.»", author = "Psicología (Walter Riso)", reflection = "Mantén la esperanza."),
        WisdomCard(id = "psi_51", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CALMA", quote = "«La calma es tu refugio.»", author = "Psicología (Silvia Congost)", reflection = "Busca tu refugio."),
        WisdomCard(id = "psi_52", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "SENTIDO", quote = "«Dale sentido a tu vida.»", author = "Psicología (Viktor Frankl)", reflection = "Es tu propósito."),
        WisdomCard(id = "psi_53", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "PAZ", quote = "«La paz interior es tu meta.»", author = "Psicología (Walter Riso)", reflection = "Persíguela."),
        WisdomCard(id = "psi_54", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMINO", quote = "«Tu camino es tuyo.»", author = "Psicología (Silvia Congost)", reflection = "Camínalo."),
        WisdomCard(id = "psi_55", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú vales mucho.»", author = "Psicología (Walter Riso)", reflection = "No lo olvides."),
        WisdomCard(id = "psi_56", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "LIBERTAD", quote = "«Tu libertad es importante.»", author = "Psicología (Silvia Congost)", reflection = "Protégela."),
        WisdomCard(id = "psi_57", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "DECISIÓN", quote = "«Decide tu futuro.»", author = "Psicología (Walter Riso)", reflection = "Tú puedes."),
        WisdomCard(id = "psi_58", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Valórate tú.»", author = "Psicología (Silvia Congost)", reflection = "Es vital."),
        WisdomCard(id = "psi_59", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CRECIMIENTO", quote = "«Crece con fuerza.»", author = "Psicología (Walter Riso)", reflection = "Sé fuerte."),
        WisdomCard(id = "psi_60", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CALMA", quote = "«La calma es necesaria.»", author = "Psicología (Silvia Congost)", reflection = "Respira."),
        WisdomCard(id = "psi_61", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMINO", quote = "«Tú marcas tu camino.»", author = "Psicología (Walter Riso)", reflection = "Sé el autor."),
        WisdomCard(id = "psi_62", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú eres valioso/a.»", author = "Psicología (Silvia Congost)", reflection = "Sí."),
        WisdomCard(id = "psi_63", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "FELICIDAD", quote = "«La felicidad es tuya.»", author = "Psicología (Walter Riso)", reflection = "Disfrútala."),
        WisdomCard(id = "psi_64", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMBIO", quote = "«El cambio comienza en ti.»", author = "Psicología (Silvia Congost)", reflection = "Sé el cambio."),
        WisdomCard(id = "psi_65", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú puedes cambiar.»", author = "Psicología (Walter Riso)", reflection = "Confía en ti."),
        WisdomCard(id = "psi_66", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "PAZ", quote = "«La paz es tu meta.»", author = "Psicología (Walter Riso)", reflection = "Persíguela."),
        WisdomCard(id = "psi_67", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMINO", quote = "«Tú marcas tu camino.»", author = "Psicología (Silvia Congost)", reflection = "Camínalo."),
        WisdomCard(id = "psi_68", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú eres valioso/a.»", author = "Psicología (Walter Riso)", reflection = "Sí."),
        WisdomCard(id = "psi_69", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "FELICIDAD", quote = "«La felicidad es tuya.»", author = "Psicología (Silvia Congost)", reflection = "Disfrútala."),
        WisdomCard(id = "psi_70", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMBIO", quote = "«El cambio comienza en ti.»", author = "Psicología (Walter Riso)", reflection = "Sé el cambio."),
        WisdomCard(id = "psi_71", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú puedes cambiar.»", author = "Psicología (Silvia Congost)", reflection = "Confía en ti."),
        WisdomCard(id = "psi_72", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "PAZ", quote = "«La paz es tu meta.»", author = "Psicología (Walter Riso)", reflection = "Persíguela."),
        WisdomCard(id = "psi_73", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMINO", quote = "«Tú marcas tu camino.»", author = "Psicología (Silvia Congost)", reflection = "Camínalo."),
        WisdomCard(id = "psi_74", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú eres valioso/a.»", author = "Psicología (Walter Riso)", reflection = "Sí."),
        WisdomCard(id = "psi_75", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "FELICIDAD", quote = "«La felicidad es tuya.»", author = "Psicología (Silvia Congost)", reflection = "Disfrútala."),
        WisdomCard(id = "psi_76", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMBIO", quote = "«El cambio comienza en ti.»", author = "Psicología (Walter Riso)", reflection = "Sé el cambio."),
        WisdomCard(id = "psi_77", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú puedes cambiar.»", author = "Psicología (Silvia Congost)", reflection = "Confía en ti."),
        WisdomCard(id = "psi_78", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "PAZ", quote = "«La paz es tu meta.»", author = "Psicología (Walter Riso)", reflection = "Persíguela."),
        WisdomCard(id = "psi_79", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMINO", quote = "«Tú marcas tu camino.»", author = "Psicología (Silvia Congost)", reflection = "Camínalo."),
        WisdomCard(id = "psi_80", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú eres valioso/a.»", author = "Psicología (Walter Riso)", reflection = "Sí."),
        WisdomCard(id = "psi_81", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "FELICIDAD", quote = "«La felicidad es tuya.»", author = "Psicología (Silvia Congost)", reflection = "Disfrútala."),
        WisdomCard(id = "psi_82", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMBIO", quote = "«El cambio comienza en ti.»", author = "Psicología (Walter Riso)", reflection = "Sé el cambio."),
        WisdomCard(id = "psi_83", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú puedes cambiar.»", author = "Psicología (Silvia Congost)", reflection = "Confía en ti."),
        WisdomCard(id = "psi_84", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "PAZ", quote = "«La paz es tu meta.»", author = "Psicología (Walter Riso)", reflection = "Persíguela."),
        WisdomCard(id = "psi_85", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMINO", quote = "«Tú marcas tu camino.»", author = "Psicología (Silvia Congost)", reflection = "Camínalo."),
        WisdomCard(id = "psi_86", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú eres valioso/a.»", author = "Psicología (Walter Riso)", reflection = "Sí."),
        WisdomCard(id = "psi_87", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "FELICIDAD", quote = "«La felicidad es tuya.»", author = "Psicología (Silvia Congost)", reflection = "Disfrútala."),
        WisdomCard(id = "psi_88", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "CAMBIO", quote = "«El cambio comienza en ti.»", author = "Psicología (Walter Riso)", reflection = "Sé el cambio."),
        WisdomCard(id = "psi_89", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "VALOR", quote = "«Tú puedes cambiar.»", author = "Psicología (Silvia Congost)", reflection = "Confía en ti."),
        WisdomCard(id = "psi_90", framework = SoltarFramework.PSICOLOGIA_MODERNA, title = "PAZ", quote = "«La paz es tu meta.»", author = "Psicología (Walter Riso)", reflection = "Persíguela."),

        // ==========================================
        // 3. CATÓLICO (Proverbios, Sabiduría Bíblica y Tradición - Esperanza y Perdón)
        // ==========================================
        // (Existing cards... I have to target the first cat card now)

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
        WisdomCard(id = "cat_91", framework = SoltarFramework.CATOLICO, title = "AMOR", quote = "«El amor es paciente.»", author = "1 Corintios 13:4", reflection = "Sé paciente."),
        WisdomCard(id = "cat_92", framework = SoltarFramework.CATOLICO, title = "ESPERANZA", quote = "«La esperanza es un ancla.»", author = "Hebreos 6:19", reflection = "Confía."),
        WisdomCard(id = "cat_93", framework = SoltarFramework.CATOLICO, title = "PAZ", quote = "«La paz sea contigo.»", author = "Juan 20:19", reflection = "Recíbela."),
        WisdomCard(id = "cat_94", framework = SoltarFramework.CATOLICO, title = "DIOS", quote = "«Dios es amor.»", author = "1 Juan 4:8", reflection = "Ámalo."),
        WisdomCard(id = "cat_95", framework = SoltarFramework.CATOLICO, title = "SABIDURÍA", quote = "«Pide sabiduría.»", author = "Santiago 1:5", reflection = "Búscala."),
        WisdomCard(id = "cat_96", framework = SoltarFramework.CATOLICO, title = "FUERZA", quote = "«Tú eres fuerte.»", author = "Salmo 28:7", reflection = "Recuérdalo."),
        WisdomCard(id = "cat_97", framework = SoltarFramework.CATOLICO, title = "LUZ", quote = "«Tú eres la luz.»", author = "Mateo 5:14", reflection = "Brilla."),
        WisdomCard(id = "cat_98", framework = SoltarFramework.CATOLICO, title = "AMOR", quote = "«Ama a tu prójimo.»", author = "Marcos 12:31", reflection = "Ama."),
        WisdomCard(id = "cat_99", framework = SoltarFramework.CATOLICO, title = "ESPERANZA", quote = "«La esperanza no defrauda.»", author = "Romanos 5:5", reflection = "Confía."),
        WisdomCard(id = "cat_100", framework = SoltarFramework.CATOLICO, title = "PAZ", quote = "«La paz está en ti.»", author = "Filipenses 4:7", reflection = "Siéntela."),
        WisdomCard(id = "cat_101", framework = SoltarFramework.CATOLICO, title = "DIOS", quote = "«Dios te ama.»", author = "Jeremías 31:3", reflection = "Acéptalo."),
        WisdomCard(id = "cat_102", framework = SoltarFramework.CATOLICO, title = "SABIDURÍA", quote = "«La sabiduría es un don.»", author = "Santiago 1:5", reflection = "Agradécelo."),
        WisdomCard(id = "cat_103", framework = SoltarFramework.CATOLICO, title = "FUERZA", quote = "«Tú puedes con todo.»", author = "Filipenses 4:13", reflection = "Sí puedes."),
        WisdomCard(id = "cat_104", framework = SoltarFramework.CATOLICO, title = "LUZ", quote = "«Sigue la luz.»", author = "Juan 8:12", reflection = "Camina."),
        WisdomCard(id = "cat_105", framework = SoltarFramework.CATOLICO, title = "AMOR", quote = "«El amor nunca muere.»", author = "1 Corintios 13:8", reflection = "Ámalo."),
        WisdomCard(id = "cat_106", framework = SoltarFramework.CATOLICO, title = "ESPERANZA", quote = "«Ten esperanza.»", author = "Salmo 31:24", reflection = "Confía."),
        WisdomCard(id = "cat_107", framework = SoltarFramework.CATOLICO, title = "PAZ", quote = "«La paz es tuya.»", author = "Juan 14:27", reflection = "Recíbela."),
        WisdomCard(id = "cat_108", framework = SoltarFramework.CATOLICO, title = "DIOS", quote = "«Dios está contigo.»", author = "Josué 1:9", reflection = "Confía."),
        WisdomCard(id = "cat_109", framework = SoltarFramework.CATOLICO, title = "SABIDURÍA", quote = "«La sabiduría es tu guía.»", author = "Proverbios 4:7", reflection = "Síguela."),
        WisdomCard(id = "cat_110", framework = SoltarFramework.CATOLICO, title = "FUERZA", quote = "«Dios es tu fortaleza.»", author = "Salmo 46:1", reflection = "Apóyate."),
        WisdomCard(id = "cat_111", framework = SoltarFramework.CATOLICO, title = "LUZ", quote = "«La luz brilla siempre.»", author = "Juan 1:5", reflection = "Confía."),
        WisdomCard(id = "cat_112", framework = SoltarFramework.CATOLICO, title = "AMOR", quote = "«El amor es el mayor don.»", author = "1 Corintios 13:13", reflection = "Ámalo."),
        WisdomCard(id = "cat_113", framework = SoltarFramework.CATOLICO, title = "ESPERANZA", quote = "«La esperanza es real.»", author = "Romanos 15:13", reflection = "Ten fe."),
        WisdomCard(id = "cat_114", framework = SoltarFramework.CATOLICO, title = "PAZ", quote = "«La paz reina en ti.»", author = "Colosenses 3:15", reflection = "Siéntela."),
        WisdomCard(id = "cat_115", framework = SoltarFramework.CATOLICO, title = "DIOS", quote = "«Dios es tu refugio.»", author = "Salmo 91:2", reflection = "Confía."),
        WisdomCard(id = "cat_116", framework = SoltarFramework.CATOLICO, title = "SABIDURÍA", quote = "«La sabiduría es vida.»", author = "Proverbios 3:18", reflection = "Vive con ella."),
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
