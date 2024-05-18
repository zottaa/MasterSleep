package com.github.zottaa.mastersleep.core

import android.os.Bundle

interface BundleWrapper {
    interface Save<T> {
        fun save(item: T)
    }

    interface Restore<T> {
        fun restore(): T
    }

    interface Mutable<T> : Save<T>, Restore<T>

    class String(
        private val bundle: Bundle
    ) : Mutable<kotlin.String> {
        override fun save(item: kotlin.String) {
            bundle.putString(KEY, item)
        }

        override fun restore(): kotlin.String = bundle.getString(KEY, "")

        companion object {
            private const val KEY = "stringKey"
        }
    }

    class StringArray(
        private val bundle: Bundle
    ) : Mutable<ArrayList<kotlin.String>> {
        override fun save(item: ArrayList<kotlin.String>) {
            bundle.putStringArrayList(KEY, item)
        }

        override fun restore(): ArrayList<kotlin.String> =
            bundle.getStringArrayList(KEY) ?: ArrayList()

        companion object {
            private const val KEY = "stringArrayListKey"
        }
    }
}
