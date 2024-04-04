package com.github.zottaa.mastersleep.alarmclock.ring

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.github.zottaa.mastersleep.alarmclock.ringtone.RingtoneService
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentClockRingBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneId

@AndroidEntryPoint
class AlarmClockRingFragment : AbstractFragment<FragmentClockRingBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentClockRingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.snoozeButton.setOnClickListener {
            findNavController().navigate(
                AlarmClockRingFragmentDirections.actionAlarmClockRingFragmentToAlarmClockScheduleFragment(
                    LocalDateTime
                        .now()
                        .plusMinutes(5)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                )
            )
        }
        binding.stopButton.setOnClickListener {
            findNavController().navigate(AlarmClockRingFragmentDirections.actionAlarmClockRingFragmentToDiaryListFragment())
        }
    }

    override fun onDestroyView() {
        requireContext().also {
            Intent(it, RingtoneService::class.java).also { intent ->
                intent.action = STOP_SERVICE
                it.startService(intent)
            }
        }
        super.onDestroyView()
    }

    companion object {
        private const val STOP_SERVICE = "STOP_SERVICE"
    }
}