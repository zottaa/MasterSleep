package com.github.zottaa.mastersleep.diary.list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.zottaa.mastersleep.databinding.CalendarItemBinding
import java.time.LocalDate

class CalendarAdapter(
    private var days: ArrayList<LocalDate> = ArrayList(),
    private var selectedDate: LocalDate
) : RecyclerView.Adapter<CalendarViewHolder>() {
    fun update(newList: ArrayList<LocalDate>) {
        val diffUtil = CalendarDaysDiffUtil(days, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        days = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateDate(newSelectedDate: LocalDate) {
        selectedDate = newSelectedDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalendarItemBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        parent.layoutParams.height = parent.height
        return CalendarViewHolder(binding)
    }

    override fun getItemCount(): Int = days.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.hold(days, position, selectedDate)
    }
}

class CalendarViewHolder(
    private val binding: CalendarItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun hold(days: ArrayList<LocalDate>, position: Int, selectedDate: LocalDate) {
        val day = days[position]

        binding.calendarItemDayName.text = day.dayOfWeek.toString().subSequence(0, 3)
        binding.calendarItemDayNumber.text = day.dayOfMonth.toString()

        if (day == selectedDate) {
            binding.calendarItemRoot.setBackgroundColor(Color.LTGRAY)
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
        oldList[oldItemPosition] == (newList[newItemPosition])
}