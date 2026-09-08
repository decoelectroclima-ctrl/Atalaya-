package com.example.ui.managers

object ProgressManager {
    private const val MAX_STAGES = 8
    private const val TARGET_DAYS = 90

    fun calculateProgressStage(streakDays: Int, vulnerabilityScore: Int?): Int {
        val timeProgress = (streakDays.toFloat() / TARGET_DAYS).coerceIn(0f, 1f)
        val timeStage = (timeProgress * (MAX_STAGES - 1)).toInt() + 1
        if (vulnerabilityScore == null) return timeStage.coerceAtMost(5)
        val healingFromScore = ((100 - vulnerabilityScore).toFloat() / 100f).coerceIn(0f, 1f)
        val scoreStage = (healingFromScore * (MAX_STAGES - 1)).toInt() + 1
        return minOf(timeStage, scoreStage)
    }

    fun getProgressRatio(streakDays: Int, vulnerabilityScore: Int?): Float {
        val stage = calculateProgressStage(streakDays, vulnerabilityScore)
        return (stage - 1).toFloat() / (MAX_STAGES - 1)
    }
}
