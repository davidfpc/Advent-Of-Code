package aoc2022

import utils.InputRetrieval

fun main() {
    Day3.execute()
}

object Day3 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private val PRIORITIES = ('a'..'z').plus('A'..'Z')
    private fun part1(input: List<String>): Int = input.map { it.toList() }.sumOf { rucksack ->
        rucksack.chunked(rucksack.size / 2).reduce { acc, items -> acc.intersect(items.toSet()).toList() }.calculatePriorities()
    }

    private fun part2(input: List<String>): Int {
        return input.map { it.toSet() }.chunked(3).sumOf {
            it.reduce { acc, rucksack -> acc.intersect(rucksack) }.calculatePriorities()
        }
    }

    private fun Iterable<Char>.calculatePriorities(): Int = this.sumOf { badge -> PRIORITIES.indexOf(badge) + 1 }

    private fun readInput(): List<String> = InputRetrieval.getFile(2022, 3).readLines()
}
