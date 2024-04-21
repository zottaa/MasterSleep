package com.github.zottaa.mastersleep.statistic.diary

import androidx.core.util.Pair
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.core.DateUtils
import com.github.zottaa.mastersleep.core.Dispatcher
import com.github.zottaa.mastersleep.core.DispatcherType
import com.github.zottaa.mastersleep.diary.core.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryStatisticViewModel @Inject constructor(
    private val notesRepository: NotesRepository.ReadList,
    private val dateUtils: DateUtils,
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher,
    private val mostFrequentWordsCalculate: MostFrequentWordsCalculate
) : ViewModel() {
    val uiState: StateFlow<UiState>
        get() = _uiState
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)

    val wordsFrequency: StateFlow<List<WordFrequency>>
        get() = _wordsFrequency
    private val _wordsFrequency: MutableStateFlow<List<WordFrequency>> = MutableStateFlow(
        arrayListOf()
    )

    val nothingFoundSnackBar: StateFlow<Boolean>
        get() = _nothingFoundSnackBar
    private val _nothingFoundSnackBar: MutableStateFlow<Boolean> = MutableStateFlow(false)

    fun resetSnackBar() {
        viewModelScope.launch(dispatcher) {
            _nothingFoundSnackBar.emit(false)
        }
    }

    fun findWordsFrequency(dateRange: Pair<String, String>) {
        viewModelScope.launch(dispatcher) {
            _uiState.emit(UiState.Progress)
            val notes = notesRepository.notesInEpochDayRange(
                dateUtils.stringToEpochDay(dateRange.first),
                dateUtils.stringToEpochDay(dateRange.second)
            ).map { it.toUi() }
            val words = notes.flatMap { it.words() }
            val wordsFrequency = mostFrequentWordsCalculate.calculate(words)
            _wordsFrequency.emit(wordsFrequency)
            if (wordsFrequency.isNotEmpty())
                _uiState.emit(UiState.Show)
            else {
                _uiState.emit(UiState.NothingFound)
                _nothingFoundSnackBar.emit(true)
            }
        }
    }
}