package org.example

import java.math.BigInteger
import kotlin.math.min

const val PRICE_A = 3
const val PRICE_B = 1
const val ADDITION: Long = 10000000000000

class Thirteen : Reader {
    private lateinit var list: List<Setup>

    fun solve() {
        list = readInput()
        println("Part one: ${partOne()}")
        println("Part two: ${partTwo()}")
    }

    private fun partOne(): Int {
        return list.mapNotNull { calculatePrice(it) }.sum()
    }

    private fun partTwo(): Long {
        return list.mapNotNull { math(it) }.sumOf { it.toLong() }
    }

    private fun math(setup: Setup): BigInteger? {
        val normPrizeX = setup.prize.x + ADDITION.toBigInteger()
        val normPrizeY = setup.prize.y + ADDITION.toBigInteger()
        if ((setup.b.x * setup.a.y - setup.b.y * setup.a.x) == BigInteger.ZERO) {
            if (normPrizeX % setup.a.x != BigInteger.ZERO || normPrizeY % setup.a.y != BigInteger.ZERO || normPrizeX %
                    setup.b.x
                != BigInteger.ZERO || normPrizeY % setup.b.y != BigInteger.ZERO
            ) {
                return null
            }
            return min((3.toBigInteger() * normPrizeX / setup.a.x).toLong(), (normPrizeX / setup.b.x).toLong()).toBigInteger()
        }
        if ((normPrizeX * setup.a.y - normPrizeY * setup.a.x) % (setup.b.x * setup.a.y - setup.b.y * setup.a.x) != BigInteger.ZERO) {
            return null
        } else {
            val b = (normPrizeX * setup.a.y - normPrizeY * setup.a.x) / (setup.b.x * setup.a.y - setup.b.y * setup.a.x)
            val a = (normPrizeX * setup.b.y - normPrizeY * setup.b.x) / (setup.a.x * setup.b.y - setup.a.y * setup.b.x)
            if (a < BigInteger.ZERO || b < BigInteger.ZERO) {
                return null
            }
            val x = a * setup.a.x + b * setup.b.x
            val y = a * setup.a.y + b * setup.b.y
            if (x != normPrizeX || y != normPrizeY) {
                return null
            }
            return (a * PRICE_A.toBigInteger() + b * (PRICE_B.toBigInteger()))
        }
    }

    private fun calculatePrice(setup: Setup): Int? {
        var minValue = 500
        for (a in 0..100) {
            for (b in 0..100) {
                val coord = Coordinates(setup.a.x * a.toBigInteger() + setup.b.x * b.toBigInteger(), setup.a.y * a.toBigInteger() + setup.b.y * b.toBigInteger())
                // println(coord.toString() + " " + setup.prize)
                if (coord == setup.prize) {
                    minValue = min(minValue, a * PRICE_A + b * PRICE_B)
                }
            }
        }
        return minValue.takeIf { it < 500 }
    }

    private fun readInput(): List<Setup> =
        readFile(13).readLines()
            .filter { it.isNotEmpty() }
            .chunked(3)
            .map { chunk ->
                val a = pattern.find(chunk[0])?.groups!!.drop(1).map { it!!.value.toBigInteger() }
                    .let { Coordinates(it.first(), it.last()) }
                val b = pattern.find(chunk[1])?.groups!!.drop(1).map { it!!.value.toBigInteger() }
                    .let { Coordinates(it.first(), it.last()) }
                val prize = pattern.find(chunk[2])?.groups!!.drop(1).map { it!!.value.toBigInteger() }
                    .let { Coordinates(it.first(), it.last()) }
                Setup(a, b, prize)
            }
}

private val pattern = "^.*?([0-9]+).*?([0-9]+)".toRegex()

data class Setup(val a: Coordinates, val b: Coordinates, val prize: Coordinates)

data class Coordinates(var x: BigInteger, var y: BigInteger)


// a * x_a + b * x_b = BIG_VALUE

// (big_value - bx * b) % a = 0
// big_value % a = (bx * b) % a
// big_value % a = (b % a) * (x_b % a) % a
// for x in 1..a check if big_value % a = (b % a) * (x % a)


// x_a = (BIG_VALUE - b * x_b) / a
// we can find BIG_VALUE % a = left
// now we need all the x_b that (b * x_b) % a = left => (b%a) * (x_b%a) % a = left
// i + ak = x_b
//
// b = x_b *

