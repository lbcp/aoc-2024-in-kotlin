import java.io.File
import kotlin.math.abs

fun main() {
    fun part1(firstList: List<Int>, secondList: List<Int>): Int {
        var result = 0
        for (i in 0 until firstList.size) {
            result += abs(firstList[i] - secondList[i])
        }
        return result
    }

    fun part2(firstList: List<Int>, secondList: List<Int>): Int {
        var result = 0
        for (num in firstList) {
            val similarity = secondList.count { it == num} * num
            result += similarity
        }
        return result
    }

    val input = mutableListOf<MutableList<Int>>()
    File("src/Input_Day1.txt").useLines { lines ->
        lines.forEach {
            input.add(
                it.split("   ")
                .map { it.toInt() }
                .toMutableList())
        }
    }
    val firstList = input.map { it[0] }.sorted()
    val secondList = input.map { it[1] }.sorted()

    part1(firstList, secondList).println()
    part2(firstList, secondList).println()
}
