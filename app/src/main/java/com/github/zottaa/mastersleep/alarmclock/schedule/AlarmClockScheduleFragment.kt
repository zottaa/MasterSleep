package com.github.zottaa.mastersleep.alarmclock.schedule

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.alarmclock.ringtone.RingtoneService
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentClockScheduleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmClockScheduleFragment : AbstractFragment<FragmentClockScheduleBinding>(), MenuProvider {
    private val args: AlarmClockScheduleFragmentArgs by navArgs()
    private val viewModel: AlarmClockScheduleViewModel by viewModels()
    private lateinit var schedule: AlarmClockSchedule
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            schedule.cancel(AlarmItem(args.time))
            findNavController().popBackStack()
        }
    }

    override fun bind(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentClockScheduleBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        schedule = AlarmClockSchedule.Base(requireContext())
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        requireActivity().findViewById<Toolbar>(R.id.toolbar).navigationIcon =
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.arrow_back
            )
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
        binding.cancelAlarmButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            requireContext().stopService(Intent(context, RingtoneService::class.java))
        }
        schedule.schedule(AlarmItem(args.time))
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_fragment_alarm_schedule, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                requireActivity().onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return false
    }
}