package com.github.zottaa.mastersleep.alarmclock.ringtone

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class VibratorWrapper(
    context: Context
) {
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    else
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun start() {
        vibrator.vibrate(
            VibrationEffect.createWaveform(longArrayOf(0, 250, 500, 800), 0)
        )
    }

    fun stop() {
        vibrator.cancel()
    }
}