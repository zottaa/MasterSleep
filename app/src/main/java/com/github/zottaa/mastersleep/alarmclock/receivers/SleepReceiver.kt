package com.github.zottaa.mastersleep.alarmclock.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.SleepClassifyEvent
import com.google.android.gms.location.SleepSegmentEvent

class SleepReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            if (SleepSegmentEvent.hasEvents(intent)) {
                val events = SleepSegmentEvent.extractEvents(intent)
                Log.d(TAG, "Logging SleepSegmentEvents")
                for (event in events) {
                    Log.d(
                        TAG,
                        "${event.startTimeMillis} to ${event.endTimeMillis} with status ${event.status}"
                    )
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

    companion object {
        private const val TAG = "SLEEP_RECEIVER"
    }
}