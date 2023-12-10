package aoc2023

import utils.InputRetrieval

fun main() {
    Day10.execute()
}

private object Day10 {
    fun execute() {
        val input = readInput()
        val foundPositions = mutableMapOf<Position, Int>()
        println("Part 1: ${part1(input, foundPositions)}")
        println("Part 2: The answer is one of these, just try both: ${part2(input, foundPositions)}")
    }

    private fun part1(input: List<String>, foundPositions: MutableMap<Position, Int>): Int {
        val startIndex: Position = getStartPosition(input)
        foundPositions[startIndex] = 0

        for (i in input.findConnectedPipes(startIndex)) {
            var moveNumber = 1
            // For each visited move, just store the number of steps that we took to get there
            input.navigatePipes(i) { move ->
                val (pos, _) = move
                if (moveNumber < (foundPositions[pos] ?: Integer.MAX_VALUE)) {
                    foundPositions[pos] = moveNumber
                }
                moveNumber += 1
            }
        }
        return foundPositions.values.max()
    }

    private fun part2(initialInput: List<String>, pipePositions: Map<Position, Int>): List<Int> {
        // Filter the input, to replace all unconnected pipes with a blank symbol ('.')
        val filteredInput = initialInput.mapIndexed { y, row ->
            String(row.mapIndexed { x, c ->
                if (pipePositions.contains(Position(x, y))) {
                    c
                } else {
                    '.'
                }
            }.toCharArray())
        }

        // Just calculate both directions, checking with side is the correct one is too big of a PITA
        val startIndex: Position = getStartPosition(filteredInput)
        return filteredInput.findConnectedPipes(startIndex).map {
            val foundPositions = mutableMapOf<Position, MutableSet<DIRECTION>>()
            // Check the starting position neighbours
            filteredInput.checkPart2(Move(startIndex, it.fromDirection), foundPositions)

            // For each visited move, just check the neighbours, and if it is a corner piece, to something special
            filteredInput.navigatePipes(it) { move ->
                filteredInput.checkPart2(move, foundPositions)
                if (filteredInput.getPos(move.pos) in listOf('L', '7', 'F', 'J')) {
                    val (_, nextDirection) = filteredInput.getNextPosition(move)
                    filteredInput.checkPart2(Move(move.pos, nextDirection), foundPositions)
                }
            }
            foundPositions.filter { c -> c.value.size == 4 }.count()
        }
    }

    private fun readInput(): List<String> = InputRetrieval.getFile(2023, 10).readLines()

    private fun List<String>.checkPart2(
        tmpMove: Move,
        foundPositions: MutableMap<Position, MutableSet<DIRECTION>>,
    ) {
        val (pos, _) = tmpMove
        when (tmpMove.fromDirection) {
            DIRECTION.DOWN -> {
                ((pos.x + 1)..<this.first().length).map { Position(it, pos.y) }
                    .takeWhile { getPos(it) == '.' }
                    .forEach { foundPositions.getOrPut(it) { mutableSetOf() }.add(DIRECTION.RIGHT) }
            }

            DIRECTION.LEFT -> {
                ((pos.y + 1)..<this.size).map { Position(pos.x, it) }
                    .takeWhile { getPos(it) == '.' }
                    .forEach { foundPositions.getOrPut(it) { mutableSetOf() }.add(DIRECTION.DOWN) }
            }

            DIRECTION.UP -> {
                (0..<(pos.x)).reversed().map { Position(it, pos.y) }
                    .takeWhile { getPos(it) == '.' }
                    .forEach { foundPositions.getOrPut(it) { mutableSetOf() }.add(DIRECTION.LEFT) }
            }

            DIRECTION.RIGHT -> {
                (0..<(pos.y)).reversed().map { Position(pos.x, it) }
                    .takeWhile { getPos(it) == '.' }
                    .forEach { foundPositions.getOrPut(it) { mutableSetOf() }.add(DIRECTION.UP) }
            }
        }
    }

    private fun getStartPosition(input: List<String>): Position =
        input.mapIndexed { index, line -> index to line.indexOfFirst { it == 'S' } }.first { it.second != -1 }.let { Position(it.second, it.first) }

    private fun List<String>.navigatePipes(move: Move, calculate: (Move) -> Unit) {
        var tmpMove = move
        while (getPos(tmpMove.pos) != 'S') {
            calculate.invoke(tmpMove)
            tmpMove = getNextPosition(tmpMove)
        }
    }

    private fun List<String>.getNextPosition(previousPosition: Move): Move {
        val (pos, fromDirection) = previousPosition
        return when (getPos(pos)) {
            '|' -> if (fromDirection == DIRECTION.DOWN) {
                Move(Position(pos.x, pos.y - 1), DIRECTION.DOWN)
            } else {
                Move(Position(pos.x, pos.y + 1), DIRECTION.UP)
            }

            '-' -> if (fromDirection == DIRECTION.LEFT) {
                Move(Position(pos.x + 1, pos.y), DIRECTION.LEFT)
            } else {
                Move(Position(pos.x - 1, pos.y), DIRECTION.RIGHT)
            }

            'L' -> if (fromDirection == DIRECTION.UP) {
                Move(Position(pos.x + 1, pos.y), DIRECTION.LEFT)
            } else {
                Move(Position(pos.x, pos.y - 1), DIRECTION.DOWN)
            }

            'J' -> if (fromDirection == DIRECTION.UP) {
                Move(Position(pos.x - 1, pos.y), DIRECTION.RIGHT)
            } else {
                Move(Position(pos.x, pos.y - 1), DIRECTION.DOWN)
            }

            '7' -> if (fromDirection == DIRECTION.DOWN) {
                Move(Position(pos.x - 1, pos.y), DIRECTION.RIGHT)
            } else {
                Move(Position(pos.x, pos.y + 1), DIRECTION.UP)
            }

            'F' -> if (fromDirection == DIRECTION.DOWN) {
                Move(Position(pos.x + 1, pos.y), DIRECTION.LEFT)
            } else {
                Move(Position(pos.x, pos.y + 1), DIRECTION.UP)
            }

            else -> {
                throw Exception("invalid transition")
            }
        }
    }

    private fun List<String>.findConnectedPipes(pos: Position): List<Move> {
        val linkedPipes = mutableListOf<Move>()
        val up = Position(pos.x, pos.y - 1)
        if (this.getPos(up) in listOf('|', '7', 'F')) {
            linkedPipes.add(Move(up, DIRECTION.DOWN))
        }
        val down = Position(pos.x, pos.y + 1)
        if (this.getPos(down) in listOf('|', 'L', 'J')) {
            linkedPipes.add(Move(down, DIRECTION.UP))
        }
        val left = Position(pos.x - 1, pos.y)
        if (this.getPos(left) in listOf('-', 'L', 'F')) {
            linkedPipes.add(Move(left, DIRECTION.RIGHT))
        }
        val right = Position(pos.x + 1, pos.y)
        if (this.getPos(right) in listOf('-', '7', 'J')) {
            linkedPipes.add(Move(right, DIRECTION.LEFT))
        }
        return linkedPipes
    }

    private fun List<String>.getPos(pos: Position): Char = if (pos.y in indices && pos.x in this.first().indices) {
        this[pos.y][pos.x]
    } else {
        '.'
    }

    private enum class DIRECTION {
        UP, DOWN, LEFT, RIGHT
    }

    private data class Position(val x: Int, val y: Int)

    private data class Move(val pos: Position, val fromDirection: DIRECTION)
}
