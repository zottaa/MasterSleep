package com.github.zottaa.mastersleep.diary.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.core.DateUtils
import com.github.zottaa.mastersleep.core.Dispatcher
import com.github.zottaa.mastersleep.core.DispatcherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val dateUtils: DateUtils,
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher
) : ViewModel(), SelectDay {
    val selectedDate: StateFlow<LocalDate>
        get() = _selectedDate
    private val _selectedDate: MutableStateFlow<LocalDate> =
        MutableStateFlow(LocalDate.now())

    val week: StateFlow<ArrayList<LocalDate>>
        get() = _week
    private val _week: MutableStateFlow<ArrayList<LocalDate>> =
        MutableStateFlow(dateUtils.daysInWeekArray(LocalDate.now()))

    fun nextWeek() {
        viewModelScope.launch(dispatcher) {
            _week.emit(dateUtils.daysInWeekArray(selectedDate.value.plusWeeks(1)))
            _selectedDate.emit(week.value[0])
        }
    }

    fun previousWeek() {
        viewModelScope.launch(dispatcher) {
            _week.emit(dateUtils.daysInWeekArray(selectedDate.value.minusWeeks(1)))
            _selectedDate.emit(week.value[6])
        }
    }

    fun restore(
        selectedDateBundleWrapper: BundleWrapper.String,
        weekBundleWrapper: BundleWrapper.StringArray
    ) {
        viewModelScope.launch {
            val selectedDateString = selectedDateBundleWrapper.restore()
            val weekStringArray = weekBundleWrapper.restore()
            withContext(dispatcher) {
                _selectedDate.emit(LocalDate.parse(selectedDateString))
                _week.emit(ArrayList(weekStringArray.map { LocalDate.parse(it) }))
            }
        }
    }

    fun save(
        selectedDateBundleWrapper: BundleWrapper.String,
        weekBundleWrapper: BundleWrapper.StringArray
    ) {
        viewModelScope.launch {
            val selectedDate = selectedDate.value.toString()
            val weekList = week.value.map { it.toString() }
            selectedDateBundleWrapper.save(selectedDate)
            weekBundleWrapper.save(ArrayList(weekList))
        }
    }

    override fun selectDay(currentDay: LocalDate) {
        viewModelScope.launch(dispatcher) {
            _selectedDate.emit(currentDay)
        }
    }
}
