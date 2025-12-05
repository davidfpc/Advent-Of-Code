package aoc2025

import utils.InputRetrieval

fun main() {
    Day4.execute()
}

private object Day4 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<String>): Long {
        val (numberOfRollsRemoved, _) = input.removeRollOfPapers()
        return numberOfRollsRemoved
    }

    private fun part2(input: List<String>): Long {
        var totalRollsRemoved = 0L
        var board = input
        do {
            val (numberOfRollsRemoved, newBoard) = board.removeRollOfPapers()
            board = newBoard
            totalRollsRemoved += numberOfRollsRemoved
        } while (numberOfRollsRemoved > 0L)
        return totalRollsRemoved
    }

    private fun List<String>.removeRollOfPapers(): Pair<Long, List<String>> {
        var accessibleRoll = 0L
        val newBoard = this.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, position ->
                if (position == ROLL_OF_PAPER) {
                    if (NEIGHBOURS.count { (deltaRow, deltaCol) -> this.getPosValue(rowIndex + deltaRow, columnIndex + deltaCol) == ROLL_OF_PAPER } < 4) {
                        accessibleRoll++
                        EMPTY_SPACE
                    } else {
                        ROLL_OF_PAPER
                    }
                } else {
                    EMPTY_SPACE
                }
            }.joinToString(separator = "")
        }
        return accessibleRoll to newBoard
    }

    private fun List<String>.getPosValue(row: Int, col: Int): Char {
        if (row < 0 || row >= this.size || col < 0 || col >= this[row].length) {
            return EMPTY_SPACE // Out of bounds positions are considered empty
        }
        return this[row][col]
    }

    private fun readInput(): List<String> = InputRetrieval.getFile(2025, 4).readLines()

    private val NEIGHBOURS = listOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)
    private const val ROLL_OF_PAPER = '@'
    private const val EMPTY_SPACE = '.'
}
