import java.io.File

fun main() {
    fun readInput(filename: String): Pair<MutableList<Int>, MutableList<List<Int>>> {
        val sum = mutableListOf<Int>()
        val values = mutableListOf<List<Int>>()
        File(filename).useLines { lines ->
            lines.forEach {
                sum.add(it.split(":")[0].toInt())
                values.add(it.split(":")[1].trim().split(" ").map { num -> num.toInt() })
            }
        }
        return Pair(sum, values)
    }

    fun part1(sums: MutableList<Int>, values: MutableList<List<Int>>): Int {
        val maxOperators = sums.size - 1

        fun calculateResults(operators: List<Char>, valuesList: List<Int> ): Int {
            // TODO: Finish this part.
            // I now need to calculate with the operators.
            var result = valuesList[0]
            return result
        }

        fun iterateCombinations(operatorList: List<Char>) {
            // TODO: Check the results of the operations.
            if (operatorList.size == maxOperators)  {
                for (i in values.indices) {
                    if (sums[i] == calculateResults(operatorList, values[i])) return
                }
            }
            iterateCombinations(operatorList + listOf('+'))
            iterateCombinations(operatorList + listOf('*'))

        }


        return 0
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val (sums, values) = readInput("src/TestInput_Day7.txt")
    println(sums)
    println(values)
    //check(part1(testInput) == 1)

    // Read the input from the `src/Day01.txt` file.
    //val input = readInput("Day01")
    //part1(input).println()
    //part2(input).println()
}
