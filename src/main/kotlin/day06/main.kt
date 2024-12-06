package day06

// https://adventofcode.com/2023/day/6

import Cell
import Coords
import DayChallenge
import Direction
import toCells
import Grid
import getNonBlankFileLines

fun main() = Day06Challenge.run()

object Day06Challenge: DayChallenge(
    day = "06",
    part1SampleResult = 41,
    part2SampleResult = 6
) {

    override fun runPart1(filePath: String): Long =
        LabGrid(getNonBlankFileLines(filePath)).let {
            it.moveGuard()
            it.countVisitedPositions()
        }

    override fun runPart2(filePath: String): Long  =
        LabGrid(getNonBlankFileLines(filePath)).let {
            it.moveGuard()
            it.countLoops()
        }
}

class LabGridCase(
    val cells: List<Cell>,
    val initialGuardPosition: Coords
) {
    private val grid = Grid(cells)
    var endedInLoop = false

    private var guardPosition = initialGuardPosition
    private var guardDirection = Direction.UP
    private val visitedCells = mutableSetOf<Pair<Coords, Direction>>()

    val visitedCoords get() = visitedCells.map { it.first }.toSet()

    fun moveGuardToExit() {
        while (grid.coordsInGrid(guardPosition)) {
            visitedCells.add(Pair(guardPosition, guardDirection))

            var nextGuardPos = guardPosition.moveTo(guardDirection)
            while (grid.hasCellInCoords(nextGuardPos)) {
                guardDirection = guardDirection.turnToRight()
                nextGuardPos = guardPosition.moveTo(guardDirection)
            }
            if (Pair(nextGuardPos, guardDirection) in visitedCells) {
                endedInLoop = true
                return
            }
            guardPosition = nextGuardPos
        }
    }

    fun countVisitedPositions() = visitedCoords.size.toLong()

    private fun Direction.turnToRight(): Direction =
        when(this) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
            else -> throw IllegalArgumentException("Invalid direction $this")
        }
}

class LabGrid(lines: List<String>) {
    private val grid: LabGridCase

    init {
        val cells = lines.toCells()
        grid = LabGridCase(
            cells = cells.filter { it.value == '#' },
            initialGuardPosition = cells.first { it.value == '^' }.coords
        )
    }

    fun moveGuard() = grid.moveGuardToExit()

    fun countLoops(): Long =
        grid.visitedCoords
            .filterNot { it == grid.initialGuardPosition }
            .count {
                val newLabGridCase = LabGridCase(
                    cells = grid.cells + Cell('#', it),
                    initialGuardPosition = grid.initialGuardPosition
                )
                newLabGridCase.moveGuardToExit()
                newLabGridCase.endedInLoop
            }.toLong()

    fun countVisitedPositions() = grid.countVisitedPositions()

}
