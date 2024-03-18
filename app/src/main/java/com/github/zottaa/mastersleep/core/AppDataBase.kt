package com.github.zottaa.mastersleep.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.zottaa.mastersleep.diary.core.NoteCache
import com.github.zottaa.mastersleep.diary.core.NotesDao


@Database(entities = [NoteCache::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun noteDao(): NotesDao
}