import java.io.File
import kotlin.time.measureTime

fun main() {
    fun readInput(filename: String): Pair<MutableList<MutableList<String>>, MutableList<String>> {
        val map = mutableListOf<MutableList<String>>()
        val instructions = mutableListOf<String>()
        var readMap = true
        File(filename).useLines { lines ->
            lines.forEach {
                if (readMap && it.isNotEmpty()) map.add(it.split("")
                    .filter { char -> char.isNotEmpty() }
                    .toMutableList())
                else if (it.isEmpty()) readMap = false
                else instructions.addAll(it.split("").filter { char -> char.isNotEmpty() })
            }
        }

        return Pair(map, instructions)
    }

    fun getRobot(map: MutableList<MutableList<String>>): Pair<Int, Int> {
        for ((rowNo, row) in map.withIndex()) {
            for ((colNo, tile) in row.withIndex()) {
                if (tile == "@") return Pair(rowNo, colNo)
            }
        }
        return Pair(999,999)
    }

    fun shiftTiles(map: MutableList<MutableList<String>>, direction: List<Int>, pos: List<Int>): Boolean {
        val nextPos = pos.zip(direction) { a, b -> a + b }
        if (map[nextPos[0]][nextPos[1]] == "#") {
            return false
        }
        if (map[nextPos[0]][nextPos[1]] == ".") {
            map[nextPos[0]][nextPos[1]] = "O"
            map[pos[0]][pos[1]] = "."
            return true
        } else if (map[nextPos[0]][nextPos[1]] == "O") {
            if (shiftTiles(map, direction, nextPos)) {
                map[nextPos[0]][nextPos[1]] = "O"
                map[pos[0]][pos[1]] = "."
                return true
            }
        }
        return false
    }

    fun moveRobot(map: MutableList<MutableList<String>>, instruction: String) {
        val robotPos = getRobot(map).toList().toMutableList()
        val direction: List<Int>
        direction = when (instruction) {
            "<" -> listOf(0, -1)
            ">" -> listOf(0, 1)
            "^" -> listOf(-1, 0)
            "v" -> listOf(1, 0)
            else -> listOf(0, 0)
        }

        val newRobotPos = robotPos.zip(direction) { a, b -> a + b}

        if (map[newRobotPos[0]][newRobotPos[1]] == "O") shiftTiles(map, direction, newRobotPos)

        if (map[newRobotPos[0]][newRobotPos[1]] == ".") {
            map[newRobotPos[0]][newRobotPos[1]] = "@"
            map[robotPos[0]][robotPos[1]] = "."
        }
    }

    fun calculateSum(map: MutableList<MutableList<String>>): Int {
        var sum = 0
        for ((rowNo, row) in map.withIndex()) {
            for ((cellNo, item) in row.withIndex()) {
                if (item == "O") {
                    sum += (rowNo * 100) + cellNo
                }
            }
        }
        return sum
    }

    fun part1(map: MutableList<MutableList<String>>, instructions: MutableList<String>): Int {
        for (instruct in instructions) {
            moveRobot(map, instruct)
        }
        return calculateSum(map)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val (map, instructions) = readInput("src/Input_Day15.txt")
    val timeTaken1 = measureTime { part1(map, instructions).println() }
    println("Part1: $timeTaken1")
    // part2(input).println()
}
