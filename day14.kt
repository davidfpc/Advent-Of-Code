import Day14.part1
import Day14.part2
import Day14.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day14 {

    fun part1(input: Set<Pair<Int, Int>>): Int {
        val map = input.toMutableSet()
        var iteration = 0
        val minX = input.minOf { it.first }
        val maxX = input.maxOf { it.first }
        val maxY = input.maxOf { it.second }
        while (true) {
            iteration++
            var newSand = 500 to 0
            while (true) {
                if (newSand.first < minX || newSand.first > maxX || newSand.second > maxY) {
                    // Out-of-bounds!
                    return iteration - 1 // Remove one because this sand doesn't count
                }

                newSand = if (!map.contains(newSand.first to newSand.second + 1)) {
                    // Go down
                    newSand.first to newSand.second + 1
                } else if (!map.contains(newSand.first - 1 to newSand.second + 1)) {
                    // Go Left and down
                    newSand.first - 1 to newSand.second + 1
                } else if (!map.contains(newSand.first + 1 to newSand.second + 1)) {
                    // Go Right and down
                    newSand.first + 1 to newSand.second + 1
                } else {
                    // reached final position
                    map.add(newSand)
                    break
                }
            }
        }
    }

    fun part2(input: Set<Pair<Int, Int>>): Int {
        val map = input.toMutableSet()
        var iteration = 0
        val maxY = input.maxOf { it.second }
        while (true) {
            iteration++
            var newSand = 500 to 0
            while (true) {
                newSand = if (!map.contains(newSand.first to newSand.second + 1)) {
                    // Go down
                    newSand.first to newSand.second + 1
                } else if (!map.contains(newSand.first - 1 to newSand.second + 1)) {
                    // Go Left and down
                    newSand.first - 1 to newSand.second + 1
                } else if (!map.contains(newSand.first + 1 to newSand.second + 1)) {
                    // Go Right and down
                    newSand.first + 1 to newSand.second + 1
                } else {
                    // reached final position
                    if (newSand == 500 to 0) {
                        // Reached the source
                        return iteration
                    }

                    map.add(newSand)
                    break
                }

                if (newSand.second == (maxY + 2)) {
                    // Reached the floor
                    iteration-- // These don't count for the final count, it's just to build a dummy floor
                    map.add(newSand)
                    break
                }
            }
        }
    }

    fun readInput(): Set<Pair<Int, Int>> = File("inputFiles/day14.txt").readLines().flatMap { trace ->
        trace.split(" -> ").asSequence().map { value -> value.split(',').map { pos -> pos.toInt() } }.windowed(2, 1)
            .map {
                val (start, end) = it
                val finalSet = mutableSetOf<Pair<Int, Int>>()
                (start.first()..end.first()).map { pos -> pos to start.last() }.toSet().let { list -> finalSet.addAll(list) } // going right
                (end.first()..start.first()).map { pos -> pos to start.last() }.toSet().let { list -> finalSet.addAll(list) } // going left
                (start.last()..end.last()).map { pos -> start.first() to pos }.toSet().let { list -> finalSet.addAll(list) } // going up
                (end.last()..start.last()).map { pos -> start.first() to pos }.toSet().let { list -> finalSet.addAll(list) } // going down
                finalSet
            }.flatten().toSet()
    }.toSet()
}
