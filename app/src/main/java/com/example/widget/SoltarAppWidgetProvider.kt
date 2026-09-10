package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
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

        private fun drawKintsugiHeartBitmap(sizePx: Int, progressStage: Int, vulnerabilityScore: Int): Bitmap {
            val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val w = sizePx.toFloat()
            val h = sizePx.toFloat()

            val goldColor = Color.parseColor("#E7A94F") // coincide con RawDarkAmber en Color.kt
            val darkCrackColor = Color.parseColor("#2C3E50")
            val heartColor = Color.parseColor("#E57373")

            val heartPath = Path().apply {
                moveTo(w / 2f, h * 0.8f)
                cubicTo(0f, h * 0.3f, w * 0.15f, 0f, w / 2f, h * 0.35f)
                cubicTo(w * 0.85f, 0f, w, h * 0.3f, w / 2f, h * 0.8f)
            }

            val alphaMultiplier = if (vulnerabilityScore >= 70) 0.8f else 1.0f
            val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = heartColor
                alpha = (((0.25f + (progressStage * 0.08f)) * alphaMultiplier) * 255).toInt().coerceIn(0, 255)
                style = Paint.Style.FILL
            }
            canvas.drawPath(heartPath, fillPaint)

            val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                style = Paint.Style.STROKE
                strokeWidth = 3.5f
            }
            canvas.drawPath(heartPath, strokePaint)

            val crack1Start = floatArrayOf(w * 0.5f, h * 0.25f)
            val crack1End = floatArrayOf(w * 0.5f, h * 0.75f)
            val crack2Start = floatArrayOf(w * 0.3f, h * 0.4f)
            val crack2End = floatArrayOf(w * 0.65f, h * 0.55f)
            val crack3Start = floatArrayOf(w * 0.4f, h * 0.6f)
            val crack3End = floatArrayOf(w * 0.7f, h * 0.35f)
            val crack4Start = floatArrayOf(w * 0.25f, h * 0.3f)
            val crack4End = floatArrayOf(w * 0.4f, h * 0.5f)

            val crackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                strokeCap = Paint.Cap.ROUND
            }

            if (progressStage == 1) {
                crackPaint.color = darkCrackColor
                crackPaint.strokeWidth = 3f
                canvas.drawLine(crack1Start[0], crack1Start[1], crack1End[0], crack1End[1], crackPaint)
                crackPaint.strokeWidth = 2.5f
                canvas.drawLine(crack2Start[0], crack2Start[1], crack2End[0], crack2End[1], crackPaint)
            } else {
                crackPaint.color = goldColor
                crackPaint.strokeWidth = 2f + (progressStage * 0.4f)
                canvas.drawLine(crack1Start[0], crack1Start[1], crack1End[0], crack1End[1], crackPaint)
                if (progressStage >= 3) {
                    crackPaint.strokeWidth = 2f + (progressStage * 0.3f)
                    canvas.drawLine(crack2Start[0], crack2Start[1], crack2End[0], crack2End[1], crackPaint)
                }
                if (progressStage >= 5) {
                    crackPaint.strokeWidth = 2.5f + (progressStage * 0.3f)
                    canvas.drawLine(crack3Start[0], crack3Start[1], crack3End[0], crack3End[1], crackPaint)
                }
                if (progressStage >= 7) {
                    crackPaint.strokeWidth = 3f + (progressStage * 0.2f)
                    canvas.drawLine(crack4Start[0], crack4Start[1], crack4End[0], crack4End[1], crackPaint)
                }
                val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = goldColor
                    style = Paint.Style.FILL
                }
                if (progressStage >= 6) {
                    canvas.drawCircle(crack1End[0], crack1End[1], 4f, dotPaint)
                    canvas.drawCircle(crack2End[0], crack2End[1], 3.5f, dotPaint)
                }
                if (progressStage == 8) {
                    canvas.drawCircle(crack3End[0], crack3End[1], 4.5f, dotPaint)
                    canvas.drawCircle(crack1Start[0], crack1Start[1], 4f, dotPaint)
                }
            }
            return bitmap
        }

        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                var days = 14
                var totalAccumulatedDays = 14
                var userName = "Viajero"
                var framework = SoltarFramework.PSICOLOGIA_MODERNA
                var appThemeMode = "LIGHT"
                var vulnerabilityScore = 40
                var dailySummary: com.example.ai.DailySummaryData? = null

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

                    val recentCheckins = db.checkinDao().getRecentCheckins(5)
                    if (recentCheckins.isNotEmpty()) {
                        val avg = recentCheckins.map { (it.pain + it.anxiety + it.rumination) / 3f }.average().toFloat()
                        vulnerabilityScore = (avg * 10f).toInt().coerceIn(0, 100)
                    }

                    val startOfDay = com.example.ai.DailySummaryEngine.getStartOfTodayMillis()
                    val todayJournals = db.journalDao().getJournalEntriesSince(startOfDay)
                    val todayThoughts = db.thoughtDao().getThoughtsSince(startOfDay)
                    val todayUrges = db.urgeEpisodeDao().getUrgeEpisodesSince(startOfDay)

                    dailySummary = com.example.ai.DailySummaryEngine.generateDailySummary(
                        journals = todayJournals,
                        thoughts = todayThoughts,
                        urges = todayUrges,
                        framework = framework,
                        userName = userName
                    )
                } catch (_: Exception) {}

                val progressStage = com.example.ui.managers.ProgressManager.calculateProgressStage(days, vulnerabilityScore)
                val heartBitmap = drawKintsugiHeartBitmap(sizePx = 150, progressStage = progressStage, vulnerabilityScore = vulnerabilityScore)

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

                val phaseBadge = when (progressStage) {
                    1 -> "⚡ Desintoxicación"
                    in 2..3 -> "🛡️ Soberanía"
                    in 4..5 -> "💡 Claridad"
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
                val quote = when (config.quoteSource) {
                    SoltarWidgetConfig.SOURCE_DAILY_SUMMARY -> dailySummary?.briefMotivationalNote ?: quoteList[quoteIndex]
                    SoltarWidgetConfig.SOURCE_CUSTOM -> config.customMantra.ifBlank { "«Sé dueño de tus decisiones y custodio de tu paz hoy.»" }
                    SoltarWidgetConfig.SOURCE_STOIC -> stoicQuotes[days % stoicQuotes.size]
                    SoltarWidgetConfig.SOURCE_CATHOLIC -> catholicQuotes[days % catholicQuotes.size]
                    SoltarWidgetConfig.SOURCE_PSYCHOLOGY -> psychologyQuotes[days % psychologyQuotes.size]
                    else -> {
                        if (dailySummary != null && dailySummary.hasActivityToday) {
                            dailySummary.briefMotivationalNote
                        } else {
                            quoteList[quoteIndex]
                        }
                    }
                }

                val views = RemoteViews(context.packageName, R.layout.widget_soltar_layout).apply {
                    setInt(R.id.widget_root, "setBackgroundResource", bgRes)
                    setFloat(R.id.widget_root, "setAlpha", alphaVal)

                    // Kintsugi Heart
                    setImageViewBitmap(R.id.widget_kintsugi_heart, heartBitmap)
                    setViewVisibility(R.id.widget_kintsugi_heart, if (config.showDaysCounter) android.view.View.VISIBLE else android.view.View.GONE)

                    // Apply Visibility settings from config
                    setViewVisibility(R.id.widget_days_count, if (config.showDaysCounter) android.view.View.VISIBLE else android.view.View.GONE)
                    setViewVisibility(R.id.widget_days_label, if (config.showDaysCounter) android.view.View.VISIBLE else android.view.View.GONE)
                    setViewVisibility(R.id.widget_framework_badge, if (config.showFrameworkBadge) android.view.View.VISIBLE else android.view.View.GONE)
                    setViewVisibility(R.id.widget_phase_badge, if (config.showPhaseBadge) android.view.View.VISIBLE else android.view.View.GONE)
                    setViewVisibility(R.id.widget_days_subtext, if (config.showSubtext) android.view.View.VISIBLE else android.view.View.GONE)

                    val showActions = config.showActionButtons
                    setViewVisibility(R.id.widget_btn_sos, if (showActions && config.showSosButton) android.view.View.VISIBLE else android.view.View.GONE)
                    setViewVisibility(R.id.widget_btn_coach, if (showActions && config.showCoachButton) android.view.View.VISIBLE else android.view.View.GONE)
                    setViewVisibility(R.id.widget_btn_journal, if (showActions && config.showJournalButton) android.view.View.VISIBLE else android.view.View.GONE)
                    setViewVisibility(R.id.widget_btn_checkin, if (showActions && config.showCheckinButton) android.view.View.VISIBLE else android.view.View.GONE)

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
