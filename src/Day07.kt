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

    fun calculateResults(operators: List<Char>, valuesList: List<Long> ): Long {
        var result = valuesList[0]
        for (i in 1 until valuesList.size) {
            when (operators[i-1]) {
                '+' -> result += valuesList[i]
                '*' -> result *= valuesList[i]
                '|' -> result = (result.toString() + valuesList[i].toString()).toLong()
            }
        }
        return result
    }

    fun part1(sums: MutableList<Long>, values: MutableList<List<Long>>): Long {
        var maxOperators = 0
        values.forEach { if (it.size > maxOperators) maxOperators = it.size - 1 }
        val correctSums = mutableSetOf<Long>()
        val correctMap = mutableMapOf<List<Long>, Long>()

        fun iterateCombinations(operatorList: List<Char>) {
            if (operatorList.size == maxOperators)  {
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

        fun iterateCombinations(operatorList: List<Char>) {
            if (operatorList.size == maxOperators) {
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
                iterateCombinations(operatorList + listOf('|'))
            }
        }
        iterateCombinations(emptyList())
        return correctMap.values.sum()
    }

    val (sums, values) = readInput("src/Input_Day7.txt")

    part1(sums, values).println()
    part2(sums, values).println()
}
