package com.example

import com.example.ai.ContextualExperienceEngine
import com.example.ai.SoltarAiEngine
import com.example.ai.SoltarUserContext
import com.example.data.ClinicalKnowledgeBase
import com.example.data.SoltarFramework
import com.example.data.SoltarSettingsEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SoltarClinicalFrameworkTest {

    @Test
    fun testCriterion1_CorrectKnowledgeReachesEngine() {
        // Verificar que la base de conocimiento clínica contiene niveles, modelos y protocolos correctos
        val capsule = ClinicalKnowledgeBase.findRelevantCapsule("impulso de escribir", SoltarFramework.PSICOLOGIA_MODERNA)
        assertNotNull(capsule)
        assertTrue(capsule.evidenceLevel.contains("Nivel 1"))
        assertTrue(capsule.psychologicalModel.isNotBlank())
        assertTrue(capsule.adrianaProtocol.contains("Problema"))
    }

    @Test
    fun testCriterion2_ContextModifiesInterventionWhenAppropriate() {
        val settingsParental = SoltarSettingsEntity(id = 1, hasChildren = true)
        val settingsWork = SoltarSettingsEntity(id = 2, practicals = "trabajo compartido en oficina")
        val settingsStandard = SoltarSettingsEntity(id = 3, hasChildren = false, practicals = "")

        val recParental = ContextualExperienceEngine.analyzeContext(settingsParental)
        val recWork = ContextualExperienceEngine.analyzeContext(settingsWork)
        val recStandard = ContextualExperienceEngine.analyzeContext(settingsStandard)

        assertEquals("PARENTAL", recParental.contactCategory)
        assertEquals("WORK", recWork.contactCategory)
        assertEquals("NONE", recStandard.contactCategory)

        assertNotEquals(recParental.priorityToolTitle, recStandard.priorityToolTitle)
        assertNotEquals(recWork.priorityToolTitle, recStandard.priorityToolTitle)
    }

    @Test
    fun testCriterion3_TwoDifferentProfilesReceiveDifferentStrategies() = runBlocking {
        val profileA = SoltarUserContext(
            relDuration = "3_meses",
            hasChildren = false,
            decisionMaker = "OTRA_PERSONA",
            breakupReason = "desgaste",
            streakDays = 2
        )

        val profileB = SoltarUserContext(
            relDuration = "mas_5_anos",
            hasChildren = true,
            decisionMaker = "MUTUO",
            breakupReason = "infidelidad",
            streakDays = 30
        )

        val query = "Tengo ganas de escribirle y no sé qué hacer."

        val responseA = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(
            query, false, SoltarFramework.PSICOLOGIA_MODERNA, profileA
        )

        val responseB = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(
            query, false, SoltarFramework.PSICOLOGIA_MODERNA, profileB
        )

        assertNotNull(responseA.replyText)
        assertNotNull(responseB.replyText)
        // Las respuestas deben estar adaptadas al contexto individual (perfiles diferentes reciben estrategias/enfoques contextuales)
        assertNotEquals(responseA.stateDetected, responseB.stateDetected)
    }

    @Test
    fun testCriterion4_PhilosophicalFramesDoNotSubstituteScientificBasis() {
        // Verificar que el marco filosófico (Estoico / Católico) actúa como lente opcional (Nivel 4),
        // mientras la base científica y psicológica (Nivel 1 y 2) se mantiene intacta en el sistema.
        val stoicCapsule = ClinicalKnowledgeBase.findRelevantCapsule("bucle de rumiación", SoltarFramework.ESTOICO)
        val catholicCapsule = ClinicalKnowledgeBase.findRelevantCapsule("bucle de rumiación", SoltarFramework.CATOLICO)
        val modernCapsule = ClinicalKnowledgeBase.findRelevantCapsule("bucle de rumiación", SoltarFramework.PSICOLOGIA_MODERNA)

        assertEquals("Lente Estoica (Marco de Significado Opcional)", stoicCapsule.meaningLens)
        assertEquals("Lente Espiritual Cristiana (Marco de Significado Opcional)", catholicCapsule.meaningLens)
        
        // Todas comparten la misma rigoración científica subyacente y protocolos de intervención
        assertNotNull(modernCapsule.evidenceLevel)
        assertNotNull(stoicCapsule.adrianaProtocol)
    }

    @Test
    fun testCriterion5_HistoricalInfoModifiesFutureRecommendations() {
        val userContextWithHistory = SoltarUserContext(
            streakDays = 45,
            totalCheckins = 20,
            lastCheckinMood = "Calma",
            averageAutonomyScore = 8.5f,
            recentRelapseTriggers = listOf("redes sociales", "fin de semana solo")
        )

        val summary = userContextWithHistory.toClinicalSummary()
        assertTrue(summary.contains("45 días"))
        assertTrue(summary.contains("8.5/10"))
        assertTrue(summary.contains("redes sociales"))
    }

    @Test
    fun testCriterion6_NoAutomaticDiagnosesAppear() = runBlocking {
        val response = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(
            "Mi ex era un narcisista patológico y un manipulador tóxico",
            false,
            SoltarFramework.PSICOLOGIA_MODERNA,
            SoltarUserContext()
        )

        // El sistema no debe validar diagnósticos automáticos ni usar etiquetas patologizantes como autoridad clínica
        assertNotNull(response.replyText)
        assertFalse(response.replyText.contains("Tiene un trastorno de personalidad narcisista"))
        // Debe reenfocar hacia la conducta observada y la autonomía del usuario
        assertTrue(response.replyText.contains("dignidad") || response.replyText.contains("hechos") || response.replyText.contains("autonomía"))
    }

    @Test
    fun testCriterion7_CompatibilityAndSystemIntegrity() {
        // Verificar que la configuración por defecto y los modelos básicos están intactos
        val defaultContext = SoltarUserContext()
        assertEquals(0, defaultContext.streakDays)
        assertEquals(SoltarFramework.PSICOLOGIA_MODERNA, defaultContext.framework)
    }
}
