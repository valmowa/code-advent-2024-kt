package org.example

class Sixth : Reader {

    data class Guard(val i : Int, val j: Int, val direction: Char)

    private val directions = mapOf(
        '^' to Pair(-1, 0),
        'v' to Pair(1, 0),
        '<' to Pair(0, -1),
        '>' to Pair(0, 1),
    )

    val moves = mutableSetOf<Pair<Int, Int>>()
    val movesWithCycleCheck = mutableSetOf<Guard>()

    fun solve() {
        val file = readFile(6)
        val lines = file.readLines()
        val (i, j, direction) = findInitial(lines)
        //move(i, j, direction, lines)
        println(moves.size - 1)
        println(solveSecond())
    }

    private fun solveSecond() {
        val file = readFile(6)
        val lines = file.readLines().toMutableList()
        val (startI, startJ, direction) = findInitial(lines)
        var result = 0
        for(i in lines.indices){
            for(j in lines[i].indices){
                if(lines[i][j] == '.'){
                    val oldLine = lines[i]
                    lines[i] = String(lines[i].toCharArray().apply {
                        this[j] = '#'
                    })
                    if(moveWithCycleCheck(Guard(startI, startJ, direction), lines)){
                        result++
                    }
                    movesWithCycleCheck.clear()
                    lines[i] = oldLine
                }
            }
        }
        println(result)
    }

    private fun findInitial(lines: List<String>): Triple<Int, Int, Char> {
        for (i in lines.indices) {
            for (j in lines[i].indices) {
                if (lines[i][j] in directions.keys) {
                    return Triple(i, j, lines[i][j])
                }
            }
        }
        throw IllegalStateException("No initial found")
    }

    private fun moveWithCycleCheck(g: Guard, lines: List<String>): Boolean {
        var guard = g
        for(i in 1..100000) {
            if (movesWithCycleCheck.contains(guard)) {
                return true
            }
            movesWithCycleCheck.add(guard)
            if (guard.i !in lines.indices || guard.j !in lines[guard.i].indices) {
                return false
            }
            guard = move(guard, lines)
        }
        return false
    }

    private fun move(guard: Guard, lines: List<String>): Guard {
        val (i,j, direction) = guard
        var newDirection = direction
        var newI = i + directions[newDirection]!!.first
        var newJ = j + directions[newDirection]!!.second
        while (newI in lines.indices && newJ in lines[i].indices && lines[newI][newJ] == '#') {
            newDirection = rotate(newDirection)
            newI = i + directions[newDirection]!!.first
            newJ = j + directions[newDirection]!!.second
        }
        return Guard(newI, newJ, newDirection)
    }

    private fun rotate(char: Char): Char = when (char) {
        '^' -> '>'
        '>' -> 'v'
        'v' -> '<'
        '<' -> '^'
        else -> throw IllegalStateException("Unknown direction")
    }
}