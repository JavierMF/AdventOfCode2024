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
    private val numbers: List<BigInteger>

    init {
        val split = line.split(": ")
        expectedResult = split.first().toBigInteger()
        numbers = split.last().split(" ").map { it.toBigInteger() }
    }

    fun solveWithOps(validOps: Set<Operation>): Long =
        if (resultCanBeObtainedWithOps(validOps)) expectedResult.toLong()
        else 0L

    private fun resultCanBeObtainedWithOps(validOps: Set<Operation>) =
        validOps.any { operation ->
            eval(
                accValue = numbers[0],
                operation = operation,
                nextNumberIndex = 1,
                validOps
            )
        }

    private fun eval(accValue: BigInteger, operation: Operation, nextNumberIndex: Int, validOperations: Set<Operation>): Boolean {
        val newAccValue = operation.applyTo(accValue, numbers[nextNumberIndex])

        return when {
            nextNumberIndex.isLastIndex() -> newAccValue == expectedResult // End of recursion
            newAccValue > expectedResult -> false // Optimization
            else -> validOperations.any { eval(newAccValue, it, nextNumberIndex + 1, validOperations) }
        }
    }

    enum class Operation {
        ADD, MUL, CONCAT;

        fun applyTo(a: BigInteger, b: BigInteger): BigInteger =
            when (this) {
                ADD -> a + b
                MUL -> a * b
                CONCAT -> BigInteger.valueOf("$a$b".toLong())
            }
    }

    private fun Int.isLastIndex() = numbers.size -1 == this
}
