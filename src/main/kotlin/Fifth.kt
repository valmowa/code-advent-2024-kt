package org.example

class Fifth: Reader {
    lateinit var rules: Set<Pair<Int, Int>>

    fun solve() {
        val file = readFile(5)
        val lines = file.readLines()
        parseRules(lines.takeWhile { it != "" })
        val pages = lines.dropWhile { it == "" || it.contains('|') }
        var result = 0
        pages.onEach { page ->
            val formatted = page.split(',').map { it.toInt() }
            if(!validatePage(formatted)) {
                result += findMiddle(formatted)
                // println(page + " " + formatted[formatted.size/2])
            }
        }
        println(result)
    }

    private fun findMiddle(page: List<Int>): Int {
        val counter = IntArray(page.size)
        for(i in page.indices) {
            for(j in i + 1..<page.size) {
                if(rules.contains(Pair(page[j], page[i]))) {
                    counter[i]++
                    counter[j]--
                } else {
                    counter[j]++
                    counter[i]--
                }
            }
        }
        for(i in counter.indices) {
            if(counter[i] == 0) {
                return page[i]
            }
        }
        throw IllegalStateException("No middle found")
    }

    private fun validatePage(page: List<Int>): Boolean {
        for(i in page.indices) {
            for(j in i + 1..<page.size) {
                if(rules.contains(Pair(page[j], page[i]))) {
                    return false
                }
            }
        }
        return true
    }


    private fun parseRules(lines: List<String>) {
        rules = lines.map { it.split('|') }
            .map { it[0].toInt() to it[1].toInt() }
            .toSet()
    }
}