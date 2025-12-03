package aoc2025

import utils.InputRetrieval

fun main() {
    Day3.execute()
}

private object Day3 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<String>): Long = input.sumOf { it.calculateLargestJoltage(2) }

    private fun part2(input: List<String>): Long = input.sumOf { it.calculateLargestJoltage(12) }

    private fun String.calculateLargestJoltage(size: Int): Long {
        var currentSolution: Set<Int> = emptySet()
        repeat(size) {
            // Get the index that generates the next biggest number, and add it to the List. Duplicates? Never heard of them!
            val indexesToTry = (0 until this.length).subtract(currentSolution)
            val newIndex = indexesToTry.maxWith { a, b -> this.getValue(currentSolution + a).compareTo(this.getValue(currentSolution + b)) }
            currentSolution = currentSolution + newIndex
        }
        return this.getValue(currentSolution)
    }

    private fun String.getValue(indexes: Set<Int>): Long {
        return indexes.sorted().joinToString(separator = "") { this[it].toString() }.toLong()
    }

    private fun readInput(): List<String> = InputRetrieval.getFile(2025, 3).readLines()
}
