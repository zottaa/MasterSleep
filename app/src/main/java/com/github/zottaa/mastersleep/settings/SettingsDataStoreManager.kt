package com.github.zottaa.mastersleep.settings

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("settings")

class SettingsDataStoreManager @Inject constructor(
    @ApplicationContext appContext: Context
) {
    private val settingsDataStore = appContext.dataStore
    suspend fun setTheme(theme: Themes) {
        settingsDataStore.edit { settings ->
            settings[FIELD_THEME] = theme.code
        }
    }

    suspend fun setLanguage(language: Languages) {
        settingsDataStore.edit { settings ->
            settings[FIELD_LANGUAGE] = language.code
        }
    }

    fun readLanguage(): Flow<Languages> =
        settingsDataStore.data.map { settings ->
            Languages.getByValue(settings[FIELD_LANGUAGE] ?: 0)
        }

    fun readTheme(): Flow<Themes> =
        settingsDataStore.data.map { settings ->
            Themes.getByValue(settings[FIELD_THEME] ?: 0)
        }

    companion object {
        private val FIELD_THEME = intPreferencesKey("theme")
        private val FIELD_LANGUAGE = intPreferencesKey("language")
    }
}