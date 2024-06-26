package com.github.zottaa.mastersleep.core

import com.github.zottaa.mastersleep.alarmclock.schedule.AlarmClockSchedule
import com.github.zottaa.mastersleep.alarmclock.schedule.SleepRequestManager
import com.github.zottaa.mastersleep.statistic.diary.MostFrequentWordsCalculate
import com.github.zottaa.mastersleep.statistic.pager.ProvideDatePicker
import com.github.zottaa.mastersleep.statistic.sleep.ChartDataSetBuilder
import com.github.zottaa.mastersleep.streaks.StreaksDataStoreManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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

    @Binds
    @Singleton
    abstract fun bindProvideDatePicker(provideDataPicker: ProvideDatePicker.Base): ProvideDatePicker

    @Binds
    @Singleton
    abstract fun bindChartDataSetBuilder(chartDataSetBuilder: ChartDataSetBuilder.Base): ChartDataSetBuilder

    @Binds
    @Singleton
    abstract fun bindStreaksDataStoreManager(streaksDataStoreManager: StreaksDataStoreManager.Base): StreaksDataStoreManager

    companion object {
        @Provides
        @Singleton
        fun provideDateUtils(): DateUtils = DateUtils.Base()

        @Provides
        @Singleton
        fun provideTimeUtils(): DateTimeUtils = DateTimeUtils.Base()

        @Provides
        @Dispatcher(DispatcherType.IO)
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Dispatcher(DispatcherType.Main)
        fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

        @Provides
        @Singleton
        fun provideNow(): Now.Base = Now.Base()

        @Provides
        @Singleton
        fun provideMostFrequentWordsCalculate(): MostFrequentWordsCalculate =
            MostFrequentWordsCalculate.Base()
    }
}

