package day04

// https://adventofcode.com/2023/day/4

import Cell
import DayChallenge
import Direction
import Direction.*
import Grid
import getNonBlankFileLines
import toCells

fun main() = Day04Challenge.run()

object Day04Challenge: DayChallenge(
    day = "04",
    part1SampleResult = 18,
    part2SampleResult = 9
) {

    override fun runPart1(filePath: String) =
        LettersGrid(getNonBlankFileLines(filePath))
            .countXMAS()

    override fun runPart2(filePath: String) =
        LettersGrid(getNonBlankFileLines(filePath))
            .countMAS()
}

class LettersGrid(lines: List<String>) {
    private val grid = Grid(lines.toCells())

    fun countXMAS() : Long = grid.cells
        .filter { it.value == 'X' }
        .sumOf { findXMASFrom(it) }

    private fun findXMASFrom(cell: Cell): Long =
        Direction.entries.sumOf { dir -> findLetterInDirection(cell, 'X', dir) }

    private fun findLetterInDirection(cell: Cell, letter: Char, dir: Direction) : Long =
        when {
            cell.value != letter -> 0L
            letter == 'X' -> findLetterInNextDirection(cell, 'M', dir)
            letter == 'M' -> findLetterInNextDirection(cell, 'A', dir)
            letter == 'A' -> findLetterInNextDirection(cell, 'S', dir)
            letter == 'S' -> 1L
            else -> 0
        }

    private fun findLetterInNextDirection(cell: Cell, letter: Char, dir: Direction) : Long {
        val nextCell =  grid.cellInDirection(cell, dir) ?: return 0L
        return findLetterInDirection(nextCell, letter, dir)
    }

    fun countMAS() : Long = grid.cells
        .filter { it.value == 'A' }
        .sumOf { findMASFrom(it) }

    private fun findMASFrom(cell: Cell): Long =
        if (leftToRightMAS(cell) && rightToLeftMAS(cell)) 1 else 0

    private fun leftToRightMAS(cell: Cell): Boolean =
        cell.hasInDirection(UPLEFT, 'M', DOWNRIGHT, 'S') ||
                cell.hasInDirection(UPLEFT, 'S', DOWNRIGHT, 'M')

    private fun rightToLeftMAS(cell: Cell): Boolean =
        cell.hasInDirection(UPRIGHT, 'M', DOWNLEFT, 'S') ||
                cell.hasInDirection(UPRIGHT, 'S', DOWNLEFT, 'M')

    private fun Cell.hasInDirection(dir1: Direction, letter1: Char, dir2: Direction, letter2: Char) =
        this.hasInDirection(dir1, letter1) && this.hasInDirection(dir2, letter2)

    private fun Cell.hasInDirection(dir: Direction, letter: Char) =
        grid.cellInDirection(this, dir)?.value == letter
}
