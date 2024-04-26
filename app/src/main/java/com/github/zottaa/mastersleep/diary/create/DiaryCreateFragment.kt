package com.github.zottaa.mastersleep.diary.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.core.BundleWrapper
import com.github.zottaa.mastersleep.databinding.FragmentDiaryCreateBinding
import com.github.zottaa.mastersleep.diary.core.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryCreateFragment : AbstractFragment<FragmentDiaryCreateBinding>(), MenuProvider {
    private val viewModel: DiaryCreateViewModel by viewModels()
    private val sharedCalendarViewModel: CalendarViewModel by activityViewModels()
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.titleTextInputEditText.text.toString()
                    .isNotEmpty() || binding.contentTextInputEditText.text.toString().isNotEmpty()
            )
                viewModel.create(
                    binding.titleTextInputEditText.text.toString(),
                    binding.contentTextInputEditText.text.toString(),
                    sharedCalendarViewModel.selectedDate.value.toEpochDay()
                )
            findNavController().popBackStack()
        }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryCreateBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressCallback()
        setupMenuProvider()
        setupToolbar()
        if (savedInstanceState != null) {
            restore(savedInstanceState)
        }
    }

    private fun setupMenuProvider() {
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }

    private fun restore(savedInstanceState: Bundle) {
        binding.titleTextInputEditText.setText(savedInstanceState.getString(TITLE_KEY))
        binding.contentTextInputEditText.setText(savedInstanceState.getString(CONTENT_KEY))
        sharedCalendarViewModel.restore(
            BundleWrapper.String(savedInstanceState),
            BundleWrapper.StringArray(savedInstanceState)
        )
    }

    private fun setupToolbar() {
        requireActivity().findViewById<Toolbar>(R.id.toolbar).navigationIcon =
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.arrow_back
            )
    }

    private fun setupOnBackPressCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TITLE_KEY, binding.titleTextInputEditText.text.toString())
        outState.putString(CONTENT_KEY, binding.contentTextInputEditText.text.toString())
        sharedCalendarViewModel.save(
            BundleWrapper.String(outState),
            BundleWrapper.StringArray(outState)
        )
    }

    companion object {
        private const val TITLE_KEY = "titleKey"
        private const val CONTENT_KEY = "contentKey"
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_fragment_create_and_edit, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                hideKeyboard()
                requireActivity().onBackPressedDispatcher.onBackPressed()
                return true
            }

            R.id.delete_menu_item -> {
                hideKeyboard()
                findNavController().popBackStack()
                return true
            }
        }
        return false
    }
}