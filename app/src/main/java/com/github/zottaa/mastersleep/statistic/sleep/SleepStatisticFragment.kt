package com.github.zottaa.mastersleep.statistic.sleep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentSleepStatisticBinding
import com.github.zottaa.mastersleep.statistic.pager.PagerFragmentViewModel

class SleepStatisticFragment : AbstractFragment<FragmentSleepStatisticBinding>() {
    private val sharedViewModel: PagerFragmentViewModel by activityViewModels()
    override fun bind(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSleepStatisticBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}