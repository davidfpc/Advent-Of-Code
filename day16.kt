import Day16.part1
import Day16.part2
import Day16.readInput
import java.io.File
import java.lang.Integer.max

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    // println("Part 2: ${part2(input)}") -> WIP
}

object Day16 {

    fun part1(input: List<Valve>): Int {
        val startingPoint = input.first { it.id == "AA" }
        return solvePart1(startingPoint, 0, 0, mutableListOf(), input)
    }

    fun part2(input: List<Valve>): Int {
        val startingPoint = input.first { it.id == "AA" }
        return solvePart2(listOf(startingPoint, startingPoint), 0, 0, listOf(), input)
    }

    private val MAX_PRESSURE_PER_MINUTE = mutableMapOf<Triple<Valve, Int, Int>, Int>()
    private fun solvePart1(currentValve: Valve, minute: Int, releasedPressure: Int, openedValves: MutableList<Valve>, input: List<Valve>, finalMinute: Int = 30): Int {
        if (minute >= finalMinute) {
            return releasedPressure
        }

        if (MAX_PRESSURE_PER_MINUTE.containsKey(Triple(currentValve, minute, releasedPressure))) {
            return MAX_PRESSURE_PER_MINUTE[Triple(currentValve, minute, releasedPressure)]!!
        }

        var newReleasedPressure = releasedPressure
        var currentMinute = minute
        var childrenMaxValueWithoutOpeningTheValve = 0
        if (currentValve.flowRate != 0 && !openedValves.contains(currentValve)) {

            // Check children without opening the valve
            childrenMaxValueWithoutOpeningTheValve = currentValve.targetValves.maxOf { valveId ->
                solvePart1(input.first { it.id == valveId }, currentMinute + 1, newReleasedPressure, openedValves.toMutableList(), input)
            }
            // Open Valve
            currentMinute++ // It takes 1 minute to open the valve
            newReleasedPressure += currentValve.flowRate * (finalMinute - currentMinute)
            openedValves.add(currentValve)
        }

        // Check to children nodes after opening the valve
        val result = max(currentValve.targetValves.maxOf { valveId ->
            solvePart1(input.first { it.id == valveId }, currentMinute + 1, newReleasedPressure, openedValves.toMutableList(), input)
        }, childrenMaxValueWithoutOpeningTheValve)
        MAX_PRESSURE_PER_MINUTE[Triple(currentValve, minute, releasedPressure)] = result
        return result
    }

    private val MAX_PRESSURE_WITH_ELEPHANT_PER_MINUTE = mutableMapOf<Triple<List<Valve>, Int, Int>, Int>()
    private fun solvePart2(
        currentValves: List<Valve>,
        minute: Int,
        releasedPressure: Int,
        openedValves: List<Valve>,
        input: List<Valve>,
        finalMinute: Int = 26,
        numberValidValves: Int = input.filter { it.flowRate != 0 }.size
    ): Int {
        if (minute >= finalMinute || numberValidValves == openedValves.size) {
            // end if time reached or if all valves are opened
            return releasedPressure
        }
        val memoizationKey = currentValves.sortedBy { it.id }
        if (MAX_PRESSURE_WITH_ELEPHANT_PER_MINUTE.containsKey(Triple(memoizationKey, minute, releasedPressure))) {
            return MAX_PRESSURE_WITH_ELEPHANT_PER_MINUTE[Triple(memoizationKey, minute, releasedPressure)]!!
        }

        val ourValve = currentValves.first()
        val elephantValve = currentValves.last()

        val childrenMaxValue = mutableListOf<Int>()

        // We need to test the following scenarios:
        // We both open
        // We open, but the elephant moves
        // We move, but the elephant opens
        // We both move

        // We both open
        if (ourValve.flowRate != 0 && !openedValves.contains(ourValve)
            && elephantValve.flowRate != 0 && !openedValves.contains(elephantValve)
            && ourValve != elephantValve
        ) {
            // we want to try and open our valve and the elephant wants to try and open their valve
            val newReleasedPressure = releasedPressure + (ourValve.flowRate * (finalMinute - (minute + 1))) + (elephantValve.flowRate * (finalMinute - (minute + 1)))
            val newOpenedValves = openedValves.toMutableList().apply { addAll(listOf(ourValve, elephantValve)) }

            for (ourTargetValveId in ourValve.targetValves) {
                val ourTargetValve = input.first { it.id == ourTargetValveId }
                for (elephantTargetValveId in elephantValve.targetValves) {
                    val elephantTargetValve = input.first { it.id == elephantTargetValveId }
                    childrenMaxValue.add(solvePart2(listOf(ourTargetValve, elephantTargetValve), minute + 2, newReleasedPressure, newOpenedValves, input))
                }
            }
        }

        // We open, but the elephant moves
        if (ourValve.flowRate != 0 && !openedValves.contains(ourValve)) {
            val newReleasedPressure = releasedPressure + (ourValve.flowRate * (finalMinute - (minute + 1)))
            val newOpenedValves = openedValves.toMutableList().apply { add(ourValve) }
            for (elephantTargetValveId in elephantValve.targetValves) {
                val elephantTargetValve = input.first { it.id == elephantTargetValveId }
                childrenMaxValue.add(solvePart2(listOf(ourValve, elephantTargetValve), minute + 1, newReleasedPressure, newOpenedValves, input))
            }
        }

        // We move, but the elephant opens
        if (elephantValve.flowRate != 0 && !openedValves.contains(elephantValve)) {
            val newReleasedPressure = releasedPressure + (elephantValve.flowRate * (finalMinute - (minute + 1)))
            val newOpenedValves = openedValves.toMutableList().apply { add(elephantValve) }
            for (ourTargetValveId in ourValve.targetValves) {
                val ourTargetValve = input.first { it.id == ourTargetValveId }
                childrenMaxValue.add(solvePart2(listOf(ourTargetValve, elephantValve), minute + 1, newReleasedPressure, newOpenedValves, input))
            }
        }

        // We both move
        for (ourTargetValveId in ourValve.targetValves) {
            val ourTargetValve = input.first { it.id == ourTargetValveId }
            for (elephantTargetValveId in elephantValve.targetValves) {
                val elephantTargetValve = input.first { it.id == elephantTargetValveId }
                childrenMaxValue.add(solvePart2(listOf(ourTargetValve, elephantTargetValve), minute + 1, releasedPressure, openedValves, input))
            }
        }

        // Save the result of this processing
        MAX_PRESSURE_WITH_ELEPHANT_PER_MINUTE[Triple(memoizationKey, minute, releasedPressure)] = childrenMaxValue.max()
        return MAX_PRESSURE_WITH_ELEPHANT_PER_MINUTE[Triple(memoizationKey, minute, releasedPressure)]!!
    }

    fun readInput(): List<Valve> = File("inputFiles/day16.txt").readLines().map {
        var input = it.removePrefix("Valve ")
        val id = input.take(2)
        input = input.drop(2).removePrefix(" has flow rate=")
        val flowRate = input.takeWhile { char -> char != ';' }
        input = input.drop(flowRate.length).removePrefix("; tunnels lead to valves ").removePrefix("; tunnel leads to valve ")
        val valves = input.split(", ").toSet()

        Valve(id, flowRate.toInt(), valves)
    }

    data class Valve(val id: String, val flowRate: Int, val targetValves: Set<String>)
}
