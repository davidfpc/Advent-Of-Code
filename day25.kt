import Day25.part1
import Day25.readInput
import java.io.File
import kotlin.math.pow

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
}

object Day25 {

    private val DIGIT_MAP = mapOf('=' to -2, '-' to -1, '0' to 0, '1' to 1, '2' to 2)
    private val DIGIT_MAP_INVERSE = DIGIT_MAP.map { it.value.toLong() to it.key }.toMap()
    fun part1(input: List<String>): String = input.sumOf { it.convertSNAFUToDecimal() }.convertDecimalToSNAFU()

    private fun String.convertSNAFUToDecimal() = this.reversed().mapIndexed { index, i -> DIGIT_MAP[i]!! * (5.0.pow(index).toLong()) }.sum()

    private fun Long.convertDecimalToSNAFU(): String {
        return if (this == 0L) ""
        else when (val m = this % 5L) {
            0L, 1L, 2L -> (this / 5L).convertDecimalToSNAFU() + DIGIT_MAP_INVERSE[m]!!
            3L, 4L -> (this / 5L + 1L).convertDecimalToSNAFU() + DIGIT_MAP_INVERSE[m - 5L]!!
            else -> throw Exception("modulus fail for $this")
        }
    }

    fun readInput(): List<String> = File("inputFiles/day25.txt").readLines()
}
