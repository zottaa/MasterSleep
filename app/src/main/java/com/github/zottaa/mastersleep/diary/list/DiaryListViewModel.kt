package com.github.zottaa.mastersleep.diary.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.core.Dispatcher
import com.github.zottaa.mastersleep.core.DispatcherType
import com.github.zottaa.mastersleep.diary.core.NoteUi
import com.github.zottaa.mastersleep.diary.core.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryListViewModel @Inject constructor(
    private val repository: NotesRepository.ReadList,
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher
) : ViewModel(), SelectDay {
    val notes: StateFlow<List<NoteUi>>
        get() = _notes
    private val _notes: MutableStateFlow<List<NoteUi>> = MutableStateFlow(listOf())

    override fun selectDay(currentDay: LocalDate) {
        viewModelScope.launch(dispatcher) {
            val notes =
                repository.notes(currentDay.toEpochDay()).map { it.toUi() }
            _notes.emit(notes)
        }
    }
}