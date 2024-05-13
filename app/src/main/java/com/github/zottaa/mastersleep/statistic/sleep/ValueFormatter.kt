package com.github.zottaa.mastersleep.statistic.sleep

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

interface ValueFormatters {
    class BarChartX : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return LocalDate.ofEpochDay(value.toLong())
                .format(DateTimeFormatter.ofPattern("dd/MM/yy"))
        }
    }

    class BarChartYToTime : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val milliSeconds = value.toLong()
            val seconds = milliSeconds / 1000
            val hours = seconds / 3600
            val remainingSeconds = seconds % 3600
            val minutes = remainingSeconds / 60
            return "$hours:$minutes"
        }
    }

    class BarChartYToDayTime : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val date = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(value.toLong()),
                ZoneOffset.UTC
            )
            return "${date.hour}:${date.minute}"
        }
    }
}