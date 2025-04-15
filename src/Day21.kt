import java.io.File
import kotlin.math.abs

fun main() {
    fun readInput(filename: String): MutableList<String> {
        val input = mutableListOf<String>()
        File(filename).useLines { lines ->
            lines.forEach { input.add(it) }
        }
        return input
    }

    fun getCoordinates(key:Char, dirPad: Boolean = false): Pair<Int, Int> {
        val directionKeypad = listOf("X^A", "<v>")
        val numPad = listOf("789", "456", "123", "X0A")
        if (!dirPad) {
            for ((i, row) in numPad.withIndex()) {
                for ((j, col) in row.withIndex())
                    if (col == key) return Pair(i, j)
            }
        } else {
            for ((i, row) in directionKeypad.withIndex()) {
                for ((j, col) in row.withIndex())
                    if (col == key) return Pair(i, j)
            }
        }
        return (Pair(0,0))
    }

    fun findShortestPath(pos: Char, target: Char, dirPad: Boolean = false): String {
        val (curRow, curCol) = getCoordinates(pos, dirPad)
        val (tRow, tCol) = getCoordinates(target, dirPad)
        val outString = StringBuilder()
        val rowMoves = curRow - tRow
        val colMoves = curCol - tCol

        if (!dirPad) {
            // To avoid hitting the empty key, I first check if I am in the last row
            if (curRow == 3) {
                repeat(rowMoves) { outString.append("^") }
                repeat(abs(colMoves)) { if (colMoves > 0) outString.append("<") else outString.append(">")}
            } else {
                repeat(abs(colMoves)) { if (colMoves > 0) outString.append("<") else outString.append(">")}
                repeat(abs(rowMoves)) { if (rowMoves > 0) outString.append("^") else outString.append("v")}
            }
        } else {
            if (curRow == 0) {
                repeat(abs(rowMoves)) { outString.append("v") }
                repeat(abs(colMoves)) { if (colMoves > 0) outString.append("<") else outString.append(">") }
            } else {
                repeat(abs(colMoves)) { if (colMoves > 0) outString.append("<") else outString.append(">") }
                repeat(abs(rowMoves)) { outString.append("^") }
            }
        }
        return outString.toString()
    }

    fun part1(input: MutableList<String>): Int {
        var result = 0
        for (ins in input) {
            println(ins)
            var finalString = ""
            var curKeyPos = 'A'
            // I'll keep the sequence generators separate for debugging
            for (i in ins) {
                //println("I want to get to the $i and start at $curKeyPos")
                val pathToKey = findShortestPath(curKeyPos, i)
                //println(pathToKey)
                curKeyPos = i
                finalString += pathToKey + "A"
            }
            println(finalString)

            // Second robot
            var secondString = ""
            curKeyPos = 'A'
            for (c in finalString) {
                // println("I want to get to the $c and start at $curKeyPos")
                val pathToKey = findShortestPath(curKeyPos, c, true)
                // println(pathToKey)
                curKeyPos = c
                secondString += pathToKey + "A"
            }
            println(secondString)

            //Historian
            var historianString = ""
            curKeyPos = 'A'
            for (c in secondString) {
                // println("I want to get to the $c and start at $curKeyPos")
                val pathToKey = findShortestPath(curKeyPos, c, true)
                // println(pathToKey)
                curKeyPos = c
                historianString += pathToKey + "A"
            }

            println(historianString)
            println(historianString.length)
            println(ins.substringBefore("A").toInt())
            println(historianString.length * ins.substringBefore("A").toInt())
            result += historianString.length * ins.substringBefore("A").toInt()
        }


        return result
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Test if implementation meets criteria from the description, like:
    //val testInput = readInput("src/TestInput_Day21.txt")
    //part1(testInput).println()
    //check(part1(testInput) == 126384)

    val input = readInput("src/Input_Day21.txt")
    part1(input).println()

    /*
    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 1)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()

     */
}
