package com.example.ui.managers

import com.example.data.SoltarSettingsEntity

object ProgressManager {
    // 8 stages for Landscape and Kintsugi Heart
    private const val MAX_STAGES = 8
    
    // Target days to reach "fully recovered" state (e.g., 90 days for full Kintsugi/Clear sky)
    private const val TARGET_DAYS = 90

    fun calculateProgressStage(streakDays: Int): Int {
        val progress = (streakDays.toFloat() / TARGET_DAYS).coerceIn(0f, 1f)
        return (progress * (MAX_STAGES - 1)).toInt() + 1 // Returns 1 to 8
    }

    fun getProgressRatio(streakDays: Int): Float {
        return (streakDays.toFloat() / TARGET_DAYS).coerceIn(0f, 1f)
    }
}
