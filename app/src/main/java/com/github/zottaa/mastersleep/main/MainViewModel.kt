package com.github.zottaa.mastersleep.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.zottaa.mastersleep.settings.Languages
import com.github.zottaa.mastersleep.settings.SettingsDataStoreManager
import com.github.zottaa.mastersleep.settings.Themes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsDataStoreManager: SettingsDataStoreManager,
    @Named("IO")
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

    fun init() {
        viewModelScope.launch(dispatcher) {
            _language.emit(
                settingsDataStoreManager.readLanguage().first()
            )
            _theme.emit(
                settingsDataStoreManager.readTheme().first()
            )
        }
    }
}