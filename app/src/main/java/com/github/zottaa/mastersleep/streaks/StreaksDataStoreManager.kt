package com.github.zottaa.mastersleep.streaks

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("streaks")

interface StreaksDataStoreManager {
    suspend fun getDiaryStreak(): Flow<Long>
    suspend fun getSleepStreak(): Flow<Long>
    fun getSleepStreakMax(): Flow<Long>
    fun getDiaryStreakMax(): Flow<Long>
    suspend fun updateDiaryStreak()
    suspend fun updateSleepStreak()
    class Base @Inject constructor(
        @ApplicationContext appContext: Context
    ) : StreaksDataStoreManager {
        private val dataStore = appContext.dataStore

        override suspend fun getDiaryStreak(): Flow<Long> {
            checkDiaryStreak()
            return dataStore.data.map { streaks ->
                streaks[FIELD_DIARY_STREAK_CURRENT] ?: 0
            }
        }

        private suspend fun checkDiaryStreak() {
            dataStore.edit { streaks ->
                val today = LocalDate.now()
                val lastDateEpochDate = streaks[FIELD_DIARY_STREAK_LAST_DATE] ?: 0
                val lastDate = LocalDate.ofEpochDay(lastDateEpochDate)
                if (lastDate.plusDays(1) != today && lastDate != today) {
                    streaks[FIELD_DIARY_STREAK_CURRENT] = 0
                }
            }
        }

        override suspend fun getSleepStreak(): Flow<Long> {
            checkSleepStreak()
            return dataStore.data.map { streaks ->
                streaks[FIELD_SLEEP_STREAK_CURRENT] ?: 0
            }
        }

        private suspend fun checkSleepStreak() {
            dataStore.edit { streaks ->
                val today = LocalDate.now()
                val lastDateEpochDate = streaks[FIELD_SLEEP_STREAK_LAST_DATE] ?: 0
                val lastDate = LocalDate.ofEpochDay(lastDateEpochDate)
                if (lastDate.plusDays(1) != today && lastDate != today) {
                    streaks[FIELD_SLEEP_STREAK_CURRENT] = 0
                }
            }
        }

        override fun getSleepStreakMax(): Flow<Long> =
            dataStore.data.map { streaks ->
                streaks[FIELD_SLEEP_STREAK_MAX] ?: 0
            }


        override fun getDiaryStreakMax(): Flow<Long> =
            dataStore.data.map { streaks ->
                streaks[FIELD_DIARY_STREAK_MAX] ?: 0
            }

        override suspend fun updateDiaryStreak() {
            dataStore.edit { streaks ->
                val today = LocalDate.now()
                val lastDateEpochDate = streaks[FIELD_DIARY_STREAK_LAST_DATE] ?: 0
                val lastDate = LocalDate.ofEpochDay(lastDateEpochDate)
                val streak = streaks[FIELD_DIARY_STREAK_CURRENT] ?: 0
                if (lastDate != today) {
                    val newStreak = if (lastDate.plusDays(1) == today)
                        streak + 1
                    else
                        1
                    val maxStreak = streaks[FIELD_DIARY_STREAK_MAX] ?: 0
                    streaks[FIELD_DIARY_STREAK_LAST_DATE] = today.toEpochDay()
                    streaks[FIELD_DIARY_STREAK_CURRENT] = newStreak
                    if (newStreak > maxStreak)
                        streaks[FIELD_DIARY_STREAK_MAX] = newStreak
                }
            }
        }

        override suspend fun updateSleepStreak() {
            dataStore.edit { streaks ->
                val today = LocalDate.now()
                val lastDateEpochDate = streaks[FIELD_SLEEP_STREAK_LAST_DATE] ?: 0
                val lastDate = LocalDate.ofEpochDay(lastDateEpochDate)
                val streak = streaks[FIELD_SLEEP_STREAK_CURRENT] ?: 0
                if (lastDate != today) {
                    val newStreak = if (lastDate.plusDays(1) == today)
                        streak + 1
                    else
                        1
                    val maxStreak = streaks[FIELD_SLEEP_STREAK_MAX] ?: 0
                    streaks[FIELD_SLEEP_STREAK_LAST_DATE] = today.toEpochDay()
                    streaks[FIELD_SLEEP_STREAK_CURRENT] = newStreak
                    if (newStreak > maxStreak)
                        streaks[FIELD_SLEEP_STREAK_MAX] = newStreak
                }
            }
        }

        companion object {
            private val FIELD_SLEEP_STREAK_CURRENT = longPreferencesKey("sleepStreakCurrent")
            private val FIELD_SLEEP_STREAK_MAX = longPreferencesKey("sleepStreakMax")
            private val FIELD_SLEEP_STREAK_LAST_DATE = longPreferencesKey("sleepStreakLastDate")
            private val FIELD_DIARY_STREAK_CURRENT = longPreferencesKey("diaryStreakCurrent")
            private val FIELD_DIARY_STREAK_MAX = longPreferencesKey("diaryStreakMax")
            private val FIELD_DIARY_STREAK_LAST_DATE = longPreferencesKey("diaryStreakLastDate")
        }
    }
}