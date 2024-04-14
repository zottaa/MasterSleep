package com.github.zottaa.mastersleep.alarmclock.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.zottaa.mastersleep.alarmclock.core.AlarmDataStoreManager
import com.github.zottaa.mastersleep.alarmclock.core.SubscribeDataStoreManager
import com.github.zottaa.mastersleep.alarmclock.schedule.AlarmClockSchedule
import com.github.zottaa.mastersleep.alarmclock.schedule.AlarmItem
import com.github.zottaa.mastersleep.alarmclock.schedule.SleepRequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var alarmDataStoreManager: AlarmDataStoreManager

    @Inject
    lateinit var alarmClockSchedule: AlarmClockSchedule

    @Inject
    lateinit var sleepRequestManager: SleepRequestManager.All

    @Inject
    lateinit var subscribeDataStoreManager: SubscribeDataStoreManager
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                goAsync {
                    val alarmTime = alarmDataStoreManager.readAlarm().first()
                    alarmClockSchedule.schedule(AlarmItem(alarmTime))
                    if (subscribeDataStoreManager.readSubscribeStatus().first())
                        sleepRequestManager.subscribeToSleepUpdates()
                }
            }
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
}

