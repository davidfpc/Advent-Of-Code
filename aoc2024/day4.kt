package aoc2024

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

    private val WORD = listOf('X', 'M', 'A', 'S')
    private fun part1(input: List<List<Char>>): Long {
        return input.mapIndexed { rowNumber, row ->
            row.mapIndexed { columnNumber, letter ->
                if (letter == WORD.first()) {
                    listOf(-1, 0, 1).sumOf { rowDelta ->
                        listOf(-1, 0, 1).sumOf { columnDelta ->
                            input.checkWord(rowNumber, columnNumber, 0, rowDelta to columnDelta)
                        }
                    }
                } else {
                    0L
                }
            }.sum()
        }.sum()
    }

    private fun List<List<Char>>.checkWord(row: Int, column: Int, wordIndex: Int, delta: Pair<Int, Int>): Long {
        if (wordIndex == (WORD.size - 1)) {
            return 1L
        }
        val (rowDelta, columnDelta) = delta
        val newRow = row + rowDelta
        val newColumn = column + columnDelta
        val nextLetterIndex = wordIndex + 1
        return if (newRow >= 0 && newRow < this.size && newColumn >= 0 && newColumn < this.first().size && this[newRow][newColumn] == WORD[nextLetterIndex]) {
            checkWord(newRow, newColumn, nextLetterIndex, delta)
        } else {
            0L
        }
    }

    private fun part2(input: List<List<Char>>): Long {
        return input.mapIndexed { rowNumber, row ->
            row.mapIndexed { columnNumber, letter ->
                if (letter == 'A') {
                    input.checkCross(rowNumber, columnNumber)
                } else {
                    0L
                }
            }.sum()
        }.sum()
    }

    private fun List<List<Char>>.checkCross(row: Int, column: Int): Long {
        // Check bounds
        if (row < 1 || column < 1 || row > this.size - 2 || column > this.first().size - 2) {
            return 0
        }
        if (((this[row - 1][column - 1] == 'M' && this[row + 1][column + 1] == 'S') || (this[row - 1][column - 1] == 'S' && this[row + 1][column + 1] == 'M'))
            && ((this[row - 1][column + 1] == 'M' && this[row + 1][column - 1] == 'S') || (this[row - 1][column + 1] == 'S' && this[row + 1][column - 1] == 'M'))
        ) {
            return 1
        }
        return 0L
    }

    private fun readInput(): List<List<Char>> = InputRetrieval.getFile(2024, 4).readLines().map { it.toList() }
}
