import java.io.File

fun main() {
    fun readInput(filename: String): MutableList<MutableList<String>>{
        val input = mutableListOf<MutableList<String>>()
        File(filename).useLines { lines ->
            lines.forEach {
                input.add(it.split("")
                    .filter { char -> char.isNotEmpty() }
                    .toMutableList()
                )
            }
        }
        return input
    }

    fun part1(map: MutableList<MutableList<String>>): Int {
        // The idea: Make a map of starting coordinates as key and a set of all plots as values.
        fun generatePlot(map: MutableList<MutableList<String>>, pos:List<Int>, letter: String): MutableSet<List<Int>> {
            val currPlot = mutableSetOf(pos)
            map[pos[0]][pos[1]] = "."
            if (pos[0] + 1 < map.size && map[pos[0]+1][pos[1]] == letter) {
                currPlot += generatePlot(map, listOf(pos[0]+1, pos[1]), letter)
            }
            if (pos[0] - 1 >= 0 && map[pos[0]-1][pos[1]] == letter) {
                currPlot += generatePlot(map, listOf(pos[0]-1, pos[1]), letter)
            }
            if (pos[1] + 1 < map[0].size && map[pos[0]][pos[1]+1] == letter) {
                currPlot += generatePlot(map, listOf(pos[0], pos[1]+1), letter)
            }
            if (pos[1] - 1 >= 0 && map[pos[0]][pos[1]-1] == letter) {
                currPlot += generatePlot(map, listOf(pos[0], pos[1]-1), letter)
            }
            return currPlot
        }

        val plotMap = mutableMapOf<Int, MutableList<List<Int>>>()
        var index = 0
        for ((row, rowList) in map.withIndex()) {
            for ((col, plant ) in rowList.withIndex()) {
                if (plant != ".") {
                    plotMap[index] = generatePlot(map, listOf(row, col), plant).toMutableList()
                    index++
                }
            }
        }
        //println(plotMap)
        //plotMap.forEach { (_, value) ->  value.sortWith(compareBy( {it[0]}, { it[1] }))}
        //println(plotMap)
        // Calculate the fences
        var price = 0
        plotMap.forEach { (_, value) ->
            var fences = 0
            value.forEach {
                fences += 4
                if (listOf(it[0]+1,it[1]) in value) fences -= 1
                if (listOf(it[0]-1,it[1]) in value) fences -= 1
                if (listOf(it[0],it[1]+1) in value) fences -= 1
                if (listOf(it[0],it[1]-1) in value) fences -= 1
                }
            price += fences * value.size
            }
        return price
    }


    fun part2(map: MutableList<MutableList<String>>): Int {
        // The idea: Make a map of starting coordinates as key and a set of all plots as values.
        fun generatePlot(map: MutableList<MutableList<String>>, pos:List<Int>, letter: String): MutableSet<List<Int>> {
            val currPlot = mutableSetOf(pos)
            map[pos[0]][pos[1]] = "."
            if (pos[0] + 1 < map.size && map[pos[0]+1][pos[1]] == letter) {
                currPlot += generatePlot(map, listOf(pos[0]+1, pos[1]), letter)
            }
            if (pos[0] - 1 >= 0 && map[pos[0]-1][pos[1]] == letter) {
                currPlot += generatePlot(map, listOf(pos[0]-1, pos[1]), letter)
            }
            if (pos[1] + 1 < map[0].size && map[pos[0]][pos[1]+1] == letter) {
                currPlot += generatePlot(map, listOf(pos[0], pos[1]+1), letter)
            }
            if (pos[1] - 1 >= 0 && map[pos[0]][pos[1]-1] == letter) {
                currPlot += generatePlot(map, listOf(pos[0], pos[1]-1), letter)
            }
            return currPlot
        }

        val plotMap = mutableMapOf<Int, MutableList<List<Int>>>()
        var index = 0
        for ((row, rowList) in map.withIndex()) {
            for ((col, plant ) in rowList.withIndex()) {
                if (plant != ".") {
                    plotMap[index] = generatePlot(map, listOf(row, col), plant).toMutableList()
                    index++
                }
            }
        }

        plotMap.forEach { (_, value) ->  value.sortWith(compareBy( {it[0]}, { it[1] }))}
        println(plotMap)
        var price = 0


        return price
    }

    val input = readInput("src/TestInput_Day12.txt")
    part1(input).println()
    part2(input).println()
}
