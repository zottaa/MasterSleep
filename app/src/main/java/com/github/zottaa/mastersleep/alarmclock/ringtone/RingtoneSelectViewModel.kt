package com.github.zottaa.mastersleep.alarmclock.ringtone

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.core.Dispatcher
import com.github.zottaa.mastersleep.core.DispatcherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RingtoneSelectViewModel @Inject constructor(
    private val ringtoneDataStore: RingtoneDataStore,
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    val selectedRingtone: StateFlow<String>
        get() = _selectedRingtone
    private val _selectedRingtone: MutableStateFlow<String> = MutableStateFlow("")

    fun init() {
        viewModelScope.launch(dispatcher) {
            _selectedRingtone.emit(getRingtoneUri().toString())
        }
    }

    fun getRingtoneUri(): Uri =
        Uri.parse(ringtoneDataStore.readRingtoneUri())


    fun setRingtoneUri(ringtoneUri: Uri) {
        viewModelScope.launch(dispatcher) {
            ringtoneDataStore.setRingtoneUri(ringtoneUri.toString())
            val newRingtoneUri = ringtoneDataStore.readRingtoneUri()
            _selectedRingtone.emit(newRingtoneUri)
        }
    }
}