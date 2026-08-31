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
import com.example.data.SoltarFramework
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        const val ACTION_COACH = "COACH"

        private val stoicQuotes = listOf(
            "«No son las cosas las que atormentan, sino la opinión sobre ellas.» — Epicteto",
            "«La mejor revancha es no ser como quien te dañó.» — Marco Aurelio",
            "«El mayor imperio es el imperio sobre uno mismo.» — Séneca",
            "«Borra la vana imaginación; frena el impulso; mantén el mando de tu mente.» — Marco Aurelio",
            "«Pide al impulso que espere; la demora es el mejor remedio para la pasión ciega.» — Séneca",
            "«Nunca digas 'lo perdí', sino 'lo he devuelto'.» — Epicteto",
            "«El fuego prueba al oro; la adversidad forja a los valientes.» — Séneca"
        )

        private val psychologyQuotes = listOf(
            "«El contacto cero no es para que el otro vuelva; es el quirófano donde sanas.» — Silvia Congost",
            "«El duelo no se cura con olvido, sino atravesando la verdad con dignidad.» — Gabriel Rolón",
            "«Puedes seguir queriendo a alguien y dejar de organizar tu vida alrededor suyo.»",
            "«La abstinencia duele en el cuerpo, pero la insistencia destruye tu autoestima.»",
            "«Tus pensamientos son eventos pasajeros, no órdenes que debas obedecer.» — Terapia ACT",
            "«Cuando ya no podemos cambiar la situación, el reto es cambiarnos a nosotros mismos.» — Viktor Frankl",
            "«Cuidar tu sueño y tu cuerpo es el primer paso para desinflamar el dolor emocional.»"
        )

        private val catholicQuotes = listOf(
            "«Por encima de todo, guarda tu corazón, porque de él brota la vida.» — Proverbios 4:23",
            "«Hay un tiempo para abrazar y un tiempo para abstenerse de abrazar.» — Eclesiastés 3:5",
            "«Él sana a los quebrantados de corazón y venda sus heridas.» — Salmo 147:3",
            "«Nada te turbe, nada te espante; todo se pasa, la paz interior permanece.» — Santa Teresa de Jesús",
            "«Dios no nos dio espíritu de cobardía, sino de poder, amor y dominio propio.» — 2 Timoteo 1:7",
            "«El perdón no justifica el daño: desata al prisionero y descubre que eras tú.» — San Agustín",
            "«En el desierto interior se purifica el alma para una madurez mayor.» — San Juan de la Cruz"
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
                var totalAccumulatedDays = 14
                var userName = "Viajero"
                var framework = SoltarFramework.PSICOLOGIA_MODERNA
                var appThemeMode = "LIGHT"

                val config = SoltarWidgetConfigManager.loadConfig(context, appWidgetId)

                try {
                    val db = AdrianaDatabase.getDatabase(context)
                    val settings = db.soltarSettingsDao().getSettingsOnce()
                    if (settings != null) {
                        val currentTime = System.currentTimeMillis()
                        val diffMillis = (currentTime - settings.breakupDateTimestamp).coerceAtLeast(0L)
                        days = (TimeUnit.MILLISECONDS.toDays(diffMillis)).toInt()

                        val initialStart = if (settings.initialStartDateTimestamp > 0) settings.initialStartDateTimestamp else settings.breakupDateTimestamp
                        val totalDiffMillis = (currentTime - initialStart).coerceAtLeast(0L)
                        totalAccumulatedDays = (TimeUnit.MILLISECONDS.toDays(totalDiffMillis)).toInt()

                        if (settings.userName.isNotBlank()) {
                            userName = settings.userName
                        }
                        framework = try {
                            SoltarFramework.valueOf(settings.preferredFramework)
                        } catch (_: Exception) {
                            SoltarFramework.PSICOLOGIA_MODERNA
                        }
                        if (settings.themeMode.isNotBlank()) {
                            appThemeMode = settings.themeMode
                        }
                    }
                } catch (_: Exception) {}

                val isDark = when (config.themeMode) {
                    SoltarWidgetConfig.THEME_DARK -> true
                    SoltarWidgetConfig.THEME_LIGHT -> false
                    else -> appThemeMode.equals("DARK", ignoreCase = true)
                }

                val bgRes = if (isDark) R.drawable.widget_background_dark else R.drawable.widget_background_light
                val alphaVal = when (config.backgroundTransparency) {
                    SoltarWidgetConfig.BG_SOLID -> 1.0f
                    SoltarWidgetConfig.BG_SEMI -> 0.75f
                    SoltarWidgetConfig.BG_TRANSPARENT -> 0.4f
                    else -> 1.0f
                }

                val phaseBadge = when {
                    days < 7 -> "⚡ Desintoxicación"
                    days < 30 -> "🛡️ Soberanía"
                    days < 60 -> "💡 Claridad"
                    else -> "✨ Reconstrucción"
                }

                val frameworkBadgeText = when (framework) {
                    SoltarFramework.ESTOICO -> "🏛️ Estoico"
                    SoltarFramework.CATOLICO -> "✝️ Católico"
                    SoltarFramework.PSICOLOGIA_MODERNA -> "🧠 Psicología"
                }

                val quoteList = when (framework) {
                    SoltarFramework.ESTOICO -> stoicQuotes
                    SoltarFramework.CATOLICO -> catholicQuotes
                    SoltarFramework.PSICOLOGIA_MODERNA -> psychologyQuotes
                }

                val quoteIndex = (days % quoteList.size).coerceIn(0, quoteList.size - 1)
                val quote = quoteList[quoteIndex]

                val views = RemoteViews(context.packageName, R.layout.widget_soltar_layout).apply {
                    setInt(R.id.widget_root, "setBackgroundResource", bgRes)
                    setFloat(R.id.widget_root, "setAlpha", alphaVal)

                    val daysCountColor = if (isDark) android.graphics.Color.parseColor("#F8FAFC") else android.graphics.Color.parseColor("#0F172A")
                    val primaryTextColor = if (isDark) android.graphics.Color.parseColor("#F8FAFC") else android.graphics.Color.parseColor("#0F172A")
                    val secondaryTextColor = if (isDark) android.graphics.Color.parseColor("#94A3B8") else android.graphics.Color.parseColor("#475569")
                    val amberColor = if (isDark) android.graphics.Color.parseColor("#E5A93C") else android.graphics.Color.parseColor("#B45309")

                    setTextColor(R.id.widget_title, amberColor)
                    setTextColor(R.id.widget_framework_badge, amberColor)
                    setTextColor(R.id.widget_days_count, daysCountColor)
                    setTextColor(R.id.widget_days_label, primaryTextColor)
                    setTextColor(R.id.widget_days_subtext, secondaryTextColor)
                    setTextColor(R.id.widget_quote_text, secondaryTextColor)

                    setTextViewText(R.id.widget_days_count, days.toString())
                    setTextViewText(R.id.widget_phase_badge, phaseBadge)
                    setTextViewText(R.id.widget_framework_badge, frameworkBadgeText)
                    setTextViewText(R.id.widget_days_label, "DÍAS DE CONTACTO CERO")
                    setTextViewText(R.id.widget_days_subtext, "$totalAccumulatedDays días totales en tu proceso • $userName")
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

                    // 3. Coach ADRIANA Button -> Open MainActivity directly with Coach Sheet
                    val coachIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(EXTRA_OPEN_ACTION, ACTION_COACH)
                    }
                    val coachPendingIntent = PendingIntent.getActivity(
                        context,
                        102,
                        coachIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    setOnClickPendingIntent(R.id.widget_btn_coach, coachPendingIntent)

                    // 4. Journal Button -> Open MainActivity with Journal Dialog
                    val journalIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(EXTRA_OPEN_ACTION, ACTION_JOURNAL)
                    }
                    val journalPendingIntent = PendingIntent.getActivity(
                        context,
                        103,
                        journalIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    setOnClickPendingIntent(R.id.widget_btn_journal, journalPendingIntent)

                    // 5. Check-in Button -> Open MainActivity with Check-in
                    val checkinIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        putExtra(EXTRA_OPEN_ACTION, ACTION_CHECKIN)
                    }
                    val checkinPendingIntent = PendingIntent.getActivity(
                        context,
                        104,
                        checkinIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    setOnClickPendingIntent(R.id.widget_btn_checkin, checkinPendingIntent)
                }

                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
