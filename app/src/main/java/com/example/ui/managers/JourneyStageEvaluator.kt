package com.example.ui.managers

import com.example.data.CheckinEntity
import com.example.data.RelapseEntity
import com.example.data.SoltarSettingsEntity

object JourneyStageEvaluator {
    data class EvaluationResult(
        val shouldUpgradeToLifeCoach: Boolean,
        val shouldPromptRecoveryRegression: Boolean,
        val transitionMessage: String?
    )

    fun evaluate(
        settings: SoltarSettingsEntity?,
        checkins: List<CheckinEntity>,
        relapses: List<RelapseEntity>,
        hasCompletedClosingRitual: Boolean
    ): EvaluationResult {
        if (settings == null) return EvaluationResult(false, false, null)

        val currentStage = settings.journeyStage // "RECOVERY" or "LIFE_COACH"
        val now = System.currentTimeMillis()

        if (currentStage == "RECOVERY") {
            // Conditions for upgrading from RECOVERY to LIFE_COACH automatically:
            // 1. Sustained low vulnerability / pain over recent checkins (e.g., at least 5 checkins with low average pain/anxiety)
            val recentCheckins = checkins.take(14)
            val hasEnoughHistory = recentCheckins.size >= 3
            val lowPainSustained = recentCheckins.isEmpty() || (recentCheckins.map { it.pain + it.anxiety + it.rumination }.average() < 12.0)

            // 2. Completed Closing Ritual
            val closingRitualDone = hasCompletedClosingRitual

            // 3. No relapse marked as "retroceso" or restarting from zero in the last 21-28 days (3-4 weeks)
            val recentRetrogradeRelapse = relapses.any { r ->
                (now - r.timestamp) < (28L * 24 * 3600 * 1000) && (r.interpretation == "retroceso" || r.isRestartingFromZero)
            }

            // 4. Autonomy stable or positive in recent checkins (average autonomy >= 5)
            val goodAutonomy = recentCheckins.isEmpty() || (recentCheckins.map { it.autonomy }.average() >= 5.0)

            // Or if time since breakup > 60 days and closing ritual done and no recent retrograde relapse
            val timeElapsed = (now - settings.breakupDateTimestamp) > (60L * 24 * 3600 * 1000)

            if (closingRitualDone && !recentRetrogradeRelapse && ((hasEnoughHistory && lowPainSustained && goodAutonomy) || timeElapsed)) {
                return EvaluationResult(
                    shouldUpgradeToLifeCoach = true,
                    shouldPromptRecoveryRegression = false,
                    transitionMessage = "Has recorrido un largo camino. Ahora podemos trabajar en quién quieres ser."
                )
            }
        } else if (currentStage == "LIFE_COACH") {
            // Regression rule: If user has a severe relapse recently while in LIFE_COACH, prompt if they want to return to Recovery support
            val severeRecentRelapse = relapses.any { r ->
                (now - r.timestamp) < (7L * 24 * 3600 * 1000) && (r.interpretation == "retroceso" || r.isRestartingFromZero)
            }
            if (severeRecentRelapse) {
                return EvaluationResult(
                    shouldUpgradeToLifeCoach = false,
                    shouldPromptRecoveryRegression = true,
                    transitionMessage = null
                )
            }
        }

        return EvaluationResult(false, false, null)
    }
}
