package com.github.zottaa.mastersleep.diary.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.zottaa.mastersleep.databinding.CalendarItemBinding
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class CalendarAdapter(
    private val backgroundColor: Int,
    private val accentColor: Int,
    private val selectDay: SelectDay,
    private var days: ArrayList<LocalDate> = ArrayList(),
    private var selectedDate: LocalDate = LocalDate.now()
) : RecyclerView.Adapter<CalendarViewHolder>() {
    fun update(newList: ArrayList<LocalDate>) {
        val diffUtil = CalendarDaysDiffUtil(days, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        days = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateDate(currentDay: LocalDate) {
        if (selectedDate != currentDay) {
            val oldIndex = days.indexOf(selectedDate)
            selectedDate = currentDay
            notifyItemChanged(oldIndex)
            notifyItemChanged(days.indexOf(selectedDate))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarItemBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return CalendarViewHolder(binding, selectDay, backgroundColor, accentColor)
    }

    override fun getItemCount(): Int = days.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.hold(days, position, selectedDate)
    }
}

class CalendarViewHolder(
    private val binding: CalendarItemBinding,
    private val selectDay: SelectDay,
    private val backgroundColor: Int,
    private val accentColor: Int
) : RecyclerView.ViewHolder(binding.root) {

    fun hold(days: ArrayList<LocalDate>, position: Int, selectedDate: LocalDate) {
        val day = days[position]

        binding.calendarItemDayName.text =
            day.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        binding.calendarItemDayNumber.text = day.dayOfMonth.toString()

        if (day.isEqual(selectedDate)) {
            binding.calendarItemRoot.setBackgroundColor(accentColor)
        } else {
            binding.calendarItemRoot.setBackgroundColor(backgroundColor)
        }

        binding.calendarItemRoot.setOnClickListener {
            selectDay.selectDay(days[position])
        }
    }
}

class CalendarDaysDiffUtil(
    private val oldList: List<LocalDate>,
    private val newList: List<LocalDate>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === (newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].isEqual(newList[newItemPosition])
}