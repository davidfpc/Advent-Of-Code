package aoc2024

import aoc2024.Day5.Input.Companion.parseInput
import utils.InputRetrieval

fun main() {
    Day5.execute()
}

private object Day5 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: Input): Int {
        return input.updates.filter { it.isOrderValid(input) }
            .sumOf { it[it.size / 2] }
    }

    private fun part2(input: Input): Int {
        return input.updates.filter { !it.isOrderValid(input) }
            .map { it.fixUpdate(input) }
            .sumOf { it[it.size / 2] }
    }

    private fun List<Int>.isOrderValid(input: Input): Boolean {
        this.indices.forEach { i ->
            val dependents = this[i].getDependents(input)
            if (dependents.any { this.take(i + 1).contains(it) }) {
                return false
            }
        }
        return true
    }

    private fun List<Int>.fixUpdate(input: Input): List<Int> {
        return this.sortedWith { a, b ->
            val dependents = a.getDependents(input)
            if (dependents.contains(b)) {
                1
            } else {
                -1
            }
        }
    }

    private fun Int.getDependents(input: Input): List<Int> {
        return input.orderingRules.filter { it.first == this }.map { it.second }
    }

    private fun readInput(): Input = InputRetrieval.getFile(2024, 5).readLines().parseInput()

    data class Input(val orderingRules: List<Pair<Int, Int>>, val updates: List<List<Int>>) {
        companion object {
            fun List<String>.parseInput(): Input {
                val orderingRules: List<Pair<Int, Int>> = this.takeWhile { it.isNotBlank() }
                    .map { rule ->
                        val (a, b) = rule.split("|").map { it.toInt() }
                        a to b
                    }
                val updates: List<List<Int>> = this.drop(orderingRules.size + 1)
                    .map { update ->
                        update.split(',').map { it.toInt() }
                    }
                return Input(orderingRules, updates)
            }
        }
    }
}
