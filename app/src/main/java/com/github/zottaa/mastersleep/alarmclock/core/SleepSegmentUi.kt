package com.github.zottaa.mastersleep.alarmclock.core

data class SleepSegmentUi(
    private val startTime: Long,
    private val sleepStart: Long,
    private val sleepEnd: Long,
    private val alarmTime: Long
)