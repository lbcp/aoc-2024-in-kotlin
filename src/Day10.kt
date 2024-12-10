import java.io.File

fun main() {
    fun readInput(filename: String): MutableList<List<Int>>{
        val input = mutableListOf<List<Int>>()
        File(filename).useLines { lines ->
            lines.forEach {
                input.add(it.split("")
                    .filter { char -> char.isNotEmpty() }
                    .map { num -> num.toInt() })
            }
        }
        return input
    }

    fun part1(map: MutableList<List<Int>>): Int {
        // The idea: Make a map of starting coordinates as key and a set of trail head coordinates as values.
        val trailMap = mutableMapOf<List<Int>, MutableSet<List<Int>>>()
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] == 0) {
                    trailMap[listOf(i, j)] = mutableSetOf()
                }
            }
        }

        fun walkPath(coordinate: List<Int>, startCoord: List<Int>) {
            if (map[coordinate[0]][coordinate[1]] == 9) {
                trailMap[startCoord]!!.add(coordinate)
                return
            }
            val currHeight = map[coordinate[0]][coordinate[1]]

            // walkUp
            if (coordinate[0] > 0 && map[coordinate[0]-1][coordinate[1]] - currHeight == 1) {
                walkPath(listOf(coordinate[0]-1, coordinate[1]), startCoord)
            }
            // walkDown
            if (coordinate[0] < map.size - 1 && map[coordinate[0]+1][coordinate[1]] - currHeight == 1) {
                walkPath(listOf(coordinate[0]+1, coordinate[1]), startCoord)
            }
            //left
            if (coordinate[1] > 0 && map[coordinate[0]][coordinate[1]-1] - currHeight == 1) {
                walkPath(listOf(coordinate[0], coordinate[1]-1), startCoord)
            }
            //right
            if (coordinate[1] < map[0].size-1 && map[coordinate[0]][coordinate[1]+1] - currHeight == 1) {
                walkPath(listOf(coordinate[0], coordinate[1]+1), startCoord)
            }
        }

        for (start in trailMap.keys) {
            walkPath(start, start)
        }
        var result = 0
        for ((_, v) in trailMap ) {
            result += v.size
        }
        return result
    }

    fun part2(map: MutableList<List<Int>>): Int  {
        val trailMap = mutableMapOf<List<Int>, Int>()
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] == 9) {
                    trailMap[listOf(i, j)] = 0
                }
            }
        }

        fun walkPath(coordinate: List<Int>, startCoord: List<Int>) {
            if (map[coordinate[0]][coordinate[1]] == 0) {
                trailMap[startCoord] = trailMap[startCoord]!! + 1
                return
            }
            val currHeight = map[coordinate[0]][coordinate[1]]
            // walkUp
            if (coordinate[0] > 0 && map[coordinate[0]-1][coordinate[1]] - currHeight == -1) {
                walkPath(listOf(coordinate[0]-1, coordinate[1]), startCoord)
            }
            // walkDown
            if (coordinate[0] < map.size - 1 && map[coordinate[0]+1][coordinate[1]] - currHeight == -1) {
                walkPath(listOf(coordinate[0]+1, coordinate[1]), startCoord)
            }
            //left
            if (coordinate[1] > 0 && map[coordinate[0]][coordinate[1]-1] - currHeight == -1) {
                walkPath(listOf(coordinate[0], coordinate[1]-1), startCoord)
            }
            //right
            if (coordinate[1] < map[0].size-1 && map[coordinate[0]][coordinate[1]+1] - currHeight == -1) {
                walkPath(listOf(coordinate[0], coordinate[1]+1), startCoord)
            }

        }

        for (start in trailMap.keys) {
            walkPath(start, start)
        }

        var result = 0
        for ((_, v) in trailMap ) {
            result += v
        }
        return result
    }

    val input = readInput("src/Input_Day10.txt")
    part1(input).println()
    part2(input).println()
}
