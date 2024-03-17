package com.github.zottaa.mastersleep.diary.list

import java.time.DayOfWeek
import java.time.LocalDate

interface DateUtils {
    fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate>

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
    }
}