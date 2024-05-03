package com.github.zottaa.mastersleep.statistic.sleep

import androidx.core.util.Pair
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.BarEntry
import com.github.zottaa.mastersleep.alarmclock.core.SleepSegmentRepository
import com.github.zottaa.mastersleep.core.DateUtils
import com.github.zottaa.mastersleep.core.Dispatcher
import com.github.zottaa.mastersleep.core.DispatcherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SleepStatisticViewModel @Inject constructor(
    private val sleepSegmentRepository: SleepSegmentRepository.Read,
    private val dateUtils: DateUtils,
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher,
    private val chartDataSetBuilder: ChartDataSetBuilder
) : ViewModel() {
    val uiState: StateFlow<SleepStatisticUiState>
        get() = _uiState
    private val _uiState: MutableStateFlow<SleepStatisticUiState> =
        MutableStateFlow(SleepStatisticUiState.Initial)
    val sleepDuration: StateFlow<List<BarEntry>>
        get() = _sleepDuration
    private val _sleepDuration: MutableStateFlow<List<BarEntry>> = MutableStateFlow(emptyList())

    val timeToFallAsleep: StateFlow<List<BarEntry>>
        get() = _timeToFallAsleep
    private val _timeToFallAsleep: MutableStateFlow<List<BarEntry>> = MutableStateFlow(emptyList())

    val timeWhenFallAsleep: StateFlow<List<BarEntry>>
        get() = _timeWhenFallAsleep
    private val _timeWhenFallAsleep: MutableStateFlow<List<BarEntry>> =
        MutableStateFlow(emptyList())

    val wakeUpTime: StateFlow<List<BarEntry>>
        get() = _wakeUpTime
    private val _wakeUpTime: MutableStateFlow<List<BarEntry>> = MutableStateFlow(emptyList())

    fun buildCharts(dateRange: Pair<String, String>) {
        viewModelScope.launch(dispatcher) {
            _uiState.emit(SleepStatisticUiState.Progress)
            val sleepSegments = sleepSegmentRepository.sleepSegments(
                dateUtils.stringDateToLongUTC(dateRange.first),
                dateUtils.stringDateToLongUTC(dateRange.second)
            )

            val sleepDurationDataSet =
                chartDataSetBuilder.sleepDurationDataSet(
                    Pair(
                        dateUtils.stringToEpochDay(dateRange.first),
                        dateUtils.stringToEpochDay(dateRange.second)
                    ),
                    sleepSegments
                )
            _sleepDuration.emit(sleepDurationDataSet)

            val timeToFallAsleepDataSet = chartDataSetBuilder.timeToFallAsleepDataSet(
                Pair(
                    dateUtils.stringToEpochDay(dateRange.first),
                    dateUtils.stringToEpochDay(dateRange.second)
                ),
                sleepSegments
            )
            _timeToFallAsleep.emit(timeToFallAsleepDataSet)

            val timeWhenFallAsleepDataSet = chartDataSetBuilder.timeWhenFallAsleepDataSet(
                Pair(
                    dateUtils.stringToEpochDay(dateRange.first),
                    dateUtils.stringToEpochDay(dateRange.second)
                ),
                sleepSegments
            )
            _timeWhenFallAsleep.emit(timeWhenFallAsleepDataSet)

            val wakeUpTimeDataSet = chartDataSetBuilder.wakeUpTimeDataSet(
                Pair(
                    dateUtils.stringToEpochDay(dateRange.first),
                    dateUtils.stringToEpochDay(dateRange.second)
                ),
                sleepSegments
            )
            _wakeUpTime.emit(wakeUpTimeDataSet)

            _uiState.emit(SleepStatisticUiState.Show)
        }
    }
}