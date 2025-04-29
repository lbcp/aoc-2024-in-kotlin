import java.io.File
import kotlin.time.measureTime

fun main() {
    fun readInput(filename: String): MutableList<Pair<String, String>>{
        val input = mutableListOf<Pair<String, String>>()
        File(filename).useLines { lines ->
            lines.forEach { line ->
                    input.add(line.split("-").let { it[0] to it[1] } )
                }
            }
        return input
    }

    fun generateNeighbours(input: MutableList<Pair<String, String>>): MutableMap<String, MutableSet<String>> {
        val neighbours = mutableMapOf<String, MutableSet<String>>()
        input.forEach { (a, b) ->
            if (!neighbours.containsKey(a)) {
                val currSet = mutableSetOf<String>()
                for ((c, d) in input) {
                    if (c == a) currSet.add(d)
                    else if (d == a) currSet.add(c)
                }
                neighbours[a] = currSet
            }
            if (!neighbours.containsKey(b)) {
                val currSet = mutableSetOf<String>()
                for ((c, d) in input) {
                    if (c == b) currSet.add(d)
                    else if (d == b) currSet.add(c)
                }
                neighbours[b] = currSet
            }
        }

        return neighbours
    }

    fun getConnected(input:MutableList<Pair<String, String>>): MutableSet<MutableSet<String>> {
        val output = mutableSetOf<MutableSet<String>>()
        val neighbourMap = generateNeighbours(input)
        input.forEach { (a, b) ->
            val doubles = neighbourMap[a]!! intersect neighbourMap[b]!!
            doubles.forEach {
                output.add(mutableSetOf(a, b, it))
            }
        }
        return output
    }

    fun part1(input: MutableList<Pair<String, String>>): Int {
        val output = getConnected(input)

        val final = mutableSetOf<MutableSet<String>>()
        output.forEach { network ->
            if (network.any { it.startsWith("t") }) {
                final.add(network)
            }
        }

        return final.size
    }

    fun part2(input: MutableList<Pair<String, String>>): String {
        var output = getConnected(input)
        val neighbourMap = generateNeighbours(input)

        // Use the output sets. Then check for each device if output set is in list.
        // In theory, I should only need to check for "recent addition", as every set
        // that does not get an additional device, should be smaller and thus be discarded.
        val extendedSet = mutableSetOf<MutableSet<String>>()
        var keepChecking = true
        while (keepChecking) {
            keepChecking = false
            extendedSet.clear()
            for (o in output) {
                for ((k, _) in neighbourMap) {
                    if (neighbourMap[k]!!.containsAll(o)) {
                        keepChecking = true
                        o.add(k)
                        extendedSet.add(o)
                    }
                }
            }
            if (extendedSet.size > 0) output = extendedSet.toMutableSet()
        }

        // Not pretty but gets the job done.
        var maxSize = 0
        var result = mutableSetOf<String>()
        for (i in output) {
            if (i.size > maxSize) {
                result = i
                maxSize = i.size
            }
        }

        return result.sorted().joinToString(",")
    }

    val testInput = readInput("src/TestInput_Day23.txt")
    check(part1(testInput) == 7)
    check(part2(testInput) == "co,de,ka,ta")

    val input = readInput("src/Input_Day23.txt")

    val timeTaken1 = measureTime { part1(input).println() }
    val timeTaken2 = measureTime { part2(input).println() }

    println("Part 1 took: $timeTaken1")
    println("Part 2 took: $timeTaken2")

}
