package com.github.zottaa.mastersleep.alarmclock.schedule

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentClockScheduleBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlarmClockScheduleFragment : AbstractFragment<FragmentClockScheduleBinding>(), MenuProvider,
    SleepRequestManager.Unsubscribe {
    private val viewModel: AlarmClockScheduleViewModel by viewModels()
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.cancel()
        }
    }

    override fun bind(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentClockScheduleBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedCallback()
        setupToolbar()
        setupMenuProvider()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.navigateToSetScreen.collect {
                        if (it)
                            findNavController().navigate(R.id.clockSetFragment)
                    }
                }
                launch {
                    viewModel.alarmTriggerTime.collect {
                        binding.alarmScheduleTextView.text =
                            requireContext().getString(R.string.alarm_trigger, it)
                    }
                }
            }
        }
    }

    private fun setupMenuProvider() {
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }

    private fun setupToolbar() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar).navigationIcon =
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.arrow_back
            )
    }

    private fun setupOnBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.schedule()
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

    override fun onDestroyView() {
        unsubscribeFromSleepUpdates()
        super.onDestroyView()
    }

    override fun unsubscribeFromSleepUpdates() {
        viewModel.unsubscribeFromSleepUpdates()
    }
}