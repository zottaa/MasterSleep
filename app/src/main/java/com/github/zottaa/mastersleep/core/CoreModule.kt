package com.github.zottaa.mastersleep.core

import com.github.zottaa.mastersleep.diary.list.DateUtils
import com.github.zottaa.mastersleep.main.Navigation
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun bindNavigation(navigation: Navigation.Base): Navigation.Mutable

    companion object {
        @Provides
        fun provideScreenSingleLiveEvent(): SingleLiveEvent<Screen> = SingleLiveEvent()

        @Provides
        @Singleton
        fun provideDateUtils(): DateUtils = DateUtils.Base()
    }
}