package com.github.zottaa.mastersleep.alarmclock.ring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.alarmclock.core.AlarmDataStoreManager
import com.github.zottaa.mastersleep.core.Dispatcher
import com.github.zottaa.mastersleep.core.DispatcherType
import com.github.zottaa.mastersleep.streaks.StreaksDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject


@HiltViewModel
class AlarmClockRingViewModel @Inject constructor(
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher,
    private val alarmDataStoreManager: AlarmDataStoreManager,
    private val streaksDataStoreManager: StreaksDataStoreManager
) : ViewModel() {
    fun snooze() {
        viewModelScope.launch(dispatcher) {
            alarmDataStoreManager.setAlarm(
                LocalDateTime.now()
                    .plusMinutes(5)
                    .withSecond(0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            )
        }
    }

    fun stop() {
        viewModelScope.launch(dispatcher) {
            streaksDataStoreManager.updateSleepStreak()
        }
    }
}