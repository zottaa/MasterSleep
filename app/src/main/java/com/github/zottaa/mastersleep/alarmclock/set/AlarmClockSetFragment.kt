package com.github.zottaa.mastersleep.alarmclock.set

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
class AlarmClockSetFragment : AbstractFragment<FragmentClockSetBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentClockSetBinding.inflate(inflater, container, false)

    private val viewModel: AlarmClockSetViewModel by viewModels()

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

            picker.show(childFragmentManager, AlarmClockSetFragment::class.java.name)
        }

        binding.startAlarmButton.setOnClickListener {
            viewModel.scheduleAlarm(DateFormat.is24HourFormat(requireContext()))
            findNavController().navigate(
                AlarmClockSetFragmentDirections.actionClockSetFragmentToAlarmClockScheduleFragment()
            )
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_diary -> {
                    findNavController().navigate(
                        AlarmClockSetFragmentDirections.actionClockSetFragmentToDiaryListFragment2()
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
                launch {
                    viewModel.selectedTime.collect {
                        binding.textClockAlarm.text = it
                    }
                }
                launch {
                    viewModel.isAlarmAlreadyScheduled.collect {
                        if (it)
                            findNavController().navigate(
                                AlarmClockSetFragmentDirections.actionClockSetFragmentToAlarmClockScheduleFragment()
                            )
                    }
                }
            }
        }
        if (savedInstanceState != null) {
            viewModel.restore(
                BundleWrapper.String(savedInstanceState)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.init(DateFormat.is24HourFormat(requireContext()))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(
            BundleWrapper.String(outState)
        )
    }
}