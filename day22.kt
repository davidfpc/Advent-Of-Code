import Day22.part1
import Day22.part2
import Day22.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day22 {

    fun part1(input: Pair<List<String>, List<Any>>): Int {
        val (initialBoard, moves) = input
        return processMoves(initialBoard, moves) { pos, board ->
            when (pos.third) {
                Direction.DOWN -> Triple(pos.first, board.map { it[pos.first] }.indexOfFirst { it != ' ' }, pos.third)
                Direction.UP -> Triple(pos.first, board.map { it[pos.first] }.indexOfLast { it != ' ' }, pos.third)
                Direction.RIGHT -> Triple(board[pos.second].indexOfFirst { it != ' ' }, pos.second, pos.third)
                Direction.LEFT -> Triple(board[pos.second].indexOfLast { it != ' ' }, pos.second, pos.third)
            }
        }
    }

    fun part2(input: Pair<List<String>, List<Any>>): Int {
        val (initialBoard, moves) = input
        return processMoves(initialBoard, moves) { _, _ -> TODO("Maybe I'll implement this next time") }
    }

    private fun processMoves(initialBoard: List<String>, moves: List<Any>, wrapFunction: (Triple<Int, Int, Direction>, List<String>) -> Triple<Int, Int, Direction>): Int {
        // Pad board so we can do the wrap
        val maxLength = initialBoard.maxOf { it.length }
        val board = initialBoard.map { if (it.length < maxLength) it.padEnd(maxLength, ' ') else it }

        var pos: Triple<Int, Int, Direction> = Triple(board.first().indexOfFirst { it == '.' }, 0, Direction.RIGHT)
        moves.forEach { move ->
            when (move) {
                is Char -> pos = Triple(pos.first, pos.second, pos.third.rotate(move))
                is Int -> {
                    for (i in 1..move) {
                        // board.printBoard(pos)
                        val prevPos = pos
                        pos = pos.applyDelta(board)
                        when (board[pos.second][pos.first]) {
                            '#' -> {
                                pos = prevPos
                                break // face a wall, skip this move
                            }

                            ' ' -> {
                                // empty position, need to wrap
                                pos = wrapFunction(pos, board)
                                if (board[pos.second][pos.first] == '#') {
                                    pos = prevPos
                                    break // face a wall, skip this move
                                }
                            }
                        }
                    }
                }
            }
        }
        return pos.calcResult()
    }

    private fun List<String>.printBoard(pos: Triple<Int, Int, Direction>) {
        for (y in this.indices) {
            for (x in this[y].indices) {
                if (x == pos.first && y == pos.second) {
                    print('X')
                } else {
                    print(this[y][x])
                }
            }
            println()
        }
        println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-")
        println("+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-")
    }

    private fun Triple<Int, Int, Direction>.applyDelta(board: List<String>): Triple<Int, Int, Direction> {
        val delta = when (this.third) {
            Direction.DOWN -> 0 to 1
            Direction.UP -> 0 to -1
            Direction.RIGHT -> 1 to 0
            Direction.LEFT -> -1 to 0
        }
        var newY = (this.second + delta.second) % board.size
        if (newY < 0) {
            newY = board.size - 1
        }
        var newX = (this.first + delta.first) % board[newY].length
        if (newX < 0) {
            newX = board[newY].length - 1
        }
        return Triple(newX, newY, this.third)
    }

    private fun Triple<Int, Int, Direction>.calcResult(): Int {
        return ((this.first + 1) * 4) + ((this.second + 1) * 1000) + when (this.third) {
            Direction.RIGHT -> 0
            Direction.DOWN -> 1
            Direction.LEFT -> 2
            Direction.UP -> 3
        }
    }

    enum class Direction {
        DOWN, UP, RIGHT, LEFT;

        fun rotate(rotation: Char): Direction {
            return when (this) {
                DOWN -> when (rotation) {
                    'R' -> LEFT
                    'L' -> RIGHT
                    else -> TODO("Invalid Option")
                }

                UP -> when (rotation) {
                    'R' -> RIGHT
                    'L' -> LEFT
                    else -> TODO("Invalid Option")
                }

                RIGHT -> when (rotation) {
                    'R' -> DOWN
                    'L' -> UP
                    else -> TODO("Invalid Option")
                }

                LEFT -> when (rotation) {
                    'R' -> UP
                    'L' -> DOWN
                    else -> TODO("Invalid Option")
                }
            }
        }
    }

    fun readInput(): Pair<List<String>, List<Any>> {
        val file = File("inputFiles/day22.txt").readLines()

        val board = file.takeWhile { it.isNotEmpty() }
        val parsedMoves = mutableListOf<Any>()

        var number = ""
        for (i in file.last()) {
            if (i.isDigit()) {
                number += i
            } else {
                if (number != "") {
                    parsedMoves.add(number.toInt())
                    number = ""
                }
                parsedMoves.add(i)
            }
        }
        if (number != "") {
            parsedMoves.add(number.toInt())
        }
        return board to parsedMoves
    }
}
