package com.example.ai

/**
 * Variantes de acompañamiento clínico y terapéutico para el marco de PSICOLOGÍA MODERNA.
 * Cada categoría clínica cuenta con 4 variantes redactadas con rigor clínico, base neurobiológica y calidez empática.
 */
object ClinicalVariantsPsicologia {

    fun getVariants(category: ClinicalCategory): List<ClinicalVariant> {
        return when (category) {
            ClinicalCategory.RECUPERAR_PAREJA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Atalaya no fomenta estrategias de manipulación ni falsas esperanzas.**",
                    bodyText = """
El distanciamiento y el contacto cero no son tácticas de seducción o psicología inversa para forzar a la expareja a volver; son el límite firme para proteger tu propia paz y reconstruir tu dignidad.

Alimentar la fantasía de 'hacer que regrese' prolonga la agonía del duelo y te mantiene en una postura de subordinación afectiva. Lo único fértil hoy es recuperar el gobierno sobre ti mismo y desenganchar el sistema de apego activado.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Desarmemos la trampa de la negociación en el duelo.**",
                    bodyText = """
En las etapas del duelo afectivo, la fase de 'negociación' empuja a la mente a buscar desesperadamente estrategias, pactos o cambios cosméticos para revertir la ruptura. Es un mecanismo de defensa para evitar sentir el dolor crudo de la pérdida.

Intentar reconquistar o dar celos solo mantiene encendido el circuito de recompensa intermitente. La aceptación radical de la realidad, aunque duela al inicio, es el único camino que desactiva el sufrimiento crónico.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El contacto cero es medicina, no una jugada estratégica.**",
                    bodyText = """
Cuando utilizamos el silencio esperando una reacción de la otra persona, seguimos bailando al compás de su atención. Eso no es desapego; es hipervigilancia enmascarada de orgullo.

Para que tu sistema nervioso empiece a desintoxicarse de la abstinencia afectiva, la intención debe ser limpia: te alejas para cuidarte, no para mandar un mensaje indirecto. Tu dignidad no es un peón en un juego de ajedrez relacional.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Rompe el ciclo de sumisión afectiva y falsas expectativas.**",
                    bodyText = """
Buscar tácticas para provocar el arrepentimiento del otro es una forma sutil de cederle todo el control sobre tu estado anímico. Si te busca te alegras, si no te busca te hundes: quedas a merced de su conducta.

La verdadera recuperación psicológica empieza cuando retiras tu energía de la ecuación del otro. Tu valor no depende de que alguien reconsidere su partida, sino de tu capacidad para validar tu propio bienestar.
                    """.trimIndent()
                )
            )

            ClinicalCategory.SENALES_DIGITALES -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Interpretar señales digitales es una trampa dopaminérgica.**",
                    bodyText = """
Una visualización en redes, un estado o un 'me gusta' no constituyen una disculpa, un compromiso ni un proyecto de vida compartido. Son estímulos mínimos que disparan picos de dopamina ilusorios.

No hagas lectura de mente ni intentes descifrar algoritmos. Cada minuto que inviertes inspeccionando sus redes es un minuto que le robas a tu propia reconstrucción. Protege tu atención y sostén el contacto cero digital.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El microchequeo digital sabotea la cicatrización neuronal.**",
                    bodyText = """
Mirar su última conexión o analizar a quién sigue activa exactamente los mismos circuitos neuronales que una recaída en una adicción de sustancias. Obtienes una dosis mínima de información que tu cerebro traduce inmediatamente en rumiación obsesiva.

Tu corteza prefrontal necesita desvincularse del estímulo para recalibrar los niveles de cortisol y noradrenalina. Corta el espionaje digital: lo que no ves no puede torturar tu imaginación.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Distingue entre interacción real y ruido algorítmico.**",
                    bodyText = """
Las redes sociales están diseñadas para fomentar la ambigüedad y la hipervigilancia. Un desliz de dedo en una pantalla no equivale a madurez comunicativa ni a un deseo genuino de reparar nada.

Si esa persona quisiera comunicarse de forma honesta y responsable, usaría un canal directo y claro. No construyas castillos en el aire sobre píxeles vacíos; cuida tu higiene emocional apagando las notificaciones.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Desactiva la lectura de mente en entornos virtuales.**",
                    bodyText = """
La mente humana aborrece la incertidumbre y tiende a rellenar los vacíos de información con sus peores miedos o sus mayores anhelos. Ver una foto o una frase y asumir que va dirigida a ti es una distorsión cognitiva clásica de personalización.

Aplica defusión cognitiva: nota cómo tu mente dice 'eso es por mí' y elige no fusionarte con ese pensamiento. Vuelve al mundo físico y a tus sensaciones presentes.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RUMIACION_BUCLE -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Frenemos el bucle: Ya tienes suficiente información para comprender esto.**",
                    bodyText = """
Seguir intentando descifrar las intenciones, silencios o contradicciones de la otra persona solo mantiene encendido el circuito de la rumiación.

Distingamos los hechos observables de la fantasía:
• **El Hecho:** El vínculo se rompió o la distancia es un hecho real en el presente.
• **La Hipótesis:** Las mil explicaciones que tu mente inventa intentando calmar la incertidumbre.
• **La Soberanía:** Lo único que puedes gobernar hoy son tus decisiones, tu descanso y tu atención.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La rumiación no es análisis productivo, es evitación emocional.**",
                    bodyText = """
Pensar una y otra vez en las mismas preguntas sin respuesta es un intento del cerebro de evitar sentir la tristeza pura de la pérdida. Analizar se siente falsamente activo, como si estuvieras 'resolviendo algo', pero en realidad solo estanca el duelo.

Nombra el proceso: 'Estoy atrapado en un bucle mental'. No intentes responder a la pregunta número cien; en su lugar, haz una pausa, baja al cuerpo y permite que la emoción subyacente se exprese sin palabras.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Aplica defusión cognitiva a los pensamientos intrusivos.**",
                    bodyText = """
Tus pensamientos son eventos mentales, no verdades absolutas ni mandatos biológicos. Cuando aparezca el clásico '¿por qué me dijo aquello si luego hizo esto?', obsérvalo como hojas que bajan por un río.

Pregúntate: '¿Pensar esto por trigésima vez hoy me acerca a la persona autónoma y en paz que quiero ser?'. Si la respuesta es no, agradece a tu mente el intento de protegerte y redirige tu atención a tu tarea actual.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Cierra el expediente mental: la respuesta ya está en la conducta.**",
                    bodyText = """
Buscamos un cierre verbal perfecto que casi nunca llega. Sin embargo, en psicología clínica sabemos que la conducta observada ES el mensaje. La falta de cuidado, el silencio o la distancia son toda la explicación que necesitas.

No requieres una confesión detallada para validar tu decisión de soltar. Deja de interrogar al pasado y empieza a escuchar las necesidades de tu presente.
                    """.trimIndent()
                )
            )

            ClinicalCategory.IMPULSO_CONTACTAR -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El impulso es solo una ola neuroquímica; no es una orden que debas obedecer.**",
                    bodyText = """
Lo que sientes en el pecho no es una señal mística de que debas romper la distancia. Es la respuesta biológica de alarma y abstinencia de tu sistema nervioso ante la pérdida de la figura vincular.

Antes de mover las manos, analicemos con rigor:
1. **¿Qué buscas realmente?** Un alivio fugaz de 10 minutos a cambio de reiniciar semanas de cicatrización emocional.
2. **¿Qué no depende de ti?** Cómo responderá o qué sentirá la otra persona.
3. **¿Qué sí depende de ti?** Tu autorregulación, tu palabra y tu templanza en este momento exacto.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Aplica la técnica de 'surfear la urgencia' (Urge Surfing).**",
                    bodyText = """
El craving relacional funciona exactamente como una ola en el mar: nace, crece gradualmente, alcanza un pico de máxima intensidad y luego decae de forma natural si no le das combustible.

No luches contra la sensación física: localízala en tu cuerpo (garganta, estómago, pecho). Obsérvala con curiosidad clínica y cronometra 15 minutos sin hacer nada con el móvil. Verás cómo el pico neuroquímico se desinfla por sí mismo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Tu sistema de apego está en estado de alarma de desamparo.**",
                    bodyText = """
Desde el punto de vista del apego, el impulso de llamar es el reflejo primitivo del mamífero buscando al cuidador para asegurar la supervivencia. Tu cerebro límbico interpreta la separación como una amenaza vital.

Pero hoy eres una persona adulta, no un niño indefenso. Puedes contenerte a ti mismo. Respira profundo, pon tu mano derecha sobre tu pecho y date la contención que pretendes mendigar afuera.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Pospón la acción impulsiva para proteger tu autoestima.**",
                    bodyText = """
El problema del mensaje impulsivo no es solo el rechazo que probablemente recibirás; es el impacto devastador en tu autoeficacia al sentir que no puedes confiar en tus propios límites.

Haz un pacto de demora: 'Si dentro de 24 horas sigo considerando que este mensaje es saludable y digno, me lo replantearé'. El 99% de las veces, cuando la amígdala se calma, el impulso desaparece.
                    """.trimIndent()
                )
            )

            ClinicalCategory.DEPENDENCIA_EMOCIONAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Diferenciemos el afecto legítimo de la dependencia emocional.**",
                    bodyText = """
Sentir que 'no puedes vivir sin esa persona' es la forma en que tu cerebro traduce el miedo al desamparo y la pérdida de la fuente habitual de regulación externa.

Revisemos esta distinción fundamental:
• **Amor maduro:** Desear compartir la vida desde la propia plenitud y respeto mutuo.
• **Dependencia:** Usar la presencia del otro como único ansiolítico para no sentir la soledad.

Tu valor como ser humano no está hipotecado a la aprobación de nadie.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Reconstruye tu centro de gravedad psicológico.**",
                    bodyText = """
En los vínculos dependientes, el centro de gravedad interno se desplaza por completo hacia la otra persona: sus estados de ánimo definen tu felicidad y sus silencios provocan tu derrumbe.

La ruptura, con todo su dolor, es la oportunidad de repatriar tu centro de gravedad. Aprender a autorregularse emocionalmente sin depender del reflejo ajeno es la habilidad psicológica más liberadora que existe.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La herida del abandono no se sana mendigando presencia.**",
                    bodyText = """
Cuando decimos 'sin ti no soy nada', estamos proyectando una herida infantil no resuelta en una relación de pareja adulta. Ninguna persona tiene la obligación ni la capacidad de cargar con tu vacío existencial.

Asume la responsabilidad compasiva de tu propia vida. Trátate con la ternura y el cuidado que esperabas recibir del otro; tú eres la única persona que estará contigo hasta el último día.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Desarticula la ilusión de que el otro es indispensable para respirar.**",
                    bodyText = """
Antes de conocer a esa persona ya tenías una vida, gustos, capacidades y resiliencia. El apego ansioso borra esa memoria y te hace creer que tu supervivencia depende de su afecto.

Comprueba la realidad física en este momento: tus pulmones se expanden, tu corazón late y sigues en pie sin su presencia. El dolor es real, pero la incapacidad de vivir es solo una distorsión cognitiva.
                    """.trimIndent()
                )
            )

            ClinicalCategory.NOSTALGIA_IDEALIZACION -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La nostalgia tiende a embellecer el pasado y borrar las heridas reales.**",
                    bodyText = """
Es totalmente legítimo y humano extrañar momentos cálidos o la sensación de refugio. Sin embargo, no permitas que la memoria selectiva y eufórica te engañe:
• **Extrañar no significa que la relación fuera viable ni sana.**
• **El dolor que sientes es el trabajo psíquico de despedir una etapa, no una invitación a volver.**
• **El duelo oscila:** Habrá días de calma y días de oleaje; esto no es un retroceso, es cicatrización.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Cuidado con el sesgo de recuerdo eufórico (Euphoric Recall).**",
                    bodyText = """
El cerebro en duelo tiende evolutivamente a rescatar las memorias gratificantes y a minimizar sistemáticamente los momentos de angustia, las discusiones y la soledad compartida. Es un truco neuroquímico para empujarte a reconectar.

Para tener una perspectiva equilibrada, no te quedes solo con la foto bonita: haz un esfuerzo consciente por recordar también las lágrimas, las faltas de respeto y los motivos reales que llevaron al desenlace.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Extrañas el anhelo de lo que pudo ser, no lo que realmente era.**",
                    bodyText = """
Muchas veces no extrañamos a la persona real con sus defectos e incompatibilidades, sino a la versión potencial que construimos en nuestra mente o a cómo nos hacía sentir en los días buenos.

Separa la persona real del personaje idealizado. Aceptar que esa persona no podía darte el amor seguro y maduro que mereces es el paso más doloroso pero más liberador del proceso.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Valida la melancolía sin permitir que dicte tu conducta.**",
                    bodyText = """
Tener un día triste o sentir añoranza no significa que el contacto cero esté fallando o que hayas vuelto a la casilla de salida. La curva de sanación no es una línea recta ascendente; es una espiral con altibajos.

Abraza la tristeza como quien recibe a un visitante temporal. Déjala estar un rato en el pecho sin juzgarte, y recuerda que sentir nostalgia no es una orden para buscar contacto.
                    """.trimIndent()
                )
            )

            ClinicalCategory.CULPA_RENCOR_RABIA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Sostener el rencor o la culpa es seguir atado/a a lo que ya pasó.**",
                    bodyText = """
La culpa te atrapa en la fantasía retrospectiva de que podías haberlo previsto todo. El rencor te hace rehén de la persona que te hirió, cediéndole un alquiler gratuito en tu cabeza.

Ni el autorreproche ni la amargura tienen el poder de reescribir la historia. El verdadero cierre no viene de que te pidan perdón; viene de decidir que tu presente no le pertenece al daño del ayer.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Desmonta el sesgo de retrospectiva: hiciste lo que supiste.**",
                    bodyText = """
Es muy fácil juzgar tus decisiones del pasado con la información, la lucidez y la perspectiva que tienes hoy. En aquel momento actuaste bajo estrés, confusión o carencias que condicionaron tus respuestas.

Sustituye la culpa estéril por responsabilidad compasiva. Aprende la lección que esa vivencia te ofrece, perdónate por no haber sabido hacerlo mejor y utiliza esa sabiduría para poner límites en el futuro.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La rabia es la energía para poner límites, no para rumiar venganza.**",
                    bodyText = """
Sentir rabia ante una traición o un trato injusto es una respuesta psicológica sana: indica que tu sistema de dignidad reconoce un atropello. El problema surge cuando esa rabia se estanca en resentimiento crónico.

Usa la fuerza de la indignación para mantener el contacto cero con absoluta firmeza. No gastes esa energía en desearle el mal a la otra persona; inviértela en blindar tu vida y reconstruir tus proyectos.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El perdón terapéutico es un acto de higiene personal, no de justificación.**",
                    bodyText = """
Perdonar no significa decir que lo que te hicieron estuvo bien, ni implica reconciliarte con quien te lastimó. En psicoterapia, perdonar significa desarmar el resentimiento para que el veneno de la ofensa deje de intoxicar tu organismo.

Te mereces soltar esa carga no porque el otro sea inocente, sino porque tu sistema nervioso necesita descansar de la hostilidad. Corta el lazo del rencor y recupera tu ligereza.
                    """.trimIndent()
                )
            )

            ClinicalCategory.CONTACTO_CERO_LIMITES -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El límite firme es el espacio aséptico donde tu herida puede sanar.**",
                    bodyText = """
Revisar perfiles, estados o buscar intermediarios es mantener una microdosis de toxicidad y alerta en tu sistema nervioso que impide la regeneración emocional.

El contacto cero y la distancia absoluta no son un castigo para el otro: son la muralla protectora que le pones a tu salud mental y a tu dignidad. Sostén el perímetro con orgullo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Los límites difusos generan sufrimiento prolongado.**",
                    bodyText = """
Intentar ser 'amigos' inmediatamente después de una ruptura cuando aún hay apego, dolor o resentimiento es una trampa de autoengaño. Solo prolonga la ambigüedad y dificulta el desapego neurobiológico.

Para poder transformar un vínculo, primero hay que cerrarlo por completo. Date el permiso de ausentarte, de silenciar y de bloquear si es necesario. Cuidar de ti no es egoísmo; es responsabilidad básica.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La asertividad no requiere explicaciones interminables.**",
                    bodyText = """
No necesitas justificar ante nadie tu necesidad de silencio. Cuando ponemos límites desde la inseguridad, tendemos a sobreexplicarnos buscando la validación de quien ya no nos escucha.

Un límite saludable es claro, breve y coherente. No hace falta pelear ni redactar cartas de despedida infinitas: basta con retirarse con paso firme y sostener la decisión en el tiempo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El contacto cero protege tu estabilidad cognitiva.**",
                    bodyText = """
Cada interacción, por breve que sea, reactiva el ciclo de anticipación y desilusión. Tu cerebro necesita al menos 60 a 90 días de desintoxicación absoluta para que los receptores dopaminérgicos recuperen su línea base.

Cada día sin contacto es una inversión directa en tu lucidez y en tu paz mental. No abras grietas en el muro: mantén tu espacio seguro y no negociable.
                    """.trimIndent()
                )
            )

            ClinicalCategory.AUTOESTIMA_RECHAZO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El desinterés o la incapacidad de alguien de amarte no define tu valor.**",
                    bodyText = """
Tu valor intrínseco no fluctúa según el trato que recibes de una persona desregulada o incompatible. Eres un ser humano completo, con dignidad y capacidad intacta de florecer.

La herida del rechazo confunde 'no haber sido elegido/a por alguien' con 'no ser valioso/a'. Desvincula tu autoconcepto de la mirada ajena; su partida habla de sus propios límites, no de tu suficiencia.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Sana la creencia nuclear de no ser suficiente.**",
                    bodyText = """
Cuando una relación termina, suele activarse el esquema cognitivo de insuficiencia ('si hubiera sido más inteligente, más atractivo/a o más complaciente, se habría quedado'). Esto es una falacia de control.

El amor maduro no es un examen que apruebas complaciendo al otro. Si tuviste que anularte o hacer malabares para que se quedara, el costo era demasiado alto. Tu autenticidad vale más que cualquier aprobación externa.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Practica la autocompasión frente a la autocrítica feroz.**",
                    bodyText = """
En momentos de descarte o abandono, la voz del crítico interno suele ser implacable. Kristin Neff nos recuerda los tres componentes de la autocompasión: amabilidad contigo mismo, humanidad compartida (millones de personas atraviesan esto hoy) y mindfulness.

Háblate a ti mismo como le hablarías a tu mejor amigo si estuviera atravesando este dolor. Deja de agredirte por haber amado o por haber confiado; la vulnerabilidad es valentía.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Reclama tu dignidad por encima de la validación externa.**",
                    bodyText = """
Haber sido rechazado por alguien solo significa que esa persona particular, con su historia, heridas y limitaciones, no pudo o no quiso vincularse contigo de forma duradera. Nada más.

No generalices un desenlace amoroso a la totalidad de tu valor existencial. Tienes talentos, afectos, valores y un futuro entero por delante que no dependen del visto bueno de nadie.
                    """.trimIndent()
                )
            )

            ClinicalCategory.SOLEDAD_VACIO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La soledad no es un abismo de castigo, sino el espacio para reencontrarte.**",
                    bodyText = """
El silencio que queda tras una ruptura asusta porque revela cuánto te habías abandonado para complacer al otro o cuánto usabas la pareja para anestesiar tu propio vacío existencial.

Estar contigo mismo no es estar desamparado; es recuperar el espacio sagrado donde tú vuelves a ser el protagonista de tu propia existencia. Aprende a convertir la soledad temida en solitud nutricia.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El dolor del vacío es la deshabituación de la compañía.**",
                    bodyText = """
Cuando compartes rutinas con alguien durante meses o años, tu cerebro crea mapas de hábitos conjuntos. Al desaparecer la otra persona, esos circuitos se disparan en el vacío, generando una sensación de desorientación espacial y temporal.

No te asustes por la sensación de 'casa vacía' o por las horas muertas. Diseña nuevas rutinas que sean exclusivamente tuyas: camina, lee, cocina para ti. Estás reconfigurando tu mapa cerebral de autonomía.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La solitud es el laboratorio de la madurez emocional.**",
                    bodyText = """
Quien no tolera estar a solas consigo mismo termina aceptando migajas afectivas y relaciones destructivas con tal de evitar el silencio. Aprender a habitar tu propia compañía es tu mayor seguro contra la dependencia futura.

Mírate en el espejo con ternura. Hazte una taza de infusión, escucha música que te inspire y reconoce que no estás vacío: estás lleno de vivencias, aprendizajes y posibilidades.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Conecta con la red de afectos disponibles a tu alrededor.**",
                    bodyText = """
A veces, en medio de la ruptura, sufrimos de 'visión de túnel' y sentimos que estamos absolutamente solos en el mundo. Sin embargo, la expareja no era la única fuente de afecto existente.

Abre tu mirada hacia amistades, familia, proyectos comunitarios o aficiones compartidas. La soledad se disuelve cuando dejas de mirar la puerta que se cerró y valoras los vínculos sinceros que siguen a tu lado.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ANSIEDAD_SOMATICA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Regula tu sistema nervioso autónomo antes de pensar o actuar.**",
                    bodyText = """
La taquicardia, la respiración superficial y el nudo en el estómago son señales de que tu sistema nervioso simpático ha activado el modo de lucha o huida. En este estado, el lóbulo frontal pierde capacidad de juicio racional.

Aplica la técnica del 'suspiro fisiológico': realiza dos inhalaciones seguidas por la nariz (una profunda y otra corta para llenar los alvéolos) y luego suelta todo el aire por la boca de forma muy lenta. Repite 3 a 5 veces para inducir calma vagal inmediata.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Anclaje somático 5-4-3-2-1 para salir de la desregulación.**",
                    bodyText = """
Cuando sientas que la angustia te sobrepasa, trae tu mente de vuelta al presente mediante los sentidos:
• Nombra 5 cosas que puedas ver a tu alrededor.
• Toca 4 texturas diferentes con tus manos.
• Identifica 3 sonidos que percibas en este momento.
• Reconoce 2 olores presentes en el ambiente.
• Nota 1 sabor en tu boca.

Este ejercicio interrumpe el bucle de pánico amigdalino y te devuelve la sensación de seguridad en el cuerpo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El cuerpo no está en peligro real; solo está procesando una pérdida.**",
                    bodyText = """
La ansiedad aguda se siente físicamente aterradora, pero no es letal ni te volverá loco/a. Tu cerebro ha interpretado la ruptura como una emergencia biológica, pero la realidad objetiva es que estás a salvo en tu habitación.

Coloca una mano sobre el pecho y otra sobre el abdomen. Siente cómo se elevan con la respiración diafragmática y repite internamente: 'Estoy a salvo, esto es solo una ola de adrenalina y pasará en unos minutos'.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Descarga la energía somática de la ansiedad en movimiento.**",
                    bodyText = """
Cuando el cuerpo bombea adrenalina y cortisol, retener esa energía sentado rumiando empeora la sensación de agobio. La biología te pide movimiento para metabolizar el estrés.

Sal a caminar a paso ligero durante 20 minutos, sacude los brazos y las piernas o date una ducha con agua tibia en la nuca. Ayuda a tu organismo a completar el ciclo del estrés para que regrese la calma.
                    """.trimIndent()
                )
            )

            ClinicalCategory.INSOMNIO_NOCHE -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La noche disminuye la inhibición prefrontal; no tomes decisiones en la cama.**",
                    bodyText = """
Durante la madrugada, los niveles de melatonina y cortisol fluctúan, mientras la corteza prefrontal reduce su actividad reguladora. Por eso los pensamientos negativos se sienten desproporcionadamente catastróficos por la noche.

Si llevas más de 20 minutos despierto/a rumiando, sal de la cama: no asocies el colchón con la angustia. Ve a un rincón tranquilo con luz tenue, lee algo no estimulante y vuelve solo cuando sientas somnolencia real.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Los sueños y despertares son parte del procesamiento del trauma relacional.**",
                    bodyText = """
Soñar con la expareja o despertar sobresaltado/a en mitad de la noche no significa que debas volver a hablarle ni que sea una premonición. Es la fase REM del sueño intentando archivar y desensibilizar las memorias emocionales.

Al despertar, no mires el teléfono ni revises la hora obsesivamente. Recuerda: 'Mi cerebro está haciendo limpieza nocturna; esto es normal en el duelo'. Respira con calma y mantén los ojos cerrados.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Aplica higiene del sueño estricta en periodos de ruptura.**",
                    bodyText = """
El duelo afecta gravemente la arquitectura del descanso. Para proteger tu salud mental, establece límites claros antes de dormir: deja el móvil fuera del dormitorio al menos una hora antes de acostarte.

Mirar pantallas con luz azul y contenido potencialmente detonante antes de dormir es una receta garantizada para el insomnio y la pesadilla. Regálate una transición nocturna segura y pacífica.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Acepta el desvelo sin angustiarte por no dormir.**",
                    bodyText = """
El mayor enemigo del sueño es la exigencia ansiosa de tener que dormirte ya. Paradójicamente, cuanta más presión te pones, más alerta se vuelve el sistema simpático.

Dile a tu mente: 'Si esta noche duermo menos, mi cuerpo descansará igualmente permaneciendo tumbado y relajado'. Quitarle dramatismo a la vigilia es el camino más directo para que el sueño regrese suavemente.
                    """.trimIndent()
                )
            )

            ClinicalCategory.FECHAS_SIGNIFICATIVAS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Planifica un protocolo de contención para las fechas sensibles.**",
                    bodyText = """
Los aniversarios, cumpleaños o festividades compartidas actúan como disparadores condicionados de memoria afectiva. Si dejas la jornada a la improvisación, el riesgo de recaída o colapso anímico se multiplica.

Diseña un plan de choque con antelación: no pases el día a solas en casa rumiando. Coordina actividades con personas de confianza, agenda tareas estructuradas y ten a mano tu lista de motivos para sostener el límite.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Desarma el significado rígido del calendario.**",
                    bodyText = """
Una fecha solo tiene el poder simbólico que nosotros decidimos proyectar sobre ella. El sol sale y se pone exactamente igual que cualquier otro día del año.

Reconoce la punzada de nostalgia: 'Hoy hace un año estábamos juntos, pero hoy elijo mi bienestar'. Valida el recuerdo de lo que fue sin convertirlo en una obligación de sufrir en el presente.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No uses la efeméride como coartada para quebrar el contacto cero.**",
                    bodyText = """
El autoengaño clásico en estas fechas es el 'solo le mandaré un mensaje de felicitación por educación'. En el 95% de los casos, detrás de esa supuesta cortesía se esconde la esperanza desesperada de reabrir el canal.

Si rompes el contacto en un día señalado, reiniciarás el reloj de la abstinencia y te expondrás a una respuesta fría o al silencio que multiplicará tu dolor. El mejor regalo que puedes darte hoy es tu propia protección.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Genera nuevos rituales que sustituyan las memorias antiguas.**",
                    bodyText = """
Para desensibilizar una fecha dolorosa, la psicología conductual recomienda asociarla progresivamente a experiencias nuevas y gratificantes. Si antes ese día era 'vuestro aniversario', transfórmalo en el día de tu autocuidado.

Cómprate un libro que deseabas leer, prepara tu comida favorita o haz una excursión a la naturaleza. Estás reescribiendo la narrativa de tu propia historia.
                    """.trimIndent()
                )
            )

            ClinicalCategory.NUEVA_PAREJA_EX -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La nueva relación ajena no invalida tu historia ni mide tu valía.**",
                    bodyText = """
Enterarse de que la expareja sale con otra persona dispara el dolor primario del descarte y la comparación destructiva. Tu mente empieza a imaginar que con la nueva pareja todo será perfecto y que el problema eras tú.

Esto es un sesgo cognitivo de idealización ajena. Las personas no cambian mágicamente sus patrones de apego, carencias o dificultades comunicativas solo por cambiar de acompañante. Céntrate en tu camino, no en la vitrina ajena.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Desactiva el síndrome del reemplazo inmediato.**",
                    bodyText = """
Muchas relaciones que se inician inmediatamente tras una ruptura son vínculos de rebote o parches analgésicos para no atravesar la incomodidad del duelo en soledad. Saltar de un vínculo a otro sin introspección suele ser síntoma de inmadurez emocional.

Tú estás eligiendo el camino valiente: sentir el dolor, sanar las heridas y reconstruirte de raíz. Ese proceso requiere tiempo, pero garantiza que tus futuras relaciones serán saludables y conscientes.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Higiene absoluta: prohíbete investigar a la nueva persona.**",
                    bodyText = """
Revisar el perfil de la nueva pareja de tu ex es una conducta de autolesión psicológica encubierta. Comparar tu físico, tu trabajo o tu vida con una versión filtrada de Instagram solo profundizará tu herida.

Aplica un bloqueo estricto y pide a tus conocidos que no te informen de nada referente a esa relación. La ignorancia en este ámbito es salud mental pura y dura.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El cierre no depende de lo que el otro haga con su vida.**",
                    bodyText = """
Tu paz no puede estar condicionada a que la otra persona permanezca sola o triste para validar que te quiso. Cada persona gestiona sus vacíos como puede o como sabe.

El verdadero cierre es interno: reconoces que vuestra etapa juntos terminó y que su presente ya no tiene ninguna jurisdicción sobre tu futuro. Vuelve tu mirada hacia tus propios sueños.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ENCUENTRO_CASUAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Protocolo de contingencia para el encuentro fortuito en persona.**",
                    bodyText = """
Ver a la expareja cara a cara de forma imprevista dispara un shock adrenérgico automático en el cuerpo. El corazón se acelera y las palabras pueden trabarse: es una respuesta normal de tu organismo.

Aplica la regla de la cortesía mínima: un saludo breve con la cabeza o un 'hola' neutral sin detener el paso. No te quedes a mantener una conversación incómoda ni te apresures a huir corriendo. Camina con naturalidad.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Autorregulación post-encuentro: permite que baje la adrenalina.**",
                    bodyText = """
Lo más difícil suele venir minutos después del cruce, cuando la mente empieza a sobreanalizar cada detalle: cómo iba vestido/a, si te miró, si sonrió o si parecía indiferente.

No te juzgues por temblar o por sentir ganas de llorar tras el encuentro. Busca un lugar seguro (un banco, una cafetería o tu casa), bebe agua y haz respiraciones lentas. En 20 minutos tu química cerebral volverá a equilibrarse.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No cedas tu territorio cotidiano por miedo a coincidir.**",
                    bodyText = """
Evitar sistemáticamente tu barrio, tu supermercado habitual o tus cafeterías favoritas por terror a encontrártelo/a solo refuerza la agorafobia emocional y empobrece tu vida.

El espacio público es de todos. Ve por tus lugares con la tranquilidad de quien no ha hecho nada malo. Si coincides, tu mejor carta es la educación sobria y la indiferencia afectiva.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El encuentro duró dos minutos; no lo conviertas en una semana de rumiación.**",
                    bodyText = """
Tu cerebro querrá desmenuzar el cruce como si fuera una película de suspenso. Pon un límite consciente a ese análisis: 'Ya pasó, fue una coincidencia aleatoria y no cambia nada de mi plan de autocuidado'.

Sigue con las tareas que tenías programadas para el día. La mejor forma de restarle poder al suceso es continuar viviendo con normalidad.
                    """.trimIndent()
                )
            )

            ClinicalCategory.COPARENTALIDAD_LOGISTICA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Contacto cero adaptativo: método BIFF para la comunicación necesaria.**",
                    bodyText = """
Cuando hay hijos, bienes compartidos o trámites legales, la desconexión total no es viable. En estos casos se aplica el método BIFF: comunicación Breve, Informativa, Firme y Favorable/Educada.

Trata todos los intercambios como comunicaciones profesionales de trabajo. Si recibes mensajes con reproches emocionales, responde únicamente a la parte logística objetiva y omite por completo cualquier provocación.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Establece canales y horarios estrictos para la logística.**",
                    bodyText = """
No permitas que las cuestiones prácticas se conviertan en una línea abierta de WhatsApp a cualquier hora del día. Define un canal exclusivo (por ejemplo, correo electrónico o una app de coparentalidad) y horarios específicos para revisar mensajes.

Blindar los canales de comunicación protege tu sistema nervioso de la hipervigilancia constante de esperar un mensaje detonante en mitad de tu jornada.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Prioriza el bienestar emocional de los hijos por encima del conflicto.**",
                    bodyText = """
Los hijos no deben ser testigos de triangulaciones, reproches velados ni mensajeros de información entre adultos. Su seguridad emocional depende de que perciban que los padres pueden coordinar lo básico con madurez.

Sé el modelo de autorregulación que tus hijos necesitan. Aunque la otra parte intente desestabilizarte, tu serenidad es el escudo protector para su desarrollo psicológico.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Delega en profesionales cuando la comunicación directa sea tóxica.**",
                    bodyText = """
Si cada intento de coordinar un aspecto práctico se convierte en una batalla campal de manipulaciones y faltas de respeto, es momento de derivar la comunicación a intermediarios: abogados, mediadores familiares o puntos de encuentro neutrales.

Poner un mediador no es un fracaso; es una medida de higiene clínica fundamental para preservar tu salud mental y evitar el desgaste crónico.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ETIQUETAS_DIAGNOSTICAS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Atalaya examina los hechos observables, no las etiquetas clínicas.**",
                    bodyText = """
Etiquetar a la expareja como 'narcisista patológico', 'tóxica' o 'psicópata' suele ser una trampa cognitiva para sentir alivio momentáneo o para justificar la rumiación continua bajo la apariencia de psicoeducación.

En clínica rigurosa no diagnosticamos a terceros ausentes. Lo relevante para tu bienestar no es el diagnóstico que pueda tener en el DSM-5, sino los hechos objetivos: si hubo falta de respeto, manipulación o daño continuado, esos hechos son suficientes para justificar tu distancia definitiva.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Céntrate en la conducta observable y en tu respuesta asertiva.**",
                    bodyText = """
Pasar horas consumiendo vídeos y artículos sobre perfiles de personalidad tóxica mantiene a esa persona en el centro de tu atención mental. Solo has cambiado el amor por la obsesión investigadora.

Pregúntate: '¿Cómo me sentía yo en esa relación? ¿Se respetaban mis límites y mis necesidades?'. La respuesta a esas dos preguntas te da toda la claridad que necesitas, sin requerir diagnósticos ajenos.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La responsabilidad afectiva comienza por tus propios límites.**",
                    bodyText = """
Poner etiquetas al otro puede colocarnos en una posición de víctima pasiva ante un 'monstruo'. Aunque hayas sufrido conductas inaceptables, tu poder de recuperación reside en asumir tu agencia: qué permitiste, qué señales ignoraste y qué límites pondrás a partir de hoy.

Al recuperar tu parte de responsabilidad sobre tus decisiones, recuperas el control de tu vida. Dejas de temer a terceros y aprendes a confiar en tu propio radar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Sustituye la etiqueta patologizante por un límite claro.**",
                    bodyText = """
No necesitas demostrarle al mundo ni a ti mismo que tu ex tenía un trastorno mental para tener derecho a alejarte. La incompatibilidad profunda o el maltrato emocional son motivos legítimos y suficientes por sí mismos.

Cierra el libro de diagnósticos. Tu trabajo terapéutico ahora es sanar tus heridas de apego y construir relaciones donde el respeto mutuo sea la base indispensable.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RECONSTRUIR_GENERAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Acompaño tu proceso con rigor, presencia y claridad.**",
                    bodyText = """
En el camino de soltar y reconstruirte hay una verdad innegociable respaldada por la evidencia clínica:
*«Puedes seguir queriendo a alguien y, al mismo tiempo, dejar de organizar tu vida alrededor de esa persona.»*

Hoy estás dando un paso más hacia tu soberanía afectiva. Cada vez que eliges tu tranquilidad por encima de la desesperación, forjas una versión de ti más libre, consciente y madura.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La recuperación es un proceso de neuroplasticidad y aprendizaje.**",
                    bodyText = """
Cada vez que sostienes el dolor sin recurrir a la conducta impulsiva, tu cerebro genera nuevas rutas neuronales de autorregulación. Estás reentrenando a tu sistema nervioso para no depender de la presencia ajena.

Ten paciencia con tus ritmos. La cicatrización psicológica lleva tiempo, pero cada pequeño acto de autocuidado diario va consolidando una base sólida que nadie podrá arrebatarte.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Reconecta con tus valores esenciales de vida.**",
                    bodyText = """
La terapia de aceptación y compromiso (ACT) nos enseña que el dolor es inevitable, pero el sufrimiento surge de abandonar lo que de verdad nos importa para luchar contra las emociones incómodas.

¿Qué valores quieres que guíen tu vida hoy? ¿La autenticidad, la creatividad, la salud, la lealtad hacia ti mismo? Elige una pequeña acción hoy que honre esos valores y ponla en marcha.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Honra tu valentía al sostener este proceso de duelo.**",
                    bodyText = """
Muchos eligen anestesiar el dolor con relaciones parche, adicciones o huidas hacia adelante. Tú estás aquí, asumiendo la incomodidad de frente y trabajando en tu propia persona.

Esa es la verdadera fortaleza psicológica. Camina con la cabeza alta: estás transitando el desierto necesario que precede a una madurez afectiva mucho más profunda y plena.
                    """.trimIndent()
                )
            )

            else -> ClinicalNewCategoriesPsicologia.getVariants(category)
        }
    }
}
