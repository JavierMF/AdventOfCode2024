package day01

// https://adventofcode.com/2024/day/1

import DayChallenge
import getNonBlankFileLines
import kotlin.math.abs

fun main() = Day01Challenge.run()

object Day01Challenge: DayChallenge(
    day = "01",
    part1SampleResult = 11,
    part2SampleResult = 31
) {

    override fun runPart1(filePath: String): Long {
        val (list1, list2) = readNumbersLists(filePath)
        return List(list1.size) { idx ->
            abs(list1[idx] - list2[idx])
        }.sum()
    }

    override fun runPart2(filePath: String): Long {
        val (list1, list2) = readNumbersLists(filePath)
        return list1.sumOf { aNumber ->
            val repetitions = list2.count { it == aNumber }
            aNumber * repetitions
        }
    }

    private fun readNumbersLists(filePath: String): Pair<MutableList<Long>, MutableList<Long>> {
        val list1 = mutableListOf<Long>()
        val list2 = mutableListOf<Long>()
        getNonBlankFileLines(filePath)
            .map { line ->
                val split = line.split(" ")
                list1.add(split.first().toLong())
                list2.add(split.last().toLong())
            }
        return Pair(list1, list2)
    }
}
