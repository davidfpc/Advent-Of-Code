package aoc2025

import utils.InputRetrieval

fun main() {
    Day5.execute()
}

private object Day5 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: Inventory): Int {
        return input.ingredients.count { ingredient ->
            input.freshIngredientRange.any { (start, end) -> ingredient in start..end }
        }
    }

    private fun part2(input: Inventory): Long {
        val freshIngredientsRange = input.freshIngredientRange.toMutableList()
        var noChanges: Boolean
        do {
            noChanges = true
            for (index in 0 until freshIngredientsRange.size) {
                val (start, end) = freshIngredientsRange[index]
                // Check for collisions in the start range
                val startCollision = freshIngredientsRange.findCollisionsStart(start, end)
                if (startCollision != null) {
                    // Start collided
                    noChanges = false
                    val (startCollidingRange, endCollidingRange) = startCollision
                    if (end <= endCollidingRange) {
                        // The range is fully contained in the other range 🔨
                        freshIngredientsRange[index] = -1L to -1L
                    } else {
                        freshIngredientsRange[index] = startCollidingRange + 1 to end
                    }
                    continue
                }

                // Check for collisions in the end range
                val endCollision = freshIngredientsRange.findCollisionsEnd(start, end)
                if (endCollision != null) {
                    // End collided
                    noChanges = false
                    val (startCollidingRange, _) = endCollision
                    if (start >= startCollidingRange) {
                        // The range is fully contained in the other range 🔨
                        freshIngredientsRange[index] = -1L to -1L
                    } else {
                        freshIngredientsRange[index] = start to startCollidingRange - 1
                    }
                }
            }
            freshIngredientsRange.removeIf { (start, _) -> start == -1L } // Hammer time 🔨
        } while (!noChanges)
        return freshIngredientsRange.toSet().sumOf { (rangeStart, rangeEnd) -> rangeEnd - rangeStart + 1 }
    }

    private fun List<Pair<Long, Long>>.findCollisionsStart(start: Long, end: Long): Pair<Long, Long>? {
        return this.firstOrNull { (rangeStart, rangeEnd) -> (start != rangeStart || end != rangeEnd) && start in rangeStart..rangeEnd }
    }

    private fun List<Pair<Long, Long>>.findCollisionsEnd(start: Long, end: Long): Pair<Long, Long>? {
        return this.firstOrNull { (rangeStart, rangeEnd) -> (start != rangeStart || end != rangeEnd) && end in rangeStart..rangeEnd }
    }

    private fun readInput(): Inventory {
        val lines = InputRetrieval.getFile(2025, 5).readLines()
        val ranges = lines.takeWhile { line -> line.isNotEmpty() }.map { line ->
            val (start, end) = line.split('-').map { it.toLong() }
            start to end
        }
        val ingredients = lines.drop(ranges.size + 1).map { it.toLong() }
        return Inventory(ranges.toSet(), ingredients.toSet())
    }

    private data class Inventory(
        val freshIngredientRange: Set<Pair<Long, Long>>,
        val ingredients: Set<Long>,
    )
}
