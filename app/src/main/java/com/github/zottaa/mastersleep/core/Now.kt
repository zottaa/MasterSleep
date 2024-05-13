package com.github.zottaa.mastersleep.core

interface Now {
    fun timeInMillis(): Long

    class Base : Now {
        override fun timeInMillis() =
            System.currentTimeMillis()

    }
}