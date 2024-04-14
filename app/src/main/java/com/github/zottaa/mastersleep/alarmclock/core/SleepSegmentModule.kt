package com.github.zottaa.mastersleep.alarmclock.core

import com.github.zottaa.mastersleep.core.AppDataBase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SleepSegmentModule {
    @Binds
    @Singleton
    abstract fun bindSleepSegmentRepositoryRead(repository: SleepSegmentRepository.Base): SleepSegmentRepository.Read

    @Binds
    @Singleton
    abstract fun bindSleepSegmentRepositoryCreate(repository: SleepSegmentRepository.Base): SleepSegmentRepository.Create

    companion object {
        @Provides
        @Singleton
        fun provideSleepSegmentDao(dataBase: AppDataBase) = dataBase.sleepSegmentDao()
    }
}