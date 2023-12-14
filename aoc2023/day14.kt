package aoc2023

import utils.InputRetrieval

fun main() {
    Day14.execute()
}

private object Day14 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: Platform): Long = input.moveRocksNorth().calculateLoadNorthBeans()

    private val cache = mutableMapOf<Platform, Pair<Platform, Int>>()
    private var platformInformation: PlatformInformation = PlatformInformation(setOf(), Position(0, 0))
    private fun part2(input: Platform, numberOfTotalCycles: Int = 1_000_000_000): Long {
        var platform = input
        var cycle = 1
        while (cycle <= numberOfTotalCycles) {
            cache[platform]?.let { (_, matchedCycle) ->
                val cycleSize = cycle - matchedCycle
                println("Cycle of size $cycleSize detected. We are in cycle $cycle and it's the same as $matchedCycle")
                val remainingCycles = (numberOfTotalCycles - cycle) % cycleSize
                val targetCycle = matchedCycle + remainingCycles
                println("Return the load of cycle $targetCycle")
                return if (targetCycle == matchedCycle) {
                    platform.calculateLoadNorthBeans()
                } else {
                    val solution = cache.values.first { (_, cycle) -> cycle == targetCycle }
                    solution.first.calculateLoadNorthBeans()
                }
            }
            val result = platform
                .moveRocksNorth()
                .moveRocksWest()
                .moveRocksSouth()
                .moveRocksEast()
            cache[platform] = result to cycle
            platform = result
            cycle++
        }
        return platform.calculateLoadNorthBeans()
    }

    private fun readInput(): Platform = InputRetrieval.getFile(2023, 14).readLines().let { Platform.parse(it) }

    private data class PlatformInformation(val staticRocks: Set<Position>, val maxPos: Position)
    private data class Platform(val movingRocks: Set<Position>) {

        private fun moveRocks(input: Platform, edgeValidation: (Position) -> Boolean, mapping: (Position) -> Position): Platform {
            // This algorithm is quite slow, it can be optimized
            var rockMoved = true
            var platform = input
            while (rockMoved) {
                rockMoved = false
                val newMovingRocksPositions = platform.movingRocks.map {
                    val newPos = mapping.invoke(it)
                    if (edgeValidation.invoke(newPos) && !platformInformation.staticRocks.contains(newPos) && !platform.movingRocks.contains(newPos)) {
                        rockMoved = true
                        newPos
                    } else {
                        it
                    }
                }.toSet()
                platform = Platform(newMovingRocksPositions)
            }
            return platform
        }

        fun moveRocksNorth(): Platform = moveRocks(this, { pos -> pos.y > -1 }) {
            Position(x = it.x, y = it.y - 1)
        }

        fun moveRocksWest(): Platform = moveRocks(this, { pos -> pos.x > -1 }) {
            Position(x = it.x - 1, y = it.y)
        }

        fun moveRocksSouth(): Platform = moveRocks(this, { pos -> pos.y < platformInformation.maxPos.y }) {
            Position(x = it.x, y = it.y + 1)
        }

        fun moveRocksEast(): Platform = moveRocks(this, { pos -> pos.x < platformInformation.maxPos.x }) {
            Position(x = it.x + 1, y = it.y)
        }

        fun calculateLoadNorthBeans(): Long = movingRocks.sumOf { platformInformation.maxPos.y - it.y.toLong() }

        companion object {
            fun parse(input: List<String>): Platform {
                val movingRocks = mutableSetOf<Position>()
                val staticRocks = mutableSetOf<Position>()
                for (i in input.indices) {
                    for (z in input.first().indices) {
                        when {
                            input[i][z] == 'O' -> movingRocks.add(Position(z, i))
                            input[i][z] == '#' -> staticRocks.add(Position(z, i))
                        }
                    }
                }
                platformInformation = PlatformInformation(staticRocks, Position(input.first().length, input.size))
                return Platform(movingRocks)
            }
        }
    }

    private data class Position(val x: Int, val y: Int)
}
