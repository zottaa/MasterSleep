package com.github.zottaa.mastersleep.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.zottaa.mastersleep.core.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val navigation: Navigation.Mutable
) : ViewModel(), Navigation.Read {

    override fun liveData(): LiveData<Screen> = navigation.liveData()
}