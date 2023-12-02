package aoc2023

import utils.InputRetrieval

fun main() {
    Day2.execute()
}

private object Day2 {

    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<Game>): Int {
        // Only 12 red cubes, 13 green cubes, and 14 blue cubes are allowed
        return input.filter { it.rounds.none { round -> round.red > 12 || round.green > 13 || round.blue > 14 } }.sumOf { it.number }
    }

    private fun part2(input: List<Game>): Int {
        // The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together
        return input.sumOf { game -> game.rounds.maxOf { it.red } * game.rounds.maxOf { it.green } * game.rounds.maxOf { it.blue } }
    }

    private fun readInput(): List<Game> = InputRetrieval.getFile(2023, 2).readLines().map { Game.parse(it) }

    data class Game(val number: Int, val rounds: List<Round>) {
        companion object {
            fun parse(input: String): Game {
                val (gameNumberStr, gamesStr) = input.split(':')
                val gameNumber: Int = gameNumberStr.removePrefix("Game ").toInt()
                val rounds = gamesStr.split(';').map { round -> Round.parse(round) }
                return Game(gameNumber, rounds)
            }
        }
    }

    data class Round(val red: Int, val green: Int, val blue: Int) {
        companion object {
            fun parse(input: String): Round {
                val colours = input.split(',').associate { s ->
                    val (value, colour) = s.trim().split(' ')
                    colour to value.toInt()
                }
                return Round(colours["red"] ?: 0, colours["green"] ?: 0, colours["blue"] ?: 0)
            }
        }
    }
}
