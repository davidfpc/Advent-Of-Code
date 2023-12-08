package aoc2023

import utils.InputRetrieval
import utils.Math

fun main() {
    Day8.execute()
}

private object Day8 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: DesertMap): Long = input.walkMap(STARTING_NODE) { it == FINAL_NODE }

    private fun part2(input: DesertMap): Long {
        // At first, I thought we would need to calculate the loop size, and not just the first occurrence. But after testing they are the same, so...
        val firstZPosition = input.nodes.keys
            .filter { it.endsWith(STARTING_NODE_PLACE) }
            .map { input.walkMap(it) { node -> node.endsWith(FINAL_NODE_PLACE) } }
        return firstZPosition.reduce { a, b -> Math.findLCM(a, b) }
    }

    private fun readInput(): DesertMap {
        val input = InputRetrieval.getFile(2023, 8).readLines()
        return DesertMap.parse(input)
    }

    private const val STARTING_NODE = "AAA"
    private const val FINAL_NODE = "ZZZ"
    private const val STARTING_NODE_PLACE = 'A'
    private const val FINAL_NODE_PLACE = 'Z'

    private data class DesertMap(val instructions: String, val nodes: Map<String, Pair<String, String>>) {
        fun walkMap(initialNode: String, reachedEnd: (String) -> Boolean): Long {
            var i = 0L
            var node = initialNode
            while (!reachedEnd.invoke(node)) {
                node = when (this.instructions[(i % this.instructions.length).toInt()]) {
                    'L' -> this.nodes[node]!!.first
                    else -> this.nodes[node]!!.second
                }
                i++
            }

            return i
        }

        companion object {
            fun parse(input: List<String>): DesertMap {
                val instructions = input.first()
                val nodes = input.drop(2).associate {
                    val (key, paths) = it.split('=')
                    val (left, right) = paths.replace('(', ' ').replace(')', ' ').trim().split(',')
                    key.trim() to (left to right.trim())
                }
                return DesertMap(instructions, nodes)
            }
        }
    }
}
