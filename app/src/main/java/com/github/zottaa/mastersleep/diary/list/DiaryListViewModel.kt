package com.github.zottaa.mastersleep.diary.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.core.DateUtils
import com.github.zottaa.mastersleep.diary.core.NoteUi
import com.github.zottaa.mastersleep.diary.core.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val dateUtils: DateUtils,
    private val repository: NotesRepository.ReadList,
    @Named("IO")
    private val dispatcher: CoroutineDispatcher
) : ViewModel(), SelectDay {

    val week: StateFlow<ArrayList<LocalDate>>
        get() = _week
    private val _week: MutableStateFlow<ArrayList<LocalDate>> =
        MutableStateFlow(arrayListOf())

    val selectedDate: StateFlow<LocalDate>
        get() = _selectedDate
    private val _selectedDate: MutableStateFlow<LocalDate> =
        MutableStateFlow(LocalDate.now())

    val notes: StateFlow<List<NoteUi>>
        get() = _notes
    private val _notes: MutableStateFlow<List<NoteUi>> = MutableStateFlow(listOf())
    fun init(firstRun: Boolean) {
        if (firstRun) {
            viewModelScope.launch(dispatcher) {
                selectDay(LocalDate.now())
                _week.emit(dateUtils.daysInWeekArray(selectedDate.value))
            }
        }
    }

    fun nextWeek() {
        viewModelScope.launch {
            _week.emit(dateUtils.daysInWeekArray(selectedDate.value.plusWeeks(1)))
            withContext(dispatcher) {
                selectDay(week.value[0])
            }
        }
    }

    fun previousWeek() {
        viewModelScope.launch {
            _week.emit(dateUtils.daysInWeekArray(selectedDate.value.minusWeeks(1)))
            withContext(dispatcher) {
                selectDay(week.value[6])
            }
        }
    }

    override fun selectDay(currentDay: LocalDate) {
        viewModelScope.launch {
            _selectedDate.emit(currentDay)
            withContext(dispatcher) {
                val notes =
                    repository.notes(_selectedDate.value.toEpochDay()).map { it.toUi() }
                _notes.emit(notes)
            }
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
                selectDay(LocalDate.parse(selectedDateString))
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
}