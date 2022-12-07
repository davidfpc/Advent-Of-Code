import Day7.createDirTree
import Day7.part1
import Day7.part2
import Day7.readInput
import java.io.File
import java.lang.Integer.min

fun main() {
    val input = readInput()
    val parsedInput = createDirTree(input)
    println("Part 1: ${part1(parsedInput)}")
    println("Part 2: ${part2(parsedInput)}")
}

object Day7 {

    fun part1(input: Dir): Int = calcCumulativeDirSize(input)

    fun part2(input: Dir): Int = calcMinDirSizeAbove(input, (30000000 - (70000000 - input.size)))

    fun createDirTree(input: List<String>): Dir {
        val topDir = Dir("/")
        var currentDir = topDir

        for (i in input) {
            if (i.startsWith("$ cd ")) {
                val dirName = i.removePrefix("$ cd ")
                currentDir = when (dirName) {
                    ".." -> currentDir.parentDir!! // Go up
                    "/" -> topDir // Go root
                    else -> currentDir.nodes.filterIsInstance<Dir>().first { it.name == dirName } // Go to child dir
                }
            } else if (!i.startsWith('$')) {
                // We only have a command output, so we can just skip the '$ ls' entry and just assume anything that doesn't start with '$' is the list output of the current dir
                val (size, fileName) = i.split(' ')
                val item = when (size) {
                    "dir" -> currentDir.nodes.filterIsInstance<Dir>().firstOrNull { it.name == fileName } ?: Dir(fileName, parentDir = currentDir)
                    else -> currentDir.nodes.filterIsInstance<File>().firstOrNull { it.name == fileName } ?: File(fileName, size.toInt())
                }
                currentDir.nodes.add(item)
            }
        }
        return topDir
    }

    /**
     * This solution is not perfect because it iterates the tree multiple times go get the dir sizes, but it is good enough for the input size
     */
    private fun calcCumulativeDirSize(node: Node, maxSize: Int = 100000): Int = when (node) {
        is File -> 0
        is Dir -> {
            val nodeSize = node.size
            val childrenCumulativeSize = node.nodes.sumOf { calcCumulativeDirSize(it) }
            if (nodeSize > maxSize) {
                childrenCumulativeSize
            } else {
                nodeSize + childrenCumulativeSize
            }
        }
    }

    /**
     * This solution is not perfect because it iterates the tree multiple times go get the dir sizes, but it is good enough for the input size
     */
    private fun calcMinDirSizeAbove(node: Node, minSize: Int): Int = when (node) {
        is File -> 0
        is Dir -> {
            val itemSize = node.size
            if (itemSize >= minSize) {
                val childSizes = node.nodes.map { calcMinDirSizeAbove(it, minSize) }.filter { it >= minSize }.minOrNull() ?: Int.MAX_VALUE
                min(itemSize, childSizes)
            } else 0
        }
    }

    fun readInput(): List<String> = File("inputFiles/day7.txt").readLines()

    sealed interface Node {
        val name: String
        val size: Int
    }

    data class File(override val name: String, override val size: Int) : Node

    data class Dir(override val name: String, val nodes: MutableSet<Node> = mutableSetOf(), val parentDir: Dir? = null) : Node {

        override val size: Int
            get() = nodes.sumOf { it.size }

        override fun hashCode() = name.hashCode()
        override fun toString() = name

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return name == (other as Dir).name
        }
    }
}
