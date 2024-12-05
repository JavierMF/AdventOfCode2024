package day05

// https://adventofcode.com/2023/day/5

import DayChallenge
import getFileFromFilePath

fun main() = Day05Challenge.run()

object Day05Challenge: DayChallenge(
    day = "05",
    part1SampleResult = 143,
    part2SampleResult = 123
) {

    override fun runPart1(filePath: String): Long =
        problemParser(filePath)
            .validUpdates()
            .sumOf { it.middleValue().toLong() }

    override fun runPart2(filePath: String): Long {
        val problem = problemParser(filePath)
        return problem
            .invalidUpdates()
            .map { problem.fixInvalidUpdate(it) }
            .sumOf { it.middleValue().toLong() }
    }

    private fun List<Int>.middleValue(): Int = this[this.size/2]

    private fun problemParser(filePath: String): PrintingProblem {
        var readingRules = true
        val rules = mutableSetOf<Pair<Int, Int>>()
        val updates = mutableListOf<List<Int>>()
        getFileFromFilePath(filePath)
            .readLines()
            .forEach { line ->
                when {
                    line.isEmpty() -> readingRules = false
                    readingRules -> line.split("|")
                        .let { rules.add(Pair(it.first().toInt(),it.last().toInt())) }
                    else -> updates.add(line.split(",").map { it.toInt() })
                }
            }
        return PrintingProblem(rules, updates)
    }

}

class PrintingProblem(
    private val rules: Set<Pair<Int, Int>>,
    private val updates: List<List<Int>>
) {
    fun validUpdates() = updates.filter { it.isValid() }
    fun invalidUpdates() = updates.filter { !it.isValid() }

    fun fixInvalidUpdate(invalidUpdate: List<Int>): List<Int> =
        invalidUpdate.sortedWith(comparator)

    private fun List<Int>.isValid(): Boolean =
        rules.all { (key, value) ->
            val prev = this.indexOf(key)
            val next = this.indexOf(value)

            if (prev == -1 || next == -1) return@all true
            else prev < next
        }

    private val comparator = Comparator { val1: Int, val2: Int ->
        when {
            Pair(val1, val2) in rules -> -1
            Pair(val2, val1) in rules -> 1
            else -> 0
        }
    }
}
