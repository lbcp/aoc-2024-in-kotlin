import java.io.File
import java.util.PriorityQueue
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


    class Task(private val functionInitializer: () -> Unit, val score: Int) {
        val functionCall: Unit by lazy { functionInitializer() }
    }


    fun part1(map: MutableList<MutableList<String>>): Int {
        // I'll use some kind of poor mens Dijkstra algorithm
        // Since the direction makes a difference I generated a rather expressive cost map for the nodes.
        val costMap = mapOf<String, MutableMap<List<Int>, Int>>(
            "north" to mutableMapOf(),
            "south" to mutableMapOf(),
            "west" to mutableMapOf(),
            "east" to mutableMapOf(),
        )

        var bestScore: Int = Int.MAX_VALUE
        val pathsFound = mutableMapOf<Int, MutableList<MutableList<List<Int>>>>()
        val pathQueue = PriorityQueue<Task>(compareBy { it.score })

        fun stepForward(
            map: MutableList<MutableList<String>>, pos: List<Int>, direction: String,
            pathTaken: MutableList<List<Int>>, score: Int
        ) {
            val curPath = pathTaken.toMutableList()
            val row = pos[0]
            val col = pos[1]
            val directionMap = mapOf(
                "north" to listOf(row-1, col),
                "south" to listOf(row + 1, col),
                "west" to listOf(row, col - 1),
                "east" to listOf(row, col + 1))
            curPath.add(pos)

            // Exit conditions
            if (map[row][col] == "E") {
                if (bestScore >= score) {
                    bestScore = score
                    val scorePath = pathsFound.getOrPut(score) { mutableListOf() }
                    scorePath.add(curPath)
                }
                return
            }
            if (costMap[direction]!![listOf(row, col)]!! < score ) {
                return
            } else if (costMap[direction]!![listOf(row, col)]!! >= score ) {
                costMap[direction]?.set(listOf(row, col), score)
            }

            val possibleDirections = mutableListOf<String>()
            if (listOf(row - 1, col) !in pathTaken && map[row - 1][col] == "." || map[row - 1][col] == "E") {
                possibleDirections.add("north")
            }
            if (listOf(row, col + 1) !in pathTaken && map[row][col + 1] == "." || map[row][col + 1] == "E") {
                possibleDirections.add("east")
            }
            if (listOf(row + 1, col) !in pathTaken && map[row + 1][col] == "." || map[row + 1][col] == "E") {
                possibleDirections.add("south")
            }
            if (listOf(row, col - 1) !in pathTaken && map[row][col - 1] == "." || map[row][col - 1] == "E") {
                possibleDirections.add("west")
            }


            if (direction in possibleDirections) {
                val newScore = score + 1
                pathQueue.add(Task(functionInitializer = {
                                        stepForward(map,directionMap[direction]!!,direction, curPath, newScore)
                                    }, newScore))
                possibleDirections.remove(direction)
            }
            for (dir in possibleDirections) {
                val newScore = score + 1001
                pathQueue.add(Task(functionInitializer = {
                                        stepForward(map,directionMap[dir]!!, dir, curPath, newScore)
                                     }, newScore))
            }
        }

        var startPos = listOf<Int>()
        for ((rowNo, row) in map.withIndex()) {
            for ((colNo, tile) in row.withIndex()) {
                costMap["north"]!![listOf(rowNo, colNo)] = Int.MAX_VALUE
                costMap["south"]!![listOf(rowNo, colNo)] = Int.MAX_VALUE
                costMap["west"]!![listOf(rowNo, colNo)] = Int.MAX_VALUE
                costMap["east"]!![listOf(rowNo, colNo)] = Int.MAX_VALUE
                if (tile == "S") {
                    startPos = listOf(rowNo, colNo)
                    costMap["east"]!![listOf(rowNo, colNo)] = 0
                }
            }
        }

        val initTask = Task(functionInitializer = {
                stepForward(
                    map,
                    startPos,
                    "east",
                    mutableListOf(),
                    0)
             }, 0)
        pathQueue.add(initTask)

        while (pathQueue.isNotEmpty()) {
            if (pathQueue.peek().score > bestScore) break
            val runTask = pathQueue.poll() ?: break
            runTask.functionCall
        }

        val bestTiles = mutableSetOf<List<Int>>()
        pathsFound[bestScore]!!.forEach { bestTiles.addAll(it) }
        println("Number of best tiles in Maze: ${bestTiles.size}")
        return bestScore
    }

    val testInput = readInput("src/TestInput_Day16.txt")
    val timeTakenT1 = measureTime { check(part1(testInput) == 7036) }
    println("Test 1 took: $timeTakenT1")

    val testInput2 = readInput("src/TestInput_Day16_2.txt")
    val timeTakenT2 = measureTime { check(part1(testInput2) == 11048) }
    println("Test 2 took: $timeTakenT2")

    val input = readInput("src/Input_Day16.txt")
    val timeTaken1 = measureTime { part1(input).println() }
    println("Full task took: $timeTaken1")

}
