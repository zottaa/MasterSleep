package com.github.zottaa.mastersleep

import com.github.zottaa.mastersleep.streaks.StreaksDataStoreManager
import com.github.zottaa.mastersleep.streaks.StreaksViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals


class StreaksViewModelTest {
    private lateinit var dispatcher: CoroutineDispatcher

    @Before
    fun setup() {
        dispatcher = Dispatchers.Unconfined
    }

    @Test
    fun getStreaks() {
        val streaksDataStore = FakeStreaksDataStoreManager.Base(
            day = 1L,
            diaryStreak = 1L,
            sleepStreak = 1L,
            maxDiaryStreak = 0L,
            maxSleepStreak = 0L
        )
        val viewModel = StreaksViewModel(
            streaksDataStore,
            dispatcher
        )
        viewModel.init()
        assertEquals(1L, viewModel.currentStreakDiary.value)
        assertEquals(1L, viewModel.currentStreakSleep.value)
    }

    @Test
    fun getStreaksThatNeededToBeCleared() {
        val streaksDataStore = FakeStreaksDataStoreManager.Base(
            day = 2L,
            diaryStreak = 1L,
            sleepStreak = 1L,
            maxDiaryStreak = 1L,
            maxSleepStreak = 1L
        )
        val viewModel = StreaksViewModel(
            streaksDataStore,
            dispatcher
        )
        viewModel.init()
        assertEquals(0L, viewModel.currentStreakDiary.value)
        assertEquals(0L, viewModel.currentStreakSleep.value)
        assertEquals(1L, viewModel.maxStreakDiary.value)
        assertEquals(1L, viewModel.maxStreakSleep.value)
    }

    private interface FakeStreaksDataStoreManager : StreaksDataStoreManager {
        fun isNeedToBeClear(): Boolean
        class Base(
            private val day: Long,
            private var diaryStreak: Long,
            private var sleepStreak: Long,
            private var maxDiaryStreak: Long,
            private var maxSleepStreak: Long
        ) : FakeStreaksDataStoreManager {
            var lastUpdateDay = 0L

            override fun isNeedToBeClear() =
                lastUpdateDay != day && lastUpdateDay != day - 1


            override suspend fun getDiaryStreak(): Flow<Long> {
                if (isNeedToBeClear())
                    diaryStreak = 0
                return flowOf(diaryStreak)
            }

            override suspend fun getSleepStreak(): Flow<Long> {
                if (isNeedToBeClear())
                    sleepStreak = 0
                return flowOf(sleepStreak)
            }

            override fun getSleepStreakMax(): Flow<Long> =
                flowOf(maxSleepStreak)

            override fun getDiaryStreakMax(): Flow<Long> =
                flowOf(maxDiaryStreak)

            override suspend fun updateDiaryStreak() {
                throw IllegalStateException("Not used in test")
            }

            override suspend fun updateSleepStreak() {
                throw IllegalStateException("Not used in test")
            }

        }
    }
}