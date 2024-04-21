package com.github.zottaa.mastersleep.core

import java.time.Instant
import java.time.LocalDateTime
import java.time.chrono.IsoChronology
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import java.util.Locale
import java.util.TimeZone


interface DateTimeUtils {
    fun stringTimeFromLong(dateTime: Long): String

    fun localDateTimeToString(localDateTime: LocalDateTime): String

    fun longToLocalDateTime(dateTime: Long): LocalDateTime

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

        private fun usesAmPm(locale: Locale): Boolean {
            val pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                FormatStyle.MEDIUM, FormatStyle.MEDIUM, IsoChronology.INSTANCE, locale
            )
            return pattern.contains("a")
        }
    }
}