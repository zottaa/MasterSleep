package com.github.zottaa.mastersleep.alarmclock.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlarmClockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        println("Alarm Triggered!")
    }
}