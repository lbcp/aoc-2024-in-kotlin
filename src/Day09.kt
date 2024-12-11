import java.io.File
import kotlin.time.measureTime

fun main() {
    fun calculateChecksum(layout: MutableList<Int>): Long {
        var sum = 0L
        for ((index, i) in layout.withIndex()) {
            if (i != -1) {
                sum += i * index
            }
        }
        return sum
    }

    fun generateDiskLayout(filename: String): MutableList<Int> {
        val input = File(filename).readText()
        val layout = mutableListOf<Int>()
        var id = 0
        var even = true
        for (s in input) {
            if (s.isWhitespace()) continue
            if (even) {
                layout.addAll( List(s.digitToInt()) { id } )
                id += 1
                even = false
            } else {
                layout.addAll( List(s.digitToInt()) { -1 } )
                even = true
            }
        }
        return layout
    }

    fun part1(layout: MutableList<Int>): Long {
        fun findCurrentLast(layout: MutableList<Int>, currPos: Int): Int {
            for (z in layout.size-1 downTo currPos) {
                if (layout[z] >= 0) return z
            }
            return -99
        }

        for (i in layout.indices) {
            if (layout[i] < 0) {
                val swapWith = findCurrentLast(layout, i)
                if (swapWith >= 0) {
                    layout[i] = layout[swapWith]
                    layout[swapWith] = -1
                }
            }
        }
        return calculateChecksum(layout)
    }

    fun part2(layout: MutableList<Int>): Long {
        val alreadyMoved = mutableSetOf<Int>()
        fun findLastBlock(layout: MutableList<Int>): IntRange {
            for (z in layout.size - 1 downTo 1) {
                if (layout[z] > 0 && layout[z] !in alreadyMoved) {
                    alreadyMoved.add(layout[z])
                    return (layout.indexOfFirst { it == (layout[z])} .. z)
                }
            }
            // Consider making this nullable
            return 0..0
        }

        fun findFreeSlots(layout: MutableList<Int>, size: Int, end: Int): IntRange {
            for (i in layout.indices) {
                if (layout[i] < 0 && (i + size <= layout.size - 1) && i < end) {
                    val subList = layout.slice(i until i+size)
                    if (subList.sum() == size * -1) return i until i+size // This makes my rather hacky approach very useful :-)
                    }
                }
            return 0..0
        }

        while (true) {
            val block = findLastBlock(layout)
            if (block.first == 0) break
            val entrySite = findFreeSlots(layout, block.count(), block.first)
            if (entrySite.first == 0) continue
            for (i in entrySite) {
                layout[i] = layout[block.first]
            }
            for (i in block) {
                layout[i] = -1
            }
        }
        return calculateChecksum(layout)

    }

    val layout1 = generateDiskLayout("src/Input_Day9.txt")
    val timeTaken1 = measureTime { part1(layout1).println() }
    println("Part1: $timeTaken1")

    val layout2 = generateDiskLayout("src/Input_Day9.txt")
    val timeTaken2 = measureTime { part2(layout2).println() }
    println("Part2: $timeTaken2")

}
