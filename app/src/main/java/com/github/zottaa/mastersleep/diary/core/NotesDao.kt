package com.github.zottaa.mastersleep.diary.core

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteCache)

    @Query("SELECT * FROM notes where id = :noteId")
    suspend fun note(noteId: Long): NoteCache

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun delete(noteId: Long)

    @Query("SELECT * FROM notes WHERE date = :epochDay")
    suspend fun notesWithEpochDay(epochDay: Long): List<NoteCache>

    @Query("SELECT * FROM notes where date between :begin and :end")
    suspend fun notesInEpochDayRange(begin: Long, end: Long): List<NoteCache>
}