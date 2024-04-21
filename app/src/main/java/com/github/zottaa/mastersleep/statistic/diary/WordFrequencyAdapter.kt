package com.github.zottaa.mastersleep.statistic.diary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.zottaa.mastersleep.databinding.WordFrequencyItemBinding

class WordFrequencyAdapter(
    private var wordsFrequency: List<WordFrequency> = emptyList()
) : RecyclerView.Adapter<WordFrequencyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordFrequencyViewHolder {
        val binding = WordFrequencyItemBinding.inflate(LayoutInflater.from(parent.context))
        return WordFrequencyViewHolder(binding)
    }

    override fun getItemCount() = wordsFrequency.size

    override fun onBindViewHolder(holder: WordFrequencyViewHolder, position: Int) {
        holder.hold(wordsFrequency[position])
    }

    fun update(newList: List<WordFrequency>) {
        val diffUtil = WordFrequencyDiffUtil(wordsFrequency, newList)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        wordsFrequency = newList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}

class WordFrequencyViewHolder(
    private val binding: WordFrequencyItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun hold(wordFrequency: WordFrequency) {
        binding.columnWord.text = wordFrequency.word
        binding.columnFrequency.text = wordFrequency.frequency.toString()
    }
}

class WordFrequencyDiffUtil(
    private val oldList: List<WordFrequency>,
    private val newList: List<WordFrequency>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}