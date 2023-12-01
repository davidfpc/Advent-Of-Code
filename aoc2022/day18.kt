package aoc2022

import utils.InputRetrieval

fun main() {
    Day18.execute()
}

object Day18 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        // println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<Cube>): Int = input.sumOf { it.countSidesNotInContactWithOtherCubes(input) }

    private fun part2(input: List<Cube>): Int {
        val context = Context(input)
        return input.sumOf {
            context.validating.clear()
            it.countSidesInContactWithWater(context)
        }
    }

    fun readInput(): List<Cube> = InputRetrieval.getFile(2022, 18).readLines().map { pos ->
        val (x, y, z) = pos.split(",").map { it.toInt() }
        Cube(x, y, z)
    }

    class Context(val input: List<Cube>) {
        val minX = input.minOf { it.x }
        val maxX = input.maxOf { it.x }
        val minY = input.minOf { it.y }
        val maxY = input.maxOf { it.y }
        val minZ = input.minOf { it.z }
        val maxZ = input.maxOf { it.z }
        val validating: MutableSet<Cube> = mutableSetOf()
    }

    data class Cube(val x: Int, val y: Int, val z: Int) {
        private fun getNeighbours(): Set<Cube> = setOf(
            Cube(x - 1, y, z), // LEFT
            Cube(x + 1, y, z), // RIGHT
            Cube(x, y, z + 1), // UP
            Cube(x, y, z - 1), // DOWN
            Cube(x, y - 1, z), // BACK
            Cube(x, y + 1, z), // FRONT
        )

        fun countSidesNotInContactWithOtherCubes(input: List<Cube>): Int = getNeighbours().count { !input.contains(it) }

        fun countSidesInContactWithWater(context: Context): Int {
            return getNeighbours().count { !context.input.contains(it) && !it.isInPocket(context) }
        }

        private fun isInPocket(context: Context): Boolean {
            context.validating.add(this)
            if (context.input.contains(this)) {
                return true
            }

            if (x < context.minX || x > context.maxX || y < context.minY || y > context.maxY || z < context.minZ || z > context.maxZ) {
                // outside of borders
                return false
            }

            // Let's check the neighbours to check if we are inside a pocket
            return getNeighbours().filter { !context.validating.contains(it) }.all { it.isInPocket(context) }
        }
    }
}
