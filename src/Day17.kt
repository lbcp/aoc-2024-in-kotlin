import java.io.File
import kotlin.math.pow

class FluxCompensator(private var regA: Long, private var regB: Long, private var regC: Long) {
    private var pointer = 0
    private var outString = mutableListOf<String>()
    private var jumpCount = mutableMapOf<Int, Int>()
    private fun translateCombo(operand: Long): Long {
        var value = 0L
        when (operand) {
            in 1..3 -> value = operand
            4L -> value = regA
            5L -> value = regB
            6L -> value = regC
        }
        return value
    }

    private fun adv(comboOperand: Long){
        // Numerator is Register A
        // Divisor is combo Operand
        val numerator = translateCombo(comboOperand)
        regA = (regA / 2.0.pow(numerator.toInt())).toLong()
        pointer += 2
    }

    private fun bxl(operand: Long) {
        //Operand is literal operand
        //val binRegB = regB.toUInt().toString(radix = 2)
        //val binOp = operand.toUInt().toString(radix = 2)
        //val xorCombination = binRegB xor binOp
        regB = regB.xor(operand)
        pointer += 2
    }

    private fun bst(comboOperand: Long) {
        val value = translateCombo(comboOperand)
        regB = value % 8
        pointer += 2
    }

    private fun jnz(operand: Long): Boolean {
        // The jump instruction is the only critical one for a brute force approach as it can result in a loop
        if (regA != 0L) {
            this.pointer = operand.toInt()
            if (jumpCount.containsKey(this.pointer)) jumpCount[this.pointer] = jumpCount[this.pointer]!! + 1
            else jumpCount[this.pointer] = 1
        }
        else this.pointer *= 2

        if (jumpCount.keys.any { it > 1000 }) {
            println("loop detected")
            return true
        }
        return false
    }

    private fun bxc(operand: Long) {
        // operand does nothing on purpose
        regB = regB.xor(regC)
        pointer += 2
    }

    private fun out(comboOperand: Long): String {
        // Check for comboStuff
        val value: Long = translateCombo(comboOperand)
        outString.add((value % 8).toString())
        pointer += 2
        return outString.joinToString(separator = ",")
    }

    private fun bdv(comboOperand: Long){
        // Numerator is Register A
        // Divisor is combo Operand
        val numerator = translateCombo(comboOperand)
        regB = (regA / 2.0.pow(numerator.toInt())).toLong()
        pointer += 2
    }

    private fun cdv(comboOperand: Long){
        // Numerator is Register A
        // Divisor is combo Operand
        val numerator = translateCombo(comboOperand)
        regC = (regA / 2.0.pow(numerator.toInt())).toLong()
        pointer += 2
    }

    fun runProgram(program: MutableList<Int>, part2: Boolean = false): String {
        var output = ""
        while(pointer < program.size - 1) {
            var mustBreak = false
            val op = program[pointer + 1].toLong()
            when (program[pointer]) {
                0 -> adv(op)
                1 -> bxl(op)
                2 -> bst(op)
                3 -> mustBreak = jnz(op)
                4 -> bxc(op)
                5 -> output = out(op)
                6 -> bdv(op)
                7 -> cdv(op)
            }
            if (part2) {
                if (mustBreak || output.length > program.size * 2) break
            }
        }
        return output
    }
}


fun main() {
    fun readInput(filename:String): Pair<MutableList<Long>, MutableList<Int>> {
        val registers = mutableListOf<Long>()
        val instructions = mutableListOf<Int>()
        File(filename).useLines { lines ->
            lines.forEach {
                if ("Register" in it) {
                    val temp = it.split(": ")[1].toLong()
                    registers.add(temp)
                }
                else if ("Program" in it) {
                    val temp = it.split(": ")[1].split(",").map { num -> num.toInt() }
                    instructions.addAll(temp)
                }
            }
        }
        return Pair(registers, instructions)
    }

    fun part1(registers: MutableList<Long>, instructions: MutableList<Int>): String {
        val device = FluxCompensator(registers[0], registers[1], registers[2])
        val result = device.runProgram(instructions)
        return result
    }

    fun part2(registers: MutableList<Long>, instructions: MutableList<Int>): String {
        // As per usual, brute force doesn't work
        // I probably have to do some recursive backtracking, starting with the last positions.
        // regA is only modified once with regA = regA / 2^^8
        // This means, in my final step, A must be smaller than 8.
        // And regB % 8 defines the output.

        for (i in 20_000_000_000..30_000_000_000) {
            val device = FluxCompensator(i, registers[1], registers[2])
            val result = device.runProgram(instructions, true)
            if (result == instructions.joinToString (separator = ",")) return result
        }
        return ""
    }

    // Test if implementation meets criteria from the description, like:
    //check(part1(listOf("test_input")) == 1)

    // Or read a large test input from the `src/Day01_test.txt` file:
    val (testReg, testInstructions) = readInput("src/TestInput_Day17.txt")
    //check(part1(testReg) == 1)
    check(part1(testReg, testInstructions) == "4,6,3,5,6,3,5,2,1,0")

    // Read the input from the `src/Day01.txt` file.
    val (registers, program) = readInput("src/Input_Day17.txt")
    part1(registers, program).println()
    part2(registers, program).println()
//part1(input).println()
    //part2(input).println()
}
