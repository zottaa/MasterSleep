package com.github.zottaa.mastersleep.statistic.diary

import androidx.lifecycle.ViewModel
import com.github.zottaa.mastersleep.core.Dispatcher
import com.github.zottaa.mastersleep.core.DispatcherType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class DiaryStatisticViewModel @Inject constructor(
    @Dispatcher(DispatcherType.IO)
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    fun init() {

    }
}