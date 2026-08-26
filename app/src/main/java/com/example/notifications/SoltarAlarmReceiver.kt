package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.widget.SoltarAppWidgetProvider

class SoltarAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            SoltarNotificationHelper.ACTION_DAILY_REMINDER -> {
                SoltarNotificationHelper.sendDailyCheckinNotification(context)
                // Schedule for the next day at 21:00
                SoltarNotificationHelper.scheduleDailyReminder(context, 21, 0)
                // Update widget
                SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
            }
            Intent.ACTION_BOOT_COMPLETED -> {
                // Re-schedule alarms after device reboot
                SoltarNotificationHelper.scheduleDailyReminder(context, 21, 0)
                // Refresh home screen widget
                SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
            }
        }
    }
}
