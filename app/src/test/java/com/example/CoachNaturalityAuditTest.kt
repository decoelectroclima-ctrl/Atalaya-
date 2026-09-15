package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.ai.OnDeviceLlmEngine
import com.example.ai.SoltarAiEngine
import com.example.ai.SoltarUserContext
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
class CoachNaturalityAuditTest {

    private lateinit var application: Application

    @Before
    fun setUp() {
        application = ApplicationProvider.getApplicationContext()
        com.example.ai.ClinicalVariantRegistry.clearMemory()
    }

    @Test
    fun intencionSaludoConDolorEsEmocional() {
        val message = "hola, estoy destrozado y no puedo más"
        val intent = SoltarAiEngine.classifyMessageIntent(message)
        assertEquals(SoltarAiEngine.MessageIntent.CONTENIDO_EMOCIONAL, intent)
    }

    @Test
    fun rumiacionExPalabraCompleta() = runBlocking {
        val history = listOf(
            "user" to "Hola",
            "assistant" to "Hola, aquí estoy.",
            "user" to "Tengo mucho estrés"
        )
        val response = SoltarAiEngine.generateResponse(
            userMessage = "tengo un examen mañana y estoy nervioso",
            conversationHistory = history,
            framework = SoltarFramework.PSICOLOGIA_MODERNA
        )
        assertFalse("No debe detectar rumiación por la subcadena 'ex' dentro de 'examen'", response.isRuminationDetected)
    }

    @Test
    fun fallbackNoRepiteSaludo() {
        val userContext = SoltarUserContext(userName = "Lucas")
        val history = listOf(
            "user" to "Hola",
            "assistant" to "Hola, Lucas. Aquí estoy para acompañarte si lo necesitas."
        )
        val response = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(
            input = "No sé qué hacer con tantas dudas",
            isRumination = false,
            framework = SoltarFramework.ESTOICO,
            userContext = userContext,
            conversationHistory = history
        )
        assertFalse("El segundo turno no debe repetir saludo 'Hola'", response.replyText.startsWith("Hola"))
    }

    @Test
    fun fallbackLongitudLimitada() {
        val userContext = SoltarUserContext(userName = "Ana")
        val response = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(
            input = "Siento que todo se desmorona y no sé por dónde empezar",
            isRumination = true,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            userContext = userContext
        )
        val words = response.replyText.split(Regex("\\s+"))
        assertTrue("La respuesta fallback debe ser <= 90 palabras (actual: ${words.size})", words.size <= 90)
    }

    @Test
    fun fallbackSinMarkdownLiteral() {
        val userContext = SoltarUserContext(userName = "Carlos", streakDays = 15)
        val response = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(
            input = "Tengo el impulso de escribirle ahora mismo",
            isRumination = false,
            framework = SoltarFramework.ESTOICO,
            userContext = userContext
        )
        assertFalse("No debe contener asteriscos dobles", response.replyText.contains("**"))
        assertFalse("No debe contener asteriscos simples literales", response.replyText.contains("*"))
    }

    @Test
    fun systemPromptSeUsaEnFlujoOnDevice() {
        val userContext = SoltarUserContext(userName = "Elena")
        val systemPrompt = SoltarAiEngine.buildPromptWithFramework(SoltarFramework.PSICOLOGIA_MODERNA, userContext)
        val fullPrompt = OnDeviceLlmEngine.buildFullPrompt(
            prompt = "Me siento culpable",
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            userContext = userContext,
            systemBlock = systemPrompt
        )
        assertTrue("El prompt on-device debe incluir las reglas clínicas y el marco", fullPrompt.contains("REGLAS CLÍNICAS") || fullPrompt.contains("ENFOQUE Y TONO DEL COACH"))
        assertTrue("El prompt on-device debe incluir el formato de turnos de chat", fullPrompt.contains("<start_of_turn>user") && fullPrompt.contains("<start_of_turn>model"))
    }

    @Test
    fun mentoriaDiarioCierraVariado() = runBlocking {
        val entry1 = "Hoy me sentí más tranquilo y pude salir a caminar sin pensar tanto en el pasado."
        val entry2 = "Hoy tuve un momento difícil al ver una foto antigua pero logré respirar y calmarme."
        
        val result1 = SoltarAiEngine.generateJournalMentorship(entry1, framework = SoltarFramework.ESTOICO)
        val result2 = SoltarAiEngine.generateJournalMentorship(entry2, framework = SoltarFramework.ESTOICO)

        val lastSentence1 = result1.feedback.lines().last { it.isNotBlank() }
        val lastSentence2 = result2.feedback.lines().last { it.isNotBlank() }

        assertNotEquals("Dos llamadas de mentoría con entradas distintas deben rotar sus frases de cierre", lastSentence1, lastSentence2)
    }
}
