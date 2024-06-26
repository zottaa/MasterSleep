package com.github.zottaa.mastersleep.diary.core

import android.widget.TextView
import com.github.zottaa.mastersleep.diary.list.DiaryListFragmentDirections


data class NoteUi(
    private val id: Long,
    private val title: String,
    private val content: String,
    private val date: Long
) {
    fun isIdTheSame(noteUi: NoteUi) = noteUi.id == id

    fun showTitle(textView: TextView) {
        textView.text = title
    }

    fun showContent(textView: TextView) {
        textView.text = content
    }

    fun mapAction(diaryListFragmentDirections: DiaryListFragmentDirections.Companion) =
        diaryListFragmentDirections.actionDiaryListFragmentToDiaryEditFragment(id)

    fun words(): List<String> {
        val raw = " $title $content"
        val words = raw.split(splitPattern.toRegex()).filter {
            it.isNotBlank()
        }.map { it.lowercase() }
        return words
    }

    companion object {
        private const val splitPattern = "[^а-яА-Яa-zA-Z]+"
    }
}
