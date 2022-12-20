import Day20.part1
import Day20.part2
import Day20.readInput
import java.io.File
import kotlin.math.abs

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day20 {

    fun part1(input: List<FileNumber>): Long {
        val mappedInput = input.toMutableList()
        mappedInput.mixElements(input)

        return mappedInput.getFinalResult()
    }

    fun part2(input: List<FileNumber>, decryptionKey: Int = 811589153): Long {
        input.forEach { it.value = it.value * decryptionKey }
        val mappedInput = input.toMutableList()
        repeat(10) {
            mappedInput.mixElements(input)
        }
        return mappedInput.getFinalResult()
    }

    /**
     * Add the initial position to provide uniqueness to the value and data class
     */
    data class FileNumber(val initialPos: Int, var value: Long)

    private fun MutableList<FileNumber>.getFinalResult(finalPositionsToSum: Set<Int> = setOf(1_000, 2_000, 3_000)): Long {
        val finalInput = this.map { it.value }.toList()
        val zeroPosIndex = finalInput.indexOfFirst { it == 0L }
        return finalPositionsToSum.sumOf { finalInput[(it + zeroPosIndex) % finalInput.size] }
    }

    private fun MutableList<FileNumber>.mixElements(input: List<FileNumber>) {
        input.forEach {
            val valueIndex = this.indexOf(it)
            this.remove(it)

            val tmpPos = valueIndex + it.value
            val newPos = if (tmpPos > 0) {
                tmpPos % this.size
            } else if (tmpPos < 0) {
                ((tmpPos + ((abs(it.value) / this.size) + 1) * this.size) % this.size)
            } else {
                this.size
            }
            this.add(newPos.toInt(), it)
        }
    }

    fun readInput(): List<FileNumber> = File("inputFiles/day20.txt").readLines().mapIndexed { index, it -> FileNumber(index, it.toLong()) }
}
