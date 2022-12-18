import Day17.RockType.MINUS
import Day17.RockType.MIRROR_L
import Day17.RockType.PLUS
import Day17.RockType.SQUARE
import Day17.RockType.VERTICAL
import Day17.part1
import Day17.part2
import Day17.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    // println("Part 2: ${part2(input)}")
}

object Day17 {

    fun part1(input: String, numberOfRocks: Int = 2022): Int {
        //
        // val filledPositions = mutableSetOf<Pair<Int, Int>>()
        // var jetIndex = 0
        // val rockPattern = listOf(MINUS, PLUS, MIRROR_L, VERTICAL, SQUARE)
        // var rockIndex = 0
        // repeat(numberOfRocks) {
        //     var moving = true
        //     // create rock
        //     val rock = Rock((filledPositions.maxOfOrNull { it.second } ?: 0) + 4, rockPattern[rockIndex % rockPattern.size])
        //     rockIndex++
        //     while (moving) {
        //         // apply jet to the rock
        //         when (input[jetIndex % input.length]) {
        //             '<' -> rock.moveLeft(filledPositions)
        //             '>' -> rock.moveRight(filledPositions)
        //         }
        //         jetIndex++
        //
        //         // apply gravity
        //         if (rock.canMoveDown(filledPositions)) {
        //             rock.moveDown(filledPositions)
        //         } else {
        //             filledPositions.addAll(rock.positions)
        //             moving = false
        //         }
        //     }
        //
        // }
        //
        // return filledPositions.maxOf { it.second }
        return 0
    }

    class Rock(y: Long, type: RockType, x: Long = 3) {

        var positions: List<Pair<Long, Long>>

        init {
            positions = when (type) {
                MINUS -> listOf(x to y, x + 1 to y, x + 2 to y, x + 3 to y)
                PLUS -> listOf(x to y + 1, x + 1 to y, x + 1 to y + 1, x + 1 to y + 2, x + 2 to y + 1)
                MIRROR_L -> listOf(x to y, x + 1 to y, x + 2 to y, x + 2 to y + 1, x + 2 to y + 2)
                VERTICAL -> listOf(x to y, x to y + 1, x to y + 2, x to y + 3)
                SQUARE -> listOf(x to y, x to y + 1, x + 1 to y + 1, x + 1 to y)
            }
        }

        fun moveLeft(board: Set<Pair<Long, Long>>) {
            val hasNotReachedBorder = positions.all { it.first - 1 > 0 }
            val newPositions = positions.map { it.first - 1 to it.second }

            if (hasNotReachedBorder && newPositions.all { !board.contains(it) }) {
                // move Left
                positions = newPositions
            }
        }

        fun moveRight(board: Set<Pair<Long, Long>>) {
            val hasNotReachedBorder = positions.all { it.first + 1 < 8 }
            val newPositions = positions.map { it.first + 1 to it.second }

            if (hasNotReachedBorder && newPositions.all { !board.contains(it) }) {
                // move Right
                positions = newPositions
            }
        }

        fun canMoveDown(board: Set<Pair<Long, Long>>): Boolean {
            val hasNotReachedBorder = positions.all { it.second - 1 > 0 }
            val newPositionsAreFree = positions.map { it.first to it.second - 1 }.all { !board.contains(it) }

            return hasNotReachedBorder && newPositionsAreFree
        }

        fun moveDown(board: Set<Pair<Long, Long>>) {
            val hasNotReachedBorder = positions.all { it.second - 1 > 0 }
            val newPositions = positions.map { it.first to it.second - 1 }

            if (hasNotReachedBorder && newPositions.all { !board.contains(it) }) {
                // move Down
                positions = newPositions
            }
        }
    }

    enum class RockType {
        MINUS,
        PLUS,
        MIRROR_L,
        VERTICAL,
        SQUARE
    }

    fun part2(input: String, numberOfRocks: Long = 1_000_000_000_000L): Long {
        val filledPositions = mutableSetOf<Pair<Long, Long>>()
        var jetIndex = 0
        val rockPattern = listOf(MINUS, PLUS, MIRROR_L, VERTICAL, SQUARE)
        var rockIndex = 0

        for (i in 0 until numberOfRocks) {

            println("Processing rock $i")
            var moving = true
            // create rock
            val rock = Rock((filledPositions.maxOfOrNull { it.second } ?: 0) + 4, rockPattern[rockIndex % rockPattern.size])
            rockIndex++
            while (moving) {
                // apply jet to the rock
                when (input[jetIndex % input.length]) {
                    '<' -> rock.moveLeft(filledPositions)
                    '>' -> rock.moveRight(filledPositions)
                }
                jetIndex++

                // apply gravity
                if (rock.canMoveDown(filledPositions)) {
                    rock.moveDown(filledPositions)
                } else {
                    filledPositions.addAll(rock.positions)
                    moving = false
                }
            }
        }

        return filledPositions.maxOf { it.second }
    }

    fun readInput(): String = File("inputFiles/day17.txt").readLines().first()
}
