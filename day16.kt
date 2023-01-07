import Day16.part1
import Day16.part2
import Day16.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day16 {

    fun part1(input: List<Valve>): Int {
        val startingPoint = input.first { it.id == "AA" }
        val cache = mutableMapOf<Triple<Valve, Int, Int>, Int>()
        return solvePart1(startingPoint, 0, 0, mutableListOf(), input, 30, cache)
    }

    fun part2(input: List<Valve>): Int {
        val startingPoint = input.first { it.id == "AA" }

        val validValves = input.filter { it.flowRate != 0 }.toSet()
        val powerSet = validValves.powerSet()
        val combinations = mutableSetOf<Set<Valve>>()
        for (element in powerSet) {
            if (validValves.minus(element) !in combinations) {
                combinations += element
            }
        }

        return combinations.withIndex().map { (index, valves) -> index to valves }.parallelStream().map { (index, humanValves) ->
            println("Processing $index out of ${combinations.size}") // This is just so we can see progress, because this takes like 2hrs, even with the parallelism
            val elephantValves = validValves.minus(humanValves)
            val humanScore = solvePart1(startingPoint, 0, 0, elephantValves.toMutableList(), input, 26, mutableMapOf())
            val elephantScore = solvePart1(startingPoint, 0, 0, humanValves.toMutableList(), input, 26, mutableMapOf())
            humanScore + elephantScore
        }.max(Integer::compare).get()
    }

    private fun <T> Collection<T>.powerSet(): Set<Set<T>> = when {
        isEmpty() -> setOf(emptySet())
        else -> this.drop(1).powerSet().let { value -> value + value.map { it + this.first() } }
    }

    private fun solvePart1(
        currentValve: Valve,
        minute: Int,
        releasedPressure: Int,
        openedValves: MutableList<Valve>,
        input: List<Valve>,
        finalMinute: Int,
        cache: MutableMap<Triple<Valve, Int, Int>, Int>
    ): Int {
        if (minute >= finalMinute) {
            return releasedPressure
        }

        val cacheId = Triple(currentValve, minute, releasedPressure)
        if (cache.containsKey(cacheId)) {
            return cache[cacheId]!!
        }

        var newReleasedPressure = releasedPressure
        var currentMinute = minute
        var childrenMaxValueWithoutOpeningTheValve = 0
        if (currentValve.flowRate != 0 && !openedValves.contains(currentValve)) {

            // Check children without opening the valve
            childrenMaxValueWithoutOpeningTheValve = currentValve.targetValves.maxOf { valveId ->
                solvePart1(input.first { it.id == valveId }, currentMinute + 1, newReleasedPressure, openedValves.toMutableList(), input, finalMinute, cache)
            }
            // Open Valve
            currentMinute++ // It takes 1 minute to open the valve
            newReleasedPressure += currentValve.flowRate * (finalMinute - currentMinute)
            openedValves.add(currentValve)
        }

        // Check to children nodes after opening the valve
        val result = currentValve.targetValves
            .map { valveId -> solvePart1(input.first { it.id == valveId }, currentMinute + 1, newReleasedPressure, openedValves.toMutableList(), input, finalMinute, cache) }
            .plus(childrenMaxValueWithoutOpeningTheValve)
            .max()

        cache[cacheId] = result
        return result
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
