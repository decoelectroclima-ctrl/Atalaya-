package com.example

import com.example.ai.ContextualExperienceEngine
import com.example.ai.SoltarAiEngine
import com.example.ai.SoltarUserContext
import com.example.data.SoltarSettingsEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class SoltarContextualPersonalizationTest {

    @Test
    fun testParentalContactPersonalization() {
        val profileWithChildren = SoltarSettingsEntity(id = 1, hasChildren = true)
        val rec = ContextualExperienceEngine.analyzeContext(profileWithChildren)
        
        assertEquals("PARENTAL", rec.contactCategory)
        assertTrue(rec.profileTypeDescription.contains("Parental"))
        assertTrue(rec.priorityToolTitle.contains("Parental"))
    }

    @Test
    fun testWorkContactPersonalization() {
        val profileWork = SoltarSettingsEntity(id = 1, practicals = "compartimos trabajo y oficina")
        val rec = ContextualExperienceEngine.analyzeContext(profileWork)
        
        assertEquals("WORK", rec.contactCategory)
        assertTrue(rec.profileTypeDescription.contains("Laboral"))
        assertTrue(rec.priorityToolTitle.contains("Profesional"))
    }

    @Test
    fun testCohabitationContactPersonalization() {
        val profileCohab = SoltarSettingsEntity(id = 1, cohabitation = true)
        val rec = ContextualExperienceEngine.analyzeContext(profileCohab)
        
        assertEquals("COHABITATION", rec.contactCategory)
        assertTrue(rec.profileTypeDescription.contains("Convivencia"))
        assertTrue(rec.strategySummary.contains("Convivencia Táctica"))
    }

    @Test
    fun testPracticalContactPersonalization() {
        val profilePractical = SoltarSettingsEntity(id = 1, practicals = "negocio y piso en alquiler")
        val rec = ContextualExperienceEngine.analyzeContext(profilePractical)
        
        assertEquals("PRACTICAL", rec.contactCategory)
        assertTrue(rec.profileTypeDescription.contains("Obligaciones Prácticas"))
        assertTrue(rec.priorityToolTitle.contains("Obligaciones"))
    }

    @Test
    fun testMutualDecisionPersonalization() {
        val profileMutual = SoltarSettingsEntity(id = 1, decisionMaker = "MUTUO")
        val rec = ContextualExperienceEngine.analyzeContext(profileMutual)
        
        assertTrue(rec.profileTypeDescription.contains("Decisión Mutua"))
        assertTrue(rec.bannerMessage.contains("ruptura acordada") || rec.bannerMessage.contains("ambivalencia"))
    }

    @Test
    fun testAnticipatedGriefPersonalization() {
        val withGrief = SoltarSettingsEntity(id = 2, anticipatedGrief = "SI_LLEVABA_TIEMPO_DECEPCIONANDOME")
        val recYes = ContextualExperienceEngine.analyzeContext(withGrief)

        assertTrue(recYes.profileTypeDescription.contains("Duelo Anticipado") || recYes.bannerMessage.contains("desgaste"))
    }

    @Test
    fun testDecisionMakerPersonalization() {
        val otherEnded = SoltarSettingsEntity(id = 1, decisionMaker = "OTRA_PERSONA")
        val selfEnded = SoltarSettingsEntity(id = 2, decisionMaker = "YO_MISMO_DECIDI")

        val recOther = ContextualExperienceEngine.analyzeContext(otherEnded)
        val recSelf = ContextualExperienceEngine.analyzeContext(selfEnded)

        assertTrue(recSelf.profileTypeDescription.contains("Decisión de Terminar") || recSelf.bannerMessage.contains("culpa"))
        assertNotEquals(recOther.profileTypeDescription, recSelf.profileTypeDescription)
    }

    @Test
    fun testInfidelityPersonalization() {
        val normalBreakup = SoltarSettingsEntity(id = 1, breakupReason = "desgaste")
        val infidelityBreakup = SoltarSettingsEntity(id = 2, breakupReason = "infidelidad y traición")

        val recNormal = ContextualExperienceEngine.analyzeContext(normalBreakup)
        val recInfidelity = ContextualExperienceEngine.analyzeContext(infidelityBreakup)

        assertTrue(recInfidelity.profileTypeDescription.contains("Infidelidad") || recInfidelity.bannerMessage.contains("traición"))
        assertNotEquals(recNormal.priorityToolTitle, recInfidelity.priorityToolTitle)
    }

    @Test
    fun testBreakupCyclesPersonalization() {
        val firstTime = SoltarSettingsEntity(id = 1, previousBreakupsCount = 0)
        val multipleCycles = SoltarSettingsEntity(id = 2, previousBreakupsCount = 4)

        val recFirst = ContextualExperienceEngine.analyzeContext(firstTime)
        val recCycles = ContextualExperienceEngine.analyzeContext(multipleCycles)

        assertTrue(recCycles.profileTypeDescription.contains("Ciclo") || recCycles.bannerMessage.contains("ciclo"))
        assertNotEquals(recFirst.strategySummary, recCycles.strategySummary)
    }

    @Test
    fun testMutationTestingRigorousDifferentiation() {
        // Mutation testing requirement: Ensure modifying key attributes produces distinct structural outputs
        val p1 = SoltarSettingsEntity(id = 1, hasChildren = true)
        val p2 = SoltarSettingsEntity(id = 2, cohabitation = true)
        val p3 = SoltarSettingsEntity(id = 3, previousBreakupsCount = 3)

        val r1 = ContextualExperienceEngine.analyzeContext(p1)
        val r2 = ContextualExperienceEngine.analyzeContext(p2)
        val r3 = ContextualExperienceEngine.analyzeContext(p3)

        assertNotEquals(r1.priorityToolTitle, r2.priorityToolTitle)
        assertNotEquals(r2.priorityToolTitle, r3.priorityToolTitle)
        assertNotEquals(r1.strategySummary, r3.strategySummary)
    }

    @Test
    fun testNonGenericExtremeProfilesQueryComparison() = runBlocking {
        val profileA = SoltarUserContext(
            relDuration = "3_meses",
            hasChildren = false,
            decisionMaker = "OTRA_PERSONA",
            breakupReason = "desgaste",
            previousBreakupsCount = 0
        )

        val profileB = SoltarUserContext(
            relDuration = "mas_5_anos",
            hasChildren = true,
            decisionMaker = "MUTUO",
            breakupReason = "infidelidad",
            previousBreakupsCount = 3
        )

        val query = "Tengo ganas de escribirle. ¿Qué hago?"

        val responseA = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(
            query, false, com.example.data.SoltarFramework.PSICOLOGIA_MODERNA, profileA
        )

        val responseB = SoltarAiEngine.executeAdvancedLocalClinicalReasoning(
            query, false, com.example.data.SoltarFramework.PSICOLOGIA_MODERNA, profileB
        )

        assertNotNull(responseA.replyText)
        assertNotNull(responseB.replyText)
        assertTrue(responseA.replyText.isNotBlank())
        assertTrue(responseB.replyText.isNotBlank())
        assertNotEquals(profileA.toClinicalSummary(), profileB.toClinicalSummary())
    }
}

