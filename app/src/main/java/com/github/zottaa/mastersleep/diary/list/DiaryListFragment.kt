package com.github.zottaa.mastersleep.diary.list

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.databinding.FragmentDiaryListBinding
import com.github.zottaa.mastersleep.diary.core.NoteUi
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class DiaryListFragment : AbstractFragment<FragmentDiaryListBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryListBinding.inflate(inflater, container, false)

    private val viewModel: DiaryListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        val calendarAdapter = setupCalendarAdapter()
        setupCalendarRecyclerView(calendarAdapter)
        val noteAdapter = setupNoteAdapter()
        setupNoteRecyclerView(noteAdapter)
        setupBottomNavigation()
        observeViewModel(calendarAdapter, noteAdapter)
        binding.nextWeekButton.setOnClickListener {
            viewModel.nextWeek()
        }
        binding.previousWeekButton.setOnClickListener {
            viewModel.previousWeek()
        }
        binding.addButton.setOnClickListener {
            val date = viewModel.selectedDate.value.toEpochDay()
            val action =
                DiaryListFragmentDirections.actionDiaryListFragmentToDiaryCreateFragment(date)
            findNavController().navigate(action)
        }

        if (savedInstanceState != null) {
            restore(savedInstanceState)
        }
        viewModel.init(savedInstanceState == null)
    }

    private fun restore(savedInstanceState: Bundle) {
        viewModel.restore(
            BundleWrapper.String(savedInstanceState),
            BundleWrapper.StringArray(savedInstanceState)
        )
    }

    private fun observeViewModel(
        calendarAdapter: CalendarAdapter,
        noteAdapter: NotesAdapter
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.selectedDate.collect {
                        calendarAdapter.updateDate(it)
                        binding.currentMonthYearTextView.text =
                            it.format(
                                DateTimeFormatter.ofPattern("MMM yyyy")
                                    .withLocale(Locale.getDefault())
                            )
                    }
                }
                launch {
                    viewModel.week.collect {
                        calendarAdapter.update(it)
                    }
                }
                launch {
                    viewModel.notes.collect {
                        noteAdapter.update(it)
                    }
                }
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.selectedItemId = R.id.action_diary
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_diary -> {
                    true
                }

                R.id.action_clock -> {
                    findNavController().navigate(
                        DiaryListFragmentDirections.actionDiaryListFragmentToClockSetFragment()
                    )
                    true
                }

                R.id.action_settings -> {
                    findNavController().navigate(
                        DiaryListFragmentDirections.actionDiaryListFragmentToSettingsFragment()
                    )
                    true
                }

                R.id.action_statistic -> {
                    findNavController().navigate(
                        DiaryListFragmentDirections.actionDiaryListFragmentToPagerFragment()
                    )
                    true
                }

                R.id.action_streaks -> {
                    findNavController().navigate(
                        DiaryListFragmentDirections.actionDiaryListFragmentToStreaksFragment()
                    )
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun setupNoteRecyclerView(noteAdapter: NotesAdapter) {
        binding.notesRecyclerView.adapter = noteAdapter
        binding.notesRecyclerView.addItemDecoration(
            SpaceItemDecoration(
                16
            )
        )
    }

    private fun setupNoteAdapter() = NotesAdapter(
        object : EditNote {
            override fun editNote(noteUi: NoteUi) {
                val action = noteUi.mapAction(DiaryListFragmentDirections)
                findNavController().navigate(action)
            }
        }
    )

    private fun setupCalendarRecyclerView(calendarAdapter: CalendarAdapter) {
        binding.calendarRecyclerView.adapter = calendarAdapter
        binding.calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        binding.calendarRecyclerView.addItemDecoration(GridItemDecoration(14))
    }

    private fun setupCalendarAdapter(): CalendarAdapter {
        val calendarAdapter = CalendarAdapter(
            MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorBackgroundFloating,
                Color.WHITE
            ),
            MaterialColors.getColor(
                requireContext(),
                com.google.android.material.R.attr.colorAccent,
                Color.LTGRAY
            ),
            object : SelectDay {
                override fun selectDay(currentDay: LocalDate) {
                    viewModel.selectDay(currentDay)
                }
            })
        return calendarAdapter
    }

    private fun setupToolbar() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar).navigationIcon = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(
            BundleWrapper.String(outState),
            BundleWrapper.StringArray(outState)
        )
    }
}