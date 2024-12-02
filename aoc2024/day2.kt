package aoc2024

import utils.InputRetrieval
import kotlin.math.abs

fun main() {
    Day2.execute()
}

private object Day2 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<List<Long>>): Int = input.count { report -> report.isReportSafe() }

    private fun part2(input: List<List<Long>>): Int = input.count { report ->
        if (!report.isReportSafe()) {
            // Just try to remove each element, and see if it is valid
            report.indices.any { report.toMutableList().apply { this.removeAt(it) }.isReportSafe() }
        } else {
            true
        }
    }

    private fun List<Long>.isReportSafe(): Boolean {
        var allDecreasing = true
        var allIncreasing = true
        for (i in 1 until this.size) {
            val prevValue = this[i - 1]
            val currValue = this[i]

            if (currValue >= prevValue) {
                allDecreasing = false
            }
            if (currValue <= prevValue) {
                allIncreasing = false
            }
            if (abs(currValue - prevValue) > 3) {
                return false
            }
        }
        return allIncreasing || allDecreasing
    }

    private fun readInput(): List<List<Long>> = InputRetrieval.getFile(2024, 2).readLines().map { report ->
        report.split(' ').map { it.toLong() }
    }
}
