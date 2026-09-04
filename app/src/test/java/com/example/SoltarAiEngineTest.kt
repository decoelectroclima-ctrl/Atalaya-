package com.example

import com.example.ai.SoltarAiEngine
import com.example.ai.SoltarUserContext
import com.example.data.ClinicalKnowledgeBase
import com.example.data.SoltarFramework
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SoltarAiEngineTest {

    @Before
    fun setUp() {
        com.example.ai.ClinicalVariantRegistry.clearMemory()
    }

    @Test
    fun testSelfHarmTriggerDetection() {
        val criticalPhrases = listOf(
            "quiero morir",
            "tengo ganas de suicidarme",
            "no quiero vivir más",
            "acabar con todo de una vez",
            "no encuentro salida y quiero dejar de respirar",
            "pastillas para no despertar",
            "hacerme daño y cortarme"
        )

        criticalPhrases.forEach { phrase ->
            assertTrue("Debe detectar ideación crítica en: '$phrase'", SoltarAiEngine.checkSelfHarmTrigger(phrase))
        }

        val benignPhrases = listOf(
            "hoy me siento triste pero quiero salir a caminar",
            "extraño a mi ex pareja",
            "tengo el impulso de escribirle",
            "¿cómo aplico el estoicismo hoy?"
        )

        benignPhrases.forEach { phrase ->
            assertFalse("No debe disparar falso positivo en: '$phrase'", SoltarAiEngine.checkSelfHarmTrigger(phrase))
        }
    }

    @Test
    fun testCrisisResponseContainsEmergencyNumbers() = runBlocking {
        val response = SoltarAiEngine.generateResponse("quiero matarme no aguanto más")
        assertEquals("SEGURIDAD", response.stateDetected)
        assertTrue(response.replyText.contains("024"))
        assertTrue(response.replyText.contains("717 003 717"))
        assertTrue(response.replyText.contains("988"))
        assertTrue(response.replyText.contains("112") || response.replyText.contains("911"))
    }

    @Test
    fun testAntiManipulationAndWinningBackExReframing() = runBlocking {
        val response = SoltarAiEngine.generateResponse("quiero recuperar a mi ex y darle celos para que me busque")
        assertEquals("ACEPTAR", response.stateDetected)
        assertTrue("Debe rechazar manipulación o falsas esperanzas", 
            response.replyText.contains("manipulación", ignoreCase = true) || 
            response.replyText.contains("esperanza", ignoreCase = true) ||
            response.replyText.contains("contacto cero", ignoreCase = true) ||
            response.replyText.contains("reconquistar", ignoreCase = true) ||
            response.replyText.contains("dignidad", ignoreCase = true) ||
            response.replyText.contains("regrese", ignoreCase = true)
        )
    }

    @Test
    fun testCyberStalkingAndStoryViewingReframing() = runBlocking {
        val response = SoltarAiEngine.generateResponse("vio mi historia de instagram y miró mi estado")
        assertEquals("DEJAR_DE_PERSEGUIR", response.stateDetected)
        assertTrue("Debe desarmar la lectura de mente o hipervigilancia en redes sociales",
            response.replyText.contains("señales", ignoreCase = true) ||
            response.replyText.contains("digital", ignoreCase = true) ||
            response.replyText.contains("pantalla", ignoreCase = true) ||
            response.replyText.contains("redes", ignoreCase = true) ||
            response.replyText.contains("red social", ignoreCase = true) ||
            response.replyText.contains("virtual", ignoreCase = true) ||
            response.replyText.contains("estado", ignoreCase = true)
        )
    }

    @Test
    fun testClinicalKnowledgeBaseMultiFramework() {
        val stoicCapsule = ClinicalKnowledgeBase.findRelevantCapsule("impulso de escribir", SoltarFramework.ESTOICO)
        assertNotNull(stoicCapsule)
        assertTrue(stoicCapsule.author.contains("Epicteto") || stoicCapsule.author.contains("Marco Aurelio") || stoicCapsule.author.contains("Séneca"))

        val catholicCapsule = ClinicalKnowledgeBase.findRelevantCapsule("dolor y soledad", SoltarFramework.CATOLICO)
        assertNotNull(catholicCapsule)

        val modernCapsule = ClinicalKnowledgeBase.findRelevantCapsule("abstinencia y culpa", SoltarFramework.PSICOLOGIA_MODERNA)
        assertNotNull(modernCapsule)
    }

    @Test
    fun testLocalClinicalReasoningProvidesActionableSteps() = runBlocking {
        val response = SoltarAiEngine.generateResponse(
            userMessage = "Siento mucha culpa por cómo terminó todo",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            userContext = SoltarUserContext(streakDays = 5)
        )

        assertNotNull(response.replyText)
        assertTrue(response.replyText.isNotEmpty())
        assertTrue("Debe proporcionar una respuesta conversacional profunda y procesada", 
            response.replyText.length > 30
        )
    }

    @Test
    fun testVariantAntiRepetitionMechanism() {
        val input = "tengo un impulso incontrolable de escribirle un mensaje"
        val framework = SoltarFramework.ESTOICO
        val context = SoltarUserContext()

        // Llamar 2 veces seguidas
        val resp1 = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(input, false, framework, context)
        val resp2 = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(input, false, framework, context)

        // Comprobar que no son exactamente idénticas de forma consecutiva
        assertNotEquals(
            "Dos respuestas consecutivas de la misma categoría no deben ser idénticas",
            resp1.replyText,
            resp2.replyText
        )
    }

    @Test
    fun testFrameworkSpecificToneAndThemes() {
        val input = "no puedo vivir sin esa persona me siento vacío"
        val context = SoltarUserContext()

        val stoicResp = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(input, false, SoltarFramework.ESTOICO, context)
        val catholicResp = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(input, false, SoltarFramework.CATOLICO, context)
        val modernResp = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(input, false, SoltarFramework.PSICOLOGIA_MODERNA, context)

        // Verificamos que las respuestas son sustancialmente distintas y coherentes con cada cosmovisión
        assertNotEquals(stoicResp.replyText, catholicResp.replyText)
        assertNotEquals(catholicResp.replyText, modernResp.replyText)
        assertNotEquals(stoicResp.replyText, modernResp.replyText)

        // Validación de palabras clave por marco
        assertTrue(
            "Marco católico debe contener referencias espirituales o de Providencia/Dios/Hijo/Criatura",
            catholicResp.replyText.contains("Dios") ||
            catholicResp.replyText.contains("Señor") ||
            catholicResp.replyText.contains("afectos") ||
            catholicResp.replyText.contains("gracia") ||
            catholicResp.replyText.contains("Creador")
        )

        assertTrue(
            "Marco psicológico debe contener referencias a apego/regulación/autorregulación/vínculo",
            modernResp.replyText.contains("apego", ignoreCase = true) ||
            modernResp.replyText.contains("psicológico", ignoreCase = true) ||
            modernResp.replyText.contains("psicología", ignoreCase = true) ||
            modernResp.replyText.contains("cerebro", ignoreCase = true) ||
            modernResp.replyText.contains("autorregular", ignoreCase = true) ||
            modernResp.replyText.contains("regulación", ignoreCase = true) ||
            modernResp.replyText.contains("vínculo", ignoreCase = true) ||
            modernResp.replyText.contains("ansiolítico", ignoreCase = true) ||
            modernResp.replyText.contains("centro de gravedad", ignoreCase = true)
        )
    }

    @Test
    fun testAllFifteenNewCategoriesClassificationAndVariants() {
        val testCases = listOf(
            "tiene pareja nueva y está con otra persona" to com.example.ai.ClinicalCategory.NUEVA_PAREJA_EX,
            "nunca voy a encontrar a nadie me voy a quedar solo para siempre" to com.example.ai.ClinicalCategory.MIEDO_FUTURO_SOLEDAD,
            "anoche le escribí y rompí el contacto" to com.example.ai.ClinicalCategory.RECAIDA_OCURRIDA,
            "soy un desastre no tengo fuerza de voluntad" to com.example.ai.ClinicalCategory.AUTOCRITICA_RECAIDA,
            "hoy me sentí bien y creo que voy mejorando" to com.example.ai.ClinicalCategory.PROGRESO_POSITIVO,
            "tengo que verlo por los niños y la custodia" to com.example.ai.ClinicalCategory.CONTACTO_INEVITABLE,
            "descubrí que me engañó y me fue infiel" to com.example.ai.ClinicalCategory.TRAICION_INFIDELIDAD,
            "lo quiero y lo odio tengo sentimientos contradictorios" to com.example.ai.ClinicalCategory.AMBIVALENCIA_EMOCIONAL,
            "no tengo apetito y siento un nudo en el pecho" to com.example.ai.ClinicalCategory.SINTOMAS_FISICOS,
            "por la noche es peor me desvelo pensando en la cama" to com.example.ai.ClinicalCategory.RUMIACION_NOCTURNA,
            "cuánto va a durar esto es normal sentir esto" to com.example.ai.ClinicalCategory.METAPREGUNTAS_PROCESO,
            "dime que hice lo correcto e hice bien en bloquearlo" to com.example.ai.ClinicalCategory.BUSQUEDA_REAFIRMACION,
            "qué hago con los regalos y borrar las fotos" to com.example.ai.ClinicalCategory.OBJETOS_RECUERDOS,
            "llevo meses y sigo igual siento que no avanzo" to com.example.ai.ClinicalCategory.ESTANCAMIENTO_PROCESO,
            "fui yo quien lo dejó y terminé yo la relación" to com.example.ai.ClinicalCategory.DUDA_HABER_TERMINADO
        )

        val frameworks = listOf(
            SoltarFramework.ESTOICO,
            SoltarFramework.PSICOLOGIA_MODERNA,
            SoltarFramework.CATOLICO
        )

        testCases.forEach { (phrase, expectedCategory) ->
            val detectedCategory = com.example.ai.ClinicalCategoryClassifier.classify(phrase, false)
            assertEquals("La frase '$phrase' debe clasificarse como $expectedCategory", expectedCategory, detectedCategory)

            frameworks.forEach { fw ->
                val response = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(
                    input = phrase,
                    isRumination = false,
                    framework = fw,
                    userContext = SoltarUserContext()
                )
                assertNotNull("La respuesta no debe ser nula para $expectedCategory en $fw", response.replyText)
                assertTrue("La respuesta debe tener longitud sustancial para $expectedCategory en $fw", response.replyText.length > 80)
            }
        }
    }
}
