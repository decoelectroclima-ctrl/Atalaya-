package com.example.ai

/**
 * Variantes clínicas y terapéuticas para las 15 nuevas categorías de Recuerda (Marco PSICOLOGÍA MODERNA).
 * Cada categoría contiene 4 intervenciones de máximo rigor clínico, base neurobiológica y calidez empática.
 */
object ClinicalNewCategoriesPsicologia {

    fun getVariants(category: ClinicalCategory): List<ClinicalVariant> {
        return when (category) {
            ClinicalCategory.NUEVA_PAREJA_EX -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El avance ajeno activa la comparación, no mide tu valía.**",
                    bodyText = """
Enterarte de que tu expareja tiene una nueva relación activa de forma inmediata el sistema de amenaza y comparación social en el cerebro límbico, detonando una profunda herida de rechazo y la ilusión cognitiva de haber sido 'reemplazado/a'.

Desde la psicología del apego sabemos que la rapidez con que alguien inicia un nuevo vínculo suele reflejar una incapacidad para tolerar la soledad, el duelo o la autorregulación emocional (relaciones rebote compensatorias). Ese movimiento habla de su propia biografía y mecanismos de evitación; jamás constituye un veredicto objetivo sobre lo que tú aportaste o mereces.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Separemos el dolor del duelo de la herida narcisista.**",
                    bodyText = """
Es fundamental discriminar dos dolores que suelen mezclarse: el dolor genuino por la pérdida del proyecto vincular, y el dolor punzante de la comparación ('¿por qué con otra persona sí y conmigo no?').

Esa pregunta es una trampa de la mente. Las dinámicas relacionales no se transforman mágicamente por cambiar de pareja; los patrones vinculares no resueltos se repiten invariablemente bajo una fachada inicial de euforia dopaminérgica. No te tortures idealizando una realidad ajena que no conoces.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Tu proceso de cicatrización no compite en una carrera.**",
                    bodyText = """
Ver que el otro 'ya rehizo su vida' puede generar una sensación angustiosa de desventaja o estancamiento. Sin embargo, procesar un duelo a fondo, sintiendo las emociones y reconstruyendo la propia autonomía lleva tiempo orgánico.

Tú estás eligiendo sanar desde la raíz, sin anestesiarte con parches afectivos precipitados. Honra tu ritmo: la madurez emocional se construye habitando el propio proceso, no fingiendo una superación cosmética.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Higiene visual y defusión ante la vida ajena.**",
                    bodyText = """
Continuar investigando, preguntando a terceros o mirando imágenes de esa nueva relación es una forma de autolesión psicológica secundaria que reactiva el circuito de dolor social en la corteza cingulada anterior.

Aplica un blindaje radical a tu campo perceptual. Lo que esa persona haga, con quién salga o lo que muestre en redes sociales ya no forma parte de tu mapa de supervivencia. Reorienta tu atención hacia tu propio cuerpo y tus metas vitales.
                    """.trimIndent()
                )
            )

            ClinicalCategory.MIEDO_FUTURO_SOLEDAD -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Identifiquemos la distorsión del pensamiento catastrofista.**",
                    bodyText = """
Pensar 'nunca voy a encontrar a nadie' o 'me voy a quedar solo/a para siempre' es una distorsión cognitiva clásica (catastrofismo y sobregeneralización). Cuando el sistema nervioso está desregulado por el dolor del apego, la amígdala secuestra la corteza prefrontal y proyecta el malestar presente hacia la totalidad del futuro.

Reconoce este pensamiento como un síntoma de agotamiento emocional, no como un dato fáctico sobre tu porvenir. Tu mente está cansada y asustada, y en ese estado fabrica certezas trágicas que no tienen respaldo empírico.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El sesgo de exclusividad: nadie es insustituible en el apego.**",
                    bodyText = """
La creencia de que 'no habrá nadie como él o ella' surge de la neurobiología del apego adulto, que imprime en la memoria emocional la sensación de que esa fuente de afecto específica es la única capaz de brindar seguridad y pertenencia.

La realidad vincular demuestra que la capacidad de conectar, intimar y amar reside en tu propio sistema relacional, no era una propiedad exclusiva de tu expareja. Cuando tu sistema nervioso recupere la calma basal, tu capacidad de resonancia afectiva volverá a florecer.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Transformar la soledad temida en soledad nutricia (Solitud).**",
                    bodyText = """
El pánico a la soledad suele esconder una desconexión histórica con uno mismo o heridas tempranas de abandono. Si consideramos que la soltería es un vacío inhabitable, cualquier relación futura se convertirá en un salvavidas desesperado y no en una elección libre.

Aprender a sostenerte y convertirte en una base segura para ti mismo es la mayor vacuna contra la dependencia afectiva. El objetivo de este tiempo no es resignarse al aislamiento, sino conquistar una relación sólida y compasiva contigo mismo/a.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Técnica de defusión cognitiva ante las profecías del miedo.**",
                    bodyText = """
En lugar de fusionarte con la frase 'me voy a quedar solo/a', prueba a reformularla internamente: *«Estoy teniendo el pensamiento de que me quedaré solo/a, y noto cómo mi cuerpo reacciona con miedo en el pecho.»*

Ese pequeño espacio entre tú y el contenido mental desactiva la reactividad biológica. Las emociones y los pensamientos catastróficos son olas pasajeras en tu consciencia; no son tu destino ni tu verdad final.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RECAIDA_OCURRIDA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Acompañamos lo sucedido sin juicio: la recaída es un dato, no un fracaso.**",
                    bodyText = """
Haberle escrito, llamado o respondido a un contacto no te devuelve al punto de partida ni anula el progreso que lograste. En el modelo de prevención de recaídas (Marlatt), un desliz es un fenómeno esperable dentro de la deshabituación del craving relacional, no un colapso moral.

Respira hondo y suelta la culpa punitiva. El cerebro en abstinencia busca dopamina y alivio cuando las defensas bajan por fatiga, tristeza o soledad. Registra el hecho con curiosidad clínica: ¿qué necesidad insatisfecha o disparador ambiental detonó la acción?
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Evitemos el Efecto de Violación de la Abstinencia (EVA).**",
                    bodyText = """
El mayor peligro tras una recaída no es el mensaje que enviaste, sino la trampa cognitiva del 'ya que caí, todo está perdido' (Efecto de Violación de la Abstinencia), que suele conducir a continuar buscando al otro en cadena.

Un solo bache en el camino no significa que debas tirar el coche por el barranco. Corta la hemorragia de inmediato: no mandes un segundo texto disculpándote ni esperando respuesta ansiosamente. Cierra la aplicación y restablece el límite ahora mismo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La neuroplasticidad no se borra con un desliz puntual.**",
                    bodyText = """
Los días, semanas o meses que pasaste sosteniendo el contacto cero crearon nuevas rutas neuronales de regulación y autonomía en tu corteza cerebral. Esas vías sinápticas no desaparecen mágicamente por haber hecho una llamada.

Tu sistema de apego tuvo un momento de pánico y buscó la figura conocida. Valida la necesidad de apego sin validar la conducta, aprende del disparador y retoma tu protocolo de autocuidado con la frente en alto.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Registrar, ajustar el protocolo y seguir adelante.**",
                    bodyText = """
En Recuerda registramos la recaída como un punto de ajuste en tu trayectoria, manteniendo el conteo de tu esfuerzo acumulado total. La sanación del apego no es una línea recta perfecta, es una espiral ascendente.

¿Qué medidas de blindaje necesitas reforzar hoy? ¿Borrar el chat de nuevo, silenciar notificaciones, pedir a un amigo de confianza que sea tu contacto de emergencia? Transforma el dolor del tropiezo en acción protectora concreta.
                    """.trimIndent()
                )
            )

            ClinicalCategory.AUTOCRITICA_RECAIDA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Autocompasión clínica frente al azote del crítico interno.**",
                    bodyText = """
Decirte 'soy un desastre', 'no tengo fuerza de voluntad' o 'siempre arruino todo' activa el eje del estrés y la hormona cortisol, inundando tu sistema de vergüenza y haciéndote paradójicamente mucho más propenso/a a futuras recaídas.

La investigación de Kristin Neff demuestra que la autocompasión y la amabilidad hacia uno mismo en el fallo predicen mayor resiliencia y autocontrol que la autocrítica destructiva. Tropezar frente a una adicción afectiva no te define como persona; háblate con el mismo respeto con que tratarías a un ser querido en recuperación.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Diferencia con rigor clínico la culpa de la vergüenza.**",
                    bodyText = """
La culpa adaptativa dice: *«Cometí una conducta que no se alinea con mi objetivo de bienestar (escribirle)»*, y te permite corregir el rumbo. La vergüenza tóxica dice: *«Yo estoy defectuoso/a y no valgo para nada»*.

Rechaza tajantemente la vergüenza tóxica. Tu valía como ser humano, tu inteligencia y tu dignidad permanecen intactas. No confundas un momento de desregulación neuroquímica con tu identidad global.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La fuerza de voluntad es un recurso biológico finito.**",
                    bodyText = """
Nadie tiene una 'fuerza de voluntad' infinita. La inhibición de impulsos depende de la energía metabólica de la corteza prefrontal; si estás agotado/a, triste, sin comer o con insomnio, tu capacidad de frenar un craving disminuye drásticamente a nivel fisiológico.

No fue falta de carácter, fue agotamiento de tus recursos de autorregulación. En lugar de castigarte, ocúpate de nutrir tu cuerpo, descansar y reducir la exposición a estímulos disparadores.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El perdón hacia uno mismo como herramienta terapéutica.**",
                    bodyText = """
Perdonarte a ti mismo/a por haber cedido al impulso no es indulgencia complaciente; es la única manera de desarmar el bucle de autodesprecio que te mantiene atrapado/a en la órbita de tu expareja.

Inhala profundo, coloca una mano sobre tu pecho para activar el sistema de apaciguamiento parasimpático y repite internamente: *«Fue difícil, fallé en el límite, pero sigo comprometido/a con mi sanación y merezco mi propia ternura.»*
                    """.trimIndent()
                )
            )

            ClinicalCategory.PROGRESO_POSITIVO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Consolidación de logros: tu sistema nervioso está sanando.**",
                    bodyText = """
Haber pasado horas o un día entero sin pensar en esa persona, o sentirte en paz y orgulloso/a de ti, es una confirmación biológica de que la neuroplasticidad está operando y tu cerebro se está deshabituando de la dependencia afectiva.

Saborea plenamente esta sensación de ligereza. En psicoterapia es crucial 'marcar' y registrar los momentos de bienestar (re-wire) para que el cerebro cree huellas de memoria donde la vida sin esa persona sea experimentada como segura, grata y placentera.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Valida tu progreso sin el miedo anticipatorio a recaer.**",
                    bodyText = """
A veces, cuando nos sentimos bien tras meses de tormenta, aparece una voz temerosa que susurra: 'seguro mañana vuelvo a estar mal'. No sabotees tu alivio con hipervigilancia.

El proceso tiene fluctuaciones, pero cada día de paz es terreno ganado para siempre. Permítete disfrutar de tu música, de tu comida, de tus conversaciones y de tu propio espacio mental sin pedir permiso a la tristeza.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Has demostrado que la autonomía es posible.**",
                    bodyText = """
Recuerda los primeros días de la ruptura, cuando sentías que el aire faltaba y que la vida no tenía horizonte sin ese vínculo. Compáralo con este instante en el que has podido sonreír y estar contigo.

Has sido tú quien ha sostenido los límites, quien ha procesado las lágrimas y quien ha elegido no destruirse. Este bienestar te pertenece enteramente a ti y a tus recursos de afrontamiento.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Celebrar los pequeños hitos fortalece la autoeficacia.**",
                    bodyText = """
En la psicología del comportamiento, celebrar los avances refuerza la autoeficacia (la creencia profunda en tu propia capacidad para superar la adversidad).

Hoy no es un día ordinario: es el testimonio tangible de tu resiliencia. Regálate un momento de gratitud hacia tu propio esfuerzo y anota cómo se siente esta tranquilidad en tu cuerpo para volver a ella siempre que lo necesites.
                    """.trimIndent()
                )
            )

            ClinicalCategory.CONTACTO_INEVITABLE -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Método Piedra Gris (Gray Rock) y límites operacionales.**",
                    bodyText = """
Cuando existe la obligación legal, laboral o parental de interactuar, el objetivo clínico no es el contacto cero absoluto sino el *contacto cero emocional*. La técnica de la 'Piedra Gris' consiste en volverte tan neutro, predecible y poco estimulante como una roca en el camino.

Comunícate utilizando el estándar BIFF: Breve, Informativo, Firme y Amable/Educado. No compartas detalles de tu vida personal, no preguntes por la suya y no reacciones a comentarios indirectos. Tu energía debe preservarse para lo funcional.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Delimitación de canales: la protección que brinda la estructura.**",
                    bodyText = """
En situaciones de coparentalidad, eventos familiares o trabajo compartido, la ambigüedad en los canales de comunicación es el mayor generador de recaídas ansiosas.

Establece reglas operativas estrictas: comunicación exclusivamente por correo electrónico o aplicaciones específicas para trámites prácticos, fijando horarios de respuesta no inmediatos. Tener reglas claras reduce drásticamente la incertidumbre biológica de tu sistema nervioso.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Planificación anticipatoria ante encuentros cara a cara.**",
                    bodyText = """
Si tienes un evento o coincidencia física obligada (una reunión de trabajo, una entrega de custodia, un evento de amigos comunes), planifica de antemano tu guion y tu estrategia de salida:

Define con quién hablarás, dónde te sentarás, qué harás si sientes taquicardia o incomodidad (un ancla sensorial o salir al baño a respirar) y a qué hora exacta te retirarás. Llegar con un mapa predeterminado evita que el secuestro emocional tome el control.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Proteger a los hijos o el entorno laboral de la triangulación.**",
                    bodyText = """
Si el contacto se debe a los hijos en común, tu máxima brújula es protegerlos de la tensión parental. Jamás utilices a los menores como mensajeros ni ventiles frustraciones de la ruptura en su presencia.

Trata la relación con tu expareja como una sociedad profesional exclusivamente enfocada en el bienestar de los niños. La madurez vincular consiste en coordinar la logística con impecabilidad mientras cuidas tu corazón en tu propio espacio privado.
                    """.trimIndent()
                )
            )

            ClinicalCategory.TRAICION_INFIDELIDAD -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El trauma de traición y la desorientación cognitiva.**",
                    bodyText = """
Descubrir una infidelidad o una red de mentiras sostenida genera lo que en psicología clínica denominamos 'trauma de traición' (Betrayal Trauma). La mente entra en estado de shock porque se quiebra el sentido de realidad compartida y la confianza básica en el entorno.

Es completamente natural sentir náuseas, temblores, hipervigilancia y una necesidad desesperada de repasar cada recuerdo del pasado para ver qué era verdad y qué era mentira. Valida tu indignación: tu sistema de alarma está respondiendo ante una amenaza severa a tu integridad vincular.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La infidelidad es una decisión unilateral, no un síntoma de tu valor.**",
                    bodyText = """
Una de las distorsiones más dolorosas tras un engaño es preguntarse: '¿qué le faltó a mi cuerpo, a mi forma de ser o a mi amor para que buscara a otra persona?'. Esa pregunta internaliza injustamente una falta que no te pertenece.

La infidelidad habla de los déficits de regulación, la inmadurez, la cobardía o la necesidad compulsiva de validación externa de quien rompió el pacto; jamás de tu suficiencia. Aunque hubiera dificultades en el vínculo, la honestidad y la comunicación eran las vías éticas; la traición fue su elección exclusiva.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Evita la trampa de la hipervigilancia retrospectiva.**",
                    bodyText = """
Es frecuente pasar horas revisando mensajes antiguos, fotos o fechas intentando encajar el rompecabezas del engaño. Esa conducta busca recuperar una ilusión de control, pero en la práctica actúa como una re-traumatización constante.

Acepta que nunca obtendrás una explicación que haga que la traición sea 'lógica' o justa. Lo único que necesitas saber ya lo sabes: esa persona cruzó una línea de deslealtad incompatible con tu bienestar y seguridad psicológica.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Sanar la confianza en tu propio discernimiento.**",
                    bodyText = """
El daño más profundo de la traición no es la pérdida del otro, sino la pérdida de confianza en uno mismo: '¿cómo no me di cuenta antes?'.

Trata a tu versión pasada con compasión. Tú confiaste porque eres una persona íntegra y capaz de vincularse de buena fe; no tenías por qué sospechar de quien decía amarte. La responsabilidad del engaño recae al 100% en el impostor, nunca en quien confió con nobleza.
                    """.trimIndent()
                )
            )

            ClinicalCategory.AMBIVALENCIA_EMOCIONAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Normalicemos la ambivalencia: el apego no es blanco o negro.**",
                    bodyText = """
Sentir amor y rechazo, ternura y rabia, o desear abrazar a alguien y al mismo tiempo no querer verlo nunca más es la experiencia más frecuente en el duelo afectivo. El cerebro humano no opera como un interruptor binario; los lazos de apego son complejos y multidimensionales.

No intentes forzarte a 'odiarle' para dejar de sufrir, ni te asustes cuando reaparezca un destello de afecto. La ambivalencia no es confusión patológica ni debilidad: es la evidencia de que estás procesando la totalidad de una relación real con sus luces y sus sombras.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Integración afectiva: puedes querer a alguien y elegir alejarte.**",
                    bodyText = """
Una de las mayores conquistas de la madurez psicológica es comprender que el afecto hacia una persona no obliga a permanecer a su lado. Se puede amar a alguien y tener la lucidez absoluta de que la relación es insostenible o destructiva para la propia salud mental.

Permite que ambas realidades coexistan en tu mente: *«Guardo cariño por lo que vivimos, y al mismo tiempo elijo no estar con esa persona por respeto a mi dignidad.»* La acción madura se guía por tus valores y límites, no por el sentimentalismo fluctuante.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La técnica de la aceptación radical de polaridades.**",
                    bodyText = """
Cuando luchas por 'aclararte' a la fuerza, generas una segunda capa de ansiedad. Practica la aceptación radical (ACT): siéntate con esa contradicción interior sin intentar resolverla de inmediato en tu cabeza.

Observa cómo un día predomina la tristeza y al siguiente la indignación. Ambas emociones tienen funciones terapéuticas distintas: la tristeza despide lo perdido y la rabia defiende tu territorio. Deja que cada una haga su trabajo sin interferir impulsivamente.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No tomes decisiones conductuales en los picos de ambivalencia.**",
                    bodyText = """
El riesgo de la ambivalencia es actuar cuando una de las dos emociones llega a su cresta: escribirle en un momento de amor abrumador o atacarle en un momento de furia ciega.

Establece una regla de seguridad: en momentos de contradicción afectiva intensa, ninguna decisión comunicativa. Espera a que la tormenta amaine y tu corteza prefrontal recupere el centro. La claridad llega con la quietud, no con la urgencia.
                    """.trimIndent()
                )
            )

            ClinicalCategory.SINTOMAS_FISICOS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La neurobiología del desamor: el dolor del rechazo es somático.**",
                    bodyText = """
Sentir un nudo en la garganta, opresión en el pecho, temblores en las extremidades, náuseas o pérdida total de apetito no es una invención psicológica; es una cascada neuroquímica real. Los estudios de neuroimagen demuestran que el dolor del rechazo vincular activa las mismas zonas cerebrales que el dolor físico agudo (corteza cingulada anterior y la ínsula).

Además, el organismo sufre una drástica caída de dopamina y oxitocina, combinada con una elevación severa del cortisol. Tu cuerpo está en un estado de abstinencia biológica legítima. Valida tus síntomas sin asustarte: tu sistema está respondiendo a una pérdida profunda. Si los síntomas somáticos persisten de forma severa, te recomendamos consultar a un profesional médico o terapeuta.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Regulación del nervio vago y contención fisiológica.**",
                    bodyText = """
Cuando el cuerpo se siente en peligro vincular, el sistema simpático dispara la frecuencia cardíaca y tensa la musculatura torácica. Para contrarrestarlo, debemos enviar señales fisiológicas de seguridad al cerebro a través del nervio vago.

Aplica la técnica de respiración con suspiro fisiológico: realiza dos inhalaciones seguidas por la nariz (una profunda y otra corta para expandir los alvéolos) y una exhalación muy lenta y prolongada por la boca. Repite este ciclo durante dos minutos; notarás cómo la taquicardia y la tensión muscular comienzan a ceder.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Cuidados somáticos básicos durante el impacto agudo.**",
                    bodyText = """
Durante las fases de alta reactividad física del duelo, las funciones básicas suelen descompensarse. Es imperativo establecer un protocolo de mínimos somáticos:

Hidrátate con agua y sales minerales, consume alimentos fáciles de digerir aunque sean en pequeñas porciones regulares (frutos secos, caldos, fruta) y date duchas de agua templada para relajar la musculatura contraída. No le exijas a tu cuerpo un rendimiento habitual mientras sus reservas biológicas están dedicadas a la contención emocional.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Acompañamiento profesional ante la cronificación somática.**",
                    bodyText = """
Si la opresión en el pecho, la falta de sueño o la imposibilidad de ingerir alimentos se prolongan por semanas o te impiden realizar tus actividades elementales, no dudes en acudir a un profesional médico o de salud mental.

Buscar ayuda psicoterapéutica o evaluación médica no es debilidad; es un acto de cuidado responsable. A veces el cuerpo necesita apoyo profesional específico para estabilizar el sistema neurovegetativo mientras se elabora el duelo psicológico.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RUMIACION_NOCTURNA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La caída de la inhibición prefrontal en las horas de oscuridad.**",
                    bodyText = """
Por la noche, al reducirse los estímulos sensoriales del entorno y acumularse la fatiga del día, la corteza prefrontal dorsolateral disminuye su capacidad de control inhibitorio, dejando vía libre a los bucles obsesivos de la amígdala y la red neuronal por defecto (DMN).

Por eso de noche todo parece diez veces más grave, doloroso e irresoluble. Comprende este fenómeno desde la fisiología: no estás llegando a conclusiones lúcidas de madrugada, estás sufriendo el ataque de un cerebro agotado y sin defensas cognitivas.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Técnica del vaciado mental (Brain Dump) en soporte físico.**",
                    bodyText = """
Si te desvelas rumiando sobre tu expareja, dar vueltas en la cama intentando 'no pensar' solo intensifica el insomnio y la angustia. Aplica la técnica del 'vaciado mental':

Enciende una luz tenue, toma una libreta física y escribe sin filtro todo lo que te esté torturando. Al plasmar las palabras en el papel, el cerebro experimenta que el contenido ha sido externalizado y depositado en un lugar seguro, permitiendo que la tensión cognitiva baje un escalón. Luego, cierra la libreta y déjala fuera del dormitorio.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Higiene de estímulos nocturnos y cuarentena de pantallas.**",
                    bodyText = """
Revisar el móvil en la cama o desvelarse mirando perfiles de redes sociales proyecta luz azul en la retina, suprimiendo la melatonina y enviando una señal de alarma dopaminérgica directa al sistema límbico.

Establece una regla inviolable de higiene del sueño: el teléfono se queda fuera del alcance de tu mano al acostarte. Si el sueño no llega, escucha audios de respiración somática o sonidos binaurales relajantes sin mirar pantallas. Protege tu noche como el espacio sagrado de tu recuperación.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El acuerdo de suspensión temporal con tu mente.**",
                    bodyText = """
Cuando un pensamiento obsesivo irrumpa en mitad de la noche, utiliza la fórmula terapéutica de suspensión temporal: *«Acepto que este tema me importa mucho, pero ahora mi cuerpo necesita dormir para tener recursos mañana. Me comprometo a dedicarle 15 minutos de atención consciente mañana a las cinco de la tarde.»*

Postergando la deliberación para un momento diurno estructurado, rompes la urgencia artificial de la rumiación nocturna y facilitas el reingreso al ciclo del sueño.
                    """.trimIndent()
                )
            )

            ClinicalCategory.METAPREGUNTAS_PROCESO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La no linealidad del duelo: desmitificando las etapas fijas.**",
                    bodyText = """
Preguntarse '¿esto es normal?' o '¿cuánto va a durar este sufrimiento?' es una respuesta adaptativa ante la incertidumbre del dolor. Es fundamental desterrar el mito de que el duelo es una escalera mecánica donde se avanza linealmente de la negación a la aceptación.

El duelo vincular se parece mucho más a las mareas o a una espiral: hay semanas de notable avance seguidas de días donde reaparece la tristeza aguda. Esa fluctuación no significa retroceso; significa que tu psique está integrando la pérdida en capas sucesivas de profundidad.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No existen plazos universales: cada sistema de apego tiene su compás.**",
                    bodyText = """
No te compares con amigos que 'lo superaron en tres meses' ni te angusties con fechas límites arbitrarias. La duración de un duelo depende de variables clínicas complejas: la duración del vínculo, el estilo de apego, la presencia de traición, el grado de fusión cotidiana y los apoyos de tu red social.

Exigirte estar 'curado/a' para cierta fecha genera una segunda capa de sufrimiento autoimpuesto. Confía en tu propia cronología biológica y psicológica; sanarás cuando tus circuitos afectivos hayan terminado de asimilar la nueva realidad.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Mide el progreso por la intensidad y duración de las olas, no por su ausencia.**",
                    bodyText = """
Si evalúas tu mejoría esperando no sentir jamás una punzada de dolor, te sentirás fracasado/a constantemente. El verdadero indicador de progreso terapéutico es cómo respondes a esas punzadas:

¿Cuánto tiempo te dura el bajón ahora comparado con las primeras semanas? ¿Consigues evitar llamarle a pesar de la tristeza? ¿Eres capaz de seguir trabajando o comiendo? Esos cambios sutiles en tu capacidad de contención son las pruebas reales de que estás sanando.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El dolor del duelo es una adaptación funcional que se extingue.**",
                    bodyText = """
Ten la certeza clínica de que ningún dolor humano permanece en su pico de intensidad para siempre. El cerebro humano está evolutivamente diseñado para adaptarse a la pérdida mediante la habituación y la reorganización sináptica.

Esta etapa de dolor agudo pasará. Lo que hoy sientes como un desgarro insoportable irá mutando paulatinamente en una memoria serena y asimilada en tu historia personal. Sostén el proceso con paciencia y amabilidad hacia ti mismo/a.
                    """.trimIndent()
                )
            )

            ClinicalCategory.BUSQUEDA_REAFIRMACION -> listOf(
                ClinicalVariant(
                    headerGreeting = "**De la búsqueda de validación externa a la autoeficacia interna.**",
                    bodyText = """
Preguntar con insistencia '¿hice bien en bloquearle?' o 'dime que hice lo correcto' es una manifestación clásica de la duda obsesiva inducida por la abstinencia afectiva. Cuando el craving relacional aprieta, la mente intenta buscar un resquicio de culpa para justificar el regreso.

En lugar de buscar que alguien de afuera te otorgue un 'permiso' para haberte cuidado, conecta con tu propia voz interna: ¿qué sentías en tu cuerpo y en tu vida en los días previos a tomar esa decisión? La certeza no se encuentra en el consenso ajeno, sino en tu fidelidad a tus propios límites.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El sesgo de memoria eufórica: cuando el cerebro maquilla el pasado.**",
                    bodyText = """
Tras poner un límite firme o terminar una relación, el cerebro suele recurrir a la 'memoria eufórica', recordando selectivamente los momentos tiernos o divertidos y censurando los momentos de angustia, desprecio o soledad que te llevaron a alejarte.

Por eso hoy dudas de tu decisión. Haz un ejercicio de memoria completa y realista: escribe en un papel las razones concretas, dolorosas y verificables que motivaron tu partida. Lee esa lista cada vez que la duda intente sabotear tu límite.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Bloquear o poner distancia no es agresión: es higiene de salud mental.**",
                    bodyText = """
A menudo la duda se viste de falso escrúpulo moral: '¿habré sido muy inmaduro/a o radical por bloquear?'. Es necesario desmitificar esto con claridad clínica:

El contacto cero o el bloqueo no se hacen para castigar al otro ni para ganar una disputa de poder; se hacen para detener la estimulación dopaminérgica compulsiva y proteger tu salud mental. No necesitas justificar una medida de primeros auxilios emocionales.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Acepta la incomodidad de sostener una decisión difícil.**",
                    bodyText = """
Tomar decisiones de salud vincular suele sentirse muy incómodo al principio porque va en contra del impulso adictivo del apego. La madurez consiste en tolerar esa incomodidad temporal sin claudicar.

Hiciste lo mejor que pudiste con la información, el desgaste y el dolor que tenías en ese momento. Deja de juzgar a tu 'yo' de entonces con la calma que tienes hoy. Honra tu determinación y mantén tu palabra.
                    """.trimIndent()
                )
            )

            ClinicalCategory.OBJETOS_RECUERDOS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Los objetos como anclajes sensoriales del craving relacional.**",
                    bodyText = """
Las fotos en el teléfono, los regalos en la mesita de noche o las prendas de ropa de tu expareja no son cosas neutras: son anclajes sensoriales (disparadores de memoria procedimental y emocional) que activan de inmediato el sistema dopaminérgico y reactivan la rumiación.

Tener esos estímulos en tu campo visual cotidiano es el equivalente a intentar dejar el tabaco teniendo cigarrillos encendidos por toda la casa. Modificar tu entorno no es inmadurez, es optimización de tus condiciones ambientales para sanar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La técnica de la 'Caja de Cuarentena' (Out of sight, out of mind).**",
                    bodyText = """
No tienes que forzarte a tomar decisiones irreversibles (como quemar o tirar todo a la basura) si eso te genera una resistencia angustiosa en este momento. La recomendación clínica más eficaz es la 'Caja de Cuarentena':

Toma una caja de cartón, recoge todos los objetos, fotos impresas y recuerdos asociados, séllala con cinta adhesiva y guárdala en el fondo de un armario, en el trastero o entrégasela a un amigo de confianza con la instrucción de no dártela durante tres meses. Sácalos de tu vista para darle un respiro a tu cerebro.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Higiene en la galería digital: mover a carpeta segura o archivar.**",
                    bodyText = """
Tener miles de fotos compartidas en el carrete del móvil es una fuente constante de recaídas involuntarias. Aplica una medida de protección digital:

Pasa todas las fotos a una carpeta en una nube o disco externo, o utiliza la función de 'ocultar recuerdos' de las aplicaciones de fotos. Pon barreras de fricción para que no puedas acceder a ellas en un segundo de impulso nocturno.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El valor simbólico del despeje: hacer espacio para lo nuevo.**",
                    bodyText = """
Deshacerse o apartar los objetos del pasado tiene una poderosa dimensión simbólica en la terapia gestalt y en los modelos de reconstrucción de identidad. Tu entorno físico refleja y condiciona tu estado mental.

Al retirar lo que perteneció a un ciclo cerrado, le estás enviando a tu mente el mensaje claro de que estás listo/a para habitar tu presente. Recupera tu espacio: decóralo a tu gusto, muévelo y conviértelo en el santuario de tu propia reconstrucción.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ESTANCAMIENTO_PROCESO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El sesgo de adaptación y la ceguera al propio progreso.**",
                    bodyText = """
Sentir que 'llevas meses y sigues igual' suele ser una ilusión óptica de la mente causada por la adaptación al dolor residual. Cuando el sufrimiento pasa de ser una crisis aguda e incapacitante a un dolor sordo y de fondo, tendemos a olvidar la intensidad del colapso inicial.

Haz una auditoría retrospectiva con datos objetivos: compara cuántas horas llorabas en la primera semana frente a hoy; cómo dormías entonces y cómo duermes ahora; cuántos días de contacto cero has sostenido. Descubrirás que el avance existe, aunque la persistencia del vacío lo opaque.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La meseta del duelo: la cicatrización profunda es invisible.**",
                    bodyText = """
En los procesos de duelo, tras las primeras semanas de cambios visibles suele presentarse una prolongada 'meseta' donde parece que nada se mueve. En realidad, es en esta fase donde se produce la consolidación de la identidad y la reconfiguración profunda del autoconcepto.

No confundas la falta de euforia con estancamiento. Sostener la vida cotidiana, ir a trabajar y mantener la dignidad en medio del desgano es una forma altísima de resiliencia silenciosa.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Identificar posibles conductas de mantenimiento del dolor.**",
                    bodyText = """
Si efectivamente ha pasado un tiempo muy prolongado y sientes un bloqueo severo, es útil revisar si existen 'conductas de seguridad' o anclajes encubiertos que estén alimentando la herida:

¿Sigues preguntando por esa persona a amigos comunes? ¿Escuchas música nostálgica en bucle? ¿Consumes contenido sobre reconciliación o exparejas en internet? A veces el estancamiento no es natural, sino el resultado de micro-dosis cotidianas de contacto indirecto que no dejan cerrar la cicatriz.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Paciencia activa y ampliación del repertorio conductual.**",
                    bodyText = """
Superar una ruptura no es solo 'esperar a que el tiempo pase', sino llenar el tiempo de nuevas experiencias significativas que compitan con la huella mnémica del pasado.

Si tu rutina se ha vuelto monótona y vacía, el cerebro recurrirá al recuerdo por pura falta de estímulos novedosos. Es hora de activar la conducta: retoma un hobby olvidado, aprende algo nuevo, reconecta con viejas amistades o cambia de ambiente. La sanación se acelera cuando el presente se vuelve interesante.
                    """.trimIndent()
                )
            )

            ClinicalCategory.DUDA_HABER_TERMINADO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El duelo del que toma la decisión: validar el dolor del límite.**",
                    bodyText = """
Existe una incomprensión social generalizada según la cual quien toma la iniciativa de cortar la relación 'no tiene derecho a sufrir' o 'la tiene fácil'. En la práctica clínica sabemos que terminar un vínculo donde aún existía afecto por razones de incompatibilidad, maltrato o desgaste es una de las decisiones más desgarradoras y valientes que existen.

Valida tu dolor sin culpa. Dejar a alguien no significa que no le quisieras; significa que comprendiste con dolorosa madurez que el amor por sí solo no basta para sostener una convivencia saludable.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El engaño de la memoria tras el alivio inicial.**",
                    bodyText = """
Tras el paso de las semanas, cuando la sensación de asfixia que motivó la ruptura se disipa, la mente suele jugarte una mala pasada: olvida el motivo que te llevó a romper y solo recuerda lo entrañable, haciéndote dudar: '¿fui muy egoísta?'.

No caigas en esa amnesia selectiva. Regresa a las causas profundas que te llevaron a dar el paso: los límites traspasados, la soledad compartida, la falta de reciprocidad. Esos motivos siguen siendo exactamente tan reales hoy como el día en que dijiste adiós.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Diferenciar la tristeza por la pérdida de la equivocación.**",
                    bodyText = """
Es fundamental desarticular la ecuación errónea: *«Como me duele tanto y le extraño, significa que me equivoqué al terminar.»*

Sentir tristeza es la respuesta natural a la pérdida de un proyecto afectivo significativo. Extrañar a alguien es una consecuencia neurobiológica del desapego, no un indicador de que debas volver atrás. Se puede extrañar intensamente y, al mismo tiempo, saber con certeza que la decisión fue la correcta.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Sostener la responsabilidad afectiva hacia ti mismo/a.**",
                    bodyText = """
Regresar a una dinámica disfuncional solo para apagar la culpa de haber hecho daño o para calmar la soledad temporal es un acto de auto-traición que tarde o temprano explotará de nuevo con mayor virulencia.

Asume el peso de haber sido quien cerró la puerta como el precio necesario por tu libertad y tu paz a largo plazo. Diste el paso para proteger tu futuro; mantén la frente alta y dale tiempo a tu vida para reorganizarse sobre bases sólidas.
                    """.trimIndent()
                )
            )

            else -> ClinicalVariantsPsicologia.getVariants(ClinicalCategory.RECONSTRUIR_GENERAL)
        }
    }
}
