import java.io.File // ktlint-disable filename
import kotlin.system.exitProcess

fun getFileFromArgs(args: Array<String>): File {
    if (args.isEmpty()) {
        println("Input file path required")
        exitProcess(-1)
    }
    return getFileFromFilePath(args.first())
}

fun getFileFromFilePath(filePath: String): File {
    val file = File(filePath)
    if (!file.exists()) {
        println("Input file does not exist")
        exitProcess(-1)
    }
    return file
}

fun getNonBlankFileLines(filePath: String) =
    getFileFromFilePath(filePath)
        .readLines()
        .filterNot(String::isBlank)


fun getNonBlankFileLines(args: Array<String>) =
    getFileFromArgs(args)
        .readLines()
        .filterNot(String::isBlank)

fun getNonBlankFileLinesAsNumbersList(filePath: String): List<List<Long>> =
    getNonBlankFileLines(filePath)
        .map { it.split(" ").map { it.toLong() } }

fun interface LineMapper<T> {
    fun map(line: String): T
}

fun <T> getEntitiesByLine(args: Array<String>, mapper: LineMapper<T>): List<T> =
    getNonBlankFileLines(args).map(mapper::map)

fun <T> getEntitiesByLine(filePath: String, mapper: LineMapper<T>): List<T> =
    getNonBlankFileLines(filePath).map(mapper::map)

inline fun <reified T> T.printIt(): T = this.let { println(it); it }

fun <T> List<List<T>>.transpose(): List<List<T>> = List(first().size) { i -> List(size) { j -> this[j][i] } }

fun <T> List<T>.elementPairs(): Sequence<Pair<T, T>> = sequence {
    for(i in 0 until size-1)
        for(j in i+1 until size)
            yield(get(i) to get(j))
}
