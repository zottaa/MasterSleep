package com.github.zottaa.mastersleep.clock.set

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.databinding.FragmentClockSetBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalTime

@AndroidEntryPoint
class ClockSetFragment : AbstractFragment<FragmentClockSetBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentClockSetBinding.inflate(inflater, container, false)

    private val viewModel: ClockSetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setAlarmTimeButton.setOnClickListener {
            val isSystem24Hour = DateFormat.is24HourFormat(requireContext())
            val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(clockFormat)
                .setTitleText(requireContext().getString(R.string.set_alarm))
                .setHour(LocalTime.now().hour)
                .setMinute(LocalTime.now().minute)
                .build()

            picker.addOnPositiveButtonClickListener {
                viewModel.setAlarmTime(picker.hour, picker.minute, isSystem24Hour)
            }

            picker.show(childFragmentManager, ClockSetFragment::class.java.name)
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_diary -> {
                    findNavController().navigate(
                        ClockSetFragmentDirections.actionClockSetFragmentToDiaryListFragment2()
                    )
                    true
                }

                R.id.action_clock -> {
                    true
                }

                else -> {
                    false
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.selectedTimeLiveData.collect {
                    binding.textClockAlarm.text = it
                }
            }
        }

        viewModel.init(DateFormat.is24HourFormat(requireContext()))

        if (savedInstanceState != null) {
            viewModel.restore(
                BundleWrapper.String(savedInstanceState)
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(
            BundleWrapper.String(outState)
        )
    }
}