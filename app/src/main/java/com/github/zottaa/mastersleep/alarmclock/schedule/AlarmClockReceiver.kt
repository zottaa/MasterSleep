package com.github.zottaa.mastersleep.alarmclock.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlarmClockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Zottaa", "AlarmClockReceiver onReceive")
    }
}