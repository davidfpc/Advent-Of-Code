package aoc2024

import utils.InputRetrieval
import kotlin.math.abs

fun main() {
    Day1.execute()
}

private object Day1 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: Map<Long, Long>): Long {
        val list1 = input.keys.sorted()
        val list2 = input.values.sorted()
        return list1.mapIndexed { index, value ->
            abs(value - list2[index])
        }.sum()
    }

    private fun part2(input: Map<Long, Long>): Long = input.values
        .sumOf { value -> value * input.keys.count { it == value } }

    private fun readInput(): Map<Long, Long> = InputRetrieval.getFile(2024, 1).readLines().associate {
        val (n1, n2) = it.split("   ")
        n1.toLong() to n2.toLong()
    }
}
