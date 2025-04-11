import java.io.File
import java.util.PriorityQueue
import kotlin.time.measureTime

class Node(val row: Int, val col: Int, val score: Int)

fun main() {
    fun readInput(filename: String): MutableList<List<Int>>{
        val input = mutableListOf<List<Int>>()
        File(filename).useLines { lines ->
            lines.forEach { line ->
                input.add(line.split(",")
                    .map { it.toInt() }  )
            }
        }

        return input
    }

    fun generateMap(input: MutableList<List<Int>>, testInput: Boolean = false, sliceSize: Int): MutableList<MutableList<String>> {
        val squareSize = if (testInput) 7 else 71
        val finalMap = MutableList(squareSize) { MutableList(squareSize) { "." } }

        input.slice(0..sliceSize).forEach {
            finalMap[it[1]][it[0]] = "#" }
        return finalMap
    }

    fun initCostMap(size: Int): MutableMap<List<Int>, Int> {
        val costMap = mutableMapOf<List<Int>, Int>()
        for (i in 0 until size) {
            for (j in 0 until size) {
                costMap[listOf(i, j)] = Int.MAX_VALUE
            }
        }

        return costMap
    }

    fun part1(map: MutableList<MutableList<String>>): Int {
        val costMap = initCostMap(map.size)
        val pathQueue = PriorityQueue<Node>(compareBy { it.score })
        val startNode = Node(0, 0, 0)

        costMap[listOf(0, 0)] = 0
        pathQueue.add(startNode)

        while (pathQueue.isNotEmpty()) {
            val curNode = pathQueue.poll()
            if (curNode.row == map.size - 1 && curNode.col == map.size - 1) {
                return curNode.score
            }

            listOf(
                Pair(curNode.row - 1, curNode.col),
                Pair(curNode.row + 1, curNode.col),
                Pair(curNode.row, curNode.col - 1),
                Pair(curNode.row, curNode.col + 1)
            ).forEach { (r, c) ->
                if (r in 0 until map.size && c in 0 until map.size && map[r][c] != "#") {
                    val newScore = curNode.score + 1
                    if (newScore < costMap[listOf(r, c)]!!) {
                        costMap[listOf(r, c)] = newScore
                        pathQueue.add(Node(r, c, newScore))
                    }
                }
            }
        }
        return -1
    }

    fun part2(input: MutableList<List<Int>>, testInput: Boolean = false): List<Int> {
        val initSize = if (testInput) 11 else 1023
        var range = (initSize until input.size).toList()
        var firstBlockBlocking = 0
        while (range.size > 1) {
            val index = range.size / 2
            val round = range[index]

            firstBlockBlocking = round
            val map = generateMap(input, testInput, round)
            range = if (part1(map) == -1) {
                range.subList(0, index)
            } else {
                range.subList(index + 1,  range.size)
            }
        }

        return input[firstBlockBlocking]
    }

    val testInput = readInput("src/TestInput_Day18.txt")
    val testMap = generateMap(testInput, testInput=true, 11)

    val timeTakenT1 = measureTime { check(part1(testMap) == 22) }
    println("Test 1 took: $timeTakenT1")
    part2(testInput, true)

    val input = readInput("src/Input_Day18.txt")
    val map = generateMap(input, false, 1023)
    val timeTaken1 = measureTime { println("Steps needed: ${part1(map)}") }
    println("Full task took: $timeTaken1")

    val timeTaken2 = measureTime { println("First blocking block: ${ part2(input) }") }
    println("Full task part 2 took: $timeTaken2")

}
