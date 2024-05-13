package com.github.zottaa.mastersleep.alarmclock.core

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "sleep_segments"
)
data class SleepSegmentCache(
    @PrimaryKey
    @ColumnInfo(name = "start_time")
    val startTime: Long,
    @ColumnInfo(name = "sleep_start")
    val sleepStart: Long,
    @ColumnInfo(name = "sleep_end")
    val sleepEnd: Long,
    @ColumnInfo(name = "alarm_time")
    val alarmTime: Long
)