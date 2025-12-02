package aoc2025

import utils.InputRetrieval

fun main() {
    Day2.execute()
}

private object Day2 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<String>): Long {
        return input.sumOf {
            if (it.isWeirdPalindrome(2)) {
                it.toLong()
            } else {
                0L
            }
        }
    }

    private fun part2(input: List<String>): Long {
        return input.sumOf {
            // I love the smell of brute force in the morning
            if ((2..it.length).any { numberPartitions -> it.isWeirdPalindrome(numberPartitions) }) {
                it.toLong()
            } else {
                0L
            }
        }
    }

    private fun String.isWeirdPalindrome(numberPartitions: Int): Boolean {
        // Not enough characters to split it in that number of partitions
        if (this.length % numberPartitions != 0) {
            return false
        }
        val size = this.length / numberPartitions
        val chunks = this.chunked(size)
        return chunks.size != 1 && chunks.toSet().size == 1
    }

    /**
     * Parses the number ranges, creating a list of strings with all numbers. I have lots of RAM, so I should use it all!
     */
    private fun readInput(): List<String> = InputRetrieval.getFile(2025, 2).readLines().first()
        .split(',')
        .flatMap {
            val (start, finish) = it.split('-').map { number -> number.toLong() }
            (start..finish).map { number -> number.toString() }
        }
}
