package com.github.zottaa.mastersleep.settings

import android.content.Context
import com.github.zottaa.mastersleep.R

enum class Themes(
    val code: Int
) {
    DEFAULT(0) {
        override fun string(context: Context) =
            context.getString(R.string.default_item)
    },
    LIGHT(1) {
        override fun string(context: Context) =
            context.getString(R.string.light)
    },
    DARK(2) {
        override fun string(context: Context) =
            context.getString(R.string.dark)
    };

    abstract fun string(context: Context): String

    companion object {
        private val VALUES = entries.toTypedArray()
        fun getByValue(value: Int) = VALUES.first { it.code == value }
    }
}