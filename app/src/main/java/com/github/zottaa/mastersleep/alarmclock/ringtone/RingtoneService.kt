package com.github.zottaa.mastersleep.alarmclock.ringtone

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.main.MainActivity

class RingtoneService : Service() {
    private lateinit var ringtone: Ringtone
    override fun onBind(intent: Intent?): IBinder? = null


    override fun onCreate() {
        super.onCreate()
        val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == START_SERVICE) {
            createNotificationChannel()
            val notificationIntent = Intent(this, MainActivity::class.java)
            notificationIntent.putExtra(INTENT_KEY, RING_FRAGMENT)
            val pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val stopServiceIntent = Intent(this, RingtoneService::class.java)
            stopServiceIntent.action = STOP_SERVICE
            val stopServicePendingIntent = PendingIntent.getService(
                this, 0, stopServiceIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alarm")
                .setContentText("Wake up!")
                .setSmallIcon(R.drawable.baseline_access_time_24)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_delete_button, "Stop", stopServicePendingIntent)
                .setOngoing(true)
                .build()

            startForeground(1, notification)
            ringtone.play()
        } else if (intent?.action == STOP_SERVICE) {
            ringtone.stop()
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        serviceChannel.setSound(null, null)
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    companion object {
        private const val CHANNEL_ID = "AlarmClockForegroundService"
        private const val INTENT_KEY = "intentAction"
        private const val RING_FRAGMENT = "ringFragment"
        private const val START_SERVICE = "START_SERVICE"
        private const val STOP_SERVICE = "STOP_SERVICE"
    }
}