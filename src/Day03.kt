import java.io.File

fun main() {
    fun readInput(filename: String): String {
        val input = File(filename).readText()
        return input
    }

    fun part1(input: String): Int {
        val matcher = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
        val statements = matcher.findAll(input)

        var result = 0
        for (i in statements) {
            val match = i.value.replace("mul(", "")
                .replace(")", "")
                .split(",")
                .map {it.toInt()}

            result += match[0]*match[1]
        }
        return result
    }

    fun part2(input: String): Int {
        val matcher = """(mul\(\d{1,3},\d{1,3}\))|(do\(\))|(don't\(\))""".toRegex()
        val statements = matcher.findAll(input)

        var result = 0
        var enabled = true
        for (i in statements) {
            if (i.value == "don't()") {
                enabled = false
                continue
            } else if (i.value == "do()") {
                enabled = true
                continue
            } else if (!enabled) continue
            val match = i.value.replace("mul(", "")
                .replace(")", "")
                .split(",")
                .map { it.toInt() }

            result += match[0] * match[1]
        }
        return result
    }


    val input = readInput("src/Input_Day3.txt")
    part1(input).println()
    part2(input).println()
}
