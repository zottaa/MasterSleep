package com.github.zottaa.mastersleep.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.zottaa.mastersleep.alarmclock.core.SleepSegmentCache
import com.github.zottaa.mastersleep.alarmclock.core.SleepSegmentDao
import com.github.zottaa.mastersleep.diary.core.NoteCache
import com.github.zottaa.mastersleep.diary.core.NotesDao


@Database(
    entities = [NoteCache::class, SleepSegmentCache::class],
    version = 1
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun noteDao(): NotesDao

    abstract fun sleepSegmentDao(): SleepSegmentDao
}