package com.github.zottaa.mastersleep.core

import android.content.Context
import androidx.room.Room
import com.github.zottaa.mastersleep.alarmclock.schedule.AlarmClockSchedule
import com.github.zottaa.mastersleep.alarmclock.schedule.SleepRequestManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindAlarmClockScheduler(scheduler: AlarmClockSchedule.Base): AlarmClockSchedule

    @Binds
    @Singleton
    abstract fun bindNow(now: Now.Base): Now

    @Binds
    @Singleton
    abstract fun bindSleepRequestManager(sleepRequestManager: SleepRequestManager.Base): SleepRequestManager.All

    companion object {
        @Provides
        @Named("IO")
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Named("Main")
        fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

        @Singleton
        @Provides
        fun provideAppDatabase(@ApplicationContext context: Context): AppDataBase =
            Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "SleepMasterDatabase.db"
            )
                .build()

        @Provides
        @Singleton
        fun provideNow(): Now.Base = Now.Base()
    }
}