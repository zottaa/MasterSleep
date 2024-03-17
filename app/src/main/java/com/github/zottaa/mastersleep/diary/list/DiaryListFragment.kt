package com.github.zottaa.mastersleep.diary.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.databinding.FragmentDiaryListBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class DiaryListFragment : AbstractFragment<FragmentDiaryListBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryListBinding.inflate(inflater, container, false)

    private val viewModel: DiaryListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CalendarAdapter(
            object : SelectDay {
                override fun selectDay(currentDay: LocalDate) {
                    viewModel.selectDay(currentDay)
                }
            })

        binding.calendarRecyclerView.adapter = adapter
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)

        viewModel.selectedDateLiveData.observe(viewLifecycleOwner) {
            adapter.updateDate(it)
            binding.currentMonthYearTextView.text =
                it.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        }

        viewModel.weekLiveData.observe(viewLifecycleOwner) {
            adapter.update(it)
        }

        binding.nextWeekButton.setOnClickListener {
            viewModel.nextWeek()
        }

        binding.previousWeekButton.setOnClickListener {
            viewModel.previousWeek()
        }

        viewModel.init()

        if (savedInstanceState != null) {
            viewModel.restore(
                BundleWrapper.String(savedInstanceState),
                BundleWrapper.StringArray(savedInstanceState)
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(
            BundleWrapper.String(outState),
            BundleWrapper.StringArray(outState)
        )
    }
}