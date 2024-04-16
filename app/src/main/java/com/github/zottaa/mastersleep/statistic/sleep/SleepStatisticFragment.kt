package com.github.zottaa.mastersleep.statistic.sleep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentSleepStatisticBinding

class SleepStatisticFragment : AbstractFragment<FragmentSleepStatisticBinding>() {
    override fun bind(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSleepStatisticBinding.inflate(inflater, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        println("SleepStatisticFragment onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        println("SleepStatisticFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        println("SleepStatisticFragment onResume")
        super.onResume()
    }

    override fun onDestroyView() {
        println("SleepStatisticFragment onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        println("SleepStatisticFragment onDestroy")
        super.onDestroy()
    }
}