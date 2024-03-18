package com.github.zottaa.mastersleep.diary.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.zottaa.mastersleep.core.AbstractFragment
import com.github.zottaa.mastersleep.databinding.FragmentDiaryCreateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryCreateFragment : AbstractFragment<FragmentDiaryCreateBinding>() {
    private val args: DiaryCreateFragmentArgs by navArgs()
    private val viewModel: DiaryCreateViewModel by viewModels()
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.titleTextInputEditText.text.toString()
                    .isNotEmpty() || binding.contentTextInputEditText.text.toString().isNotEmpty()
            )
                viewModel.create(
                    binding.titleTextInputEditText.text.toString(),
                    binding.contentTextInputEditText.text.toString(),
                    args.date
                )
            findNavController().popBackStack()
        }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentDiaryCreateBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        if (savedInstanceState != null) {
            binding.titleTextInputEditText.setText(savedInstanceState.getString(TITLE_KEY))
            binding.contentTextInputEditText.setText(savedInstanceState.getString(CONTENT_KEY))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TITLE_KEY, binding.titleTextInputEditText.text.toString())
        outState.putString(CONTENT_KEY, binding.contentTextInputEditText.text.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }

    companion object {
        private const val TITLE_KEY = "titleKey"
        private const val CONTENT_KEY = "contentKey"
    }
}