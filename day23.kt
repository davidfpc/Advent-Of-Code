import Day23.part1
import Day23.part2
import Day23.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day23 {
    fun part1(input: List<Point>): Int {

        var board = input
        val checkOrder = mutableListOf(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)
        repeat(10) { _ ->
            board = processElvesMoves(board, checkOrder)
            // 3 - Update direction order: move the first element to the end
            checkOrder.removeFirst().apply { checkOrder.add(this) }
        }
        return board.calculateResult()
    }

    /**
     * Optimizations? Don't know that word! Let's turn my laptop into a heater!
     */
    fun part2(input: List<Point>): Int {

        var round = 1
        var move = true
        var board = input
        val checkOrder = mutableListOf(Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST)
        while (move) {
            val newBoard = processElvesMoves(board, checkOrder).toSet()
            // 3 - Update direction order: move the first element to the end
            checkOrder.removeFirst().apply { checkOrder.add(this) }

            if (board.intersect(newBoard.toSet()).size == board.size) {
                // No changes
                move = false
            } else {
                board = newBoard.toList()
                round++
            }
        }

        return round
    }

    private fun processElvesMoves(board: List<Point>, checkOrder: List<Direction>): List<Point> {
        val newPositions = mutableListOf<Pair<Point, Point>>()
        // 1- Propose next moves
        board.forEach { elf ->
            var newPos = elf

            if (checkOrder.flatMap { it.deltaPositions }.map { Point(elf.x + it.first, elf.y + it.second) }.any { board.contains(it) }) {
                for (i in checkOrder) {
                    val freeDirection = !i.deltaPositions.map { Point(elf.x + it.first, elf.y + it.second) }.any { board.contains(it) }
                    if (freeDirection) {
                        val targetPosition = Point(elf.x + i.targetDelta.first, elf.y + i.targetDelta.second)
                        newPos = targetPosition
                        break // No need to check other direction
                    }
                }
            }
            newPositions.add(newPos to elf)
        }
        // 2- Move, and if conflict arise, block the move for those elves
        return if (newPositions.map { it.first }.toSet().size != newPositions.size) {
            val newBoard = mutableListOf<Point>()
            // Houston, we have a conflict!
            newPositions.groupBy { it.first }.forEach {
                if (it.value.size == 1) {
                    newBoard.add(it.key)
                } else {
                    it.value.forEach { pos -> newBoard.add(pos.second) }
                }
            }
            newBoard
        } else {
            newPositions.map { it.first }
        }
    }

    private fun List<Point>.printBoard() {
        val minX = this.minOf { it.x }
        val maxX = this.maxOf { it.x } + 1
        val minY = this.minOf { it.y }
        val maxY = this.maxOf { it.y } + 1
        for (y in minY until maxY) {
            for (x in minX until maxX) {
                if (this.contains(Point(x, y))) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    enum class Direction(val deltaPositions: List<Pair<Int, Int>>, val targetDelta: Pair<Int, Int>) {
        NORTH(listOf(-1 to -1, 0 to -1, 1 to -1), 0 to -1),
        SOUTH(listOf(-1 to 1, 0 to 1, 1 to 1), 0 to 1),
        WEST(listOf(-1 to -1, -1 to 0, -1 to 1), -1 to 0),
        EAST(listOf(1 to -1, 1 to 0, 1 to 1), 1 to 0);
    }

    private fun List<Point>.calculateResult(): Int {
        val minX = this.minOf { it.x }
        val maxX = this.maxOf { it.x } + 1
        val minY = this.minOf { it.y }
        val maxY = this.maxOf { it.y } + 1

        val width = maxX - minX
        val height = maxY - minY
        return (width * height) - this.size
    }

    data class Point(val x: Int, val y: Int)

    fun readInput(): List<Point> {

        val elfList = mutableListOf<Point>()
        File("inputFiles/day23.txt").readLines().forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c == '#') {
                    elfList.add(Point(x, y))
                }
            }
        }
        return elfList
    }
}
