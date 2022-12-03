import Day2.part1
import Day2.part2
import Day2.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day2 {

    /**
     * Rock Paper Scissors (Part 1)
     * Opponent Move:
     * A - Rock
     * B - Paper
     * C - Scissors
     * Our Move:
     * X - Rock - 1pts
     * Y - Paper - 2pts
     * Z - Scissors - 3pts
     * Outcome of the round:
     * Lost - 0pts
     * Draw - 3pts
     * Won - 6pts
     */
    private val SCORE_MAP = mapOf("A X" to 4, "A Y" to 8, "A Z" to 3, "B X" to 1, "B Y" to 5, "B Z" to 9, "C X" to 7, "C Y" to 2, "C Z" to 6)
    fun part1(input: List<String>): Int = input.sumOf { SCORE_MAP[it]!! }

    /**
     * Rock Paper Scissors (Part 2)
     * Opponent Move:
     * A - Rock
     * B - Paper
     * C - Scissors
     * Outcome of the round:
     * X - Must Lose - 0pts
     * Y - Must Draw - 3pts
     * Z - Must Win - 6pts
     * Our Move:
     * Rock - 1pts
     * Paper - 2pts
     * Scissors - 3pts
     */
    private val SCORE_MAP_PART_2 = mapOf("A X" to 3, "A Y" to 4, "A Z" to 8, "B X" to 1, "B Y" to 5, "B Z" to 9, "C X" to 2, "C Y" to 6, "C Z" to 7)
    fun part2(input: List<String>): Int = input.sumOf { SCORE_MAP_PART_2[it]!! }

    fun readInput(): List<String> = File("inputFiles/day2.txt").readLines()
}
