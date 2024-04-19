package com.github.zottaa.mastersleep.alarmclock.ringtone

import android.content.Context
import android.media.RingtoneManager
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("ringtone")

@Singleton
class RingtoneDataStore @Inject constructor(
    @ApplicationContext
    private val appContext: Context
) {
    private val dataStore = appContext.dataStore

    fun readRingtoneUri(): String = runBlocking {
        dataStore.data.map { ringtoneUriStore ->
            ringtoneUriStore[FIELD_RINGTONE_URI]
                ?: RingtoneManager.getActualDefaultRingtoneUri(
                    appContext,
                    RingtoneManager.TYPE_RINGTONE
                ).toString()
        }.first()
    }


    suspend fun setRingtoneUri(ringtoneUri: String) {
        dataStore.edit { ringtoneUriStore ->
            ringtoneUriStore[FIELD_RINGTONE_URI] = ringtoneUri
        }
    }

    companion object {
        private val FIELD_RINGTONE_URI = stringPreferencesKey("ringtoneUri")
    }
}