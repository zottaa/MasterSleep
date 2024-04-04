package com.github.zottaa.mastersleep.alarmclock.ring

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.zottaa.mastersleep.alarmclock.ringtone.RingtoneService
import com.github.zottaa.mastersleep.alarmclock.schedule.AlarmClockSchedule
import com.github.zottaa.mastersleep.alarmclock.schedule.AlarmItem
import com.github.zottaa.mastersleep.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneId

@AndroidEntryPoint
class RingtoneServiceActionsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            val stopServiceIntent = Intent(context, RingtoneService::class.java).apply {
                action = STOP_SERVICE
            }
            val stopServicePendingIntent = PendingIntent.getService(
                context, 0, stopServiceIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            stopServicePendingIntent.send()

            val navigationIntent = Intent(context, MainActivity::class.java)
            when (intent.action) {
                STOP_ACTION -> setupStopActionIntent(navigationIntent)
                SNOOZE_ACTION -> {
                    val newAlarmTime = LocalDateTime.now()
                        .plusMinutes(1)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                    setupSnoozeActionIntent(navigationIntent, newAlarmTime)
                    AlarmClockSchedule.Base(context).schedule(AlarmItem(newAlarmTime))
                }
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 1, navigationIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            pendingIntent.send()
        }
    }

    private fun setupStopActionIntent(intent: Intent) {
        intent.putExtra(INTENT_KEY, DIARY_LIST_FRAGMENT)
    }

    private fun setupSnoozeActionIntent(intent: Intent, newAlarmTime: Long) {
        intent.putExtra(INTENT_KEY, SCHEDULE_FRAGMENT)
        intent.putExtra(
            NEW_ALARM_TIME,
            newAlarmTime
        )
    }

    companion object {
        private const val INTENT_KEY = "intentAction"
        private const val SCHEDULE_FRAGMENT = "scheduleFragment"
        private const val NEW_ALARM_TIME = "newAlarmTime"
        private const val DIARY_LIST_FRAGMENT = "diaryListFragment"
        private const val STOP_SERVICE = "STOP_SERVICE"
        private const val SNOOZE_ACTION = "snooze"
        private const val STOP_ACTION = "stop"
    }
}