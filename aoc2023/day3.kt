package aoc2023

import utils.InputRetrieval

fun main() {
    Day3.execute()
}

private object Day3 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<String>): Int {
        val numbers = mutableListOf<Int>()
        var tmpNumber = ""
        var valid = false
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, c ->
                if (c.isDigit()) {
                    tmpNumber += c
                    valid = valid || input.getNeighbours(rowIndex, colIndex).any { !it.isDigit() && it != '.' } // If there is a neighbour with a symbol, it's a valid piece number
                } else {
                    if (valid && tmpNumber.isNotEmpty()) {
                        numbers.add(tmpNumber.toInt())
                    }
                    // Reset flag and base value
                    valid = false
                    tmpNumber = ""
                }
            }
        }
        return numbers.sum()
    }

    private fun part2(input: List<String>): Int {
        val numbers = mutableListOf<Int>()
        input.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, c ->
                if (c == '*') {
                    // Check if it's a gear:
                    val neighbours = input.getNeighbourNumbers(rowIndex, colIndex)
                    if (neighbours.size == 2) {
                        numbers.add(neighbours.first() * neighbours.last()) // Gear ratio
                    } else if (neighbours.size > 2) {
                        println("This should not happen!")
                    }
                }
            }
        }
        return numbers.sum()
    }

    private fun readInput(): List<String> = InputRetrieval.getFile(2023, 3).readLines()

    private fun List<String>.getPos(x: Int, y: Int): Char = if (x in indices && y in this.first().indices) {
        this[x][y]
    } else {
        '.'
    }

    private fun List<String>.getNeighbours(x: Int, y: Int): List<Char> = listOf(
        getPos(x - 1, y - 1), // TOP LEFT
        getPos(x - 1, y),        // TOP
        getPos(x - 1, y + 1), // TOP RIGHT
        getPos(x, y - 1),        // LEFT
        getPos(x, y + 1),        // RIGHT
        getPos(x + 1, y - 1), // BOTTOM LEFT
        getPos(x + 1, y),        // BOTTOM
        getPos(x + 1, y + 1), // BOTTOM RIGHT
    )

    private fun List<String>.getNeighbourNumbers(x: Int, y: Int): List<Int> {
        // LEFT
        val numbers = mutableListOf<Int>()
        if (getPos(x, y - 1).isDigit()) {
            numbers.add(this[x].getNumberToTheLeftOf(y))
        }
        // RIGHT
        if (getPos(x, y + 1).isDigit()) {
            numbers.add(this[x].getNumberToTheRightOf(y + 1))
        }
        // TOP
        val topPos = listOf(getPos(x - 1, y - 1), getPos(x - 1, y), getPos(x - 1, y + 1))
        numbers.addAll(this[x - 1].getNumberFromTopBot(topPos, y))
        // BOTTOM
        val botPos = listOf(getPos(x + 1, y - 1), getPos(x + 1, y), getPos(x + 1, y + 1))
        numbers.addAll(this[x + 1].getNumberFromTopBot(botPos, y))

        return numbers
    }

    private fun String.getNumberToTheLeftOf(y: Int): Int = this.substring(0, y).takeLastWhile { it.isDigit() }.toInt()
    private fun String.getNumberToTheRightOf(y: Int): Int = this.substring(y).takeWhile { it.isDigit() }.toInt()

    private fun String.getNumberFromTopBot(
        pos: List<Char>,
        y: Int,
    ): List<Int> {
        val numbers = mutableListOf<Int>()
        if (pos.none { it.isDigit() }) {                                                         // ...
            // empty top
        } else if (pos.all { it.isDigit() }) {                                                   // XXX
            // number is directly on top - only 3-digit number are allowed!
            numbers.add(pos.joinToString(separator = "").toInt())
        } else {
            if (pos.first().isDigit() && !pos[1].isDigit() && !pos.last().isDigit()) {           // X..
                numbers.add(this.getNumberToTheLeftOf(y))
            } else if (pos.first().isDigit() && pos[1].isDigit()) {                              // XX.
                numbers.add(this.getNumberToTheLeftOf(y + 1))
            } else if (!pos.first().isDigit() && !pos[1].isDigit() && pos.last().isDigit()) {   // ..X
                numbers.add(this.getNumberToTheRightOf(y + 1))
            } else if (pos[1].isDigit() && pos.last().isDigit()) {                              // .XX
                numbers.add(this.getNumberToTheRightOf(y))
            } else if (pos[1].isDigit()) {                                                      // .X.
                // Only middle contains stuff
                numbers.add(pos[1].toString().toInt())
            } else {                                                                            // X.X
                numbers.add(this.getNumberToTheLeftOf(y))
                numbers.add(this.getNumberToTheRightOf(y + 1))
            }
        }
        return numbers
    }
}
