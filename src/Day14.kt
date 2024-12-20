import java.io.File

fun main() {
    fun readInput(filename: String): MutableList<Pair<List<Int>, List<Int>>> {
        val input = mutableListOf<Pair<List<Int>, List<Int>>>()
        File(filename).useLines { lines ->
            lines.forEach {
                val temp = it.split(" ")
                val pos = temp[0].replace("p=", "")
                    .split(",")
                    .map { num -> num.toInt() }
                val velocity = temp[1].replace("v=", "")
                    .split(",")
                    .map { num -> num.toInt() }
                input.add(Pair(pos, velocity))
            }
        }
        return input
    }

    fun part1(robots: MutableList<Pair<List<Int>, List<Int>>>, gridX: Int = 101, gridY: Int = 103): Int {
        val seconds = 100
        val finalBotPos = mutableListOf<List<Int>>()
        for (bot in robots) {
            val (pos, velocity) = bot
            val newPos = mutableListOf((pos[0] + velocity[0] * seconds) % gridX, (pos[1] + velocity[1] * seconds) % gridY)
            //println("New position for robot: X=${pos[0] + (newPos[0] % 11)}, Y=${pos[1] + (newPos[1] % 7)}")

            if (newPos[0] < 0) newPos[0] += gridX
            if (newPos[1] < 0) newPos[1] += gridY
            //println("New position for robot: X=${newPos[0]}, Y=${newPos[1]}")
            finalBotPos.add(newPos.toList())
        }

        //finalBotPos.sortBy { it[0] }
        val safetyGrid = mutableMapOf(0 to 0, 1 to 0, 2 to 0, 3 to 0)
        var result = 1
        for (bot in finalBotPos) {
            if (bot[0] < gridX / 2) {
                if (bot[1] < gridY / 2) safetyGrid[0] = safetyGrid[0]!! + 1
                else if (bot[1] > gridY / 2) safetyGrid[1] = safetyGrid[1]!! + 1
            } else if (bot[0] > gridX / 2) {
                if (bot[1] < gridY / 2) safetyGrid[2] = safetyGrid[2]!! + 1
                else if (bot[1] > gridY / 2) safetyGrid[3] = safetyGrid[3]!! + 1
            }
        }
        for (key in safetyGrid.values) result *= key
        return result
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("src/TestInput_Day14.txt")
    check(part1(testInput, 11, 7) == 12)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("src/Input_Day14.txt")
    part1(input).println()
    //part2(input).println()
}
