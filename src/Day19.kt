import java.io.File

fun main() {
    fun readInput(filename: String): MutableList<List<String>>{
        val input = mutableListOf<List<String>>()
        var firstLine = true
        File(filename).useLines { lines ->
            val stripes = mutableListOf<String>()
            lines.forEach {
                if (firstLine) {
                    input.add(it.split(", "))
                    firstLine = false
                } else {
                    if (it.isNotEmpty()) stripes.add(it)
                }
            }
            input.add(stripes.toList())
        }
        return input
    }

    fun part1(input: MutableList<List<String>>): Int {
        fun checkPossible(pattern: String, towels: List<String>): Boolean {
            if (pattern.isEmpty()) {
                return true
            }
            for (towel in towels) {
                val towelSize = towel.length
                if (pattern.length >= towelSize && towel == pattern.substring(0 until towelSize)){
                    if (checkPossible(pattern.substring(towelSize), towels)) return true
                }
            }
            return false
        }

        val towels = input[0]
        val stripes = input[1]
        var result = 0
        for (pattern in stripes) {
            if (checkPossible(pattern, towels)) {
                result++
            } else println("Patter could not be matched: $pattern" )
        }

        return result
    }

    fun part2(input: MutableList<List<String>>): Int {
        fun checkPossible(pattern: String, towels: List<String>): Int {
            if (pattern.isEmpty()) {
                return 1
            }
            var combos = 0
            for (towel in towels) {
                val towelSize = towel.length
                if (pattern.length >= towelSize && towel == pattern.substring(0 until towelSize)){
                    combos += (checkPossible(pattern.substring(towelSize), towels))
                }
            }
            return combos
        }

        val towels = input[0]
        val stripes = input[1]
        var result = 0
        for ((index, pattern) in stripes.withIndex()) {
            println("${(index.toFloat() / stripes.size) * 100}% completed.")
            result +=checkPossible(pattern, towels)
        }
        return result
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("src/TestInput_Day19.txt")
    println(testInput)
    check(part1(testInput) == 6)
    check(part2(testInput) == 16)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("src/Input_Day19.txt")
    //part1(input).println()
    part2(input).println()
}
