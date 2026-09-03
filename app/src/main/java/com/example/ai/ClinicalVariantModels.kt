package com.example.ai

import com.example.data.SoltarFramework
import java.util.concurrent.ConcurrentHashMap

/**
 * Categorías clínicas ampliadas para el acompañamiento y razonamiento de Recuerda.
 * Cubre las 11 originales más 7 patrones específicos de alta prevalencia en rupturas y duelos vinculares.
 */
enum class ClinicalCategory(val state: String) {
    RECUPERAR_PAREJA("ACEPTAR"),
    SENALES_DIGITALES("DEJAR_DE_PERSEGUIR"),
    RUMIACION_BUCLE("DEJAR_DE_PERSEGUIR"),
    IMPULSO_CONTACTAR("REGULAR"),
    DEPENDENCIA_EMOCIONAL("COMPRENDER"),
    NOSTALGIA_IDEALIZACION("ACEPTAR"),
    CULPA_RENCOR_RABIA("ACEPTAR"),
    CONTACTO_CERO_LIMITES("REGULAR"),
    AUTOESTIMA_RECHAZO("COMPRENDER"),
    SOLEDAD_VACIO("ACEPTAR"),
    ANSIEDAD_SOMATICA("REGULAR"),
    INSOMNIO_NOCHE("REGULAR"),
    FECHAS_SIGNIFICATIVAS("ACEPTAR"),
    NUEVA_PAREJA_EX("ACEPTAR"),
    ENCUENTRO_CASUAL("REGULAR"),
    COPARENTALIDAD_LOGISTICA("REGULAR"),
    ETIQUETAS_DIAGNOSTICAS("COMPRENDER"),
    RECONSTRUIR_GENERAL("RECONSTRUIR"),
    // 15 Nuevas Categorías Clínicas
    MIEDO_FUTURO_SOLEDAD("COMPRENDER"),
    RECAIDA_OCURRIDA("REGULAR"),
    AUTOCRITICA_RECAIDA("COMPRENDER"),
    PROGRESO_POSITIVO("RECONSTRUIR"),
    CONTACTO_INEVITABLE("REGULAR"),
    TRAICION_INFIDELIDAD("ACEPTAR"),
    AMBIVALENCIA_EMOCIONAL("COMPRENDER"),
    SINTOMAS_FISICOS("REGULAR"),
    RUMIACION_NOCTURNA("REGULAR"),
    METAPREGUNTAS_PROCESO("COMPRENDER"),
    BUSQUEDA_REAFIRMACION("ACEPTAR"),
    OBJETOS_RECUERDOS("ACEPTAR"),
    ESTANCAMIENTO_PROCESO("COMPRENDER"),
    DUDA_HABER_TERMINADO("COMPRENDER")
}

data class ClinicalVariant(
    val headerGreeting: String,
    val bodyText: String
)

object ClinicalCategoryClassifier {

    fun classify(input: String, isRumination: Boolean): ClinicalCategory {
        val lower = input.lowercase()

        return when {
            // 1. Diagnósticos apresurados y etiquetas patologizantes a terceros
            lower.contains("narcisista") || lower.contains("psicópata") || lower.contains("sociópata") ||
            lower.contains("manipulador tóxico") || lower.contains("personalidad límite") || lower.contains("es un tóxico") || lower.contains("es una tóxica") -> {
                ClinicalCategory.ETIQUETAS_DIAGNOSTICAS
            }

            // 2. Intentos de reconquista, manipulación o recuperar a la expareja
            lower.contains("recuperar") || lower.contains("volver con") || lower.contains("hacer que vuelva") ||
            lower.contains("hacer que me busque") || lower.contains("darle celos") || lower.contains("estrategia para que") ||
            lower.contains("reconquistar") || lower.contains("que se arrepienta") -> {
                ClinicalCategory.RECUPERAR_PAREJA
            }

            // [NUEVA 3] Recaída ya ocurrida (distinta del impulso previo)
            lower.contains("le escribí") || lower.contains("le escribi") || lower.contains("lo llamé") || lower.contains("lo llame") ||
            lower.contains("la llamé") || lower.contains("la llame") || lower.contains("tuve una recaída") || lower.contains("tuve una recaida") ||
            lower.contains("recaí") || lower.contains("recai") || lower.contains("rompí el contacto") || lower.contains("rompi el contacto") ||
            lower.contains("acabo de escribirle") || lower.contains("le mandé mensaje") || lower.contains("le mande mensaje") ||
            lower.contains("le contesté") || lower.contains("le conteste") || lower.contains("le respondí") || lower.contains("le respondi") -> {
                ClinicalCategory.RECAIDA_OCURRIDA
            }

            // [NUEVA 4] Autocrítica dura tras una recaída
            lower.contains("soy un desastre") || lower.contains("no tengo fuerza de voluntad") ||
            lower.contains("siempre hago lo mismo") || lower.contains("no sirvo para esto") ||
            lower.contains("no tengo voluntad") || lower.contains("volví a fallar") || lower.contains("volvi a fallar") ||
            lower.contains("no tengo autocontrol") || lower.contains("arruiné todo") || lower.contains("arruine todo") -> {
                ClinicalCategory.AUTOCRITICA_RECAIDA
            }

            // [NUEVA 5] Progreso positivo / celebración
            lower.contains("hoy me sentí bien") || lower.contains("hoy me senti bien") ||
            lower.contains("creo que voy mejorando") || lower.contains("voy mejorando") ||
            lower.contains("no pensé en él") || lower.contains("no pensé en ella") ||
            lower.contains("no pense en el") || lower.contains("no pense en ella") ||
            lower.contains("estoy orgulloso de mí") || lower.contains("estoy orgullosa de mí") ||
            lower.contains("estoy orgulloso de mi") || lower.contains("estoy orgullosa de mi") ||
            lower.contains("me siento en paz hoy") || lower.contains("hoy tuve un buen día") || lower.contains("hoy tuve un buen dia") ||
            lower.contains("siento alivio hoy") -> {
                ClinicalCategory.PROGRESO_POSITIVO
            }

            // [NUEVA 1] Pareja nueva del ex
            lower.contains("tiene pareja nueva") || lower.contains("está con otra persona") || lower.contains("esta con otra persona") ||
            lower.contains("sale con alguien") || lower.contains("ya rehizo su vida") || lower.contains("rehizo su vida") ||
            lower.contains("está con otra") || lower.contains("esta con otra") || lower.contains("está con otro") || lower.contains("esta con otro") ||
            lower.contains("nueva pareja") || lower.contains("nuevo novio") || lower.contains("nueva novia") ||
            lower.contains("ya tiene a alguien") || lower.contains("conoció a alguien") || lower.contains("conocio a alguien") ||
            lower.contains("me reemplazó") || lower.contains("me reemplazo") -> {
                ClinicalCategory.NUEVA_PAREJA_EX
            }

            // [NUEVA 2] Miedo al futuro / catastrofismo sobre quedarse solo/a
            lower.contains("nunca voy a encontrar a nadie") || lower.contains("nunca encontrare a nadie") ||
            lower.contains("me voy a quedar solo para siempre") || lower.contains("me voy a quedar sola para siempre") ||
            lower.contains("quedarme solo para siempre") || lower.contains("quedarme sola para siempre") ||
            lower.contains("no habrá nadie como él") || lower.contains("no habrá nadie como ella") ||
            lower.contains("no habra nadie como el") || lower.contains("no habra nadie como ella") ||
            lower.contains("no habrá nadie como") || lower.contains("no habra nadie como") ||
            lower.contains("jamás encontraré a nadie") || lower.contains("jamas encontrare a nadie") ||
            lower.contains("moriré solo") || lower.contains("moriré sola") || lower.contains("morire solo") || lower.contains("morire sola") -> {
                ClinicalCategory.MIEDO_FUTURO_SOLEDAD
            }

            // [NUEVA 7] Traición o infidelidad como causa de la ruptura
            lower.contains("me engañó") || lower.contains("me engano") || lower.contains("me fue infiel") ||
            lower.contains("descubrí que") || lower.contains("descubri que") || lower.contains("me mintió todo este tiempo") ||
            lower.contains("me mintio todo este tiempo") || lower.contains("infidelidad") || lower.contains("vida paralela") ||
            lower.contains("me engañaba") || lower.contains("me traicionó") || lower.contains("me traiciono") ||
            lower.contains("traición de confianza") || lower.contains("traicion de confianza") -> {
                ClinicalCategory.TRAICION_INFIDELIDAD
            }

            // [NUEVA 8] Ambivalencia (amor y rechazo a la vez)
            lower.contains("lo quiero y lo odio") || lower.contains("la quiero y la odio") ||
            lower.contains("lo amo y lo odio") || lower.contains("la amo y la odio") ||
            lower.contains("no sé qué siento") || lower.contains("no se que siento") ||
            lower.contains("sentimientos contradictorios") || lower.contains("sentimientos encontrados") ||
            lower.contains("a ratos lo extraño") || lower.contains("a ratos la extraño") ||
            lower.contains("lo odio pero lo extraño") || lower.contains("la odio pero la extraño") -> {
                ClinicalCategory.AMBIVALENCIA_EMOCIONAL
            }

            // [NUEVA 10] Rumiación nocturna específica
            lower.contains("por la noche es peor") || lower.contains("de noche es peor") ||
            lower.contains("no puedo dormir pensando en") || lower.contains("no puedo dormir pensando") ||
            lower.contains("me desvelo pensando") || lower.contains("por las noches es peor") ||
            lower.contains("en la cama pensando") || lower.contains("madrugadas pensando") ||
            lower.contains("desvelo pensando") -> {
                ClinicalCategory.RUMIACION_NOCTURNA
            }

            // [NUEVA 9] Síntomas físicos del duelo
            lower.contains("no tengo apetito") || lower.contains("no puedo comer") || lower.contains("sin apetito") ||
            lower.contains("me tiembla el cuerpo") || lower.contains("tiembla el cuerpo") ||
            lower.contains("nudo en el pecho") || lower.contains("opresión en el pecho") || lower.contains("opresion en el pecho") ||
            lower.contains("nudo en la garganta") || lower.contains("pecho apretado") || lower.contains("no puedo respirar") ||
            lower.contains("falta el aire") || lower.contains("taquicardia") || lower.contains("dolor físico") || lower.contains("dolor fisico") ||
            lower.contains("dolor en el pecho") -> {
                ClinicalCategory.SINTOMAS_FISICOS
            }

            // [NUEVA 11] Meta-preguntas sobre el proceso mismo
            lower.contains("es normal") || lower.contains("esto es normal") ||
            lower.contains("cuánto va a durar esto") || lower.contains("cuanto va a durar esto") ||
            lower.contains("cuánto va a durar") || lower.contains("cuanto va a durar") ||
            lower.contains("voy a superar esto algún día") || lower.contains("voy a superar esto algun dia") ||
            lower.contains("voy a superar esto") || lower.contains("voy a superarlo") ||
            lower.contains("cuánto tiempo dura el duelo") || lower.contains("cuanto tiempo dura el duelo") ||
            lower.contains("algún día dejará de doler") || lower.contains("algun dia dejara de doler") -> {
                ClinicalCategory.METAPREGUNTAS_PROCESO
            }

            // [NUEVA 12] Búsqueda de reafirmación sobre decisiones ya tomadas
            lower.contains("hice bien en bloquearlo") || lower.contains("hice bien en bloquearla") ||
            lower.contains("hice bien en bloquear") || lower.contains("dime que hice lo correcto") ||
            lower.contains("dime si hice lo correcto") || lower.contains("debería haberlo dejado") ||
            lower.contains("deberia haberlo dejado") || lower.contains("debería haberla dejado") ||
            lower.contains("deberia haberla dejado") || lower.contains("hice bien en terminar") ||
            lower.contains("hice bien en dejarlo") || lower.contains("hice bien en dejarla") ||
            lower.contains("tomé la decisión correcta") || lower.contains("tome la decision correcta") -> {
                ClinicalCategory.BUSQUEDA_REAFIRMACION
            }

            // [NUEVA 13] Objetos y recuerdos físicos
            lower.contains("borrar las fotos") || lower.contains("borrar fotos") ||
            lower.contains("tengo sus cosas en casa") || lower.contains("tengo sus cosas") ||
            lower.contains("qué hago con los regalos") || lower.contains("que hago con los regalos") ||
            lower.contains("devolver sus cosas") || lower.contains("qué hago con sus cosas") ||
            lower.contains("que hago con sus cosas") || lower.contains("guardar las fotos") ||
            lower.contains("tirar sus cosas") || lower.contains("los regalos que me dio") -> {
                ClinicalCategory.OBJETOS_RECUERDOS
            }

            // [NUEVA 14] Sensación de estancamiento pese al tiempo transcurrido
            lower.contains("llevo meses y sigo igual") || lower.contains("llevo mucho tiempo y sigo igual") ||
            lower.contains("no avanzo") || lower.contains("parece que no mejoro nunca") ||
            lower.contains("sigo en el mismo punto") || lower.contains("no veo avance") ||
            lower.contains("sigo igual que al principio") || lower.contains("no mejoro") ||
            lower.contains("siento que no avanzo") || lower.contains("estancado") || lower.contains("estancada") -> {
                ClinicalCategory.ESTANCAMIENTO_PROCESO
            }

            // [NUEVA 15] Duda sobre haber sido quien terminó la relación
            lower.contains("fui yo quien lo dejó") || lower.contains("fui yo quien la dejó") ||
            lower.contains("fui yo quien lo dejo") || lower.contains("fui yo quien la dejo") ||
            lower.contains("fui yo quien terminó") || lower.contains("fui yo quien termino") ||
            lower.contains("terminé yo la relación") || lower.contains("termine yo la relacion") ||
            lower.contains("tomé yo la decisión") || lower.contains("tome la decision") ||
            lower.contains("yo decidí terminar") || lower.contains("yo decidi terminar") ||
            lower.contains("yo lo dejé") || lower.contains("yo la dejé") ||
            lower.contains("yo lo deje") || lower.contains("yo la deje") ||
            lower.contains("yo rompí la relación") || lower.contains("yo rompi") -> {
                ClinicalCategory.DUDA_HABER_TERMINADO
            }

            // [NUEVA 6] Contacto inevitable (hijos, trabajo, amigos comunes)
            lower.contains("tengo que verlo por los niños") || lower.contains("tengo que verla por los niños") ||
            lower.contains("por los niños") || lower.contains("los niños") || lower.contains("hijos en común") ||
            lower.contains("hijos en comun") || lower.contains("trabajamos juntos") || lower.contains("trabajamos juntas") ||
            lower.contains("en el trabajo") || lower.contains("tenemos amigos en común") || lower.contains("tenemos amigos en comun") ||
            lower.contains("amigos en común") || lower.contains("amigos en comun") ||
            lower.contains("hay una boda") || lower.contains("hay un evento") || lower.contains("evento familiar") ||
            lower.contains("custodia") || lower.contains("pensión") || lower.contains("pension") || lower.contains("abogado") -> {
                ClinicalCategory.CONTACTO_INEVITABLE
            }

            // 4. Señales e interacción indirecta en redes sociales y entornos digitales
            lower.contains("vio mi historia") || lower.contains("miró mi estado") || lower.contains("me desbloqueó") ||
            lower.contains("está en línea") || lower.contains("a quién sigue") || lower.contains("le dio like") ||
            lower.contains("revisé su perfil") || lower.contains("miré su foto") || lower.contains("subió una foto") ||
            lower.contains("borró su foto") || lower.contains("puso una indirecta") || lower.contains("última conexión") -> {
                ClinicalCategory.SENALES_DIGITALES
            }

            // 5. Fechas de riesgo y aniversarios dolorosos
            lower.contains("cumpleaños") || lower.contains("aniversario") || lower.contains("san valentín") ||
            lower.contains("navidad") || lower.contains("año nuevo") || lower.contains("nuestra fecha") ||
            lower.contains("fecha especial") || lower.contains("el día que nos conocimos") -> {
                ClinicalCategory.FECHAS_SIGNIFICATIVAS
            }

            // 6. Encuentros casuales o cara a cara
            lower.contains("me lo encontré") || lower.contains("me la crucé") || lower.contains("la vi en") ||
            lower.contains("lo vi en") || lower.contains("coincidimos") || lower.contains("cara a cara") ||
            lower.contains("verlo en persona") || lower.contains("verla en persona") -> {
                ClinicalCategory.ENCUENTRO_CASUAL
            }

            // 7. Insomnio, madrugadas y pesadillas con la expareja
            lower.contains("no puedo dormir") || lower.contains("insomnio") || lower.contains("pesadilla") ||
            lower.contains("soñé con") || lower.contains("madrugada") || lower.contains("desperté llorando") ||
            lower.contains("desvelo") || lower.contains("sueño con él") || lower.contains("sueño con ella") -> {
                ClinicalCategory.INSOMNIO_NOCHE
            }

            // 8. Ansiedad somática aguda y desregulación fisiológica
            lower.contains("ansiedad") || lower.contains("ataque de pánico") || lower.contains("pánico") ||
            lower.contains("desesperación") || lower.contains("temblor") || lower.contains("angustia física") -> {
                ClinicalCategory.ANSIEDAD_SOMATICA
            }

            // 9. Impulso urgente de contacto o craving de comunicación
            lower.contains("impulso") || lower.contains("escribir") || lower.contains("llamar") ||
            lower.contains("contactar") || lower.contains("buscarlo") || lower.contains("buscarla") ||
            lower.contains("mensaje") || lower.contains("romper el contacto") || lower.contains("mandarle un audio") ||
            lower.contains("hablarle") -> {
                ClinicalCategory.IMPULSO_CONTACTAR
            }

            // 10. Límites firmes, contacto cero y blindaje personal
            lower.contains("límite") || lower.contains("contacto cero") || lower.contains("bloque") ||
            lower.contains("espiar") || lower.contains("ver su") || lower.contains("silenciar") ||
            lower.contains("borrar su número") || lower.contains("eliminar de redes") -> {
                ClinicalCategory.CONTACTO_CERO_LIMITES
            }

            // 11. Dependencia emocional y sensación de necesidad existencial
            lower.contains("no puedo vivir sin") || lower.contains("le necesito") || lower.contains("la necesito") ||
            lower.contains("no soy nada sin") || lower.contains("dependo de") || lower.contains("obsesión") ||
            lower.contains("adicción") || lower.contains("sin él no soy") || lower.contains("sin ella no soy") -> {
                ClinicalCategory.DEPENDENCIA_EMOCIONAL
            }

            // 12. Autoestima, herida de rechazo y desvalorización
            lower.contains("autoestima") || lower.contains("no valgo") || lower.contains("inútil") ||
            lower.contains("rechazo") || lower.contains("vergüenza") || lower.contains("fracaso") ||
            lower.contains("no fui suficiente") || lower.contains("me dejó por") || lower.contains("no sirvo") -> {
                ClinicalCategory.AUTOESTIMA_RECHAZO
            }

            // 13. Culpa, rencor, rabia, injusticia y resentimiento
            lower.contains("culpa") || lower.contains("perdón") || lower.contains("rencor") ||
            lower.contains("injusto") || lower.contains("odio") || lower.contains("rabia") ||
            lower.contains("resentimiento") || lower.contains("venganza") ||
            lower.contains("por qué me hizo esto") -> {
                ClinicalCategory.CULPA_RENCOR_RABIA
            }

            // 14. Soledad dolorosa y sensación de desamparo o vacío
            lower.contains("soledad") || lower.contains("solo") || lower.contains("sola") ||
            lower.contains("vacío") || lower.contains("desamparo") || lower.contains("estar sin nadie") ||
            lower.contains("casa vacía") -> {
                ClinicalCategory.SOLEDAD_VACIO
            }

            // 15. Nostalgia, recuerdos e idealización de la historia
            lower.contains("extraño") || lower.contains("nostalgia") || lower.contains("idealiz") ||
            lower.contains("recuerdo") || lower.contains("echo de menos") || lower.contains("duele") ||
            lower.contains("triste") || lower.contains("melancolía") || lower.contains("momentos bonitos") -> {
                ClinicalCategory.NOSTALGIA_IDEALIZACION
            }

            // 16. Rumiación obsesiva y bucle de porqués
            isRumination || lower.contains("por qué") || lower.contains("porque hizo") ||
            lower.contains("descifrar") || lower.contains("analizar") || lower.contains("darle vueltas") ||
            lower.contains("bucle") || lower.contains("no entiendo sus razones") -> {
                ClinicalCategory.RUMIACION_BUCLE
            }

            // 17. Reconstrucción, avance y soberanía general (Fallback enriquecido)
            else -> {
                ClinicalCategory.RECONSTRUIR_GENERAL
            }
        }
    }
}

object ClinicalVariantRegistry {

    // Memoria en tiempo de ejecución para evitar repetir la misma variante dos veces consecutivas
    private val lastUsedVariantIndex = ConcurrentHashMap<String, Int>()

    fun getResolvedVariant(
        input: String,
        isRumination: Boolean,
        framework: SoltarFramework,
        userContext: SoltarUserContext
    ): Triple<String, String, String> {
        val category = ClinicalCategoryClassifier.classify(input, isRumination)
        val variantList = getVariantsForCategoryAndFramework(category, framework)

        val memoryKey = "${category.name}_${framework.name}"
        val lastIndex = lastUsedVariantIndex[memoryKey]

        // Excluir la última utilizada si hay más de 1 variante disponible
        val eligibleIndices = if (variantList.size > 1 && lastIndex != null) {
            variantList.indices.filter { it != lastIndex }
        } else {
            variantList.indices.toList()
        }

        val chosenIndex = eligibleIndices.random()
        lastUsedVariantIndex[memoryKey] = chosenIndex

        val chosenVariant = variantList[chosenIndex]

        // Contextualización adicional con base en días de contacto cero o factores parentales
        val enrichedBody = applyContextualNuances(chosenVariant.bodyText, category, userContext)

        return Triple(category.state, chosenVariant.headerGreeting, enrichedBody)
    }

    private fun getVariantsForCategoryAndFramework(
        category: ClinicalCategory,
        framework: SoltarFramework
    ): List<ClinicalVariant> {
        return when (framework) {
            SoltarFramework.ESTOICO -> ClinicalVariantsEstoico.getVariants(category)
            SoltarFramework.PSICOLOGIA_MODERNA -> ClinicalVariantsPsicologia.getVariants(category)
            SoltarFramework.CATOLICO -> ClinicalVariantsCatolico.getVariants(category)
        }
    }

    private fun applyContextualNuances(
        baseText: String,
        category: ClinicalCategory,
        userContext: SoltarUserContext
    ): String {
        val notes = mutableListOf<String>()

        if (userContext.streakDays > 0 && (category == ClinicalCategory.IMPULSO_CONTACTAR || category == ClinicalCategory.CONTACTO_CERO_LIMITES || category == ClinicalCategory.SENALES_DIGITALES)) {
            notes.add("🛡️ *Llevas ${userContext.streakDays} días sosteniendo este límite protector. No entregues ese territorio ganado por un momento de alivio pasajero.*")
        }

        if (userContext.hasChildren && (category == ClinicalCategory.COPARENTALIDAD_LOGISTICA || category == ClinicalCategory.CONTACTO_CERO_LIMITES)) {
            notes.add("👨‍👧 *Tu brújula en este punto es el bienestar de tus hijos y la comunicación estrictamente funcional y desprovista de carga emocional.*")
        }

        return if (notes.isNotEmpty()) {
            baseText + "\n\n" + notes.joinToString("\n")
        } else {
            baseText
        }
    }

    // Funciones utilitarias para pruebas y auditoría clínica
    fun countTotalVariants(): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        ClinicalCategory.values().forEach { cat ->
            val stoicCount = ClinicalVariantsEstoico.getVariants(cat).size
            val psychoCount = ClinicalVariantsPsicologia.getVariants(cat).size
            val catholicCount = ClinicalVariantsCatolico.getVariants(cat).size
            map[cat.name] = stoicCount + psychoCount + catholicCount
        }
        return map
    }

    fun clearMemory() {
        lastUsedVariantIndex.clear()
    }
}
