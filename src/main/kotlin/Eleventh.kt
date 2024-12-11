package org.example


class Eleventh : Reader {
    private lateinit var list: MutableList<Long>

    private val directions = arrayOf(
        Pair(0, 1),
        Pair(1, 0),
        Pair(0, -1),
        Pair(-1, 0)
    )

    fun solve() {
        list = readInput()
        //println(partOne())
        println(partTwo())
    }

    fun partOne(): Int {
        for(i in 1..25) {
            val newList = mutableListOf<Long>()
            for(j in list.indices) {
                when{
                    list[j] == 0L -> newList.add(1)
                    numOfDigits(list[j]) % 2 == 0 -> {
                        val (first, second) = splitStone(list[j])
                        newList.add(first)
                        newList.add(second)
                    }
                    else -> newList.add(list[j] * 2024)
                }
            }
            list = newList
        }
        return list.size
    }

    private val memo = mutableMapOf<Pair<Long, Int>, Long>()

    fun partTwo(): Long {
        return list.sumOf { calcForNum(it, 75) }
    }

    private fun calcForNum(num: Long, count: Int): Long {
        if(count == 0) return 1
        if(memo[num to count] != null) return memo[num to count]!!
        val res = when {
            num == 0L -> calcForNum(1L, count - 1)
            numOfDigits(num) % 2 == 0 -> {
                val (first, second) = splitStone(num)
                calcForNum(first, count - 1) + calcForNum(second, count - 1)
            }
            else -> calcForNum(num * 2024, count - 1)
        }
        memo[num to count] = res
        return res
    }

    private fun splitStone(i: Long): Pair<Long, Long> {
        val str = i.toString()
        val first = str.take(str.length / 2)
        val second = str.takeLast(str.length / 2)
        return first.toLong() to second.toLong()
    }

    private fun numOfDigits(num: Long): Int {
        var count = 0
        var n = num
        while (n != 0L) {
            n /= 10
            ++count
        }
        return count
    }




    private fun readInput(): MutableList<Long> =
        readFile(11).readText().split(" ").map { it.toLong() }.toMutableList()
}