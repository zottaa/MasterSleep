package com.github.zottaa.mastersleep.diary.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zottaa.mastersleep.core.BundleWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val dateUtils: DateUtils
) : ViewModel(), SelectDay {

    val weekLiveData: LiveData<ArrayList<LocalDate>>
        get() = _weekLiveData
    private val _weekLiveData: MutableLiveData<ArrayList<LocalDate>> = MutableLiveData()

    val selectedDateLiveData: LiveData<LocalDate>
        get() = _selectedDateLiveData
    private val _selectedDateLiveData: MutableLiveData<LocalDate> = MutableLiveData()
    fun init() {
        _selectedDateLiveData.value = LocalDate.now()
        _weekLiveData.value = dateUtils.daysInWeekArray(selectedDateLiveData.value!!)
    }

    fun nextWeek() {
        _weekLiveData.value = dateUtils.daysInWeekArray(selectedDateLiveData.value!!.plusWeeks(1))
        _selectedDateLiveData.value = weekLiveData.value!![0]
    }

    fun previousWeek() {
        _weekLiveData.value = dateUtils.daysInWeekArray(selectedDateLiveData.value!!.minusWeeks(1))
        _selectedDateLiveData.value = weekLiveData.value!![6]
    }

    override fun selectDay(currentDay: LocalDate) {
        _selectedDateLiveData.value = currentDay
        println(currentDay.dayOfYear.toString())
    }

    fun restore(
        selectedDateBundleWrapper: BundleWrapper.String,
        weekBundleWrapper: BundleWrapper.StringArray
    ) {
        val selectedDateString = selectedDateBundleWrapper.restore()
        val weekStringArray = weekBundleWrapper.restore()

        _selectedDateLiveData.value = LocalDate.parse(selectedDateString)
        _weekLiveData.value = ArrayList(weekStringArray.map { LocalDate.parse(it) })
    }

    fun save(
        selectedDateBundleWrapper: BundleWrapper.String,
        weekBundleWrapper: BundleWrapper.StringArray
    ) {
        val selectedDate = selectedDateLiveData.value?.toString() ?: ""
        val weekList = weekLiveData.value?.map { it.toString() } ?: emptyList()

        selectedDateBundleWrapper.save(selectedDate)
        weekBundleWrapper.save(ArrayList(weekList))
    }
}