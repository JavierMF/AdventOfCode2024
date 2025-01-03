import kotlin.math.abs

data class Cell(val value: Char, val coords: Coords) {
    val isSymbol = value !in '0' .. '9'
}

fun List<String>.toCells(mapper: (Char, Int, Int) -> Cell? = defaultCellMapper) =
    this.flatMapIndexed { rowIndex, line ->
        line.mapIndexedNotNull { colIndex, char -> mapper(char, colIndex, rowIndex) }
    }

val defaultCellMapper = { c: Char, x: Int, y: Int ->
    when (c) {
        '.' -> null
        else -> Cell(c, Coords(x, y))
    }
}

data class Coords(val x: Int, val y: Int) {

    fun neighbours(): Set<Coords> = range(x)
        .flatMap { posX ->
            range(y).map { posY -> Coords(posX, posY) }
                .filter { it != this }
        }.toSet()

    fun verticalNeighbours(): Set<Coords> = setOf(
            this.moveTo(Direction.UP),
            this.moveTo(Direction.DOWN),
            this.moveTo(Direction.LEFT),
            this.moveTo(Direction.RIGHT),
    )

    private fun range(i: Int): Set<Int> = (i - 1 .. i + 1).toSet()

    fun neighboursInGrid(maxX: Int, maxY: Int): Set<Coords> = rangeInBoundary(x, maxX)
        .flatMap { posX ->
            rangeInBoundary(y, maxY).map { posY -> Coords(posX, posY) }
                .filter { it != this }
        }.toSet()

    private fun rangeInBoundary(i: Int, max: Int): Set<Int> = (i - 1..i + 1)
        .filter { it in 0..max }.toSet()

    fun rightCoord(): Coords = moveTo(Direction.RIGHT)
    fun leftCoord(): Coords = moveTo(Direction.LEFT)
    fun upCoord(): Coords = moveTo(Direction.UP)
    fun downCoord(): Coords = moveTo(Direction.DOWN)
    fun moveTo(dir: Direction): Coords = Coords(x + dir.x, y + dir.y)

    fun distanceTaxicabTo(other: Coords): Int = abs(x - other.x) + abs(y - other.y)
}

enum class Direction(val x: Int, val y: Int, val char:Char) {
    UP(0, -1, '↑'),
    UPRIGHT(1, -1, '/'),
    UPLEFT(-1, -1, '\\'),
    DOWN(0, 1, '↓'),
    DOWNRIGHT(1, 1, '\\'),
    DOWNLEFT(-1, 1, '/'),
    RIGHT(1, 0, '→'),
    LEFT(-1, 0, '←');

    fun opposite(): Direction =
            when(this) {
                UP -> DOWN
                DOWN -> UP
                LEFT -> RIGHT
                RIGHT -> LEFT
                UPRIGHT -> DOWNLEFT
                UPLEFT -> DOWNRIGHT
                DOWNRIGHT -> UPLEFT
                DOWNLEFT -> UPRIGHT
            }
}

data class Grid(
    val cells: List<Cell>,
    private val initMaxX: Int? = null,
    private val initMaxY: Int? = null,
) {
    private val cellsByCoords = cells.associateBy { it.coords }

    val maxX = initMaxX ?: cells.map { it.coords.x }.max()
    val maxY = initMaxY ?: cells.map { it.coords.y }.max()

    fun cellNeighbours(cell: Cell) = cell.coords.neighboursInGrid(maxX, maxY).mapNotNull { cellInCoords((it)) }
    fun cellInDirection(cell: Cell, dir: Direction) = cellInCoords(cell.coords.moveTo(dir))

    fun cellInCoords(coords: Coords): Cell? = cellsByCoords[coords]

    fun hasCellInCoords(coords: Coords) = cellInCoords(coords) != null

    fun coordsInGrid(coords: Coords) = coords.x in 0..maxX  && coords.y in 0 .. maxY

    fun allCoords(): Set<Coords> = (0 .. maxX)
        .flatMap { x -> (0 .. maxY).map { y -> Coords(x, y) } }
        .toSet()
}
