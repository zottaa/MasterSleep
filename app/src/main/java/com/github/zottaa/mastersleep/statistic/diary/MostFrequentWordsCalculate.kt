package com.github.zottaa.mastersleep.statistic.diary

import java.util.PriorityQueue

interface MostFrequentWordsCalculate {
    fun calculate(words: List<String>): List<WordFrequency>

    class Base : MostFrequentWordsCalculate {
        override fun calculate(words: List<String>): List<WordFrequency> {
            val priorityQueue = PriorityQueue<WordFrequency> { a, b ->
                b.frequency.compareTo(a.frequency)
            }
            for (word in words) {
                val existingWordFrequency = priorityQueue.find { it.word == word }
                if (existingWordFrequency != null) {
                    val updatedWordFrequency = WordFrequency(
                        existingWordFrequency.word,
                        existingWordFrequency.frequency + 1
                    )
                    priorityQueue.remove(existingWordFrequency)
                    priorityQueue.offer(updatedWordFrequency)
                } else {
                    priorityQueue.offer(WordFrequency(word, 1))
                }
            }
            val result = mutableListOf<WordFrequency>()
            while (priorityQueue.isNotEmpty()) {
                priorityQueue.poll()?.let { result.add(it) }
            }
            return result
        }
    }
}

data class WordFrequency(val word: String, val frequency: Long)