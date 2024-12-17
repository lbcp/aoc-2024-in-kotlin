import java.io.File
import java.math.RoundingMode

fun main() {
    fun readInput(filename: String): MutableMap<Int, MutableList<List<Long>>>{
        val clawMachines = mutableMapOf<Int, MutableList<List<Long>>>()
        var id = 0
        File(filename).useLines { lines ->
            lines.forEach {
                if ("Button A:" in it) {
                    val butA = it.split(", ")
                        .map { char -> char.replace("[^0-9]".toRegex(), "").toLong() }
                    clawMachines[id] = mutableListOf(butA)
                }
                else if ("Button B:" in it) {
                    val butB = it.split(", ")
                        .map { char -> char.replace("[^0-9]".toRegex(), "").toLong() }
                    clawMachines[id]!!.add(butB)
                }
                else if ("Prize:" in it) {
                    val prize = it.split(", ")
                        .map { char -> char.replace("[^0-9]".toRegex(), "").toLong()}
                    clawMachines[id]!!.add(prize)
                }
                else id += 1
            }
        }
        return clawMachines
    }

    fun readInput2(filename: String): MutableMap<Int, MutableList<List<Long>>>{
        val clawMachines = mutableMapOf<Int, MutableList<List<Long>>>()
        var id = 0
        File(filename).useLines { lines ->
            lines.forEach {
                if ("Button A:" in it) {
                    val butA = it.split(", ")
                        .map { char -> char.replace("[^0-9]".toRegex(), "").toLong() }
                    clawMachines[id] = mutableListOf(butA)
                }
                else if ("Button B:" in it) {
                    val butB = it.split(", ")
                        .map { char -> char.replace("[^0-9]".toRegex(), "").toLong() }
                    clawMachines[id]!!.add(butB)
                }
                else if ("Prize:" in it) {
                    val prize = it.split(", ")
                        .map { char -> char.replace("[^0-9]".toRegex(), "").toLong() + 10000000000000}
                    clawMachines[id]!!.add(prize)
                }
                else id += 1
            }
        }
        return clawMachines
    }

    fun part1(machines: MutableMap<Int, MutableList<List<Long>>>): Long {
        // Idea: Make a list of all possible positions if only one button is pressed
        // Then calculate the sums of all possible positions
        fun generateSums(steps: List<Long>, maxVals : List<Long>): List<List<Long>> {
            val sums = mutableListOf<List<Long>>()
            var x = 0L
            var y = 0L
            while (x < maxVals[0] && y < maxVals[1]) {
                sums.add(listOf(x, y))
                x += steps[0]
                y += steps[1]
            }
            return sums.toList()
        }

        fun addSums(sumsA: List<List<Long>>, sumsB: List<List<Long>>, prizeLoc: List<Long>): Long {
            var minCost = -1L
            for ((countA, valueA) in sumsA.withIndex()) {
                for ((countB, valueB) in sumsB.withIndex()) {
                    val sum = listOf(valueA[0]+valueB[0], valueA[1]+valueB[1])
                    if (sum == prizeLoc) {
                        val cost = (countA.toLong() * 3) + countB.toLong()
                        if (cost < minCost || minCost == -1L) minCost = cost
                    }
                }
            }
            if (minCost == -1L) minCost = 0
            return minCost
        }

        var winCosts = 0L
        machines.forEach { (_, lists) ->
            val allX = generateSums(lists[0], lists[2])
            val allY = generateSums(lists[1], lists[2])
            winCosts += addSums(allX, allY, lists[2])

        }
        return winCosts
    }

    fun checkResult(target: Long, pressesA: Long, pressesB: Long, stepA: Long, stepB: Long): Boolean {
        return target == (pressesA*stepA) + (pressesB*stepB)
    }

    fun part2(machines: MutableMap<Int, MutableList<List<Long>>>): Long {
        var winCosts = 0L
        machines.forEach { (_, lists) ->
            val a = lists[0]
            val b = lists[1]
            val aX = a[0]
            val aY = a[1]
            val bX = b[0]
            val bY = b[1]
            val prize = lists[2]
            val pX = prize[0]
            val pY = prize[1]

            // Since nothing worked, I had to find out if a prize position is reachable.
            // The equations are as follows:
            // pX = (aX * pressesA) + (bX * pressesB)
            // pY = (aY * pressesA) + (bY * pressesB)
            //
            // This results in 2 equations with 2 unknowns.
            // pB = (pX - (aX *pA)) / bX
            // pB = (pY - (aY * pA)) / bY
            // There is a rounding issue. Thus, I need to convert it to Double then to long
            val pB = (pY - aY * pX / aX.toDouble()) / (bY - aY * bX / aX.toDouble())
            val pA = (pX - pB * bX) / aX

            val pressesB = (pB.toBigDecimal().setScale(0, RoundingMode.HALF_UP)).toLong()
            val pressesA = (pA.toBigDecimal().setScale(0, RoundingMode.HALF_UP)).toLong()

            if (checkResult(pX, pressesA, pressesB, aX, bX) && checkResult(pY, pressesA, pressesB, aY, bY)) {
                winCosts += (3 * pressesA) + pressesB
            }

        }
        return winCosts
    }

    val input = readInput("src/Input_Day13.txt")
    part1(input).println()
    val input2 = readInput2("src/Input_Day13.txt")
    part2(input2).println()
}
