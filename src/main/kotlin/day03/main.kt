package day03

// https://adventofcode.com/2023/day/3

import DayChallenge
import getNonBlankFileLines

fun main() = Day03Challenge.run()

object Day03Challenge: DayChallenge(
    day = "03",
    part1SampleResult = 161,
    part2SampleResult = 48
) {

    private const val mulRegexStr = """mul\((?<n1>\d+),(?<n2>\d+)\)"""
    private const val doRegexStr = """do\(\)"""
    private const val dontRegexStr = """don't\(\)"""

    private val part1Regex = mulRegexStr.toRegex()
    private val part2Regex = """$doRegexStr|$dontRegexStr|$mulRegexStr""".toRegex()

    override fun runPart1(filePath: String): Long =
        getNonBlankFileLines(filePath)
            .flatMap { line ->
                part1Regex.findAll(line)
                    .map { it.getMultipliedValues() }
            }.sum()

    override fun runPart2(filePath: String): Long {
        var enabled = true
        return getNonBlankFileLines(filePath)
            .flatMap { line ->
                part2Regex.findAll(line).map {
                    when {
                        it.isDont() -> { enabled = false; 0L }
                        it.isDo() -> { enabled = true; 0L }
                        else -> if (enabled) it.getMultipliedValues() else 0L
                    }
                }
            }.sum()
    }

    private fun MatchResult.getLongValue(name: String) =
        this.groups[name]?.value?.toLong() ?: 1L

    private fun MatchResult.isDo() = this.value == "do()"
    private fun MatchResult.isDont() = this.value == "don't()"
    private fun MatchResult.getMultipliedValues() =
        this.getLongValue("n1") * this.getLongValue("n2")

}
