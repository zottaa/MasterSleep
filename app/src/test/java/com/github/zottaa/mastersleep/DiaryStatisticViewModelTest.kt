package com.github.zottaa.mastersleep

import androidx.core.util.Pair
import com.github.zottaa.mastersleep.core.DateUtils
import com.github.zottaa.mastersleep.diary.core.NoteCache
import com.github.zottaa.mastersleep.statistic.diary.DiaryStatisticUiState
import com.github.zottaa.mastersleep.statistic.diary.DiaryStatisticViewModel
import com.github.zottaa.mastersleep.statistic.diary.MostFrequentWordsCalculate
import com.github.zottaa.mastersleep.statistic.diary.WordFrequency
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DiaryStatisticViewModelTest {
    private lateinit var viewModel: DiaryStatisticViewModel

    @Before
    fun setup() {
        val dataSource = NotesRepositoryTest.FakeDataSource.Base()
        dataSource.expectList(
            listOf(
                NoteCache(1, "hello world", "hello hello hello hello", 0),
                NoteCache(2, "wrld", "wrld wrld hello ,,,,world", 1),
                NoteCache(3, "hello", "", 1),
                NoteCache(4, "wrld!", "wrld", 2)
            )
        )
        val now = NotesRepositoryTest.FakeNow.Base(777L)
        val repository = FakeNotesRepository.Base(mutableListOf(), now, dataSource)
        viewModel = DiaryStatisticViewModel(
            repository,
            DateUtils.Base(),
            Dispatchers.Unconfined,
            MostFrequentWordsCalculate.Base()
        )
    }

    @Test
    fun main_scenario() {
        assertEquals(DiaryStatisticUiState.Initial, viewModel.diaryStatisticUiState.value)
        viewModel.findWordsFrequency(Pair("01/01/1970", "03/01/1970"))
        assertEquals(DiaryStatisticUiState.Show, viewModel.diaryStatisticUiState.value)
        assertEquals(
            listOf(
                WordFrequency(word = "hello", frequency = 7),
                WordFrequency(word = "wrld", frequency = 5),
                WordFrequency(word = "world", frequency = 2)
            ),
            viewModel.wordsFrequency.value
        )
    }

    @Test
    fun range_check() {
        assertEquals(DiaryStatisticUiState.Initial, viewModel.diaryStatisticUiState.value)
        viewModel.findWordsFrequency(Pair("01/01/1970", "02/01/1970"))
        assertEquals(DiaryStatisticUiState.Show, viewModel.diaryStatisticUiState.value)
        assertEquals(
            listOf(
                WordFrequency(word = "hello", frequency = 7),
                WordFrequency(word = "wrld", frequency = 3),
                WordFrequency(word = "world", frequency = 2)
            ),
            viewModel.wordsFrequency.value
        )
    }

    @Test
    fun nothing_found() {
        assertEquals(DiaryStatisticUiState.Initial, viewModel.diaryStatisticUiState.value)
        viewModel.findWordsFrequency(Pair("01/01/1971", "03/01/1971"))
        assertEquals(DiaryStatisticUiState.NothingFound, viewModel.diaryStatisticUiState.value)
        assertEquals(
            emptyList<WordFrequency>(),
            viewModel.wordsFrequency.value
        )
        assertEquals(true, viewModel.nothingFoundSnackBar.value)
    }
}