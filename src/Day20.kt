import java.io.File
import kotlin.math.abs
import kotlin.time.measureTime

fun main() {
    fun readInput(filename: String): MutableList<MutableList<String>>{
        val input = mutableListOf<MutableList<String>>()
        File(filename).useLines { lines ->
            lines.forEach {
                input.add(it.split("")
                    .filter { char -> char.isNotEmpty() }
                    .toMutableList()
                )
            }
        }
        return input
    }

    fun generateWalkList(map: MutableList<MutableList<String>>): MutableList<Pair<Int, Int>> {
        var start = Pair(0, 0)
        var finish = Pair(0, 0)
        val path = mutableListOf<Pair<Int, Int>>()
        for ((rowNo, row) in map.withIndex()) {
            for ((colNo, tile) in row.withIndex()) {
                when (tile) {
                    "." -> path.add(Pair(rowNo, colNo))
                    "S" -> {
                        path.add(Pair(rowNo, colNo))
                        start = Pair(rowNo, colNo)
                    }
                    "E" -> {
                        path.add(Pair(rowNo, colNo))
                        finish = Pair(rowNo, colNo)
                    }
                }
            }
        }
        val walkedPath = mutableListOf<Pair<Int, Int>>()
        var curPos = Pair(start.first, start.second)
        while (curPos != finish) {
            walkedPath.add(curPos)
            path.remove(curPos)
            curPos = if (Pair(curPos.first + 1, curPos.second) in path) Pair(curPos.first + 1, curPos.second)
                        else if (Pair(curPos.first - 1, curPos.second) in path) Pair(curPos.first - 1, curPos.second)
                        else if (Pair(curPos.first, curPos.second + 1) in path) Pair(curPos.first, curPos.second + 1)
                        else if (Pair(curPos.first, curPos.second - 1) in path) Pair(curPos.first, curPos.second - 1)
                        else throw AssertionError("That should not happen. Check walk list")
        }
        walkedPath.add(finish)
        return walkedPath
    }

    fun checkShortcut(remainingPath: List<Pair<Int, Int>>): MutableMap<Int, Int> {
        val possibleShortcuts = mutableMapOf<Int, Int>() // key = saved time, value = number of appearances
        val curPos = remainingPath.first()
        val pathSize = remainingPath.size

        if (Pair(curPos.first + 2, curPos.second) in remainingPath) {
            val index = remainingPath.indexOf(Pair(curPos.first + 2, curPos.second))
            val restPath = remainingPath.subList(index, pathSize).size
            val difference = pathSize - 2 - restPath
            possibleShortcuts.getOrPut(difference) { 0 }
            possibleShortcuts[difference] = possibleShortcuts[difference]!! + 1
        }
        if (Pair(curPos.first - 2, curPos.second) in remainingPath) {
            val index = remainingPath.indexOf(Pair(curPos.first - 2, curPos.second))
            val restPath = remainingPath.subList(index, pathSize).size
            val difference = pathSize - 2 - restPath
            possibleShortcuts.getOrPut(difference) { 0 }
            possibleShortcuts[difference] = possibleShortcuts[difference]!! + 1
        }
        if (Pair(curPos.first, curPos.second + 2) in remainingPath) {
            val index = remainingPath.indexOf(Pair(curPos.first, curPos.second + 2))
            val restPath = remainingPath.subList(index, pathSize).size
            val difference = pathSize - 2 - restPath
            possibleShortcuts.getOrPut(difference) { 0 }
            possibleShortcuts[difference] = possibleShortcuts[difference]!! + 1
        }
        if (Pair(curPos.first, curPos.second - 2) in remainingPath) {
            val index = remainingPath.indexOf(Pair(curPos.first, curPos.second - 2))
            val restPath = remainingPath.subList(index, pathSize).size
            val difference = pathSize - 2 - restPath
            possibleShortcuts.getOrPut(difference) { 0 }
            possibleShortcuts[difference] = possibleShortcuts[difference]!! + 1
        }
        return possibleShortcuts
    }

    fun checkShortcut2(remainingPath: List<Pair<Int, Int>>): MutableMap<Int, Int> {
        val possibleShortcuts = mutableMapOf<Int, Int>() // key = saved time, value = number of appearances
        val curPos = remainingPath.first()
        val pathSize = remainingPath.size
        val reachableTiles = remainingPath.filter { (x, y) -> abs(x - curPos.first) + abs(y - curPos.second) <= 20 }
        for (tile in reachableTiles) {
            val index = remainingPath.indexOf(tile)
            val restPath = remainingPath.subList(index, pathSize).size
            val stepsNeeded = abs(tile.first - curPos.first) + abs(tile.second - curPos.second)
            val difference = pathSize - restPath - stepsNeeded
            possibleShortcuts.getOrPut(difference) { 0 }
            possibleShortcuts[difference] = possibleShortcuts[difference]!! + 1
        }
        return possibleShortcuts
    }

    fun part1(map: MutableList<MutableList<String>>): Int {
        // The idea is that I generate a List with the path. If I pass through a wall, I only have to check,
        // where in the list I am and then calculate the number of remaining tiles.
        val walkedPath = generateWalkList(map)
        val allShortcuts = mutableMapOf<Int, Int>()
        for ((index, pos) in walkedPath.withIndex()) {
            if (pos == walkedPath.last()) break
            val shortcuts = checkShortcut(walkedPath.subList(index, walkedPath.size))
            shortcuts.forEach { (key, value) -> allShortcuts.merge(key, value) {oldVal, newVal -> oldVal + newVal} }
        }

        val sum = allShortcuts.filter { (key, _) -> key >= 100 }.values
        return sum.sum()
    }

    fun part2(map: MutableList<MutableList<String>>): Int {
        // Part 2 theory.
        // I "only" need to find all tiles that are within 20 steps to all directions.
        // I could "draw" a diamond around my current position to isolate the reachable positions

        val walkedPath = generateWalkList(map)
        val allShortcuts = mutableMapOf<Int, Int>()
        for ((index, pos) in walkedPath.withIndex()) {
            if (pos == walkedPath.last()) break
            val shortcuts = checkShortcut2(walkedPath.subList(index, walkedPath.size))
            shortcuts.forEach { (key, value) -> allShortcuts.merge(key, value) {oldVal, newVal -> oldVal + newVal} }
        }

        val sum = allShortcuts.filter { (key, _) -> key >= 100 }.values
        return sum.sum()
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("src/TestInput_Day20.txt")
    println(testInput)
    check(part1(testInput) == 0)
    //check(part2(testInput) == 285) has to be disabled for the real Input

    val input = readInput("src/Input_Day20.txt")
    val timeTaken1 = measureTime { part1(input).println() }
    println("Part1 took: $timeTaken1")
    val timeTaken2 = measureTime { part2(input).println() }
    println("Part2 took: $timeTaken2")

}
