import java.io.File

fun main() {
    fun part1(input: List<List<Int>>): Int {
        fun checkAscending(report: List<Int>): Int {
            // Returns 1 if it is safe; else returns 0
            for (i in 0 until report.size-1) {
                if (report[i+1] - report[i] !in 1..3) return 0
            }
            return 1
        }

        fun checkDescending(report: List<Int>): Int {
            for (i in 0 until report.size-1) {
                if (report[i] - report[i+1] !in 1..3) return 0
            }
            return 1
        }

        var result = 0
        for (report in input) {
            result += checkAscending(report)
            result += checkDescending(report)
        }
        return result
    }

    fun part2(input: List<List<Int>>): Int {
        fun checkAscending(report: List<Int>, inner: Boolean = false): Int {
            for (i in 0 until report.size-1) {
                if (report[i+1] - report[i] !in 1..3) {
                    return if (inner) 0
                    else {
                        // I know that can be much prettier...
                        val dampenedReportRight = report.toMutableList()
                        dampenedReportRight.removeAt(i+1)
                        val dampenedReportLeft = report.toMutableList()
                        dampenedReportLeft.removeAt(i)
                        if (checkAscending(dampenedReportRight.toList(), true) == 1 ||
                            checkAscending(dampenedReportLeft.toList(), true) == 1) return 1
                        else 0
                    }
                }
            }
            return 1
        }

        fun checkDescending(report: List<Int>, inner: Boolean = false): Int {
            for (i in 0 until report.size-1) {
                if (report[i] - report[i+1] !in 1..3) {
                    return if (inner) 0
                    else {
                        val dampenedReportRight = report.toMutableList()
                        dampenedReportRight.removeAt(i+1)
                        val dampenedReportLeft = report.toMutableList()
                        dampenedReportLeft.removeAt(i)
                        if (checkDescending(dampenedReportRight.toList(), true) == 1 ||
                            checkDescending(dampenedReportLeft.toList(), true) == 1) return 1
                        else 0
                    }
                }
            }
            return 1
        }

        var result = 0
        for (report in input) {
            if (checkAscending(report) == 0 && checkDescending(report) == 0) println(report)
            result += checkAscending(report)
            result += checkDescending(report)
        }
        return result
    }

    val input = mutableListOf<List<Int>>()
    File("src/Input_Day2.txt").useLines { lines ->
        lines.forEach {
            input.add(
                it.split(" ")
                .map { item -> item.toInt() })
        }
    }

    part1(input).println()
    part2(input).println()
}
