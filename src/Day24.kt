import java.io.File
import kotlin.time.measureTime

fun main() {
    fun readInput(filename: String): Pair<MutableMap<String, Int>, MutableList<String>> {
        val valueMap = mutableMapOf<String, Int>()
        val wiring = mutableListOf<String>()

        File(filename).useLines { lines ->
            var readWiring = false
            lines.forEach { line ->
                if (line.isEmpty()) {
                    readWiring = true
                } else if (!readWiring) {
                    val wire = line.split(": ")
                    valueMap[wire[0]] = wire[1].toInt()
                } else {
                    wiring.add(line)
                }
            }
        }

        return Pair(valueMap, wiring)
    }

    fun part1(valuesMap: MutableMap<String, Int>, wiring: MutableList<String>): Int {
        var remainingWiring = wiring

        while (remainingWiring.isNotEmpty()) {
            val leftovers = mutableListOf<String>()
            for (instruction in wiring) {
                val equation = instruction.split(" -> ")
                val result = equation[1].trim()
                val leftPart = equation[0].split(" ")
                if (leftPart[0] in valuesMap && leftPart[2] in valuesMap) {
                    valuesMap[result] = when (leftPart[1]) {
                        "AND" -> if (valuesMap[leftPart[0]]!! == 1 && valuesMap[leftPart[2]]!! == 1) 1 else 0
                        "OR" -> if (valuesMap[leftPart[0]]!! == 1 || valuesMap[leftPart[2]]!! == 1) 1 else 0
                        "XOR" -> if ((valuesMap[leftPart[0]]!! == 1 && valuesMap[leftPart[2]]!! == 0) ||
                            (valuesMap[leftPart[0]]!! == 0 && valuesMap[leftPart[2]]!! == 1)
                        ) 1 else 0

                        else -> 999
                    }
                } else leftovers.add(instruction)
            }
            remainingWiring = leftovers
        }
        val finalKeys = valuesMap.filterKeys { it.startsWith("z") }
            .toSortedMap()
            .values
            .joinToString("")
            .reversed()

        println(finalKeys)
        println(valuesMap)
        return wiring.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val (valuesMapTest, wiringTest) = readInput("src/TestInput_Day24_medium.txt")
    part1(valuesMapTest, wiringTest)

    val (valuesMap, wiring) = readInput("src/Input_Day24.txt")
    part1(valuesMap, wiring)

}
