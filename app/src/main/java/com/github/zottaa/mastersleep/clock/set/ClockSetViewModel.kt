package com.github.zottaa.mastersleep.clock.set

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zottaa.mastersleep.core.BundleWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ClockSetViewModel @Inject constructor() : ViewModel() {
    val selectedTimeLiveData: LiveData<String>
        get() = _selectedTimeLiveData
    private val _selectedTimeLiveData: MutableLiveData<String> = MutableLiveData()

    fun init(is24HourFormat: Boolean) {
        setAlarmTime(LocalTime.now().hour, LocalTime.now().minute, is24HourFormat)
    }
    fun setAlarmTime(hour: Int, minute: Int, is24HourFormat: Boolean) {
        if (is24HourFormat) {
            _selectedTimeLiveData.value = String.format("%02d:%02d", hour, minute)
        } else {
            val amPm = if (hour < 12) "AM" else "PM"
            val hour12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
            val formattedTime = String.format("%02d:%02d %s", hour12, minute, amPm)
            _selectedTimeLiveData.value = formattedTime
        }
    }

    fun save(
        selectedTimeBundleWrapper: BundleWrapper.String
    ) {
        selectedTimeBundleWrapper.save(selectedTimeLiveData.value ?: "")
    }

    fun restore(
        selectedTimeBundleWrapper: BundleWrapper.String
    ) {
        _selectedTimeLiveData.value = selectedTimeBundleWrapper.restore()
    }
}