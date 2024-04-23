package com.github.zottaa.mastersleep.streaks

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
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentStreaksBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StreaksFragment : AbstractFragment<FragmentStreaksBinding>() {
    private val viewModel: StreaksViewModel by viewModels()
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentStreaksBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<Toolbar>(R.id.toolbar).navigationIcon = null
        binding.bottomNavigation.selectedItemId = R.id.action_streaks
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_diary -> {
                    findNavController().navigate(
                        StreaksFragmentDirections.actionStreaksFragmentToDiaryListFragment()
                    )
                    true
                }

                R.id.action_clock -> {
                    findNavController().navigate(
                        StreaksFragmentDirections.actionStreaksFragmentToClockSetFragment()
                    )
                    true
                }

                R.id.action_settings -> {
                    findNavController().navigate(
                        StreaksFragmentDirections.actionStreaksFragmentToSettingsFragment()
                    )
                    true
                }

                R.id.action_statistic -> {
                    findNavController().navigate(
                        StreaksFragmentDirections.actionStreaksFragmentToPagerFragment()
                    )
                    true
                }

                R.id.action_streaks -> {
                    true
                }

                else -> {
                    false
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.currentStreakDiary.collect {
                        binding.diaryStreakTextView.text =
                            requireContext().getString(R.string.current_streak, it)
                    }
                }
                launch {
                    viewModel.maxStreakDiary.collect {
                        binding.diaryMaxStreakTextView.text =
                            requireContext().getString(R.string.current_max_streak, it)
                    }
                }
                launch {
                    viewModel.currentStreakSleep.collect {
                        binding.sleepStreakTextView.text =
                            requireContext().getString(R.string.current_streak, it)
                    }
                }
                launch {
                    viewModel.maxStreakSleep.collect {
                        binding.sleepMaxStreakTextView.text =
                            requireContext().getString(R.string.current_max_streak, it)
                    }
                }
            }
        }
        viewModel.init()
    }
}