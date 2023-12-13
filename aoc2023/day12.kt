package aoc2023

import utils.InputRetrieval

fun main() {
    Day12.execute()
}

private object Day12 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<SpringStatus>): Long = input
        .sumOf { it.calculatePossibilities() }

    private fun part2(input: List<SpringStatus>, numberOfFolds: Int = 5): Long = input
        .map { SpringStatus(List(numberOfFolds) { _ -> it.springs }.joinToString("?"), List(numberOfFolds) { _ -> it.damagedGroups }.flatten()) }
        .sumOf { it.calculatePossibilities() }

    private fun readInput(): List<SpringStatus> = InputRetrieval.getFile(2023, 12)
        .readLines()
        .map { SpringStatus.parse(it) }

    private data class SpringStatus(val springs: String, val damagedGroups: List<Int>, val knownDamagedIndex: List<Int> = springs.indices.filter { springs[it] == '#' }) {

        private val cache = mutableMapOf<String, Long>()
        fun calculatePossibilities(index: Int = 0, damagedGroupIndex: Int = 0): Long {
            val cacheKey = "$damagedGroupIndex-$index"
            cache[cacheKey]?.let { return it }

            // Reached the end of the String, validate if valid
            if (index >= springs.length) {
                return if (damagedGroupIndex >= damagedGroups.size) 1L else 0L
            }
            // Reached the end of the Damaged Groups, validate if all remaining are working
            if (damagedGroupIndex >= damagedGroups.size) {
                return if (this.springs.drop(index).any { it == '#' }) 0L else 1L
            }

            // Test the scenario where it is damaged
            val expectedDamaged = damagedGroups[damagedGroupIndex]
            val remainingSlots = springs.length - index
            val possibilitiesIfDamaged = if (
                this.springs[index] != '.'
                && expectedDamaged <= remainingSlots
                && !this.springs.substring(index..<(index + expectedDamaged)).any { it == '.' }
                && !(remainingSlots != expectedDamaged && this.springs[index + expectedDamaged] == '#')
            ) {
                calculatePossibilities(index + expectedDamaged + 1, damagedGroupIndex + 1)
            } else {
                0L
            }

            // Test the scenario where it is operational
            val possibilitiesIfWorking = if (this.springs[index] != '#') {
                this.calculatePossibilities(index + 1, damagedGroupIndex)
            } else {
                0L
            }

            return (possibilitiesIfWorking + possibilitiesIfDamaged)
                .also { cache[cacheKey] = it }
        }

        companion object {
            fun parse(input: String): SpringStatus {
                val (gears, groups) = input.split(' ')
                return SpringStatus(gears, groups.split(',').map { it.toInt() })
            }
        }
    }
}
