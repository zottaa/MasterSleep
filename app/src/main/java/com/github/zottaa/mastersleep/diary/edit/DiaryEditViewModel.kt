package com.github.zottaa.mastersleep.diary.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.diary.core.NoteUi
import com.github.zottaa.mastersleep.diary.core.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DiaryEditViewModel @Inject constructor(
    private val repository: NotesRepository.Edit,
    @Named("IO")
    private val dispatcher: CoroutineDispatcher,
    @Named("Main")
    private val dispatcherMain: CoroutineDispatcher
) : ViewModel() {

    val noteLiveData: LiveData<NoteUi>
        get() = _noteLiveData
    private val _noteLiveData: MutableLiveData<NoteUi> = MutableLiveData()

    fun init(noteId: Long) {
        viewModelScope.launch(dispatcher) {
            val note = repository.note(noteId).toUi()
            withContext(dispatcherMain) {
                _noteLiveData.value = note
            }
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