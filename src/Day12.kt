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


    fun walkOuter(tiles: MutableList<List<Int>>): Int {
        var turns = 1
        var direction = "down"
        val startTile = tiles[0]
        var currTile = tiles[0]
        val walkedTile = mutableListOf<List<Int>>()
        while (startTile != currTile || direction != "left") {
            walkedTile.add(currTile)
            when (direction) {
                "down" ->
                    if (listOf(currTile[0], currTile[1] - 1) in tiles) {
                        direction = "left"
                        currTile = listOf(currTile[0], currTile[1] - 1)
                        turns += 1
                    } else if (listOf(currTile[0] + 1, currTile[1]) in tiles) {
                        currTile = listOf(currTile[0] + 1, currTile[1])
                    } else {
                        direction = "right"
                        turns += 1
                    }

                "left" ->
                    if (listOf(currTile[0] - 1, currTile[1]) in tiles) {
                        direction = "up"
                        currTile = listOf(currTile[0] - 1, currTile[1])
                        turns += 1
                    } else if (listOf(currTile[0], currTile[1] - 1) in tiles) {
                        currTile = listOf(currTile[0], currTile[1] - 1)
                    }
                    else {
                        direction = "down"
                        turns += 1
                    }

                "right" ->
                    if (listOf(currTile[0] + 1, currTile[1]) in tiles) {
                        direction = "down"
                        currTile = listOf(currTile[0] + 1, currTile[1])
                        turns += 1
                    } else if (listOf(currTile[0], currTile[1] + 1) in tiles) {
                        currTile =
                            listOf(currTile[0], currTile[1] + 1)
                    }
                    else {
                        direction = "up"
                        turns += 1
                    }

                "up" ->
                    if (listOf(currTile[0], currTile[1] + 1) in tiles) {
                        direction = "right"
                        currTile = listOf(currTile[0], currTile[1] + 1)
                        turns += 1
                    } else if (listOf(currTile[0] - 1, currTile[1]) in tiles) {
                        currTile =
                            listOf(currTile[0] - 1, currTile[1])
                    }
                    else {
                        direction = "left"
                        turns += 1
                    }
            }

        }
        return turns
    }

    fun walkInner(tiles: MutableList<List<Int>>, startTile: List<Int>, plot: Int): Pair<Int, MutableList<List<Int>>> {
    // First try. Does not catch all edge cases.
        //val inputMap = readInput("src/Input_Day12.txt")
        //val outerValue = inputMap[startTile[0]][startTile[1]-1]
        val upperCorner = tiles.first()
        val lowerCorner = tiles.last()
        var turns = 1
        var direction = "down"
        var currTile = startTile.toMutableList().toList()
        val walkedTile = mutableListOf<List<Int>>()
        if (listOf(currTile[0] - 1, currTile[1]) !in tiles ||
            listOf(currTile[0] - 1, currTile[1] - 1) !in tiles) return Pair(0, walkedTile)
        while (currTile != startTile || direction != "left") {
            //println("I am at position: $currTile")
            //println("I am going $direction")
            //println("I did $turns turns already.")
            val orderedByLast = tiles.filter { it[0] == currTile[0] }
            if (orderedByLast.isEmpty()) return Pair(0, walkedTile)
            val outerLeft = orderedByLast.sortedBy { it[1] }.first()
            val outRight = orderedByLast.sortedBy { it[1] }.last()

            walkedTile.add(currTile)
            /*
            println("##Start checking##")
            println(startTile)
            println(currTile)
            println(direction)
             */
            if (currTile[0] < 0 || currTile[1] < 0 || currTile[0] > startTile[0] + 1000 || currTile[1] > startTile[1] + 1000) {
                return Pair(0, walkedTile)
            }
            if (listOf(currTile[0], currTile[1] - 1) !in tiles && listOf(currTile[0], currTile[1] + 1) !in tiles &&
                listOf(currTile[0] - 1, currTile[1]) !in tiles && listOf(currTile[0] + 1, currTile[1]) !in tiles) {
                return Pair(0, walkedTile)
            }
            if (orderedByLast.isEmpty() || currTile[0] < upperCorner[0] || currTile[1] > lowerCorner[0] ||
                currTile[1] < outerLeft[1] || currTile[1] > outRight[1]) return Pair(0, walkedTile)
            when (direction) {
                "down" -> if (listOf(currTile[0], currTile[1] - 1) !in tiles) {
                    direction = "left"
                    turns += 1
                    currTile = listOf(currTile[0] - 1, currTile[1])
                } else if (listOf(currTile[0], currTile[1] - 1) in tiles && listOf(currTile[0] + 1, currTile[1]) !in tiles) {
                    direction = "down"
                    currTile = listOf(currTile[0] + 1, currTile[1])
                } else if (listOf(currTile[0], currTile[1] - 1) in tiles && listOf(currTile[0] + 1, currTile[1]) in tiles) {
                    // check diagonally -> Do for all
                    if (listOf(currTile[0] + 1, currTile[1] - 1) !in tiles) {
                        println("edgeCase")
                        currTile = listOf(currTile[0] + 1, currTile[1] - 1)
                        direction = "left"
                    } else direction = "right"
                    turns += 1
                } else {
                    println("That should not happen")
                    println(tiles)
                    return Pair(0, walkedTile)
                }

                "left" -> if (listOf(currTile[0] - 1, currTile[1]) !in tiles) {
                        direction = "up"
                        currTile = listOf(currTile[0] - 1, currTile[1])
                        turns += 1
                    } else if (listOf(currTile[0] - 1, currTile[1]) in tiles && listOf(currTile[0], currTile[1] - 1) !in tiles) {
                        currTile = listOf(currTile[0], currTile[1] - 1)
                    } else if  (listOf(currTile[0] - 1, currTile[1]) in tiles && listOf(currTile[0], currTile[1] - 1) in tiles) {
                    // check diagonally -> Do for all
                        if (listOf(currTile[0] - 1, currTile[1] - 1) !in tiles) {
                            println("edgeCase")
                            currTile = listOf(currTile[0] - 1, currTile[1] - 1)
                            direction = "up"
                        } else direction = "down"
                        turns += 1
                    } else {
                        println("That should not happen")
                        println(tiles)
                        return Pair(0, walkedTile)
                    }
                "right" ->
                    if (listOf(currTile[0] + 1, currTile[1]) !in tiles) {
                        direction = "down"
                        currTile = listOf(currTile[0] + 1, currTile[1])
                        turns += 1
                    } else if (listOf(currTile[0] + 1, currTile[1]) in tiles && listOf(currTile[0], currTile[1] + 1) !in tiles) {
                        currTile = listOf(currTile[0], currTile[1] + 1)
                    }
                    else if (listOf(currTile[0] + 1, currTile[1]) in tiles && listOf(currTile[0], currTile[1] + 1) in tiles) {
                        if (listOf(currTile[0] + 1, currTile[1] + 1) !in tiles) {
                            println("edgeCase")
                            currTile = listOf(currTile[0] + 1, currTile[1] + 1)
                            direction = "down"
                        } else direction = "up"
                        turns += 1
                    } else {
                        println("That should not happen")
                        println(tiles)
                        return Pair(0, walkedTile)
                    }

                "up" ->
                    if (listOf(currTile[0], currTile[1] + 1) !in tiles) {
                        direction = "right"
                        currTile = listOf(currTile[0], currTile[1] + 1)
                        turns += 1
                    } else if (listOf(currTile[0], currTile[1] + 1) in tiles && listOf(currTile[0] - 1, currTile[1]) !in tiles) {
                        currTile =
                            listOf(currTile[0] - 1, currTile[1])
                    } else if (listOf(currTile[0], currTile[1] + 1) in tiles && listOf(currTile[0] - 1, currTile[1]) in tiles) {
                        if (listOf(currTile[0] - 1, currTile[1] + 1) !in tiles) {
                            println("edgeCase")
                            currTile = listOf(currTile[0] - 1, currTile[1] + 1)
                            direction = "right"
                        } else direction = "left"
                        turns += 1
                    } else {
                        println("That should not happen")
                        println(tiles)
                        return Pair(0, walkedTile)
                    }
                }
            }
        return Pair(turns, walkedTile)
    }

    fun walkInner2(tiles: MutableList<List<Int>>): Int {
        var turns = 1
        var direction = "right"
        val startTile = tiles[0]
        var currTile = tiles[0]
        val walkedTile = mutableSetOf<List<Int>>()
        while (currTile !in walkedTile && direction != "right") {
            walkedTile.add(currTile)
            when (direction) {
                "down" -> // done?
                    if (listOf(currTile[0], currTile[1] - 1) in tiles) {
                        direction = "left"
                        currTile = listOf(currTile[0], currTile[1] - 1)
                        turns += 1
                    } else if (listOf(currTile[0], currTile[1] - 1) !in tiles && listOf(currTile[0] + 1, currTile[1]) in tiles) {
                        currTile = listOf(currTile[0] + 1, currTile[1])
                    } else if (listOf(currTile[0], currTile[1] - 1) !in tiles && listOf(currTile[0] + 1, currTile[1]) !in tiles
                        && listOf(currTile[0], currTile[1] + 1) in tiles) {
                        direction = "right"
                        turns += 1
                    } else return 0

                "left" -> //done
                    if (listOf(currTile[0] - 1, currTile[1]) in tiles) {
                        direction = "up"
                        currTile = listOf(currTile[0] - 1, currTile[1])
                        turns += 1
                    } else if (listOf(currTile[0] - 1, currTile[1]) !in tiles && listOf(currTile[0], currTile[1] - 1) in tiles) {
                        currTile = listOf(currTile[0], currTile[1] - 1)
                    }
                    else if (listOf(currTile[0] - 1, currTile[1]) !in tiles && listOf(currTile[0], currTile[1] - 1) !in tiles
                        && listOf(currTile[0] + 1, currTile[1]) in tiles){
                        direction = "down"
                        turns += 1
                    } else return 0

                "right" -> //done
                    if (listOf(currTile[0] + 1, currTile[1]) in tiles) {
                        direction = "down"
                        currTile = listOf(currTile[0] + 1, currTile[1])
                        turns += 1
                    } else if (listOf(currTile[0] + 1, currTile[1]) !in tiles && listOf(currTile[0], currTile[1] + 1) in tiles) {
                        currTile = listOf(currTile[0], currTile[1] + 1)
                    }
                    else if (listOf(currTile[0] + 1, currTile[1]) !in tiles && listOf(currTile[0], currTile[1] + 1) !in tiles
                        && listOf(currTile[0] - 1, currTile[1]) in tiles) {
                        direction = "up"
                        turns += 1
                    } else return 0

                "up" ->
                    if (listOf(currTile[0], currTile[1] + 1) in tiles) {
                        direction = "right"
                        currTile = listOf(currTile[0], currTile[1] + 1)
                        turns += 1
                    } else if (listOf(currTile[0], currTile[1] + 1) !in tiles && listOf(currTile[0] - 1, currTile[1]) in tiles) {
                        currTile =
                            listOf(currTile[0] - 1, currTile[1])
                    }
                    else if (listOf(currTile[0], currTile[1] + 1) !in tiles && listOf(currTile[0] - 1, currTile[1]) !in tiles
                        && listOf(currTile[0], currTile[1] - 1) in tiles) {
                        direction = "left"
                        turns += 1
                    } else return 0
            }

        }
        return turns
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
        //println(plotMap)
        plotMap.forEach { (_, value) ->  value.sortWith(compareBy( {it[0]}, { it[1] }))}
        //println(plotMap)

        var price = 0
        plotMap.forEach { (plot, tiles) ->
            // I want to walk along the edges and add one side per "turn" I have to take
            val turns = walkOuter(tiles)

            // Check for inner plot
            // Walk inner plot(s)
            // Get highest X and Y
            val lowY = tiles.first()[0]
            val highY = tiles.last()[0]
            val walkedTiles = mutableSetOf<List<Int>>()
            for (i in lowY .. highY) {
                val line = tiles.filter { it[0] == i }
                if (line.last()[1] - line.first()[1] > line.size - 1) {
                    // get first coordinate
                    for ((index, l)in line.withIndex()) {

                        if (listOf(l[0], l[1] + 1) !in line && l[1] + 1 < line.last()[1]) {

                            //println(l)
                            //println(walkedTiles)
                            if (listOf(l[0], l[1] + 1) in walkedTiles) continue
                            // Check if it is really surrounded
                            var (res, walked) = walkInner(tiles, listOf(l[0], l[1] + 1), plot)
                            //println(listOf(l[0], l[1] + 1))
                            walkedTiles.addAll(walked)

                            if (res > 3) {
                                price += res * tiles.size
                                println("Plot ID: $plot")
                                //println("Tiles: $tiles")
                                println("Inner Tiles: $walked")
                                println("Sides added: $res")
                            }
                            //println(walked)
                            //break
                        }
                    }
                }
            }

            // println("Plot $plot has $turns turns.")
            price += turns * tiles.size
        }

        return price
    }

    val testInput = readInput("src/TestInput_Day12.txt")
    check(part2(testInput) == 1206)
    val testInput1 = readInput("src/TestInput_Day12_2.txt")
    check(part2(testInput1) == 368)
    val testInput3 = readInput("src/TestInput_Day12_3.txt")
    check(part2(testInput3) == 236)
    val testInput4 = readInput("src/TestInput_Day12_4.txt")
    check(part2(testInput4) == 436)

    val input = readInput("src/Input_Day12.txt")
    check(part2(testInput4) == 815788)
    //part1(input).println()
    part2(input).println()
}
