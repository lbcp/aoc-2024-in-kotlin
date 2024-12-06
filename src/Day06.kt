import java.io.File

fun main() {
    fun readInput(filename: String): MutableList<MutableList<String>>{
        val input = mutableListOf<MutableList<String>>()
        File(filename).useLines { lines ->
            lines.forEach {
                input.add(it.split("")
                    .filter { char -> char.isNotEmpty() }
                    .toMutableList())
            }
        }
        return input
    }


    fun part1(input: MutableList<MutableList<String>>): Int {
        fun getGuardPos(map: MutableList<MutableList<String>>): MutableList<MutableList<Int>> {
            val guard = mutableListOf<MutableList<Int>>()
            for (i in map.indices) {
                for (j in map.indices) {
                    if (map[i][j] == "^" || map[i][j] == "<" || map[i][j] == ">" || map[i][j] == "v") {
                        guard.add(mutableListOf(i, j))
                        when (map[i][j]) {
                            "^" -> guard.add(mutableListOf(-1, 0))
                            "<" -> guard.add(mutableListOf(0, -1))
                            ">" -> guard.add(mutableListOf(0, 1))
                            "v" -> guard.add(mutableListOf(1, 0))
                        }
                    }
                }
            }
            return guard
        }
        fun moveGuard(map: MutableList<MutableList<String>>, guardPos: MutableList<MutableList<Int>>): MutableList<MutableList<String>> {
            // Counts the steps taken, because I did not read correctly.
            var result = 0
            map[guardPos[0][0]][guardPos[0][1]] = "X"
            while (true) {
                var nextPos = guardPos[0].zip(guardPos[1]) { a, b -> a + b }
                if (nextPos[0] < 0 || nextPos[0] >= map.size ||
                    nextPos[1] < 0 || nextPos[1] >= map[1].size) return map
                else if (map[nextPos[0]][nextPos[1]] == "#") {
                    when (guardPos[1]) {
                        mutableListOf(-1, 0) -> guardPos[1] = mutableListOf(0, 1)
                        mutableListOf(0, 1) -> guardPos[1] = mutableListOf(1, 0)
                        mutableListOf(1, 0) -> guardPos[1] = mutableListOf(0, -1)
                        mutableListOf(0, -1) -> guardPos[1] = mutableListOf(-1, 0)
                        else -> throw Exception("That shouldn't happen. Check guard pos.")
                    }
                    nextPos = guardPos[0].zip(guardPos[1]) { a, b -> a + b }
                }
                result += 1
                guardPos[0] = nextPos.toMutableList()
                map[guardPos[0][0]][guardPos[0][1]] = "X"
                //println("Map")
                //map.forEach { println(it) }
            }
        }
        val guardPos = getGuardPos(input)
        val endMap = moveGuard(input, guardPos)
        var result = 0
        endMap.forEach { result += it.count { char -> char == "X" } }
        return result
    }

    fun part2(input: List<String>): Int {
        // In theory, I need to check at every single position of the guard, what would happen if I add an obstacle.
        // Something like raytracing in the next direction, bouncing of and see it the ray comes back.
        // This means, before moving the guard. I need to create a ray on a 90 degree angle. Check if it hits something,
        // If so, rotate the ray, check if it hits, rotate again, check if it hits rotate and see if it returns.
        // The catch is, that the loop may not be a square in the end. Thus, I pretty much have to iterate over it again and again.
        return input.size
    }

    val input = readInput("src/Input_Day6.txt")
    println(input)
    // Read the input from the `src/Day01.txt` file.

    part1(input).println()
    //part2(input).println()
}
