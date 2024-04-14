package com.github.zottaa.mastersleep.alarmclock.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.alarmclock.core.AlarmDataStoreManager
import com.github.zottaa.mastersleep.alarmclock.core.SubscribeDataStoreManager
import com.github.zottaa.mastersleep.core.Now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val now: Now,
    private val sleepRequestManager: SleepRequestManager.All,
    private val subscribeDataStoreManager: SubscribeDataStoreManager
) : ViewModel(), SleepRequestManager.Unsubscribe {

    val navigateToSetScreen: StateFlow<Boolean>
        get() = _navigateToSetScreen
    private val _navigateToSetScreen: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun schedule() {
        viewModelScope.launch(dispatcher) {
            val alarmTime = alarmDataStoreManager.readAlarm().first()
            if (alarmTime < now.timeInMillis()) {
                _navigateToSetScreen.emit(true)
            } else {
                alarmClockSchedule.schedule(AlarmItem(alarmTime))
                sleepRequestManager.subscribeToSleepUpdates()
                subscribeDataStoreManager.setSubscribeStatus(true)
            }
        }
    }

    fun cancel() {
        viewModelScope.launch(dispatcher) {
            val alarmTime = alarmDataStoreManager.readAlarm().first()
            alarmClockSchedule.cancel(AlarmItem(alarmTime))
            alarmDataStoreManager.setAlarm(0L)
            _navigateToSetScreen.emit(true)
        }
    }

    override fun unsubscribeFromSleepUpdates() {
        viewModelScope.launch(dispatcher) {
            sleepRequestManager.unsubscribeFromSleepUpdates()
            subscribeDataStoreManager.setSubscribeStatus(false)
        }
    }
}