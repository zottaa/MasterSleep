package com.github.zottaa.mastersleep

import com.github.zottaa.mastersleep.diary.core.NoteCache
import com.github.zottaa.mastersleep.diary.core.NoteUi
import com.github.zottaa.mastersleep.diary.core.NotesRepository
import com.github.zottaa.mastersleep.diary.list.DiaryListViewModel
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DiaryListViewModelTest {
    @Test
    fun main_scenario() {
        val now = NotesRepositoryTest.FakeNow.Base(7777L)
        val dataSource = NotesRepositoryTest.FakeDataSource.Base()
        dataSource.expectList(
            listOf(
                NoteCache(1, "title1", "content1", 1L),
                NoteCache(2, "title2", "content2", 1L),
                NoteCache(3, "title3", "content3", 2L)
            )
        )
        val repository = NotesRepository.Base(now, dataSource)
        val viewModel = DiaryListViewModel(
            repository,
            Dispatchers.Unconfined
        )
        viewModel.selectDay(LocalDate.ofEpochDay(1))
        assertEquals(
            listOf(
                NoteUi(1, "title1", "content1", 1L),
                NoteUi(2, "title2", "content2", 1L)
            ),
            viewModel.notes.value
        )
        viewModel.selectDay(LocalDate.ofEpochDay(2))
        assertEquals(
            listOf(
                NoteUi(3, "title3", "content3", 2L)
            ),
            viewModel.notes.value
        )
    }
}