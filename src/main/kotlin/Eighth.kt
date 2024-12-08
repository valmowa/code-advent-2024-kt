package org.example

import kotlin.math.abs


class Eighth : Reader {
    private lateinit var lines: List<String>
    private val freqs = mutableMapOf<Char, List<Pair<Int, Int>>>()
    private val nodesPartOne = mutableSetOf<Pair<Int, Int>>()
    private val nodesPartTwo = mutableSetOf<Pair<Int, Int>>()

    fun solve() {
        val file = readFile(8)
        lines = file.readLines()
        parseMap()
        partOne()
        partTwo()

    }

    private fun partTwo() {
        freqs.keys.onEach {
            val sameFreqs = freqs[it]!!
            for(i in sameFreqs.indices) {
                for(j in i..<sameFreqs.size) {
                    var (x1, y1) = sameFreqs[i]
                    var (x2, y2) = sameFreqs[j]
                    if (x1 == x2 && y1 == y2) {
                        continue
                    }
                    var dx = x1 - x2
                    var dy = y1 - y2
                    val gcd = gcd(abs(dx), abs(dy))
                    dx /= gcd
                    dy /= gcd
                    while(x2 in lines.indices && y2 in lines[0].indices) {
                        nodesPartTwo.add(Pair(x2, y2))
                        x2 -= dx
                        y2 -= dy
                    }
                    x2 += dx
                    y2 += dy
                    while(x2 in lines.indices && y2 in lines[0].indices) {
                        nodesPartTwo.add(Pair(x2, y2))
                        x2 += dx
                        y2 += dy
                    }
                    // println("($x1, $y1) -> ($x2, $y2) = $dx, $dy; new ones: ($x1 + $dx, $y1 + $dy) and ($x2 - $dx, $y2 - $dy)")
               }
            }
        }
        println(nodesPartTwo.size)
    }

    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    private fun partOne() {
        freqs.keys.onEach {
            val sameFreqs = freqs[it]!!
            for(i in sameFreqs.indices) {
                for(j in i..<sameFreqs.size) {
                    val (x1, y1) = sameFreqs[i]
                    val (x2, y2) = sameFreqs[j]
                    if (x1 == x2 && y1 == y2) {
                        continue
                    }
                    val dx = x1 - x2
                    val dy = y1 - y2
                    // println("($x1, $y1) -> ($x2, $y2) = $dx, $dy; new ones: ($x1 + $dx, $y1 + $dy) and ($x2 - $dx, $y2 - $dy)")
                    if(x2 - dx in lines.indices && y2 - dy in lines[0].indices) {
                        nodesPartOne.add(Pair(x2 - dx, y2 - dy))
                    }
                    if(x1 + dx in lines.indices && y1 + dy in lines[0].indices) {
                        nodesPartOne.add(Pair(x1 + dx, y1 + dy))
                    }

                }
            }
        }
        println(nodesPartOne.size)
    }

    private fun parseMap() {
        lines.forEachIndexed { i, line ->
            line.forEachIndexed { j, c ->
                if (c != '.') {
                    freqs[c] = freqs.getOrDefault(c, emptyList()) + Pair(i, j)
                }
            }
        }
    }
}