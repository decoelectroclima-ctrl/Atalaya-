package com.example.ai

/**
 * Variantes clínicas y espirituales para las 15 nuevas categorías de Recuerda (Marco CATÓLICO).
 * Cada categoría contiene 4 intervenciones con profunda unción espiritual, esperanza teologal y sabiduría pastoral.
 */
object ClinicalNewCategoriesCatolico {

    fun getVariants(category: ClinicalCategory): List<ClinicalVariant> {
        return when (category) {
            ClinicalCategory.NUEVA_PAREJA_EX -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La Providencia de Dios sobre tu vida no depende de la conducta ajena.**",
                    bodyText = """
Saber que tu expareja ha iniciado un nuevo vínculo o que parece haber 'rehicho su vida' sacude las fibras del orgullo y la memoria. Sin embargo, tu valor infinito ante el Padre Celestial fue sellado en la Cruz y nada ni nadie en esta tierra puede rebajarlo.

No compares tu camino con el de quien busca consuelos inmediatos en el mundo. Entrega a esa persona a la misericordia divina y despréndete del anhelo de saber de su vida; Dios tiene para ti un designio de paz, santidad y maduración interior que ningún desenlace ajeno puede frustrar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No envidies los espejismos de quien huye de su propio desierto.**",
                    bodyText = """
A menudo, correr a los brazos de otra persona con rapidez no es signo de plenitud ni de victoria espiritual, sino el intento temeroso de esquivar el silencio, el examen de conciencia y el dolor necesario del alma.

Tú estás transitando este desierto con valentía, purificando tu corazón y apoyándote en la Roca firme que es Cristo. No te dejes encandilar por apariencias efímeras: la verdadera paz del corazón no se improvisa con parches afectivos.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Bendice desde la distancia y custodia tu templo interior.**",
                    bodyText = """
El Evangelio nos llama a desear el bien incluso de quienes nos han herido o dejado atrás, pero la caridad cristiana no exige alimentar la curiosidad destructiva ni mirar por la cerradura de vidas ajenas.

Haz una oración sincera encomendando sus almas a Dios y, en ese mismo instante, traza una muralla santa de desapego en tu corazón. Tu alma es templo del Espíritu Santo; no permitas que la amargura o el despecho profanen su sosiego.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Descansa en la soberanía de los tiempos del Señor.**",
                    bodyText = """
Puede que sientas la tentación de preguntarte por qué a otros les parece tan fácil avanzar mientras tú experimentas el peso del duelo. Recuerda el Salmo 37: *«Encomienda tu camino al Señor, confía en Él, que Él actuará.»*

Dios no tiene prisa: Él trabaja en lo hondo. Tu historia no ha terminado aquí; estás siendo moldeado/a en el crisol de la paciencia para un amor más puro, noble y conforme a la voluntad divina.
                    """.trimIndent()
                )
            )

            ClinicalCategory.MIEDO_FUTURO_SOLEDAD -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El Señor es tu pastor: nada te habrá de faltar.**",
                    bodyText = """
El enemigo de las almas aprovecha los valles de soledad para sembrar la desesperanza: 'nunca encontrarás a nadie', 'te quedarás solo/a para siempre'. Esas voces son mentiras que buscan nublar la bondad paternal de Dios.

El Padre, que viste a los lirios del campo y alimenta a las aves del cielo con infinita ternura, conoce hasta el último cabello de tu cabeza y los anhelos más puros de tu corazón. Desecha el catastrofismo y descansa filialmente en Su providencia.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No conviertas a ninguna criatura en el ídolo de tu felicidad.**",
                    bodyText = """
Decir 'no habrá nadie como él o ella' es un signo de cómo el afecto humano puede llegar a usurpar el lugar de lo absoluto en el alma. San Agustín lo descubrió tras amargas lágrimas: *«Nos hiciste, Señor, para Ti, y nuestro corazón está inquieto hasta que descanse en Ti.»*

Ningún ser humano imperfecto era la fuente definitiva de tu paz o de tu sentido existencial. Deja de temer al porvenir: si Dios permite este tiempo de soltería, es para sanar tu corazón y enseñarte a amar desde la libertad de los hijos de Dios.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La santa soledad es el taller donde Dios habla al alma.**",
                    bodyText = """
En las Sagradas Escrituras, Dios siempre llevó a sus elegidos al desierto para hablarles al corazón (Oseas 2, 14). La soledad no es una maldición ni un callejón sin salida; es el espacio sagrado donde se desmoronan los apegos desordenados.

Acoge este tiempo no con temor, sino como una gracia providencial para consolidar tu vida de oración, tu discernimiento vocacional y tus obras de servicio hacia los demás.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La virtud teologal de la Esperanza frente a la incertidumbre.**",
                    bodyText = """
El católico no camina a ciegas por el destino; camina sostenido por la Esperanza cristiana, que no defrauda. No sabes qué rostros, qué amistades o qué misión te deparará el futuro, pero sabes en Quién tienes puesta tu fe.

Reza con las palabras de San Pablo: *«Sé en quién he creído, y estoy persuadido de que tiene poder para custodiar mi depósito hasta aquel Día.»* Entrega tu mañana y ocúpate de amar y cumplir tu deber el día de hoy.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RECAIDA_OCURRIDA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Dios no se cansa de perdonar ni de sostener al caído.**",
                    bodyText = """
Haber mandado ese mensaje, realizado esa llamada o caído en la tentación del contacto no te convierte en una causa perdida. El justo cae siete veces, y siete veces se vuelve a levantar sostenido por la diestra del Señor (Proverbios 24, 16).

No permitas que el acusador llene tu alma de tinieblas haciéndote creer que todo tu esfuerzo fue inútil. Reconoce con humildad la flaqueza de tu carne ante el Señor, pide su fortaleza y renueva tu firme propósito de enmienda en este mismo minuto.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El hijo pródigo se levantó del barro sin mirar atrás.**",
                    bodyText = """
En la parábola evangélica, cuando el hijo pródigo comprendió su error en medio del fango, no se quedó revolcándose en la culpa ni buscando justificaciones; tomó la firme resolución de levantarse y caminar hacia la casa del Padre.

Haz tú lo mismo. Has comprobado una vez más que en ese vínculo roto solo hay vacío e inquietud de espíritu. Levántate, sacúdete el polvo de la recaída y vuelve al santuario de tu propia paz interior.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No justifiques la falta con un segundo tropiezo.**",
                    bodyText = """
La tentación suele susurrar tras una recaída: 'ya que le escribiste, insístele a ver qué responde'. Corta de raíz esa insidia con santa templanza.

No envíes disculpas prolijas ni mendigues respuestas. Tu mejor testimonio de conversión y autodominio es el silencio humilde y resuelto a partir de este instante. Dios perfecciona su fuerza en tu debilidad.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Acepta la lección de humildad que encierra la caída.**",
                    bodyText = """
Quizás te habías confiado demasiado en tus propias fuerzas humanas y descuidaste la vigilancia y la oración. Los santos enseñan que Dios a veces permite que tropecemos para recordarnos nuestra fragilidad radical y obligarnos a apoyarnos en Él.

Agradece este baño de realismo: te recuerda que sin la gracia no puedes nada. Fortalece tus defensas, evita las ocasiones de pecado o tropiezo afectivo y reanuda tu camino con mayor sobriedad.
                    """.trimIndent()
                )
            )

            ClinicalCategory.AUTOCRITICA_RECAIDA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La voz que te maldice y desprecia no proviene de Dios.**",
                    bodyText = """
Llamarte a ti mismo/a 'inútil', 'desastre' o 'débil' tras haber cedido al impulso no es fruto del Espíritu Santo, sino del espíritu de acusación que busca hundirte en la desesperación y alejarte de la misericordia divina.

El dolor por haber fallado debe llevarte a la contrición humilde y esperanzada, jamás a la condenación y al odio hacia tu propia persona. Dios te contempla con una infinita compasión pedagógica; mírate con los ojos de misericordia con que el Padre te mira.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Distingue el santo dolor de la culpa estéril.**",
                    bodyText = """
San Pablo decía a los corintios que hay una tristeza según Dios que produce un arrepentimiento saludable que lleva a la salvación, y hay una tristeza del mundo que produce muerte y amargura (2 Corintios 7, 10).

El castigo mental con el que te flagelas es esa tristeza del mundo que paraliza tu alma. Renuncia a esa soberbia herida que pretendía ser perfecta por sí misma. Pide al Señor su gracia y perdónate con ternura fraterna.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Trátate como al prójimo más necesitado en este día.**",
                    bodyText = """
El mandato evangélico nos pide amar al prójimo como a nosotros mismos. Si vieras a un hermano afligido llorando tras haber caído en una debilidad afectiva, ¿lo pisotearías con insultos o le tenderías la mano para levantarlo con caridad?

Comienza practicando la caridad cristiana contigo mismo/a. Reconoce tu miseria sin escándalo, abrázate con misericordia y camina con paso humilde pero seguro.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**En la herida abierta se derrama el bálsamo de la Gracia.**",
                    bodyText = """
Decía San Francisco de Sales que la impaciencia con nuestras propias imperfecciones proviene del amor propio disgustado de verse imperfecto. No te indignes de ser débil; haz de esa debilidad una súplica ferviente al Señor.

*«Señor Jesús, Hijo de David, ten piedad de mí que soy un pecador necesitado de tu luz.»* Respira en esa verdad: Dios no busca almas inmaculadas por su propio orgullo, sino corazones contritos y humillados que se dejan levantar por Su amor.
                    """.trimIndent()
                )
            )

            ClinicalCategory.PROGRESO_POSITIVO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Canta al Señor un cántico nuevo por la paz recuperada.**",
                    bodyText = """
Haber pasado horas o una jornada entera en serenidad, sin que el peso del recuerdo te asfixie, es una obra evidente de la gracia divina operando en tu fidelidad cotidiana. Da gracias a Dios con el salmista: *«Cambiaste mi luto en danzas, me desataste el sayal y me colmaste de alegría.»*

Saborea esta paz no como una casualidad terrenal, sino como el don del Espíritu Santo que consuela al afligido. Estás saliendo del mar Rojo y la orilla de la tierra prometida comienza a vislumbrarse.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Guarda este testimonio de luz para los días de niebla.**",
                    bodyText = """
San Ignacio de Loyola enseña en sus reglas de discernimiento que en tiempo de consolación y paz espiritual debemos acumular fuerzas y recordar la bondad de Dios para cuando regrese la desolación o la prueba.

Disfruta de este alivio en el pecho y de esta ligereza interior. Agradece a Dios por haberte dado la fuerza para resistir en los momentos más oscuros y renuévate en el compromiso de seguir cuidando la pureza de tu corazón.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La paz de Cristo es más profunda que cualquier apego efímero.**",
                    bodyText = """
Hoy estás experimentando lo que Jesús prometió a los suyos: *«La paz os dejo, mi paz os doy; no os la doy yo como el mundo la da.»* (Juan 14, 27). La paz que viene de ordenar la vida conforme a la verdad es infinitamente superior a la euforia transitoria de un afecto desordenado.

Continúa cultivando tus deberes con alegría y rectitud de intención. Estás viviendo la libertad que nace de no estar encadenado/a a ninguna dependencia terrenal.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Humilde gratitud por los frutos de la perseverancia.**",
                    bodyText = """
No te vanaglories de tu fuerza personal, pero sí alégrate santamente de haber sido dócil a los límites y a la prudencia. Cada día de fidelidad a ti mismo/a y a Dios ha sido una piedra viva en la reconstrucción de tu templo interior.

Camina hoy con la frente en alto y una sonrisa serena: Dios está haciendo nuevas todas las cosas en ti.
                    """.trimIndent()
                )
            )

            ClinicalCategory.CONTACTO_INEVITABLE -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Prudencia como serpientes y sencillez como palomas.**",
                    bodyText = """
Cuando el contacto es un deber ineludible por los hijos comunes, el trabajo o compromisos familiares ineludibles, recuerda la advertencia de Nuestro Señor en Mateo 10, 16: sé prudente y astuto para no exponerte al daño, y conserva la sencillez del alma para no guardar rencor.

Tu conducta debe ser irreprochable, civil y ceñida con exactitud a la obligación moral o logística que los convoca. No busques explicaciones ni te detengas en conversaciones sobre el pasado; cumple tu parte con rectitud cristiana y regresa a tu retiro de paz.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La caridad verdadera jamás es enemiga de la prudencia.**",
                    bodyText = """
Existe a veces la falsa idea de que ser buen cristiano exige ser ingenuo, complaciente o dejarse pisotear por quien quebrantó la confianza. Santo Tomás de Aquino enseña que la caridad sin la virtud de la prudencia degenera en imprudencia destructiva.

Trata a esa persona con educación sobria, pero con murallas protectoras firmes. Dios no te pide que seas vulnerable ante quien demostró no respetar tu corazón; te pide rectitud moral y distancia santa.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Los hijos como don sagrado que exige paz parental.**",
                    bodyText = """
Si la coparentalidad es la causa del contacto, contempla a tus hijos como un tesoro sagrado que Dios ha puesto bajo tu custodia. Ellos no deben ser jamás campo de batalla ni testigos de desavenencias adultas.

Que cada intercambio sea un testimonio de madurez cristiana: puntualidad impecable, respeto en las formas y atención exclusiva a sus necesidades escolares, de salud o recreación. La paz que siembres en esa logística será una bendición para su porvenir.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Encomienda el encuentro a San Miguel Arcángel y a tu Ángel Custodio.**",
                    bodyText = """
Antes de asistir a esa reunión laboral o encuentro forzado, reza una breve jaculatoria a tu Ángel de la Guarda pidiéndole que custodie tus palabras, serene tus latidos y proteja tu corazón de cualquier dardo inflamado de provocación.

Entra con la dignidad de un hijo de Dios y sal con la tranquilidad de haber cumplido tu deber sin ceder un milímetro de tu sosiego interior.
                    """.trimIndent()
                )
            )

            ClinicalCategory.TRAICION_INFIDELIDAD -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La verdad te ha hecho libre, aunque la verdad desgarre el alma.**",
                    bodyText = """
El dolor de descubrir el engaño o la infidelidad es uno de los cálices más amargos de beber; Nuestro Señor mismo experimentó el beso de la traición en el huerto de Getsemaní por parte de uno de los suyos.

No te avergüences de tu llanto ni de tu santa indignación moral. Quien miente y quebranta un pacto sagrado ofende ante todo a Dios y mancha su propia alma con el pecado de la perfidia. Tú ofreciste lealtad y confianza; tu ofrenda fue pura ante los ojos de Dios.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El perdón cristiano es liberación del alma, no reconciliación obligada.**",
                    bodyText = """
Es vital comprender la doctrina de la Iglesia: perdonar de corazón significa renunciar a la venganza, al odio y al deseo de que el otro sea destruido, entregando el juicio a la justicia divina. Pero el perdón no exige reanudar la relación ni volver a exponerse al abuso o a la infidelidad.

Puedes perdonar cristianamente la deuda espiritual desde la distancia y, al mismo tiempo, poner un límite absoluto y definitivo a la convivencia. El pastor prudente no vuelve a meter a las ovejas en la cueva del lobo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No dejes que el pecado ajeno envenene tu propia caridad.**",
                    bodyText = """
El mayor peligro espiritual tras una traición es que el veneno del cinismo y la amargura se instale en tu corazón, haciéndote desconfiar de todo el mundo o desear el mal a quien te engañó. Recuerda Romanos 12, 21: *«No te dejes vencer por el mal, antes bien vence al mal con el bien.»*

Vence al mal no permitiendo que la deslealtad del otro te robe tu capacidad de ser una persona noble, pura y temerosa de Dios. Deja la justicia en manos de Aquel que escruta los corazones.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Cristo comprende en carne viva tu dolor por la deslealtad.**",
                    bodyText = """
Une tus lágrimas al místico costado herido de Cristo crucificado. Él sabe lo que es ser vendido por monedas, abandonado por los más íntimos y expuesto a la burla pública.

Ofrece este inmenso dolor en reparación por tus pecados y por la conversión de los desleales. En medio de esta agonía espiritual, experimentarás la gracia consoladora del Resucitado que restaura a los de corazón quebrantado y venda sus heridas.
                    """.trimIndent()
                )
            )

            ClinicalCategory.AMBIVALENCIA_EMOCIONAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El corazón humano es un mar agitado: afianza tu ancla en la Verdad.**",
                    bodyText = """
Sentir afecto y resentimiento en la misma hora, o desear abrazar y a la vez huir, es la prueba de la división interior que la condición caída experimenta ante el desgarro de un lazo. San Pablo lo expresaba con dolor: *«No hago el bien que quiero, sino el mal que no quiero.»* (Romanos 7, 19).

No te juzgues con desesperación por no tener un corazón de una sola pieza en este momento. Reconoce tu turbación interior ante el Sagrario o en tu rincón de oración y pide la gracia de que la recta razón iluminada por la fe gobierne sobre las pasiones desatadas.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La caridad no se confunde con el sentimentalismo ciego.**",
                    bodyText = """
El amor cristiano (Ágape) no es un cosquilleo en el estómago ni un apego ciego que todo lo tolera sin discernimiento; es buscar el bien verdadero de las personas. Y el mayor bien para ti y para esa persona a menudo es la distancia que corta la dinámica de pecado, daño mutuo o idolatría.

Puedes orar por la salvación de esa persona y desear que encuentre a Dios, sin por ello consentir en regresar a una relación que te destruye. El amor maduro sabe decir 'no' cuando la verdad lo exige.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**En la duda afectiva, guíate por los mandamientos y la paz interior.**",
                    bodyText = """
Cuando las emociones tiren de ti en direcciones contrarias, no tomes decisiones basadas en el impulso que grite más fuerte. Busca el consejo sabio de un confesor o director espiritual prudente y apóyate en lo que produce verdadera paz duradera en la conciencia.

El Espíritu Santo no habla en el estrépito de la contradicción desatada, sino en la brisa suave de la fidelidad a los mandamientos de Dios y a la propia dignidad.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Somete tus afectos a la luz de la Cruz.**",
                    bodyText = """
Pide al Señor que purifique tu memoria de todo rencor estéril y despoje a tu imaginación de toda idealización fantasiosa. Deja que la gracia queme las impurezas del apego para que solo quede lo que agrada a Dios.

Conforme pongas a Cristo en el centro de tu corazón, los afectos desordenados irán perdiendo su fuerza de gravedad y tu alma recuperará la luminosa unidad de propósito.
                    """.trimIndent()
                )
            )

            ClinicalCategory.SINTOMAS_FISICOS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El cuerpo es vaso de barro que siente la herida del alma.**",
                    bodyText = """
El llanto amargo, el pecho cerrado por la angustia, la fatiga extrema y el nudo en la garganta nos recuerdan nuestra fragilidad creatural. San Pablo nos decía que *«llevamos este tesoro en vasijas de barro para que se vea que una fuerza tan extraordinaria es de Dios y no de nosotros.»* (2 Corintios 4, 7).

No te avergüences de tus síntomas físicos ni intentes reprimirlos con dureza insensible. Descansa tu cuerpo fatigado, come alimentos sencillos dando gracias a Dios y cuida la salud que el Creador te encomendó como mayordomo fiel. Si el malestar físico es intenso o prolongado, acudir al médico es un deber de prudencia moral.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La oración de abandono calma el sobresalto de la carne.**",
                    bodyText = """
Cuando sientas que la respiración se acorta o que el corazón late con angustia desbocada, siéntate en silencio, pon tu mano derecha sobre tu pecho y repite con cadencia lenta la plegaria de los monjes del desierto: *«Señor Jesucristo, Hijo de Dios vivo, ten piedad de mí.»*

Deja que la dulzura del Nombre de Jesús apacigüe las tormentas somáticas. Al compás de la oración confiada, el cuerpo recibe el sosiego que el mundo no puede darle.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El valor redentor del sufrimiento físico ofrecido con amor.**",
                    bodyText = """
En la teología católica, el dolor no es un absurdo sin sentido: cuando se une a la Pasión de Cristo, se transforma en fuente inagotable de gracia y purificación para el alma y para la Iglesia.

Ofrece cada palpitación dolorosa, cada noche de desvelo y cada lágrima por una intención noble: la conversión de un ser querido, la paz en tu familia o el alivio de las almas más solas del purgatorio. Al darle un sentido sobrenatural, el sufrimiento físico deja de ser una tortura y se convierte en camino de santidad.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Caridad hacia tu propia naturaleza corporal.**",
                    bodyText = """
San Francisco de Asís llamaba cariñosamente al cuerpo 'el hermano asno', advirtiendo que no se le debe maltratar ni exigir cargas desmedidas cuando está extenuado por el camino.

Ten paciencia con tus ritmos biológicos. Duerme lo que puedas, camina al sol para recibir la luz de la creación y rodéate de personas de fe que te sostengan en la oración. El Señor levantará tus fuerzas como las de las águilas.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RUMIACION_NOCTURNA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**En paz me acuesto y en seguida me duermo, porque solo Tú, Señor, me haces vivir confiado.**",
                    bodyText = """
El Salmo 4 nos ofrece el mejor remedio contra el asedio de la rumiación en la oscuridad. Durante la noche, el alma se siente desguarnecida y los pensamientos del pasado cobran una apariencia gigantesca e intimidante.

Rechaza dialogar con los recuerdos a media noche. Reza la oración de Completas de la Iglesia antes de dormir y confía tu descanso a las manos de María Santísima: *«Bajo tu amparo nos acogemos, Santa Madre de Dios; no deseches las súplicas que te dirigimos en nuestras necesidades.»*
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Apaga las pantallas del mundo y enciende la lámpara de la fe.**",
                    bodyText = """
Desvelarse mirando las redes sociales o buscando señales de la expareja en la madrugada es una ocasión voluntaria de tormento que profana la noche destinada al reposo de los hijos de Dios.

Retira el teléfono de tu habitación. Si la vigilia te sorprende, reza un misterio del Santo Rosario con cuentas en la mano; el Rosario es una espada espiritual que disipa las tinieblas de la obsesión y adormece al alma en el regazo de la Virgen.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Entrega los enigmas del pasado al Divino Juicio.**",
                    bodyText = """
Dar vueltas a las palabras que se dijeron, a las promesas rotas o a los silencios no cambiará el curso de lo acontecido. Recuerda las palabras de Jesús en el Sermón de la Montaña: *«¿Quién de vosotros, por mucho que se preocupe, puede añadir un solo codo a la estatura de sus días?»* (Mateo 6, 27).

Dile al Señor: *«Jesús, yo no entiendo por qué sucedió esto, pero confío en Ti. Te entrego mis preguntas y elijo dormir bajo tu santa mirada.»*
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La bendición nocturna sobre tu mente atribulada.**",
                    bodyText = """
Traza sobre tu frente y sobre tu almohada la señal de la Santa Cruz antes de reclinar la cabeza. La Cruz santifica tu reposo y expulsa las sugestiones de angustia y remordimiento que merodean en la noche.

Descansa sabiendo que el Dios que cuida de Israel no duerme ni reposa; mientras tú duermes, Su Providencia vela incansablemente por tu vida y por tu porvenir.
                    """.trimIndent()
                )
            )

            ClinicalCategory.METAPREGUNTAS_PROCESO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Todo tiene su tiempo bajo el cielo: paciencia en el crisol.**",
                    bodyText = """
Preguntarte '¿cuánto va a durar este desierto?' o '¿algún día volveré a sentirme en paz?' es un clamor humano que los salmistas repitieron incansablemente: *«¿Hasta cuándo, Señor, me olvidarás? ¿Hasta cuándo me esconderás tu rostro?»* (Salmo 13).

La Escritura nos enseña en Eclesiastés 3 que todo tiene su momento oportuno: tiempo de llorar y tiempo de reír; tiempo de abrazar y tiempo de abstenerse de abrazar. No quieras acelerar la cosecha antes de que la semilla haya muerto a sus apegos terrenales. Confía en el tiempo de Dios.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La tribulación produce paciencia, la paciencia virtud probada, y la virtud probada esperanza.**",
                    bodyText = """
San Pablo en Romanos 5, 3-5 nos entrega la clave espiritual del proceso de duelo. Este tiempo de dolor no es un castigo ciego de Dios; es una pedagogía santa donde se forja la fortaleza de tu alma y la madurez de tu fe.

Sí, es completamente normal que el duelo lleve tiempo y que haya días donde sientas que el dolor regresa. No te desalientes: la esperanza cristiana no defrauda, porque el amor de Dios ha sido derramado en nuestros corazones.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Dios no mide tu sanación por la ausencia de lágrimas.**",
                    bodyText = """
Jesús lloró ante la tumba de su amigo Lázaro y experimentó angustia de muerte en Getsemaní. Llorar y sentir dolor por la ruptura de un vínculo que fue querido no es pecado ni falta de fe; es el tributo de amor que el corazón rinde a la pérdida.

No te juzgues por seguir sintiendo tristeza tras meses de proceso. Dios recoge cada una de tus lágrimas en su odre (Salmo 56, 8) y las transformará en bendición eterna.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Basta a cada día su propio afán.**",
                    bodyText = """
No intentes cargar hoy con el peso de los próximos tres años de tu vida. Nuestro Señor nos enseñó a pedir el 'pan de cada día', no el pan de toda la década por adelantado.

Pide la gracia para ser fiel, sobrio/a y orante el día de hoy. Deja que el mañana se preocupe de sí mismo. El Señor que te sostiene hoy, estará allí mañana para darte la fuerza que necesites.
                    """.trimIndent()
                )
            )

            ClinicalCategory.BUSQUEDA_REAFIRMACION -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El testimonio de una conciencia limpia es tu mayor baluarte.**",
                    bodyText = """
Buscar que alguien te diga si 'hiciste bien en poner ese límite' o si 'estuvo bien bloquear el contacto' es el síntoma de una tentación de escrúpulo que el enemigo susurra para hacerte flaquear en tu determinación.

Examina con serenidad delante del Sagrario: ¿lo hiciste movido/a por odio y venganza, o por la necesidad moral de salvaguardar tu alma, tu paz y tu dignidad de los engaños y el desorden? Si tu intención fue custodiar el templo de tu alma, camina con la frente en alto y no des oídos a la falsa culpa.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Huir de las ocasiones de pecado y desorden es mandato de prudencia.**",
                    bodyText = """
El Evangelio es categórico: *«Si tu ojo derecho te es ocasión de caer, sácatelo y arrójalo de ti.»* (Mateo 5, 29). Poner contacto cero y bloquear canales donde reinaba la tentación, el maltrato o la rumiación no es un acto de mezquindad; es la aplicación estricta de la prudencia evangélica de apartarse de la ocasión de caída.

No te dejes embaucar por falsas consideraciones de 'quedar bien con el mundo'. Tu primer deber moral es preservar el estado de gracia y la cordura de tu alma.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La firmeza en la decisión es signo de coherencia cristiana.**",
                    bodyText = """
Cuando tomaste la decisión de distanciarte, la viste con claridad meridiana bajo la luz de los hechos y la oración. Ahora que arrecia el viento de la soledad, el demonio quiere hacerte dudar de lo que viste con certeza en la luz.

San Ignacio advierte: *«En tiempo de desolación nunca hacer mudanza.»* No revoques tus determinaciones prudentes cuando estés vulnerable por el cansancio o la nostalgia. Permanece firme en la verdad que Dios te reveló.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Descansa en la absolución de tus escrúpulos.**",
                    bodyText = """
Hiciste lo correcto al cuidar tu vida. No tienes por qué disculparte por haber detenido una hemorragia que amenazaba con vaciar tu alma de alegría y de fe.

Da gracias a Dios por haberte concedido la valentía de decir 'basta', y no permitas que la nostalgia te haga añorar las cadenas de las que el Señor te ha liberado.
                    """.trimIndent()
                )
            )

            ClinicalCategory.OBJETOS_RECUERDOS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**No erijas ídolos materiales de un pasado que ya no existe.**",
                    bodyText = """
Conservar regalos, cartas o fotografías a la vista como reliquias sagradas puede convertirse en una forma encubierta de idolatría del recuerdo, donde el corazón rinde culto a lo que ya murió en lugar de abrirse a la vida de la gracia presente.

Examina con sobriedad cristiana qué lugar ocupan esos objetos en tu hogar. Si son ocasiones continuas de apego desordenado y tristeza estéril, apártalos con caridad y firmeza: tu santuario personal debe pertenecer al Dios vivo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Desapego evangélico de los despojos del camino.**",
                    bodyText = """
En el camino de seguimiento de Cristo, los discípulos debieron dejar sus redes y sus barcas para poder caminar libres. Aferrarse a las pertenencias materiales de una relación terminada es querer navegar arrastrando el ancla en el fondo del mar.

Si hay cosas que pertenecen a la otra persona, devuélvelas con cortesía y brevedad cristiana a través de un tercero si es prudente. Lo que sea tuyo, guárdalo en una caja fuera de tu vista o deshazte de ello con serenidad.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Purificación de la memoria y orden en el aposento.**",
                    bodyText = """
Santa Teresa de Jesús recomendaba rodearse de imágenes santas y de orden exterior para favorecer el recogimiento interior de la oración. Un entorno abarrotado de recuerdos del desamor distrae el alma de su unión con Dios.

Limpia tu espacio físico y digital. Haz una carpeta protegida o borra lo que te encadene; bendice tu habitación con agua bendita y coloca en su lugar una cruz o una imagen de Nuestra Señora. Que tu hogar respire la paz de Dios.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Agradece con nobleza y suelta la carga material.**",
                    bodyText = """
No necesitas destruir con odio lo que en su momento fue un detalle de cariño; el odio es tan esclavo del pasado como la nostalgia idólatra.

Puedes bendecir a Dios en silencio por lo bueno que hubo en su momento histórico y, a la vez, soltar esos objetos sin drama. La materia es polvo y al polvo volverá; lo que permanece es tu alma santificada para la eternidad.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ESTANCAMIENTO_PROCESO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La noche oscura del alma precede al resplandor del alba.**",
                    bodyText = """
San Juan de la Cruz describió con genialidad teológica la 'noche oscura' como aquel estado donde Dios despoja al alma de todos los consuelos sensibles para arraigarla en la fe pura y en la fidelidad desnuda.

Sentir que 'llevas meses y sigues igual' suele ser esa misma purificación pasiva: el Señor te está enseñando a caminar sin el apoyo de las emociones inmediatas, fiándote solo de Su palabra. No te desesperes en la aridez; es en el silencio de la meseta donde el corazón se ensancha para Dios.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El reino de los cielos es como el grano de mostaza que crece en lo oculto.**",
                    bodyText = """
Nuestra cultura contemporánea exige resultados visibles inmediatos, pero la pedagogía divina es lenta, callada y profunda como la fermentación de la levadura en la masa (Lucas 13, 21).

Aunque tus sentidos no perciban cambios deslumbrantes hoy, la constancia con la que te levantas, cumples con tus deberes de estado y perseveras en la oración está obrando una transformación invisible e indestructible en tu alma.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Cuidado con la acedia y la tibieza espiritual.**",
                    bodyText = """
Los Padres del Desierto advertían contra el 'demonio del mediodía' o la acedia: esa sensación de pesadez, fastidio y desgana que nos tienta a pensar que nada vale la pena y que estamos atascados sin remedio.

Combate esa tentación no con vanas cavilaciones, sino con la acción caritativa. Sal de ti mismo/a: visita a un enfermo, ayuda en una obra de caridad en tu parroquia, sirve a tu familia. El agua estancada se corrompe, pero el torrente que se dona a los demás recupera su pureza cristalina.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Confía en el divino Alfarero que no deja su obra a medias.**",
                    bodyText = """
Dice el profeta Jeremías que el alfarero toma la vasija de barro que se echó a perder en sus manos y la vuelve a moldear como mejor le parece. Tú estás en las manos del divino Alfarero.

No le digas al Señor cómo ni cuándo debe terminar de moldear tu corazón. Descansa dócil bajo su mirada amorosa; el día menos pensado mirarás atrás y te asombrarás de la obra de gracia que el Señor consumó en ti.
                    """.trimIndent()
                )
            )

            ClinicalCategory.DUDA_HABER_TERMINADO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El dolor del buen samaritano que debió cauterizar la llaga.**",
                    bodyText = """
Quien debe tomar la decisión de apartarse y poner fin a un vínculo desordenado o destructor experimenta una soledad particular: el peso moral del límite. Se puede sentir un dolor desolador en el corazón y, al mismo tiempo, tener la convicción moral de haber actuado conforme a la ley de Dios.

No te martirices creyendo que 'si te duele, es porque pecaste o te equivocaste'. El dolor es la consecuencia inevitable de romper un vínculo que importaba; la rectitud de la decisión se mide por su correspondencia con la verdad, no por la ausencia de aflicción.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No confundas la compasión con la complicidad en el desorden.**",
                    bodyText = """
A veces, tras terminar una relación, asalta el escrúpulo: '¿y si no tuve suficiente paciencia cristiana? ¿Y si le hice daño?'. Es crucial recordar que la paciencia cristiana busca la salvación y el bien, no es alcahuetería de la toxicidad, la mentira o el desprecio a tu dignidad como hijo/a de Dios.

Permanecer en un vínculo destructivo a menudo solo alimenta el pecado y la soberbia de la otra persona. Poner un fin definitivo fue el mayor acto de caridad hacia ti y hacia la verdad que podías realizar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Nadie que pone la mano en el arado y mira hacia atrás es apto para el Reino.**",
                    bodyText = """
Jesús nos amonesta con ternura y firmeza en Lucas 9, 62: no mires atrás como la mujer de Lot, que quedó convertida en estatua de sal por añorar la ciudad de la perdición.

Tomaste la determinación en conciencia recta, tras haber rezado y sufrido. No vuelvas ahora a negociar con la ruina solo porque el desierto es arduo. Pon tu mirada en Cristo y camina hacia adelante con santa perseverancia.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La paz de una elección tomada en la verdad.**",
                    bodyText = """
El precio de tu fidelidad a la verdad y a la propia dignidad fue la renuncia dolorosa a ese afecto. Es una herida noble, semejante a las cicatrices de batalla que honran a quien prefirió el honor y el mandato divino antes que la comodidad mundana.

Entrega al Señor a quien dejaste marchar; Él es capaz de velar por su alma infinitamente mejor que tú. Tú continúa construyendo tu vida sobre el cimiento inquebrantable del amor a Dios.
                    """.trimIndent()
                )
            )

            else -> ClinicalVariantsCatolico.getVariants(ClinicalCategory.RECONSTRUIR_GENERAL)
        }
    }
}
