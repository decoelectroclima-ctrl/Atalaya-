package com.example.widget

import android.content.Context
import android.content.SharedPreferences

data class SoltarWidgetConfig(
    val quoteSource: String = SOURCE_PROFILE,
    val customMantra: String = "",
    val themeMode: String = THEME_DARK, // "DARK", "LIGHT", "AUTO"
    val showDaysCounter: Boolean = true,
    val showFrameworkBadge: Boolean = true,
    val showPhaseBadge: Boolean = true,
    val showSubtext: Boolean = true,
    val showActionButtons: Boolean = true,
    val showSosButton: Boolean = true,
    val showCoachButton: Boolean = true,
    val showJournalButton: Boolean = true,
    val showCheckinButton: Boolean = true,
    val showConfigureButton: Boolean = true
) {
    companion object {
        const val SOURCE_PROFILE = "PROFILE"
        const val SOURCE_STOIC = "STOIC"
        const val SOURCE_PSYCHOLOGY = "PSYCHOLOGY"
        const val SOURCE_CATHOLIC = "CATHOLIC"
        const val SOURCE_CUSTOM = "CUSTOM"

        const val THEME_DARK = "DARK"
        const val THEME_LIGHT = "LIGHT"
        const val THEME_AUTO = "AUTO"
    }
}

object SoltarWidgetConfigManager {

    private const val PREFS_NAME = "com.example.widget.SoltarWidgetPrefs"
    private const val PREFIX_KEY = "widget_"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun loadConfig(context: Context, appWidgetId: Int): SoltarWidgetConfig {
        val prefs = getPrefs(context)
        val p = "$PREFIX_KEY${appWidgetId}_"

        return SoltarWidgetConfig(
            quoteSource = prefs.getString("${p}quote_source", SoltarWidgetConfig.SOURCE_PROFILE)
                ?: SoltarWidgetConfig.SOURCE_PROFILE,
            customMantra = prefs.getString("${p}custom_mantra", "") ?: "",
            themeMode = prefs.getString("${p}theme_mode", SoltarWidgetConfig.THEME_DARK)
                ?: SoltarWidgetConfig.THEME_DARK,
            showDaysCounter = prefs.getBoolean("${p}show_days", true),
            showFrameworkBadge = prefs.getBoolean("${p}show_framework_badge", true),
            showPhaseBadge = prefs.getBoolean("${p}show_phase_badge", true),
            showSubtext = prefs.getBoolean("${p}show_subtext", true),
            showActionButtons = prefs.getBoolean("${p}show_actions", true),
            showSosButton = prefs.getBoolean("${p}show_sos", true),
            showCoachButton = prefs.getBoolean("${p}show_coach", true),
            showJournalButton = prefs.getBoolean("${p}show_journal", true),
            showCheckinButton = prefs.getBoolean("${p}show_checkin", true),
            showConfigureButton = prefs.getBoolean("${p}show_configure", true)
        )
    }

    fun saveConfig(context: Context, appWidgetId: Int, config: SoltarWidgetConfig) {
        val prefs = getPrefs(context)
        val p = "$PREFIX_KEY${appWidgetId}_"

        prefs.edit()
            .putString("${p}quote_source", config.quoteSource)
            .putString("${p}custom_mantra", config.customMantra)
            .putString("${p}theme_mode", config.themeMode)
            .putBoolean("${p}show_days", config.showDaysCounter)
            .putBoolean("${p}show_framework_badge", config.showFrameworkBadge)
            .putBoolean("${p}show_phase_badge", config.showPhaseBadge)
            .putBoolean("${p}show_subtext", config.showSubtext)
            .putBoolean("${p}show_actions", config.showActionButtons)
            .putBoolean("${p}show_sos", config.showSosButton)
            .putBoolean("${p}show_coach", config.showCoachButton)
            .putBoolean("${p}show_journal", config.showJournalButton)
            .putBoolean("${p}show_checkin", config.showCheckinButton)
            .putBoolean("${p}show_configure", config.showConfigureButton)
            .apply()
    }

    fun deleteConfig(context: Context, appWidgetId: Int) {
        val prefs = getPrefs(context)
        val p = "$PREFIX_KEY${appWidgetId}_"

        prefs.edit()
            .remove("${p}quote_source")
            .remove("${p}custom_mantra")
            .remove("${p}theme_mode")
            .remove("${p}show_days")
            .remove("${p}show_framework_badge")
            .remove("${p}show_phase_badge")
            .remove("${p}show_subtext")
            .remove("${p}show_actions")
            .remove("${p}show_sos")
            .remove("${p}show_coach")
            .remove("${p}show_journal")
            .remove("${p}show_checkin")
            .remove("${p}show_configure")
            .apply()
    }
}
