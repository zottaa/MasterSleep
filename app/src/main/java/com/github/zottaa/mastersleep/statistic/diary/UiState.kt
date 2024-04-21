package com.github.zottaa.mastersleep.statistic.diary

import android.view.View
import com.github.zottaa.mastersleep.databinding.FragmentDiaryStatisticBinding

interface UiState {
    fun show(binding: FragmentDiaryStatisticBinding)

    object Progress : UiState {
        override fun show(binding: FragmentDiaryStatisticBinding) {
            binding.diaryStatisticProgressBar.visibility = View.VISIBLE
            binding.diaryStatisticRecyclerView.visibility = View.GONE
            binding.diaryStatisticHeaderRow.visibility = View.GONE
        }
    }

    object Initial : UiState {
        override fun show(binding: FragmentDiaryStatisticBinding) {
            binding.diaryStatisticProgressBar.visibility = View.GONE
            binding.diaryStatisticRecyclerView.visibility = View.GONE
            binding.diaryStatisticHeaderRow.visibility = View.GONE
        }
    }

    object Show : UiState {
        override fun show(binding: FragmentDiaryStatisticBinding) {
            binding.diaryStatisticProgressBar.visibility = View.GONE
            binding.diaryStatisticRecyclerView.visibility = View.VISIBLE
            binding.diaryStatisticHeaderRow.visibility = View.VISIBLE
        }
    }

    object NothingFound : UiState {
        override fun show(binding: FragmentDiaryStatisticBinding) {
            binding.diaryStatisticProgressBar.visibility = View.GONE
            binding.diaryStatisticHeaderRow.visibility = View.GONE
        }
    }
}