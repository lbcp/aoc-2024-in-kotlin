import java.io.File

fun main() {
    fun readInput(filename: String): Pair<MutableList<Long>, MutableList<List<Long>>> {
        val sum = mutableListOf<Long>()
        val values = mutableListOf<List<Long>>()
        File(filename).useLines { lines ->
            lines.forEach {
                sum.add(it.split(":")[0].toLong())
                values.add(it.split(":")[1].trim().split(" ").map { num -> num.toLong() })
            }
        }
        return Pair(sum, values)
    }

    fun part1(sums: MutableList<Long>, values: MutableList<List<Long>>): Long {
        var maxOperators = 0
        values.forEach { if (it.size > maxOperators) maxOperators = it.size - 1 }
        val correctSums = mutableSetOf<Long>()
        val correctMap = mutableMapOf<List<Long>, Long>()
        var totalSum: Long = 0

        fun calculateResults(operators: List<Char>, valuesList: List<Long> ): Long {
            // TODO: Finish this part.
            // I now need to calculate with the operators.
            var result = valuesList[0]
            for (i in 1 until valuesList.size) {
                when (operators[i-1]) {
                    '+' -> result += valuesList[i]
                    '*' -> result *= valuesList[i]
                }
            }
            return result
        }

        fun iterateCombinations(operatorList: List<Char>) {
            if (operatorList.size == maxOperators)  {
                println(operatorList)
                for (i in values.indices) {
                    if (sums[i] == calculateResults(operatorList, values[i])) {
                        correctSums.add(sums[i])
                        correctMap[values[i]] = sums[i]
                    }
                }
                return
            } else {
                iterateCombinations(operatorList + listOf('+'))
                iterateCombinations(operatorList + listOf('*'))
            }
        }
        iterateCombinations(emptyList())
        return correctMap.values.sum()
    }

    fun part2(sums: MutableList<Long>, values: MutableList<List<Long>>): Long {
        var maxOperators = 0
        values.forEach { if (it.size > maxOperators) maxOperators = it.size - 1 }
        val correctSums = mutableSetOf<Long>()
        val correctMap = mutableMapOf<List<Long>, Long>()
        var totalSum: Long = 0

        fun calculateResults(listOfOperators: List<Char>, listOfValues: List<Long> ): Long {
            // For Part 2 I first need to combine the numbers and purge the | operator
            var operators = mutableListOf<Char>()
            var valuesList = mutableListOf<Long>()
            if ('|' in listOfOperators) {
                operators = listOfOperators.filter { it != '|'}.toMutableList()
                var j = 0
                for (i in listOfValues.indices) {
                    println(j)
                    if (j >= listOfValues.size) break
                    if (listOfOperators[i] == '|' && listOfValues.size-1 >= j+1) {
                        valuesList.add((listOfValues[j].toString() + listOfValues[j+1].toString()).toLong())
                        j += 2
                    } else {
                        valuesList.add(listOfValues[j])
                        j += 1
                    }
                }
            } else {
                operators = listOfOperators.toMutableList()
                valuesList = listOfValues.toMutableList()
            }
            println(listOfValues)
            println(listOfOperators)
            println(valuesList)
            println(operators)

            var result = valuesList[0]
            for (i in 1 until valuesList.size) {
                when (operators[i-1]) {
                    '+' -> result += valuesList[i]
                    '*' -> result *= valuesList[i]
                    '|' -> continue
                }
            }
            return result
        }

        fun iterateCombinations(operatorList: List<Char>) {
            if (operatorList.size == maxOperators + 1) {
                for (i in values.indices) {
                    if (sums[i] == calculateResults(operatorList, values[i])) {
                        correctSums.add(sums[i])
                        correctMap[values[i]] = sums[i]
                    }
                }
                return
            } else if (operatorList.size == maxOperators) {
                // the last operator must not be an |
                iterateCombinations(operatorList + listOf('+'))
                iterateCombinations(operatorList + listOf('*'))
            } else {
                iterateCombinations(operatorList + listOf('+'))
                iterateCombinations(operatorList + listOf('*'))
                iterateCombinations(operatorList + listOf('|'))
            }
        }
        iterateCombinations(emptyList())
        println(correctMap)
        return correctMap.values.sum()
    }

    val (sums, values) = readInput("src/TestInput_Day7.txt")
    //check(part1(testInput) == 1)

    // Read the input from the `src/Day01.txt` file.
    //val input = readInput("Day01")
    part1(sums, values).println()
    part2(sums, values).println()
}
