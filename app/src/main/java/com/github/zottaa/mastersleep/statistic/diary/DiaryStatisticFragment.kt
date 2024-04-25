package com.github.zottaa.mastersleep.statistic.diary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentDiaryStatisticBinding
import com.github.zottaa.mastersleep.diary.list.SpaceItemDecoration
import com.github.zottaa.mastersleep.statistic.pager.PagerFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DiaryStatisticFragment : AbstractFragment<FragmentDiaryStatisticBinding>() {
    private val sharedViewModel: PagerFragmentViewModel by activityViewModels()
    private val viewModel: DiaryStatisticViewModel by viewModels()
    override fun bind(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDiaryStatisticBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = WordFrequencyAdapter()
        setupWordFrequencyRecyclerView(adapter)
        observeViewModels(adapter)
    }

    private fun setupWordFrequencyRecyclerView(adapter: WordFrequencyAdapter) {
        binding.diaryStatisticRecyclerView.adapter = adapter
        binding.diaryStatisticRecyclerView.addItemDecoration(
            SpaceItemDecoration(
                16
            )
        )
    }

    private fun observeViewModels(adapter: WordFrequencyAdapter) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    sharedViewModel.dateRange.collect {
                        viewModel.findWordsFrequency(it)
                    }
                }
                launch {
                    viewModel.diaryStatisticUiState.collect {
                        it.show(binding)
                    }
                }
                launch {
                    viewModel.wordsFrequency.collect {
                        adapter.update(it)
                    }
                }
                launch {
                    viewModel.nothingFoundSnackBar.collect { shouldShow ->
                        if (shouldShow) {
                            viewModel.resetSnackBar()
                            Snackbar.make(
                                requireActivity().findViewById(android.R.id.content),
                                requireContext().getString(R.string.nothing_found),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }
}