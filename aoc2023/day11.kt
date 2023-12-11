package aoc2023

import utils.InputRetrieval
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day11.execute()
}

private object Day11 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<String>): Long = input.sumDifferencesBetweenTheGalaxies(2)

    private fun part2(input: List<String>): Long = input.sumDifferencesBetweenTheGalaxies(1_000_000)

    private fun readInput(): List<String> = InputRetrieval.getFile(2023, 11).readLines()

    private fun List<String>.sumDifferencesBetweenTheGalaxies(universeExpansionRate: Int = 2): Long {
        // Get Galaxy positions
        val galaxies = mutableListOf<GalaxyPosition>()
        this.forEachIndexed { y: Int, row: String ->
            row.forEachIndexed { x, c ->
                if (c == '#') {
                    galaxies.add(GalaxyPosition(x + 1L, y + 1L))
                }
            }
        }

        // Expand universe
        val universe = galaxies.expandUniverse(universeExpansionRate).toList()

        // Calculate distance between galaxies
        val pairs = universe.flatMapIndexed { index, s -> universe.slice(index + 1 until universe.size).map { s to it } }
        return pairs.sumOf {
            val (a, b) = it
            max(a.x, b.x) - min(a.x, b.x) + max(a.y, b.y) - min(a.y, b.y)
        }
    }

    private fun List<GalaxyPosition>.expandUniverse(universeExpansionRate: Int): List<GalaxyPosition> {
        val expandedUniverseTransformation = this.associateWith { it }.toMutableMap()

        val xPositions = this.map { it.x }.toSet()
        val yPositions = this.map { it.y }.toSet()

        val xSpacesToExpand = (xPositions.min()..<xPositions.max()).filter { !xPositions.contains(it) }
        val ySpacesToExpand = (yPositions.min()..<yPositions.max()).filter { !yPositions.contains(it) }

        xSpacesToExpand.forEach {
            expandedUniverseTransformation.keys.forEach { galaxy ->
                if (galaxy.x > it) {
                    val tmp = expandedUniverseTransformation[galaxy]!!
                    expandedUniverseTransformation[galaxy] = GalaxyPosition(tmp.x + (universeExpansionRate - 1), tmp.y)
                }
            }
        }
        ySpacesToExpand.forEach {
            expandedUniverseTransformation.keys.forEach { galaxy ->
                if (galaxy.y > it) {
                    val tmp = expandedUniverseTransformation[galaxy]!!
                    expandedUniverseTransformation[galaxy] = GalaxyPosition(tmp.x, tmp.y + (universeExpansionRate - 1))
                }
            }
        }
        return expandedUniverseTransformation.values.toList()
    }

    private data class GalaxyPosition(val x: Long, val y: Long)
}
