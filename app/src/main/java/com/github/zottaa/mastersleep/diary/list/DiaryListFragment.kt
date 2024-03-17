package com.github.zottaa.mastersleep.diary.list

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentDiaryListBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DiaryListFragment : AbstractFragment<FragmentDiaryListBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val date = LocalDate.now()
        binding.currentMonthYearTextView.text =
            date.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        val days = daysInWeekArray(date)

        val adapter = CalendarAdapter(selectedDate = date)

        binding.calendarRecyclerView.adapter = adapter
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)

        adapter.update(days)
    }

    private fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate> {
        val days = ArrayList<LocalDate>()

        var current = selectedDate.with(DayOfWeek.MONDAY)

        while (current.dayOfWeek != DayOfWeek.SUNDAY) {
            days.add(current)
            current = current.plusDays(1)
        }

        days.add(current)

        return days
    }

    private fun sundayForDate(currentDate: LocalDate): LocalDate {
        val oneWeekAgo = currentDate.minusWeeks(1)

        var current = currentDate

        while (current.isAfter(oneWeekAgo)) {
            if (current.dayOfWeek == DayOfWeek.SUNDAY)
                return current

            current = current.minusDays(1)
        }
        return current
    }
}