package aoc2023

import utils.InputRetrieval

fun main() {
    Day13.execute()
}

private object Day13 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<ReflectionPattern>): Long = input.sumOf { it.summarize() }
    private fun part2(input: List<ReflectionPattern>): Long = input.sumOf { it.summarize(smudges = 1) }

    private fun readInput(): List<ReflectionPattern> {
        return InputRetrieval.getFile(2023, 13).readText().split("\n\n").map { ReflectionPattern.parse(it) }
    }

    private data class ReflectionPattern(val pattern: List<String>) {
        fun numberOfReflectedColumns(smudges: Int = 0): Long {
            // Transpose the pattern to reuse the line reflection method
            val transposed = List(pattern.first().length) { j ->
                String(List(pattern.size) { i ->
                    pattern[i][j]
                }.toCharArray())
            }
            return numberOfReflectedLines(transposed, smudges)
        }

        fun numberOfReflectedLines(inputPattern: List<String> = pattern, smudges: Int = 0): Long {
            val foundReflectionLines = mutableListOf<Int>()
            for (i in 1..<inputPattern.size) {
                var errors = 0
                for (line in 1..minOf(i, inputPattern.size - i)) {
                    val below = inputPattern[i - line]
                    val above = inputPattern[i + line - 1]
                    errors += above.indices.count { z -> above[z] != below[z] }
                    if (errors > smudges) {
                        break
                    }
                }
                if (errors == smudges) {
                    foundReflectionLines.add(i)
                }
            }
            return foundReflectionLines.sum().toLong()
        }

        fun summarize(smudges: Int = 0): Long {
            val columns = this.numberOfReflectedColumns(smudges = smudges)
            val lines = this.numberOfReflectedLines(smudges = smudges)
            // println("Column: $columns, Line: $lines")
            return columns + (lines * 100L)
        }

        companion object {
            fun parse(input: String) = ReflectionPattern(input.split("\n").filter { it.isNotBlank() })
        }
    }
}
