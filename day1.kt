import Day1.part1
import Day1.part2
import Day1.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day1 {
    /**
     * Get Elf with the maximum calories.
     */
    fun part1(input: List<List<Int>>): Int = input.maxOf { it.sum() }

    /**
     * Get Top 3 Elves with the maximum calories.
     */
    fun part2(input: List<List<Int>>): Int = input.map { it.sum() }.sortedDescending().take(3).sum()

    fun readInput(): List<List<Int>> {
        val inputString = File("inputFiles/day1.txt").readText().dropLast(1)
        return inputString.split("\n\n").map { it.split('\n').map { calories -> calories.toInt() } }
    }
}
