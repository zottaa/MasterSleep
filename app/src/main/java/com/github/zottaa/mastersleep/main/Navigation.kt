package com.github.zottaa.mastersleep.main

import androidx.lifecycle.LiveData
import com.github.zottaa.mastersleep.core.Screen
import com.github.zottaa.mastersleep.core.SingleLiveEvent
import javax.inject.Inject

interface Navigation {
    interface Read {
        fun liveData(): LiveData<Screen>
    }

    interface Update {
        fun update(destination: Screen)
    }

    interface Mutable : Read, Update

    class Base @Inject constructor(
        private val liveData: SingleLiveEvent<Screen>
    ) :
        Mutable {

        override fun liveData(): LiveData<Screen> = liveData

        override fun update(destination: Screen) {
            liveData.value = destination
        }

    }
}