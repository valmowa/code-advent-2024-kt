package org.example

private val height = 101
private val width = 103
private val quadrant_height = height / 2
private val quadrant_width = width / 2

class Fourteen : Reader {
    private lateinit var list: List<Robot>

    fun solve() {
        list = readInput()
        list.onEach { println(it.posAfterSeconds(100)) }
        println("Part one: ${partOne()}")
        println("Part two: ${partTwo()}")
    }

    private fun partOne(): Int {
        return list.mapNotNull { it.quadrantAfterSeconds(100) }
            .groupBy { it }
            .map { it.value.size.also { println(it) } }
            .reduce { acc, i -> acc * i }
    }

    private fun partTwo() {
        (300..350).onEach {
            println(it)
            list.map { robot -> robot.posAfterSeconds(1) }.also { print(getGrid(it)) }
        }
    }

    fun print(grid : List<CharArray>) {
        grid.forEach {
            println(String(it))
        }
    }

    private fun getGrid(l: List<Robot>): List<CharArray> {
        val grid = List(height) { CharArray(width) { '.' } }
        l.forEach {
            grid[it.x][it.y] = '#'
        }
        return grid
    }

    enum class Quadrant {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

        companion object {
            fun from(x: Int, y: Int): Quadrant {
                return when {
                    x <= quadrant_height && y <= quadrant_width -> TOP_LEFT
                    x <= quadrant_height && y > quadrant_width -> TOP_RIGHT
                    x > quadrant_height && y <= quadrant_width -> BOTTOM_LEFT
                    x > quadrant_height && y > quadrant_width -> BOTTOM_RIGHT
                    else -> throw IllegalArgumentException("Invalid quadrant")
                }
            }
        }
    }

    data class Robot(val x: Int, val y: Int, val v_x: Int, val v_y: Int) {
        fun posAfterSeconds(seconds: Int): Robot {
            var new_x = (x + v_x * seconds) % height
            if(new_x < 0) {
                new_x += height
            }
            var new_y = (y + v_y * seconds) % width
            if(new_y < 0) {
                new_y += width
            }
            return Robot(new_x, new_y, v_x, v_y)
        }

        fun quadrantAfterSeconds(seconds: Int): Quadrant? {
            val newRobot = posAfterSeconds(seconds)
            if (newRobot.x == quadrant_height || newRobot.y == quadrant_width) {
                return null
            }
            return Quadrant.from(newRobot.x, newRobot.y)
        }
    }

    private val pattern = "(-?\\d+)".toRegex()

    private fun readInput(): List<Robot> =
        readFile(14).readLines()
            .map { pattern.findAll(it).map { it.value.toInt() }.toList() }
            .map { Robot(it[0], it[1], it[2], it[3]) }
}

