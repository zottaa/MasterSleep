package com.github.zottaa.mastersleep

import com.github.zottaa.mastersleep.core.Now
import com.github.zottaa.mastersleep.diary.core.Note
import com.github.zottaa.mastersleep.diary.core.NoteCache
import com.github.zottaa.mastersleep.diary.core.NotesDao
import com.github.zottaa.mastersleep.diary.core.NotesRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class NotesRepositoryTest {
    private lateinit var now: FakeNow
    private lateinit var dataSource: FakeDataSource
    private lateinit var repository: NotesRepository.All

    @Before
    fun setup() {
        now = FakeNow.Base(7777L)
        dataSource = FakeDataSource.Base()
        repository = NotesRepository.Base(now = now, dao = dataSource)
    }

    @Test
    fun test_add() {
        runBlocking {
            dataSource.expectList(
                listOf(
                    NoteCache(id = 0L, title = "title1", content = "content1", date = 1L),
                    NoteCache(id = 1L, title = "title2", content = "content2", date = 1L)
                )
            )
            val actual = repository.notes(1L)
            val expected = listOf(
                Note(id = 0L, title = "title1", content = "content1", date = 1L),
                Note(id = 1L, title = "title2", content = "content2", date = 1L)
            )
            assertEquals(expected, actual)
            repository.create("title3", "content3", 1L)
            dataSource.checkList(
                listOf(
                    NoteCache(id = 0L, title = "title1", content = "content1", date = 1L),
                    NoteCache(id = 1L, title = "title2", content = "content2", date = 1L),
                    NoteCache(id = 7777L, title = "title3", content = "content3", date = 1L),
                )
            )
        }
    }

    @Test
    fun test_ite_in_day() {
        runBlocking {
            dataSource.expectList(
                listOf(
                    NoteCache(id = 0L, title = "title1", content = "content1", date = 1L),
                    NoteCache(id = 1L, title = "title2", content = "content2", date = 1L)
                )
            )
            val actual = repository.notes(1L)
            val expected = listOf(
                Note(id = 0L, title = "title1", content = "content1", date = 1L),
                Note(id = 1L, title = "title2", content = "content2", date = 1L)
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun test_range() {
        runBlocking {
            dataSource.expectList(
                listOf(
                    NoteCache(id = 0L, title = "title1", content = "content1", date = 1L),
                    NoteCache(id = 1L, title = "title2", content = "content2", date = 2L)
                )
            )
            val actual = repository.notesInEpochDayRange(1L, 2L)
            val expected = listOf(
                Note(id = 0L, title = "title1", content = "content1", date = 1L),
                Note(id = 1L, title = "title2", content = "content2", date = 2L)
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun test_delete() {
        runBlocking {
            repository.create("title1", "content1", 1L)
            dataSource.checkList(listOf(NoteCache(id = 7777L, "title1", "content1", 1L)))

            repository.create("title2", "content2", 1L)
            dataSource.checkList(
                listOf(
                    NoteCache(id = 7777L, "title1", "content1", 1L),
                    NoteCache(id = 7778L, "title2", "content2", 1L)
                )
            )

            repository.deleteNote(id = 7778L)
            dataSource.checkList(listOf(NoteCache(id = 7777L, "title1", "content1", 1L)))
        }
    }

    @Test
    fun test_update() {
        runBlocking {
            repository.create("title1", "content1", 1L)
            dataSource.checkList(listOf(NoteCache(id = 7777L, "title1", "content1", 1L)))

            repository.create("title2", "content2", 1L)
            dataSource.checkList(
                listOf(
                    NoteCache(id = 7777L, "title1", "content1", 1L),
                    NoteCache(id = 7778L, "title2", "content2", 1L)
                )
            )

            repository.updateNote(id = 7778L, "new title", "new content")
            dataSource.checkList(
                listOf(
                    NoteCache(id = 7777L, "title1", "content1", 1L),
                    NoteCache(id = 7778L, "new title", "new content", 1L)
                )
            )
        }
    }

    interface FakeNow : Now {
        class Base(private var value: Long) : FakeNow {
            override fun timeInMillis() =
                value++

        }
    }

    interface FakeDataSource : NotesDao {
        fun checkList(expected: List<NoteCache>)

        fun expectList(list: List<NoteCache>)

        class Base : FakeDataSource {
            private val list = mutableListOf<NoteCache>()
            override fun checkList(expected: List<NoteCache>) {
                assertEquals(expected, list)
            }

            override fun expectList(list: List<NoteCache>) {
                this.list.addAll(list)
            }

            override suspend fun insert(note: NoteCache) {
                val found = list.find { it.id == note.id }
                val add = found == null
                if (add) {
                    list.add(note)
                } else {
                    list[list.indexOf(found)] = note
                }
            }

            override suspend fun note(noteId: Long): NoteCache {
                return list.find { it.id == noteId }!!
            }

            override suspend fun delete(noteId: Long) {
                list.remove(note(noteId))
            }

            override suspend fun notesWithEpochDay(epochDay: Long): List<NoteCache> {
                return list.filter { it.date == epochDay }
            }

            override suspend fun notesInEpochDayRange(begin: Long, end: Long): List<NoteCache> {
                return list.filter { it.date in begin..end }
            }

        }
    }
}