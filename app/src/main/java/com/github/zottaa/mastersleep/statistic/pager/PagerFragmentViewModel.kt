package com.github.zottaa.mastersleep.statistic.pager

import androidx.core.util.Pair
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.core.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class PagerFragmentViewModel @Inject constructor(
    private val dateUtils: DateUtils,
    @Named("IO")
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    val dateRange: StateFlow<Pair<String, String>>
        get() = _dateRange
    private val _dateRange: MutableStateFlow<Pair<String, String>> = MutableStateFlow(
        Pair(
            dateUtils.stringDate(LocalDate.now().minusWeeks(1)),
            dateUtils.stringDate(LocalDate.now())
        )
    )

    fun pickDateRange(begin: Long, end: Long) {
        viewModelScope.launch(dispatcher) {
            _dateRange.emit(Pair(dateUtils.dateFromLong(begin), dateUtils.dateFromLong(end)))
        }
    }

    fun restore(
        pairBundleWrapper: BundleWrapper.StringArray
    ) {
        viewModelScope.launch {
            val dateRangeInStrings = pairBundleWrapper.restore()
            _dateRange.emit(Pair(dateRangeInStrings[0], dateRangeInStrings[1]))
        }
    }

    fun save(
        pairBundleWrapper: BundleWrapper.StringArray
    ) {
        val dataRangeInStrings = arrayListOf(dateRange.value.first, dateRange.value.second)
        pairBundleWrapper.save(dataRangeInStrings)
    }
}