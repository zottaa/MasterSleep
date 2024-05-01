package com.github.zottaa.mastersleep.statistic.sleep

import androidx.core.util.Pair
import com.github.mikephil.charting.data.BarEntry
import com.github.zottaa.mastersleep.alarmclock.core.SleepSegment
import com.github.zottaa.mastersleep.core.DateTimeUtils
import javax.inject.Inject

interface ChartDataSetBuilder {
    fun sleepDurationDataSet(
        epochRange: Pair<Long, Long>,
        sleepSegments: List<SleepSegment>
    ): List<BarEntry>

    fun timeToFallAsleepDataSet(
        epochRange: Pair<Long, Long>,
        sleepSegments: List<SleepSegment>
    ): List<BarEntry>

    fun timeWhenFallAsleepDataSet(
        epochRange: Pair<Long, Long>,
        sleepSegments: List<SleepSegment>
    ): List<BarEntry>

    fun wakeUpTimeDataSet(
        epochRange: Pair<Long, Long>,
        sleepSegments: List<SleepSegment>
    ): List<BarEntry>

    class Base @Inject constructor(
        private val dateTimeUtils: DateTimeUtils
    ) : ChartDataSetBuilder {
        override fun sleepDurationDataSet(
            epochRange: Pair<Long, Long>,
            sleepSegments: List<SleepSegment>
        ): List<BarEntry> = (epochRange.first..epochRange.second).map { epochDay ->
            val sleepSegment = sleepSegments.find { segment ->
                dateTimeUtils.isInEpochDay(epochDay, segment.startTime)
            }
            val duration = sleepSegment?.let {
                it.sleepEnd - it.sleepStart.toFloat()
            } ?: 0f
            BarEntry(epochDay.toFloat(), duration)
        }

        override fun timeToFallAsleepDataSet(
            epochRange: Pair<Long, Long>,
            sleepSegments: List<SleepSegment>
        ): List<BarEntry> = (epochRange.first..epochRange.second).map { epochDay ->
            val sleepSegment = sleepSegments.find { segment ->
                dateTimeUtils.isInEpochDay(epochDay, segment.startTime)
            }
            val timeToFallAsleep = sleepSegment?.let {
                it.sleepStart.toFloat() - it.startTime
            } ?: 0f
            BarEntry(epochDay.toFloat(), timeToFallAsleep)
        }

        override fun timeWhenFallAsleepDataSet(
            epochRange: Pair<Long, Long>,
            sleepSegments: List<SleepSegment>
        ): List<BarEntry> = (epochRange.first..epochRange.second).map { epochDay ->
            val sleepSegment = sleepSegments.find { segment ->
                dateTimeUtils.isInEpochDay(epochDay, segment.startTime)
            }
            val timeWhenFallAsleep = sleepSegment?.sleepStart?.toFloat() ?: 0f
            BarEntry(epochDay.toFloat(), timeWhenFallAsleep)
        }

        override fun wakeUpTimeDataSet(
            epochRange: Pair<Long, Long>,
            sleepSegments: List<SleepSegment>
        ): List<BarEntry> = (epochRange.first..epochRange.second).map { epochDay ->
            val sleepSegment = sleepSegments.find { segment ->
                dateTimeUtils.isInEpochDay(epochDay, segment.startTime)
            }
            val wakeUpTime = sleepSegment?.sleepEnd?.toFloat() ?: 0f
            BarEntry(epochDay.toFloat(), wakeUpTime)
        }
    }
}