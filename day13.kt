import Day13.part1
import Day13.part2
import Day13.readInput
import java.io.File
import kotlin.math.min

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}

object Day13 {

    fun part1(input: List<String>): Int {
        return input.windowed(2, 3).mapIndexed { index, it ->
            val (left, right) = it
            val result = parse(left).isSmaller(parse(right))
            if (result == Result.LOWER) {
                index + 1
            } else 0
        }.sum()
    }

    private val DELIMITERS = mutableListOf(parse("[[2]]", isDivider = true), parse("[[6]]", isDivider = true))
    fun part2(input: List<String>): Int {
        val parsedInput = input.filter { it.isNotEmpty() }.map { parse(it) }
        val inputWithDividers = DELIMITERS.apply { addAll(parsedInput) }

        return inputWithDividers.sortedWith { a: DigitOrList, b: DigitOrList -> a.isSmaller(b).value }
            .mapIndexed { index, value ->
                if (value.isDivider) {
                    index + 1
                } else {
                    0
                }
            }
            .filter { it != 0 }
            .reduce(Int::times)
    }

    private fun parse(input: String, isDivider: Boolean = false): DigitOrList {
        val allParents = ArrayDeque<MutableList<DigitOrList>>()
        var currentList: MutableList<DigitOrList>? = null

        var i = 0
        while (i < input.length) {
            when (input[i]) {
                in '0'..'9' -> {
                    // Get all numbers
                    var finalIndex = i + 1
                    while (input[finalIndex] != ',' && input[finalIndex] != ']') {
                        finalIndex++
                    }
                    val values = DigitOrList(input.substring(i, finalIndex).toInt())
                    currentList?.add(values)
                    i = finalIndex
                }

                '[' -> {
                    currentList?.also { allParents.add(it) }
                    currentList = mutableListOf()
                    i++
                }

                ']' -> {
                    allParents.removeLastOrNull()?.also { parent ->
                        currentList?.also { parent.add(DigitOrList(it)) }
                        currentList = parent
                    }
                    i++
                }

                else -> i++
            }
        }
        return DigitOrList(currentList!!, isDivider)
    }

    data class DigitOrList(var digit: Int?, var list: List<DigitOrList>?, val isDivider: Boolean) {
        constructor(value: Int, isDivider: Boolean = false) : this(value, null, isDivider)
        constructor(value: List<DigitOrList>, isDivider: Boolean = false) : this(null, value, isDivider)

        private fun isDigit() = digit != null

        fun isSmaller(other: DigitOrList): Result {
            if (this.isDigit() && other.isDigit()) {
                return if (this.digit!! < other.digit!!) {
                    Result.LOWER
                } else if (this.digit!! > other.digit!!) {
                    Result.GREATER
                } else {
                    Result.EQUAL
                }
            }
            if (!this.isDigit() && !other.isDigit()) {
                val size = min(this.list!!.size, other.list!!.size)
                return (0 until size).map { this.list!![it].isSmaller(other.list!![it]) }.firstOrNull { it != Result.EQUAL }
                    ?: if (this.list!!.size < other.list!!.size) {
                        Result.LOWER
                    } else if (this.list!!.size > other.list!!.size) {
                        Result.GREATER
                    } else {
                        Result.EQUAL
                    }
            }
            if (this.isDigit()) {
                return DigitOrList(listOf(this)).isSmaller(other)
            }
            return this.isSmaller(DigitOrList(listOf(other)))
        }
    }

    enum class Result(val value: Int) {
        EQUAL(0),
        LOWER(-1),
        GREATER(1)
    }

    fun readInput(): List<String> = File("inputFiles/day13.txt").readLines()
}
