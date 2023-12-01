package aoc2022

import utils.InputRetrieval
import java.util.ArrayDeque

fun main() {
    Day5.execute()
}

object Day5 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: Pair<List<ArrayDeque<Char>>, List<Move>>): String {
        val (initialStatus, moves) = input
        val currentStatus = initialStatus.map { it.clone() } // Create a copy of the queues, so we don't modify the original input
        moves.forEach { move -> repeat(move.number) { currentStatus[move.targetPos - 1].push(currentStatus[move.startPos - 1].pop()) } }
        return currentStatus.getTopElements()
    }

    private fun part2(input: Pair<List<ArrayDeque<Char>>, List<Move>>): String {
        val (currentStatus, moves) = input
        moves.forEach { move ->
            val tmpList = mutableListOf<Char>()
            repeat(move.number) { tmpList.add(currentStatus[move.startPos - 1].pop()) }
            repeat(move.number) { currentStatus[move.targetPos - 1].push(tmpList.removeLast()) }
        }
        return currentStatus.getTopElements()
    }

    private fun readInput(): Pair<List<ArrayDeque<Char>>, List<Move>> {
        val (initialStatus, moves) = InputRetrieval.getFile(2022, 5).readText().dropLast(1).split("\n\n")

        val boardState = MutableList(initialStatus.trim().last().digitToInt()) { ArrayDeque<Char>() }
        initialStatus.lines().dropLast(1).reversed().forEach {
            it.chunked(4).forEachIndexed { index, value ->
                if (value.isNotBlank()) {
                    boardState[index].push(value.removePrefix("[").first())
                }
            }
        }

        val parsedMoves = moves.lines().map { Move(it.removePrefix("move ").split(" from ", " to ")) }
        return boardState to parsedMoves
    }

    private fun List<ArrayDeque<Char>>.getTopElements(): String = this.map { it.first }.joinToString("")

    data class Move(
        val number: Int,
        val startPos: Int,
        val targetPos: Int,
    ) {
        constructor(input: List<String>) : this(input.first().toInt(), input[1].toInt(), input.last().toInt())
    }
}
