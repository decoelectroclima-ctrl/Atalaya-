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

    const val NOTIFICATION_ID_DAILY = 1001
    const val NOTIFICATION_ID_SUPPORT = 1002
    const val NOTIFICATION_ID_MILESTONE = 1003

    const val ACTION_DAILY_REMINDER = "com.example.soltar.ACTION_DAILY_REMINDER"
    const val REQUEST_CODE_DAILY_ALARM = 2001

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

            notificationManager.createNotificationChannel(dailyChannel)
            notificationManager.createNotificationChannel(supportChannel)
            notificationManager.createNotificationChannel(milestonesChannel)
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
                    "🏛️ ADRIANA • Temple y Soberanía Diaria",
                    stoicDailyQuotes
                )
                SoltarFramework.CATOLICO -> Pair(
                    "✝️ ADRIANA • Paz, Esperanza y Custodia",
                    catholicDailyQuotes
                )
                SoltarFramework.PSICOLOGIA_MODERNA -> Pair(
                    "🧠 ADRIANA • Autonomía y Claridad Emocional",
                    psychologyDailyQuotes
                )
            }

            val selectedQuote = quotesList.random()

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

            // Action Intent 1 -> Coach ADRIANA
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
                .setContentTitle(title)
                .setContentText(selectedQuote)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("$selectedQuote\n\n$userName, honra hoy tu camino de autonomía y paz.")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(mainPendingIntent)
                .addAction(0, "💬 Coach", coachPendingIntent)
                .addAction(0, "📖 Diario", journalPendingIntent)
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

    fun sendMilestoneNotification(context: Context, days: Int) {
        if (!hasNotificationPermission(context)) return

        createNotificationChannels(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
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
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_MILESTONE, notification)
        } catch (_: SecurityException) {}
    }
}
