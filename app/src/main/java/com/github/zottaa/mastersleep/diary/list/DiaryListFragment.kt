package com.github.zottaa.mastersleep.diary.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentDiaryListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryListFragment : AbstractFragment<FragmentDiaryListBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryListBinding.inflate(inflater, container, false)


}