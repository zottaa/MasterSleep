package com.github.zottaa.mastersleep.diary.core

import android.widget.TextView

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
}