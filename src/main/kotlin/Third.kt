package org.example

import java.io.File



class Third : Reader {
    var globalLastDont: Int? = null
    var globalLastDo: Int? = null

    fun resolve() {
        val file = readFile()
        val lines = file.readLines()
        val result = lines.sumOf { processLine(it) }
        println(result)
    }

    private fun processLine(line: String): Int {
        val instructionPattern = "mul\\(\\d+,\\d+\\)".toRegex()
        val doPattern = "do\\(\\)".toRegex()
        val dontPattern = "don't\\(\\)".toRegex()
        val dos = doPattern.findAll(line)
        val donts = dontPattern.findAll(line)

        val instructions = instructionPattern.findAll(line)
        var prevDont: Int? = null
        val result = instructions
            .filter { inst ->
                val instructionStart = inst.range.first
                val lastDo = dos.lastOccurrenceBeforeIndex(instructionStart)
                val lastDont = donts.lastOccurrenceBeforeIndex(instructionStart)
                globalLastDont = lastDont ?: globalLastDont
                globalLastDo = lastDo ?: globalLastDo
                prevDont = lastDont
                val start = lastDo == null && globalLastDo == null && lastDont == null && globalLastDont == null
                val newLineWithLastDo = lastDo == null && globalLastDo != null && lastDont == null && (globalLastDont == null || globalLastDont!! < globalLastDo!!)
                val currentLineWithDo = lastDo != null && lastDo > (lastDont ?: 0)
                start || newLineWithLastDo || currentLineWithDo
            }
            .sumOf { processInstruction(it.value) }
        return result
    }

    private fun Sequence<MatchResult>.lastOccurrenceBeforeIndex(index: Int): Int? {
        return this.lastOrNull { it.range.last < index }?.range?.endInclusive
    }

    private fun processInstruction(instruction: String): Int {
        return instruction.drop(4)
            .dropLast(1)
            .split(',')
            .map { it.toInt() }
            .reduce { x, y -> x * y }
    }
}

interface Reader {
    fun readFile(number: Int = 3): File = File("src/main/resources/1/$number.txt")
}