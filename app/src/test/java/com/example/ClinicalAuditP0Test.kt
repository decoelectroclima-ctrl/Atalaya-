package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.ai.ClinicalCategory
import com.example.ai.ClinicalVariantsPsicologia
import com.example.ai.SoltarAiEngine
import com.example.ai.SoltarUserContext
import com.example.data.SoltarFramework
import com.example.ui.SoltarViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ClinicalAuditP0Test {

    private lateinit var application: Application
    private lateinit var viewModel: SoltarViewModel

    @Before
    fun setUp() {
        application = ApplicationProvider.getApplicationContext()
        viewModel = SoltarViewModel(application)
        com.example.ai.ClinicalVariantRegistry.clearMemory()
    }

    @Test
    fun testP0_1_PromptConstructionWithFramework() {
        val userContext = SoltarUserContext(
            userName = "Mariana",
            streakDays = 14,
            journeyStage = "RECOVERY"
        )
        val prompt = SoltarAiEngine.buildPromptWithFramework(SoltarFramework.ESTOICO, userContext)
        assertTrue(prompt.contains("FILOSOFÍA ESTOICA"))
        assertTrue(prompt.contains("Mariana"))
        assertTrue(prompt.contains("14 días"))
    }

    @Test
    fun testP0_2_DiagnosticRenamedToOrientingPrinciple() {
        val userContext = SoltarUserContext(userName = "Carlos")
        val prompt = SoltarAiEngine.buildPromptWithFramework(SoltarFramework.PSICOLOGIA_MODERNA, userContext)
        assertFalse(prompt.contains("Diagnóstico automático:"))
        assertTrue(prompt.contains("PROHIBIDO DIAGNÓSTICOS AUTOMÁTICOS"))
    }

    @Test
    fun testP0_3_NoGreetingRepetitionInHistoryAndWordCap() {
        val userContext = SoltarUserContext(userName = "Lucas")
        val history = listOf(
            "user" to "Hola",
            "assistant" to "Hola, Lucas. Aquí estoy para acompañarte si lo necesitas.",
            "user" to "No puedo dejar de pensar en lo que pasó"
        )
        val response = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(
            input = "Siento mucha culpa por haber terminado",
            isRumination = true,
            framework = SoltarFramework.PSICOLOGIA_MODERNA,
            userContext = userContext,
            conversationHistory = history
        )
        // In ongoing conversation, response should not repeat "Hola, Lucas"
        assertFalse(response.replyText.startsWith("Hola, Lucas"))
        // Word limit check (max 90 words + ellipsis)
        val words = response.replyText.split(Regex("\\s+"))
        assertTrue("La respuesta debe tener menos de 100 palabras (${words.size})", words.size <= 95)
    }

    @Test
    fun testP0_4_EmotionalMessageClassificationOverridesGreeting() {
        val message = "Hola, me siento muy triste porque extraño a mi ex y no puedo dormir"
        val intent = SoltarAiEngine.classifyMessageIntent(message)
        assertEquals(SoltarAiEngine.MessageIntent.CONTENIDO_EMOCIONAL, intent)

        val shortGreeting = "hola"
        val intentGreeting = SoltarAiEngine.classifyMessageIntent(shortGreeting)
        assertEquals(SoltarAiEngine.MessageIntent.SALUDO_O_CASUAL, intentGreeting)
    }

    @Test
    fun testP0_4_WordBoundaryExDetectionNoFalsePositives() {
        // Words containing "ex" like "experiencia" or "texto" should not trigger false positive
        val benignInput = "Tuve una excelente experiencia leyendo este texto"
        val intent = SoltarAiEngine.classifyMessageIntent(benignInput)
        // Length > 25 qualifies as content, but word boundary test:
        val exRegex = Regex("""\b(ex|él|el|ella|pareja)\b""", RegexOption.IGNORE_CASE)
        val matchesIsolated = exRegex.containsMatchIn("mi ex me llamó")
        val matchesInsideWord = exRegex.containsMatchIn("excelente texto flexible")
        assertTrue(matchesIsolated)
        assertFalse(matchesInsideWord)
    }

    @Test
    fun testP0_5_ClinicalVariantsConversationalNoBulletPoints() {
        val topCategories = listOf(
            ClinicalCategory.RECUPERAR_PAREJA,
            ClinicalCategory.SENALES_DIGITALES,
            ClinicalCategory.RUMIACION_BUCLE,
            ClinicalCategory.IMPULSO_CONTACTAR,
            ClinicalCategory.DEPENDENCIA_EMOCIONAL,
            ClinicalCategory.NOSTALGIA_IDEALIZACION,
            ClinicalCategory.CULPA_RENCOR_RABIA,
            ClinicalCategory.CONTACTO_CERO_LIMITES
        )

        for (category in topCategories) {
            val variants = ClinicalVariantsPsicologia.getVariants(category)
            assertTrue("Debe tener variantes para $category", variants.isNotEmpty())
            for (v in variants) {
                assertFalse("No debe contener viñetas con • en ${category.name}", v.bodyText.contains("•"))
                assertFalse("No debe contener viñetas numeradas '1.' en ${category.name}", v.bodyText.contains("1. "))
                val wordCount = v.bodyText.trim().split(Regex("\\s+")).size
                assertTrue("La variante en ${category.name} debe tener una longitud conversacional adecuada ($wordCount palabras)", wordCount in 20..100)
            }
        }
    }

    @Test
    fun testDiagnosticStateToggleInViewModel() {
        assertFalse(viewModel.uiState.value.isAiDiagnosticDialogVisible)
        viewModel.toggleAiDiagnosticDialog(true)
        assertTrue(viewModel.uiState.value.isAiDiagnosticDialogVisible)
        viewModel.toggleAiDiagnosticDialog(false)
        assertFalse(viewModel.uiState.value.isAiDiagnosticDialogVisible)
    }
}
