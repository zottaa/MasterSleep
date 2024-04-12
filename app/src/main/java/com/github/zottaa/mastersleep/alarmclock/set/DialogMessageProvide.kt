package com.github.zottaa.mastersleep.alarmclock.set

interface DialogMessageProvide {
    fun message(): String

    object Empty : DialogMessageProvide {
        override fun message(): String = "Unknown permission!"
    }
}