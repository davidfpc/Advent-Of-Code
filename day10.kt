import Day10.part1
import Day10.part2
import Day10.readInput
import java.io.File

fun main() {
    val input = readInput()
    println("Part 1: ${part1(input)}")
    println("Part 2:")
    part2(input)
}

object Day10 {

    private val CICLES = listOf(20, 60, 100, 140, 180, 220)

    fun part1(input: List<String>): Int {
        return processInstructions(input, 220).mapIndexed { index, value ->
            if (CICLES.contains(index)) {
                index * value
            } else {
                0
            }
        }.sum()
    }

    fun part2(input: List<String>) {
        val spritePositions = processInstructions(input, 240)
        (1..240).forEach {
            val currentSpritePos = spritePositions[it]
            // print twice as many pixels to make it more readable
            if (((it - 1) % 40) in (currentSpritePos - 1..currentSpritePos + 1)) {
                print("##")
            } else {
                print("..")
            }
            if ((it % 40) == 0) {
                println()
            }

        }
    }

    private fun processInstructions(input: List<String>, maxCycles: Int): List<Int> {
        var x = 1 // initial signal strength
        val result = mutableListOf(1)
        var instructionPointer = 0
        var processing = false

        // run 220 cycles
        for (it in 1..maxCycles) {
            result.add(x)
            // process instruction
            if (instructionPointer == input.size) {
                continue
            }
            val instruction = input[instructionPointer]
            when (instruction.substring(0..3)) {
                "noop" -> instructionPointer++
                "addx" -> if (processing) {
                    processing = false
                    instructionPointer++
                    x += instruction.substringAfter("addx ").toInt()
                } else {
                    processing = true
                }
            }
        }
        return result
    }

    fun readInput(): List<String> = File("inputFiles/day10.txt").readLines()
}
