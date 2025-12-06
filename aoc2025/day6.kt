package aoc2025

import utils.InputRetrieval

fun main() {
    Day6.execute()
}

private object Day6 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<Operation>): Long {
        return input.sumOf { operation -> operation.calculatePart1() }
    }

    private fun part2(input: List<Operation>): Long {
        return input.sumOf { operation -> operation.calculatePart2() }
    }

    private fun readInput(): List<Operation> {
        val lines = InputRetrieval.getFile(2025, 6).readLines()
        val numberLines = lines.dropLast(1)
        val parseOperations = lines.last().splitOperations()

        var currentIndex = 0
        return parseOperations.map { (op, size) ->
            val numbers = numberLines.map { it.substring(currentIndex, currentIndex + size) }
            currentIndex += size + 1
            Operation(command = op.toString(), values = numbers)
        }
    }

    private fun String.splitOperations(): List<Pair<Char, Int>> {
        return Regex("(\\S)(\\s*)").findAll(this)
            .map { matchResult ->
                val op = matchResult.groupValues[1].first()
                val size = matchResult.groupValues[2].length
                op to size
            }.toMutableList().apply {
                val endFileHandling = this.removeLast()
                this.add(endFileHandling.first to endFileHandling.second + 1)
            }
    }

    data class Operation(
        val command: String, val values: List<String>,
    ) {

        fun calculate(parsedValues: List<Long>): Long {
            return when (this.command) {
                "+" -> parsedValues.sum()
                "*" -> parsedValues.reduce { acc, l -> acc * l }
                else -> throw Exception("Unknown operation")
            }
        }

        fun calculatePart1(): Long {
            val parsedValues = values.map { it.trim().toLong() }
            return calculate(parsedValues)
        }

        fun calculatePart2(): Long {
            val parsedValues = (0 until values.first().length).map { column ->
                values.map { it[column] }.joinToString("").trim().toLong()
            }
            return calculate(parsedValues)
        }
    }
}
