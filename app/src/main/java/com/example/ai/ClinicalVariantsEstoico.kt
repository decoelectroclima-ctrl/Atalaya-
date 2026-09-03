package com.example.ai

/**
 * Variantes de acompañamiento clínico y filosófico para el marco ESTOICO.
 * Cada categoría clínica cuenta con 4 variantes redactadas con sobriedad, dignidad y templanza.
 */
object ClinicalVariantsEstoico {

    fun getVariants(category: ClinicalCategory): List<ClinicalVariant> {
        return when (category) {
            ClinicalCategory.RECUPERAR_PAREJA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Atalaya no fomenta estrategias de manipulación ni falsas esperanzas.**",
                    bodyText = """
El distanciamiento y el contacto cero no son ardides tácticos para forzar la voluntad ajena; son el bastión infranqueable donde proteges tu templanza y reconstruyes tu dignidad.

Intentar doblegar el destino o pretender 'hacer que regrese' es una afrenta directa a la dicotomía del control. Lo único sobre lo que tienes potestad innegociable es tu propia conducta y tu integridad en este instante.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El sabio no suplica ni elabora trampas de seducción.**",
                    bodyText = """
Querer reconquistar a quien ha decidido alejarse revela que has depositado tu sosiego en manos de otro. Como recordaba Epicteto, quien busca la aprobación de un tercero renuncia inmediatamente a su propia libertad.

Sostener el contacto cero para provocar celos o forzar una reacción es una forma velada de servidumbre. Tu tarea hoy no es recuperar a nadie, sino evitar perderte a ti mismo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Renuncia a las ilusiones: la realidad ya ha dictado su veredicto.**",
                    bodyText = """
Gastar energía en idear cómo hacer que te busquen solo alimenta una esperanza que te encadena al sufrimiento. La serenidad estoica comienza cuando aceptas los hechos con sobriedad radical.

El vínculo se ha disuelto o está fracturado; pretender revivirlo mediante maquinaciones exteriores es querer detener las olas del mar con las manos. Dirige tu voluntad hacia lo que sí puedes gobernar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La verdadera victoria no es que vuelvan, sino no mendigar.**",
                    bodyText = """
Ningún ser humano digno debe organizar su existencia para despertar el interés de quien se marchó. Cuando abandonas las estrategias de recuperación, recuperas al instante tu ciudadela interior.

El distanciamiento sincero es un pacto de respeto contigo mismo. No conviertas tu dolor en una comedia de manipulación; camina erguido hacia tu propia templanza.
                    """.trimIndent()
                )
            )

            ClinicalCategory.SENALES_DIGITALES -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Interpretar señales digitales es una trampa dopaminérgica.**",
                    bodyText = """
Una visualización furtiva, un estado o un 'me gusta' en una pantalla no constituyen rectitud, disculpa ni proyecto de vida. Son simples sombras en la caverna digital.

No te conviertas en un adivino de insignificancias. Cada minuto que inviertes inspeccionando sus redes o descifrando sus conexiones es un minuto que le arrebatas a tu propia paz y reconstrucción.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Custodia tu atención: lo que miras en una pantalla te domina.**",
                    bodyText = """
Marco Aurelio enseñaba que nuestra alma se tiñe del color de nuestros pensamientos más habituales. Si alimentas tu mente con el rastreo de perfiles ajenos, tú mismo abres las puertas a la inquietud.

Ver si está en línea o quién le sigue no cambia un ápice tu realidad presente. Corta de raíz esa vigilancia estéril; tu tiempo es tu posesión más sagrada.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Las sombras digitales carecen de sustancia y virtud.**",
                    bodyText = """
Buscar mensajes ocultos o indirectas en estados virtuales es una forma voluntaria de tormento. Pretendes leer la mente de quien no está a tu lado, ignorando la regla de oro estoica: juzgar solo los hechos tangibles.

El hecho real es la distancia; la pantalla solo proyecta espejismos diseñados para inquietar a los incautos. Apaga el dispositivo y vuelve a tu centro.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No concedas poder a una simple notificación.**",
                    bodyText = """
Que alguien mire tu historia o altere su perfil es un acontecimiento enteramente exterior a ti. Si permites que una interacción virtual te desestabilice, estás cediendo el gobierno de tus emociones a un algoritmo.

Practica la ataraxia: observa el impulso de investigar, reconócelo como una vanidad del ego, y regresa de inmediato al gobierno de tus actos.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RUMIACION_BUCLE -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Frenemos el bucle: Ya tienes suficiente información para comprender esto.**",
                    bodyText = """
Seguir buscando explicaciones a sus silencios o contradicciones es alimentar un fuego con cenizas. La mente cree que pensando sin tregua hallará alivio, pero solo perpetúa la confusión.

Distingamos los hechos de las conjeturas:
• **El Hecho:** La relación terminó y hoy reina la distancia.
• **La Conjetura:** Las mil historias que tu intelecto inventa para huir del dolor presente.
• **Tu Deber:** Cuidar tu cuerpo, tu honra y tu quietud en este día.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La mente desbocada es el peor de los tiranos.**",
                    bodyText = """
Séneca advertía que sufrimos mucho más a menudo por la imaginación que por la realidad. Los círculos concéntricos de preguntas sin respuesta no buscan la verdad, sino mantener viva la adicción al vínculo.

No necesitas entender cada motivo oculto para decidir vivir con dignidad hoy. Acepta que hay enigmas ajenos que nunca te corresponderá descifrar; enfócate en tu propia virtud.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Corta el hilo de los 'por qué' y céntrate en el 'qué hago ahora'.**",
                    bodyText = """
Preguntarse infinitamente por qué actuó como actuó es mirar hacia atrás mientras caminas al borde del abismo. No encontrarás paz examinando las razones de una conducta que no te perteneció.

Sustituye la rumiación por la acción sobria. Deja que el pasado repose donde debe estar: en lo irrevocable. Lo único vivo y disponible es tu capacidad de elegir rectamente aquí y ahora.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Acepta la incertidumbre como un hecho natural.**",
                    bodyText = """
Pretender certezas absolutas sobre las intenciones de otra persona es una empresa absurda. Los seres humanos son mudables y a menudo incoherentes; fundar tu calma en comprenderlos es edificar sobre arena.

Haz silencio voluntario. Cuando la mente empiece a encadenar sospechas y análisis, recuérdale: 'Esto no depende de mí, por tanto no perturba la fortaleza de mi carácter'.
                    """.trimIndent()
                )
            )

            ClinicalCategory.IMPULSO_CONTACTAR -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El impulso es solo una ola; tú eres la roca firme que permanece.**",
                    bodyText = """
La urgencia de escribir o llamar no es una llamada del destino, sino un espasmo de agitación en tu pecho. Las emociones intensas se asemejan a tempestades: si no les otorgas tu consentimiento, se extinguen solas.

Antes de mover un solo dedo, sopesa con frialdad:
1. ¿Qué pretendes obtener? Un fugaz apaciguamiento de la ansiedad a cambio de quebrantar tu propia palabra.
2. ¿Qué depende de ti? Tu templanza, tu honor y tu capacidad de esperar a que la marea baje.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Ninguna decisión apresurada ha honrado al sabio.**",
                    bodyText = """
Epicteto nos dejó una máxima lúcida: ante cualquier impresión vehemente, di: 'Espérame un poco; déjame ver quién eres y qué representas'. No actúes bajo el yugo de la desesperación.

Escribir en este estado no restablecerá la armonía; solo exhibirá debilidad y renovará el ciclo de frustración. Guarda silencio y reconquista tu dominio.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La templanza se demuestra precisamente cuando el deseo quema.**",
                    bodyText = """
Cualquiera puede mantener la serenidad cuando el mar está en calma. Es en este instante exacto, con las manos inquietas y el impulso a flor de piel, donde se forja tu temple interior.

Recuerda que cada vez que vences una urgencia sin claudicar, aumentas tu estatura moral. Respira despacio y permítele al arrebato disiparse sin concederle tus actos.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No rompas tu pacto por un momento de cobardía emocional.**",
                    bodyText = """
Has trazado una línea de protección con motivo. Buscar el contacto ahora no aliviará tu herida; solo la reabrirá para que vuelva a sangrar ante los ojos de quien decidió prescindir de ti.

Sostén la incomodidad somática. El dolor pasará; el arrepentimiento de haberte arrastrado permanece mucho más tiempo. Elige la dignidad.
                    """.trimIndent()
                )
            )

            ClinicalCategory.DEPENDENCIA_EMOCIONAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Diferenciemos el afecto legítimo de la servidumbre interior.**",
                    bodyText = """
Creer que 'no puedes vivir' sin otra persona es una distorsión del juicio. Naciste completo y dispones de cuanto hace falta para sostenerte a ti mismo con rectitud.

El afecto sincero desea el bien del otro desde la propia plenitud; la dependencia busca un amo o un bálsamo para anestesiar el vacío propio. Reclama tu soberanía: tu valor no cotiza en el afecto ajeno.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Nadie es dueño de tu felicidad salvo tu propia razón.**",
                    bodyText = """
Marco Aurelio anotaba que nada que proceda del exterior puede dañar la ciudadela del alma, a menos que uno mismo le otorgue ese permiso. Si sientes que tu vida se derrumba por una ausencia, estás confundiendo la parte con el todo.

Esa persona era un acompañante en tu trayecto, no la fuente de tu existencia. Despierta de esa fascinación desmedida y vuelve a ser el dueño de tu casa.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No pongas tu bienestar en la balanza de un tercero.**",
                    bodyText = """
Hacer depender tu estabilidad del afecto o la compañía de alguien es colocarte en la posición del esclavo voluntario. Séneca enseñaba que el hombre más poderoso es aquel que es dueño de sí mismo.

Agradece lo vivido, pero reconoce con severa claridad que tu deber primero es contigo: gobernarte, bastarte y no mendigar un sitio en una vida que ya no te incluye.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Tu completitud es un hecho de la naturaleza.**",
                    bodyText = """
La sensación de insuficiencia es una ilusión creada por el apego ciego. La naturaleza te ha dotado de razón, fortaleza y discernimiento para afrontar cualquier pérdida terrenal.

Quien se cree incompleto busca muletas; quien se reconoce íntegro aprende a caminar sin apoyos prestados. Abraza tu propia sustancia.
                    """.trimIndent()
                )
            )

            ClinicalCategory.NOSTALGIA_IDEALIZACION -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La nostalgia tiende a embellecer el pasado y borrar las heridas reales.**",
                    bodyText = """
La memoria desleal selecciona los instantes dorados y oculta con conveniencia el desgaste, el desdén o las discordias que hicieron insostenible el vínculo.

No confundas el dolor de la pérdida con una orden de restaurar lo roto. Extrañar es natural; pretender que aquello era un paraíso sin fisuras es faltar a la verdad de los hechos.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Mira el pasado con luz cenital, no con romanticismo ciego.**",
                    bodyText = """
Los recuerdos son como estatuas en la niebla: vistos de lejos parecen perfectos, pero de cerca revelan sus grietas insalvables. Si la relación terminó, fue porque carecía de las condiciones para prosperar en virtud.

Acepta lo que fue con gratitud sobria, pero no permitas que una ensoñación empañe tu juicio sobre el presente. Mira la realidad de frente.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Honra lo vivido sin convertirlo en una cárcel.**",
                    bodyText = """
Todo en el cosmos está sujeto al cambio y a la impermanencia. Aferrarse a lo que ya no es equivale a querer retener el agua del río entre los dedos cerrados.

El duelo tiene sus estaciones: habrá días templados y días desapacibles. No te asustes por sentir punzadas de tristeza; obsérvalas con serenidad y déjalas marcharse sin alterar tu rumbo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El ayer no contiene nada nuevo para tu presente.**",
                    bodyText = """
Revisar mentalmente las estampas del pasado como quien hojea un álbum marchito te impide advertir los deberes del día. La nostalgia es un veneno dulce que paraliza la voluntad.

Recuerda las razones por las que hubo de ponerse un límite. No traiciones tu propio discernimiento por un arranque de melancolía pasajera.
                    """.trimIndent()
                )
            )

            ClinicalCategory.CULPA_RENCOR_RABIA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Sostener el rencor o la culpa es seguir atado a lo que ya pasó.**",
                    bodyText = """
La culpa se alimenta de la ficción de que podías haberlo previsto y controlado todo. El rencor te convierte en cautivo de quien te infligió agravio.

Ni los autorreproches amargos ni el resentimiento tienen la potestad de corregir una sola línea de la historia. El verdadero cierre procede de tu determinación de no concederle a la ofensa poder sobre tu presente.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La ira es un veneno que uno bebe esperando que muera el otro.**",
                    bodyText = """
Séneca dedicó su tratado a mostrar la ceguera de la cólera: destruye la paz de quien la alberga mientras deja intacto al causante. El odio es un desperdicio infame de tu energía vital.

Si alguien actuó con mezquindad o deslealtad, se dañó a sí mismo privándose de rectitud moral. Tú, por tu parte, no te envanezcas ni te pudras en deseos de desquite; sigue tu camino limpio.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Libérate del fardo de las culpas estériles.**",
                    bodyText = """
Actuaste con la madurez y la luz de que disponías en aquel momento. Recriminarte hoy con el conocimiento adquirido a posteriori es un ejercicio injusto y desprovisto de sabiduría.

Extrae la lección ética de lo ocurrido y suelta el látigo. Castigarte mentalmente no repara nada; solo te debilita para afrontar los compromisos de tu vida actual.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El perdón no disculpa al otro: rescata tu sosiego.**",
                    bodyText = """
Perdonar en sentido estoico significa comprender que los extravíos ajenos forman parte del orden del mundo. Marco Aurelio recordaba cada mañana: 'Hoy me encontraré con el ingrato, el violento, el desleal; obran así por ignorancia del bien'.

No aguardes a que reconozcan su falta para recuperar tu sosiego. Corta el lazo del agravio y decreta tu propia absolución.
                    """.trimIndent()
                )
            )

            ClinicalCategory.CONTACTO_CERO_LIMITES -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El límite firme es el espacio aséptico donde tu herida puede sanar.**",
                    bodyText = """
El contacto cero no es un castigo destinado a hacer sufrir al otro; es la empalizada necesaria que eriges para proteger tu salud anímica y tu dignidad intacta.

Quien transige con concesiones tibias prolonga la agonía. Mantén la muralla alta y no permitas que la curiosidad o la conmiseración falsa vulneren tu fortaleza.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La frontera entre la paz y el caos depende de tu firmeza.**",
                    bodyText = """
Marco Aurelio insistía en la necesidad de podar lo superfluo tanto en los actos como en los pensamientos. Bloquear, silenciar o distanciarte no es inmadurez: es lucidez higiénica.

Si dejas hendiduras por donde se cuele información o contacto, la mente hallará pretextos para recaer. Sé categórico: tu reconstrucción exige una separación total y sin ambages.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No negocies con aquello que debilita tu virtud.**",
                    bodyText = """
Tolerar mensajes ambiguos, encuentros fortuitos o diálogos innecesarios es jugar con fuego cerca de la pólvora. Si has decidido soltar, sé consecuente con tu resolución.

El límite no requiere estridencias ni reproches: basta un silencio inquebrantable sostenido por la certeza de que tu tranquilidad vale más que cualquier cortesía vacía.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La distancia es el antídoto contra la confusión.**",
                    bodyText = """
Mientras sigas recibiendo noticias o interactuando, el juicio se mantendrá turbio. El contacto cero despeja la niebla y permite ver la realidad en su desnuda verdad.

Defiende tu perímetro con la serenidad del centinela que conoce su deber. Tu paz interior no se encuentra sujeta a discusión ni a prórrogas.
                    """.trimIndent()
                )
            )

            ClinicalCategory.AUTOESTIMA_RECHAZO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El desinterés ajeno no tiene la capacidad de tasarte.**",
                    bodyText = """
Tu valor intrínseco no fluctúa según el dictamen o la incapacidad afectiva de una persona. Eres un ser dotado de razón y sustancia moral por derecho propio.

La herida del rechazo confunde 'no haber sido elegido' con 'carecer de mérito'. Epicteto lo expresó con claridad: si alguien no te aprecia, es su propio entendimiento el que queda en falta, no tu ser.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Nadie puede quitarte lo que tú mismo no entregues.**",
                    bodyText = """
Si te sientes disminuido o humillado, examina tus propios juicios. No es el abandono lo que te abate, sino la opinión que has formado sobre ese abandono.

Que una relación haya concluido no te convierte en un fracaso viviente. La dignidad reside en cómo te conduces ante la adversidad, no en acumular afectos prestados.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La aprobación ajena es una moneda sin valor en tu ciudadela.**",
                    bodyText = """
Buscar el refrendo exterior para considerarte digno de respeto es la raíz de toda tribulación. Quien se conoce a sí mismo no se envanece con la lisonja ni se desmorona ante el desdén.

Levántate y limpia tus vestiduras. Hay trabajo noble que hacer en tu vida; no permanezcas postrado aguardando que quien no supo valorarte te dé permiso para respirar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Tu valor moral es independiente de las circunstancias externas.**",
                    bodyText = """
El oro no pierde su nobleza porque alguien ignore su pureza y lo confunda con hierro. Así tu alma conserva intacta su capacidad de valentía, generosidad y sabiduría.

Deja de evaluar tu valía a través de los ojos de quien decidió prescindir de ti. Mírate a través de tus deberes cumplidos y tu firmeza en la prueba.
                    """.trimIndent()
                )
            )

            ClinicalCategory.SOLEDAD_VACIO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La soledad no es un abismo de castigo, sino el espacio para reencontrarte.**",
                    bodyText = """
El silencio que queda tras una ruptura acongoja porque desvela cuánto te habías descuidado para satisfacer al otro. Ese vacío no es una fosa: es un solar despejado para edificar de nuevo.

Estar a solas contigo no es indigencia; es reconquistar el retiro sagrado donde tú vuelves a ser el artífice de tus días. Aprende a disfrutar de tu propia compañía.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El sabio nunca se encuentra enteramente desamparado.**",
                    bodyText = """
Séneca decía que la soledad resulta insoportable solo para quien no puede convivir pacíficamente consigo mismo. Si tu propia mente está en orden, cualquier aposento vacío se transforma en santuario.

No te precipites a llenar el silencio con distracciones vanas o compañías triviales. Atraviesa este desierto con serenidad; en él hallarás las raíces de tu temple.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Habita tu silencio con sobria nobleza.**",
                    bodyText = """
El miedo a estar solo es el causante de casi todas las concesiones serviles en el amor. Cuando aprendes a sostenerte en tu propia gravedad, quedas blindado para siempre contra el sometimiento.

Aprovecha este recogimiento forzoso para ordenar tus prioridades, leer, templar tu cuerpo y cultivar las virtudes que las prisas del vínculo te hicieron postergar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La soledad voluntaria es escuela de libertad.**",
                    bodyText = """
Quien teme estar a solas acaba pagando cualquier precio por un destello de compañía, por tóxica que sea. Celebrar tu autonomía te confiere un poder inexpugnable.

Este vacío es solo la ausencia de lo accesorio; lo esencial —tu razón, tu conciencia y tu respiración— sigue contigo sin mengua alguna.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ANSIEDAD_SOMATICA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El cuerpo se alarma, pero el alma permanece en su trono.**",
                    bodyText = """
La taquicardia y la opresión en el pecho son reacciones animales de la fisiología ante una pérdida percibida como peligro. No confundas el temblor de la carne con la destrucción de tu ser.

Respira hondo y despacio: alarga la exhalación el doble que la inhalación. Dile a tu juicio: 'Esto es una conmoción somática pasajera; no encierra ningún mal moral ni peligro real'.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Domina el pánico mediante la quietud voluntaria.**",
                    bodyText = """
Marco Aurelio nos recordaba que el alma racional no se perturba a sí misma a menos que asienta a las impresiones tumultuosas. La angustia que sientes en la garganta es un eco fisiológico; no lo amplifiques con pensamientos catastróficos.

Planta tus pies firmes en el suelo. Observa cómo late el corazón sin intentar forzarlo; permite que la tormenta descargue su fuerza en el cuerpo mientras tu mente custodia el timón.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Ninguna sensación corporal dura eternamente.**",
                    bodyText = """
El dolor somático es como un visitante ruidoso: entra con violencia, pero si no le ofreces asiento ni conversación, termina marchándose por donde vino.

No luches frenéticamente contra la ansiedad: resistirse incrementa la tensión. Afloja los hombros, relaja la mandíbula y confía en la capacidad natural de tu organismo para recobrar el equilibrio.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Firmeza ante la turbulencia somática.**",
                    bodyText = """
Cuando sientas que te falta el aire o que la desesperación aprieta, vuelve a las sensaciones inmediatas: el tacto de la ropa, el suelo bajo tus plantas, el peso de tus brazos.

Tu mente quiere volar hacia el pasado o el porvenir desolador; tráela con mano firme al segundo presente. Aquí, ahora mismo, nada te está destruyendo.
                    """.trimIndent()
                )
            )

            ClinicalCategory.INSOMNIO_NOCHE -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La noche agiganta los temores; no juzgues tu vida a oscuras.**",
                    bodyText = """
De madrugada, con la fatiga acumulada y los sentidos desarmados, los fantasmas del apego cobran proporciones gigantescas. Lo que a las tres de la mañana parece una ruina insalvable, a la luz del sol se reduce a su medida justa.

Si no puedes conciliar el sueño, no permanezcas revolviéndote en la cama batallando con los recuerdos. Levántate, bebe un sorbo de agua y lee unas páginas sobrias hasta que el letargo regrese.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No concedas crédito a las cavilaciones del desvelo.**",
                    bodyText = """
Séneca recomendaba no entablar debates filosóficos cruciales en estados de agotamiento. En la penumbra la mente carece de filtro crítico y tiende a inventar los peores desenlaces.

Reconoce la vigilia sin irritación: 'Hoy mi cuerpo reposa menos, pero mi espíritu no se dejará amedrentar por sombras'. Cierra los ojos y atiende únicamente a la cadencia de tu aliento.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Las pesadillas son solo despojos de la memoria en reposo.**",
                    bodyText = """
Soñar con quien se marchó o despertar con sobresalto es el cerebro procesando la abstinencia y el desapego. No interpretes esos sueños como señales cósmicas ni premoniciones.

Eran imágenes desordenadas, nada más. Has despertado, la habitación está en orden y tu libertad sigue intacta. Descansa sin alimentar el drama.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Protege tu descanso como un deber sagrado.**",
                    bodyText = """
La noche está hecha para la tregua del guerrero, no para reiniciar batallas perdidas en la imaginación. Suspende todo análisis hasta que amanezca.

Entrega el día que se fue con todas sus asperezas. Mañana requerirás entereza; no malgastes tus horas de sueño en juicios estériles.
                    """.trimIndent()
                )
            )

            ClinicalCategory.FECHAS_SIGNIFICATIVAS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El calendario es una convención de los hombres, no una condena.**",
                    bodyText = """
Que hoy sea un aniversario, un cumpleaños o una festividad solo le concede el peso que tu juicio decida otorgarle. El día transcurre con las mismas veinticuatro horas que cualquier otro.

La mente tiende a anticipar agonías en estas fechas. Prepárate con templanza de antemano: diseña tu jornada con ocupaciones útiles y mantente firme en la ataraxia.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Amor fati: acepta el paso de los ciclos con entereza.**",
                    bodyText = """
Los tiempos pasados cumplieron su propósito y concluyeron conforme a la naturaleza de las cosas. Querer que hoy sea como aquel día de hace un año es rebelarse contra el orden del universo.

Atraviesa este hito con serenidad sobria. No envíes mensajes de cortesía que solo encubren el deseo de reanudar el lazo. Deja que la fecha pase sin estridencias.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Distingue el recuerdo de la obligación de claudicar.**",
                    bodyText = """
Es inevitable que la fecha despierte remembranzas; somos seres dotados de memoria. La diferencia estriba en permitir que el recuerdo se convierta en excusa para vulnerar tus principios.

Siente el peso del día si es menester, pero compórtate con la rectitud de quien ha jurado cuidar de sí mismo. La fecha se extinguirá a medianoche; tu palabra permanecerá.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Construye nuevos significados sobre las antiguas efemérides.**",
                    bodyText = """
El significado de este día no está grabado en piedra por obra de otra persona. Puedes consagrarlo hoy al ejercicio del autodominio, al cultivo de tu sabiduría o al servicio de los tuyos.

Transforma la conmemoración dolorosa en el aniversario de tu propia madurez. Lo que antes fue atadura, hoy puede ser testimonio de tu temple.
                    """.trimIndent()
                )
            )

            ClinicalCategory.NUEVA_PAREJA_EX -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La vida de los otros es materia ajena a tu gobierno.**",
                    bodyText = """
Que la persona con quien compartiste ayer esté hoy con otra no altera tu naturaleza ni disminuye tu dignidad en un solo gramo. Quien se marchó es libre de extraviarse o recomponerse como juzgue oportuno.

Compararte con su nueva circunstancia es una necedad que solo engendra amargura. Retira tu mirada del espectáculo exterior y cuida de tu propio huerto.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No envidies espejismos que no conoces.**",
                    bodyText = """
Nadie sabe qué desdichas, vacíos o discordias se ocultan tras una nueva unión apresurada. Juzgar la felicidad ajena por lo que se ve o se rumorea es una ingenuidad impropia de un espíritu sensato.

Tu tarea no es vigilar quién reemplaza a quién en el teatro del mundo. Tu único encargo verdadero es asegurarte de que tú no te traicionas a ti mismo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El valor de tu historia pasada no se anula por un nuevo presente.**",
                    bodyText = """
El hecho de que continúe su camino no invalida las virtudes que pusiste en el vínculo ni mancha tu honra. Los desenlaces de la vida humana son mudables; aferrarse a ser el último capítulo de alguien es vanidad.

Suelta toda pretensión de exclusividad retrospectiva. El lazo concluyó; lo que haga a partir de ese punto pertenece a su cuenta, no a la tuya.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Firmeza ante la herida de la vanidad herida.**",
                    bodyText = """
Mucho de lo que duele al saber que está con alguien no es amor genuino, sino el aguijón del ego que se resiste a ser olvidado. Sé sincero contigo y reconoce ese ardor por lo que es.

El sabio no necesita ser recordado con nostalgia por nadie para saber quién es. Deséale en silencio que encuentre la sabiduría que a ti te corresponde cultivar, y sigue adelante.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ENCUENTRO_CASUAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Serenidad ante el encuentro imprevisto: nada te ha tocado.**",
                    bodyText = """
Cruzarse en la vía pública o en un salón compartido pone a prueba el temple en un segundo. Si se produce el cruce, recuerda a Epicteto: 'Un cuerpo se encontró con otro cuerpo; las almas permanecen inviolables'.

Mantén la mirada serena, un saludo sobrio si la distancia lo exige, y prosigue tu camino sin apresurar el paso ni exhibir turbación. No concedas a un encuentro fortuito el poder de desordenar tu semana.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La cortesía fría es el mejor escudo del sabio.**",
                    bodyText = """
No necesitas huir como un fugitivo ni entablar conversaciones cargadas de reproches o nostalgia. Si el contacto visual es inevitable, asiente con educación distante y sigue con tus obligaciones.

La retirada no es cobardía cuando busca proteger la paz lograda. No te detengas a indagar cómo le va ni a buscar señales en sus gestos: pasa de largo con paso firme.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Reconquista tu calma tras el sobresalto.**",
                    bodyText = """
Es normal que la adrenalina suba tras ver de pronto a quien ocupó tanto espacio en tu vida. Da unos pasos lentos, inhala aire fresco y recuerda quién eres hoy.

Ese encuentro duró apenas unos segundos; no permitas que la mente lo dilate durante horas mediante recreaciones obsesivas. El suceso ya concluyó.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El espacio público no le pertenece a nadie.**",
                    bodyText = """
No evites calles, plazas o comercios por temor a coincidir; eso sería concederle el dominio sobre tu geografía cotidiana. Camina con la tranquilidad del ciudadano libre.

Si coincides, demuestra con tu compostura que eres una persona asentada en su propio eje. La dignidad se manifiesta en la naturalidad del proceder.
                    """.trimIndent()
                )
            )

            ClinicalCategory.COPARENTALIDAD_LOGISTICA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Separa el deber práctico de las afecciones sentimentales.**",
                    bodyText = """
Cuando median hijos, finanzas o asuntos legales, la distancia total resulta impracticable. En tales circunstancias rige la doctrina del deber: exactitud, frialdad cortés y laconismo espartano.

Trata todo intercambio como lo harías con un asociado mercantil con quien no guardas intimidad. Ni un reproche, ni una confidencia, ni una queja: solo los hechos necesarios.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El laconismo es tu muralla en la comunicación obligada.**",
                    bodyText = """
Responde únicamente a lo que atañe a la logística, fechas, horas y necesidades objetivas. Si introduce provocaciones, reproches o anzuelos nostálgicos, déjalos caer en el vacío del silencio.

No respondas a la emoción con emoción. Mantén la sobriedad documental; la disciplina en las palabras desactiva cualquier tentativa de conflicto estéril.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Protege a los más débiles mediante tu propia templanza.**",
                    bodyText = """
Si hay hijos en común, tu sosiego es su único refugio verdadero. Emplear a los pequeños como mensajeros o desahogar en ellos el despecho es una falta moral grave contra quienes dependen de ti.

Sé el adulto virtuoso que la situación demanda. Que vean en ti el ejemplo de quien sabe cumplir su deber sin amargura y sin claudicar en sus principios.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Las obligaciones se gestionan con método, no con pasión.**",
                    bodyText = """
Redacta acuerdos breves y transparentes sobre custodias, enseres o trámites pendientes. Cuanto más nítidas sean las reglas, menor será el espacio para disputas emocionales.

Cumple tu parte con puntualidad intachable y exige lo mismo con firmeza serena. La rectitud en los actos desarma cualquier hostilidad exterior.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ETIQUETAS_DIAGNOSTICAS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Atalaya examina los hechos observables, no las etiquetas clínicas.**",
                    bodyText = """
Categorizar a la otra persona como 'narcisista', 'psicópata' o 'tóxica' suele ser una estratagema del ego para sentirse moralmente superior o para prolongar la obsesión analítica bajo ropaje técnico.

Los estoicos no juzgan almas ajenas; examinan únicamente la conducta observada y sus propios límites. Si su conducta fue lesiva o destructiva, el hecho basta para sostener la distancia, sin requerir un veredicto psiquiátrico.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Deja de jugar al juez de personalidades ajenas.**",
                    bodyText = """
Pasar semanas leyendo tratados sobre trastornos de la personalidad ajena te mantiene hipnotizado por la misma figura que pretendes soltar. Cambias el amor dependiente por el estudio obsesivo del agresor.

Marco Aurelio anotaba: 'No pierdas lo que te queda de vida en cavilar sobre lo que hacen otros'. Vuelve la mirada hacia ti: ¿qué debilidad propia te llevó a tolerar lo intolerable?
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La única diagnosis urgente es la de tu propia templanza.**",
                    bodyText = """
Poco importa qué patología explique la conducta de quien te lastimó; lo relevante para tu salvaguarda es que su comportamiento vulneró tu dignidad y tu integridad.

Centra tu estudio en tus propios actos. Desarrolla la firmeza necesaria para decir 'no' sin vacilar y para no volver a franquear la puerta a quien carece de virtud recíproca.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Reemplaza las etiquetas rimbombantes por límites sobrios.**",
                    bodyText = """
Las etiquetas teóricas suelen alimentar el resentimiento y el victimismo estéril. El sabio reconoce el daño recibido, asume su cuota de aprendizaje y corta la relación sin necesidad de colgar estigmas.

La conducta fue dañina; el vínculo es inviable; tu retirada es definitiva. Todo lo demás es literatura superflua que perturba tu serenidad.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RECONSTRUIR_GENERAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Acompaño tu proceso con rigor, presencia y sobriedad.**",
                    bodyText = """
En la senda del autodominio estoico hay una verdad que debe alumbrar tus pasos:
*«Puedes seguir guardando afecto a alguien en la memoria y, a la vez, no concederle un solo milímetro de mando sobre tu vida presente.»*

Hoy estás dando un paso definitivo hacia tu soberanía afectiva. Cada vez que prefieres la quietud a la desesperación, estás forjando una personalidad libre y templada.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El carácter se forja en el crisol de la renuncia lúcida.**",
                    bodyText = """
Séneca decía que no hay hombre más desdichado que aquel a quien la adversidad nunca puso a prueba; se le privó de conocer su propia fuerza. Esta ruptura es el terreno donde demuestras de qué estás hecho.

No te compadezcas ni te consideres derrotado. Tienes frente a ti la oportunidad inmensa de refundar tu existencia conforme a principios nobles y duraderos.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Edifica tu vida sobre cimientos inexpugnables.**",
                    bodyText = """
Cuando un vínculo se extingue, queda al descubierto la verdad de nuestra estructura interior. Si dependías de su calor para caminar, es tiempo de aprender a generar tu propio fuego.

Retoma tus deberes, cuida de tu cuerpo con austeridad noble y busca la excelencia en tus labores cotidianas. El propósito se conquista en los actos de cada día.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Avanza con paso firme y la cabeza erguida.**",
                    bodyText = """
Epicteto advertía que no debemos desear que las cosas sucedan como nosotros queremos, sino querer que sucedan tal como suceden: solo así seremos felices.

El pasado cumplió su cometido; el presente reclama tu atención total. Abraza tu destino con magnanimidad y continúa tu camino sin mirar atrás.
                    """.trimIndent()
                )
            )

            else -> ClinicalNewCategoriesEstoico.getVariants(category)
        }
    }
}
