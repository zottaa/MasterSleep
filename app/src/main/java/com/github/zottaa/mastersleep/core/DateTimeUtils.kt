package com.github.zottaa.mastersleep.core

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale
import java.util.TimeZone


interface DateTimeUtils {
    fun stringTimeFromLong(dateTime: Long): String

    fun localDateTimeToString(localDateTime: LocalDateTime): String

    fun longToLocalDateTime(dateTime: Long): LocalDateTime

    fun isInEpochDay(epochDay: Long, timeToCheck: Long): Boolean

    class Base : DateTimeUtils {
        override fun stringTimeFromLong(dateTime: Long) =
            localDateTimeToString(longToLocalDateTime(dateTime))


        override fun localDateTimeToString(localDateTime: LocalDateTime): String {
            return if (usesAmPm(Locale.getDefault())) {
                val amPm = if (localDateTime.hour < 12) "AM" else "PM"
                val hour =
                    if (localDateTime.hour > 12) localDateTime.hour - 12 else localDateTime.hour
                String.format(
                    "%02d:%02d %s %02d/%02d/%04d",
                    hour,
                    localDateTime.minute,
                    amPm,
                    localDateTime.dayOfMonth,
                    localDateTime.monthValue,
                    localDateTime.year
                )
            } else {
                String.format(
                    "%02d:%02d %02d/%02d/%04d",
                    localDateTime.hour,
                    localDateTime.minute,
                    localDateTime.dayOfMonth,
                    localDateTime.monthValue,
                    localDateTime.year
                )
            }
        }

        override fun longToLocalDateTime(dateTime: Long): LocalDateTime =
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(dateTime),
                TimeZone.getDefault().toZoneId()
            )

        override fun isInEpochDay(epochDay: Long, timeToCheck: Long): Boolean {
            val day = LocalDateTime.of(LocalDate.ofEpochDay(epochDay), LocalTime.MIN).toEpochSecond(
                ZoneOffset.of(ZoneId.systemDefault().id)
            )
            val nextDay =
                LocalDateTime.of(LocalDate.ofEpochDay(epochDay + 1), LocalTime.MIN).toEpochSecond(
                    ZoneOffset.of(ZoneId.systemDefault().id)
                )
            return timeToCheck in day..<nextDay
        }

        private fun usesAmPm(locale: Locale): Boolean {
            val pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                FormatStyle.MEDIUM, FormatStyle.MEDIUM, IsoChronology.INSTANCE, locale
            )
            return pattern.contains("a")
        }
    }
}