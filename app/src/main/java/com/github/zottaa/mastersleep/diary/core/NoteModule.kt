package com.github.zottaa.mastersleep.diary.core

import com.github.zottaa.mastersleep.core.AppDataBase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    @Binds
    @Singleton
    abstract fun bindNotesRepositoryEdit(repository: NotesRepository.Base): NotesRepository.Edit

    companion object {
        @Provides
        @Singleton
        fun provideNotesDao(dataBase: AppDataBase) = dataBase.noteDao()
    }
}