package org.example

import kotlin.math.min


class Ninth : Reader {
    private lateinit var text: String

    fun solve() {
        text = readInput()
        partOne()
        partTwo()
    }

    sealed class Space(var count: Int) {
        class Empty(count: Int) : Space(count)
        class File(count: Int, val initialIndex: Int) : Space(count)
    }

    fun partTwo() {
        val spaces = text.mapIndexed { index, c ->
            if (index % 2 == 0) {
                Space.File(c.digitToInt(), index/2)
            } else {
                Space.Empty(c.digitToInt())
            }
        }.toMutableList()
        for (i in spaces.size - 1 downTo  0) {
            val space = spaces[i]

            if (space is Space.File) {
                val emptyToReplace = spaces.take(i).firstOrNull { it is Space.Empty && it.count >= space.count }
                if (emptyToReplace != null) {
                    (emptyToReplace as Space.Empty).count -= space.count
                    val index = spaces.indexOf(emptyToReplace)
                    spaces.remove(space)
                    spaces.add(index, space)
                    spaces.add(i, Space.Empty(space.count))
                }

            }
        }
        var result: Long = 0
        var files = 0
        spaces.onEach { space ->
            // printFile(space)
            if (space is Space.File) {
                result += calc(files, space.count, space.initialIndex)
            }
            files += space.count
        }
        println()
        println(result)
    }

    private fun printFile(space: Space) {
        if(space is Space.File) {
            print("${space.initialIndex}".repeat(space.count))
        } else {
            print(".".repeat(space.count))
        }
    }

    fun partOne() {
        var i = 0
        var left = 0
        var right = text.length - 1
        if (right % 2 == 1) {
            right--
        }
        var emptyLeft = 0
        var fileLeft = 0
        var result: Long = 0
        while (left < right) {
            //println("$left $right $emptyLeft $fileLeft")

            val leftValue = text[left].digitToInt()
            val rightValue = text[right].digitToInt()
            if (left % 2 == 0) {
                result += calc(i, leftValue, left / 2)
                i += leftValue
                left++
            } else {
                if (leftValue == 0) {
                    left++
                    continue
                } else if (emptyLeft == 0) {
                    emptyLeft = leftValue
                }
                if (rightValue == 0) {
                    right -= 2
                    continue
                } else if (fileLeft == 0) {
                    fileLeft = rightValue
                }
                val filled = min(emptyLeft, fileLeft)
                result += calc(i, filled, right / 2)
                i += filled
                emptyLeft -= filled
                fileLeft -= filled
                if (emptyLeft == 0) {
                    left++
                }
                if (fileLeft == 0) {
                    right -= 2
                }
            }
        }
        result += calc(i, fileLeft, right / 2)
        println(result)
    }

    private fun calc(i: Int, count: Int, value: Int): Long {
        return ((i + count - 1) * (i + count) - i * (i - 1)).toLong() * value / 2
    }

    private fun readInput(): String = readFile(9).readText()
}