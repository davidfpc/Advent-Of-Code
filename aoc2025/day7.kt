package aoc2025

import utils.InputRetrieval

fun main() {
    Day7.execute()
}

private object Day7 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<String>): Long = input.splitBeams().first

    private fun part2(input: List<String>): Long = input.splitBeams().second

    private fun List<String>.splitBeams(): Pair<Long, Long> {
        var beams: List<Pair<Long, Int>> = listOf(1L to this.first().indexOfFirst { it == 'S' })
        var splitCount = 0L
        (1 until this.size).forEach { line ->
            beams = beams.flatMap { (count, position) ->
                if (this[line][position] == '^') {
                    splitCount++
                    listOf(count to position - 1, count to position + 1)
                } else {
                    listOf(count to position)
                }
            }.groupBy { it.second }.map { (pos, list) ->
                list.sumOf { it.first } to pos
            }
        }
        return splitCount to beams.sumOf { it.first }
    }

    private fun readInput(): List<String> {
        return InputRetrieval.getFile(2025, 7).readLines()
    }
}
