package com.github.zottaa.mastersleep.diary.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.databinding.FragmentDiaryListBinding
import com.github.zottaa.mastersleep.diary.core.NoteUi
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

        val calendarAdapter = CalendarAdapter(
            object : SelectDay {
                override fun selectDay(currentDay: LocalDate) {
                    viewModel.selectDay(currentDay)
                }
            })
        binding.calendarRecyclerView.adapter = calendarAdapter
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)

        val noteAdapter = NotesAdapter(
            object : EditNote {
                override fun editNote(noteUi: NoteUi) {
                    val action = noteUi.mapAction(DiaryListFragmentDirections)
                    findNavController().navigate(action)
                }
            }
        )
        binding.notesRecyclerView.adapter = noteAdapter
        binding.notesRecyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.notesRecyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

        viewModel.selectedDateLiveData.observe(viewLifecycleOwner) {
            calendarAdapter.updateDate(it)
            binding.currentMonthYearTextView.text =
                it.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
        }

        viewModel.weekLiveData.observe(viewLifecycleOwner) {
            calendarAdapter.update(it)
        }

        viewModel.notesLiveData.observe(viewLifecycleOwner) {
            noteAdapter.update(it)
        }

        binding.nextWeekButton.setOnClickListener {
            viewModel.nextWeek()
        }

        binding.previousWeekButton.setOnClickListener {
            viewModel.previousWeek()
        }

        binding.addButton.setOnClickListener {
            val date = viewModel.selectedDateLiveData.value?.toEpochDay() ?: 0L
            val action =
                DiaryListFragmentDirections.actionDiaryListFragmentToDiaryCreateFragment(date)
            findNavController().navigate(action)
        }

        if (savedInstanceState != null) {
            viewModel.restore(
                BundleWrapper.String(savedInstanceState),
                BundleWrapper.StringArray(savedInstanceState)
            )
        }
        viewModel.init(savedInstanceState == null)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(
            BundleWrapper.String(outState),
            BundleWrapper.StringArray(outState)
        )
    }
}