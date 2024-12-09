package day09

// https://adventofcode.com/2024/day/9

import DayChallenge
import getNonBlankFileLines

fun main() = Day09Challenge.run()

object Day09Challenge: DayChallenge(
    day = "09",
    part1SampleResult = 1928,
    part2SampleResult = 2858
) {

    override fun runPart1(filePath: String): Long {
        val hardDisk = getNonBlankFileLines(filePath).readHardDisk()

        var reverseCounter = hardDisk.size - 1
        val compacted = hardDisk.mapIndexedNotNull { index, n ->
            when {
                index > reverseCounter -> null
                n == -1 -> {
                   while(hardDisk[reverseCounter] == -1) reverseCounter -=1
                   reverseCounter -= 1
                    if (index > (reverseCounter + 1)) null else hardDisk[reverseCounter + 1]
                }
                else -> n
            }
        }
        return compacted.mapIndexed { index, c -> (index * c).toLong() }.sum()
    }

    override fun runPart2(filePath: String): Long  {
        TODO("Not implemented")
    }

    private fun List<String>.readHardDisk(): List<Int> {
        var currentFileName = -1
        return this.flatMap { line ->
                line.flatMapIndexed { index, s ->
                    when (index % 2) {
                        0 -> {
                            currentFileName += 1
                            List(s.digitToInt()) { currentFileName }
                        }

                        1 -> {
                            List(s.digitToInt()) { -1 }
                        }

                        else -> throw IllegalArgumentException()
                    }
                }
            }
    }
}
