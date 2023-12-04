package aoc2023

import utils.InputRetrieval
import kotlin.math.pow

fun main() {
    Day4.execute()
}

private object Day4 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<ScratchCard>): Int = input.sumOf { it.value() }

    // We could improve the performance of this, so we could cache data, but it's good enough... And this way I can use the PC as a heat source
    private fun part2(input: List<ScratchCard>): Int {
        val ticketCount: MutableMap<Int, Int> = input.associate { it.cardId to 1 }.toMutableMap()
        val winningTickets = input.filter { it.winningTicket() }.associateBy { it.cardId }
        winningTickets.values.forEach {
            it.increaseCardCount(ticketCount, winningTickets)
        }
        return ticketCount.values.sum()
    }

    private fun readInput(): List<ScratchCard> = InputRetrieval.getFile(2023, 4).readLines().map { ScratchCard.parse(it) }

    private fun ScratchCard.increaseCardCount(ticketCount: MutableMap<Int, Int>, winningTickets: Map<Int, ScratchCard>) {
        (1..this.matchingNumberCount).map { this.cardId + it }.forEach {
            ticketCount[it] = ticketCount[it]!! + 1
            winningTickets[it]?.increaseCardCount(ticketCount, winningTickets)
        }
    }

    data class ScratchCard(
        val cardId: Int,
        val winningNumbers: List<Int>,
        val numbers: List<Int>,
        val matchingNumberCount: Int = numbers.filter { number -> winningNumbers.contains(number) }.size,
    ) {
        fun winningTicket(): Boolean = this.matchingNumberCount > 0
        fun value(): Int = if (winningTicket()) (2.0).pow(this.matchingNumberCount - 1).toInt() else 0

        companion object {
            fun parse(input: String): ScratchCard {
                val (cardId, rawNumbers) = input.split(':')
                val (winningNumbers, numbers) = rawNumbers.split('|')
                return ScratchCard(
                    cardId = cardId.removePrefix("Card").trim().toInt(),
                    winningNumbers = winningNumbers.split(' ').filter { it.isNotBlank() }.map { it.toInt() },
                    numbers = numbers.split(' ').filter { it.isNotBlank() }.map { it.toInt() })
            }
        }
    }
}
