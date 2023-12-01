package aoc2022

import utils.InputRetrieval

fun main() {
    Day1.execute()
}

object Day1 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    /**
     * Get Elf with the maximum calories.
     */
    private fun part1(input: List<List<Int>>): Int = input.maxOf { it.sum() }

    /**
     * Get Top 3 Elves with the maximum calories.
     */
    private fun part2(input: List<List<Int>>): Int = input.map { it.sum() }.sortedDescending().take(3).sum()

    private fun readInput(): List<List<Int>> {
        val inputString = InputRetrieval.getFile(2022, 1).readText().dropLast(1)
        return inputString.split("\n\n").map { it.split('\n').map { calories -> calories.toInt() } }
    }
}
