package com.github.zottaa.mastersleep.alarmclock.core

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("subscribe")

@Singleton
class SubscribeDataStoreManager @Inject constructor(
    @ApplicationContext appContext: Context
) {
    private val subscribeDataStore = appContext.dataStore

    suspend fun setSubscribeStatus(status: Boolean) {
        subscribeDataStore.edit { subscribes ->
            subscribes[FIELD_STATUS] = status
        }
    }

    fun readSubscribeStatus(): Flow<Boolean> =
        subscribeDataStore.data.map { subscribes ->
            subscribes[FIELD_STATUS] ?: false
        }

    companion object {
        private val FIELD_STATUS = booleanPreferencesKey("status")
    }
}