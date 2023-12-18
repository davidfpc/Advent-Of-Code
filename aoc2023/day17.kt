package aoc2023

import utils.InputRetrieval
import java.util.TreeSet

fun main() {
    Day17.execute()
}

private object Day17 {

    fun execute() {
        INPUT = readInput()
        println("Part 1: ${part1()}")
        println("Part 2: ${part2()}")
    }

    private fun part1(): Int {
        dijkstra { it.neighbours() }
        return endNodes().minOf { it.dist }
    }

    private fun part2(): Int {
        dijkstra { it.ultraCrucibleNeighbours() }
        return endNodes(true).minOf { it.dist }
    }

    private fun readInput(): List<List<Int>> = InputRetrieval.getFile(2023, 17).readLines().map {
        it.map { c -> c.digitToInt() }
    }

    private fun endNodes(ultraCrucible: Boolean = false): List<Vertex> {
        val maxX = INPUT.first().size - 1
        val maxY = INPUT.size - 1
        return if (ultraCrucible) {
            NODES.values.filter { it.x == maxX && it.y == maxY && it.lastDirectionChange > 2 }
        } else {
            NODES.values.filter { it.x == maxX && it.y == maxY }
        }
    }

    lateinit var INPUT: List<List<Int>>

    private val NODES = mutableMapOf<Triple<Pair<Int, Int>, Direction, Int>, Vertex>()
    private fun getNode(pos: Pair<Int, Int>, dir: Direction, lastChange: Int): Vertex? {
        val (x, y) = pos
        val maxX = INPUT.first().size
        val maxY = INPUT.size
        if (x < 0 || y < 0 || x >= maxX || y >= maxY) {
            return null
        }
        return NODES.getOrPut(Triple(pos, dir, lastChange)) {
            Vertex(x = x, y = y, direction = dir, cost = INPUT[y][x], lastDirectionChange = lastChange)
        }
    }

    /** Implementation of dijkstra's algorithm, based on https://rosettacode.org/wiki/Dijkstra%27s_algorithm#Kotlin */
    private fun dijkstra(debug: Boolean = false, neighboursCalculation: (Vertex) -> Set<Vertex>) {
        val maxX = INPUT.first().size - 1
        val maxY = INPUT.size - 1
        NODES.clear()
        val q = TreeSet<Vertex>()
        val startingNodes = listOf(
            getNode(0 to 0, Direction.RIGHT, 0)!!,
            getNode(0 to 0, Direction.DOWN, 0)!!
        )
        startingNodes.forEach {
            it.dist = 0
            q.add(it)
        }
        while (!q.isEmpty()) {
            // Vertex with the shortest distance (first iteration will return source)
            val u = q.pollFirst()!!
            if (debug && u.x == maxX && u.y == maxY) {
                print("Found the target Node: ")
                u.printPath()
                println()
            }
            // If distance is infinite we can ignore 'u' (and any other remaining vertices), since they are unreachable
            if (u.dist == Int.MAX_VALUE) break

            // Look at distances to each neighbour
            for (v in neighboursCalculation.invoke(u)) {
                val alternateDist = u.dist + v.cost
                if (alternateDist < v.dist) { // Shorter path to neighbour found
                    q.remove(v)
                    v.dist = alternateDist
                    v.previous = u
                    q.add(v)
                }
            }
        }
    }

    private enum class Direction { UP, DOWN, LEFT, RIGHT }
    private data class Vertex(
        val x: Int,
        val y: Int,
        val direction: Direction,
        val cost: Int,
        var dist: Int = Int.MAX_VALUE,
        val lastDirectionChange: Int,
        var previous: Vertex? = null,
    ) : Comparable<Vertex> {

        fun neighbours(): Set<Vertex> {
            val neighbours = mutableSetOf<Vertex>()
            when (direction) {
                Direction.UP, Direction.DOWN -> {
                    // Go forward
                    if (lastDirectionChange < 2) {
                        if (direction == Direction.UP) {
                            getNode(x to y - 1, Direction.UP, lastDirectionChange + 1)?.let { neighbours.add(it) }
                        } else {
                            getNode(x to y + 1, Direction.DOWN, lastDirectionChange + 1)?.let { neighbours.add(it) }
                        }
                    }
                    // Go left
                    getNode(x - 1 to y, Direction.LEFT, 0)?.let { neighbours.add(it) }
                    // Go Right
                    getNode(x + 1 to y, Direction.RIGHT, 0)?.let { neighbours.add(it) }
                }

                Direction.LEFT, Direction.RIGHT -> {
                    // Go forward
                    if (lastDirectionChange < 2) {
                        if (direction == Direction.LEFT) {
                            getNode(x - 1 to y, Direction.LEFT, lastDirectionChange + 1)?.let { neighbours.add(it) }
                        } else {
                            getNode(x + 1 to y, Direction.RIGHT, lastDirectionChange + 1)?.let { neighbours.add(it) }
                        }
                    }
                    // Go up
                    getNode(x to y - 1, Direction.UP, 0)?.let { neighbours.add(it) }
                    // Go down
                    getNode(x to y + 1, Direction.DOWN, 0)?.let { neighbours.add(it) }
                }
            }
            return neighbours
        }

        fun ultraCrucibleNeighbours(): Set<Vertex> {
            val neighbours = mutableSetOf<Vertex>()
            when (direction) {
                Direction.UP, Direction.DOWN -> {
                    // Go forward
                    if (lastDirectionChange < 9) {
                        if (direction == Direction.UP) {
                            getNode(x to y - 1, Direction.UP, lastDirectionChange + 1)?.let { neighbours.add(it) }
                        } else {
                            getNode(x to y + 1, Direction.DOWN, lastDirectionChange + 1)?.let { neighbours.add(it) }
                        }
                    }
                    if (lastDirectionChange > 2) {
                        // Go left
                        getNode(x - 1 to y, Direction.LEFT, 0)?.let { neighbours.add(it) }
                        // Go Right
                        getNode(x + 1 to y, Direction.RIGHT, 0)?.let { neighbours.add(it) }
                    }
                }

                Direction.LEFT, Direction.RIGHT -> {
                    // Go forward
                    if (lastDirectionChange < 9) {
                        if (direction == Direction.LEFT) {
                            getNode(x - 1 to y, Direction.LEFT, lastDirectionChange + 1)?.let { neighbours.add(it) }
                        } else {
                            getNode(x + 1 to y, Direction.RIGHT, lastDirectionChange + 1)?.let { neighbours.add(it) }
                        }
                    }
                    if (lastDirectionChange > 2) {
                        // Go up
                        getNode(x to y - 1, Direction.UP, 0)?.let { neighbours.add(it) }
                        // Go down
                        getNode(x to y + 1, Direction.DOWN, 0)?.let { neighbours.add(it) }
                    }
                }
            }
            return neighbours
        }

        fun printPath() {
            val name = "[${x},${y}]"
            when (previous) {
                this -> print(name)

                null -> print("$name(unreached)")

                else -> {
                    previous!!.printPath()
                    print(" -> $name(${dist})")
                }
            }
        }

        override fun compareTo(other: Vertex): Int {
            if (dist == other.dist) {
                return "${x}-${y}-${direction}-${lastDirectionChange}".compareTo("${other.x}-${other.y}-${other.direction}-${other.lastDirectionChange}")
            }
            return dist.compareTo(other.dist)
        }
    }
}
