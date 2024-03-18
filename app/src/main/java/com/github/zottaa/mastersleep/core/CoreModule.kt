package com.github.zottaa.mastersleep.core

import android.content.Context
import androidx.room.Room
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
abstract class CoreModule {

    @Binds
    @Singleton
    abstract fun bindNow(now: Now.Base): Now

    companion object {
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
        @Named("IO")
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Named("Main")
        fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

        @Provides
        @Singleton
        fun provideNow(): Now.Base = Now.Base()
    }
}