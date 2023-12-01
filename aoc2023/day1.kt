package aoc2023

import utils.InputRetrieval

fun main() {
    Day1.execute()
}

private object Day1 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<String>): Int = input.sumOf {
        val values = it.filter { value -> value.isDigit() }
        if (values.isEmpty()) {
            println("No numbers found! <<$it>>")
            0
        } else {
            "${values.first()}${values.last()}".toInt()
        }
    }

    private val REPLACEMENTS: List<Pair<String, String>> = listOf(
        "one" to "o1e",
        "two" to "t2o",
        "three" to "t3e",
        "four" to "4",
        "five" to "5e",
        "six" to "6",
        "seven" to "7n",
        "eight" to "e8t",
        "nine" to "n9e"
    )

    private fun part2(input: List<String>): Int {
        val mappedInput = input.map {
            REPLACEMENTS.fold(it) { value, rep -> value.replace(rep.first, rep.second) }
        }
        return part1(mappedInput)
    }

    private fun readInput(): List<String> = InputRetrieval.getFile(2023, 1).readLines()
}
