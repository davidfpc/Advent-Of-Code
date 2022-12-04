import Day4.part1
import Day4.part2
import Day4.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day4 {

    fun part1(input: List<List<Int>>): Int = input.filter { pair ->
        val (start1, end1, start2, end2) = pair
        return@filter (start1 <= start2 && end1 >= end2) || (start2 <= start1 && end2 >= end1)
    }.size

    fun part2(input: List<List<Int>>): Int = input.filter { pair ->
        val (start1, end1, start2, end2) = pair
        return@filter (start1..end1).intersect(start2..end2).isNotEmpty()
    }.size

    fun readInput(): List<List<Int>> = File("inputFiles/day4.txt").readLines().map { pair -> pair.split(',', '-').map { it.toInt() } }
}
