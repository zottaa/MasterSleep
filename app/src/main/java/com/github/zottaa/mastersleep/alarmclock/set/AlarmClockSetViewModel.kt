package com.github.zottaa.mastersleep.alarmclock.set

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.core.BundleWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    val selectedTimeLiveData: StateFlow<String>
        get() = _selectedTimeLiveData
    private val _selectedTimeLiveData: MutableStateFlow<String> = MutableStateFlow("")

    fun init(is24HourFormat: Boolean) {
        setAlarmTime(LocalTime.now().hour, LocalTime.now().minute, is24HourFormat)
    }

    fun setAlarmTime(hour: Int, minute: Int, is24HourFormat: Boolean) {
        viewModelScope.launch(dispatcher) {
            if (is24HourFormat) {
                _selectedTimeLiveData.emit(String.format("%02d:%02d", hour, minute))
            } else {
                val amPm = if (hour < 12) "AM" else "PM"
                val hour12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
                val formattedTime = String.format("%02d:%02d %s", hour12, minute, amPm)
                _selectedTimeLiveData.emit(formattedTime)
            }
        }
    }

    fun alarmTimeInLong(is24HourFormat: Boolean): Long {
        val formatter = if (is24HourFormat) {
            DateTimeFormatter.ofPattern("HH:mm")
        } else {
            DateTimeFormatter.ofPattern("hh:mm a")
        }
        val localDate = LocalDate.now()
        val localTime = LocalTime.parse(_selectedTimeLiveData.value, formatter)
        val localDateTime = localDate.atTime(localTime)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun save(
        selectedTimeBundleWrapper: BundleWrapper.String
    ) {
        viewModelScope.launch {
            selectedTimeBundleWrapper.save(selectedTimeLiveData.value)
        }
    }

    fun restore(
        selectedTimeBundleWrapper: BundleWrapper.String
    ) {
        viewModelScope.launch {
            _selectedTimeLiveData.emit(selectedTimeBundleWrapper.restore())
        }
    }
}