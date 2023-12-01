package aoc2022

import utils.InputRetrieval

fun main() {
    Day4.execute()
}

object Day4 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<List<Int>>): Int = input.filter { pair ->
        val (start1, end1, start2, end2) = pair
        return@filter (start1 <= start2 && end1 >= end2) || (start2 <= start1 && end2 >= end1)
    }.size

    private fun part2(input: List<List<Int>>): Int = input.filter { pair ->
        val (start1, end1, start2, end2) = pair
        return@filter (start1..end1).intersect(start2..end2).isNotEmpty()
    }.size

    private fun readInput(): List<List<Int>> = InputRetrieval.getFile(2022, 4).readLines().map { pair -> pair.split(',', '-').map { it.toInt() } }
}
