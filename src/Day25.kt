import java.io.File
import kotlin.time.measureTime

fun main() {
    fun readInput(filename: String): Pair<MutableList<MutableList<String>>, MutableList<MutableList<String>>> {
        val locks = mutableListOf<MutableList<String>>()
        val keys = mutableListOf<MutableList<String>>()
        val inputs = mutableListOf<MutableList<String>>()

        File(filename).useLines { lines ->
            var tempInput = mutableListOf<String>()
            lines.forEach {
                if (it.isEmpty()) {
                    inputs.add(tempInput)
                    tempInput = mutableListOf<String>()
                } else {
                    tempInput.add(it)
                }
            }
            inputs.add(tempInput)
        }
        println(inputs)

        inputs.forEach {
            if (it[0].contains(".")) keys.add(it) else locks.add(it) }

        return Pair(keys, locks)
    }

    fun part1(keys: MutableList<MutableList<String>>, locks: MutableList<MutableList<String>>): Int {
        // I assume no empty space in each lock or key
        val keyLengths = mutableListOf<MutableList<Int>>()
        val lockLengths = mutableListOf<MutableList<Int>>()

        keys.forEach { key ->
            val keyLength = MutableList(key[0].length) {  key.size - 1}
            for (row in key) {
                for ((j, col) in row.withIndex()) {
                    if (col == '.') {
                        keyLength[j] -= 1
                    }
                }
            }
            keyLengths.add(keyLength)
        }

        locks.forEach { lock ->
            val lockLength = MutableList(lock[0].length) {  lock.size -1 }
            for (row in lock) {
                for ((j, col) in row.withIndex()) {
                    if (col == '.') {
                        lockLength[j] -= 1
                    }
                }
            }
            lockLengths.add(lockLength)
        }

        var result = 0
        keyLengths.forEach { key ->
            lockLengths.forEach {
                if (key.zip(it) { a, b -> a + b}
                    .all {number -> number <= 5} ) result += 1
            }
        }

        return result
    }

    fun part2(input: List<Long>): Int {
        // Todo: Finish part two
        return 0
    }

    val (keys, locks) = readInput("src/Input_Day25.txt")

    // part1(keys, locks).println()
    val timeTaken1 = measureTime { part1(keys, locks).println() }
    println("Part1: $timeTaken1")

    // val timeTaken2 = measureTime { part2(input).println() }
    // println("Part2: $timeTaken2")
}
