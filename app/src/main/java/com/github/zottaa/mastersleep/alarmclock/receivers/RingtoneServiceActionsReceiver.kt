package com.github.zottaa.mastersleep.alarmclock.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.zottaa.mastersleep.alarmclock.core.AlarmDataStoreManager
import com.github.zottaa.mastersleep.alarmclock.core.SleepSegmentRepository
import com.github.zottaa.mastersleep.alarmclock.ringtone.RingtoneService
import com.github.zottaa.mastersleep.alarmclock.schedule.AlarmClockSchedule
import com.github.zottaa.mastersleep.alarmclock.schedule.AlarmItem
import com.github.zottaa.mastersleep.core.Now
import com.github.zottaa.mastersleep.main.MainActivity
import com.github.zottaa.mastersleep.streaks.StreaksDataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class RingtoneServiceActionsReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: SleepSegmentRepository.Create

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

            val actionIntent = Intent(context, MainActivity::class.java)
            when (intent.action) {
                SNOOZE_ACTION -> {
                    val alarmDataStoreManager = AlarmDataStoreManager(context)
                    val alarmClockSchedule = AlarmClockSchedule.Base(context)
                    val newAlarmTime = LocalDateTime.now()
                        .plusMinutes(5)
                        .withSecond(0)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                    goAsync {
                        alarmDataStoreManager.setAlarm(newAlarmTime)
                    }
                    alarmClockSchedule.schedule(AlarmItem(newAlarmTime))
                }

                STOP_ACTION -> {
                    val streaksDataStoreManager = StreaksDataStoreManager.Base(context)
                    val alarmDataStoreManager = AlarmDataStoreManager(context)
                    val now = Now.Base()
                    goAsync {
                        if (alarmDataStoreManager.readSleepStart().first() != 0L) {
                            repository.create(now.timeInMillis())
                            alarmDataStoreManager.setSleepStart(0L)
                        }
                        streaksDataStoreManager.updateSleepStreak()
                    }
                }
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 1, actionIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            pendingIntent.send()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun goAsync(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val pendingResult = goAsync()
        GlobalScope.launch(context) {
            try {
                block()
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        private const val STOP_SERVICE = "STOP_SERVICE"
        private const val SNOOZE_ACTION = "snooze"
        private const val STOP_ACTION = "stop"
    }
}