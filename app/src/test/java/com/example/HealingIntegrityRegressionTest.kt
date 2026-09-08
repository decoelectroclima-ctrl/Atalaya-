package com.example

import com.example.data.CheckinEntity
import com.example.data.RelapseEntity
import com.example.data.SoltarSettingsEntity
import com.example.ui.managers.JourneyStageEvaluator
import com.example.ui.managers.ProgressManager
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Tests de regresion para el bug de "sanacion falsa al dia 90".
 * Si alguno de estos falla, el bug (o uno equivalente) fue reintroducido.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class HealingIntegrityRegressionTest {

    @Test
    fun `90 dias de racha con score de vulnerabilidad alto NO debe llegar a la etapa visual maxima`() {
        val stage = ProgressManager.calculateProgressStage(streakDays = 90, vulnerabilityScore = 80)
        assertTrue("La etapa fue $stage pero deberia quedar baja con un score de vulnerabilidad malo", stage <= 3)
    }

    @Test
    fun `90 dias de racha con score de vulnerabilidad bajo SI puede llegar a la etapa maxima`() {
        val stage = ProgressManager.calculateProgressStage(streakDays = 90, vulnerabilityScore = 0)
        assertEquals(8, stage)
    }

    @Test
    fun `tiempo solo con score nulo queda limitado de forma conservadora`() {
        val stage = ProgressManager.calculateProgressStage(streakDays = 90, vulnerabilityScore = null)
        assertTrue(stage <= 5)
    }

    @Test
    fun `65 dias transcurridos con checkins malos NO debe subir a Life Coach`() = runBlocking {
        val settings = SoltarSettingsEntity(
            journeyStage = "RECOVERY",
            breakupDateTimestamp = System.currentTimeMillis() - (65L * 24 * 3600 * 1000)
        )
        val badCheckins = (1..6).map { i ->
            CheckinEntity(dateKey = "2026-08-0$i", pain = 8f, anxiety = 8f, rumination = 7f, autonomy = 2f, firstThoughts = "")
        }
        val result = JourneyStageEvaluator.evaluate(
            settings = settings,
            checkins = badCheckins,
            relapses = emptyList(),
            hasCompletedClosingRitual = true,
            recentFreeText = emptyList()
        )
        assertFalse("La app subio a Life Coach pese a datos de checkin malos - bug del dia 90 reintroducido", result.shouldUpgradeToLifeCoach)
    }

    @Test
    fun `65 dias transcurridos con checkins buenos sostenidos y sin IA disponible SI puede subir`() = runBlocking {
        val settings = SoltarSettingsEntity(
            journeyStage = "RECOVERY",
            breakupDateTimestamp = System.currentTimeMillis() - (65L * 24 * 3600 * 1000)
        )
        val goodCheckins = (1..6).map { i ->
            CheckinEntity(dateKey = "2026-08-0$i", pain = 1f, anxiety = 2f, rumination = 1f, autonomy = 8f, firstThoughts = "Me siento en paz.")
        }
        val result = JourneyStageEvaluator.evaluate(
            settings = settings,
            checkins = goodCheckins,
            relapses = emptyList(),
            hasCompletedClosingRitual = true,
            recentFreeText = emptyList()
        )
        assertTrue(result.shouldUpgradeToLifeCoach)
    }

    @Test
    fun `historial de checkins insuficiente NO debe subir aunque haya pasado el tiempo`() = runBlocking {
        val settings = SoltarSettingsEntity(
            journeyStage = "RECOVERY",
            breakupDateTimestamp = System.currentTimeMillis() - (120L * 24 * 3600 * 1000)
        )
        val result = JourneyStageEvaluator.evaluate(
            settings = settings,
            checkins = emptyList(),
            relapses = emptyList(),
            hasCompletedClosingRitual = true,
            recentFreeText = emptyList()
        )
        assertFalse("Subio con cero historial de checkins - requisito de datos saltado", result.shouldUpgradeToLifeCoach)
    }

    @Test
    fun `una recaida grave reciente bloquea el ascenso aunque todo lo demas este en verde`() = runBlocking {
        val settings = SoltarSettingsEntity(
            journeyStage = "RECOVERY",
            breakupDateTimestamp = System.currentTimeMillis() - (100L * 24 * 3600 * 1000)
        )
        val goodCheckins = (1..6).map { i ->
            CheckinEntity(dateKey = "2026-09-0$i", pain = 1f, anxiety = 1f, rumination = 1f, autonomy = 9f, firstThoughts = "")
        }
        val recentRelapse = listOf(
            RelapseEntity(
                timestamp = System.currentTimeMillis() - (2L * 24 * 3600 * 1000),
                whatHappened = "Test relapse", trigger = "Test", emotion = "Test",
                thought = "Test", behavior = "Test", consequence = "Test", learning = "Test",
                interpretation = "retroceso", isRestartingFromZero = true
            )
        )
        val result = JourneyStageEvaluator.evaluate(
            settings = settings,
            checkins = goodCheckins,
            relapses = recentRelapse,
            hasCompletedClosingRitual = true,
            recentFreeText = emptyList()
        )
        assertFalse(result.shouldUpgradeToLifeCoach)
    }
}
