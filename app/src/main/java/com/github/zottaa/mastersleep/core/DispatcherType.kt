package com.github.zottaa.mastersleep.core

import javax.inject.Qualifier

enum class DispatcherType {
    IO, Main
}

@Qualifier
annotation class Dispatcher(val type: DispatcherType)