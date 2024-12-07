package day07

// https://adventofcode.com/2023/day/7

import DayChallenge
import day07.CalibrationEquation.Operation.*
import getNonBlankFileLines
import java.math.BigInteger

fun main() = Day07Challenge.run()

object Day07Challenge: DayChallenge(
    day = "07",
    part1SampleResult = 3749,
    part2SampleResult = 11387
) {

    override fun runPart1(filePath: String): Long =
        getNonBlankFileLines(filePath)
            .map { CalibrationEquation(it) }
            .sumOf { it.solveWithOps(setOf(MUL, ADD)) }

    override fun runPart2(filePath: String): Long  =
        getNonBlankFileLines(filePath)
            .map { CalibrationEquation(it) }
            .sumOf { it.solveWithOps(setOf(MUL, ADD, CONCAT)) }
}

class CalibrationEquation(line: String) {
    private val expectedResult: BigInteger
    private val numbers: List<Long>

    init {
        val split = line.split(": ")
        expectedResult = split.first().toBigInteger()
        numbers = split.last().split(" ").map { it.toLong() }
    }

    fun part1(): Long = solveWithOps(setOf(MUL, ADD))
    fun part2(): Long = solveWithOps(setOf(MUL, ADD, CONCAT))

    fun solveWithOps(validOps: Set<Operation>): Long =
        if (
            validOps.any {
                eval(BigInteger.valueOf(numbers[0]), it, 1, validOps)
            }
        ) expectedResult.toLong()
        else 0L

    private fun eval(accValue: BigInteger, operation: Operation, nextNumberIndex: Int, valOps: Set<Operation>): Boolean {
        val newAccValue = when (operation) {
            ADD -> accValue + BigInteger.valueOf(numbers[nextNumberIndex])
            MUL -> accValue * BigInteger.valueOf(numbers[nextNumberIndex])
            CONCAT -> BigInteger.valueOf("$accValue${numbers[nextNumberIndex]}".toLong())
        }

        return if (numbers.size == (nextNumberIndex + 1)) {
            newAccValue == expectedResult
        } else if (newAccValue > expectedResult) {
            false
        } else {
            valOps.any {
                eval(newAccValue, it, nextNumberIndex + 1, valOps)
            }
        }
    }

    enum class Operation { ADD, MUL, CONCAT }
}
