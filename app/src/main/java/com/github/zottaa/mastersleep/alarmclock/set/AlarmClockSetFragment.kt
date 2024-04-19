package com.github.zottaa.mastersleep.alarmclock.set

import android.Manifest
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.alarmclock.ringtone.RingtoneSelectViewModel
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
    private val sharedViewModel: RingtoneSelectViewModel by activityViewModels()

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<Toolbar>(R.id.toolbar).navigationIcon = null
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(
                )
            ) { permissions ->
                val allPermissionGranted = permissions.all { it.value }
                if (allPermissionGranted) {
                    startAlarmClock()
                } else {
                    permissions.entries.forEach { entry ->
                        val permission = entry.key
                        val isGranted = entry.value
                        val permissionDialogProvide by lazy {
                            PermissionDialogProvide.Base(
                                requireActivity(),
                                requireContext()
                            )
                        }
                        if (!isGranted && !ActivityCompat.shouldShowRequestPermissionRationale(
                                requireActivity(),
                                permission
                            )
                        ) {
                            permissionDialogProvide.showPermissionDenialDialog(
                                permission
                            )
                            return@registerForActivityResult
                        }
                    }
                    requestRuntimePermission(requestPermissionLauncher)
                }
            }
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
            requestRuntimePermission(requestPermissionLauncher)
        }

        binding.ringtoneChooseTextView.setOnClickListener {
            findNavController().navigate(
                AlarmClockSetFragmentDirections
                    .actionClockSetFragmentToRingtoneSelectDialog()
            )
        }

        binding.bottomNavigation.selectedItemId = R.id.action_clock
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

                R.id.action_settings -> {
                    findNavController().navigate(
                        AlarmClockSetFragmentDirections.actionClockSetFragmentToSettingsFragment()
                    )
                    true
                }

                R.id.action_statistic -> {
                    findNavController().navigate(
                        AlarmClockSetFragmentDirections.actionClockSetFragmentToPagerFragment()
                    )
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
                                AlarmClockSetFragmentDirections
                                    .actionClockSetFragmentToAlarmClockScheduleFragment()
                            )
                    }
                }
                launch {
                    viewModel.navigateToSchedule.collect {
                        if (it) {
                            findNavController().navigate(
                                AlarmClockSetFragmentDirections
                                    .actionClockSetFragmentToAlarmClockScheduleFragment()
                            )
                        }
                    }
                }
                launch {
                    sharedViewModel.selectedRingtone.collect {
                        binding.ringtoneChooseTextView.text =
                            RingtoneManager
                                .getRingtone(requireContext(), Uri.parse(it))
                                .getTitle(requireContext())
                    }
                }
            }
        }
        sharedViewModel.init()
        if (savedInstanceState != null) {
            viewModel.restore(
                BundleWrapper.String(savedInstanceState)
            )
        }
    }


    private fun requestRuntimePermission(
        activityResultLauncher: ActivityResultLauncher<Array<String>>
    ) {
        val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                POST_NOTIFICATIONS,
                ACTIVITY_RECOGNITION
            )
        } else {
            arrayOf(ACTIVITY_RECOGNITION)
        }

        when {
            permissionsToRequest.all {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) == PackageManager.PERMISSION_GRANTED
            } -> {
                startAlarmClock()
            }

            permissionsToRequest.any {
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), it)
            } -> {
                val permissionDialogProvide by lazy {
                    PermissionDialogProvide.Base(
                        requireActivity(),
                        requireContext()
                    )
                }
                permissionsToRequest.forEach { permission ->
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            permission
                        )
                    )
                        permissionDialogProvide.showPermissionRationaleDialog(
                            activityResultLauncher,
                            permission
                        )
                }
            }

            else -> activityResultLauncher.launch(permissionsToRequest)
        }
    }

    private fun startAlarmClock() {
        viewModel.scheduleAlarm(DateFormat.is24HourFormat(requireContext()))
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

    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        private const val POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS
        private const val ACTIVITY_RECOGNITION = Manifest.permission.ACTIVITY_RECOGNITION
    }
}