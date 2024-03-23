package com.github.zottaa.mastersleep.diary.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.diary.core.NoteUi
import com.github.zottaa.mastersleep.diary.core.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DiaryEditViewModel @Inject constructor(
    private val repository: NotesRepository.Edit,
    @Named("IO")
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    val noteLiveData: StateFlow<NoteUi>
        get() = _noteLiveData
    private val _noteLiveData: MutableStateFlow<NoteUi> = MutableStateFlow(NoteUi(-1L, "", "", 0L))

    fun init(noteId: Long) {
        viewModelScope.launch(dispatcher) {
            val note = repository.note(noteId).toUi()
            _noteLiveData.emit(note)
        }
    }

    fun delete(noteId: Long) {
        viewModelScope.launch(dispatcher) {
            repository.deleteNote(noteId)
        }
    }

    fun update(
        noteId: Long,
        newTitle: String,
        newContent: String
    ) {
        viewModelScope.launch(dispatcher) {
            repository.updateNote(noteId, newTitle, newContent)
        }
    }
}