package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.AdrianaDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SoltarAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    companion object {
        const val EXTRA_OPEN_ACTION = "EXTRA_OPEN_ACTION"
        const val ACTION_URGE_MODE = "URGE_MODE"
        const val ACTION_JOURNAL = "JOURNAL"
        const val ACTION_CHECKIN = "CHECKIN"

        private val quotes = listOf(
            "«No son las cosas las que atormentan, sino la opinión sobre ellas.» — Epicteto",
            "«La mejor revancha es no ser como quien te dañó.» — Marco Aurelio",
            "«El contacto cero es el espacio sagrado para recuperar tu eje.» — Silvia Congost",
            "«El duelo no se apura, se atraviesa con paciencia y verdad.» — Gabriel Rolón",
            "«Tu valor no disminuye por la incapacidad de alguien de verlo.»",
            "«Guarda tu corazón, porque de él mana la vida.» — Proverbios 4:23",
            "«Sé dueño de tus decisiones y custodio de tu paz hoy.» — Marco Aurelio"
        )

        fun notifyWidgetDataChanged(context: Context) {
            try {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val thisWidget = ComponentName(context, SoltarAppWidgetProvider::class.java)
                val allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget)
                for (id in allWidgetIds) {
                    updateAppWidget(context, appWidgetManager, id)
                }
            } catch (_: Exception) {}
        }

        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                var days = 14
                var userName = "Viajero"
                try {
                    val db = AdrianaDatabase.getDatabase(context)
                    val settings = db.soltarSettingsDao().getSettingsOnce()
                    if (settings != null) {
                        val diffMillis = System.currentTimeMillis() - settings.breakupDateTimestamp
                        days = (TimeUnit.MILLISECONDS.toDays(diffMillis)).coerceAtLeast(0).toInt()
                        if (settings.userName.isNotBlank()) {
                            userName = settings.userName
                        }
                    }
                } catch (_: Exception) {}

                val phaseBadge = when {
                    days < 7 -> "⚡ Desintoxicación"
                    days < 30 -> "🛡️ Soberanía"
                    days < 60 -> "💡 Claridad"
                    else -> "✨ Reconstrucción"
                }

                val quoteIndex = (days % quotes.size).coerceIn(0, quotes.size - 1)
                val quote = quotes[quoteIndex]

                val views = RemoteViews(context.packageName, R.layout.widget_soltar_layout).apply {
                    setTextViewText(R.id.widget_days_count, days.toString())
                    setTextViewText(R.id.widget_phase_badge, phaseBadge)
                    setTextViewText(R.id.widget_days_label, if (days == 1) "DÍA DE SOBERANÍA" else "DÍAS DE SOBERANÍA")
                    setTextViewText(R.id.widget_days_subtext, "$userName • Cuidando tu paz")
                    setTextViewText(R.id.widget_quote_text, quote)

                    // 1. Root Intent -> Open MainActivity normally
                    val rootIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                    val rootPendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        rootIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    setOnClickPendingIntent(R.id.widget_root, rootPendingIntent)

                    // 2. SOS Button -> Open MainActivity with SOS / Urge Mode
                    val sosIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(EXTRA_OPEN_ACTION, ACTION_URGE_MODE)
                    }
                    val sosPendingIntent = PendingIntent.getActivity(
                        context,
                        101,
                        sosIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    setOnClickPendingIntent(R.id.widget_btn_sos, sosPendingIntent)

                    // 3. Journal Button -> Open MainActivity with Journal Dialog
                    val journalIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(EXTRA_OPEN_ACTION, ACTION_JOURNAL)
                    }
                    val journalPendingIntent = PendingIntent.getActivity(
                        context,
                        102,
                        journalIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    setOnClickPendingIntent(R.id.widget_btn_journal, journalPendingIntent)
                }

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
