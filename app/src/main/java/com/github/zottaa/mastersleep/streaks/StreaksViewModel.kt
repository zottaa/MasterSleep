package com.github.zottaa.mastersleep.streaks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.core.Dispatcher
import com.github.zottaa.mastersleep.core.DispatcherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StreaksViewModel @Inject constructor(
    private val streaksDataStoreManager: StreaksDataStoreManager,
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    val currentStreakSleep: StateFlow<Long>
        get() = _currentStreakSleep
    private val _currentStreakSleep: MutableStateFlow<Long> = MutableStateFlow(0)

    val maxStreakSleep: StateFlow<Long>
        get() = _maxStreakSleep
    private val _maxStreakSleep: MutableStateFlow<Long> = MutableStateFlow(0)

    val currentStreakDiary: StateFlow<Long>
        get() = _currentStreakDiary
    private val _currentStreakDiary: MutableStateFlow<Long> = MutableStateFlow(0)

    val maxStreakDiary: StateFlow<Long>
        get() = _maxStreakDiary
    private val _maxStreakDiary: MutableStateFlow<Long> = MutableStateFlow(0)
    fun init() {
        viewModelScope.launch(dispatcher) {
            _currentStreakSleep.emit(streaksDataStoreManager.getSleepStreak().first())
            _maxStreakSleep.emit(streaksDataStoreManager.getSleepStreakMax().first())
            _currentStreakDiary.emit(streaksDataStoreManager.getDiaryStreak().first())
            _maxStreakDiary.emit(streaksDataStoreManager.getDiaryStreakMax().first())
        }
    }
}