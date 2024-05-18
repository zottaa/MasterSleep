package com.github.zottaa.mastersleep.statistic.diary

import java.util.PriorityQueue

interface MostFrequentWordsCalculate {
    fun calculate(words: List<String>): List<WordFrequency>
    class Base : MostFrequentWordsCalculate {
        override fun calculate(words: List<String>): List<WordFrequency> {
            val map = HashMap<String, Long>()
            words.forEach { word ->
                map.put(word, map.getOrDefault(word, 0) + 1)
            }
            val priorityQueue = PriorityQueue<WordFrequency> { a, b ->
                b.frequency.compareTo(a.frequency)
            }
            map.forEach { entry ->
                priorityQueue.add(WordFrequency(entry.key, entry.value))
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
