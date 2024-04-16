package com.github.zottaa.mastersleep.statistic.diary

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class DiaryStatisticViewModel @Inject constructor(
    @Named("IO")
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    fun init() {

    }
}