package com.github.zottaa.mastersleep.statistic.pager

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.zottaa.mastersleep.statistic.diary.DiaryStatisticFragment
import com.github.zottaa.mastersleep.statistic.sleep.SleepStatisticFragment

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = FRAGMENT_NUMBER

    override fun createFragment(position: Int) =
        if (position == 0)
            SleepStatisticFragment()
        else
            DiaryStatisticFragment()

    companion object {
        private const val FRAGMENT_NUMBER = 2
    }
}