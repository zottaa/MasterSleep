package com.github.zottaa.mastersleep.diary.core

import com.github.zottaa.mastersleep.core.Now
import javax.inject.Inject

interface NotesRepository {
    interface ReadList {
        suspend fun notes(epochDay: Long): List<Note>

        suspend fun notesInEpochDayRange(begin: Long, end: Long): List<Note>
    }

    interface Create {
        suspend fun create(title: String = "", content: String = "", date: Long)
    }

    interface Edit {
        suspend fun deleteNote(id: Long)
        suspend fun updateNote(id: Long, title: String = "", content: String = "")
        suspend fun note(id: Long): Note
    }

    interface All : ReadList, Create, Edit

    class Base @Inject constructor(
        private val now: Now,
        private val dao: NotesDao
    ) : All {
        override suspend fun notes(date: Long): List<Note> =
            dao.notesWithEpochDay(date).map { Note(it.id, it.title, it.content, it.date) }

        override suspend fun notesInEpochDayRange(begin: Long, end: Long): List<Note> =
            dao.notesInEpochDayRange(begin, end).map { Note(it.id, it.title, it.content, it.date) }



        override suspend fun create(title: String, content: String, date: Long) {
            dao.insert(NoteCache(now.timeInMillis(), title, content, date))
        }

        override suspend fun deleteNote(id: Long) {
            dao.delete(id)
        }

        override suspend fun updateNote(id: Long, title: String, content: String) {
            val note = dao.note(id)
            dao.insert(note.copy(title = title, content = content))
        }

        override suspend fun note(id: Long): Note =
            dao.note(id).let { Note(it.id, it.title, it.content, it.date) }

    }
}

data class Note(
    private val id: Long,
    private val title: String,
    private val content: String,
    private val date: Long
) {
    fun toUi() =
        NoteUi(id, title, content, date)

}