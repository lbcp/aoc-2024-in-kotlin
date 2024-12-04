import java.io.File
import kotlin.math.abs

fun main() {
    fun readInput(filename: String): MutableList<List<String>>{
        val input = mutableListOf<List<String>>()
        File(filename).useLines { lines ->
            lines.forEach {
                input.add(it.split(""))
            }
        }
        return input
    }

    fun part1(input: MutableList<List<String>>): Int {
        // Search for first X, generate a list of all four-letter words from that X and count it.
        // I hope that will be easier to read than checking each letter individually. -> I was wrong...
        fun generateWords(row: Int, col: Int): List<String> {
            //TODO: This needs to be optimised. That doesn't feel right at all.
            val leftToRight = mutableListOf<String>()
            val rightToLeft = mutableListOf<String>()
            val topToBottom = mutableListOf<String>()
            val bottomToTop = mutableListOf<String>()
            val upLeftToRight = mutableListOf<String>()
            val downLeftToRight = mutableListOf<String>()
            val upRightToLeft = mutableListOf<String>()
            val downRightToLeft = mutableListOf<String>()
            for (count in 0..3) {
                leftToRight.add(input
                    .getOrElse(row) { listOf("Z")}
                    .getOrElse(col+count) { "Z" })
                rightToLeft.add(input
                    .getOrElse(row) { listOf("Z")}
                    .getOrElse(col-count) { "Z" })
                topToBottom.add(input
                    .getOrElse(row+count) { listOf("Z")}
                    .getOrElse(col) { "Z" })
                bottomToTop.add(input
                    .getOrElse(row-count) { listOf("Z")}
                    .getOrElse(col) { "Z" })
                upLeftToRight.add(input
                    .getOrElse(row-count) { listOf("Z")}
                    .getOrElse(col+count) { "Z" })
                downLeftToRight.add(input
                    .getOrElse(row+count) { listOf("Z")}
                    .getOrElse(col+count) { "Z" })
                upRightToLeft.add(input
                    .getOrElse(row-count) { listOf("Z")}
                    .getOrElse(col-count) { "Z" })
                downRightToLeft.add(input
                    .getOrElse(row+count) { listOf("Z")}
                    .getOrElse(col-count) { "Z" })
            }
            val wordList = listOf(leftToRight.joinToString(separator = ""),
                rightToLeft.joinToString(separator = ""),
                topToBottom.joinToString(separator = ""),
                bottomToTop.joinToString(separator = ""),
                upLeftToRight.joinToString(separator = ""),
                downLeftToRight.joinToString(separator = ""),
                upRightToLeft.joinToString(separator = ""),
                downRightToLeft.joinToString(separator = ""),
                )
            return wordList
        }

        val wordList = mutableListOf<String>()
        for (i in 0 until input.size) {
            for (j in 0 until input[i].size) {
                if (input[i][j] == "X") {
                    wordList += generateWords(i, j)
                }
            }
        }

        return wordList.count { it == "XMAS" }
    }

    fun part2(input: MutableList<List<String>>): Int {
        var result = 0
        for (i in 0 until input.size) {
            for (j in 0 until input[i].size) {
                if (input[i][j] == "A") {
                    // Here I only need to check two diagonals
                    val word1 = mutableListOf<String>()
                    word1.add(input.getOrElse(i-1) { listOf("Z")}.getOrElse(j-1) { "Z" })
                    word1.add(input.getOrElse(i) { listOf("Z")}.getOrElse(j) { "Z" })
                    word1.add(input.getOrElse(i+1) { listOf("Z")}.getOrElse(j+1) { "Z" })
                    val word2 = mutableListOf<String>()
                    word2.add(input.getOrElse(i+1) { listOf("Z")}.getOrElse(j-1) { "Z" })
                    word2.add(input.getOrElse(i) { listOf("Z")}.getOrElse(j) { "Z" })
                    word2.add(input.getOrElse(i-1) { listOf("Z")}.getOrElse(j+1) { "Z" })

                    if ((word1.joinToString(separator = "") == "MAS" || word1.joinToString(separator = "") == "SAM") &&
                        (word2.joinToString(separator = "") == "MAS" || word2.joinToString(separator = "") == "SAM"))
                        result += 1
                }
            }
        }
        return result
    }

    val input = readInput("src/Input_Day4.txt")

    println("Result part 1:")
    part1(input).println()
    println("Result part 2:")
    part2(input).println()
}
