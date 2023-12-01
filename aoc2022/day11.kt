package aoc2022

import utils.InputRetrieval

fun main() {
    Day11.execute()
}

object Day11 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        input.forEach { it.reset() }
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<Monkey>): Long = monkeyBusiness(input, 20) { it / 3L }

    private fun part2(input: List<Monkey>): Long {

        val modulus = input.map { it.test.value }.reduce(Long::times)
        return monkeyBusiness(input, 10000) { it % modulus }
    }

    private fun monkeyBusiness(input: List<Monkey>, rounds: Int, worryLevelManagement: (Long) -> Long): Long {
        val inspectedItemsPerMonkey = MutableList(input.size) { 0L }
        repeat(rounds) {
            // Process Monkeys
            input.forEachIndexed { index, monkey ->
                monkey.items.forEach { item ->
                    val newItemValue = worryLevelManagement.invoke(monkey.operation.apply(item))
                    val targetMonkey = monkey.test.apply(newItemValue)
                    input[targetMonkey].items.add(newItemValue)
                    inspectedItemsPerMonkey[index]++
                }
                monkey.items.clear()
            }
        }
        return inspectedItemsPerMonkey.sorted().takeLast(2).reduce { acc, value -> acc * value }
    }

    private fun readInput(): List<Monkey> = InputRetrieval.getFile(2022, 11).readText().split("\n\n").map {
        Monkey.parse(it)
    }

    class Monkey(var items: MutableList<Long>, var operation: Operation, var test: Test, private val originalList: List<Long> = items.toList()) {

        fun reset() {
            items = originalList.toMutableList()
        }

        companion object {
            fun parse(input: String): Monkey {
                val lines = input.lines()
                val startingItems = lines[1].trim().removePrefix("Starting items: ").split(", ").map { it.toLong() }.toMutableList()
                val operation = Operation.parse(lines[2])
                val test = Test.parse(lines[3], lines[4], lines[5])
                return Monkey(startingItems, operation, test)
            }
        }
    }

    class Operation(private val operator: Char, private val value: String) {
        fun apply(itemValue: Long): Long {
            val parsedValue = when (value) {
                "old" -> itemValue
                else -> value.toLong()
            }
            return when (operator) {
                '+' -> itemValue + parsedValue
                '*' -> itemValue * parsedValue
                else -> throw RuntimeException("Impossible!") // Based on the input, this is impossible
            }
        }

        companion object {
            fun parse(input: String): Operation {
                val tmp = input.substringAfter("old ")
                val (operator, value) = tmp.split(' ')
                return Operation(operator.first(), value)
            }
        }
    }

    class Test(val value: Long, private val ifTrueMonkey: Int, private val ifFalseMonkey: Int) {
        fun apply(itemValue: Long): Int = when (itemValue % value == 0L) {
            true -> ifTrueMonkey
            false -> ifFalseMonkey
        }

        companion object {
            fun parse(operation: String, ifTrueAction: String, ifFalseAction: String): Test {
                val value = operation.substringAfter("divisible by ").toLong()
                val ifTrueMonkey = ifTrueAction.substringAfter("throw to monkey ").toInt()
                val ifFalseMonkey = ifFalseAction.substringAfter("throw to monkey ").toInt()
                return Test(value, ifTrueMonkey, ifFalseMonkey)
            }
        }
    }
}
