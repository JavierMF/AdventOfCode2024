package day08

// https://adventofcode.com/2023/day/8

import Cell
import Coords
import DayChallenge
import Grid
import elementPairs
import getNonBlankFileLines
import toCells
import kotlin.math.absoluteValue

fun main() = Day08Challenge.run()

object Day08Challenge: DayChallenge(
    day = "08",
    part1SampleResult = 14,
    part2SampleResult = 34
) {

    override fun runPart1(filePath: String): Long =
        AntennasMap(getNonBlankFileLines(filePath))
            .computeAntinodesSize()

    override fun runPart2(filePath: String): Long  {
        TODO("Not implemented")
    }
}

class AntennasMap(antennasList: List<String>) {
    private val maxX = antennasList.first().length - 1
    private val maxY = antennasList.size - 1
    private val antennas = Grid(antennasList.toCells(), maxX, maxY)

    fun computeAntinodesSize(): Long {
        val antennasByFrequency = antennas.cells.groupBy { it.value }

        return antennasByFrequency
            .flatMap { (_: Char, freqAntennas: List<Cell>) ->
                freqAntennas.elementPairs().flatMap { generateAntinodes(it) }
            }.toSet().size.toLong()
    }

    private fun Coords.distanceTo(that: Coords): Coords {
        val diffX = (this.x - that.x).absoluteValue
        val diffY = (this.y - that.y).absoluteValue

        return Coords(diffX, diffY)
    }

    private fun generateAntinodes(antennasPair: Pair<Cell, Cell>): List<Coords> {
        val coords1 = antennasPair.first.coords
        val coords2 = antennasPair.second.coords

        val distance = coords1.distanceTo(coords2)

        val dirCoord1X = if (coords1.x < coords2.x) -1 else 1
        val dirCoord1Y = if (coords1.y < coords2.y) -1 else 1

        val dirCoord2X = - dirCoord1X
        val dirCoord2Y = - dirCoord1Y

        val antinode1 = Coords(
            x = coords1.x + (dirCoord1X * distance.x),
            y = coords1.y + (dirCoord1Y * distance.y),
        )
        val antinode2 = Coords(
            x = coords2.x + (dirCoord2X * distance.x),
            y = coords2.y + (dirCoord2Y * distance.y),
        )

        return listOf(antinode1, antinode2).filter { antennas.coordsInGrid(it) }
    }

}
