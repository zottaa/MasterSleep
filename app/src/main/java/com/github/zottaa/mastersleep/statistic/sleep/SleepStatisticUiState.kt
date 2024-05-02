package com.github.zottaa.mastersleep.statistic.sleep

import android.view.View
import com.github.zottaa.mastersleep.databinding.FragmentSleepStatisticBinding

interface SleepStatisticUiState {
    fun show(binding: FragmentSleepStatisticBinding)

    object Progress : SleepStatisticUiState {
        override fun show(binding: FragmentSleepStatisticBinding) {
            binding.wakeUpTimeChart.visibility = View.GONE
            binding.wakeUpTimeChart.visibility = View.GONE
            binding.timeToFallAsleepChart.visibility = View.GONE
            binding.durationChart.visibility = View.GONE
            binding.sleepStatisticProgressBar.visibility = View.VISIBLE
        }
    }

    object Initial : SleepStatisticUiState {
        override fun show(binding: FragmentSleepStatisticBinding) {
            binding.wakeUpTimeChart.visibility = View.GONE
            binding.wakeUpTimeChart.visibility = View.GONE
            binding.timeToFallAsleepChart.visibility = View.GONE
            binding.durationChart.visibility = View.GONE
            binding.sleepStatisticProgressBar.visibility = View.GONE
        }
    }

    object Show : SleepStatisticUiState {
        override fun show(binding: FragmentSleepStatisticBinding) {
            binding.wakeUpTimeChart.visibility = View.VISIBLE
            binding.wakeUpTimeChart.visibility = View.VISIBLE
            binding.timeToFallAsleepChart.visibility = View.VISIBLE
            binding.durationChart.visibility = View.VISIBLE
            binding.sleepStatisticProgressBar.visibility = View.GONE
        }
    }
}