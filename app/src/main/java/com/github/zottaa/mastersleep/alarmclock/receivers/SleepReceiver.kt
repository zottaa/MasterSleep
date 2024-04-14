package com.github.zottaa.mastersleep.alarmclock.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.github.zottaa.mastersleep.alarmclock.core.SleepSegmentRepository
import com.google.android.gms.location.SleepClassifyEvent
import com.google.android.gms.location.SleepSegmentEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
class SleepReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: SleepSegmentRepository.Create
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (SleepSegmentEvent.hasEvents(intent)) {
                val events = SleepSegmentEvent.extractEvents(intent)
                Log.d(TAG, "Logging SleepSegmentEvents")
                for (event in events) {
                    if (event.status == SleepSegmentEvent.STATUS_SUCCESSFUL) {
                        goAsync {
                            repository.create(event.startTimeMillis, event.endTimeMillis)
                        }
                    }
                }
            } else if (SleepClassifyEvent.hasEvents(intent)) {
                val events = SleepClassifyEvent.extractEvents(intent)
                Log.d(TAG, "Logging SleepClassifyEvents")
                for (event in events) {
                    Log.d(
                        TAG,
                        "Confidence: ${event.confidence} - Light: ${event.light} - Motion: ${event.motion}"
                    )
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

        private const val TAG = "SLEEP"
    }
}