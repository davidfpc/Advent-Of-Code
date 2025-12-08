package aoc2025

import utils.InputRetrieval
import kotlin.math.sqrt

fun main() {
    Day8.execute()
}

private object Day8 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: Input, numberOfConnections: Int = 1000): Long {
        val (circuits, _) = input.connectJunctionBoxes { _, connectionIndex -> connectionIndex == numberOfConnections - 1 }
        return circuits.map { it.size }.sortedDescending().take(3).reduce { acc, l -> acc * l }.toLong()
    }

    private fun part2(input: Input): Long {
        val (_, finalConnectionIndex) = input.connectJunctionBoxes { circuits, _ -> circuits.size == 1 }
        val finalConnection = input.connections[finalConnectionIndex]
        return finalConnection.second.x * finalConnection.third.x
    }

    private fun Input.connectJunctionBoxes(exitCondition: (circuits: List<List<JunctionBoxesPosition>>, connectionIndex: Int) -> Boolean): Pair<List<List<JunctionBoxesPosition>>, Int> {
        // Connect Junction Boxes
        val circuits: MutableList<List<JunctionBoxesPosition>> = this.positions.map { listOf(it) }.toMutableList()
        var connectionIndex = -1
        while (!exitCondition(circuits, connectionIndex)) {
            connectionIndex++
            val (_, junction1, junction2) = this.connections[connectionIndex]
            // Get the circuits containing each junction boxes
            val circuit1 = circuits.first { it.contains(junction1) }
            val circuit2 = circuits.first { it.contains(junction2) }
            // If it's the same circuit, ignore it!
            if (circuit1 != circuit2) {
                // Merge circuits
                circuits.remove(circuit1)
                circuits.remove(circuit2)
                circuits.add(circuit1 + circuit2)
            }
        }
        return circuits to connectionIndex
    }

    private fun readInput(): Input {
        val positions = InputRetrieval.getFile(2025, 8).readLines().map { position ->
            val (x, y, z) = position.split(',').map { it.toLong() }
            JunctionBoxesPosition(x = x, y = y, z = z)
        }
        val distances = positions.flatMapIndexed { index, position ->
            positions.drop(index + 1).map { otherPosition ->
                Triple(position.calculateDistance(otherPosition), position, otherPosition)
            }
        }.sortedBy { it.first }
        return Input(positions = positions, connections = distances)
    }

    private fun JunctionBoxesPosition.calculateDistance(other: JunctionBoxesPosition): Double {
        // Calculate Distance
        val dx = this.x - other.x
        val dy = this.y - other.y
        val dz = this.z - other.z
        return sqrt((dx * dx + dy * dy + dz * dz).toDouble())
    }

    private data class JunctionBoxesPosition(val x: Long, val y: Long, val z: Long)

    private data class Input(val positions: List<JunctionBoxesPosition>, val connections: List<Triple<Double, JunctionBoxesPosition, JunctionBoxesPosition>>)
}
