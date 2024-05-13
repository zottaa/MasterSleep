package com.github.zottaa.mastersleep.settings

import android.content.Context
import com.github.zottaa.mastersleep.R

enum class Languages(
    val code: Int,
    val languageTag: String
) {
    ENGLISH(0, "en") {
        override fun string(context: Context) =
            context.getString(R.string.english)
    },
    RUSSIAN(1, "ru") {
        override fun string(context: Context) =
            context.getString(R.string.russian)
    };

    abstract fun string(context: Context): String

    companion object {
        private val VALUES = entries.toTypedArray()
        fun getByValue(value: Int) = VALUES.first { it.code == value }
        fun getByLocaleTag(tag: String) =
            VALUES.firstOrNull { it.languageTag == tag.subSequence(0, 2) }
    }
}