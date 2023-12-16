package aoc2023

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

    private fun part1(input: List<Race>): Long = input
        .map { race -> (0L..race.time).filter { holdingTime: Long -> holdingTime * (race.time - holdingTime) > race.recordDistance }.size.toLong() }
        .reduce { acc: Long, i: Long -> acc * i }

    private fun part2(input: List<Race>): Long {
        val race = input.reduce { acc, race -> Race("${acc.time}${race.time}".toLong(), "${acc.recordDistance}${race.recordDistance}".toLong()) }
        return part1(listOf(race))
    }

    private fun readInput(): List<Race> {
        val (rawTime, rawDistance) = InputRetrieval.getFile(2023, 6).readLines()
        val time = rawTime.removePrefix("Time:").split(' ').filter { it.isNotBlank() }
        val distance = rawDistance.removePrefix("Distance:").split(' ').filter { it.isNotBlank() }
        return time.mapIndexed { i, c -> Race(c.toLong(), distance[i].toLong()) }
    }

    private data class Race(val time: Long, val recordDistance: Long)
}
