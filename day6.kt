import Day6.part1
import Day6.part2
import Day6.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day6 {

    fun part1(input: String): Int = input.windowed(4, 1).map { it.toSet().size == 4 }.indexOfFirst { it } + 4

    fun part2(input: String): Int = input.windowed(14, 1).map { it.toSet().size == 14 }.indexOfFirst { it } + 14

    fun readInput(): String = File("inputFiles/day6.txt").readLines().first()
}
