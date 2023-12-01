package aoc2022

import utils.InputRetrieval

fun main() {
    Day8.execute()
}

object Day8 {
    fun execute() {
        val input = readInput()
        val (visibleCount, highestScenicScore) = solve(input)
        println("Part 1: $visibleCount")
        println("Part 2: $highestScenicScore")
    }

    private fun solve(input: List<String>): Pair<Int, Int> {
        val columnLength = input.size
        val lineLength = input[0].length
        var visibleCount = (columnLength * 2) + (lineLength * 2) - 4
        var highestScenicScore = 0
        for (y in 1 until columnLength - 1) {
            for (x in 1 until lineLength - 1) {
                val treeSize = input[y][x]
                // Verify visibility
                if (
                    input[y].subSequence(0, x).all { it < treeSize } // Check left
                    || input[y].subSequence(x + 1, lineLength).all { it < treeSize }// Check Right
                    || input.subList(0, y).map { it[x] }.all { it < treeSize }// Check top
                    || input.subList(y + 1, columnLength).map { it[x] }.all { it < treeSize }// Check bottom
                ) {
                    visibleCount++
                }
                // Calculate Scenic Score
                val scenicScore = input[y].subSequence(0, x).toList().reversed().countVisibleTrees(treeSize) * // Check left
                    input[y].subSequence(x + 1, lineLength).toList().countVisibleTrees(treeSize) * // Check Right
                    input.subList(0, y).map { it[x] }.reversed().countVisibleTrees(treeSize) * // Check top
                    input.subList(y + 1, columnLength).map { it[x] }.countVisibleTrees(treeSize)// Check bottom
                if (scenicScore > highestScenicScore) {
                    highestScenicScore = scenicScore
                }
            }
        }
        return visibleCount to highestScenicScore
    }

    private fun List<Char>.countVisibleTrees(treeSize: Char): Int {
        val size = this.size
        val filteredSize = this.takeWhile { it < treeSize }.size
        return if (filteredSize < size) {
            filteredSize + 1
        } else {
            filteredSize
        }
    }

    private fun readInput(): List<String> = InputRetrieval.getFile(2022, 8).readLines()
}
