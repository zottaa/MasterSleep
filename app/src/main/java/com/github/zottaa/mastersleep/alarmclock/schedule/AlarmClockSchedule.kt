package com.github.zottaa.mastersleep.alarmclock.schedule

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

interface AlarmClockSchedule {
    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)

    class Base(
        private val context: Context
    ) : AlarmClockSchedule {
        private val alarmManager = context.getSystemService(AlarmManager::class.java)

        @SuppressLint("MissingPermission")
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