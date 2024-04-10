package com.github.zottaa.mastersleep.alarmclock.schedule

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.github.zottaa.mastersleep.alarmclock.receivers.AlarmClockReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface AlarmClockSchedule {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)

    class Base @Inject constructor(
        @ApplicationContext private val context: Context
    ) : AlarmClockSchedule {
        private val alarmManager = context.getSystemService(AlarmManager::class.java)

        @SuppressLint("ScheduleExactAlarm")
        override fun schedule(item: AlarmItem) {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(
                    item.triggerTime,
                    null
                ),
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    Intent(context, AlarmClockReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }

        override fun cancel(item: AlarmItem) {
            alarmManager.cancel(
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    Intent(context, AlarmClockReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }
}

data class AlarmItem(
    val triggerTime: Long
)