package com.github.zottaa.mastersleep

import com.github.zottaa.mastersleep.statistic.diary.MostFrequentWordsCalculate
import com.github.zottaa.mastersleep.statistic.diary.WordFrequency
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class MostFrequentWordsCalculateTest {
    private lateinit var mostFrequentWordsCalculate: MostFrequentWordsCalculate

    @Before
    fun setup() {
        mostFrequentWordsCalculate = MostFrequentWordsCalculate.Base()
    }

    @Test
    fun main_scenario() {
        val words = listOf(
            "world",
            "hello",
            "world",
            "hello",
            "worl",
            "hello",
        )
        val expected = listOf(
            WordFrequency("hello", 3),
            WordFrequency("world", 2),
            WordFrequency("worl", 1)
        )
        val actual = mostFrequentWordsCalculate.calculate(words)
        assertEquals(expected, actual)
    }

    @Test
    fun empty_scenario() {
        val words = listOf<String>()
        val expected = listOf<WordFrequency>()
        val actual = mostFrequentWordsCalculate.calculate(words)
        assertEquals(expected, actual)
    }
}