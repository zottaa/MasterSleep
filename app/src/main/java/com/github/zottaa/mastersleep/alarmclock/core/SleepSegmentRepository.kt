package com.github.zottaa.mastersleep.alarmclock.core

import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface SleepSegmentRepository {
    interface Read {
        suspend fun sleepSegments(startDate: Long, endDate: Long): List<SleepSegment>
    }

    interface Create {
        suspend fun create(sleepEndTime: Long)
    }

    interface All : Read, Create

    class Base @Inject constructor(
        private val alarmDataStoreManager: AlarmDataStoreManager,
        private val dao: SleepSegmentDao
    ) : All {
        override suspend fun sleepSegments(startDate: Long, endDate: Long): List<SleepSegment> =
            dao.sleepSegment(startDate, endDate)
                .map { SleepSegment(it.startTime, it.sleepStart, it.sleepEnd, it.alarmTime) }


        override suspend fun create(sleepEndTime: Long) {
            dao.insert(
                SleepSegmentCache(
                    alarmDataStoreManager.readAlarm().first(),
                    alarmDataStoreManager.readSleepStart().first(),
                    sleepEndTime,
                    alarmDataStoreManager.readStartTime().first()
                )
            )
        }

    }
}

data class SleepSegment(
    val startTime: Long,
    val sleepStart: Long,
    val sleepEnd: Long,
    val alarmTime: Long
)