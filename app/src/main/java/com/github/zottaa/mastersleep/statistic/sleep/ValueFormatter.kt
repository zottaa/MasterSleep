package com.github.zottaa.mastersleep.statistic.sleep

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
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

    class BarChartY : ValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return LocalDateTime.ofEpochSecond(
                value.toLong(),
                0,
                ZoneOffset.UTC
            ).toLocalTime().toString()
        }
    }
}