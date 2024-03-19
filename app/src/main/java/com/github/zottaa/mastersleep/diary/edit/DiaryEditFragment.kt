package com.github.zottaa.mastersleep.diary.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentDiaryEditBinding
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryEditFragment : AbstractFragment<FragmentDiaryEditBinding>() {
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
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        binding.deleteNoteButton.setOnClickListener {
            hideKeyboard()
            viewModel.delete(args.noteId)
            findNavController().popBackStack()
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }


}