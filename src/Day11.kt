import java.io.File
import kotlin.time.measureTime

fun main() {
    fun readInput(filename: String): List<Long> {
        val input = File(filename).readText().split(" ").map { it.toLong() }
        return input
    }

    fun part1(input: List<Long>): Int {
        fun applyRules(stones: List<Long>): List<Long> {
            val newList = mutableListOf<Long>()
            for (s in stones) {
                when {
                    s == 0.toLong() -> newList.add(1)
                    s.toString().length % 2 == 0 -> {
                        val half = s.toString().length / 2
                        newList.add(s.toString().substring(0 until half).toLong())
                        newList.add(s.toString().substring(half).toLong())
                    }
                    else -> newList.add(s * 2024)
                }
            }
            return newList.toList()
        }
        var result = input

        repeat(25) {
            result = applyRules(result)
        }

        return result.size
    }

    fun part2(stones: List<Long>): Long {
        var currentStones = stones.map { it to 1L }.toMutableList()

        repeat(75) {
            val nextStones = mutableListOf<Pair<Long, Long>>()

            for ((s, count) in currentStones) {
                when {
                    s == 0L -> nextStones.add(1L to count)
                    s.toString().length % 2 == 0 -> {
                        val half = s.toString().length / 2
                        val left = s.toString().substring(0, half).toLong()
                        val right = s.toString().substring(half).toLong()
                        nextStones.add(left to count)
                        nextStones.add(right to count)
                    }
                    else -> nextStones.add(s * 2024 to count)
                }
            }

            currentStones = nextStones.groupBy({ it.first }, { it.second })
                .map { (stone, counts) -> stone to counts.sum() }
                .toMutableList()
        }

        return currentStones.sumOf { it.second }
    }

    val input = readInput("src/Input_Day11.txt")
    println(input)

    val timeTaken1 = measureTime { part1(input).println() }
    val timeTaken2 = measureTime { part2(input).println() }
    println("Part1: $timeTaken1")
    println("Part2: $timeTaken2")
}
