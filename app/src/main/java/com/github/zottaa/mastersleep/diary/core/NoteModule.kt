package com.github.zottaa.mastersleep.diary.core

import com.github.zottaa.mastersleep.core.AppDataBase
import com.github.zottaa.mastersleep.diary.create.DiaryCreateViewModel
import com.github.zottaa.mastersleep.diary.list.DateUtils
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NoteModule {

    @Binds
    @Singleton
    abstract fun bindNotesRepositoryReadList(repository: NotesRepository.Base): NotesRepository.ReadList

    @Binds
    @Singleton
    abstract fun bindNotesRepositoryCreate(repository: NotesRepository.Base): NotesRepository.Create

    companion object {
        @Provides
        @Singleton
        fun provideDateUtils(): DateUtils = DateUtils.Base()

        @Provides
        @Singleton
        fun provideNotesDao(dataBase: AppDataBase) = dataBase.noteDao()
    }
}