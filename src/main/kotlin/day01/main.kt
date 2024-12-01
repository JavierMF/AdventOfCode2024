package day01

// https://adventofcode.com/2024/day/1

import DayChallenge
import getNonBlankFileLines
import transpose
import kotlin.math.absoluteValue

fun main() = Day01Challenge.run()

object Day01Challenge: DayChallenge(
    day = "01",
    part1SampleResult = 11,
    part2SampleResult = 31
) {

    override fun runPart1(filePath: String): Long {
        val (list1, list2) = readNumbersLists(filePath)
        return list1.sorted()
            .zip(list2.sorted())
            .sumOf { (a, b) -> (a - b).absoluteValue }
    }

    override fun runPart2(filePath: String): Long {
        val (list1, list2) = readNumbersLists(filePath)
        return list1.sumOf { aNumber ->
            val repetitions = list2.count { it == aNumber }
            aNumber * repetitions
        }
    }

    private fun readNumbersLists(filePath: String): Pair<List<Long>, List<Long>> {
        val listOfLists = getNonBlankFileLines(filePath)
            .map { line ->
                val split = line.split(" ")
                listOf(split.first().toLong(), split.last().toLong())
            }.transpose()
        return Pair(listOfLists.first(), listOfLists[1])
    }
}
