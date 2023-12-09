package aoc2023

import utils.InputRetrieval

fun main() {
    Day9.execute()
}

private object Day9 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<List<Long>>): Long = input.sumOf { it.calculateLastDigit() }

    private fun part2(input: List<List<Long>>): Long = input.sumOf { it.calculateFirstDigit() }

    private fun readInput(): List<List<Long>> = InputRetrieval.getFile(2023, 9)
        .readLines()
        .map { str -> str.split(' ').map { it.toLong() } }

    private fun List<Long>.calculateLastDigit(): Long = calculateDigit({ it.last() }) { a, b -> a + b }
    private fun List<Long>.calculateFirstDigit(): Long = calculateDigit({ it.first() }) { a, b -> a - b }

    private fun List<Long>.calculateDigit(digitRetrieval: (List<Long>) -> Long, op: (Long, Long) -> Long): Long {
        return if (this.all { it == 0L }) {
            0L
        } else {
            val diff = this.windowed(2).map { it.last() - it.first() }
            op(digitRetrieval.invoke(this), diff.calculateDigit(digitRetrieval, op))
        }
    }
}
