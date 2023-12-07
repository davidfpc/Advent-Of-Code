package aoc2023

import utils.InputRetrieval

fun main() {
    Day7.execute()
}

private object Day7 {
    fun execute() {
        val input = readInput()
        println("Part 1: ${part1(input)}")
        println("Part 2: ${part2(input)}")
    }

    private fun part1(input: List<Hand>): Long = input
        .sortedWith(HandComparator(CARD_STRENGTH) { it.handType })
        .mapIndexed { i, hand -> hand.bid * (i + 1L) }
        .sum()

    private fun part2(input: List<Hand>): Long = input
        .sortedWith(HandComparator(CARD_STRENGTH_PART_2) { it.jokerHandType })
        .mapIndexed { i, hand -> hand.bid * (i + 1L) }
        .sum()

    private fun readInput(): List<Hand> = InputRetrieval.getFile(2023, 7)
        .readLines()
        .map {
            val (cards, bid) = it.split(' ')
            Hand(cards, bid.toInt())
        }

    private enum class HandType {
        FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, THREE_OF_A_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD
    }

    private val CARD_STRENGTH = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    private val CARD_STRENGTH_PART_2 = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    private class HandComparator(val cardStrength: List<Char>, val handTypeMapper: (Hand) -> HandType) : Comparator<Hand> {
        override fun compare(a: Hand, b: Hand): Int {
            val handTypeOfA = handTypeMapper.invoke(a)
            val handTypeOfB = handTypeMapper.invoke(b)
            return if (handTypeOfA == handTypeOfB) { // They have the same type, fallback to check the bigger label of the first different card
                val (aCard, bCard) = a.cards.zip(b.cards).first { it.first != it.second }
                if (cardStrength.indexOf(aCard) > cardStrength.indexOf(bCard)) {
                    -1
                } else {
                    1
                }
            } else {
                handTypeOfB.ordinal - handTypeOfA.ordinal
            }
        }
    }

    private data class Hand(val cards: String, val bid: Int, val handType: HandType = calculateHandType(cards), val jokerHandType: HandType = calculateHandTypeWithJoker(cards)) {

        companion object {
            fun calculateHandType(cards: String): HandType {
                val tmp = cards.groupBy { it }
                return when (tmp.size) {
                    1 -> HandType.FIVE_OF_A_KIND
                    2 -> when (tmp.map { it.value.size }.max()) {
                        4 -> HandType.FOUR_OF_A_KIND
                        else -> HandType.FULL_HOUSE
                    }

                    3 -> when (tmp.map { it.value.size }.max()) {
                        3 -> HandType.THREE_OF_A_KIND
                        else -> HandType.TWO_PAIR
                    }

                    4 -> HandType.ONE_PAIR
                    else -> HandType.HIGH_CARD
                }
            }

            fun calculateHandTypeWithJoker(cards: String): HandType {
                return if (!cards.contains('J')) {
                    calculateHandType(cards)
                } else {
                    val numberOfJokers = cards.count { it == 'J' }
                    val tmp = cards.filter { it != 'J' }.groupBy { it }
                    return when (tmp.size) {
                        0, 1 -> HandType.FIVE_OF_A_KIND
                        2 -> when (tmp.map { it.value.size }.max() + numberOfJokers) {
                            4 -> HandType.FOUR_OF_A_KIND
                            else -> HandType.FULL_HOUSE
                        }

                        3 -> when (tmp.map { it.value.size }.max() + numberOfJokers) {
                            3 -> HandType.THREE_OF_A_KIND
                            else -> HandType.TWO_PAIR
                        }

                        else -> HandType.ONE_PAIR
                    }
                }
            }
        }
    }
}
