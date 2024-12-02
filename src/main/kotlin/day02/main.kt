package day02

// https://adventofcode.com/2023/day/2

import DayChallenge
import getNonBlankFileLinesAsNumbersList
import kotlin.math.absoluteValue

fun main() = Day02Challenge.run()

object Day02Challenge: DayChallenge(
    day = "02",
    part1SampleResult = 2,
    part2SampleResult = 4,
) {

    override fun runPart1(filePath: String): Long =
        getNonBlankFileLinesAsNumbersList(filePath)
            .count { it.isSafe() }.toLong()

    override fun runPart2(filePath: String): Long =
        getNonBlankFileLinesAsNumbersList(filePath)
            .count { it.isSafe2() }.toLong()

    private fun List<Long>.isSafe() =
        this.reports().count { !it } == 0

    private fun  List<Long>.isSafe2(): Boolean {
        if (this.isSafe()) return true

        return this.asSequence().mapIndexed { index, _ ->
            this.withoutItemAt(index).isSafe()
        }.firstOrNull { it } != null
    }

    private fun <L> Iterable<L>.withoutItemAt(index: Int)
            = filterIndexed{ i, _ -> i != index }

    private fun List<Long>.reports(): List<Boolean> {
        val sequenceIncrease: Boolean = this[0] < this[1]

        return this.mapIndexed { index, number ->
            if (index == 0) return@mapIndexed true
            val previous = this[index - 1]

            val thisPairIncrease = previous < number

            if (sequenceIncrease != thisPairIncrease) return@mapIndexed false
            else (number - previous).absoluteValue in 1..3
        }
    }
}

