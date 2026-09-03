package com.example.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R
import com.example.data.AdrianaDatabase
import com.example.data.SoltarFramework
import com.example.widget.SoltarAppWidgetProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

object SoltarNotificationHelper {

    const val CHANNEL_DAILY = "soltar_daily_reflection"
    const val CHANNEL_SUPPORT = "soltar_support_channel"
    const val CHANNEL_MILESTONES = "soltar_milestones"
    const val CHANNEL_INACTIVITY = "soltar_inactivity_empathy"

    const val NOTIFICATION_ID_DAILY = 1001
    const val NOTIFICATION_ID_SUPPORT = 1002
    const val NOTIFICATION_ID_MILESTONE = 1003
    const val NOTIFICATION_ID_INACTIVITY = 1004

    const val ACTION_DAILY_REMINDER = "com.example.soltar.ACTION_DAILY_REMINDER"
    const val REQUEST_CODE_DAILY_ALARM = 2001
    const val ACTION_MANDATORY_JOURNAL = "com.example.soltar.ACTION_MANDATORY_JOURNAL"
    const val REQUEST_CODE_MANDATORY_JOURNAL = 2002
    const val ACTION_CUSTOM_NOTIFICATION = "com.example.soltar.ACTION_CUSTOM_NOTIFICATION"

    private val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
    val MILESTONE_DAYS = setOf(1, 3, 7, 14, 21, 30, 60, 90, 180, 365)

    private val stoicDailyQuotes = listOf(
        "«Tienes poder sobre tu mente, no sobre los acontecimientos externos. Comprende esto y hallarás tu fuerza.» — Marco Aurelio",
        "«No son las cosas las que nos atormentan, sino la opinión y el juicio que tenemos sobre ellas.» — Epicteto",
        "«El mayor imperio sobre la tierra es el imperio sobre uno mismo y tus propias decisiones.» — Séneca",
        "«Borra la vana imaginación; frena el impulso; mantén el mando absoluto de tu mente rectora.» — Marco Aurelio",
        "«Pide al impulso que espere; la demora y la pausa consciente son el mejor remedio para la pasión.» — Séneca",
        "«Nunca digas 'lo he perdido', sino 'lo he devuelto a la naturaleza'.» — Epicteto",
        "«La mejor venganza y el mayor triunfo personal es no parecerte a quien cometió la injusticia.» — Marco Aurelio",
        "«El fuego prueba al oro; la adversidad forja y templa a los espíritus libres.» — Séneca",
        "«No busques que los acontecimientos ocurran según tu capricho, sino deséalos tal y como suceden.» — Epicteto",
        "«La tranquilidad del ánimo se conquista cuando renuncias a mendigar la atención ajena.» — Marco Aurelio"
    )

    private val psychologyDailyQuotes = listOf(
        "«El contacto cero no es para que el otro vuelva; es el quirófano donde tú sanas tu autoestima.» — Silvia Congost",
        "«El duelo no se cura con distracciones mágicas, sino nombrando la verdad con dignidad y paciencia.» — Gabriel Rolón",
        "«Puedes seguir queriendo a alguien y, al mismo tiempo, dejar de organizar tu vida alrededor suyo.»",
        "«La abstinencia duele en el cuerpo, pero la insistencia donde no hay reciprocidad destruye el alma.»",
        "«Tus pensamientos son eventos mentales pasajeros; no son órdenes que debas obedecer ciegamente.» — Terapia ACT",
        "«Cuando ya no somos capaces de cambiar una situación, nos encontramos ante el desafío de cambiarnos a nosotros mismos.» — Viktor Frankl",
        "«Cuidar tu sueño, tu cuerpo y tus límites es el primer paso biológico para calmar la angustia del apego.» — Dra. Marian Rojas Estapé",
        "«La autocompasión no es debilidad ni lástima; es tratarte con la firmeza y el cariño que le darías a tu mejor amigo.» — Kristin Neff",
        "«No necesitas explicaciones ajenas ni cierres perfectos para decidir que hoy recuperas tu paz.»",
        "«Cada día que eliges no ceder al impulso, tu cerebro desactiva un circuito de dependencia.»"
    )

    private val catholicDailyQuotes = listOf(
        "«Por encima de todo lo que guardes, guarda tu corazón, porque de él brota la vida.» — Proverbios 4:23",
        "«Todo tiene su momento oportuno bajo el cielo: tiempo de abrazar y tiempo de abstenerse de abrazar.» — Eclesiastés 3:5",
        "«Él sana a los quebrantados de corazón y venda sus heridas más profundas.» — Salmo 147:3",
        "«Nada te turbe, nada te espante; todo se pasa, la paz interior permanece.» — Santa Teresa de Jesús",
        "«Dios no nos ha dado un espíritu de cobardía, sino de poder, de amor y de dominio propio.» — 2 Timoteo 1:7",
        "«El rencor es veneno para el alma: el perdón desata al prisionero y descubre que eras tú.» — San Agustín",
        "«En la noche oscura del alma se purifica el amor para madurar en libertad y gracia.» — San Juan de la Cruz",
        "«El Señor es mi pastor, nada me falta; en verdes praderas me hace reposar y renueva mis fuerzas.» — Salmo 23",
        "«El dolor es el megáfono de la Providencia para despertar a un alma dormida.» — C.S. Lewis",
        "«Encomienda tu camino al Señor, confía en su tiempo y Él obrará la paz en tu corazón.» — Salmo 37:5"
    )

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val dailyChannel = NotificationChannel(
                CHANNEL_DAILY,
                "Reflexión Diaria y Motivación",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Citas inspiradoras, balance cotidiano y orientación según tu marco (Estoico, Moderno o Católico)."
                enableVibration(true)
            }

            val supportChannel = NotificationChannel(
                CHANNEL_SUPPORT,
                "Soporte Emocional y Contención",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de respaldo ante momentos de vulnerabilidad o impulsos."
                enableVibration(true)
            }

            val milestonesChannel = NotificationChannel(
                CHANNEL_MILESTONES,
                "Logros y Días de Soberanía",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Celebración sobria de tus hitos en Contacto Cero."
            }

            val inactivityChannel = NotificationChannel(
                CHANNEL_INACTIVITY,
                "Acompañamiento Empático",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Recordatorios cariñosos y sin juicio tras periodos de inactividad."
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(dailyChannel)
            notificationManager.createNotificationChannel(supportChannel)
            notificationManager.createNotificationChannel(milestonesChannel)
            notificationManager.createNotificationChannel(inactivityChannel)
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

    fun scheduleDailyReminder(context: Context, hourOfDay: Int = 21, minute: Int = 0) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_DAILY_REMINDER
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DAILY_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_DAILY_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_DAILY_ALARM,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    fun scheduleMandatoryJournalReminder(context: Context, hourOfDay: Int = 20, minute: Int = 0) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_MANDATORY_JOURNAL
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MANDATORY_JOURNAL,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        } catch (_: SecurityException) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    fun cancelMandatoryJournalReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_MANDATORY_JOURNAL
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_MANDATORY_JOURNAL,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    fun sendMandatoryJournalNotification(
        context: Context,
        framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
        userName: String = "Viajero"
    ) {
        if (!hasNotificationPermission(context)) return
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_JOURNAL)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            5002,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_DAILY)
            .setSmallIcon(R.drawable.ic_stat_soltar)
            .setContentTitle("📖 Hora de tu Diario Diario Obligatorio")
            .setContentText("Hola $userName. Es momento de escribir tu reflexión de hoy para mantener tu claridad y desbloquear tu espacio personal.")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Hola $userName. Es momento de escribir tu reflexión de hoy para mantener tu claridad, registrar tu avance y desbloquear tu espacio personal."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(0, "✍️ Escribir Diario", pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(1006, notification)
        } catch (_: SecurityException) {}
    }

    fun scheduleCustomNotification(context: Context, item: com.example.data.CustomNotificationItem) {
        if (!item.enabled) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_CUSTOM_NOTIFICATION
            putExtra("notification_id", item.id)
            putExtra("notification_title", item.title)
            putExtra("notification_message", item.message)
        }
        val requestCode = (3000 + (Math.abs(item.id) % 10000)).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, item.hour)
            set(Calendar.MINUTE, item.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        } catch (_: SecurityException) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    fun cancelCustomNotification(context: Context, id: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, SoltarAlarmReceiver::class.java).apply {
            action = ACTION_CUSTOM_NOTIFICATION
        }
        val requestCode = (3000 + (Math.abs(id) % 10000)).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    fun rescheduleCustomNotificationNextDay(context: Context, id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AdrianaDatabase.getDatabase(context)
                val settings = db.soltarSettingsDao().getSettingsOnce() ?: return@launch
                if (settings.customNotificationsJson.isNotBlank()) {
                    val list = json.decodeFromString<List<com.example.data.CustomNotificationItem>>(settings.customNotificationsJson)
                    val item = list.find { it.id == id }
                    if (item != null && item.enabled) {
                        scheduleCustomNotification(context, item)
                    }
                }
            } catch (_: Exception) {}
        }
    }

    fun sendCustomNotification(context: Context, title: String, message: String) {
        if (!hasNotificationPermission(context)) return
        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            7000 + (System.currentTimeMillis() % 1000).toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_DAILY)
            .setSmallIcon(R.drawable.ic_stat_soltar)
            .setContentTitle(title.ifBlank { "Recordatorio de Soberanía" })
            .setContentText(message.ifBlank { "Mantén tu enfoque y respira hondo." })
            .setStyle(NotificationCompat.BigTextStyle().bigText(message.ifBlank { "Mantén tu enfoque y respira hondo." }))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(2000 + (System.currentTimeMillis() % 1000).toInt(), notification)
        } catch (_: SecurityException) {}
    }

    fun rescheduleFromSettings(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AdrianaDatabase.getDatabase(context)
                val settings = db.soltarSettingsDao().getSettingsOnce()
                if (settings != null) {
                    if (settings.notificationsEnabled) {
                        scheduleDailyReminder(context, settings.reminderHour, settings.reminderMinute)
                    } else {
                        cancelDailyReminder(context)
                    }

                    // Mandatory journal reminder is always scheduled (non-disableable)
                    scheduleMandatoryJournalReminder(context, settings.mandatoryJournalHour, settings.mandatoryJournalMinute)

                    // Custom notifications
                    if (settings.customNotificationsJson.isNotBlank()) {
                        try {
                            val list = json.decodeFromString<List<com.example.data.CustomNotificationItem>>(settings.customNotificationsJson)
                            list.forEach { item ->
                                if (item.enabled) {
                                    scheduleCustomNotification(context, item)
                                } else {
                                    cancelCustomNotification(context, item.id)
                                }
                            }
                        } catch (_: Exception) {}
                    }
                } else {
                    scheduleDailyReminder(context, 21, 0)
                    scheduleMandatoryJournalReminder(context, 20, 0)
                }
            } catch (_: Exception) {
                scheduleDailyReminder(context, 21, 0)
                scheduleMandatoryJournalReminder(context, 20, 0)
            }
        }
    }

    fun checkAndTriggerScheduledReminders(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AdrianaDatabase.getDatabase(context)
                val settings = db.soltarSettingsDao().getSettingsOnce()

                if (settings != null && !settings.notificationsEnabled) {
                    return@launch
                }

                val framework = try {
                    SoltarFramework.valueOf(settings?.preferredFramework ?: "PSICOLOGIA_MODERNA")
                } catch (_: Exception) {
                    SoltarFramework.PSICOLOGIA_MODERNA
                }
                val userName = settings?.userName?.ifBlank { "Viajero" } ?: "Viajero"

                // 1. Check for Contact Cero Milestones
                val breakupTimestamp = settings?.breakupDateTimestamp ?: (System.currentTimeMillis() - (14L * 24 * 3600 * 1000))
                val elapsedMillis = (System.currentTimeMillis() - breakupTimestamp).coerceAtLeast(0L)
                val daysElapsed = (elapsedMillis / (1000L * 3600 * 24)).toInt()
                val lastCelebrated = settings?.lastMilestoneCelebrated ?: 0

                val isMilestone = MILESTONE_DAYS.contains(daysElapsed) && daysElapsed > lastCelebrated

                if (isMilestone) {
                    sendMilestoneNotification(context, daysElapsed, framework, userName)
                    if (settings != null) {
                        db.soltarSettingsDao().saveSettings(settings.copy(lastMilestoneCelebrated = daysElapsed))
                    }
                    scheduleDailyReminder(context, settings?.reminderHour ?: 21, settings?.reminderMinute ?: 0)
                    return@launch
                }

                // 1.5. Check for Anticipated Risk Dates (5-7 days before or today)
                val riskDatesList = db.riskDateDao().getAllRiskDatesOnce()
                val todayCal = Calendar.getInstance()
                val currentYear = todayCal.get(Calendar.YEAR)
                var riskDateTriggered = false

                for (rd in riskDatesList) {
                    val targetCal = Calendar.getInstance().apply {
                        set(Calendar.YEAR, currentYear)
                        set(Calendar.MONTH, rd.month - 1)
                        set(Calendar.DAY_OF_MONTH, rd.day)
                        set(Calendar.HOUR_OF_DAY, 9)
                        set(Calendar.MINUTE, 0)
                    }
                    if (targetCal.timeInMillis < todayCal.timeInMillis) {
                        targetCal.add(Calendar.YEAR, 1)
                    }
                    val diffMillis = targetCal.timeInMillis - todayCal.timeInMillis
                    val daysUntil = (diffMillis / (1000L * 3600 * 24)).toInt()

                    if (daysUntil in 0..rd.reminderDaysBefore) {
                        if (rd.lastNotifiedYear != currentYear) {
                            sendRiskDateAnticipatedNotification(context, rd, daysUntil, framework, userName)
                            db.riskDateDao().insertRiskDate(rd.copy(lastNotifiedYear = currentYear))
                            riskDateTriggered = true
                            break
                        }
                    }
                }

                if (riskDateTriggered) {
                    scheduleDailyReminder(context, settings?.reminderHour ?: 21, settings?.reminderMinute ?: 0)
                    return@launch
                }

                // 2. Check for 3+ Days Inactivity
                val latestCheckin = db.checkinDao().getLatestCheckin()
                val lastInactivityNotice = settings?.lastInactivityNoticeSentTimestamp ?: 0L
                val inactivityAlertsEnabled = settings?.inactivityAlertsEnabled ?: true

                val daysSinceLastCheckin = if (latestCheckin != null) {
                    ((System.currentTimeMillis() - latestCheckin.timestamp) / (1000L * 3600 * 24)).toInt()
                } else {
                    daysElapsed.coerceAtLeast(0)
                }

                val hoursSinceLastNotice = (System.currentTimeMillis() - lastInactivityNotice) / (1000L * 3600)

                if (daysSinceLastCheckin >= 3 && inactivityAlertsEnabled && hoursSinceLastNotice >= 48) {
                    sendInactivityEmpatheticNotification(context, daysSinceLastCheckin, userName, framework)
                    if (settings != null) {
                        db.soltarSettingsDao().saveSettings(
                            settings.copy(lastInactivityNoticeSentTimestamp = System.currentTimeMillis())
                        )
                    }
                    scheduleDailyReminder(context, settings?.reminderHour ?: 21, settings?.reminderMinute ?: 0)
                    return@launch
                }

                // 3. Regular Daily Check-in & Wisdom Reminder
                sendDailyCheckinNotification(context)

                // Schedule for the next day
                scheduleDailyReminder(context, settings?.reminderHour ?: 21, settings?.reminderMinute ?: 0)
            } catch (_: Exception) {
                sendDailyCheckinNotification(context)
                scheduleDailyReminder(context, 21, 0)
            }
        }
    }

    fun sendDailyCheckinNotification(context: Context) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannels(context)

        CoroutineScope(Dispatchers.IO).launch {
            var framework = SoltarFramework.PSICOLOGIA_MODERNA
            var userName = "Viajero"

            try {
                val db = AdrianaDatabase.getDatabase(context)
                val settings = db.soltarSettingsDao().getSettingsOnce()
                if (settings != null) {
                    framework = try {
                        SoltarFramework.valueOf(settings.preferredFramework)
                    } catch (_: Exception) {
                        SoltarFramework.PSICOLOGIA_MODERNA
                    }
                    if (settings.userName.isNotBlank()) {
                        userName = settings.userName
                    }
                }
            } catch (_: Exception) {}

            val (title, quotesList) = when (framework) {
                SoltarFramework.ESTOICO -> Pair(
                    "🏛️ Recuerda • Temple y Soberanía Diaria",
                    stoicDailyQuotes
                )
                SoltarFramework.CATOLICO -> Pair(
                    "✝️ Recuerda • Paz, Esperanza y Custodia",
                    catholicDailyQuotes
                )
                SoltarFramework.PSICOLOGIA_MODERNA -> Pair(
                    "🧠 Recuerda • Autonomía y Claridad Emocional",
                    psychologyDailyQuotes
                )
            }

            val selectedQuote = quotesList.random()

            var adaptiveQuote = selectedQuote
            var adaptiveTitle = title
            try {
                val db = AdrianaDatabase.getDatabase(context)
                val recentCheckins = db.checkinDao().getRecentCheckins(5)
                if (recentCheckins.isNotEmpty()) {
                    val aiNotification = com.example.ai.OnDeviceLlmEngine.generateDailyNotification(
                        checkins = recentCheckins,
                        framework = framework,
                        userName = userName
                    )
                    adaptiveTitle = aiNotification.title
                    adaptiveQuote = aiNotification.body
                } else {
                    val latestCheckin = db.checkinDao().getLatestCheckin()
                    if (latestCheckin != null) {
                        when {
                            latestCheckin.comparisonWithYesterday.contains("Mejor", true) || latestCheckin.urgeToContact <= 3f -> {
                                adaptiveTitle = "🌿 Recuerda • Seguimiento Emocional (Progreso)"
                                adaptiveQuote = "¿Cómo estás hoy? Queremos ver si esa calma que estabas recuperando sigue ahí."
                            }
                            latestCheckin.urgeToContact > 5f || latestCheckin.comparisonWithYesterday.contains("Peor", true) -> {
                                adaptiveTitle = "🛡️ Recuerda • Seguimiento Emocional (Contención)"
                                adaptiveQuote = "¿Cómo estás hoy? ¿Ha vuelto ese impulso de contactar? Detente un instante antes de actuar."
                            }
                            else -> {
                                adaptiveTitle = "🧠 Recuerda • Check-in Emocional Diario"
                                adaptiveQuote = "¿Cómo te encuentras hoy emocionalmente? Registra tu evolución en 1 minuto."
                            }
                        }
                    }
                }
            } catch (_: Exception) {}

            // Main Tap Intent -> Open Check-in (Inicio)
            val mainIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_CHECKIN)
            }
            val mainPendingIntent = PendingIntent.getActivity(
                context,
                3001,
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Action Intent 1 -> Coach Recuerda
            val coachIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_COACH)
            }
            val coachPendingIntent = PendingIntent.getActivity(
                context,
                3002,
                coachIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Action Intent 2 -> Open Journal
            val journalIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_JOURNAL)
            }
            val journalPendingIntent = PendingIntent.getActivity(
                context,
                3003,
                journalIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Action Intent 3 -> Open SOS / Urge Mode
            val sosIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_URGE_MODE)
            }
            val sosPendingIntent = PendingIntent.getActivity(
                context,
                3004,
                sosIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(context, CHANNEL_DAILY)
                .setSmallIcon(R.drawable.ic_stat_soltar)
                .setContentTitle(adaptiveTitle)
                .setContentText(adaptiveQuote)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("$adaptiveQuote\n\n$userName, pulsa para completar tu check-in emocional rápido.")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)
                .addAction(0, "✨ Check-in Rápido", mainPendingIntent)
                .addAction(0, "💬 Coach", coachPendingIntent)
                .addAction(0, "🚨 SOS", sosPendingIntent)
                .build()

            try {
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_DAILY, notification)
            } catch (_: SecurityException) {}
        }
    }

    fun sendSupportNotification(context: Context, title: String, message: String) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_URGE_MODE)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            4001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_SUPPORT)
            .setSmallIcon(R.drawable.ic_stat_soltar)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(0, "🧘 Respiración de Emergencia", pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_SUPPORT, notification)
        } catch (_: SecurityException) {}
    }

    fun sendInactivityEmpatheticNotification(
        context: Context,
        daysInactive: Int,
        userName: String = "Viajero",
        framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA
    ) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannels(context)

        val (title, bodyMessage) = when (framework) {
            SoltarFramework.ESTOICO -> Pair(
                "🌿 Recuerda • Regreso al centro interior",
                "Hola $userName. Llevas $daysInactive días sin registrar cómo estás. Recuerda que no hay juicio: la virtud es la paciencia de volver a la calma sin reproches. ¿Hacemos 1 minuto de pausa consciente hoy?"
            )
            SoltarFramework.CATOLICO -> Pair(
                "🌿 Recuerda • Un remanso de paz y escucha",
                "Querido/a $userName, hace $daysInactive días que no pasas por aquí. 'Venid a mí los que estéis cansados...' Tu camino de sanación sigue vivo. Aquí tienes un espacio sin prisas cuando lo necesites."
            )
            SoltarFramework.PSICOLOGIA_MODERNA -> Pair(
                "🌿 Recuerda • Aquí estoy contigo, $userName",
                "Llevas $daysInactive días sin registrar datos. Sanar no es un proceso lineal y no hay nada que reprocharte. Si hoy sientes pesadez o nostalgia, regálate un minuto de autocompasión."
            )
        }

        val checkinIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_CHECKIN)
        }
        val checkinPendingIntent = PendingIntent.getActivity(
            context,
            3101,
            checkinIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val coachIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_COACH)
        }
        val coachPendingIntent = PendingIntent.getActivity(
            context,
            3102,
            coachIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val sosIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_URGE_MODE)
        }
        val sosPendingIntent = PendingIntent.getActivity(
            context,
            3103,
            sosIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_INACTIVITY)
            .setSmallIcon(R.drawable.ic_stat_soltar)
            .setContentTitle(title)
            .setContentText(bodyMessage)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bodyMessage))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(checkinPendingIntent)
            .addAction(0, "✨ Check-in Rápido", checkinPendingIntent)
            .addAction(0, "💬 Hablar con Recuerda", coachPendingIntent)
            .addAction(0, "🧘 Respirar", sosPendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_INACTIVITY, notification)
        } catch (_: SecurityException) {}
    }

    fun sendMilestoneNotification(
        context: Context,
        days: Int,
        framework: SoltarFramework = SoltarFramework.PSICOLOGIA_MODERNA,
        userName: String = "Viajero"
    ) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_CHECKIN)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            5001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_MILESTONES)
            .setSmallIcon(R.drawable.ic_stat_soltar)
            .setContentTitle("🎉 Hito Alcanzado: $days Días de Contacto Cero")
            .setContentText("Has sostenido tu decisión con valentía. Tu sistema nervioso se está reconfigurando hacia la paz.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Has sostenido $days días de Contacto Cero con valentía.\n\n$userName, cada segundo sostenido es una victoria de tu soberanía personal.")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(0, "✨ Ver mi Proceso", pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_MILESTONE, notification)
        } catch (_: SecurityException) {}
    }

    fun sendRiskDateAnticipatedNotification(
        context: Context,
        riskDate: com.example.data.RiskDateEntity,
        daysUntil: Int,
        framework: SoltarFramework,
        userName: String
    ) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannels(context)

        val title = if (daysUntil == 0) {
            "🚨 ALERTA • Hoy es ${riskDate.title}"
        } else {
            "🛡️ Recuerda • Fecha de Riesgo en $daysUntil días (${riskDate.title})"
        }

        val strategyText = if (riskDate.customStrategy.isNotBlank()) {
            "Estrategia: ${riskDate.customStrategy}"
        } else {
            com.example.ai.OnDeviceLlmEngine.generateRiskDateCopingStrategy(
                riskDateTitle = riskDate.title,
                daysUntil = daysUntil,
                pastTriggers = emptyList(),
                framework = framework
            ).replace("**", "")
        }

        val bodyMessage = if (daysUntil == 0) {
            "Hola $userName. Hoy se cumple ${riskDate.title}. El riesgo de impulso es alto, pero ya estás preparado/a. $strategyText"
        } else {
            "Hola $userName. Se acerca ${riskDate.title} en $daysUntil días. Nos anticipamos al golpe para proteger tu paz. $strategyText"
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_URGE_MODE)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            6001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_SUPPORT)
            .setSmallIcon(R.drawable.ic_stat_soltar)
            .setContentTitle(title)
            .setContentText(bodyMessage)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bodyMessage))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(0, "🛡️ Modo Impulso", pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(1005, notification)
        } catch (_: SecurityException) {}
    }
}
