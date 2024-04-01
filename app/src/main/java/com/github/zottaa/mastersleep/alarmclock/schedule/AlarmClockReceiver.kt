package com.github.zottaa.mastersleep.alarmclock.schedule

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.zottaa.mastersleep.alarmclock.ringtone.RingtoneService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlarmClockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.startService(Intent(context, RingtoneService::class.java))
    }
}