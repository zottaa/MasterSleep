package com.github.zottaa.mastersleep.diary.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.zottaa.mastersleep.databinding.NoteItemBinding
import com.github.zottaa.mastersleep.diary.core.NoteUi

class NotesAdapter(
    private var list: List<NoteUi> = emptyList()
) : RecyclerView.Adapter<NoteListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val binding = NoteItemBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return NoteListViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        holder.hold(list[position])
    }

    fun update(newList: List<NoteUi>) {
        val diffUtil = NoteListDiffUtil(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }
}



class NoteListViewHolder(
    private val binding: NoteItemBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun hold(noteUi: NoteUi) {
        noteUi.apply {
            showTitle(binding.titleTextView)
            showContent(binding.contentTextView)
        }
    }
}

class NoteListDiffUtil(
    private val oldList: List<NoteUi>,
    private val newList: List<NoteUi>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].isIdTheSame(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == (newList[newItemPosition])
}