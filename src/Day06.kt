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


    fun moveFakeGuard(obMap: MutableList<MutableList<String>>,
                      fakeGuardPos: MutableList<MutableList<Int>>,
                      ): Boolean {
        val prevGuardPositions = mutableListOf<MutableList<MutableList<Int>>>()
        while (true) {
            prevGuardPositions.add(fakeGuardPos.toMutableList())
            var nextPos = fakeGuardPos[0].zip(fakeGuardPos[1]) { a, b -> a + b }
            if (nextPos[0] < 0 || nextPos[0] >= obMap.size ||
                nextPos[1] < 0 || nextPos[1] >= obMap[1].size) return false
            else if (obMap[nextPos[0]][nextPos[1]] == "#" || obMap[nextPos[0]][nextPos[1]] == "O" ) {
                while (obMap[nextPos[0]][nextPos[1]] == "#" || obMap[nextPos[0]][nextPos[1]] == "O" ) {
                    when (fakeGuardPos[1]) {
                        mutableListOf(-1, 0) -> fakeGuardPos[1] = mutableListOf(0, 1)
                        mutableListOf(0, 1) -> fakeGuardPos[1] = mutableListOf(1, 0)
                        mutableListOf(1, 0) -> fakeGuardPos[1] = mutableListOf(0, -1)
                        mutableListOf(0, -1) -> fakeGuardPos[1] = mutableListOf(-1, 0)
                        else -> throw Exception("That shouldn't happen. Check guard pos.")
                    }
                    nextPos = fakeGuardPos[0].zip(fakeGuardPos[1]) { a, b -> a + b }
                }
            }
            fakeGuardPos[0] = nextPos.toMutableList()
            if (fakeGuardPos in prevGuardPositions) return true
        }
    }


    fun verifyObstacle(obstacle: List<Int>): Boolean {
        // I do not why but somehow wrong obstacles are placed. This is just to remove them.
        val modMap = readInput("src/Input_Day6.txt")
        val controlGuard = getGuardPos(modMap)
        modMap[obstacle[0]][obstacle[1]] = "#"
        if (controlGuard[0] == obstacle.toMutableList()) return false
        val beenHereList = mutableSetOf(controlGuard)
        while (true) {
            beenHereList.add(controlGuard.toMutableList())
            var nextPos = controlGuard[0].zip(controlGuard[1]) { a, b -> a + b }
            if (nextPos[0] < 0 || nextPos[0] >= modMap.size ||
                nextPos[1] < 0 || nextPos[1] >= modMap[1].size) {
                return false
            }
            else if (modMap[nextPos[0]][nextPos[1]] == "#") {
                while (modMap[nextPos[0]][nextPos[1]] == "#") {
                    when (controlGuard[1]) {
                        mutableListOf(-1, 0) -> controlGuard[1] = mutableListOf(0, 1)
                        mutableListOf(0, 1) -> controlGuard[1] = mutableListOf(1, 0)
                        mutableListOf(1, 0) -> controlGuard[1] = mutableListOf(0, -1)
                        mutableListOf(0, -1) -> controlGuard[1] = mutableListOf(-1, 0)
                        else -> throw Exception("That shouldn't happen. Check guard pos.")
                    }
                    nextPos = controlGuard[0].zip(controlGuard[1]) { a, b -> a + b }
                }
            }
            controlGuard[0] = nextPos.toMutableList()
            if (controlGuard in beenHereList) return true
        }
    }

    fun moveGuard(map: MutableList<MutableList<String>>,
                  guardPos: MutableList<MutableList<Int>>,
                  part2: Boolean = false): MutableList<MutableList<String>> {
        map[guardPos[0][0]][guardPos[0][1]] = "X"
        val obstacles = mutableSetOf<List<Int>>()
        var step = 0
        while (true) {
            var nextPos = guardPos[0].zip(guardPos[1]) { a, b -> a + b }
            if (nextPos[0] < 0 || nextPos[0] >= map.size ||
                nextPos[1] < 0 || nextPos[1] >= map[1].size) {
                if (part2) {
                    for (o in obstacles) {
                        if (verifyObstacle(o)) map[o[0]][o[1]] = "O"
                        else (println("Couldn't verify at $o"))
                    }
                }
                return map
            }
            else if (map[nextPos[0]][nextPos[1]] == "#") {
                while (map[nextPos[0]][nextPos[1]] == "#") {
                    when (guardPos[1]) {
                        mutableListOf(-1, 0) -> guardPos[1] = mutableListOf(0, 1)
                        mutableListOf(0, 1) -> guardPos[1] = mutableListOf(1, 0)
                        mutableListOf(1, 0) -> guardPos[1] = mutableListOf(0, -1)
                        mutableListOf(0, -1) -> guardPos[1] = mutableListOf(-1, 0)
                        else -> throw Exception("That shouldn't happen. Check guard pos.")
                    }
                    nextPos = guardPos[0].zip(guardPos[1]) { a, b -> a + b }
                }
            }

            if (part2) {
                val obstacleMap: MutableList<MutableList<String>> = map.map { it.toMutableList() }.toMutableList()
                val fakeGuardPos: MutableList<MutableList<Int>> = guardPos.map { it.toMutableList() }.toMutableList()
                obstacleMap[nextPos[0]][nextPos[1]] = "O"
                if (moveFakeGuard(obstacleMap, fakeGuardPos)) obstacles.add(nextPos)
                step += 1
            }

            guardPos[0] = nextPos.toMutableList()
            map[guardPos[0][0]][guardPos[0][1]] = "X"
        }
    }

    fun part1(input: MutableList<MutableList<String>>): Int {
        val guardPos = getGuardPos(input)
        val endMap = moveGuard(input, guardPos)
        var result = 0
        endMap.forEach { result += it.count { char -> char == "X" } }
        return result
    }

    fun part2(input: MutableList<MutableList<String>>): Int {
        var result = 0
        val guardPos = getGuardPos(input)
        val endMap = moveGuard(input, guardPos, part2 = true)
        endMap.forEach { result += it.count { char -> char == "O" } }
        return result
    }

    val input = readInput("src/Input_Day6.txt")
    part1(input).println()
    val input2 = readInput("src/Input_Day6.txt") // because the input is modified in part1
    part2(input2).println()
}
