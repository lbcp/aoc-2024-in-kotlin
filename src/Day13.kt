import java.io.File
import kotlin.math.max
import kotlin.math.min
import kotlin.math.*

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
        machines.forEach { key, lists ->
            val allX = generateSums(lists[0], lists[2])
            val allY = generateSums(lists[1], lists[2])
            winCosts += addSums(allX, allY, lists[2])

        }

        return winCosts
    }


    fun part2(machines: MutableMap<Int, MutableList<List<Long>>>): Long {
        // For part 2, I try to use the greatest common divisor.
        // https://en.wikipedia.org/wiki/Greatest_common_divisor
        // I assume that it will always be cheaper if the ration of B/A presses is the smallest.
        fun getGCD(num1:Long, num2:Long): Long {
            // Euclidean algorithm from Wikipedia
            var larger = max(num1, num2)
            var smaller = min(num1, num2)
            while (larger != 0L) {
                val temp = larger
                larger = larger % smaller
                smaller = temp
            }
            return smaller
        }


        var winCosts = 0L

        machines.forEach { (id, lists) ->
            val a = lists[0]
            val b = lists[1]
            val prize = lists[2]
            val x_range = 2..(a[0] + b[0]) * 100
            val y_range = 2..(a[1] + b[1]) * 100
            println("Machine id: $id")
            println("$a")
            println("$b")
            println(prize)
            println(prize[0] % a[0] == 0L)
            println(prize[1] % a[1] == 0L)
            println("X")
            for (i in x_range) {
                if (prize[0] % i == 0L) println(i)
            }
            println("Y")
            for (i in y_range) {
                if (prize[1] % i == 0L) println(i)
            }


                /*
                val gcdX = getGCD(a[0], b[0])
            val gcdY = getGCD(a[1], b[1])
            val gcdPrizeX = getGCD(prize[0], a[0])
            val gcdPrizeY = getGCD(prize[1], a[1])
            //if (max(gcdPrizeX, gcdPrizeY) % min(gcdPrizeX, gcdPrizeY) == 0L) {
                println("Can be done with $id")
                println(a)
                println(b)
                println(prize)
                println("GCD X: $gcdX; GCD Y: $gcdY")
                println("Price GCD X: $gcdPrizeX; GCD Y: $gcdPrizeY")
            //}
        */
        }



        return winCosts
    }

    //val input = readInput("src/Input_Day13.txt")
    //part1(input).println()
    val input2 = readInput2("src/TestInput_Day13.txt")
    part2(input2).println()
}
