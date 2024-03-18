package com.github.zottaa.mastersleep.diary.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.core.SingleLiveEvent
import com.github.zottaa.mastersleep.diary.core.NoteUi
import com.github.zottaa.mastersleep.diary.core.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    private val dispatcher: CoroutineDispatcher,
    @Named("Main")
    private val dispatcherMain: CoroutineDispatcher
) : ViewModel(), SelectDay {

    val weekLiveData: LiveData<ArrayList<LocalDate>>
        get() = _weekLiveData
    private val _weekLiveData: MutableLiveData<ArrayList<LocalDate>> = SingleLiveEvent()

    val selectedDateLiveData: LiveData<LocalDate>
        get() = _selectedDateLiveData
    private val _selectedDateLiveData: MutableLiveData<LocalDate> = SingleLiveEvent()

    val notesLiveData: LiveData<List<NoteUi>>
        get() = _notesLiveData
    private val _notesLiveData: MutableLiveData<List<NoteUi>> = MutableLiveData()
    fun init(firstRun: Boolean) {
        if (firstRun) {
            selectDay(LocalDate.now())
            _weekLiveData.value = dateUtils.daysInWeekArray(selectedDateLiveData.value!!)
        }
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
        viewModelScope.launch(dispatcher) {
            val notes = repository.notes(_selectedDateLiveData.value!!.toEpochDay()).map { it.toUi() }
            withContext(dispatcherMain) {
                _notesLiveData.value = notes
            }
        }
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