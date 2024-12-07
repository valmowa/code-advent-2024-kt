package org.example


class Fourth : Reader {
    val target = "XMAS"
    val mas = charArrayOf('M', 'A', 'S')
    val sam = charArrayOf('S', 'A', 'M')
    val memo = mutableMapOf<Triple<Int, Int, Int>, Int>()
    val directions = arrayOf(1 to 0, -1 to 0, 0 to -1, 0 to 1, -1 to -1, -1 to 1, 1 to -1, 1 to 1)

    fun resolve() {
        val file = readFile(4)
        val lines = file.readLines()
        var globalResult = 0
        for (i in lines.indices) {
            val line = lines[i]
            for (j in line.indices) {
                if(checkForXMAS(lines, i, j)){
                    globalResult++
                }
            }
        }
        println(globalResult)
    }

    private fun checkForXMAS(lines: List<String>, i: Int, j: Int): Boolean {
        if(i + 2 >= lines.size || j + 2 >= lines[i].length) {
            return false
        }
        val firstDiag = charArrayOf(lines[i][j], lines[i + 1][j + 1], lines[i + 2][j + 2])
        val secondDiag = charArrayOf(lines[i + 2][j], lines[i + 1][j + 1], lines[i][j + 2])
        return (firstDiag.contentEquals(mas) || firstDiag.contentEquals(sam)) &&
                (secondDiag.contentEquals(mas) || secondDiag.contentEquals(sam))
    }

    private fun checkWithDirection(
        expectedCharIndex: Int,
        lines: List<String>,
        i: Int,
        j: Int,
        direction: Pair<Int, Int>
    ): Int {
        val d = direction
        if (i !in lines.indices || j !in lines[i].indices) {
            return 0
        }
        if (lines[i][j] != target[expectedCharIndex]) {
            return 0
        }
        if (expectedCharIndex == 3) {
            return 1
        }
        val result = checkWithDirection(expectedCharIndex + 1, lines, i + d.first, j + d.second, d)
        return result
    }

    private fun check(expectedCharIndex: Int, lines: List<String>, i: Int, j: Int): Int {
        val key = Triple(i, j, expectedCharIndex)
        if (memo[key] != null) return memo[key]!!
        if (i !in lines.indices || j !in lines[i].indices) {
            memo[key] = 0
            return 0
        }
        if (lines[i][j] != target[expectedCharIndex]) {
            memo[key] = 0
            return 0
        }
        if (expectedCharIndex == 3) {
            memo[key] = 1
            return 1
        }
        var result = 0
        for (d in directions) {
            result += check(expectedCharIndex + 1, lines, i + d.first, j + d.second)
        }
        memo[key] = result
        return result
    }
}