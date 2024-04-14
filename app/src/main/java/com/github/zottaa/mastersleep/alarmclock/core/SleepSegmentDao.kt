package com.github.zottaa.mastersleep.alarmclock.core

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SleepSegmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sleepSegment: SleepSegmentCache)

    @Query("SELECT * FROM sleep_segments where alarm_time between :startDate and :endDate")
    suspend fun sleepSegment(startDate: Long, endDate: Long): List<SleepSegmentCache>
}