package com.github.zottaa.mastersleep.statistic.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.databinding.FragmentViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PagerFragment : AbstractFragment<FragmentViewPagerBinding>() {
    private val sharedViewModel: PagerFragmentViewModel by activityViewModels()
    private lateinit var adapter: FragmentPageAdapter

    @Inject
    lateinit var provideDatePicker: ProvideDatePicker

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentViewPagerBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<Toolbar>(R.id.toolbar).navigationIcon = null

        binding.bottomNavigation.selectedItemId = R.id.action_statistic
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_diary -> {
                    findNavController().navigate(
                        PagerFragmentDirections.actionPagerFragmentToDiaryListFragment()
                    )
                    true
                }

                R.id.action_clock -> {
                    findNavController().navigate(
                        PagerFragmentDirections.actionPagerFragmentToClockSetFragment()
                    )
                    true
                }

                R.id.action_settings -> {
                    findNavController().navigate(
                        PagerFragmentDirections.actionPagerFragmentToSettingsFragment()
                    )
                    true
                }

                R.id.action_streaks -> {
                    findNavController().navigate(
                        PagerFragmentDirections.actionPagerFragmentToStreaksFragment()
                    )
                    true
                }

                R.id.action_statistic -> {
                    true
                }

                else -> {
                    false
                }
            }
        }

        binding.dateRangeTextView.setOnClickListener {
            val picker = provideDatePicker.provide(sharedViewModel.dateRange.value)
            picker.show(childFragmentManager, PagerFragment::class.java.name)
            picker.addOnPositiveButtonClickListener {
                sharedViewModel.pickDateRange(it.first, it.second)
            }
        }

        val tabLayout = binding.tabLayout
        val pager = binding.pager
        adapter = FragmentPageAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        pager.adapter = adapter
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text =
                if (position == 0)
                    requireContext().getString(R.string.sleep)
                else
                    requireContext().getString(R.string.item_navigation_diary)
        }.attach()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.dateRange.collect {
                    binding.dateRangeTextView.text =
                        requireContext().getString(R.string.date_range, it.first, it.second)
                }
            }
        }

        if (savedInstanceState != null) {
            sharedViewModel.restore(BundleWrapper.StringArray(savedInstanceState))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        sharedViewModel.save(BundleWrapper.StringArray(outState))
    }
}