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
        requireContext().startService(Intent(context, RingtoneService::class.java))
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
        requireContext().stopService(Intent(context, RingtoneService::class.java))
        super.onDestroyView()
    }
}