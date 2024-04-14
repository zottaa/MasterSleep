package com.github.zottaa.mastersleep.alarmclock.core

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("alarm")

@Singleton
class AlarmDataStoreManager @Inject constructor(
    @ApplicationContext appContext: Context
) {
    private val alarmDataStore = appContext.dataStore

    suspend fun setAlarm(time: Long) {
        alarmDataStore.edit { alarm ->
            alarm[FIELD_TIME] = time
        }
    }

    suspend fun setStartTime(startTime: Long) {
        alarmDataStore.edit { alarm ->
            alarm[FIELD_START_TIME] = startTime
        }
    }

    fun readAlarm(): Flow<Long> =
        alarmDataStore.data.map { alarm ->
            alarm[FIELD_TIME] ?: 0L
        }

    fun readStartTime(): Flow<Long> =
        alarmDataStore.data.map { alarm ->
            alarm[FIELD_START_TIME] ?: 0L
        }

    companion object {
        private val FIELD_TIME = longPreferencesKey("time")
        private val FIELD_START_TIME = longPreferencesKey("startTime")
    }
}