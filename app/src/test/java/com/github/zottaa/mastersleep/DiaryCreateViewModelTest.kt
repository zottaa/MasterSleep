package com.github.zottaa.mastersleep

import com.github.zottaa.mastersleep.core.Now
import com.github.zottaa.mastersleep.diary.core.Note
import com.github.zottaa.mastersleep.diary.core.NoteCache
import com.github.zottaa.mastersleep.diary.core.NotesDao
import com.github.zottaa.mastersleep.diary.core.NotesRepository
import com.github.zottaa.mastersleep.diary.create.DiaryCreateViewModel
import com.github.zottaa.mastersleep.streaks.StreaksDataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DiaryCreateViewModelTest {
    private lateinit var now: Now
    private lateinit var dataSource: NotesRepositoryTest.FakeDataSource

    @Before
    fun setup() {
        now = NotesRepositoryTest.FakeNow.Base(7777L)
        dataSource = NotesRepositoryTest.FakeDataSource.Base()
    }

    @Test
    fun main_scenario() {
        val order = mutableListOf<String>()
        val repository: NotesRepository.Create =
            FakeNotesRepository.Base(order, now = now, dao = dataSource)
        val viewModel = DiaryCreateViewModel(
            repository,
            Dispatchers.Unconfined,
            FakeStreaksDataStoreManager.Base(1L, order)
        )
        viewModel.create("title1", "content1", 1L)
        dataSource.checkList(listOf(NoteCache(7777L, "title1", "content1", 1L)))
        assertEquals(
            listOf(
                "NotesRepository#create",
                "StreaksDataStoreManager#updateDiaryStreak"
            ), order
        )
    }

    @Test
    fun not_need_to_update_streak_scenario() {
        val order = mutableListOf<String>()
        val repository: NotesRepository.Create =
            FakeNotesRepository.Base(order, now = now, dao = dataSource)
        val viewModel = DiaryCreateViewModel(
            repository,
            Dispatchers.Unconfined,
            FakeStreaksDataStoreManager.Base(0L, order)
        )
        viewModel.create("title1", "content1", 1L)
        dataSource.checkList(listOf(NoteCache(7777L, "title1", "content1", 1L)))
        assertEquals(
            listOf(
                "NotesRepository#create",
            ), order
        )
    }
}

interface FakeNotesRepository : NotesRepository.All {
    class Base(
        private val order: MutableList<String>,
        private val now: Now,
        private val dao: NotesDao
    ) : FakeNotesRepository {
        override suspend fun notes(epochDay: Long): List<Note> =
            dao.notesWithEpochDay(epochDay).map { Note(it.id, it.title, it.content, it.date) }

        override suspend fun notesInEpochDayRange(begin: Long, end: Long): List<Note> =
            dao.notesInEpochDayRange(begin, end).map { Note(it.id, it.title, it.content, it.date) }


        override suspend fun create(title: String, content: String, date: Long) {
            order.add("NotesRepository#create")
            dao.insert(NoteCache(now.timeInMillis(), title, content, date))
        }

        override suspend fun deleteNote(id: Long) {
            order.add("NotesRepository#delete")
            dao.delete(id)
        }

        override suspend fun updateNote(id: Long, title: String, content: String) {
            order.add("NotesRepository#update")
            val note = dao.note(id)
            dao.insert(
                note.copy(
                    title = title,
                    content = content
                )
            )
        }

        override suspend fun note(id: Long): Note =
            dao.note(id).let { Note(it.id, it.title, it.content, it.date) }
    }
}

interface FakeStreaksDataStoreManager : StreaksDataStoreManager {
    fun isNeedToBeUpdate(): Boolean
    class Base(
        private val day: Long,
        private val order: MutableList<String>
    ) : FakeStreaksDataStoreManager {
        var lastUpdateDay = 0L

        override fun isNeedToBeUpdate() =
            lastUpdateDay == day - 1


        override suspend fun getDiaryStreak(): Flow<Long> {
            throw IllegalStateException("Not used in test")
        }

        override suspend fun getSleepStreak(): Flow<Long> {
            throw IllegalStateException("Not used in test")
        }

        override fun getSleepStreakMax(): Flow<Long> =
            throw IllegalStateException("Not used in test")

        override fun getDiaryStreakMax(): Flow<Long> =
            throw IllegalStateException("Not used in test")

        override suspend fun updateDiaryStreak() {
            if (isNeedToBeUpdate()) {
                order.add("StreaksDataStoreManager#updateDiaryStreak")
            }
        }

        override suspend fun updateSleepStreak() {
            throw IllegalStateException("Not used in test")
        }
    }
}