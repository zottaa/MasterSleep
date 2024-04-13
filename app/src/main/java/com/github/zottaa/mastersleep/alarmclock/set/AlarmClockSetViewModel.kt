package com.github.zottaa.mastersleep.alarmclock.set

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.alarmclock.core.AlarmDataStoreManager
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.core.Now
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AlarmClockSetViewModel @Inject constructor(
    @Named("IO")
    private val dispatcher: CoroutineDispatcher,
    private val alarmDataStoreManager: AlarmDataStoreManager,
    private val now: Now
) : ViewModel() {
    val selectedTime: StateFlow<String>
        get() = _selectedTime
    private val _selectedTime: MutableStateFlow<String> = MutableStateFlow("")

    val isAlarmAlreadyScheduled: StateFlow<Boolean>
        get() = _isAlarmAlreadyScheduled
    private val _isAlarmAlreadyScheduled: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val navigateToSchedule: StateFlow<Boolean>
        get() = _navigateToSchedule
    private val _navigateToSchedule: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun init(is24HourFormat: Boolean) {
        viewModelScope.launch(dispatcher) {
            setAlarmTime(LocalTime.now().hour, LocalTime.now().minute, is24HourFormat)
            val alarmTime = alarmDataStoreManager.readAlarm().first()
            if (alarmTime > now.timeInMillis())
                _isAlarmAlreadyScheduled.emit(true)
        }
    }

    fun scheduleAlarm(is24HourFormat: Boolean) {
        viewModelScope.launch(dispatcher) {
            alarmDataStoreManager.setAlarm(alarmTimeInLong(is24HourFormat))
            _navigateToSchedule.emit(true)
        }
    }

    fun setAlarmTime(hour: Int, minute: Int, is24HourFormat: Boolean) {
        viewModelScope.launch(dispatcher) {
            if (is24HourFormat) {
                _selectedTime.emit(String.format("%02d:%02d", hour, minute))
            } else {
                val amPm = if (hour < 12) "AM" else "PM"
                val hour12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
                val formattedTime = String.format("%02d:%02d %s", hour12, minute, amPm)
                _selectedTime.emit(formattedTime)
            }
        }
    }

    private fun alarmTimeInLong(is24HourFormat: Boolean): Long {
        val formatter = if (is24HourFormat) {
            DateTimeFormatter.ofPattern("HH:mm")
        } else {
            DateTimeFormatter.ofPattern("hh:mm a")
        }

        val localTime = LocalTime.parse(_selectedTime.value, formatter)
        val localDate =
            if (localTime > LocalTime.now()) LocalDate.now() else LocalDate.now().plusDays(1)
        val localDateTime = localDate.atTime(localTime)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun save(
        selectedTimeBundleWrapper: BundleWrapper.String
    ) {
        viewModelScope.launch {
            selectedTimeBundleWrapper.save(selectedTime.value)
        }
    }

    fun restore(
        selectedTimeBundleWrapper: BundleWrapper.String
    ) {
        viewModelScope.launch {
            _selectedTime.emit(selectedTimeBundleWrapper.restore())
        }
    }
}