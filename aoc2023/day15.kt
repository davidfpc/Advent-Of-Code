package aoc2023

import utils.InputRetrieval

fun main() {
    Day15.execute()
}

private object Day15 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<Operation>): Long = input.sumOf { it.holidayASCIIStringHelper.toLong() }
    private fun part2(input: List<Operation>): Long {
        // Could I have used other data structure and it would be easier? Yes, but then the joke would be lost ;)
        val holidayASCIIStringHelperManualArrangementProcedure = HashMap((0..255).associateWith { Box(it) })
        input.forEach {
            it.execute(holidayASCIIStringHelperManualArrangementProcedure)

        }
        return holidayASCIIStringHelperManualArrangementProcedure.values.sumOf { it.focalPower() }
    }

    private fun String.holidayASCIIStringHelper(): Int {
        var hash = 0
        this.forEach {
            hash = ((hash + it.code) * 17) % 256
        }
        return hash
    }

    private fun readInput(): List<Operation> = InputRetrieval.getFile(2023, 15).readLines()
        .first().split(',')
        .map { Operation.parse(it) }

    private enum class OperationCode { REMOVE, REPLACE_OR_ADD }

    private data class Operation(val label: String, val operationCode: OperationCode, val focalLength: Int?, val holidayASCIIStringHelper: Int) {

        fun execute(holidayASCIIStringHelperManualArrangementProcedure: HashMap<Int, Box>) {
            val box = holidayASCIIStringHelperManualArrangementProcedure[label.holidayASCIIStringHelper()]!!
            when (operationCode) {
                OperationCode.REMOVE -> box.lenses.removeAll { it.label == this.label }
                OperationCode.REPLACE_OR_ADD -> {
                    val lens = box.lenses.find { it.label == this.label }
                    if (lens == null) {
                        box.lenses.add(Lens(this.label, this.focalLength!!))
                    } else {
                        lens.focalLength = this.focalLength!!
                    }
                }
            }
        }

        companion object {
            fun parse(input: String): Operation {
                val operationCode = when {
                    input.contains('-') -> OperationCode.REMOVE
                    else -> OperationCode.REPLACE_OR_ADD
                }
                val label = input.substringBefore('=').substringBefore('-')
                val focalLength = if (operationCode == OperationCode.REPLACE_OR_ADD) {
                    input.substringAfter('=').toInt()
                } else null
                return Operation(label, operationCode, focalLength, input.holidayASCIIStringHelper())
            }
        }
    }

    private data class Box(val id: Int, val lenses: MutableList<Lens> = mutableListOf()) {
        fun focalPower(): Long = lenses.mapIndexed { index, lens -> (this.id + 1L) * (index + 1L) * lens.focalLength }.sum()
    }

    private data class Lens(val label: String, var focalLength: Int)
}
