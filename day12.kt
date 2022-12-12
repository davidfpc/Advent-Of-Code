import Day12.part1
import Day12.part2
import Day12.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day12 {

    fun part1(input: Graph): Int {
        calculateShortestPathFromSource(input, input.startNode)
        return input.endNode.distance
    }

    /**
     * 🔨, love it! Just throw CPU power at the problem!
     */
    fun part2(input: Graph): Int = input.nodes.filter { it.name.startsWith('a') }.minOf {
        input.reset()
        calculateShortestPathFromSource(input, it)
        input.endNode.distance
    }

    fun readInput(): Graph {
        var input = File("inputFiles/day12.txt").readLines()
        val graph = Graph()

        val nodesMap = mutableMapOf<Pair<Int, Int>, Node>()
        (input.indices).forEach { y ->
            input[0].indices.forEach { x ->
                val node = Node("${input[y][x]}: $x-$y")
                // Check if start Node
                when (input[y][x]) {
                    'S' -> graph.startNode = node
                    'E' -> graph.startNode = node
                }
                graph.nodes.add(node)
                nodesMap[x to y] = node
            }
        }
        input = input.map { it.replace('S', 'a').replace('E', 'z') } // Set the start and end elevation

        (input.indices).forEach { y ->
            input[0].indices.forEach { x ->
                val currentElevation = input[y][x]
                val currentNode = nodesMap[x to y]!!

                val positions = listOf(-1 to 0, 0 to -1, 0 to 1, 1 to 0).map { (x + it.first to y + it.second) }
                    .filter { it.first >= 0 && it.first < input[0].length }
                    .filter { it.second >= 0 && it.second < input.size }

                positions.forEach {
                    if (input[it.second][it.first] - currentElevation <= 1) {
                        currentNode.addDestination(nodesMap[it.first to it.second]!!)
                    }
                }

            }
        }
        return graph
    }

    private fun calculateShortestPathFromSource(graph: Graph, source: Node): Graph {
        source.distance = 0
        val settledNodes = mutableSetOf<Node>()
        val unsettledNodes = mutableSetOf(source)
        while (unsettledNodes.size != 0) {
            val currentNode = unsettledNodes.minBy { it.distance }
            unsettledNodes.remove(currentNode)
            for ((adjacentNode, edgeWeight) in currentNode.adjacentNodes.entries) {
                if (!settledNodes.contains(adjacentNode)) {
                    val sourceDistance: Int = currentNode.distance
                    if ((sourceDistance + edgeWeight) < adjacentNode.distance) {
                        adjacentNode.distance = sourceDistance + edgeWeight
                        val shortestPath = currentNode.shortestPath.toMutableList()
                        shortestPath.add(currentNode)
                        adjacentNode.shortestPath = shortestPath
                    }
                    unsettledNodes.add(adjacentNode)
                }
            }
            settledNodes.add(currentNode)
        }
        return graph
    }

    class Graph {
        lateinit var startNode: Node
        lateinit var endNode: Node
        val nodes: MutableSet<Node> = mutableSetOf()
        fun reset() {
            nodes.forEach { it.reset() }
        }
    }

    class Node(val name: String) {
        var shortestPath = mutableListOf<Node>()
        var distance = Int.MAX_VALUE
        var adjacentNodes: MutableMap<Node, Int> = HashMap()
        fun addDestination(destination: Node) {
            adjacentNodes[destination] = 1
        }

        fun reset() {
            this.distance = Int.MAX_VALUE
            this.shortestPath = mutableListOf()
        }

        override fun toString(): String = "Node($name)"
    }
}
