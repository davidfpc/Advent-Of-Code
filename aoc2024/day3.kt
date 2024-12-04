package aoc2024

import utils.InputRetrieval

fun main() {
    Day3.execute()
}

private object Day3 {
    val MULTIPLICATION_REGEX = "mul\\(([0-9]*),([0-9]*)\\)".toRegex()
    val DO_REGEX = "do\\(\\)".toRegex()
    val DONT_REGEX = "don't\\(\\)".toRegex()

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: String): Long = MULTIPLICATION_REGEX.findAll(input).sumOf { match -> match.executeMultiplicationOperation() }

    private fun part2(input: String): Long {
        val validMultiplicationInstructions = MULTIPLICATION_REGEX.findAll(input)
        val validDoInstructions = DO_REGEX.findAll(input).map { it.range.first }.toMutableList()
        val validDontInstructions = DONT_REGEX.findAll(input).map { it.range.first }.toMutableList()
        var enabled = true
        return validMultiplicationInstructions.sumOf {
            if (enabled) {
                if (it.range.first < (validDontInstructions.firstOrNull() ?: Int.MAX_VALUE)) {
                    it.executeMultiplicationOperation()
                } else {
                    enabled = false
                    val removed = validDontInstructions.removeFirstOrNull() ?: Int.MAX_VALUE
                    validDoInstructions.removeAll { doPos -> doPos < removed }
                    0
                }
            } else {
                if (it.range.first < (validDoInstructions.firstOrNull() ?: 0)) {
                    0
                } else {
                    enabled = true
                    val removed = validDoInstructions.removeFirstOrNull() ?: 0
                    validDontInstructions.removeAll { dontPos -> dontPos < removed }
                    it.executeMultiplicationOperation()
                }
            }
        }
    }

    private fun MatchResult.executeMultiplicationOperation(): Long {
        val (_, a, b) = this.groupValues
        return a.toLong() * b.toLong()
    }

    private fun readInput(): String = InputRetrieval.getFile(2024, 3).readText()
}
