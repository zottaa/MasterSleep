package com.github.zottaa.mastersleep.diary.edit

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
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.zottaa.mastersleep.R
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentDiaryEditBinding
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryEditFragment : AbstractFragment<FragmentDiaryEditBinding>(), MenuProvider {
    private val viewModel: DiaryEditViewModel by viewModels()
    private val args: DiaryEditFragmentArgs by navArgs()
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.contentEditTextInputEditText.text.toString()
                    .isEmpty() && binding.titleEditTextInputEditText.text.toString().isEmpty()
            )
                viewModel.delete(args.noteId)
            findNavController().popBackStack()
        }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryEditBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        requireActivity().findViewById<Toolbar>(R.id.toolbar).navigationIcon =
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.arrow_back
            )
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
        
        viewModel.noteLiveData.observe(viewLifecycleOwner) {
            it.showTitle(binding.titleEditTextInputEditText)
            it.showContent(binding.contentEditTextInputEditText)
        }

        viewModel.init(args.noteId)

        addUpdateListener(binding.contentEditTextInputEditText)
        addUpdateListener(binding.titleEditTextInputEditText)
    }


    private fun addUpdateListener(textInputEditText: TextInputEditText) {
        textInputEditText.addTextChangedListener {
            if (binding.contentEditTextInputEditText.text.toString()
                    .isNotEmpty() || binding.titleEditTextInputEditText.text.toString().isNotEmpty()
            )
                viewModel.update(
                    args.noteId,
                    binding.titleEditTextInputEditText.text.toString(),
                    binding.contentEditTextInputEditText.text.toString()
                )
        }
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
                viewModel.delete(args.noteId)
                findNavController().popBackStack()
                return true
            }
        }
        return false
    }
}