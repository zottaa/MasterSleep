package com.github.zottaa.mastersleep.alarmclock.ringtone

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.alarmclock.receivers.RingtoneServiceActionsReceiver
import com.github.zottaa.mastersleep.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RingtoneService : Service() {
    private lateinit var ringtone: Ringtone
    private lateinit var vibrator: VibratorWrapper

    @Inject
    lateinit var dataStore: RingtoneDataStore

    override fun onBind(intent: Intent?): IBinder? = null


    override fun onCreate() {
        super.onCreate()
        val ringtoneUri = Uri.parse(dataStore.readRingtoneUri())
        vibrator = VibratorWrapper(this)
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri)
        ringtone.audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        ringtone.isLooping = true
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

            val stopIntent = Intent(this, RingtoneServiceActionsReceiver::class.java)
            stopIntent.action = STOP_ACTION
            val stopPendingIntent = PendingIntent.getBroadcast(
                this, 0, stopIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val snoozeIntent = Intent(this, RingtoneServiceActionsReceiver::class.java)
            snoozeIntent.action = SNOOZE_ACTION
            val snoozePendingIntent = PendingIntent.getBroadcast(
                this, 0, snoozeIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(this.getString(R.string.alarm))
                .setContentText(this.getString(R.string.wake_up))
                .setSmallIcon(R.mipmap.ic_master_sleep)
                .setContentIntent(pendingIntent)
                .addAction(
                    R.drawable.ic_delete_button,
                    this.getString(R.string.stop),
                    stopPendingIntent
                )
                .addAction(
                    R.drawable.baseline_access_time_24,
                    this.getString(R.string.postpone),
                    snoozePendingIntent
                )
                .setOngoing(true)
                .build()

            startForeground(1, notification)
            ringtone.play()
            vibrator.start()
        } else if (intent?.action == STOP_SERVICE) {
            vibrator.stop()
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
        private const val SNOOZE_ACTION = "snooze"
        private const val STOP_ACTION = "stop"
    }
}