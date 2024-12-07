package org.example


class Seventh : Reader {
    private lateinit var lines: List<String>

    fun solve() {
        val file = readFile(7)
        lines = file.readLines()
        firstPart()
        secondPart()

    }

    private fun firstPart() {
        val result = lines.map { parseLine(it) }.map { (target, elements) ->
            val result = matches(target, 0, 0, elements)
            result to target
        }.filter { it.first }
            .sumOf { it.second }
        println(result)
    }

    private fun secondPart() {
        val result = lines.map { parseLine(it) }.map { (target, elements) ->
            val result = matchesTwo(target, 0, 0, elements)
            result to target
        }.filter { it.first }
            .sumOf { it.second }
        println(result)
    }

    private fun matchesTwo(target: Long, currentValue: Long, index: Int, elements: LongArray): Boolean {
        return if (index == elements.size) {
            target == currentValue
        } else {
            matchesTwo(target, currentValue + elements[index], index + 1, elements) || matchesTwo(
                target,
                multiply(currentValue, elements[index]),
                index + 1,
                elements
            ) || matchesTwo(
                target,
                concat(currentValue, elements[index]),
                index + 1,
                elements
            )
        }
    }

    private fun matches(target: Long, currentValue: Long, index: Int, elements: LongArray): Boolean {
        return if (index == elements.size) {
            target == currentValue
        } else {
            matches(target, currentValue + elements[index], index + 1, elements) || matches(
                target,
                currentValue * elements[index],
                index + 1,
                elements
            )
        }
    }

    private fun parseLine(line: String): Pair<Long, LongArray> {
        val split = line.split(":")
        val target = split[0].toLong()
        val elements = split[1].trimIndent().split(" ").map { it.toLong() }.toLongArray()
        return Pair(target, elements)
    }

    private fun concat(first: Long, second: Long): Long {
        return "$first$second".toLong()
    }

    private fun multiply(first: Long, second: Long): Long {
        if(first == 0L){
            return second
        }
        return first * second
    }
}