package com.github.zottaa.mastersleep.alarmclock.ringtone

import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.IBinder

class RingtoneService : Service() {
    private lateinit var ringtone: Ringtone
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri)
        ringtone.play()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        ringtone.stop()
    }
}