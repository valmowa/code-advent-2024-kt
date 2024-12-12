package org.example

import kotlin.math.max
import kotlin.math.sign


class Twelfth : Reader {
    private lateinit var list: List<String>
    private val visited = mutableSetOf<Pair<Int, Int>>()

    private val directions = arrayOf(
        Pair(0, 1),
        Pair(1, 0),
        Pair(0, -1),
        Pair(-1, 0)
    )

    fun solve() {
        list = readInput()
        println(partOne())
        visited.clear()
        println(partTwo())
    }

    private fun partOne(): Long {
        var result = 0L
        list.forEachIndexed { i, row ->
            row.forEachIndexed { j, _ ->
                if (Pair(i, j) !in visited) {
                    val (p, a) = visitOne(i, j)
                    result += p * a
                }
            }
        }
        return result
    }

    private fun partTwo(): Long {
        var result = 0L
        list.forEachIndexed { i, row ->
            row.forEachIndexed { j, _ ->
                if (Pair(i, j) !in visited) {
                    val (p, a) = visitTwo(i, j)
                    result += calcSides(p) * a
                }
            }
        }
        return result
    }

    private fun calcSides(sides: List<Side>): Int {
        var prev: Side = Side.Vertical(-2, -2, 0)
        var verticals = 0
        sides.filterIsInstance<Side.Vertical>()
            .sortedWith(Comparator.comparing(Side::y).thenComparing(Side::sign).thenComparing(Side::x))
            .onEach {
                if(it.y != prev.y || it.x != prev.x + 1 || it.sign != prev.sign) {
                    verticals++
                }
                prev = it
            }

        var horizontals = 0
        prev = Side.Horizontal(-2, -2, 0)
        sides.filterIsInstance<Side.Horizontal>()
            .sortedWith(Comparator.comparing(Side::x).thenComparing(Side::sign).thenComparing(Side::y))
            .onEach {
                if(it.x != prev.x || it.y != prev.y + 1 || it.sign != prev.sign) {
                    horizontals++
                }
                prev = it
            }
        return verticals + horizontals
    }

    sealed class Side(val x: Int, val y: Int, val sign: Int) {
        class Vertical(x: Int, y: Int, sign: Int) : Side(x, y, sign)
        class Horizontal(x: Int, y: Int, sign: Int) : Side(x, y, sign)

        override fun toString(): String {
            return "$x $y $sign"
        }
    }

    private fun visitTwo(i: Int, j: Int): Pair<MutableList<Side>, Int> {
        var perimeter = mutableListOf<Side>()
        var area = 1
        visited.add(Pair(i, j))
        directions.forEach { (dx, dy) ->
            val x = i + dx
            val y = j + dy
            if (x in list.indices && y in list[0].indices && list[x][y] == list[i][j]) {
                if( Pair(x, y) !in visited) {
                    val (p, a) = visitTwo(x, y)
                    area += a
                    perimeter.addAll(p)
                }
            } else {
                if (dx == 0) {
                    perimeter.add(Side.Vertical(x, j, dy.sign))
                } else {
                    perimeter.add(Side.Horizontal(i, y, dx.sign))
                }
            }
        }
        return Pair(perimeter, area)
    }

    private fun visitOne(i: Int, j: Int): Pair<Int, Int> {
        var perimeter = 0
        var area = 1
        visited.add(Pair(i, j))
        directions.forEach { (dx, dy) ->
            val x = i + dx
            val y = j + dy
            if (x in list.indices && y in list[0].indices && list[x][y] == list[i][j]) {
                if( Pair(x, y) !in visited) {
                    val (p, a) = visitOne(x, y)
                    area += a
                    perimeter += p
                }
            } else {
                perimeter++
            }
        }
        return Pair(perimeter, area)
    }


    private fun readInput(): List<String> =
        readFile(12).readLines()
}