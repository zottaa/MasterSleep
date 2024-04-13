package com.github.zottaa.mastersleep.alarmclock.schedule

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.github.zottaa.mastersleep.alarmclock.receivers.SleepReceiver
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.SleepSegmentRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface SleepRequestManager {
    interface Subscribe {
        fun subscribeToSleepUpdates()
    }

    interface Unsubscribe {
        fun unsubscribeFromSleepUpdates()
    }

    interface All : Subscribe, Unsubscribe

    class Base @Inject constructor(
        @ApplicationContext
        private val context: Context
    ) : All {
        private val sleepReceiverPendingIntent = SleepReceiver.createPendingIntent(context)

        override fun subscribeToSleepUpdates() {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                ActivityRecognition.getClient(context)
                    .requestSleepSegmentUpdates(
                        sleepReceiverPendingIntent,
                        SleepSegmentRequest.getDefaultSleepSegmentRequest()
                    )
            }
        }

        override fun unsubscribeFromSleepUpdates() {
            ActivityRecognition.getClient(context)
                .removeSleepSegmentUpdates(sleepReceiverPendingIntent)
        }
    }
}