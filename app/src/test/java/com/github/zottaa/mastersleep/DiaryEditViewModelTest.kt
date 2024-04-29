package com.github.zottaa.mastersleep

import com.github.zottaa.mastersleep.core.Now
import com.github.zottaa.mastersleep.diary.core.NoteCache
import com.github.zottaa.mastersleep.diary.edit.DiaryEditViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DiaryEditViewModelTest {
    private lateinit var now: Now
    private lateinit var dataSource: NotesRepositoryTest.FakeDataSource
    private lateinit var dispatcher: CoroutineDispatcher

    @Before
    fun setup() {
        now = NotesRepositoryTest.FakeNow.Base(7777L)
        dataSource = NotesRepositoryTest.FakeDataSource.Base()
        dispatcher = Dispatchers.Unconfined
    }

    @Test
    fun update() {
        val order = mutableListOf<String>()
        dataSource.expectList(
            listOf(
                NoteCache(1L, "title1", "content1", 1L),
                NoteCache(2L, "title2", "content2", 2L)
            )
        )
        val repository = FakeNotesRepository.Base(order, now, dataSource)
        val viewModel = DiaryEditViewModel(
            repository,
            dispatcher,
            FakeStreaksDataStoreManager.Base(1L, order)
        )
        dataSource.checkList(
            listOf(
                NoteCache(1L, "title1", "content1", 1L),
                NoteCache(2L, "title2", "content2", 2L)
            )
        )
        viewModel.update(2L, "new_title", "new_content")
        dataSource.checkList(
            listOf(
                NoteCache(1L, "title1", "content1", 1L),
                NoteCache(2L, "new_title", "new_content", 2L)
            )
        )
        assertEquals(
            listOf(
                "NotesRepository#update",
                "StreaksDataStoreManager#updateDiaryStreak"
            ),
            order
        )
    }

    @Test
    fun update_not_need_to_update_streak() {
        val order = mutableListOf<String>()
        dataSource.expectList(
            listOf(
                NoteCache(1L, "title1", "content1", 1L),
                NoteCache(2L, "title2", "content2", 2L)
            )
        )
        val repository = FakeNotesRepository.Base(order, now, dataSource)
        val viewModel = DiaryEditViewModel(
            repository,
            dispatcher,
            FakeStreaksDataStoreManager.Base(0L, order)
        )
        dataSource.checkList(
            listOf(
                NoteCache(1L, "title1", "content1", 1L),
                NoteCache(2L, "title2", "content2", 2L)
            )
        )
        viewModel.update(2L, "new_title", "new_content")
        dataSource.checkList(
            listOf(
                NoteCache(1L, "title1", "content1", 1L),
                NoteCache(2L, "new_title", "new_content", 2L)
            )
        )
        assertEquals(
            listOf(
                "NotesRepository#update",
            ),
            order
        )
    }

    @Test
    fun delete() {
        val order = mutableListOf<String>()
        dataSource.expectList(
            listOf(
                NoteCache(1L, "title1", "content1", 1L),
                NoteCache(2L, "title2", "content2", 2L)
            )
        )
        val repository = FakeNotesRepository.Base(order, now, dataSource)
        val viewModel = DiaryEditViewModel(
            repository,
            dispatcher,
            FakeStreaksDataStoreManager.Base(0L, order)
        )
        dataSource.checkList(
            listOf(
                NoteCache(1L, "title1", "content1", 1L),
                NoteCache(2L, "title2", "content2", 2L)
            )
        )
        viewModel.delete(2L)
        dataSource.checkList(
            listOf(
                NoteCache(1L, "title1", "content1", 1L),
            )
        )
        assertEquals(
            listOf(
                "NotesRepository#delete",
            ),
            order
        )
    }
}