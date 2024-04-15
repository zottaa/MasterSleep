package com.github.zottaa.mastersleep.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : AbstractFragment<FragmentSettingsBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentSettingsBinding.inflate(inflater, container, false)

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var chosenLanguage: Languages
    private lateinit var chosenTheme: Themes

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<Toolbar>(R.id.toolbar).navigationIcon = null
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_diary -> {
                    findNavController().navigate(
                        SettingsFragmentDirections.actionSettingsFragmentToDiaryListFragment()
                    )
                    true
                }

                R.id.action_clock -> {
                    findNavController().navigate(
                        SettingsFragmentDirections.actionSettingsFragmentToClockSetFragment()
                    )
                    true
                }

                R.id.action_settings -> {
                    true
                }

                else -> {
                    false
                }
            }
        }

        binding.languageCard.setOnClickListener {
            SettingsDialogProvide.Language(requireContext())
                .provide(
                    Languages.entries.map { it.string(requireContext()) }.toTypedArray(),
                    viewModel.language.value.code,
                    {
                        if (chosenLanguage != viewModel.language.value) {
                            viewModel.changeLocale(chosenLanguage)
                        }
                    }
                ) { checkedIndex ->
                    chosenLanguage = Languages.getByValue(checkedIndex)
                }
                .show()
        }

        binding.themeCard.setOnClickListener {
            SettingsDialogProvide.Theme(requireContext())
                .provide(
                    Themes.entries.map { it.string(requireContext()) }.toTypedArray(),
                    viewModel.theme.value.code,
                    {
                        if (chosenTheme != viewModel.theme.value) {
                            viewModel.changeTheme(chosenTheme)
                        }
                    }
                ) { checkedIndex ->
                    chosenTheme = Themes.getByValue(checkedIndex)
                }
                .show()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.language.collect {
                        binding.languageTextView.text = it.string(requireContext())
                        chosenLanguage = it

                    }
                }
                launch {
                    viewModel.theme.collect {
                        binding.themeTextView.text = it.string(requireContext())
                        chosenTheme = it
                    }
                }
                launch {
                    viewModel.configurationChange.collect {
                        if (it) {
                            requireActivity().recreate()
                        }
                    }
                }
            }
        }
        viewModel.init()
    }
}