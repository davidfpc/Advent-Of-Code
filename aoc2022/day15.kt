package aoc2022

import utils.InputRetrieval
import kotlin.math.abs

fun main() {
    Day15.execute()
}

object Day15 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<Sensor>, row: Int = 2_000_000): Int {
        val beaconPositions = input.map { it.closestBeacon.x to it.closestBeacon.y }.toSet()
        val coveredPositions = mutableSetOf<Pair<Int, Int>>()
        input.forEach {
            val coveredManhattanDistance = it.coveredManhattanDistance()
            val canReachTargetRow = it.y - coveredManhattanDistance <= row && row <= it.y + coveredManhattanDistance
            var x = 0
            while (canReachTargetRow) {
                // Add all covered positions in the target Row to a set
                if (it.manhattanDistance(it.x + x, row) > coveredManhattanDistance) {
                    break
                }
                coveredPositions.add(it.x + x to row)
                coveredPositions.add(it.x - x to row)
                x++
            }
        }
        coveredPositions.removeAll(beaconPositions)
        return coveredPositions.filter { it.second == row }.size
    }

    private fun part2(input: List<Sensor>, maxValue: Int = 4_000_000): Long { // 4_000_000

        input.forEach { sensor ->
            // Because there is just a single point, get the positions just outside its reach and check those
            val coveredManhattanDistance = sensor.coveredManhattanDistance() + 1

            var x = coveredManhattanDistance
            var y = 0
            while (x >= 0) {
                val pointsToCheck = listOf(sensor.x + x to sensor.y + y, sensor.x - x to sensor.y + y, sensor.x + x to sensor.y - y, sensor.x - x to sensor.y - y)

                pointsToCheck.filter { it.first in 0..maxValue && it.second in 0..maxValue }
                    .forEach { pointToCheck ->
                        var overlap = false
                        for (sensorToValidate in input) {
                            if (sensorToValidate.manhattanDistance(pointToCheck.first, pointToCheck.second) <= sensorToValidate.coveredManhattanDistance()) {
                                overlap = true
                                break
                            }
                        }
                        if (!overlap) {
                            println("Found point $pointToCheck!")
                            return (pointToCheck.first * 4_000_000L) + pointToCheck.second
                        }
                    }

                // Check next point
                x--
                y++
            }
        }
        return -1
    }

    private fun readInput(): List<Sensor> = InputRetrieval.getFile(2022, 15).readLines().map { input ->
        //Sensor at x=2, y=18: closest beacon is at x=-2, y=15
        val (sensorInput, beaconInput) = input.split(": closest beacon is at ")

        val beacon = beaconInput.split(", ").map { it.removePrefix("x=").removePrefix("y=").toInt() }.let {
            val (beaconX, beaconY) = it
            Beacon(beaconX, beaconY)
        }
        sensorInput.split(", ").map { it.removePrefix("Sensor at x=").removePrefix("y=").toInt() }.let {
            val (sensorX, sensorY) = it
            Sensor(sensorX, sensorY, beacon)
        }
    }

    data class Sensor(val x: Int, val y: Int, val closestBeacon: Beacon) {

        fun coveredManhattanDistance(): Int = this.manhattanDistance(closestBeacon.x, closestBeacon.y)
    }

    data class Beacon(val x: Int, val y: Int)

    private fun Sensor.manhattanDistance(toX: Int, toY: Int): Int = abs(this.x - toX) + abs(this.y - toY)

    fun Set<Pair<Int, Int>>.print() {
        val minX = this.minOf { it.first }
        val maxX = this.maxOf { it.first }
        val minY = this.minOf { it.second }
        val maxY = this.maxOf { it.second }

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                if (this.contains(x to y)) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }

        println()
    }
}
