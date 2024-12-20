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

    fun part1(map: MutableList<MutableList<String>>): Int {
        // It takes too much time.
        // I think I should implement a priority queue that adds 1000 to each turn, so that I do not have to get
        // every route.
        val roads = mutableMapOf<Int, Pair<MutableList<List<Int>>, Int>>()

        fun stepForward(
            map: MutableList<MutableList<String>>, pos: List<Int>, direction: String,
            pathTaken: MutableList<List<Int>>, turns: Int
        ) {
            val curPath = pathTaken.toMutableList()
            val row = pos[0]
            val col = pos[1]
            val curIndex = roads.size
            if (map[row][col] == "E") roads[curIndex] = Pair(curPath, turns)

            curPath.add(pos)
            if (listOf(row - 1, col) !in pathTaken && map[row - 1][col] == "." || map[row - 1][col] == "E") {
                var newTurns = turns
                if (direction != "north") newTurns += 1
                stepForward(map, listOf(row - 1, col), "north", curPath, newTurns)
            }
            if (listOf(row + 1, col) !in pathTaken && map[row + 1][col] == "." || map[row + 1][col] == "E") {
                var newTurns = turns
                if (direction != "south") newTurns += 1
                stepForward(map, listOf(row + 1, col), "south", curPath, newTurns)
            }
            if (listOf(row, col - 1) !in pathTaken && map[row][col - 1] == "." || map[row][col - 1] == "E") {
                var newTurns = turns
                if (direction != "west") newTurns += 1
                stepForward(map, listOf(row, col - 1), "west", curPath, newTurns)
            }
            if (listOf(row, col + 1) !in pathTaken && map[row][col + 1] == "." || map[row][col + 1] == "E") {
                var newTurns = turns
                if (direction != "east") newTurns += 1
                stepForward(map, listOf(row, col + 1), "east", curPath, newTurns)
            }
        }
        // The idea is that I generate a List with the path. If I pass through a wall, I only have to check,
        // where in the list I am and then calculate the number of remaining tiles.
        var startPos = listOf<Int>()
        for ((rowNo, row) in map.withIndex()) {
            for ((colNo, tile) in row.withIndex()) {
                if (tile == "S") startPos = listOf(rowNo, colNo)
            }
        }
        stepForward(map, startPos, "east", mutableListOf<List<Int>>(), 0)
        var scores = mutableListOf<Int>()
        roads.forEach { (key, value) ->
            scores.add(value.first.size - 1 + (value.second * 1000))
        }
        println(scores.sorted())
        return scores.min()
    }

    fun part2(map: MutableList<MutableList<String>>): Int {
        // Part 2 theory.
        // I "only" need to find all tiles that are within 20 steps to all directions.
        // I could "draw" a diamond around my current position to isolate the reachable positions

        return 0
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("src/TestInput_Day16.txt")
    val timeTakenT1 = measureTime {  check(part1(testInput) == 7036) }

    val testInput2 = readInput("src/TestInput_Day16_2.txt")
    val timeTakenT2 = measureTime { check(part1(testInput2) == 11048) }
    println("PartT1 took: $timeTakenT1")
    println("PartT2 took: $timeTakenT2")

    val input = readInput("src/Input_Day16.txt")
    val timeTaken1 = measureTime { part1(input).println() }
    println("Part1 took: $timeTaken1")
    /*
    val timeTaken2 = measureTime { part2(input).println() }
    println("Part2 took: $timeTaken2")
     */
}
