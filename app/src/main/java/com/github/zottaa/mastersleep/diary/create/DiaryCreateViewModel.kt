package com.github.zottaa.mastersleep.diary.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.diary.core.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DiaryCreateViewModel @Inject constructor(
    private val repository: NotesRepository.Create,
    @Named("IO")
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    fun create(title: String, content: String, date: Long) {
        viewModelScope.launch(dispatcher) {
            repository.create(title, content, date)
        }
    }
}