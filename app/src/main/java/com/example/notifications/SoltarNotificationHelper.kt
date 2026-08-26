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
import com.example.widget.SoltarAppWidgetProvider
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

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val dailyChannel = NotificationChannel(
                CHANNEL_DAILY,
                "Reflexión Diaria y Check-in",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Recordatorios cotidianos para registrar tu estado, reflexionar y mantener el foco."
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

        // Main Tap Intent -> Open Check-in
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

        // Action Intent 1 -> Open Journal
        val journalIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_JOURNAL)
        }
        val journalPendingIntent = PendingIntent.getActivity(
            context,
            3002,
            journalIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action Intent 2 -> Open SOS / Urge Mode
        val sosIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(SoltarAppWidgetProvider.EXTRA_OPEN_ACTION, SoltarAppWidgetProvider.ACTION_URGE_MODE)
        }
        val sosPendingIntent = PendingIntent.getActivity(
            context,
            3003,
            sosIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dailyQuotes = listOf(
            "«El dolor es inevitable; prolongar el sufrimiento alimentando la ilusión es opcional.»",
            "«Hoy ganaste un día más de libertad y respeto propio. Haz tu balance de hoy.»",
            "«Sé el custodio de tu paz mental. ¿Cómo está tu respiración esta noche?»",
            "«No necesitas respuestas ajenas para cerrar un ciclo con dignidad.»"
        )
        val selectedQuote = dailyQuotes.random()

        val notification = NotificationCompat.Builder(context, CHANNEL_DAILY)
            .setSmallIcon(R.drawable.ic_stat_soltar)
            .setContentTitle("🛡️ Check-in de Soberanía Emocional")
            .setContentText("Dedica 1 minuto a registrar cómo te sientes y honrar tu camino.")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$selectedQuote\n\nCompleta tu check-in diario o escribe en tu diario personal.")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(mainPendingIntent)
            .addAction(0, "📖 Abrir Diario", journalPendingIntent)
            .addAction(0, "🚨 SOS Impulso", sosPendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_DAILY, notification)
        } catch (_: SecurityException) {}
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
