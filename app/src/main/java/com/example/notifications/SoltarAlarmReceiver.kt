package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.widget.SoltarAppWidgetProvider

class SoltarAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            SoltarNotificationHelper.ACTION_DAILY_REMINDER -> {
                // Check milestones, inactivity or standard daily wisdom check-in
                SoltarNotificationHelper.checkAndTriggerScheduledReminders(context)
                // Refresh home screen widget
                SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
            }
            SoltarNotificationHelper.ACTION_MANDATORY_JOURNAL -> {
                SoltarNotificationHelper.sendMandatoryJournalNotification(context)
                SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
            }
            SoltarNotificationHelper.ACTION_CUSTOM_NOTIFICATION -> {
                val id = intent.getLongExtra("notification_id", -1L)
                val title = intent.getStringExtra("notification_title") ?: "Recordatorio de Soberanía"
                val message = intent.getStringExtra("notification_message") ?: "Mantén tu enfoque y respira hondo."
                SoltarNotificationHelper.sendCustomNotification(context, title, message)
                if (id != -1L) {
                    SoltarNotificationHelper.rescheduleCustomNotificationNextDay(context, id)
                }
            }
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                // Re-schedule alarms after device reboot according to user's saved time
                SoltarNotificationHelper.rescheduleFromSettings(context)
                // Refresh home screen widget
                SoltarAppWidgetProvider.notifyWidgetDataChanged(context)
            }
        }
    }
}
