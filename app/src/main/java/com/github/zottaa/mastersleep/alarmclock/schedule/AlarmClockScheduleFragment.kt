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
import androidx.navigation.fragment.navArgs
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentClockScheduleBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class AlarmClockScheduleFragment : AbstractFragment<FragmentClockScheduleBinding>(), MenuProvider {
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
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToSetScreen.collect {
                    if (it)
                        findNavController().navigate(R.id.clockSetFragment)
                }
            }
        }
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
}