import Day3.part1
import Day3.part2
import Day3.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day3 {

    private val PRIORITIES = ('a'..'z').plus('A'..'Z')
    fun part1(input: List<String>): Int = input.map { it.toList() }.sumOf { rucksack ->
        rucksack.chunked(rucksack.size / 2).reduce { acc, items -> acc.intersect(items.toSet()).toList() }.calculatePriorities()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.toSet() }.chunked(3).sumOf {
            it.reduce { acc, rucksack -> acc.intersect(rucksack) }.calculatePriorities()
        }
    }

    private fun Iterable<Char>.calculatePriorities(): Int = this.sumOf { badge -> PRIORITIES.indexOf(badge) + 1 }

    fun readInput(): List<String> = File("inputFiles/day3.txt").readLines()
}
