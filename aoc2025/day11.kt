package aoc2025

import utils.InputRetrieval

fun main() {
    Day11.execute()
}

private object Day11 {
    const val START_PART_1 = "you"
    const val START_PART_2 = "svr"
    val MANDATORY_VISITED_NODES = setOf("dac", "fft")
    const val END = "out"

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: Map<String, GraphNode>): Long = input.walkGraph(
        currentNode = START_PART_1,
        visitedNodes = setOf(START_PART_1)
    )

    private fun part2(input: Map<String, GraphNode>): Long {
        return 0L
    }

    private fun Map<String, GraphNode>.walkGraph(currentNode: String, visitedNodes: Set<String>): Long {
        if (currentNode == END) return 1L
        return this[currentNode]?.linkedDevices
            ?.filter { it !in visitedNodes } // Prevent Loops
            ?.sumOf { walkGraph(it, visitedNodes + it) } ?: 0L
    }

    private fun readInput(): Map<String, GraphNode> {
        val nodes = InputRetrieval.getFile(2025, 11).readLines().map {
            val (id, linkedDevicesRaw) = Regex("""(.*): (.*)""").matchEntire(it)!!.destructured
            GraphNode(id, linkedDevicesRaw.split(" "))
        }
        return nodes.associateBy { it.id }
    }

    data class GraphNode(val id: String, val linkedDevices: List<String>)
}
