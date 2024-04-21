package com.github.zottaa.mastersleep.core

import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

interface DateUtils {
    fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate>

    fun dateFromLong(dateInMilliSeconds: Long): String

    fun stringDate(date: LocalDate, pattern: DateTimeFormatter): String

    fun stringDate(date: LocalDate): String
    fun stringDateToLong(date: String, pattern: SimpleDateFormat): Long

    fun stringDateToLong(date: String): Long

    fun stringToEpochDay(date: String): Long


    class Base : DateUtils {
        override fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate> {
            val days = ArrayList<LocalDate>()

            var current = selectedDate.with(DayOfWeek.MONDAY)

            while (current.dayOfWeek != DayOfWeek.SUNDAY) {
                days.add(current)
                current = current.plusDays(1)
            }

            days.add(current)

            return days
        }

        override fun dateFromLong(dateInMilliSeconds: Long): String {
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = dateInMilliSeconds
            val pattern = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return pattern.format(utc.time)
        }

        override fun stringDate(date: LocalDate, pattern: DateTimeFormatter): String {
            return pattern.format(date)
        }


        override fun stringDate(date: LocalDate) =
            stringDate(date, DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault()))

        override fun stringDateToLong(date: String, pattern: SimpleDateFormat): Long {
            val parsedDate = pattern.parse(date)
            if (parsedDate != null) {
                return parsedDate.time
            }
            return 0L
        }

        override fun stringDateToLong(date: String): Long =
            stringDateToLong(date, SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()))

        override fun stringToEpochDay(date: String): Long {
            val dateLong = stringDateToLong(date)
            val localDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(dateLong),
                TimeZone.getDefault().toZoneId()
            ).toLocalDate()
            return localDate.toEpochDay()
        }

    }
}