package aoc2025

import utils.InputRetrieval
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day9.execute()
}

private object Day9 {
    fun execute() {
        val input = readInput()
        println(input)
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<Position>): Long {
        return input.flatMapIndexed { index, position ->
            input.drop(index + 1).map { otherPosition ->
                // calculate the area of the square formed by the two positions
                val height = max(position.y, otherPosition.y) - min(position.y, otherPosition.y) + 1
                val width = max(position.x, otherPosition.x) - min(position.x, otherPosition.x) + 1
                height * width
            }
        }.maxOf { it }
    }

    private fun part2(input: List<Position>): Long {
        return 0L
    }

    private fun readInput(): List<Position> = InputRetrieval.getFile(2025, 9).readLines()
        .map { position ->
            val (x, y) = position.split(',')
            Position(x = x.toLong(), y = y.toLong())
        }

    private data class Position(val x: Long, val y: Long)
}
