package com.github.zottaa.mastersleep.alarmclock.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.github.zottaa.mastersleep.alarmclock.ringtone.RingtoneService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlarmClockReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            Intent(context, RingtoneService::class.java).also { intent ->
                intent.action = START_SERVICE
                it.startService(intent)
            }
        }
    }

    companion object {
        private const val START_SERVICE = "START_SERVICE"
    }
}