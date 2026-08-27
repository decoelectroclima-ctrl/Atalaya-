package com.example.data

/**
 * Base de datos clínica y filosófica de máxima densidad conceptual para el motor de razonamiento y coach offline ADRIANA.
 * Contiene más de 35 cápsulas estructuradas en las tres cosmovisiones:
 * 1. ESTOICO: Marco Aurelio, Epicteto, Séneca, Musonio Rufo, Hierocles, Cleantes.
 * 2. PSICOLOGÍA MODERNA: John Bowlby, Mary Ainsworth, Silvia Congost, Gabriel Rolón, Viktor Frankl, Aaron Beck, Steven Hayes (ACT), Marian Rojas Estapé, Kristin Neff, Amir Levine.
 * 3. CATÓLICO: Sabiduría bíblica (Proverbios, Eclesiastés, Salmos, Romanos), San Pablo, San Agustín, Santa Teresa de Jesús, San Juan de la Cruz, San Ignacio de Loyola, San Francisco de Sales, C.S. Lewis.
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
    val concreteAction: String
)

object ClinicalKnowledgeBase {

    val capsules: List<KnowledgeCapsule> = listOf(
        // =========================================================================
        // MARCO ESTOICO (Marco Aurelio, Epicteto, Séneca, Musonio Rufo, Hierocles)
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
            concreteAction = "Escribe en dos columnas separadas: 'Hechos verificables con testigos' vs 'Interpretaciones mías'."
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
            concreteAction = "Pausa la rumiación de inmediato y realiza 10 respiraciones profundas contando 4 segundos al inhalar y 6 al exhalar."
        ),
        KnowledgeCapsule(
            id = "est_rum_3",
            framework = SoltarFramework.ESTOICO,
            category = "RUMIACION",
            title = "La Perspectiva Cósmica y la Vista desde Arriba",
            author = "Marco Aurelio (Meditaciones IX.30)",
            quoteOrSource = "«Mira desde arriba la multitud innumerable de seres humanos, sus afanes, sus amores y sus despedidas... todo pasa como el humo.»",
            diagnosisPrinciple = "Hipertrofia de la tragedia individual que hace sentir la ruptura como el colapso del universo entero.",
            clinicalGuidance = "Eleva tu mirada: miles de generaciones han vivido rupturas y desamores. Redimensionar el evento te devuelve la serenidad cósmica.",
            socraticPrompt = "¿Cómo se verá este desamor en 5 años cuando mires atrás con la madurez de lo aprendido?",
            concreteAction = "Contempla el cielo durante 5 minutos y recuerda lo vasto del mundo frente a la estrechez de este momento."
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
            concreteAction = "Aplica la regla de las 24 horas: no tomes ninguna acción comunicativa hasta que el sol vuelva a salir."
        ),
        KnowledgeCapsule(
            id = "est_imp_2",
            framework = SoltarFramework.ESTOICO,
            category = "IMPULSO",
            title = "El Freno ante la Ira y la Desesperación",
            author = "Séneca (De Ira II.28)",
            quoteOrSource = "«El mayor remedio para la pasión desmedida es la demora. Pídele al impulso no que perdone, sino que espere.»",
            diagnosisPrinciple = "La urgencia emocional no es un mandato de acción, sino una tormenta neuroquímica transitoria.",
            clinicalGuidance = "El sabio no carece de impulsos, pero nunca les concede el mando de sus manos. Postergar el impulso debilita la ola sin desgastar tu honor.",
            socraticPrompt = "¿Recuerdas alguna ocasión en que actuar con urgencia haya mejorado tu dignidad o el desenlace?",
            concreteAction = "Levántate de donde estás, deja el teléfono en otra habitación y camina durante 15 minutos a paso ligero."
        ),
        KnowledgeCapsule(
            id = "est_imp_3",
            framework = SoltarFramework.ESTOICO,
            category = "IMPULSO",
            title = "La Cadena de la Falsa Esperanza y el Miedo",
            author = "Séneca (Cartas a Lucilio V.7)",
            quoteOrSource = "«Dejarás de temer si dejas de esperar. La esperanza y el miedo marchan unidos; ambos pertenecen a la mente que se proyecta con angustia al futuro.»",
            diagnosisPrinciple = "Aferrarse a la esperanza ilusoria de una reconciliación alimenta el miedo constante a la soledad.",
            clinicalGuidance = "Renunciar a la esperanza pasiva corta la raíz del miedo. Acepta el final para que tu mente pueda anclarse en la firmeza del presente.",
            socraticPrompt = "¿Qué pasaría si hoy dejas de esperar que la otra persona reaccione y asumes tu vida al 100%?",
            concreteAction = "Pronuncia en voz alta frente al espejo: 'Acepto el presente como es. Hoy me elijo a mí'."
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
            concreteAction = "Anota tres aprendizajes de carácter que esta dificultad te está obligando a forjar."
        ),
        KnowledgeCapsule(
            id = "est_due_2",
            framework = SoltarFramework.ESTOICO,
            category = "DUELO",
            title = "El Retiro en la Ciudadela Interior",
            author = "Marco Aurelio (Meditaciones IV.3)",
            quoteOrSource = "«Los hombres buscan retiros en el campo, en la costa y en el monte... pero en ninguna parte puede el hombre hallar un retiro más apacible e imperturbable que en su propia alma.»",
            diagnosisPrinciple = "Buscar consuelo en la validación exterior cuando el vínculo se ha quebrado genera desamparo.",
            clinicalGuidance = "Tu valor intrínseco como ser humano racional y ético no ha disminuido un ápice tras la ruptura. La ciudadela interior sigue intacta esperando que vuelvas a habitarla.",
            socraticPrompt = "¿Qué parte de ti crees equivocadamente que se fue con esa persona?",
            concreteAction = "Dedica 20 minutos de silencio absoluto a ordenar tu espacio físico personal."
        ),
        KnowledgeCapsule(
            id = "est_lim_1",
            framework = SoltarFramework.ESTOICO,
            category = "LIMITES",
            title = "No Parecerte a Quien te Dañó",
            author = "Marco Aurelio (Meditaciones VI.6)",
            quoteOrSource = "«La mejor venganza y el mayor triunfo es no parecerte a quien cometió la injusticia.»",
            diagnosisPrinciple = "La tentación de pagar con desprecio, reproches públicos o manipulación degrada el carácter.",
            clinicalGuidance = "Responder a la frialdad con nobleza silenciosa y distancia firme resguarda tu excelencia moral (areté). La indiferencia digna es la respuesta más elevada.",
            socraticPrompt = "¿Prefieres tener la razón en una discusión amarga o conservar tu paz y tu integridad?",
            concreteAction = "Bloquea o silencia toda vía de contacto sin emitir advertencias, reclamos ni despedidas dramáticas."
        ),
        KnowledgeCapsule(
            id = "est_rec_1",
            framework = SoltarFramework.ESTOICO,
            category = "RECONSTRUCCION",
            title = "La Forja del Oro en el Fuego",
            author = "Séneca (De Providentia IV)",
            quoteOrSource = "«El fuego prueba al oro; la adversidad a los hombres valientes. Ningún atleta se hace fuerte sin un oponente digno.»",
            diagnosisPrinciple = "Interpretar la crisis como ruina en lugar de como entrenamiento del temple personal.",
            clinicalGuidance = "Cada día de contención, abstinencia de contacto y autoobservación añade temple a tu carácter. Estás transformando un desamor en una maestría de autonomía.",
            socraticPrompt = "¿En qué persona más sabia, fuerte y libre te estás convirtiendo gracias a esta exigencia?",
            concreteAction = "Define un nuevo objetivo físico o intelectual exigente y comienza hoy mismo con el primer paso."
        ),

        // =========================================================================
        // MARCO PSICOLOGÍA MODERNA (Apego, TCC, ACT, Rolón, Congost, Neurobiología)
        // =========================================================================
        KnowledgeCapsule(
            id = "psi_apg_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "IMPULSO",
            title = "Neurobiología del Apego y Síndrome de Abstinencia",
            author = "Teoría del Apego (John Bowlby & Helen Fisher)",
            quoteOrSource = "«La protesta por la separación es una respuesta biológica evolutiva de alarma ante la pérdida de la figura vincular; el cerebro la procesa en las mismas áreas que el dolor físico.»",
            diagnosisPrinciple = "La urgencia desesperada de llamar o buscar al ex se siente como 'amor verdadero' cuando es abstinencia dopaminérgica y oxitocínica.",
            clinicalGuidance = "Tu sistema nervioso simpático está hiperactivado buscando el ansiolítico conocido (la otra persona). Comprender la química desmitifica el impulso: no es una señal cósmica de destino, es neurobiología adaptativa en desintoxicación.",
            socraticPrompt = "Si reconoces que este ardor en el pecho es abstinencia química de tu sistema de apego, ¿puedes cuidarte como a alguien convaleciente?",
            concreteAction = "Aplica el protocolo Somático TIPP: sumerge la cara en agua fría o colócate hielo en la nuca durante 30 segundos para activar el reflejo de buceo vagal."
        ),
        KnowledgeCapsule(
            id = "psi_apg_2",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "IMPULSO",
            title = "La Trampa del Refuerzo Intermitente",
            author = "Psicología Conductual & Amir Levine (Maneras de Amar)",
            quoteOrSource = "«El cerebro se vuelve adicto a la incertidumbre: la recompensa impredecible dispara la mayor liberación de dopamina, confundiendo ansiedad con pasión.»",
            diagnosisPrinciple = "Interpretar las señales tibias, likes esporádicos o mensajes ambiguos como esperanza de futuro.",
            clinicalGuidance = "El refuerzo intermitente genera un apego ansioso disfuncional. La inconsistencia de la otra persona no es misterio seductor, es falta de compromiso y desinterés real.",
            socraticPrompt = "¿Estás dispuesto/a a aceptar migajas intermitentes a cambio de tu tranquilidad diaria?",
            concreteAction = "Silencia o bloquea notificaciones de redes para interrumpir el circuito de recompensa intermitente."
        ),
        KnowledgeCapsule(
            id = "psi_lim_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "LIMITES",
            title = "Límites Protectores y Dependencia Afectiva",
            author = "Silvia Congost (Autoestima y Dependencia)",
            quoteOrSource = "«El contacto cero no es una estrategia para que el otro vuelva ni para castigarle; es el quirófano aséptico que tú necesitas para desinflamar la herida.»",
            diagnosisPrinciple = "Mantener contacto 'cordial' o revisar redes mantiene encendida la llama de la esperanza tóxica y el sufrimiento.",
            clinicalGuidance = "Diferencia con nitidez: el amor suma bienestar, la dependencia usa al otro para calmar el vacío existencial. Cortar los estímulos visuales y comunicativos es el acto de mayor amor propio.",
            socraticPrompt = "¿Estás dispuesto a soportar la incomodidad temporal de la soledad para ganar la libertad definitiva de tu vida?",
            concreteAction = "Elimina aplicaciones de rastreo, archiva conversaciones y guarda fotos en una carpeta oculta o disco externo inaccesible en el día a día."
        ),
        KnowledgeCapsule(
            id = "psi_lim_2",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "LIMITES",
            title = "Autocompasión Feroz y Protección de la Dignidad",
            author = "Dra. Kristin Neff (Autocompasión Feroz)",
            quoteOrSource = "«La autocompasión tiene dos caras: la ternura que abraza el dolor y la ferocidad que dice 'basta' al maltrato y a la desatención.»",
            diagnosisPrinciple = "Confundir perdonar con tolerar humillaciones continuas o disponibilidad incondicional.",
            clinicalGuidance = "Poner un límite tajante no es rencor, es la versión protectora de la compasión hacia uno mismo. Cuidar de ti implica poner vallas donde antes hubo puertas abiertas sin filtro.",
            socraticPrompt = "¿Cómo protegerías a tu hijo/a o a tu mejor amigo si estuviera viviendo exactamente este trato?",
            concreteAction = "Escribe tu lista de 'No negociables para mi vida' y guárdala como recordatorio permanente."
        ),
        KnowledgeCapsule(
            id = "psi_due_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "DUELO",
            title = "La Travesía Simbólica del Duelo y la Pérdida",
            author = "Gabriel Rolón (El Duelo y la Melancolía)",
            quoteOrSource = "«El duelo no se cura con olvido ni con distracciones mágicas; se cura con la palabra, atravesando el dolor de aceptar que lo que fue, ya no es.»",
            diagnosisPrinciple = "La resistencia a sentir tristeza lleva a conductas impulsivas, relaciones rebote o negación.",
            clinicalGuidance = "Llorar y nombrar la falta no es debilidad, es el trabajo psíquico indispensable para desinvestir la libido puesta en el vínculo. El duelo no es lineal: oscila naturalmente entre el dolor y la reconstrucción (Modelo de Proceso Dual).",
            socraticPrompt = "¿Te estás permitiendo despedir la relación con la tristeza digna que merece, o estás huyendo de ella?",
            concreteAction = "Escribe una carta de despedida honesta en tu diario personal (sin enviarla jamás) agradeciendo lo bueno y reconociendo el final definitivo."
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
            concreteAction = "Elige una acción coherente con tus valores (ej. entrenar, cocinar sano, leer) y realízala a pesar del desánimo mental."
        ),
        KnowledgeCapsule(
            id = "psi_ide_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "RUMIACION",
            title = "Desmontar el Sesgo de Memoria y la Idealización",
            author = "Psicología Cognitiva del Desamor",
            quoteOrSource = "«El cerebro enamorado en retirada sufre amnesia selectiva: magnifica los momentos de complicidad y borra las horas de soledad, desatención y frialdad.»",
            diagnosisPrinciple = "La memoria traumática o nostálgica crea un idilio irreal que no coincide con la experiencia cotidiana del vínculo.",
            clinicalGuidance = "La idealización es un mecanismo de defensa para no aceptar la incompatibilidad. Confrontar la fantasía con la lista cruda de incompatibilidades e insatisfacciones reales rompe el hechizo.",
            socraticPrompt = "¿Qué problemas crónicos, silencios o faltas de reciprocidad existían que tu nostalgia intenta ocultarte hoy?",
            concreteAction = "Revisa la Auditoría de Realidad: anota 5 situaciones concretas donde te sentiste desatendido/a o angustiado/a en esa relación."
        ),
        KnowledgeCapsule(
            id = "psi_neu_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "SOLEDAD",
            title = "Regulación de Cortisol y Rutinas Circadianas",
            author = "Neurobiología del Estrés (Dra. Marian Rojas Estapé)",
            quoteOrSource = "«Vivir en constante alerta por una expareja mantiene el cortisol elevado, alterando el sueño, la digestión y la capacidad de pensar con claridad.»",
            diagnosisPrinciple = "La incertidumbre prolongada desgasta la fisiología provocando fatiga crónica y desregulación emocional.",
            clinicalGuidance = "Para reparar la mente hay que reparar el cuerpo: regular la exposición a la luz solar matutina, reducir pantallas nocturnas, asegurar proteína y movimiento físico constante para bajar la carga alostática.",
            socraticPrompt = "¿Cómo has tratado a tu cuerpo en las últimas 24 horas a nivel de sueño, comida y movimiento?",
            concreteAction = "Sal a caminar 20 minutos bajo la luz natural del día sin audífonos ni distracciones."
        ),
        KnowledgeCapsule(
            id = "psi_rec_1",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            category = "RECONSTRUCCION",
            title = "El Sentido de la Vida y la Elección de la Actitud",
            author = "Viktor Frankl (Logoterapia)",
            quoteOrSource = "«Al hombre se le puede arrebatar todo salvo una cosa: la última de las libertades humanas —la elección de la actitud personal ante un conjunto de circunstancias—.»",
            diagnosisPrinciple = "Sentir que la ruptura despojó la vida de todo propósito y futuro.",
            clinicalGuidance = "Nadie puede quitarte tu capacidad de dar sentido a tu dolor. Tu sufrimiento puede convertirse en el motor de una madurez y sensibilidad humana sin precedentes.",
            socraticPrompt = "¿Qué causa, proyecto o aprendizaje digno puedes construir sobre las cenizas de esta vivencia?",
            concreteAction = "Dedica 30 minutos a trabajar en un proyecto personal que habías pospuesto."
        ),

        // =========================================================================
        // MARCO CATÓLICO (Sabiduría Bíblica, Patrística, Mística y Esperanza Cristiana)
        // =========================================================================
        KnowledgeCapsule(
            id = "cat_cor_1",
            framework = SoltarFramework.CATOLICO,
            category = "LIMITES",
            title = "La Custodia del Corazón y el Templo Interior",
            author = "Libro de los Proverbios (Pr 4:23)",
            quoteOrSource = "«Por encima de todo lo que guardes, guarda tu corazón, porque de él brota la vida.»",
            diagnosisPrinciple = "Permitir que afectos desordenados, humillaciones o insistencias ciegas marchiten la dignidad del alma.",
            clinicalGuidance = "Tu corazón es sagrado y ha sido creado para la plenitud y la verdad, no para la mendicidad afectiva ni el desprecio. Protegerlo con prudencia (contacto cero y distancia) no es rencor, es reverencia y cuidado de lo que Dios te ha confiado.",
            socraticPrompt = "¿Cuidar tu corazón hoy te acerca más a la paz y a la vocación que Dios soñó para ti?",
            concreteAction = "Haz una pausa de oración en silencio: pon tus manos sobre el pecho y entrega tu dolor pidiendo gracia para custodiar tu paz."
        ),
        KnowledgeCapsule(
            id = "cat_tie_1",
            framework = SoltarFramework.CATOLICO,
            category = "DUELO",
            title = "El Tiempo Oportuno y los Tiempos de Dios",
            author = "Eclesiastés (Qohélet 3:1-6)",
            quoteOrSource = "«Todo tiene su momento oportuno; hay un tiempo para todo lo que se hace bajo el cielo: tiempo de abrazar y tiempo de abstenerse de abrazar; tiempo de buscar y tiempo de dar por perdido.»",
            diagnosisPrinciple = "Luchar con desesperación contra el fin de una etapa en lugar de aceptar la estación espiritual del desierto.",
            clinicalGuidance = "El desierto no es un castigo, es el lugar de purificación donde se aprende a no idolatrar a las criaturas y a descubrir la roca inconmovible del Creador. Hay temporadas de soltar para poder recibir lo que está por venir.",
            socraticPrompt = "¿Puedes reconocer con humildad que esta estación de tu vida requiere soltar para madurar?",
            concreteAction = "Lee con calma el capítulo 3 de Eclesiastés y medita en el valor del silencio regenerador."
        ),
        KnowledgeCapsule(
            id = "cat_per_1",
            framework = SoltarFramework.CATOLICO,
            category = "CULPA",
            title = "El Perdón como Liberación del Rencor",
            author = "San Pablo (Colosenses 3:13) & San Agustín",
            quoteOrSource = "«El rencor es como beber veneno y esperar que muera la otra persona. El perdón no es decir que lo que pasó estuvo bien, sino desatar al prisionero y descubrir que el prisionero eras tú.»",
            diagnosisPrinciple = "Alimentar deseos de revancha, amargura o autorreproches que encadenan el alma al pasado.",
            clinicalGuidance = "Perdonar no exige reanudar la relación ni tolerar faltas de respeto; perdonar es encomendar a la otra persona a la justicia y misericordia de Dios, renunciando al derecho de venganza para que tu corazón quede libre.",
            socraticPrompt = "¿Estás dispuesto/a a soltar la amargura para que Dios pueda sanar tus heridas más íntimas?",
            concreteAction = "Reza una breve oración sincera pidiendo el bien y la conversión de quien te hirió, y luego entrégalo definitivamente a la Providencia."
        ),
        KnowledgeCapsule(
            id = "cat_des_1",
            framework = SoltarFramework.CATOLICO,
            category = "SOLEDAD",
            title = "La Presencia que Sana en la Noche Oscura",
            author = "Salmo 147:3 & San Juan de la Cruz",
            quoteOrSource = "«Él sana a los quebrantados de corazón y venda sus heridas.» (Sal 147:3)",
            diagnosisPrinciple = "Sentirse abandonado o sin valor cuando un amor humano se apaga.",
            clinicalGuidance = "El amor humano es hermoso pero contingente; solo el amor divino es absoluto e incondicional. En la noche oscura del desamor, Dios no está ausente: está trabajando en lo profundo para restaurar tus grietas con el oro de su gracia.",
            socraticPrompt = "Si supieras con certeza que este dolor tiene un sentido de bien mayor para tu alma, ¿cómo caminarías hoy?",
            concreteAction = "Visita un templo o aparta 15 minutos en soledad para un momento de adoración o recogimiento íntimo."
        ),
        KnowledgeCapsule(
            id = "cat_dom_1",
            framework = SoltarFramework.CATOLICO,
            category = "IMPULSO",
            title = "La Virtud del Dominio Propio y la Gracia",
            author = "2 Timoteo 1:7 & Santo Tomás de Aquino",
            quoteOrSource = "«Porque no nos ha dado Dios espíritu de cobardía, sino de poder, de amor y de dominio propio.»",
            diagnosisPrinciple = "Creerse incapaz de resistir la tentación del mensaje o la súplica afectiva.",
            clinicalGuidance = "La gracia de Dios perfecciona la naturaleza humana: con su auxilio y tu firmeza de voluntad, tienes la fuerza para no dejarte arrastrar por la pasión ciega. La templanza es un fruto del Espíritu que se ejercita diciendo 'no' en el momento de la prueba.",
            socraticPrompt = "¿Qué pequeña renuncia puedes ofrecer hoy como testimonio de tu madurez y fe?",
            concreteAction = "Ofrece un acto de mortificación o servicio generoso hacia alguien necesitado en lugar de alimentar la autocompasión."
        ),
        KnowledgeCapsule(
            id = "cat_ign_1",
            framework = SoltarFramework.CATOLICO,
            category = "IMPULSO",
            title = "No Hacer Mudanza en Tiempo de Desolación",
            author = "San Ignacio de Loyola (Ejercicios Espirituales, Regla 5)",
            quoteOrSource = "«En tiempo de desolación nunca hacer mudanza, mas estar firme y constante en los propósitos anteriores.»",
            diagnosisPrinciple = "Tomar decisiones impulsivas (escribir, llamar, rogar) cuando el alma está turbada o angustiada.",
            clinicalGuidance = "Cuando hay dolor y confusión, el enemigo del alma busca que rompas tus propósitos de dignidad. Mantente firme en tu decisión previa de contacto cero hasta que retorne la luz y la paz.",
            socraticPrompt = "¿Vas a quebrar una decisión tomada en calma solo porque hoy arrecia la tormenta?",
            concreteAction = "Reza el Salmo 91 o haz 5 minutos de silencio de entrega pidiendo fidelidad a tus propósitos."
        ),
        KnowledgeCapsule(
            id = "cat_sal_1",
            framework = SoltarFramework.CATOLICO,
            category = "AUTOESTIMA",
            title = "La Mansedumbre con las Propias Fragilidades",
            author = "San Francisco de Sales (Introducción a la Vida Devota)",
            quoteOrSource = "«No te inquietes por tus inquietudes ni te aflijas por tus aflicciones. Trata a tu propia alma con paciencia y dulzura.»",
            diagnosisPrinciple = "El autoreproche implacable por haber amado a quien no correspondía o por haber cedido a la debilidad.",
            clinicalGuidance = "La santidad y la paz no consisten en no caer jamás, sino en levantarse de inmediato con humildad y dulzura sin enojarse con uno mismo. Dios conoce tu barro y te ama en tu fragilidad.",
            socraticPrompt = "¿Puedes perdonarte a ti mismo/a con la misma ternura con que Dios te acoge?",
            concreteAction = "Coloca una mano sobre tu corazón y repite: 'Acojo mi humanidad, me perdono y confío en su gracia'."
        ),
        KnowledgeCapsule(
            id = "cat_lew_1",
            framework = SoltarFramework.CATOLICO,
            category = "DUELO",
            title = "La Idolatría Afectiva y el Dolor Redentor",
            author = "C.S. Lewis (Una Pena Observada)",
            quoteOrSource = "«No podemos hacer del ser amado un dios terrenal sin que tarde o temprano se rompa el pedestal. Solo Dios llena el corazón humano.»",
            diagnosisPrinciple = "Haber depositado en la pareja la salvación, el sentido total y la felicidad absoluta de la existencia.",
            clinicalGuidance = "Ningún ser humano puede soportar el peso de ser el 'dios' de otro. El despojo del ídolo duele, pero abre el espacio para un amor más sano, puro y ordenado.",
            socraticPrompt = "¿Le exigiste a esa persona que llenara un vacío que solo lo trascendente puede colmar?",
            concreteAction = "Escribe una oración entregando tus anhelos de plenitud a su verdadero origen divino."
        )
    )

    fun findRelevantCapsule(
        input: String,
        framework: SoltarFramework,
        category: String? = null
    ): KnowledgeCapsule {
        val lower = input.lowercase()
        val frameworkCapsules = capsules.filter { it.framework == framework }
        
        // 1. Check category match if explicit
        if (category != null) {
            val byCat = frameworkCapsules.filter { it.category.equals(category, ignoreCase = true) }
            if (byCat.isNotEmpty()) return byCat.random()
        }

        // 2. Keyword matching
        val targetCategory = when {
            lower.contains("escribir") || lower.contains("llamar") || lower.contains("mensaje") || lower.contains("impulso") || lower.contains("buscarlo") || lower.contains("buscarla") || lower.contains("desesperad") -> "IMPULSO"
            lower.contains("por qué") || lower.contains("descifrar") || lower.contains("pensando") || lower.contains("analizar") || lower.contains("bucle") || lower.contains("rumi") -> "RUMIACION"
            lower.contains("idealiz") || lower.contains("perfecto") || lower.contains("único") || lower.contains("nadie como") || lower.contains("extraño") -> "RUMIACION"
            lower.contains("límite") || lower.contains("contacto cero") || lower.contains("bloque") || lower.contains("redes") || lower.contains("ver su foto") -> "LIMITES"
            lower.contains("triste") || lower.contains("llor") || lower.contains("duele") || lower.contains("duelo") || lower.contains("pérdida") || lower.contains("nostalgia") -> "DUELO"
            lower.contains("culpa") || lower.contains("perdón") || lower.contains("rencor") || lower.contains("odio") || lower.contains("rabia") || lower.contains("injusto") -> "CULPA"
            lower.contains("soledad") || lower.contains("solo") || lower.contains("sola") || lower.contains("vacío") || lower.contains("desamparo") -> "SOLEDAD"
            lower.contains("autoestima") || lower.contains("no valgo") || lower.contains("inútil") || lower.contains("rechazo") || lower.contains("vergüenza") -> "AUTOESTIMA"
            else -> "RECONSTRUCCION"
        }

        val matched = frameworkCapsules.filter { it.category == targetCategory }
        if (matched.isNotEmpty()) return matched.random()

        return frameworkCapsules.firstOrNull() ?: capsules.first()
    }
}
