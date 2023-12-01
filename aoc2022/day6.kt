package aoc2022

import utils.InputRetrieval

fun main() {
    Day6.execute()
}

object Day6 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: String): Int = input.windowed(4, 1).map { it.toSet().size == 4 }.indexOfFirst { it } + 4

    private fun part2(input: String): Int = input.windowed(14, 1).map { it.toSet().size == 14 }.indexOfFirst { it } + 14

    private fun readInput(): String = InputRetrieval.getFile(2022, 6).readLines().first()
}
