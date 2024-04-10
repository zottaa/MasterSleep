package com.github.zottaa.mastersleep.alarmclock.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.alarmclock.core.AlarmDataStoreManager
import com.github.zottaa.mastersleep.core.Now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AlarmClockScheduleViewModel @Inject constructor(
    @Named("IO")
    private val dispatcher: CoroutineDispatcher,
    private val alarmDataStoreManager: AlarmDataStoreManager,
    private val alarmClockSchedule: AlarmClockSchedule,
    private val now: Now
) : ViewModel() {
    val isAlarmAlreadyPlayed: StateFlow<Boolean>
        get() = _isAlarmAlreadyPlayed
    private val _isAlarmAlreadyPlayed: MutableStateFlow<Boolean> = MutableStateFlow(false)


    fun schedule() {
        viewModelScope.launch(dispatcher) {
            val alarmTime = alarmDataStoreManager.readAlarm().first()
            if (alarmTime < now.timeInMillis())
                _isAlarmAlreadyPlayed.emit(true)
            else
                alarmClockSchedule.schedule(AlarmItem(alarmTime))
        }
    }

    fun cancel() {
        viewModelScope.launch(dispatcher) {
            val alarmTime = alarmDataStoreManager.readAlarm().first()
            alarmClockSchedule.cancel(AlarmItem(alarmTime))
            alarmDataStoreManager.setAlarm(0L)
        }
    }

}