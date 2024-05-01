package com.github.zottaa.mastersleep.statistic.sleep

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentSleepStatisticBinding
import com.github.zottaa.mastersleep.statistic.pager.PagerFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SleepStatisticFragment : AbstractFragment<FragmentSleepStatisticBinding>() {
    private val sharedViewModel: PagerFragmentViewModel by activityViewModels()
    private val viewModel: SleepStatisticViewModel by viewModels()
    private val barChartConfigure by lazy {
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireContext().resources.configuration.isNightModeActive
            } else {
                val currentNightMode =
                    requireContext().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
                currentNightMode == Configuration.UI_MODE_NIGHT_YES
            }
        ) {
            BarChartConfigure.Night()
        } else {
            BarChartConfigure.Day()
        }
    }

    override fun bind(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSleepStatisticBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModels()
    }

    private fun observeViewModels() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    sharedViewModel.dateRange.collect {
                        viewModel.buildCharts(it)
                    }
                }
                launch {
                    viewModel.sleepDuration.collect { dataSet ->
                        barChartConfigure.configure(
                            binding.durationChart,
                            "Duration",
                            dataSet
                        )
                        binding.durationChart.invalidate()
                    }
                }
                launch {
                    viewModel.timeToFallAsleep.collect { dataSet ->
                        barChartConfigure.configure(
                            binding.timeToFallAsleepChart,
                            "Time To Fall Asleep",
                            dataSet
                        )
                        binding.timeToFallAsleepChart.invalidate()
                    }
                }
                launch {
                    viewModel.wakeUpTime.collect { dataSet ->
                        barChartConfigure.configure(
                            binding.wakeUpTimeChart,
                            "Wake Up Time",
                            dataSet
                        )
                        binding.wakeUpTimeChart.invalidate()
                    }
                }
                launch {
                    viewModel.timeWhenFallAsleep.collect { dataSet ->
                        barChartConfigure.configure(
                            binding.timeWhenFallAsleepChart,
                            "Time When Fall Asleep",
                            dataSet
                        )
                        binding.timeWhenFallAsleepChart.invalidate()
                    }
                }
                launch {
                    viewModel.uiState.collect { uiState ->
                        uiState.show(binding)
                    }
                }
            }
        }
    }
}