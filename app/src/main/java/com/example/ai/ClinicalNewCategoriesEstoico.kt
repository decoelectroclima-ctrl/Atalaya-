package com.example.ai

/**
 * Variantes clínicas y filosóficas para las 15 nuevas categorías de Recuerda (Marco ESTOICO).
 * Cada categoría contiene 4 intervenciones de máxima profundidad, sobriedad y templanza.
 */
object ClinicalNewCategoriesEstoico {

    fun getVariants(category: ClinicalCategory): List<ClinicalVariant> {
        return when (category) {
            ClinicalCategory.NUEVA_PAREJA_EX -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La conducta ajena es un indiferente exterior a tu virtud.**",
                    bodyText = """
Que tu expareja comparta sus días con otra persona no resta un milímetro a tu valía ni a tu dignidad. La vida de los demás, sus elecciones afectivas y la rapidez con que buscan compañía pertenecen estrictamente a lo que no depende de ti.

El sufrimiento que experimentas ahora no procede del hecho en sí, sino de la comparación ficticia que tu mente añade. Recuerda a Epicteto: no son las cosas las que nos perturban, sino los juicios que formulamos sobre ellas. Su camino es ajeno; tu deber es gobernar el tuyo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El avance del otro no invalida tu proceso ni tu honor.**",
                    bodyText = """
Observar que alguien reemplaza un vínculo con celeridad suele revelar una incapacidad para sostener la soledad o el vacío, no un veredicto sobre lo que tú vales. Quien corre a refugiarse en otros brazos suele huir de su propia sombra.

Tu proceso de duelo y reconstrucción es sobrio y profundo. No compitas en una carrera ilusoria de apariencias: mantén tu ciudadela interior a salvo de la envidia y del despecho.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Distingue el dolor de la pérdida de la herida del orgullo.**",
                    bodyText = """
Séneca advertía que sufrimos con frecuencia más por la vanidad herida que por la realidad objetiva. Pregúntate con crudeza filosófica: ¿te duele la ausencia de esa persona o te indigna que continúe sin ti?

Si separas la pérdida auténtica del orgullo lastimado, descubrirás que lo que arde es el deseo infantil de ser insustituible. Acepta con nobleza que los seres humanos siguen sus propios cursos, y centra tu energía en perfeccionar tu propio carácter.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El sabio contempla el destino ajeno con serenidad y distancia.**",
                    bodyText = """
Pretender que quien se fue permanezca enclaustrado en el recuerdo es pretender mandar sobre el viento. Cada quien camina según su propio entendimiento de la felicidad o del error.

Tu triunfo estoico no radica en que el otro fracase en sus nuevos afectos, sino en que a ti deje de importarte con quién camina. Devuélvele a la naturaleza lo que le pertenece y atiende a tu presente.
                    """.trimIndent()
                )
            )

            ClinicalCategory.MIEDO_FUTURO_SOLEDAD -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El futuro no existe: no sufras por sombras imaginarias.**",
                    bodyText = """
Afirmar con certeza trágica que 'nunca encontrarás a nadie' o que 'te quedarás solo para siempre' es un acto de arrogancia epistémica. La mente en dolor asume dotes de adivinación que ningún mortal posee.

Como recordaba Séneca: *«Hay más cosas que nos asustan que las que nos destruyen, y sufrimos más a menudo en la imaginación que en la realidad.»* Desarma esa profecía absolutista; ocúpate del día de hoy, que ya encierra su propia exigencia.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La soledad no es carencia, es el templo del autodominio.**",
                    bodyText = """
Temer a la soledad es confesar que se teme a la propia compañía. Si consideras que estar sin pareja es una condena, cualquier vínculo que establezcas nacerá de la mendicidad y del miedo, no de la virtud.

Aprende a bastarte a ti mismo. Quien sabe convivir en paz consigo mismo no mendiga afecto ni acepta migajas por pánico al silencio. Conviértete en alguien a quien te gustaría acompañar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Cuestiona la ilusión de que no habrá nadie igual.**",
                    bodyText = """
Decir 'no habrá nadie como él o ella' es a la vez una obviedad y un engaño. Nadie es idéntico a nadie, pero asegurar que esa persona concentraba todo el bien del universo es pura miopía nacida del anhelo.

El cosmos es vasto y tu vida no ha concluido. Si mantienes tu alma templada, generosa y sabia, atraerás lo que sea acorde a tu virtud; y si el destino dispone que camines en solitario, lo harás con la grandeza de un espíritu libre.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Premeditatio Malorum: abraza la soledad y perderá su veneno.**",
                    bodyText = """
Haz el ejercicio estoico de mirar de frente a tu mayor temor: ¿qué ocurriría si, en efecto, no vuelves a tener pareja durante largo tiempo? ¿Acaso dejarías de respirar, de pensar con rectitud, de servir al bien común o de cultivar tu sabiduría?

Cuando constatas que tu valor esencial permanece inalterable aun en la soledad más austera, el fantasma del futuro pierde todo su poder de intimidación sobre ti.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RECAIDA_OCURRIDA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Lo hecho, hecho está: no sumes autoflagelación al tropiezo.**",
                    bodyText = """
Haber roto el contacto, enviado un mensaje o realizado una llamada ya pertenece al pasado irreversible. No tienes el poder de retroceder las manecillas del tiempo para borrar la acción; flagelarte solo multiplica la perturbación.

Un soldado no se queda tendido en el polvo llorando por haber caído en la trinchera. Sacúdete el lodo, asume la lección con frialdad analítica y reanuda tu postura de guardia en este mismo instante.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Un desliz no anula los días de templanza que forjaste.**",
                    bodyText = """
La mente suele engañarse creyendo que una recaída reinicia el contador de tu dignidad a cero. Esa es una falacia de blanco o negro. Los días en que resististe y ejercitaste el autodominio siguen impresos en tu carácter.

Registra lo sucedido con serenidad. Identifica qué emoción o momento de cansancio precedió al impulso y sella esa grieta para el porvenir. Vuelve de inmediato a tu bastión.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Contempla la recaída como un hecho pedagógico.**",
                    bodyText = """
Marco Aurelio nos advertía que los obstáculos en el camino son, en verdad, el camino. Este contacto no te devolvió la paz ni resolvió nada; al contrario, te ha mostrado con crudeza renovada por qué decidiste apartarte.

Usa esa incomodidad como combustible para la templanza. Ya has comprobado una vez más que allí no hay respuesta ni sosiego; deja de buscar agua en un pozo seco.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La nobleza se demuestra en la rapidez para levantarse.**",
                    bodyText = """
Cualquiera puede caer ante una ola inesperada de nostalgia o debilidad; solo el espíritu recto recupera la compostura sin caer en el drama ni en la autocompasión paralizante.

No envíes un segundo mensaje para 'explicar' o 'justificar' el primero. Corta en seco en este segundo. Tu silencio a partir de ahora es tu mejor declaración de soberanía.
                    """.trimIndent()
                )
            )

            ClinicalCategory.AUTOCRITICA_RECAIDA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Distingue el error de la acción de tu naturaleza esencial.**",
                    bodyText = """
Haber cedido a un impulso es un fallo en el juicio del momento; concluir que 'eres un desastre' o que 'no tienes fuerza de voluntad' es una extralimitación tiránica de tu mente.

Epicteto enseñaba que quien comete un error no se convierte en escoria, sino en un ser humano que no vio con claridad la impresión correcta. Corrige la visión, ajusta tus defensas y deja de insultar al principio rector que habita en ti.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La autocrítica destructiva es vanidad disfrazada de rigor.**",
                    bodyText = """
Tratarte con desprecio por haber fallado no te hace más disciplinado; paradójicamente, te debilita y te sumerge en el desamparo, haciéndote aún más vulnerable al próximo impulso.

El sabio no se insulta: se corrige. Si fueras el mentor de un joven pupilo que tropezó, ¿le escupirías agravios o le ordenarías levantarse y vigilar mejor su flanco? Haz contigo lo segundo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El autodominio es un músculo que se entrena con paciencia.**",
                    bodyText = """
Nadie nace siendo filósofo consumado ni atleta invicto. La templanza se forja a través de caídas, roces y rectificaciones constantes. No exijas perfección instantánea a quien apenas está aprendiendo a navegar una tormenta.

Rechaza la etiqueta de 'fracasado'. Tuviste un momento de flaqueza; reconócelo con humildad, toma aire y persevera en la práctica diaria.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Guarda respeto a tu propia ciudadela interior.**",
                    bodyText = """
¿Permitirías que un extraño entre en tu hogar y te llame incompetente o débil una y otra vez? Entonces, ¿por qué toleras que tu propia voz interna te vilipendie de ese modo?

Silencia el murmullo de la culpa estéril. La verdadera fuerza no consiste en no haber titubeado jamás, sino en recobrar la rectitud moral sin vacilar tras el tropiezo.
                    """.trimIndent()
                )
            )

            ClinicalCategory.PROGRESO_POSITIVO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Honra este remanso de serenidad como fruto de tu temple.**",
                    bodyText = """
Haber tenido horas o un día entero sin que el recuerdo te asedie no es casualidad ni azar: es la recompensa natural de haber respetado tus límites y ordenado tu facultad rectora.

Disfruta de esta ligereza y paz del alma (ataraxia) con gratitud serena. Comprueba con tus propios ojos que el dolor no era eterno y que la calma vuelve cuando dejas de alimentar el fuego del apego.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Consolida la victoria interior sin soberbia ni descuido.**",
                    bodyText = """
Marco Aurelio recordaba que el alma debe acostumbrarse a la buena fortuna y a la calma sin perder su centro. Sentirte bien hoy es un hito monumental que demuestra tu capacidad de regeneración.

Permítete sonreír y respirar con alivio. Estás reconquistando tu tiempo, tus proyectos y tu espacio mental. Graba en tu memoria este estado: es la prueba irrefutable de que la libertad te sienta bien.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El sosiego conquistado es tu bien más preciado.**",
                    bodyText = """
Durante semanas tu mente estuvo encadenada a la rumiación y al sobresalto; hoy has saboreado la independencia de pensar en ti, en tu trabajo y en tus propios anhelos.

No temas que esta calma sea fugaz ni te anticipes a futuras tormentas. El presente te ofrece paz: acógela, fortifícate en ella y reconoce tu propio mérito en haber sostenido el rumbo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La virtud de la perseverancia comienza a dar sus frutos.**",
                    bodyText = """
Como la semilla que trabaja en silencio bajo la tierra antes de que brote la espiga, tus días de renuncia sobria están revelando su verdadera cosecha.

Hoy te perteneces un poco más que ayer. Continúa cultivando tus deberes cotidianos, honra este bienestar y camina con la certeza de que tu carácter es hoy más sólido que cuando todo comenzó.
                    """.trimIndent()
                )
            )

            ClinicalCategory.CONTACTO_INEVITABLE -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Cumple tu deber con impecabilidad y distancia estoica.**",
                    bodyText = """
Cuando el contacto es ineludible por hijos, deberes laborales o asuntos prácticos compartidos, el sabio actúa como el embajador de una nación soberana: con cortesía impecable, brevedad absoluta y sin mezclar el dolor personal con la función.

No busques complicidad ni reproches en la mirada. Tu papel allí es estrictamente logístico y funcional. Cumple tu tarea con excelencia y retírate a tu propia paz sin concederle ni una palabra superflua.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La firmeza no necesita aspereza: la sobriedad es tu escudo.**",
                    bodyText = """
Tener que coincidir en un evento, reunión de trabajo o entrega de los niños pone a prueba tu ecuanimidad. No necesitas mostrar frialdad hostil ni forzar una sonrisa artificial; la sobriedad natural es tu mejor armadura.

Responde únicamente a lo estrictamente necesario, utilizando oraciones concisas y desprovistas de carga emocional. Quien no reacciona ante las provocaciones del pasado demuestra que ya no es vulnerable a ellas.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El escenario exterior no puede perturbar tu hegemonikón.**",
                    bodyText = """
Epicteto nos recordaba que somos actores en una obra cuyo argumento no hemos elegido. Si te corresponde desempeñar el papel de interactuar civilizadamente por causas mayores, hazlo con la máxima dignidad.

Lo que la otra persona diga, vista o gesticule en ese encuentro es un adiaphoron (indiferente externo). No permitas que un saludo forzado o un comentario incidental derribe las murallas de tu tranquilidad interior.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Delimita la frontera entre lo funcional y lo íntimo.**",
                    bodyText = """
El contacto obligado no es una reapertura vincular. Es un trámite civil, laboral o parental que debes gestionar con la misma neutralidad con que atenderías un asunto en una oficina pública.

Mantén las comunicaciones por canales escritos y neutros siempre que sea posible. Protege el santuario de tu vida personal y no abras rendijas a conversaciones que no sean indispensables para el objetivo en cuestión.
                    """.trimIndent()
                )
            )

            ClinicalCategory.TRAICION_INFIDELIDAD -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La deslealtad mancha a quien la comete, jamás a ti.**",
                    bodyText = """
Haber sido víctima de engaño o traición quema las entrañas, pero el discernimiento estoico exige claridad implacable: la mentira, la duplicidad y la cobardía moral son faltas cometidas por el otro contra su propia excelencia moral.

Nadie puede herir tu virtud si tú no consientes en degradarte. Tú actuaste de buena fe y con lealtad; esa es tu victoria moral que nadie te puede arrebatar. Quien traicionó se ha despojado a sí mismo de honor y templanza.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Agradece a la verdad que te haya librado de la mentira.**",
                    bodyText = """
El dolor de descubrir el engaño es agudo, pero la ignorancia prolongada al lado de un impostor hubiera sido infinitamente más dañina. La verdad, aunque desgarre, te ha devuelto a la realidad de las cosas.

No añadas amargura a la herida imaginando venganzas o justicias cósmicas. El mayor castigo para el desleal es tener que convivir eternamente con su propia falta de integridad. Tú, por tu parte, caminas libre y limpio.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No proyectes la bajeza de uno sobre el resto del mundo.**",
                    bodyText = """
El peligro de la traición es que te tiente a caer en el cinismo y en el desprecio general hacia el género humano. Marco Aurelio se recordaba al amanecer: *«Hoy me cruzaré con personas desleales, ingratas e insolentes; pero ninguna de ellas puede hacerme daño porque no participo de su vileza.»*

Que alguien haya sido incapaz de honrar un compromiso no significa que la lealtad o la decencia hayan muerto en el mundo. Conserva tu capacidad de ser noble, pero aprende a elegir con mayor prudencia en el porvenir.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Rompe el vínculo con lo podrido y restaura tu honor.**",
                    bodyText = """
Preguntarse '¿por qué me mintió?' es querer encontrar lógica en la debilidad de carácter ajena. No busques coherencia en quien prefirió el engaño al coraje de la honestidad.

Cierra esa puerta con la determinación de quien retira de su mesa un alimento descompuesto. Tu honor no depende de la fidelidad ajena, sino de tu negativa absoluta a seguir compartiendo tu vida con quien no es digno de confianza.
                    """.trimIndent()
                )
            )

            ClinicalCategory.AMBIVALENCIA_EMOCIONAL -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Es natural que en el alma convivan impresiones contrarias.**",
                    bodyText = """
Sentir afecto por momentos y enojo al siguiente no es locura ni hipocresía; es el reflejo de una mente que está procesando la complejidad de la experiencia humana. Los hábitos del corazón no se borran de un plumazo.

No te angusties pretendiendo obligarte a sentir una sola emoción pura. Observa la marea de tus impresiones como quien mira la lluvia desde la ventana: déjalas estar sin darles crédito ciego a la hora de actuar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La razón debe ser la brújula entre vientos opuestos.**",
                    bodyText = """
Puedes añorar la calidez que existió en ciertos instantes y, simultáneamente, tener la convicción lúcida de que esa persona o ese vínculo vulneraba tu dignidad y no convenía a tu vida.

Ambas cosas son ciertas en planos distintos: la añoranza habla de la memoria; la firmeza habla de la sabiduría práctica. Deja que el principio rector decida tus pasos, no el vaivén de tus afectos transitorios.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No confundas la nostalgia del pasado con el deseo del presente.**",
                    bodyText = """
La ambivalencia se nutre de mezclar lo que fue con lo que es. Cuando sientas que aún le quieres, pregúntate con sobriedad: ¿quieres a la persona real que demostró ser al final, o añoras la imagen de lo que prometía ser en sus mejores días?

Acepta esa dualidad sin forzar una resolución precipitada. Conforme el tiempo avance y mantengas tu conducta firme, la razón disipará la niebla y la paz prevalecerá sobre el torbellino.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La templanza consiste en gobernar la discordia interior.**",
                    bodyText = """
Un barco que navega entre dos corrientes no zozobra si el timonel mantiene el pulso firme. Tu timón son tus principios y tus límites intocables.

Permite que las contradicciones de tu mente se agoten solas por falta de combustible. Si no actúas impulsivamente en los momentos de ternura ni en los de rabia, tu serenidad emergerá intacta.
                    """.trimIndent()
                )
            )

            ClinicalCategory.SINTOMAS_FISICOS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El cuerpo refleja la tormenta del alma: cuídalo con templanza.**",
                    bodyText = """
El nudo en la garganta, la opresión en el pecho, la falta de apetito o el insomnio son la respuesta biológica del organismo ante la pérdida y el sobresalto. No luches con ira contra tus propios síntomas físicos ni los interpretes como debilidad de carácter.

Trata a tu cuerpo como el fiel animal de carga que sostiene tu espíritu. Dale agua fresca, comida sencilla aunque no sientas deseo, camina al aire libre y practica respiraciones lentas para apaciguar el pulso. Si la afección persiste o se agrava, acudir a un profesional de la salud es un acto de sensatez práctica, no una rendición.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Somete la agitación corporal a la calma del hegemonikón.**",
                    bodyText = """
Séneca recordaba que cuando el cuerpo se rebela por el dolor, el espíritu debe prestarle su serenidad. Si el pecho se contrae, dile a tu respiración que se dilate; si el cuerpo tiembla, siéntate con la espalda recta y las plantas de los pies bien asentadas en la tierra.

No aumentes la angustia fisiológica con pensamientos de pánico. El cuerpo se restablecerá gradualmente de esta desintoxicación si no lo saturas de estimulantes ni de rumiación obsesiva.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El dolor físico del desapego es el precio de la convalecencia.**",
                    bodyText = """
Cuando se extirpa una parte dañada, la herida sangra e inflama los tejidos adyacentes antes de cicatrizar. No esperes sentirte rebosante de vigor en medio del proceso quirúrgico del alma.

Acepta el malestar somático con paciencia austera. Descansa cuando te sea posible, no te exijas hazañas titánicas en estos días y busca el consejo médico prudente si tu salud general se ve comprometida.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La disciplina corporal auxilia a la razón filosófica.**",
                    bodyText = """
Los antiguos filósofos sabían que una mente perturbada necesita un cuerpo ordenado. Oblígate a mantener rutinas básicas: aseo riguroso, horarios de sueño regulares aunque no duermas de inmediato, y movimiento físico que agote la tensión acumulada.

Cuando alineas tu biología con la sobriedad, la mente encuentra el terreno despejado para recuperar su lucidez y su fortaleza soberana.
                    """.trimIndent()
                )
            )

            ClinicalCategory.RUMIACION_NOCTURNA -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La noche magnifica los fantasmas: suspende el juicio hasta el alba.**",
                    bodyText = """
En la oscuridad y el silencio de la madrugada, cuando el cansancio embota el entendimiento, las pasiones cobran una talla desmesurada que no les corresponde. Pretender resolver los enigmas de tu vida a las tres de la mañana es la mayor de las insensateces.

Dile a tus pensamientos con autoridad marcial: *«A ustedes no los juzgaré en las tinieblas. Si tienen algo que alegar, preséntense mañana al mediodía bajo la luz del sol.»* Aleja el teléfono, acuesta la cabeza y entrega la noche al reposo necesario.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La almohada es para el descanso, no para el litigio mental.**",
                    bodyText = """
Dar vueltas en la cama recreando conversaciones ficticias o analizando agravios pasados no cambia un solo milímetro de tu historia; solo consume la energía que necesitarás para vivir con virtud al despertar.

Si la mente insiste en rumiar, levántate un instante, escribe en un papel las frases que te atormentan para sacarlas del pecho, bébete un vaso de agua y regresa a la cama sin permitir más debate. El insomnio se vence quitándole audiencia.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Marco Aurelio y el gobierno de la mente en la vigilia.**",
                    bodyText = """
El emperador se recordaba que la mente debe ser como una esfera pulida y brillante, que ilumina su propia quietud sin dejarse envolver por sombras externas. La noche pasa inexorablemente; no permitas que te robe tu dignidad.

Concentra tu atención en el flujo regular de tu respiración. Cuenta los latidos, relaja los músculos de la mandíbula y de los hombros y acepta la noche con templanza: dormirás cuando la naturaleza lo disponga, pero no entregarás tu vigilia a la desesperación.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Corta el festín nocturno de la nostalgia estéril.**",
                    bodyText = """
La melancolía aprovecha la penumbra porque sabe que tus defensas diurnas están desactivadas. Reconoce esa trampa con astucia estoica: no estás descubriendo verdades profundas de madrugada; simplemente estás cansado/a y desarmado/a ante las memorias.

Niega el asentimiento a esas impresiones nocturnas. Deja que fluyan sin engancharte a ellas y espera con paciencia la claridad del nuevo día.
                    """.trimIndent()
                )
            )

            ClinicalCategory.METAPREGUNTAS_PROCESO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El tiempo del alma no atiende a calendarios mercantiles.**",
                    bodyText = """
Preguntarse '¿cuánto va a durar esto?' o '¿es normal que aún me duela?' revela la impaciencia del deseo por apresurar lo que debe madurar en su propio curso. El grano de trigo permanece bajo la nieve el tiempo exacto que la naturaleza manda antes de germinar.

No hay plazos universales ni tablas cronológicas para el desapego. Tu labor no es calcular el final de la travesía, sino dar el paso que te corresponde hoy con impecable entereza.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El dolor es el precio del aprendizaje: no lo midas con disgusto.**",
                    bodyText = """
Sentir que el proceso es largo y sinuoso es absolutamente congruente con la condición humana. Un vínculo significativo altera patrones de vida, expectativas y hábitos profundos; desmantelarlos exige tiempo y trabajo constante.

No te juzgues por no haber 'superado' todo en un mes o en un año. Lo que importa no es la velocidad, sino que tu dirección no se desvíe hacia la sumisión o el retroceso.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Quien pregunta cuándo llegará la meta pierde de vista el camino.**",
                    bodyText = """
Si caminas por un sendero pedregoso y preguntas a cada paso cuántas millas faltan, el viaje se torna insoportable. En cambio, si atiendes a dónde posas cada pie en este instante, llegarás a la cumbre sin desgastarte en vanas quejas.

Confía en que cada día que no claudicas, tu alma se vuelve más sólida. La sanación no es un interruptor que se apaga de golpe, sino un amanecer paulatino que apenas se nota hasta que la luz ya lo inunda todo.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La normalidad del duelo radica en su naturaleza orgánica.**",
                    bodyText = """
Sí, es completamente normal que haya días de alivio seguidos de recaídas emocionales; es la naturaleza de las mareas internas. El progreso estoico no es una línea recta sin altibajos, sino la firmeza de carácter con que afrontas cada oleada.

Abraza el proceso tal como es, sin exigirle concesiones a la realidad. Estás transitando tu prueba con dignidad; no la empañes con prisas estériles.
                    """.trimIndent()
                )
            )

            ClinicalCategory.BUSQUEDA_REAFIRMACION -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Devuelve la mirada a tu propio tribunal interior.**",
                    bodyText = """
¿Por qué buscas que una voz externa confirme si 'hiciste bien' en poner ese límite o en bloquear el contacto? El sabio no busca en el aplauso o en la opinión de terceros la validez de sus principios morales.

Regresa al momento en que tomaste la determinación: ¿lo hiciste movido/a por la necesidad de preservar tu paz y tu dignidad frente al desorden? Si tu motivo fue la custodia de tu propia virtud, ninguna opinión ajena puede agregarle rectitud ni quitársela.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La duda retrospectiva es un síntoma de la flaqueza del deseo.**",
                    bodyText = """
Cuando el dolor inicial se atenúa, la memoria suele blanquear el pasado y susurrarte: '¿y si exageraste?'. Es la trampa clásica que tiende la mente añorante para sabotear las decisiones valientes.

No negocies con la duda cuando estás en el valle de la soledad. Confía en la lucidez que tuviste cuando los hechos estaban frescos y tomaste la decisión de alejarte. Sostén tu palabra como el compromiso más sagrado contigo mismo/a.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Hiciste lo que correspondía para proteger tu ciudadela.**",
                    bodyText = """
Poner límites firmes o cortar los canales de comunicación nunca es un acto de soberbia o maldad cuando busca detener la hemorragia de tu autoestima. Fue un acto de defensa legítima de tu propia tranquilidad.

Deja de pedir permiso para haberte cuidado. Asume tu elección con la gravedad de un gobernante que protege su territorio: la decisión fue tuya, fue necesaria y no requiere disculpas ante el tribunal de tus nostalgias.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El consentimiento de tu razón es tu único juez soberano.**",
                    bodyText = """
Pregúntate con honestidad implacable: de haber permanecido en esa dinámica, ¿quién serías hoy? ¿Qué quedaría de tu sosiego y de tu integridad?

La respuesta sincera a esa pregunta es toda la confirmación que necesitas. No busques afuera lo que ya habita con meridiana claridad en tu propia conciencia.
                    """.trimIndent()
                )
            )

            ClinicalCategory.OBJETOS_RECUERDOS -> listOf(
                ClinicalVariant(
                    headerGreeting = "**La materia no tiene alma: el valor se lo otorgas tú.**",
                    bodyText = """
Las cartas, los regalos, las fotos y las pertenencias olvidadas son solo papel, madera, metales o píxeles. En sí mismos, carecen de poder para hacerte bien o mal; son indiferentes materiales.

El poder perturbador se lo concede tu propia mente cuando los convierte en talismanes de la memoria o en altares de un pasado que ya no existe. Decide con serenidad qué hacer con ellos, no desde la rabia impulsiva ni desde el apego idólatra.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El sabio despeja su entorno de lo que obstaculiza la virtud.**",
                    bodyText = """
Si tener esas fotos en la galería o esos objetos en la habitación actúa como una trampa continua que reaviva la fantasía y estimula la vana imaginación, retíralos con sobriedad y sin dramatismo teatral.

No necesitas una ceremonia de fuego ni una purga llena de rencor. Guárdalos en una caja fuera de tu alcance visual o devuélvelos con brevedad civil si corresponden al otro. Tu hogar debe ser un templo de templanza, no un museo del dolor.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Borrar o guardar: haz lo que dicte tu tranquilidad presente.**",
                    bodyText = """
No hay una ley universal que obligue a borrar cada recuerdo de inmediato si eso te genera angustia de pérdida, ni tampoco virtud alguna en conservar gigabytes de fotos para torturarte en la noche.

Pregúntate con ecuanimidad: ¿este objeto me ayuda hoy a ser más libre, más templado y más lúcido, o me ancla a la servidumbre del pasado? Lo que te ate a la servidumbre, suéltalo sin temblar.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Devuelve a cada cosa su justo lugar en el cosmos.**",
                    bodyText = """
Cuando un ciclo concluye en la naturaleza, las hojas caen y nutren el suelo para nuevas estaciones. Forzar la permanencia de los despojos materiales de un vínculo marchito es pretender congelar el otoño.

Agradece lo que esos objetos testimoniaron en su hora adecuada y da paso a un espacio limpio donde tu nueva vida pueda respirar sin interferencias del ayer.
                    """.trimIndent()
                )
            )

            ClinicalCategory.ESTANCAMIENTO_PROCESO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**El crecimiento profundo rara vez se manifiesta con ruido.**",
                    bodyText = """
Creer que 'llevas meses y sigues igual' suele ser una distorsión generada por el cansancio. Las raíces de los árboles más robustos crecen en la penumbra profunda del suelo sin que nadie advierta su avance a simple vista.

Si miras con objetividad estoica, comprobarás que hoy toleras silencios, ausencias y verdades que al inicio te hubieran desmoronado por completo. No confunda la persistencia de un dolor sordo con la falta de avance interior.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El desierto parece interminable hasta que se alcanza el oasis.**",
                    bodyText = """
En la travesía de la reconstrucción hay largas mesetas donde no se perciben grandes revelaciones ni euforias, solo la rutina austera de seguir adelante. Esa perseverancia en lo llano es precisamente donde se forja el carácter.

No exijas fuegos artificiales a tu mente. El hecho de levantarte cada día, cumplir con tu labor y no humillarte mendigando atención ajena ya es una victoria silenciosa y colosal.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**Cuestiona tu estándar de evaluación: ¿qué llamas 'igual'?**",
                    bodyText = """
Si defines el avance como 'no sentir jamás una pizca de tristeza', te has impuesto un listón inhumano e irracional. El sabio estoico no es de piedra; experimenta el zarpazo de la memoria, pero no permite que gobierne su juicio.

Que aún sientas un rescoldo no significa que estés atrapado en el incendio del primer día. Reconoce la brecha entre la angustia desesperada del inicio y la melancolía serena de hoy.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La paciencia es la templanza aplicada a la duración del tiempo.**",
                    bodyText = """
Séneca enseñaba que la impaciencia ante la adversidad solo consigue multiplicar el peso de la carga. Deja de medir obsesivamente tu estado día a día como quien desentierra una semilla para ver si ya echó raíces.

Dedícate con devoción a tus deberes de hoy, cultiva tu mente, cuida de los tuyos y deja que el tiempo cumpla su oficio curativo sin exigirle plazos forzados.
                    """.trimIndent()
                )
            )

            ClinicalCategory.DUDA_HABER_TERMINADO -> listOf(
                ClinicalVariant(
                    headerGreeting = "**Haber tomado la decisión no te inmuniza contra el dolor.**",
                    bodyText = """
Existe el engaño común de creer que quien termina una relación no tiene derecho al duelo o que, si duele, es señal de que cometió un error. El cirujano que amputa un miembro gangrenado para salvar la vida del paciente siente el peso del dolor, pero no por ello duda de la necesidad de la operación.

Pusiste fin al vínculo no porque fuera fácil o agradable, sino porque era indispensable para custodiar tu dignidad y tu coherencia de vida. El dolor actual no desmiente la sabiduría de tu elección.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**No confundas el vacío de la ausencia con el arrepentimiento.**",
                    bodyText = """
Cortar un lazo afectivo deja un vacío inmediato en la rutina y en la piel. Es fácil que la mente confunda ese silencio incómodo con la necesidad de volver atrás.

Recuerda los motivos éticos y racionales que te condujeron a la partida: el desgaste, la falta de correspondencia o la toxicidad no han desaparecido mágicamente porque ahora sientas añoranza. Sostén tu decisión con noble firmeza.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**El amor propio exige a veces el coraje de la renuncia.**",
                    bodyText = """
Dejar marchar a quien se ama cuando la relación destruye la propia paz es uno de los mayores actos de virtud y lucidez que un ser humano puede consumar. Requiere más temple renunciar por principio que quedarse por debilidad.

Honra tu valentía. No vuelvas sobre tus pasos como el perro que regresa a su propio vómito; camina hacia adelante sabiendo que elegiste la difícil senda del respeto hacia ti mismo/a.
                    """.trimIndent()
                ),
                ClinicalVariant(
                    headerGreeting = "**La rectitud de la causa prevalece sobre la comodidad del hábito.**",
                    bodyText = """
Marco Aurelio se recordaba: *«Si no conviene, no lo hagas; si no es verdad, no lo digas.»* Terminaste porque comprendiste que sostener esa dinámica era una mentira contraria a tu bienestar superior.

No permitas que la nostalgia transitoria mancille la claridad del juicio que tuviste cuando diste el paso. Mantente fiel a tu determinación: el dolor pasará, pero la dignidad recuperada permanecerá contigo.
                    """.trimIndent()
                )
            )

            else -> ClinicalVariantsEstoico.getVariants(ClinicalCategory.RECONSTRUIR_GENERAL)
        }
    }
}
