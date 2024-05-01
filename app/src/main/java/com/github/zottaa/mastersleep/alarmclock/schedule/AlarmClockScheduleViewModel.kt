package com.github.zottaa.mastersleep.alarmclock.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.alarmclock.core.AlarmDataStoreManager
import com.github.zottaa.mastersleep.alarmclock.core.SleepSegmentRepository
import com.github.zottaa.mastersleep.alarmclock.core.SubscribeDataStoreManager
import com.github.zottaa.mastersleep.core.DateTimeUtils
import com.github.zottaa.mastersleep.core.Dispatcher
import com.github.zottaa.mastersleep.core.DispatcherType
import com.github.zottaa.mastersleep.core.Now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AlarmClockScheduleViewModel @Inject constructor(
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher,
    private val dateTimeUtils: DateTimeUtils,
    private val alarmDataStoreManager: AlarmDataStoreManager,
    private val alarmClockSchedule: AlarmClockSchedule,
    private val now: Now,
    private val sleepRequestManager: SleepRequestManager.All,
    private val subscribeDataStoreManager: SubscribeDataStoreManager,
    private val sleepSegmentRepository: SleepSegmentRepository.Create
) : ViewModel(), SleepRequestManager.Unsubscribe {

    val navigateToSetScreen: StateFlow<Boolean>
        get() = _navigateToSetScreen
    private val _navigateToSetScreen: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val alarmTriggerTime: StateFlow<String>
        get() = _alarmTriggerTime
    private val _alarmTriggerTime: MutableStateFlow<String> = MutableStateFlow(
        dateTimeUtils.localDateTimeToString(LocalDateTime.now())
    )

    fun schedule() {
        viewModelScope.launch(dispatcher) {
            val alarmTime = alarmDataStoreManager.readAlarm().first()
            _alarmTriggerTime.emit(dateTimeUtils.stringTimeFromLong(alarmTime))
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
            if (alarmDataStoreManager.readSleepStart().first() != 0L) {
                sleepSegmentRepository.create(now.timeInMillis())
                alarmDataStoreManager.setSleepStart(0L)
            }
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