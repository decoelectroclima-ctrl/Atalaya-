package com.example.ai

/**
 * Variantes de acompañamiento clínico y espiritual para el marco CATÓLICO.
 * Cada categoría clínica cuenta con 4 variantes redactadas con serenidad, esperanza cristiana y dignidad.
 */
object ClinicalVariantsCatolico {

    fun getVariants(category: ClinicalCategory): List<ClinicalVariant> {
        return when (category) {
            ClinicalCategory.RECUPERAR_PAREJA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Atalaya no fomenta estrategias de manipulación ni falsas esperanzas.**",
                    bodyText = """
El distanciamiento y el contacto cero no son tretas de seducción para torcer la voluntad ajena; son la custodia santa que pones a tu corazón herido para recuperar la paz interior y la dignidad de hijo de Dios.

Alimentar la obsesión de 'hacer que regrese' nubla tu discernimiento espiritual y te aleja de la confianza en la Divina Providencia. Si un lazo se ha roto, lo único fecundo hoy es abandonarte en las manos de Dios y buscar su gracia.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No mendigues el afecto humano: tu alma descansa en el amor de Dios.**",
                    bodyText = """
Querer forzar una reconciliación mediante ardides o celos contradice la pureza de intención que el Evangelio nos pide. Quien mendiga el cariño de quien ha decidido partir suele olvidar la infinita dignidad con que ha sido creado.

Pon tus anhelos a los pies de la cruz. Deja de urdir planes para alterar el curso de los acontecimientos; aprende a rezar con humildad: 'Señor, no se haga mi voluntad, sino la tuya'.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Acepta la realidad con humildad y docilidad de espíritu.**",
                    bodyText = """
Dios a menudo permite el fin de una relación para librarnos de sufrimientos mayores o para apartarnos de caminos que marchitaban nuestra vocación y nuestra santidad.

Gastar tus energías en inventar estrategias de reconquista es resistirte a la prueba que purifica tu fe. Abraza el presente con serenidad y permite que el Señor sane tus heridas más hondas.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La verdadera paz procede del abandono confiado, no del control.**",
                    bodyText = """
Pretender manipular la libertad del prójimo para calmar nuestro dolor es una ilusión que solo engendra amargura. Dios respeta la libertad de cada alma y nos invita a custodiar la nuestra.

El distanciamiento sincero es un acto de reverencia hacia tu propia vida. Entrégale tu tristeza al Buen Pastor; Él sabe cómo rehacer lo quebrado a su debido tiempo.
                    """.trimIndent()
                )
            )

            ClinicalCategory.SENALES_DIGITALES -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Interpretar señales digitales es una trampa dopaminérgica.**",
                    bodyText = """
Una visualización furtiva en una red social, un estado o un 'me gusta' no constituyen amor verdadero, rectitud ni un propósito sagrado de vida. Son distracciones efímeras que turban el sosiego del alma.

No te conviertas en esclavo de las pantallas buscando señales vacías. Como exhorta la Escritura: 'Por encima de todo, guarda tu corazón, porque de él mana la vida'. Protege tu mirada y sostén el silencio digital.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Custodia tus ojos y tu imaginación ante las vanidades del mundo.**",
                    bodyText = """
San Agustín advertía sobre el peligro de la inquietud de los ojos y la curiosidad vana, que dispersan la mente y roban la oración. Rastrear conexiones o perfiles de redes no te dará consuelo; solo acrecienta la desazón.

Cada minuto consagrado a espiar lo que hace la otra persona es un minuto que le quitas al diálogo íntimo con Dios y al cuidado de tu familia y tus deberes. Apaga el teléfono y busca la presencia del Señor en el silencio.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El amor cristiano se manifiesta en la verdad, no en sombras virtuales.**",
                    bodyText = """
Si alguien desea de corazón pedir perdón o reconstruir un vínculo con madurez, no utiliza indirectas algorítmicas ni gestos ambiguos en redes. Las cosas de Dios se mueven en la luz, en la verdad y cara a cara.

No te engañes alimentando esperanzas sobre un simple 'visto' o una historia efímera. Ofrece esa inquietud al Sagrario y mantén tu atención en las tareas del día.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No permitas que una notificación robe la paz que Dios te regala.**",
                    bodyText = """
La paz de Cristo es un don inmenso; no la entregues a merced de lo que una pantalla muestre o deje de mostrar. La hipervigilancia digital es un desierto estéril que seca la vida interior.

Decide hacer un ayuno voluntario de redes sociales. Verás cómo, al despejarse el ruido del mundo, vuelves a percibir el murmullo de la gracia en tu día a día.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RUMIACION_BUCLE -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Frenemos el bucle: Ya tienes suficiente información para comprender esto.**",
                    bodyText = """
Seguir intentando descifrar las intenciones ocultas, silencios o contradicciones de la otra persona solo mantiene encendido el circuito de la rumiación y la inquietud interior.

Distingamos los hechos de las suposiciones:
• **El Hecho:** La relación concluyó y la distancia es la realidad presente.
• **La Suposición:** Las mil dudas con las que tu mente intenta comprender lo incomprensible.
• **Tu Deber:** Cuidar tu templo, orar y cumplir con rectitud tus compromisos cotidianos.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Descansa en la certeza de que Dios conoce lo que tú ignoras.**",
                    bodyText = """
San Pablo nos recuerda: 'Sabemos que a los que aman a Dios todas las cosas les ayudan a bien'. No necesitas desentrañar cada motivo secreto ni entender por qué la otra persona obró con ingratitud o incoherencia.

Pretender saberlo todo es una forma de soberbia intelectual que te agota. Di con sencillez: 'Señor, en tus manos encomiendo este misterio que hoy no comprendo', y vuelve al sosiego.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Sustituye la rumiación estéril por la oración confiada.**",
                    bodyText = """
Darle vueltas sin fin a las mismas preguntas del pasado es rezarle al dolor en vez de rezarle a Dios. Cada pensamiento obsesivo que se repite es una invitación a la entrega, no a la disección infinita.

Cada vez que aparezca el torbellino de los 'por qués', conviértelo en una jaculatoria: 'Jesús, en ti confío'. Corta el bucle con la fuerza de la gracia y reanuda tus labores.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La verdad ya te ha sido revelada a través de los frutos.**",
                    bodyText = """
'Por sus frutos los conoceréis', dice el Evangelio. Los frutos del vínculo en su tramo final fueron el dolor, la inestabilidad o la ruptura: no requieres más confirmación teórica para aceptar que la etapa terminó.

Deja de buscar excusas en la memoria. Acepta con paz la realidad de las cosas y pide a Dios fortaleza para sembrar semillas de santidad en tu presente.
                    """.trimIndent()
                )
            )

            ClinicalCategory.IMPULSO_CONTACTAR -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El impulso es solo una ola neuroquímica; no es una orden que debas obedecer.**",
                    bodyText = """
Lo que sientes arder en el pecho no es una llamada providencial a quebrar tu pacto de distancia, sino la angustia pasajera de la carne ante la pérdida del afecto que antes la calmaba.

Antes de actuar precipitadamente, reflexiona en presencia de Dios:
1. **¿Qué buscas realmente?** Un alivio momentáneo de diez minutos a costa de reiniciar semanas de sanación del alma.
2. **¿Qué no depende de ti?** El corazón o las respuestas de la otra persona.
3. **¿Qué sí te corresponde?** Tu fidelidad a ti mismo, tu templanza y tu pureza de juicio ante Dios.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La virtud de la templanza se conquista en este instante exacto.**",
                    bodyText = """
Santo Tomás de Aquino definía la templanza como la virtud que refrena los impulsos desordenados para mantener el alma en la recta razón. Cuando la urgencia de escribir te quema por dentro, estás ante la oportunidad de crecer espiritualmente.

No cedas al dictado de la debilidad. Respira, arrodíllate un minuto si te ayuda a serenar el espíritu, y pídele al Espíritu Santo el don de fortaleza y dominio de ti mismo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No desandes en un minuto de desespero el camino ganado con Dios.**",
                    bodyText = """
Has levantado un vallado para preservar tu integridad y curar tus llagas. Enviar ese mensaje apresurado no traerá el amor que esperas; solo dejará tu corazón expuesto a nueva frialdad y desasosiego.

Ofrece ese dolor ardiente en la cruz por tu propia madurez. Los grandes hombres y mujeres de fe se forjaron resistiendo pacientemente las horas oscuras de la prueba.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Espera a que el agua turbia se aclare antes de dar un paso.**",
                    bodyText = """
San Ignacio de Loyola enseñaba una regla de oro en el discernimiento de espíritus: 'En tiempo de desolación nunca hacer mudanza'. Si estás turbado, triste o ansioso, no tomes decisiones ni envíes mensajes.

Permanece quieto y fiel a tus resoluciones de calma. Espera a que la gracia y la serenidad retornen a tu alma; solo desde la paz de Dios se puede ver con claridad.
                    """.trimIndent()
                )
            )

            ClinicalCategory.DEPENDENCIA_EMOCIONAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Diferenciemos el amor generoso de la idolatría afectiva.**",
                    bodyText = """
Sentir que 'no puedes vivir' sin esa persona es una señal clara de que habías puesto en una criatura humana lo que solo le corresponde llenar al Creador.

El primer mandamiento nos recuerda ordenar los afectos: amar a Dios sobre todas las cosas y al prójimo desde esa plenitud. Ningún ser humano puede ser tu salvador ni tu único refugio existencial.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Eres hijo amado de Dios; tu dignidad es inmensa y no se mendiga.**",
                    bodyText = """
Has sido rescatado a un precio altísimo y dotado de una vocación eterna. Creerte insignificante o desecho porque una persona no correspondió a tu amor es ignorar el valor inestimable que tienes a los ojos de Dios.

Levanta la mirada. Tu vida no ha perdido su horizonte sagrado; este desierto puede ser el lugar bendito donde descubras que en Dios nada te falta.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Purifica tus afectos en el fuego de la verdad.**",
                    bodyText = """
San Juan de la Cruz enseñaba que el alma debe desasirse de las ataduras desordenadas para poder gozar de la verdadera libertad espiritual. La dependencia no es amor; es apego temeroso que busca saciar la sed en aljibes agrietados.

Acepta que el agua viva mana de otra fuente. Cuando colocas a Dios en el centro, tus afectos humanos encuentran por fin su medida justa, sana y liberadora.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No entregues las llaves de tu alegría a quien no puede custodiarla.**",
                    bodyText = """
Hacer depender tu sosiego de la mirada de otro ser mortal es edificar tu casa sobre arena movediza. Las personas fallan, cambian o se marchan; solo Dios permanece inmutable.

Afianza tus cimientos en la Roca que no vacila. Desde esa seguridad inquebrantable podrás volver a amar con libertad, sin miedo a perderte a ti mismo.
                    """.trimIndent()
                )
            )

            ClinicalCategory.NOSTALGIA_IDEALIZACION -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La nostalgia tiende a embellecer el pasado y borrar las heridas reales.**",
                    bodyText = """
La memoria humana suele conservar los momentos dulces y pasar por alto el sufrimiento, la frialdad o la pérdida de paz que caracterizaron la relación.

Extrañar no significa que el camino fuera recto ni que debas regresar atrás. El pueblo de Israel en el desierto añoraba las cebollas de Egipto olvidando la esclavitud que padecía. No vuelvas la mirada a lo que Dios te ha ayudado a superar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Agradece lo bueno vivido y bendice lo que concluyó.**",
                    bodyText = """
Dar gracias por las bendiciones que compartisteis es un acto noble de caridad cristiana; pretender retenerlas cuando su tiempo pasó es negarte a crecer. Hay un tiempo para abrazar y un tiempo para despedir, como enseña el Eclesiastés.

Guarda en el corazón los aprendizajes con gratitud limpia, pero no te quedes mirando la tumba vacía de una historia que ya no tiene vida. Camina hacia la aurora.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El dolor del desprendimiento es la prueba de tu capacidad de amar.**",
                    bodyText = """
Sentir tristeza o melancolía es totalmente natural cuando se ha entregado el corazón. Jesucristo mismo lloró ante la tumba de su amigo Lázaro; la pena no es pecado ni falta de fe.

Permite que las lágrimas corran sin amargura. Ofrece ese desgarro por tu propia santificación, sabiendo que 'los que sembraron con lágrimas cosecharán entre cantares'.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No te quedes varado como la estatua de sal.**",
                    bodyText = """
El pasaje del Génesis nos advierte del peligro de mirar atrás con apego desesperado cuando el Señor nos conduce hacia un lugar seguro. La nostalgia desmedida paraliza el alma e impide recibir las nuevas gracias que Dios tiene preparadas.

Fija tu vista en el horizonte que tienes por delante. Confía en que el plan del Señor para tu vida es de esperanza y de paz, no de perpetua aflicción.
                    """.trimIndent()
                )
            )

            ClinicalCategory.CULPA_RENCOR_RABIA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Sostener el rencor o la culpa es seguir atado/a a lo que ya pasó.**",
                    bodyText = """
La culpa te encadena a la ilusión de que podías haberlo previsto todo, y el rencor te hace cautivo de quien te ofendió. Ni los autorreproches amargos ni el resentimiento tienen el poder de cambiar una sola línea de lo vivido.

El perdón cristiano no es debilidad ni complicidad con la injusticia: es desatar las ataduras del odio para que el corazón respire la gracia de Dios. Entrega el juicio al único que es Justo y Misericordioso.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El perdón libera primero al que perdona.**",
                    bodyText = """
Guardar rencor en el pecho es como llevar una brasa encendida en las manos esperando lanzársela al otro: el primero en quemarse eres tú. En el Padrenuestro rezamos: 'Perdona nuestras ofensas como nosotros perdonamos'.

No necesitas sentir afecto ni volver a relacionarte con esa persona para perdonarla en el corazón. Basta con decir ante Dios: 'Señor, elijo no cobrarle esta deuda; la dejo en tus manos'. Y en ese instante tu alma queda libre.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Acude a la Divina Misericordia si te remuerde la culpa.**",
                    bodyText = """
Si cometiste errores o tuviste actitudes de las que hoy te arrepientes, llévalas al sacramento de la reconciliación o a la oración sincera ante el Sagrario. El perdón de Dios es infinito; no te creas más severo que Él insistiendo en condenarte.

Si el Señor te absuelve, ¿quién eres tú para seguir flagelándote? Aprende con humildad de tus caídas y vive hoy como una criatura renovada por la gracia.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No permitas que la amargura marchite tu generosidad futura.**",
                    bodyText = """
La herida de la traición o del desengaño tienta al alma a encerrarse en la dureza y el cinismo. San Pablo nos pide: 'Desterrad de vosotros toda amargura, ira, enfado y maledicencia'.

Pídele a Dios un corazón nuevo, de carne y no de piedra. El dolor bien llevado no endurece el espíritu; al contrario, lo hace más compasivo y sabio para cuidar a los demás.
                    """.trimIndent()
                )
            )

            ClinicalCategory.CONTACTO_CERO_LIMITES -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El límite firme es el espacio aséptico donde tu herida puede sanar.**",
                    bodyText = """
El contacto cero no es soberbia ni venganza; es el vallado prudente que la virtud de la prudencia te manda construir para no tentar a la debilidad ni permitir que continúe la confusión.

Quien ama el peligro en él perece, advierte el libro de los Proverbios. Cortar de raíz las vías de contacto es un acto de honestidad contigo mismo y de respeto hacia los designios del Señor.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La prudencia cristiana exige apartarse de las ocasiones de caída.**",
                    bodyText = """
Si mantener una línea abierta de comunicación te quita la paz, despierta celos o te sume en la ansiedad, tu deber moral es alejarte. La santidad de vida requiere valentía para poner límites claros.

Bloquear o silenciar no es odio: es legítima defensa de tu salud anímica y de tu estado de gracia. Sostén tu decisión en la oración con firmeza serena.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La caridad verdadera empieza por el orden del propio corazón.**",
                    bodyText = """
A veces nos engañamos pensando que debemos seguir en contacto 'por caridad' o 'para ayudar al otro'. Si ayudarle te destruye a ti y fomenta una dinámica destructiva, eso no es amor cristiano: es imprudencia.

Encomienda a esa persona a la misericordia de Dios en tus oraciones cotidianas, pero mantén tu distancia terrenal. Dios se basta para cuidarla sin necesidad de que tú hipoteques tu sosiego.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El silencio es el espacio donde el alma vuelve a escuchar a Dios.**",
                    bodyText = """
Mientras sigas debatiendo, respondiendo reproches o curioseando novedades, el ruido exterior no te dejará oír la voz del Espíritu Santo. El contacto cero instaura el silencio necesario para discernir.

Habita este retiro voluntario con espíritu agradecido. En el recogimiento Dios habla al corazón y restaura lo que el pecado o el desgaste mundano quebrantaron.
                    """.trimIndent()
                )
            )

            ClinicalCategory.AUTOESTIMA_RECHAZO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El desinterés ajeno no tiene el poder de disminuir tu dignidad sagrada.**",
                    bodyText = """
Tu valor inalienable fue sellado cuando Dios te llamó a la existencia por puro amor. No depende de los ojos de una criatura frágil, imperfecta y necesitada como tú.

El rechazo humano, por hondo que cale, no puede arrebatarte tu condición de templo del Espíritu Santo. Quien no supo o no pudo amarte no es el juez de tu alma; tu juez y tu Padre es el Señor, y Él te ama con amor eterno.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La piedra que desecharon los arquitectos es la que Dios ensalza.**",
                    bodyText = """
El Evangelio nos muestra repetidamente cómo Dios se fija en lo que el mundo descarta o menosprecia. Si te sientes desvalorizado o no elegido, recuerda que el mismo Cristo experimentó el desprecio de los suyos.

Tu dolor no es signo de fracaso, sino de humanidad. Ofrece la humillación sentida como ofrenda de humildad y verás florecer en ti una fortaleza que no procede de los hombres, sino del cielo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No busques en las criaturas la plenitud que solo Dios otorga.**",
                    bodyText = """
Santa Teresa de Jesús dejó una joya para las horas oscuras: 'Nada te turbe, nada te espante; todo se pasa, Dios no se muda. La paciencia todo lo alcanza; quien a Dios tiene nada le falta: solo Dios basta'.

Si hoy sientes un vacío amargo por el rechazo, deja que esa herida sea la puerta por donde entre el amor verdadero de Dios. Eres valioso sin necesidad de mendigar nada.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Tus dones y tu bondad siguen intactos ante los ojos de Dios.**",
                    bodyText = """
Haber sufrido un desenlace doloroso no anula tu capacidad para hacer el bien, ser generoso y construir una vida luminosa. Los talentos que Dios depositó en tus manos siguen esperando ser fructificados.

Sacúdete el polvo del desánimo. Hay hermanos a tu lado que necesitan de tu sonrisa, de tu trabajo y de tu caridad; levántate y camina en la esperanza.
                    """.trimIndent()
                )
            )

            ClinicalCategory.SOLEDAD_VACIO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La soledad no es un desierto estéril, sino el Tabor del encuentro íntimo.**",
                    bodyText = """
En la tradición bíblica, el desierto es siempre el lugar donde Dios despoja a su pueblo de ídolos para hablarle al corazón con ternura. El vacío que hoy sientes es el espacio que la Providencia abre para que aprendas a descansar en Él.

No temas a las horas de silencio en tu hogar. Llena ese espacio con oración, lecturas que eleven tu espíritu y la certeza profunda de que el Señor jamás te abandona.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Nunca estás en verdadera soledad si tu alma vive en gracia.**",
                    bodyText = """
Jesús prometió a sus discípulos: 'Yo estaré con vosotros todos los días hasta el fin del mundo'. La soledad que agobia al hombre contemporáneo procede a menudo de haberse desconectado de su Creador.

Descubre la riqueza de la soledad habitada por Dios. Habla con Él con la misma confianza con que un hijo habla con su padre; en su compañía encontrarás el descanso que ningún abrazo humano te garantizó.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Abre tu corazón a la fraternidad y a las obras de caridad.**",
                    bodyText = """
El dolor del aislamiento se disuelve cuando el alma sale de sí misma para consolar a otros. En la Iglesia y en tu comunidad hay enfermos, ancianos y personas necesitadas de una palabra cálida.

Sal al encuentro del prójimo que sufre. Al llevar alivio a quien padece mayor necesidad que tú, experimentarás cómo el vacío interior se colma de la alegría pura del Evangelio.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Aprende la serenidad del claustro interior.**",
                    bodyText = """
Los monjes y las almas contemplativas buscan voluntariamente el retiro para encontrar la perla preciosa de la paz espiritual. Este tiempo de menor bullicio relacional es tu monasterio particular.

Aprovéchalo para rezar el Rosario con devoción, dar paseos en la naturaleza bendiciendo la creación y redescubrir la hermosura de una vida ordenada y pacífica.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ANSIEDAD_SOMATICA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Entrega la turbación del cuerpo al Señor de la Paz.**",
                    bodyText = """
La angustia en el pecho y el temblor involuntario son el grito de la naturaleza asustada ante la intemperie. Recuerda la barca en el lago de Galilea en plena tormenta: Jesús dormía sereno, y al despertar mandó callar a los vientos y al mar.

Clama en tu interior: '¡Señor, sálvame que perezco!'. Respira lentamente, poniendo tu mano sobre el pecho, y deja que su presencia calme las aguas revueltas de tu fisiología.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La oración de abandono calma la agitación de la mente.**",
                    bodyText = """
El beato Carlos de Foucauld nos regaló una oración admirable para los instantes de pánico: 'Padre mío, me pongo en tus manos, haz de mí lo que quieras; sea lo que sea, te doy las gracias'.

Cuando sientas que te falta el aire, no luches con tus propias fuerzas humanas, que son escasas. Entrégate por completo a la Providencia divina; nada ocurre sin el permiso de Dios y Él no te dejará caer.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Cuida con delicadeza del templo que es tu cuerpo.**",
                    bodyText = """
Tu cuerpo es morada de Dios y hoy está herido y exhausto por el peso de la pena. No le exijas hazañas imposibles ni te recrimines por sentir taquicardia o agotamiento.

Bebe un vaso de agua fresca, afloja los músculos tensos y reposa unos instantes en silencio. Dios conoce nuestra fragilidad y se compadece de nuestras fatigas terrenales.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No temas a la tempestad: el Señor sostiene tus pasos.**",
                    bodyText = """
San Pío de Pietrelcina repetía incansablemente: 'Ora, ten fe y no te preocupes. La preocupación es inútil; Dios es misericordioso y escuchará tu oración'.

Cuando la crisis de angustia amenace con desbordarte, repite el nombre de Jesús con cada respiración pausada. El nombre del Salvador disipa las tinieblas del pánico.
                    """.trimIndent()
                )
            )

            ClinicalCategory.INSOMNIO_NOCHE -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Entrega la vigilia al Corazón de Cristo.**",
                    bodyText = """
En la oscuridad de la noche los temores se agrandan y el enemigo de las almas busca sembrar desolación cuando las defensas bajan. No te quedes en el lecho combatiendo pensamientos sombríos con tus solas fuerzas.

Toma el crucifijo o las cuentas del Rosario entre tus dedos. Reza cada Avemaría con lentitud, descansando en el regazo de la Virgen María como un niño fatigado tras una larga jornada.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La noche de Getsemaní: acompaña al Señor en tu vigilia.**",
                    bodyText = """
Cristo mismo experimentó la angustia mortal y el insomnio en el huerto de los Olivos mientras sus discípulos dormían. Tu desvelo doloroso puede ser transformado en unión con la oración de Jesús.

Dile: 'Señor, velo contigo esta noche; recibe mi cansancio y mi dolor por la paz de mi alma y la de los míos'. La vigilia ofrecida con fe deja de ser tormento y se convierte en bendición.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Encomienda tu descanso a la protección del ángel custodio.**",
                    bodyText = """
Antes de cerrar los ojos, pide a tu ángel de la guarda que custodie tus pensamientos y aleje las pesadillas y la inquietud de tu estancia. 'En paz me acuesto y en seguida me duermo, porque solo tú, Señor, me haces vivir confiado' (Salmo 4).

Si despiertas sobresaltado, no abras el teléfono ni mires la hora. Respira el aire de la noche y repite en paz: 'Dios está aquí'.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No te afanes por el sueño que tarda en venir.**",
                    bodyText = """
El afán por querer dormir genera más tensión y aleja el descanso. Acepta con mansedumbre que esta noche tu reposo sea más breve; Dios te dará las fuerzas necesarias para la jornada de mañana.

Permanece en quietud, contemplando la bondad del Señor y dejando que el cuerpo se relaje en su presencia providente.
                    """.trimIndent()
                )
            )

            ClinicalCategory.FECHAS_SIGNIFICATIVAS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Consagra este día especial a Dios en vez de entregarlo a la tristeza.**",
                    bodyText = """
Que hoy marque un aniversario o una efeméride no te obliga a hundirte en el dolor. Puedes transformar este día consagrándolo al Señor: acude a la Santa Misa, comulga con devoción y ofrece la jornada por la salvación de las almas.

El tiempo pertenece a Dios; no permitas que el pasado le robe la bendición a este día nuevo que el Creador ha puesto ante tus ojos.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La caridad en la distancia: ora y suelta el anhelo de contacto.**",
                    bodyText = """
La tentación clásica en aniversarios o cumpleaños es enviar una felicitación para mitigar la soledad o reabrir la puerta. Si sabes que el contacto dañará tu paz y la pureza de tu proceso, el mejor modo de desearle el bien al otro es rezar en secreto por su alma.

Encomiéndale a Dios en tu oración privada y sostén tu silencio exterior con templanza santa. Eso es caridad madura y prudente.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El Señor hace nuevas todas las cosas.**",
                    bodyText = """
El Apocalipsis nos consuela con la voz del Señor: 'He aquí que yo hago nuevas todas las cosas'. No te empeñes en revivir mentalmente las celebraciones de años pasados con añoranza desesperada.

Dios tiene nuevas bendiciones preparadas para ti si estás dispuesto a soltar las vasijas viejas. Vive el día de hoy con esperanza renovada.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Refúgiate en los afectos sanos que Dios te regala.**",
                    bodyText = """
No pases las fechas señaladas encerrado a solas con tus memorias. Comparte la mesa con tu familia, visita a buenos amigos o dedícate a una obra generosa.

El amor cristiano es amplio y fecundo; se extiende mucho más allá de una sola persona. Celebra la vida rodeado de quienes verdaderamente te aman y te respetan.
                    """.trimIndent()
                )
            )

            ClinicalCategory.NUEVA_PAREJA_EX -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Encomienda su camino a Dios y custodia tu propia vocación.**",
                    bodyText = """
Enterarte de que la otra persona ha iniciado una nueva relación puede herir el amor propio y suscitar la amargura del descarte. Sin embargo, su destino espiritual y sus elecciones ya no están bajo tu cuidado.

Reza con generosidad sincera: 'Señor, concédele la luz y la conversión, y a mí la paz y la fidelidad a tu Evangelio'. Al bendecir en vez de maldecir, rompes toda atadura de resentimiento.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No te compares con la felicidad aparente del mundo.**",
                    bodyText = """
Las apariencias externas suelen ser engañosas. Nadie conoce las cruces, los vacíos o las fragilidades que se esconden tras una nueva relación iniciada precipitadamente.

Tu valor ante Dios no disminuye porque otra persona ocupe el lugar que antes era tuyo. Dios te tiene reservado un propósito santo; mantén tus ojos fijos en el cielo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Guarda tu mirada de toda curiosidad destructiva.**",
                    bodyText = """
Buscar información o fotos de la nueva pareja es una falta de prudencia y una ocasión voluntaria de herir tu alma. San Pablo nos pide fijarnos en 'todo lo que es verdadero, noble, justo, puro y amable'.

Cierra de golpe esa puerta de espionaje. Pide al Señor la gracia de la indiferencia santa frente a lo que ya no te pertenece.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El plan de Dios es perfecto y no se frustra por las mudanzas humanas.**",
                    bodyText = """
Si Dios permitió que esa persona tomara otro rumbo, confía en que su providencia tiene caminos mejores para ti. 'Mis planes no son vuestros planes, ni vuestros caminos mis caminos', dice el Señor.

Descansa en la fe de que estás en las mejores manos. Lo que hoy parece una pérdida irreparable puede ser el comienzo de una bendición insospechada.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ENCUENTRO_CASUAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Serenidad y caridad cristiana en el encuentro inesperado.**",
                    bodyText = """
Si te cruzas con la expareja de forma imprevista, mantén la templanza y el decoro de un cristiano. No hay necesidad de huir con desespero ni de fingir afectos que ya no corresponden a la realidad.

Un saludo breve y respetuoso es suficiente para cumplir con la caridad. Continúa tu camino orando en silencio por la paz en tu corazón y en el suyo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La paz de Cristo es tu coraza frente al sobresalto.**",
                    bodyText = """
Es comprensible que el corazón palpite con fuerza ante la presencia repentina de quien fue tan cercano. Recuerda las palabras de Jesús a sus apóstoles: 'La paz os dejo, mi paz os doy; no se turbe vuestro corazón ni se acobarde'.

Haz una respiración honda, invoca a tu ángel custodio y sigue adelante sin mirar atrás. El encuentro ya pasó; tu alma permanece bajo la sombra del Altísimo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No concedas al encuentro el poder de desordenar tu vida de oración.**",
                    bodyText = """
El enemigo suele aprovechar estos sucesos fortuitos para suscitar rumiación, dudas y nostalgia durante días enteros. Reconoce la trampa y corta el diálogo interior de inmediato.

Acude a una iglesia a visitar al Santísimo si te es posible, o haz un acto de desagravio en tu mente. Deja el incidente a los pies del Señor y reanuda tus quehaceres con gozo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El mundo es amplio: camina con la libertad de los hijos de Dios.**",
                    bodyText = """
No te recluyas ni cambies tus itinerarios habituales por miedo a coincidir. Camina con la cabeza erguida, sabiendo que nada puede apartarte del amor de Dios.

Si vuelves a coincidir, que sea tu serenidad y tu compostura sobria el testimonio de que has puesto tu confianza en el Señor.
                    """.trimIndent()
                )
            )

            ClinicalCategory.COPARENTALIDAD_LOGISTICA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Contacto cero adaptativo con espíritu de rectitud y prudencia.**",
                    bodyText = """
Cuando hay hijos en común o deberes materiales insoslayables, la caridad y la justicia exigen cumplir las obligaciones con rigor intachable, manteniendo a la vez la debida distancia interior.

Comunícate con brevedad, verdad y respeto formal. No mezcles asuntos del pasado ni quejas personales en las conversaciones sobre la educación o bienestar de los menores.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Los hijos son un tesoro sagrado que Dios te ha confiado.**",
                    bodyText = """
Tu deber como padre o madre es proteger la inocencia y la paz de tus hijos por encima de cualquier herida o rencor conyugal. Jamás hables mal del otro progenitor ante ellos ni los involucres en disputas económicas.

Sé para ellos un reflejo de la ternura y la solidez de Dios. Tu paciencia heroica en medio de la dificultad será la mejor catequesis que recibirán en su vida.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Cumple tus compromisos con justicia escrupulosa.**",
                    bodyText = """
Sé puntual en las entregas, claro en las cuentas y exacto en los acuerdos. La rectitud moral en las cosas prácticas desarma las insidias y deja tu conciencia completamente limpia ante Dios y ante los hombres.

Si la otra parte actúa con deslealtad o provocación, no respondas con la misma moneda. Vence el mal con la fuerza del bien y deja que la justicia de Dios respalde tus pasos.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Pide la intercesión de la Sagrada Familia de Nazaret.**",
                    bodyText = """
Encomienda la situación familiar a Jesús, María y José. Ellos conocieron la incomprensión, el destierro y las dificultades prácticas, y supieron responder siempre con obediencia y amor santo.

Coloca a tus hijos y la gestión de la coparentalidad bajo su manto protector. Con su gracia sabrás actuar siempre con la sabiduría del justo.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ETIQUETAS_DIAGNOSTICAS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Atalaya examina los hechos observables, no las etiquetas clínicas.**",
                    bodyText = """
Juzgar las almas ajenas colgándoles etiquetas de 'narcisista' o 'tóxica' suele nacer del rencor o de la soberbia que busca sentirse justa condenando al prójimo. 'No juzguéis y no seréis juzgados', nos advirtió Jesús.

No te corresponde a ti diagnosticar al otro; eso queda para los profesionales de la salud mental y, en última instancia, para Dios. Tu tarea es mirar los hechos objetivos: si hubo daño continuado o falta de respeto, apartarte con firmeza es un deber de prudencia y custodia del corazón.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Céntrate en sanar tus propias debilidades antes que en juzgar las ajenas.**",
                    bodyText = """
El Evangelio nos interpela: '¿Por qué miras la mota en el ojo de tu hermano y no reparas en la viga que llevas en el tuyo?'. Pasar el tiempo analizando las patologías de la expareja te distrae de tu propio examen de conciencia.

Pídele a Dios luz para ver en qué te equivocaste tú, qué apegos desordenados necesitas purificar y cómo puedes crecer en santidad. Ahí es donde está tu salvación y tu libertad.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Distingue entre el pecado y el pecador.**",
                    bodyText = """
La enseñanza de la Iglesia nos invita a rechazar con firmeza el pecado o el daño moral, mientras oramos por la salvación del pecador. No necesitas odiar ni estigmatizar a quien te lastimó para defender tus límites con valentía.

Pon la distancia necesaria para proteger tu integridad física y espiritual, y encomienda a esa persona a la misericordia divina para que Dios toque su corazón cuando Él disponga.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La caridad no es ingenua: el bien exige prudencia y límites.**",
                    bodyText = """
Ser cristiano no significa tolerar pasivamente el maltrato, el engaño o la manipulación destructiva. Jesús mismo fue categórico con los hipócritas y se retiraba cuando querían despeñarlo.

Deshazte de la literatura patologizante que solo alimenta el resentimiento. Quédate con la verdad simple: el vínculo era insano y destructivo; la prudencia manda cerrar la puerta y seguir al Señor.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RECONSTRUIR_GENERAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Acompaño tu camino con serenidad, verdad y esperanza.**",
                    bodyText = """
En la senda de soltar y reconstruirte bajo la gracia de Dios, graba esta verdad en tu alma:
*«Puedes seguir rezando por la salvación de alguien y, al mismo tiempo, cerrar definitivamente la puerta para proteger la paz que Dios te ha concedido.»*

Hoy estás dando un paso valiente hacia tu verdadera libertad espiritual. Cada vez que eliges la confianza y la templanza por encima de la desesperación, permites que Dios moldee en ti un corazón limpio, maduro y generoso.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El Señor sana los corazones quebrantados y venda sus heridas.**",
                    bodyText = """
El Salmo 147 nos consuela con esta promesa infalible. Ningún dolor ofrecido con fe se pierde; las pruebas bien sobrellevadas son tierra fértil donde brota una santidad más profunda y fecunda.

Ten paciencia contigo mismo. Dios no tiene prisa; Él trabaja en lo secreto de tu alma, purificando tus intenciones y preparándote para obras mayores de servicio y amor verdadero.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Tu vocación no se agota en una ruptura afectiva.**",
                    bodyText = """
Dios te ha creado con un propósito eterno que ninguna pérdida terrenal puede frustrar. Tu vida tiene un valor infinito, repleto de llamadas diarias a hacer el bien, ser luz en tu entorno y santificar tu trabajo.

Redescubre la alegría del servicio humilde. Cuando pones tus manos al servicio del prójimo y tu corazón en las cosas de arriba, la tristeza del pasado se disipa como el rocío matinal.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Avanza con paso firme bajo el amparo de la gracia.**",
                    bodyText = """
San Pablo nos alienta: 'Todo lo puedo en Aquel que me conforta'. No caminas solo por este sendero; el Buen Pastor te lleva sobre sus hombros cuando las fuerzas escasean.

Mira hacia adelante con gozo sereno. La fidelidad de Dios es eterna; confía en su amor y camina con la paz de quien sabe que su destino está guardado en el cielo.
                    """.trimIndent()
                )
            )

            else -> ClinicalNewCategoriesCatolico.getVariants(category)
        }
    }
}
