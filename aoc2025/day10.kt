package aoc2025

import utils.InputRetrieval

fun main() {
    Day10.execute()
}

private object Day10 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<Machine>): Long = input.sumOf {
        it.calculateMinimumPresses()
    }

    private fun part2(input: List<Machine>): Long = 0L

    private fun Machine.calculateMinimumPresses(): Long {
        // Implement a Breath-First Search to find the minimum number of presses
        val cache = mutableMapOf<String, Long>()
        val initialStatus = ".".repeat(this.lightDiagram.length)
        val queue = ArrayDeque<Pair<String, Long>>().apply { // Status, pressCount
            this.add(initialStatus to 0L)
        }
        while (queue.isNotEmpty()) {
            val (currentStatus, numberOfPresses) = queue.removeFirst()
            if (currentStatus == lightDiagram) {
                return numberOfPresses
            }

            if ((cache[currentStatus] ?: Long.MAX_VALUE) <= numberOfPresses) {
                // Already found a better way to reach this status
                continue
            }
            cache[currentStatus] = numberOfPresses

            for (buttonIndex in buttons.indices) {
                val button = this.buttons[buttonIndex]
                val newStatus = currentStatus.toMutableList().mapIndexed { index, indicatorState ->
                    if (button.contains(index)) {
                        if (indicatorState == '#') '.' else '#' // Flip the indicator state
                    } else {
                        indicatorState
                    }
                }.joinToString(separator = "")
                queue.add(newStatus to numberOfPresses + 1)
            }
        }
        return -1L
    }

    private fun readInput(): List<Machine> = InputRetrieval.getFile(2025, 10).readLines().map {
        val (lightDiagram, buttonsRaw, joltageRaw) = Regex("""\[(.*)] (.*?) \{(.*)}""").matchEntire(it)!!.destructured
        val buttons = buttonsRaw.split(" ").map { button ->
            button.removeSurrounding("(", ")").split(',').map(String::toInt)
        }
        val joltage = joltageRaw.split(',').map(String::toInt)
        Machine(lightDiagram = lightDiagram, buttons = buttons, joltage = joltage)
    }

    private data class Machine(
        val lightDiagram: String,
        val buttons: List<List<Int>>,
        val joltage: List<Int>,
    )
}
