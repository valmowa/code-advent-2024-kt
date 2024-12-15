package org.example

import java.io.File
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.random.Random

var iterations = 0
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    //


    // println("first: " + solve1("src/main/resources/1/1.txt"))
    // println("second: " + solve2("src/main/resources/1/1.txt"))
/*    println(solve3("src/main/resources/1/2.txt").toString() + " " + iterations)
   println(solve3WithDp("src/main/resources/1/2.txt").toString() + " " + dpIterations)
    test()*/
    //Third().resolve()
    //Fourth().resolve()
    // Fifth().solve()
    //Sixth().solve()
    //Seventh().solve()
    // Eighth().solve()
    // Ninth().solve()
    // Tenth().solve()
    // Eleventh().solve()
    // Twelfth().solve()
    // Thirteen().solve()
    Fourteen().solve()

}

fun solve3WithDp(fileName: String): Long {
    var res: Long = 0
    File(fileName).readLines().map {
        val reports = it.split(" ").map { it.toInt() }
        val solver = DPSolver(reports)
        if(solver.check(DPSolver.Key(1, 0, 1, reports[0])) > 0){
            res++
        } else if(solver.check(DPSolver.Key(2, 0, 0, reports[1])) > 0){
            res++
        } else {
            // nothing
        }

    }
    return res
}

fun genReports() = (1..10000).toList()

fun solve3(fileName: String): Long {
    var res: Long = 0
    File(fileName).readLines().map {
        val reports = it.split(" ").map { it.toInt() }
        var minErrors = min(reports.errorsForDirection(1, -1), reports.errorsForDirection(-1, -1))
        for(i in reports.indices){
           minErrors = min(minErrors, reports.errorsForDirection(1, i))
            minErrors = min(minErrors, reports.errorsForDirection(-1, i))
        }
        if(minErrors < 1) {
            res++;
        }
    }
    return res
}
//


fun List<Int>.errorsForDirection(direction: Int, skip: Int): Int {
    var errors = 0
    val reports = this
    iterations++
    for (i in 0 until reports.size - 1) {
        var add = 1
        if(i == skip){
            continue
        }
        if(i + 1 == skip){
            add = 2
        }
        if(i + add == reports.size) {
            return errors
        }
        if (reports[i] > reports[i + add] && direction == 1) {
            errors++;
        }
        if (reports[i] < reports[i + add] && direction == -1) {
            errors++;
        }
        val diff = reports[i] - reports[i + add]
        if(abs(diff) < 1 || abs(diff) > 3){
            errors++;
        }
    }
    return errors
}


fun solve1(fileName: String): Long {
    var res: Long = 0
    val firstList = mutableListOf<Int>()
    val secondList = mutableListOf<Int>()
    File(fileName).readLines().map {
        val (a, b) = it.split("   ").map { it.toInt() }
        firstList.add(a)
        secondList.add(b)
    }
    firstList.sort()
    secondList.sort()
    for (i in 0 until firstList.size) {
        res += (firstList[i] - secondList[i]).absoluteValue
    }
    return res
}

fun solve2(fileName: String): Long {
    var res: Long = 0
    val firstList = mutableListOf<Int>()
    val secondMap = mutableMapOf<Int, Int>()
    File(fileName).readLines().map {
        val (a, b) = it.split("   ").map { it.toInt() }
        firstList.add(a)
        val prevValue = secondMap.getOrDefault(b, 0)
        secondMap[b] = prevValue + 1
    }
    firstList.onEach {
        res += it * (secondMap[it] ?: 0)
    }
    return res
}

fun solve3WithMemo(fileName: String): Long {
    var res: Long = 0
    File(fileName).readLines().map {
        val reports = it.split(" ").map { it.toInt() }
        val solver = DPSolver(reports)
        if(solver.check(DPSolver.Key(1, 0, 1, reports[0])) > 0){
            res++
        } else if(solver.check(DPSolver.Key(2, 0, 0, reports[1])) > 0){
            res++
        } else {
            // nothing
        }

    }
    return res
}

var dpIterations = 0

fun test() {
    val reports = (1..1000).map { Random.nextInt() }
    val solver = DPSolver(reports)
    val res = solver.check(DPSolver.Key(1, 0, 1, reports[0])) + solver.check(DPSolver.Key(2, 0, 0, reports[1]))
    println(res > 0)
    println(dpIterations)

}

class DPSolver(private val reports: List<Int>) {
    data class Key(val index: Int, val direction: Int, val skips: Int, val prevEl: Int)

    private val memo: MutableMap<Key, Int> = mutableMapOf()

    fun check(key: Key): Int {
        dpIterations++
        var result = 0
        if (key.index == reports.size) {
            memo[key] = 1
            return 1
        }
        if (memo[key] != null) {
            return memo[key]!!
        }
        val newDirection = getDirectionIfValidOrZero(key.prevEl, reports[key.index], key.direction)
        if (newDirection != 0) { // zero -> invalid with current element
            result += check(Key(key.index + 1, newDirection, key.skips, reports[key.index]))
        }
        if(key.skips > 0){ // no more skips
            result += check(Key(key.index + 1, key.direction, key.skips - 1, key.prevEl))
        }
        result = if(result > 0) 1 else 0
        memo[key] = result
        return result
    }

    private fun getDirectionIfValidOrZero(v1: Int, v2: Int, direction: Int): Int {
        val diff = abs(v1 - v2)
        return when {
            diff < 1 || diff > 3 -> 0
            v1 > v2 && direction <= 0 -> -1
            v1 < v2 && direction >= 0 -> 1
            else -> 0
        }
    }
}