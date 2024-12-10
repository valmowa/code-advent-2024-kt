package org.example


class Tenth : Reader {
    private lateinit var map: Array<IntArray>

    private val directions = arrayOf(
        Pair(0, 1),
        Pair(1, 0),
        Pair(0, -1),
        Pair(-1, 0)
    )

    fun solve() {
        map = readInput()
        println(partOne())
        println(partTwo())
    }

    private fun partTwo(): Int {
        var result = 0
        map.onEachIndexed { x, row ->
            row.onEachIndexed { y, _ ->
                if (map[x][y] == 0) {
                    result += calcScore(x, y, 0)
                }
            }
        }
        return result
    }

    private fun partOne(): Int {
        var result = 0
        map.onEachIndexed { x, row ->
            row.onEachIndexed { y, _ ->
                if (map[x][y] == 0) {
                    val visited = mutableSetOf<Pair<Int, Int>>()
                    visit(x, y, 0, visited)
                    result += visited.size
                }
            }
        }
        return result
    }

    private fun calcScore(x: Int, y: Int, currentNumber: Int): Int {
        if (currentNumber == 9) {
            return 1
        } else {
            return directions.sumOf { (dx, dy) ->
                val newX = x + dx
                val newY = y + dy
                if (newX in map.indices && newY in map[0].indices && map[newX][newY] == currentNumber + 1) {
                    calcScore(newX, newY, currentNumber + 1)
                } else {
                    0
                }
            }
        }
    }

    private fun visit(x: Int, y: Int, currentNumber: Int, visited: MutableSet<Pair<Int, Int>> = mutableSetOf()) {
        if (currentNumber == 9) {
            visited.add(Pair(x, y))
        } else {
            directions.onEach { (dx, dy) ->
                val newX = x + dx
                val newY = y + dy
                if (newX in map.indices && newY in map[0].indices && map[newX][newY] == currentNumber + 1) {
                    visit(newX, newY, currentNumber + 1, visited)
                }
            }
        }
    }


    private fun readInput(): Array<IntArray> =
        readFile(10).readLines()
            .map { line -> line.toCharArray().map { it.digitToIntOrNull() ?: -1 }.toIntArray() }
            .toTypedArray()
}