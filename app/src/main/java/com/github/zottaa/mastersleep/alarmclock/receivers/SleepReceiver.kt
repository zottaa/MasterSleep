package com.github.zottaa.mastersleep.alarmclock.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.github.zottaa.mastersleep.alarmclock.core.AlarmDataStoreManager
import com.google.android.gms.location.SleepClassifyEvent
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
class SleepReceiver : BroadcastReceiver() {
    @Inject
    lateinit var alarmDataStore: AlarmDataStoreManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (SleepClassifyEvent.hasEvents(intent)) {
                val events = SleepClassifyEvent.extractEvents(intent)
                for (event in events) {
                    println("SleepClassifyEvent: confidence: ${event.confidence}, light: ${event.light}, motion: ${event.motion}")
                    if (event.confidence > 95) {
                        goAsync {
                            if (alarmDataStore.readSleepStart().first() == 0L)
                                alarmDataStore.setSleepStart(event.timestampMillis)
                        }
                    }
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

    companion object {
        fun createPendingIntent(context: Context): PendingIntent {
            val flags =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
                else
                    PendingIntent.FLAG_CANCEL_CURRENT
            return PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, SleepReceiver::class.java),
                flags
            )
        }
    }
}