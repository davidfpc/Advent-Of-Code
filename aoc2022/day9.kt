package aoc2022

import utils.InputRetrieval

fun main() {
    Day9.execute()
}

object Day9 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<Pair<Char, Int>>): Int = calcMoves(input, 2)

    private fun part2(input: List<Pair<Char, Int>>): Int = calcMoves(input, 10)

    private fun calcMoves(input: List<Pair<Char, Int>>, knotsSize: Int): Int {
        val tailPositions = mutableSetOf(0 to 0)
        val currentPositions: MutableList<Pair<Int, Int>> = MutableList(knotsSize) { 0 to 0 } // x to y

        input.forEach { move ->
            repeat(move.second) {
                var currentHeadPosition = currentPositions.first()
                when (move.first) {
                    'R' -> currentHeadPosition = currentHeadPosition.first + 1 to currentHeadPosition.second
                    'L' -> currentHeadPosition = currentHeadPosition.first - 1 to currentHeadPosition.second
                    'U' -> currentHeadPosition = currentHeadPosition.first to currentHeadPosition.second + 1
                    'D' -> currentHeadPosition = currentHeadPosition.first to currentHeadPosition.second - 1
                }
                currentPositions[0] = currentHeadPosition
                for (i in 1 until currentPositions.size) {
                    currentPositions[i] = updateTailPosition(currentPositions[i - 1], currentPositions[i])
                }
                tailPositions.add(currentPositions.last())
            }
        }
        return tailPositions.size
    }

    private fun updateTailPosition(headPosition: Pair<Int, Int>, tailPosition: Pair<Int, Int>): Pair<Int, Int> {
        val (headXPos, headYPos) = headPosition
        val (tailXPos, tailYPos) = tailPosition

        // Move horizontally
        if (headYPos == tailYPos) {
            if (headXPos > (tailXPos + 1)) {
                return (tailXPos + 1) to tailYPos
            } else if (headXPos < (tailXPos - 1)) {
                return (tailXPos - 1) to tailYPos
            }
        }

        // Move vertically
        if (headXPos == tailXPos) {
            if (headYPos > (tailYPos + 1)) {
                return tailXPos to tailYPos + 1
            } else if (headYPos < (tailYPos - 1)) {
                return tailXPos to (tailYPos - 1)
            }
        }

        // Move diagonally
        if (headXPos > tailXPos && headYPos > (tailYPos + 1) || headXPos > (tailXPos + 1) && headYPos > tailYPos) {
            // Move diagonally Up (right)
            return tailXPos + 1 to tailYPos + 1
        } else if (headXPos > tailXPos && headYPos < (tailYPos - 1) || headXPos > (tailXPos + 1) && headYPos < tailYPos) {
            // Move diagonally Down (right)
            return tailXPos + 1 to tailYPos - 1
        } else if (headXPos < tailXPos && headYPos > (tailYPos + 1) || headXPos < (tailXPos - 1) && headYPos > tailYPos) {
            // Move diagonally Up (left)
            return tailXPos - 1 to tailYPos + 1
        } else if (headXPos < tailXPos && headYPos < (tailYPos - 1) || headXPos < (tailXPos - 1) && headYPos < tailYPos) {
            // Move diagonally Down (left)
            return tailXPos - 1 to tailYPos - 1
        }

        return tailPosition
    }

    fun readInput(): List<Pair<Char, Int>> = InputRetrieval.getFile(2022, 9).readLines().map {
        val (direction, numberPos) = it.split(' ')
        direction.first() to numberPos.toInt()
    }
}
