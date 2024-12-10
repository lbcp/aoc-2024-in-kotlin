import java.io.File

fun main() {
    fun readInput(filename: String): MutableList<List<String>>{
        val input = mutableListOf<List<String>>()
        File(filename).useLines { lines ->
            lines.forEach {
                input.add(it.split("")
                    .filter { char -> char.isNotEmpty() })
            }
        }
        return input
    }

    fun createPairs(map: MutableList<List<String>>): List<Pair<List<Int>, List<Int>>> {
        val antennaPos = mutableMapOf<String, MutableList<List<Int>>>()
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] != ".") {
                    if (antennaPos.containsKey(map[i][j])) antennaPos[map[i][j]]!!.add(listOf(i, j))
                    else {
                        antennaPos[map[i][j]] = mutableListOf(listOf(i, j))
                    }
                }
            }
        }

        val pairs = mutableListOf<Pair<List<Int>, List<Int>>>()
        for ((_, v) in antennaPos) {
            for (i in v.indices) {
                for (j in i+1 until v.size)
                    pairs.add(Pair(v[i], v[j]))
            }
        }
        return pairs
    }

    fun part1(map: MutableList<List<String>>): Int {
        val pairs = createPairs(map)
        val antinodes = mutableSetOf<List<Int>>()
        for ((l, r) in pairs) {
            val vec = l.zip(r) { a, b -> a - b}
            val leftAntinode = l.zip(vec) { a, b -> a + b}
            val rightAntinode = r.zip(vec) { a, b -> a - b}

            if (leftAntinode[0] >= 0 && leftAntinode[0] < map.size &&
                leftAntinode[1] >= 0 && leftAntinode[1] < map[0].size) {
                antinodes.add(leftAntinode)
            }
            if (rightAntinode[0] >= 0 && rightAntinode[0] < map.size &&
                rightAntinode[1] >= 0 && rightAntinode[1] < map[0].size) {
                antinodes.add(rightAntinode)
            }
        }
        return antinodes.size
    }

    fun part2(map: MutableList<List<String>>): Int  {
        val pairs = createPairs(map)
        val antinodes = mutableSetOf<List<Int>>()
        for ((l, r) in pairs) {
            antinodes.add(l)
            antinodes.add(r)
            val vec = l.zip(r) { a, b -> a - b}
            var leftAntinode = l.zip(vec) { a, b -> a + b}
            var rightAntinode = r.zip(vec) { a, b -> a - b}
            while (leftAntinode[0] >= 0 && leftAntinode[0] < map.size &&
                        leftAntinode[1] >= 0 && leftAntinode[1] < map[0].size) {
                antinodes.add(leftAntinode)
                leftAntinode = leftAntinode.zip(vec) { a, b -> a + b}
            }
            while (rightAntinode[0] >= 0 && rightAntinode[0] < map.size &&
                rightAntinode[1] >= 0 && rightAntinode[1] < map[0].size) {
                antinodes.add(rightAntinode)
                rightAntinode = rightAntinode.zip(vec) { a, b -> a - b}
            }
        }
        return antinodes.size
    }

    val input = readInput("src/Input_Day8.txt")
    part1(input).println()
    part2(input).println()
}
