package com.github.zottaa.mastersleep.settings

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
class SettingsViewModel @Inject constructor(
    private val settingsDataStoreManager: SettingsDataStoreManager,
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    val theme: StateFlow<Themes>
        get() = _theme
    private val _theme: MutableStateFlow<Themes> =
        MutableStateFlow(Themes.DEFAULT)

    val language: StateFlow<Languages>
        get() = _language
    private val _language: MutableStateFlow<Languages> =
        MutableStateFlow(Languages.ENGLISH)

    val configurationChange: StateFlow<Boolean>
        get() = _configurationChange
    private val _configurationChange: MutableStateFlow<Boolean> =
        MutableStateFlow(false)

    fun init() {
        viewModelScope.launch(dispatcher) {
            _language.emit(
                settingsDataStoreManager.readLanguage().first()
            )
            _theme.emit(
                settingsDataStoreManager.readTheme().first()
            )
            _configurationChange.emit(false)
        }
    }

    fun changeLocale(chosenLanguage: Languages) {
        viewModelScope.launch {
            settingsDataStoreManager.setLanguage(chosenLanguage)
            configurationChange()
        }
    }

    fun changeTheme(chosenTheme: Themes) {
        viewModelScope.launch {
            settingsDataStoreManager.setTheme(chosenTheme)
            configurationChange()
        }
    }

    private suspend fun configurationChange() {
        _configurationChange.emit(true)
    }
}