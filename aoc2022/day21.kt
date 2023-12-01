package aoc2022

import utils.InputRetrieval

fun main() {
    Day21.execute()
}

object Day21 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input).toLong()}")
    }

    private const val ROOT_MONKEY_NAME = "root"
    const val HUMAN_NAME = "humn"

    private fun part1(input: List<Monkey>): Long {
        val monkeyMap: Map<String, Monkey> = input.associateBy { it.id }
        return monkeyMap[ROOT_MONKEY_NAME]!!.getValue(monkeyMap)
    }

    private fun part2(input: List<Monkey>): Double {
        val monkeyMap: MutableMap<String, Monkey> = input.associateBy { it.id }.toMutableMap()
        val rootMonkey = monkeyMap[ROOT_MONKEY_NAME]!!
        val firstMonkey = monkeyMap[rootMonkey.op!!.firstMonkey]!!
        val secondMonkey = monkeyMap[rootMonkey.op.secondMonkey]!!
        return if (firstMonkey.containsHuman(monkeyMap)) {
            firstMonkey.getHumanValue(monkeyMap, secondMonkey.getValue(monkeyMap).toDouble())
        } else {
            secondMonkey.getHumanValue(monkeyMap, firstMonkey.getValue(monkeyMap).toDouble())
        }
    }

    private fun readInput(): List<Monkey> = InputRetrieval.getFile(2022, 21).readLines().map { Monkey.parse(it) }

    data class Monkey(val id: String, var value: Int?, val op: Operation?) {

        fun getValue(monkeyMap: Map<String, Monkey>): Long {
            return value?.toLong() ?: op!!.solve(monkeyMap)
        }

        fun containsHuman(monkeyMap: Map<String, Monkey>): Boolean {
            return if (this.id == HUMAN_NAME) {
                true
            } else if (value != null) {
                false
            } else if (op!!.firstMonkey == HUMAN_NAME || op.secondMonkey == HUMAN_NAME) {
                true
            } else {
                return monkeyMap[op.firstMonkey]!!.containsHuman(monkeyMap) || monkeyMap[op.secondMonkey]!!.containsHuman(monkeyMap)
            }
        }

        fun getHumanValue(monkeyMap: Map<String, Monkey>, expectedValue: Double): Double {
            if (id == HUMAN_NAME) {
                return expectedValue
            }
            val firstMonkey = monkeyMap[this.op!!.firstMonkey]!!
            val secondMonkey = monkeyMap[this.op.secondMonkey]!!

            if (firstMonkey.containsHuman(monkeyMap)) {
                val newExpectedValue = when (op.operator) {
                    '+' -> expectedValue - secondMonkey.getValue(monkeyMap)
                    '*' -> expectedValue / secondMonkey.getValue(monkeyMap)
                    '/' -> expectedValue * secondMonkey.getValue(monkeyMap)
                    '-' -> expectedValue + secondMonkey.getValue(monkeyMap)
                    else -> throw RuntimeException("Impossible!") // Based on the input, this is impossible
                }
                return firstMonkey.getHumanValue(monkeyMap, newExpectedValue)
            } else {
                val newExpectedValue = when (op.operator) {
                    '+' -> expectedValue - firstMonkey.getValue(monkeyMap)
                    '*' -> expectedValue / firstMonkey.getValue(monkeyMap)
                    '/' -> expectedValue * firstMonkey.getValue(monkeyMap)
                    '-' -> firstMonkey.getValue(monkeyMap) - expectedValue // This is different because math! ;)
                    else -> throw RuntimeException("Impossible!") // Based on the input, this is impossible
                }
                return secondMonkey.getHumanValue(monkeyMap, newExpectedValue)
            }
        }

        companion object {
            fun parse(input: String): Monkey {
                val id = input.substringBefore(':')
                val remainder = input.substringAfter(": ")
                var number: Int? = null
                var operation: Operation? = null
                if (remainder.contains(' ')) {
                    // operation
                    val (firstMonkey, op, secondMonkey) = remainder.split(' ')
                    operation = Operation(op.first(), firstMonkey, secondMonkey)
                } else {
                    number = remainder.toInt()
                }
                return Monkey(id, number, operation)
            }
        }
    }

    data class Operation(val operator: Char, val firstMonkey: String, val secondMonkey: String) {
        fun solve(monkeyMap: Map<String, Monkey>): Long {
            val firstMonkeyValue = monkeyMap[firstMonkey]!!.getValue(monkeyMap)
            val secondMonkeyValue = monkeyMap[secondMonkey]!!.getValue(monkeyMap)
            return when (operator) {
                '+' -> firstMonkeyValue + secondMonkeyValue
                '*' -> firstMonkeyValue * secondMonkeyValue
                '/' -> firstMonkeyValue / secondMonkeyValue
                '-' -> firstMonkeyValue - secondMonkeyValue
                else -> throw RuntimeException("Impossible!") // Based on the input, this is impossible
            }
        }
    }
}
