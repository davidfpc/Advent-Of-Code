package aoc2023

import utils.InputRetrieval
import java.util.Stack

fun main() {
    Day16.execute()
}

private object Day16 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<List<Position>>): Int = input.calculateEnergy(0, 0, Delta(1, 0))

    private fun part2(input: List<List<Position>>): Int {
        val pointToVerify = mutableSetOf<Triple<Int, Int, Delta>>()
        for (i in input.first().indices) {
            // Top row, heading downward
            pointToVerify.add(Triple(i, 0, Delta(0, 1)))
            // Bottom row, heading upward
            pointToVerify.add(Triple(i, input.size - 1, Delta(0, -1)))
        }
        for (i in input.indices) {
            // Leftmost column, heading right
            pointToVerify.add(Triple(0, i, Delta(1, 0)))
            // Rightmost column, heading left
            pointToVerify.add(Triple(input.first().size - 1, i, Delta(-1, 0)))
        }

        return pointToVerify.maxOf {
            val (x, y, delta) = it
            input.calculateEnergy(x, y, delta)
        }
    }

    private fun readInput(): List<List<Position>> = InputRetrieval.getFile(2023, 16)
        .readLines()
        .map { row -> row.map { Position(it) } }

    private fun List<List<Position>>.calculateEnergy(x: Int, y: Int, delta: Delta): Int {
        this.reset()
        this.walk(x, y, delta)
        return this.energized()
    }

    private fun List<List<Position>>.walk(initialX: Int, initialY: Int, initialDelta: Delta) {
        val stack = Stack<Triple<Int, Int, Delta>>()
        stack.push(Triple(initialX, initialY, initialDelta))

        while (stack.isNotEmpty()) {
            val (x, y, delta) = stack.pop()

            // Verify boundaries
            if (x < 0 || x >= this.first().size || y < 0 || y >= this.size) {
                continue
            }

            val pos = this[y][x]
            pos.energized = true

            // Prevent Loops
            if (pos.energizedFrom.contains(delta)) {
                continue
            } else {
                pos.energizedFrom.add(delta)
            }

            when (pos.tileType) {
                '.' -> stack.push(Triple(x + delta.x, y + delta.y, delta))
                '|' -> {
                    if (delta.y == 0) {
                        stack.push(Triple(x, y + 1, Delta(0, 1)))
                        stack.push(Triple(x, y - 1, Delta(0, -1)))
                    } else {
                        // Act like a '.'
                        stack.push(Triple(x + delta.x, y + delta.y, delta))
                    }
                }

                '-' -> {
                    if (delta.x == 0) {
                        stack.push(Triple(x + 1, y, Delta(1, 0)))
                        stack.push(Triple(x - 1, y, Delta(-1, 0)))
                    } else {
                        // Act like a '.'
                        stack.push(Triple(x + delta.x, y + delta.y, delta))
                    }
                }

                '\\' -> {
                    val newDelta = if (delta.y == 0) {
                        if (delta.x == 1) {
                            Delta(0, 1) // Go down
                        } else {
                            Delta(0, -1) // Go up
                        }
                    } else {
                        if (delta.y == 1) {
                            Delta(1, 0) // Go right
                        } else {
                            Delta(-1, 0) // Go left
                        }
                    }
                    stack.push(Triple(x + newDelta.x, y + newDelta.y, newDelta))
                }

                '/' -> {
                    val newDelta = if (delta.y == 0) {
                        if (delta.x == 1) {
                            Delta(0, -1) // Go up
                        } else {
                            Delta(0, 1) // Go down
                        }
                    } else {
                        if (delta.y == 1) {
                            Delta(-1, 0) // Go left
                        } else {
                            Delta(1, 0) // Go right
                        }
                    }

                    stack.push(Triple(x + newDelta.x, y + newDelta.y, newDelta))
                }
            }
        }
    }

    private fun List<List<Position>>.energized(): Int = this.sumOf { it.count { tile -> tile.energized } }

    private fun List<List<Position>>.reset() {
        for (i in this) {
            for (z in i) {
                z.energized = false
                z.energizedFrom.clear()
            }
        }
    }

    private data class Position(val tileType: Char, var energized: Boolean = false, val energizedFrom: MutableList<Delta> = mutableListOf())
    private data class Delta(val x: Int, val y: Int)
}
