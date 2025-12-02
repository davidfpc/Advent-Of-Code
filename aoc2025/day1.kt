package aoc2025

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

    private fun part1(input: List<Int>): Int {
        var dialValue = 50
        return input.count {
            dialValue = (dialValue + it).floorMod(100)
            dialValue == 0
        }
    }

    private fun part2(input: List<Int>): Int {
        var dialValue = 50
        return input.sumOf {
            val prevValue = dialValue
            val newValue = (dialValue + it)
            dialValue = newValue.floorMod(100)

            // Count how many times we cross 0. For negative rotations, we need to check if we actually crossed 0, to add a count.
            abs(newValue.div(100)) + if (it < 0 && prevValue != 0 && newValue <= 0) 1 else 0
        }
    }

    private fun Int.floorMod(other: Int) = (this % other + other) % other

    /**
     * Parses the dial rotation, converting left into negative numbers.
     */
    private fun readInput(): List<Int> = InputRetrieval.getFile(2025, 1).readLines()
        .map { ((if (it[0] == 'L') "-${it.drop(1)}" else it.drop(1))).toInt() }
}
