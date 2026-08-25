package com.example.data

import com.example.ai.ReadingChapter
import com.example.ai.ReadingPill

object ThinkerRefugeData {

    val READING_PILLS = listOf(
        ReadingPill(
            id = "p1",
            author = "Marco Aurelio (Meditaciones)",
            category = "Estoicismo Clásico",
            textBody = "«Tienes poder sobre tu mente, no sobre los acontecimientos externos. Date cuenta de esto y encontrarás la fortaleza inquebrantable de tu ciudadela interior.»"
        ),
        ReadingPill(
            id = "p2",
            author = "Gabriel Rolón (La Pasión y el Duelo)",
            category = "Psicoanálisis del Duelo",
            textBody = "«El duelo no es olvidar lo que se amó, sino encontrarle un lugar digno al dolor en la propia biografía para no seguir desangrándose en el presente.»"
        ),
        ReadingPill(
            id = "p3",
            author = "Dr. Joe Dispenza (Sobrenatural)",
            category = "Neurociencia Aplicada",
            textBody = "«No puedes crear un nuevo futuro si sigues pensando y sintiendo con el mismo estado de ser que construyó tu pasado adictivo.»"
        ),
        ReadingPill(
            id = "p4",
            author = "Séneca (Cartas a Lucilio)",
            category = "Estoicismo Clásico",
            textBody = "«Sufrimos más a menudo en la imaginación que en la realidad. Acostúmbrate a evaluar los hechos con juicio objetivo sin carga emocional.»"
        ),
        ReadingPill(
            id = "p5",
            author = "Salmo 139 (Poesía Sapiencial)",
            category = "Lamento Estructural",
            textBody = "«Si dijere: Ciertamente las tinieblas me encubrirán; aun la noche resplandecerá alrededor de mí en la intemperie.»"
        ),
        ReadingPill(
            id = "p6",
            author = "Epicteto (Enquiridión)",
            category = "Estoicismo Clásico",
            textBody = "«No son las cosas que nos pasan las que nos perturban, sino los juicios y fantasías que construimos sobre esas cosas.»"
        ),
        ReadingPill(
            id = "p7",
            author = "Dr. Andrew Huberman (Stanford Neurobiology)",
            category = "Neurociencia Aplicada",
            textBody = "«El impulso obsesivo por buscar a la expareja activa las mismas vías dopaminérgicas que la abstinencia severa. Reconocer la neuroquímica le quita el velo romántico al impulso.»"
        ),
        ReadingPill(
            id = "p8",
            author = "Sigmund Freud (Duelo y Melancolía)",
            category = "Psicoanálisis",
            textBody = "«En el duelo, el mundo se ha vuelto pobre y vacío; en la melancolía, es el propio yo el que se siente empobrecido hasta recuperar su soberanía.»"
        )
    )

    val READING_CHAPTERS = listOf(
        ReadingChapter(
            id = "c1",
            title = "La Ciudadela Interior y la Dicotomía del Control",
            author = "Marco Aurelio & Séneca",
            estimatedReadTimeMin = 3,
            category = "Estoicismo Clásico",
            textBody = """
## LA CIUDADELA INTERIOR Y LA DICOTOMÍA DEL CONTROL

### I. El Dominio de los Juicios
Todo lo que nos rodea se divide en dos categorías absolutas: aquello que depende estrictamente de nuestras decisiones (nuestros pensamientos, principios y acciones) y aquello que está completamente fuera de nuestro control (las decisiones de otros, el pasado, las traiciones y las respuestas emocionales ajenas).

### II. Desarticular el Juicio de Perjuicio
Si alguien decide alejarse de tu vida o faltar a la lealtad, la pérdida no es tuya. Quien pierde la virtud y el honor es quien actúa con injusticia. Tu deber es mantener la serenidad dentro de tu propia fortaleza mental.

***«Retira la opinión de 'se me ha hecho un daño', y el daño mismo desaparece inmediatamente.» — Marco Aurelio***

### III. Práctica Diaria
1. Cada vez que sientas un pinchazo de angustia, pregúntate: *¿Esta circunstancia depende de mi conducta presente?*
2. Si la respuesta es no, declárala irrelevante para tu dignidad actual.
            """.trimIndent(),
            isCoreOnly = false
        ),
        ReadingChapter(
            id = "c2",
            title = "Desarticulación Neural del Pasado",
            author = "Dr. Joe Dispenza",
            estimatedReadTimeMin = 4,
            category = "Neurociencia Aplicada",
            textBody = """
## DESARTICULACIÓN NEURAL DEL PASADO ADICTIVO

### I. El Bucle Emocional-Fisiológico
Cuando revives repetidamente los recuerdos de tu relación pasada, tus neuronas disparan en las mismas redes de circuitos aprendidos. Cada pensamiento nostálgico desencadena una cascada de neuropéptidos y cortisol que tu cuerpo ha aprendido a anticipar como una dosis química.

### II. Romper la Adicción al Dolor
El cerebro no distingue entre la experiencia real presente y la memoria evocada con alta carga emocional. Para el cuerpo, recordar el rechazo equivale a sufrir el rechazo hoy mismo.

***«Para cambiar tu vida, debes cambiar tu estado biológico de ser. No puedes sanar en el mismo entorno neuronal que te enfermó.» — Dr. Joe Dispenza***

### III. Protocolo de Reconfiguración
- **Observación Consciente:** Reconoce el pensamiento sin juzgarlo como "mío". Obsérvalo como un impulso de disparo red sináptica.
- **Interrupción Somática:** Ejecuta un suspiro fisiológico para cortar la señal simpática en el tronco encefálico.
            """.trimIndent(),
            isCoreOnly = false
        ),
        ReadingChapter(
            id = "c3",
            title = "La Caída de la Estatua de Humo: Idealización vs Realidad",
            author = "Gabriel Rolón",
            estimatedReadTimeMin = 5,
            category = "Psicoanálisis del Duelo",
            textBody = """
## LA CAÍDA DE LA ESTATUA DE HUMO: IDEALIZACIÓN Y DUELO

### I. La Trampa de la Memoria Selectiva
Durante las primeras etapas de la separación, el psiquismo doliente construye una trampa desoladora: edifica una estatua de humo idealizada sobre el ex. Se borran los desprecios, la falta de compromiso y las frías despedidas, dejando únicamente la nostalgia de lo que pudo haber sido.

### II. El Dolor Necesario
El dolor del duelo no es un enemigo que deba adormecerse con anestésicos o distracciones vacías. Es el precio que paga el sujeto soberano por haber apostado su afecto en un territorio donde el otro decidió no responder.

***«El verdadero trabajo del duelo no consiste en olvidar, sino en desarmar el pedestal sobre el que pusimos a quien nos dolió.» — Gabriel Rolón***

### III. Pasos de Liberación
1. Escribe los hechos fríos sin poesía ni disculpas.
2. Acepta que la persona que extrañas hoy solo existe en tu recuerdo idealizado.
            """.trimIndent(),
            isCoreOnly = true
        ),
        ReadingChapter(
            id = "c4",
            title = "El Lamento Estructural y la Dignidad en la Intemperie",
            author = "Salmos Sapienciales",
            estimatedReadTimeMin = 4,
            category = "Poesía Sapiencial",
            textBody = """
## EL LAMENTO ESTRUCTURAL Y LA DIGNIDAD EN LA INTEMPERIE

### I. El Grito Desde el Desamparo
La tradición sapiencial enseña que la tristeza no es una debilidad moral, sino la reacción natural del alma noble frente a la intemperie y la traición. El lamento auténtico no busca consuelos superficiales, sino la firmeza espiritual para atravesar el valle.

### II. La Noche Brillará
Aun en la más profunda noche de soledad, la luz de la conciencia y la integridad interior no pueden ser apagadas por voluntades ajenas.

***«Aunque camine por el valle de la sombra, mi corazón no temerá la desolación, porque la verdad habita en mi fortaleza.» — Salmo 23 (Reframing Estoico)***

### III. Affirmación de Soberanía
- Acepto mi tristeza como el proceso de purificación de mi templo interior.
- Mi dignidad no depende de la aprobación de quien decidió soltar mi mano.
            """.trimIndent(),
            isCoreOnly = true
        )
    )
}
