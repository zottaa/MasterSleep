package com.github.zottaa.mastersleep.alarmclock.ring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.alarmclock.core.AlarmDataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class AlarmClockRingViewModel @Inject constructor(
    @Named("IO")
    private val dispatcher: CoroutineDispatcher,
    private val alarmDataStoreManager: AlarmDataStoreManager,
) : ViewModel() {
    fun snooze() {
        viewModelScope.launch(dispatcher) {
            alarmDataStoreManager.setAlarm(
                LocalDateTime.now()
                .plusMinutes(5)
                .withSecond(0)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli())
        }
    }
}