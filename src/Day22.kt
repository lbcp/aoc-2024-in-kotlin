import java.io.File
import kotlin.math.round

fun main() {
    fun readInput(filename: String): MutableList<Long>{
        val input = mutableListOf<Long>()
        File(filename).useLines { lines ->
            lines.forEach {
                input.add(it.toLong())
            }
        }
        return input
    }

    fun mix(givenVal: Long, secretNo: Long): Long {
        return givenVal xor secretNo
    }

    fun prune(secretNo: Long):Long {
        return secretNo.mod(16777216).toLong()
    }

    fun part1(input: MutableList<Long>): Long{
        var result = 0L
        for (i in input) {
            // Secret Number evolves
            var secretNumber = i
            for (iter in 0 until 2000) {
                val firstOp = secretNumber * 64
                secretNumber = prune(mix(firstOp, secretNumber))
                val secOp = secretNumber / 32
                secretNumber = prune(mix(secOp, secretNumber))
                val thirdOp = secretNumber * 2048
                secretNumber = prune(mix(thirdOp, secretNumber))
            }
            result += secretNumber
        }
        return result
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("src/TestInput_Day22.txt")
    // Test if implementation meets criteria from the description, like:
    //val testInput = mutableListOf(123L)
    check(part1(testInput) == 37327623L)

    val input = readInput("src/Input_Day22.txt")
    part1(input).println()
    //part2(input).println()


}