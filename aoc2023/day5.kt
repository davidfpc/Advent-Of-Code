package aoc2023

import utils.InputRetrieval

fun main() {
    Day5.execute()
}

private object Day5 {
    fun execute() {
        val input = readInput()
        val (seedRanges, maps) = input
        println("Part 1: ${part1(seedRanges, maps)}")
        println("Part 2: ${part2(seedRanges, maps)}")
    }

    private fun part1(seeds: Iterable<Long>, maps: List<Mapping>): Long = seeds.minOf {
        maps.fold(it) { seed, mapping -> mapping.getMappedValue(seed) }
    }

    // Same as yesterday. We could improve the performance of this by changing the algorithm, but... I'm cold and the heat from the CPU fans feel nice
    private fun part2(seedRanges: List<Long>, maps: List<Mapping>): Long {
        return seedRanges.chunked(2).minOf {
            println("Start calculation for chunk: ${it.first()}")
            part1((it.first()..<(it.first() + it.last())), maps)
        }
    }

    private fun readInput(): Pair<List<Long>, List<Mapping>> {
        var lines = InputRetrieval.getFile(2023, 5).readLines()
        // Seeds
        val seeds = lines.first().removePrefix("seeds: ").split(' ').map { it.toLong() }
        lines = lines.drop(2) // Remove seeds and empty line

        val mappings = mutableListOf<Mapping>()
        repeat(7) {
            lines = lines.drop(1) // Remove header
            val inputMap = lines.takeWhile { it.isNotBlank() }
            mappings.add(Mapping.parse(inputMap))
            lines = lines.drop(inputMap.size + 1) // Remove maps and empty line
        }
        return seeds to mappings
    }

    private data class Mapping(private val mapping: List<Triple<Long, Long, Long>>) {
        fun getMappedValue(value: Long): Long {
            val map = mapping.firstOrNull { (it.first <= value) && (value < (it.first + it.third)) }
            return map?.let { it.second + (value - it.first) } ?: value
        }

        companion object {
            fun parse(input: List<String>): Mapping {
                val mapping = input.map {
                    val (dest, source, range) = it.split(' ').map { a -> a.toLong() }
                    Triple(source, dest, range)
                }.toList()
                return Mapping(mapping)
            }
        }
    }
}
