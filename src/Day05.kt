import java.io.File

fun main() {
    fun readInput1(filename: String): MutableList<List<Int>>{
        val input = mutableListOf<List<Int>>()
        File(filename).useLines { lines ->
            lines.forEach {
                if ("|" in it) input.add(
                    it.split("|")
                        .map { it.toInt() })
            }
        }
        return input
    }

    fun readInput2(filename: String): MutableList<List<Int>>{
        val input = mutableListOf<List<Int>>()
        File(filename).useLines { lines ->
            lines.forEach {
                if ("," in it) input.add(it.split(",").map { it.toInt()})
            }
        }
        return input
    }

    fun part1(rules: MutableList<List<Int>>, updates: MutableList<List<Int>>): Int {
        // I simply check for each number if it violates any rules
        fun checkRules(num1:Int, num2:Int): Boolean {
            rules.forEach { if (it[0] == num2 && it[1] == num1) {
                return false
                }
            }
            return true
        }

        var result = 0
        jump@ for (update in updates) {
            for (i in 0 until update.size) {
                for (j in i until update.size) {
                    if (!checkRules(update[i], update[j])) continue@jump
                }
            }
            result += update[(update.size/2)]
        }
        return result
    }

    fun part2(input: MutableList<List<String>>): Int {

        return 0
    }

    val rules = readInput1("src/Input_Day5.txt")
    val pages = readInput2("src/Input_Day5.txt")

    println("Result part 1:")
    part1(rules, pages).println()
    println("Result part 2:")
    //part2(input).println()
}
