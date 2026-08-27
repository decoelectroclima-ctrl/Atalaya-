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
