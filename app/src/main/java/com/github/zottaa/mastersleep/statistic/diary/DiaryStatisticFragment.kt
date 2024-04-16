package com.github.zottaa.mastersleep.statistic.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentDiaryStatisticBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryStatisticFragment : AbstractFragment<FragmentDiaryStatisticBinding>() {
    override fun bind(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDiaryStatisticBinding.inflate(inflater, container, false)
}